/*
 * Copyright 2013 jonathan.
 *
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
 */
package com.jonathancolt.nicity.profile.latent;

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

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

/**
 *
 * @author jonathan
 */
public class LatentSecurity extends SecurityManager {
    private final SecurityManager securityManager;

    public LatentSecurity(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    @Override
    public boolean getInCheck() {
        return securityManager.getInCheck();
    }

    @Override
    public Object getSecurityContext() {
        return securityManager.getSecurityContext();
    }

    @Override
    public void checkPermission(Permission perm) {
        securityManager.checkPermission(perm);
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        securityManager.checkPermission(perm, context);
    }

    @Override
    public void checkCreateClassLoader() {
        securityManager.checkCreateClassLoader();
    }

    @Override
    public void checkAccess(Thread t) {
        securityManager.checkAccess(t);
    }

    @Override
    public void checkAccess(ThreadGroup g) {
        securityManager.checkAccess(g);
    }

    @Override
    public void checkExit(int status) {
        securityManager.checkExit(status);
    }

    @Override
    public void checkExec(String cmd) {
        securityManager.checkExec(cmd);
    }

    @Override
    public void checkLink(String lib) {
        securityManager.checkLink(lib);
    }

    @Override
    public void checkRead(FileDescriptor fd) {
        securityManager.checkRead(fd);
    }

    @Override
    public void checkRead(String file) {
        securityManager.checkRead(file);
    }

    @Override
    public void checkRead(String file, Object context) {
        securityManager.checkRead(file, context);
    }

    @Override
    public void checkWrite(FileDescriptor fd) {
        securityManager.checkWrite(fd);
    }

    @Override
    public void checkWrite(String file) {
        securityManager.checkWrite(file);
    }

    @Override
    public void checkDelete(String file) {
        securityManager.checkDelete(file);
    }

    @Override
    public void checkConnect(String host, int port) {
        securityManager.checkConnect(host, port);
    }

    @Override
    public void checkConnect(String host, int port, Object context) {
        securityManager.checkConnect(host, port, context);
    }

    @Override
    public void checkListen(int port) {
        securityManager.checkListen(port);
    }

    @Override
    public void checkAccept(String host, int port) {
        securityManager.checkAccept(host, port);
    }

    @Override
    public void checkMulticast(InetAddress maddr) {
        securityManager.checkMulticast(maddr);
    }

    @Override
    public void checkMulticast(InetAddress maddr, byte ttl) {
        securityManager.checkMulticast(maddr, ttl);
    }

    @Override
    public void checkPropertiesAccess() {
        securityManager.checkPropertiesAccess();
    }

    @Override
    public void checkPropertyAccess(String key) {
        securityManager.checkPropertyAccess(key);
    }

    @Override
    public boolean checkTopLevelWindow(Object window) {
        return securityManager.checkTopLevelWindow(window);
    }

    @Override
    public void checkPrintJobAccess() {
        securityManager.checkPrintJobAccess();
    }

    @Override
    public void checkSystemClipboardAccess() {
        securityManager.checkSystemClipboardAccess();
    }

    @Override
    public void checkAwtEventQueueAccess() {
        securityManager.checkAwtEventQueueAccess();
    }

    @Override
    public void checkPackageAccess(String pkg) {
        securityManager.checkPackageAccess(pkg);
    }

    @Override
    public void checkPackageDefinition(String pkg) {
        securityManager.checkPackageDefinition(pkg);
    }

    @Override
    public void checkSetFactory() {
        securityManager.checkSetFactory();
    }

    @Override
    public void checkMemberAccess(Class<?> clazz, int which) {
        securityManager.checkMemberAccess(clazz, which);
    }

    @Override
    public void checkSecurityAccess(String target) {
        securityManager.checkSecurityAccess(target);
    }

    @Override
    public ThreadGroup getThreadGroup() {
        return securityManager.getThreadGroup();
    }

}
