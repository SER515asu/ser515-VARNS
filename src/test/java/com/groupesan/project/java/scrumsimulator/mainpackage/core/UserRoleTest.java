package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserRoleTest {
    private UserRoleSingleton instance;

    @BeforeEach
    public void setup() {
        instance = UserRoleSingleton.getInstance();
    }

    @Test
    public void testChangingRoleToScrumMaster() {
        instance.setUserRole(UserRoleSingleton.UserRole.SCRUM_MASTER);
        Assertions.assertEquals(instance.getUserRole(), UserRoleSingleton.UserRole.SCRUM_MASTER, "User role should be SCRUM_MASTER");
    }

    @Test
    public void testChangingRoleToDeveloper() {
        instance.setUserRole(UserRoleSingleton.UserRole.DEVELOPER);
        Assertions.assertEquals(instance.getUserRole(), UserRoleSingleton.UserRole.DEVELOPER, "User role should be DEVELOPER");
    }

    @Test
    public void testChangingRoleToProductOwner() {
        instance.setUserRole(UserRoleSingleton.UserRole.PRODUCT_OWNER);
        Assertions.assertEquals(instance.getUserRole(), UserRoleSingleton.UserRole.PRODUCT_OWNER, "User role should be PRODUCT_OWNER");
    }
}
