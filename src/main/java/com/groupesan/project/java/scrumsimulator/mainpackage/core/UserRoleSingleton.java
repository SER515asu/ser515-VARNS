package com.groupesan.project.java.scrumsimulator.mainpackage.core;

public class UserRoleSingleton {
    public enum Role {
        SCRUM_MASTER,
        DEVELOPER,
        PRODUCT_OWNER
    }

    private static UserRoleSingleton instance;
    private Role role;

    private UserRoleSingleton() {}

    public static synchronized UserRoleSingleton getInstance() {
        if (instance == null) {
            instance = new UserRoleSingleton();
            instance.role = Role.SCRUM_MASTER;
        }
        return instance;
    }

    public Role getRole() {
        return instance.role;
    }

    public void setRole(Role role) {
        instance.role = role;
    }
}
