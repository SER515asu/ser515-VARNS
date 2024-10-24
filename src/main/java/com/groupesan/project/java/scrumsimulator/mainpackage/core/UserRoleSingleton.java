package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import java.util.HashMap;
import java.util.Map;

public class UserRoleSingleton {

    private static UserRoleSingleton instance;
    private UserRole userRole;
    private static final Map<String, UserRole> userRoleLabelToValue = new HashMap<>(
            Map.of(
                    "Scrum Master", UserRole.SCRUM_MASTER,
                    "Developer", UserRole.DEVELOPER,
                    "Product Owner", UserRole.PRODUCT_OWNER,
                    "Scrum Administrator", UserRole.SCRUM_ADMIN
            )
    );

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
        if (userRole == null) {
            return;
        }
        instance.userRole = userRole;
    }

    public static UserRole getUserRoleValueFromLabel(String label) {
        if (userRoleLabelToValue.containsKey(label)) {
            return userRoleLabelToValue.get(label);
        }
        return null;
    }
}
