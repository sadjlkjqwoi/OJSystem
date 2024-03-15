package com.feioj.feiojcodesandbox.security;

import java.security.Permission;

public class DefaultSecurity extends SecurityManager {
    @Override
    public void checkPermission(Permission perm) {
        System.out.println("默认不做任何设置");
        super.checkPermission(perm);
    }
}

