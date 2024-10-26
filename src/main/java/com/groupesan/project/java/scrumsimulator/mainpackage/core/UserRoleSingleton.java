package com.groupesan.project.java.scrumsimulator.mainpackage.core;

public class UserRoleSingleton {
    public enum UserRole {
        SCRUM_MASTER,
        DEVELOPER,
        PRODUCT_OWNER
    }

    private static UserRoleSingleton instance;
    private UserRole userRole;

    private UserRoleSingleton() {}

    public static synchronized UserRoleSingleton getInstance() {
        if (instance == null) {
            instance = new UserRoleSingleton();
            instance.userRole = UserRole.SCRUM_MASTER;
        }
        return instance;
    }

    public UserRole getUserRole() {
        return instance.userRole;
    }

    public void setUserRole(UserRole userRole) {
        instance.userRole = userRole;
    }
}
