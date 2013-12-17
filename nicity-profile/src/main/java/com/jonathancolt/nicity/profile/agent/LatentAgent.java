package com.jonathancolt.nicity.profile.agent;

/*
 * #%L
 * nicity-profile
 * %%
 * Copyright (C) 2013 Jonathan Colt
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * -javaagent:<path to your profiler project directory>/target/LatentProfiler-1.0-SNAPSHOT.jar=interfacePackagePrefix
 *
 * @author jonathan
 */
public class LatentAgent implements ClassFileTransformer {

    private String[] includePackages;
    private String[] excludePackages;

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        String[] host_clusterName_serviceName_version_AndPackages = agentArgs.split(":");
        String includePackages = (host_clusterName_serviceName_version_AndPackages.length > 4) ? host_clusterName_serviceName_version_AndPackages[4] : "";
        String excludePackages = (host_clusterName_serviceName_version_AndPackages.length > 5) ? host_clusterName_serviceName_version_AndPackages[5] : "";

        new LatentAgent(includePackages,excludePackages, instrumentation);
        new LatentHttpPump(host_clusterName_serviceName_version_AndPackages[0],
                host_clusterName_serviceName_version_AndPackages[1],
                host_clusterName_serviceName_version_AndPackages[2],
                host_clusterName_serviceName_version_AndPackages[3]).start();

    }

    public LatentAgent(String includePackages, String excludePackages, Instrumentation instrumentation) {
        instrumentation.addTransformer(this);
        this.includePackages = includePackages.split(",");
        this.excludePackages = excludePackages.split(",");
    }

    @Override
    public byte[] transform(ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer)
            throws IllegalClassFormatException {
        if (className.contains("com.jonathancolt.nicity.profile")) { // keep from instrumenting ourself
            return classfileBuffer;
        }


        try {
            String javassistClassName = className.replace('/', '.');// first modify the class name for javassist. convert slashes to dots:
            ClassPool cp = ClassPool.getDefault();
            cp.insertClassPath(new ByteArrayClassPath(javassistClassName, classfileBuffer));
            CtClass cc = cp.get(javassistClassName);
            if (cc.isInterface()) {
                return classfileBuffer;
            }
            if (cc.isFrozen()) {
                cc.defrost();
            }
            if (cc.isFrozen()) {
                return classfileBuffer;
            }

            CtClass[] interfaces = cc.getInterfaces();
            if (interfaces != null && interfaces.length > 0) {
                if (!isDevelopControlledInterface(interfaces)) {
                    return classfileBuffer;
                }

                CtMethod[] methods = cc.getMethods();
                instrumentClass(cc);
                for (int k = 0; k < methods.length; k++) { // do not instrument inherited methods:
                    CtClass interfaceClass = isInterfaceMethod(interfaces, methods[k]);
                    if (interfaceClass != null) {
                        instrumentMethod(interfaceClass.getName(), methods, k, cc);
                    }
                }
            }
            return cc.toBytecode();

        } catch (NotFoundException | CannotCompileException | IOException exc) {
            System.err.println(exc.getClass().getName() + ": " + exc.getMessage());
            exc.printStackTrace();
            return classfileBuffer;
        }
    }

    private void instrumentClass(CtClass cc) throws NotFoundException, CannotCompileException {
        CtClass latencyClass = ClassPool.getDefault().get("com.jonathancolt.nicity.profile.latent.Latency");
        CtField latency = new CtField(latencyClass, "latency", cc);
        latency.setModifiers(Modifier.STATIC | Modifier.PRIVATE | Modifier.FINAL);
        cc.addField(latency, "com.jonathancolt.nicity.profile.latent.Latency.singleton()");
        System.out.println("Instrumenting Class:"+cc.getName());
    }

    private void instrumentMethod(String interfaceName, CtMethod[] methods, int k, CtClass cc) throws CannotCompileException, NotFoundException {

        if (methods[k].isEmpty()) {
            return;
        }
        System.out.println("Instrumenting Method:" + interfaceName + " " + methods[k].getLongName());

        String fname = "latent" + k;
        CtClass latentClass = ClassPool.getDefault().get("com.jonathancolt.nicity.profile.latent.Latent");
        CtField f = new CtField(latentClass, fname, cc);
        f.setModifiers(Modifier.STATIC | Modifier.PRIVATE);
        cc.addField(f);

        String longMethodName = methods[k].getLongName();
        int splitIndex = longMethodName.lastIndexOf('.', longMethodName.indexOf('('));
        String cname = longMethodName.substring(0, splitIndex);
        String mname = longMethodName.substring(splitIndex);
        methods[k].insertBefore(fname + " = latency.enter(" + fname + ",\"" + interfaceName +"\",\"" + cname + "\",\"" + mname + "\",String.valueOf(1));");
        methods[k].insertAfter(fname + ".exit();",true);
    }

    private CtClass isInterfaceMethod(CtClass[] interfaces, CtMethod method) {
        for (CtClass iface : interfaces) {
            if (iface != null) {
                for (CtMethod ifMethod : iface.getDeclaredMethods()) {
                    if (ifMethod.getName().equals(method.getName())) {
                        return iface;
                    }
                }
            }
        }
        return null;
    }

    private boolean isDevelopControlledInterface(CtClass[] interfaces) {
        boolean inControlOfInterfaces = false;
        for (int i = 0; i < interfaces.length; i++) {
            boolean inControlOfInterface = false;
            NEXT_PACKAGE: for (String includable : includePackages) {
                String interfaceName = interfaces[i].getName();
                if (!interfaceName.contains("com.jonathancolt.nicity.profile")) {
                    if (interfaceName.contains(includable)) {
                        for(String excludable:excludePackages) {
                            if (interfaceName.contains(excludable)) {
                                continue NEXT_PACKAGE;
                            }
                        }


                        System.out.println("In control of interface:" + interfaces[i].getName());
                        inControlOfInterface = true;
                    }
                }
            }
            if (inControlOfInterface) {
                inControlOfInterfaces = true;
            } else {
                interfaces[i] = null;
            }
        }
        return inControlOfInterfaces;
    }
}
