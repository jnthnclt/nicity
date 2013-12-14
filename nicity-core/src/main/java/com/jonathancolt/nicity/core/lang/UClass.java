/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonathancolt.nicity.core.lang;

/*
 * #%L
 * nicity-core
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 *
 * @author jonathan
 */
public class UClass {

    public static void main(String[] args) {
        System.out.println(new File(".").getAbsolutePath());
        for (Class c : getClasseNamesInPackage(Comparable.class, null, "./target/nicity-core-1.0-SNAPSHOT.jar")) {
            System.out.println(c);
        }
    }

    public static List<Class> getClasseNamesInPackage(Class isAssignableFrom, String packageName, String jarName) {

        if (packageName == null) {
            packageName = "";
        }
        ArrayList<Class> classes = new ArrayList<>();
        packageName = packageName.replaceAll("\\.", "/");
        try {
            JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
            JarEntry jarEntry;
            while (true) {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                if ((jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class"))) {
                    try {
                        String className = jarEntry.getName().replaceAll("/", "\\.");
                        className = UFile.removeExtension(className);// removes traling .class
                        if (className.lastIndexOf("$") != -1) {
                            continue;
                        }
                        //System.out.println(className);
                        Class clazz = Class.forName(className,true,Thread.currentThread().getContextClassLoader());
                        if (isAssignableFrom != null && !isAssignableFrom.isAssignableFrom(clazz)) {
                            continue;
                        }
                        classes.add(clazz);
                    } catch (NoClassDefFoundError x) {
                        continue;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }
}
