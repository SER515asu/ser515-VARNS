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
        instance.setRole(UserRoleSingleton.Role.SCRUM_MASTER);
        Assertions.assertEquals(instance.getRole(), UserRoleSingleton.Role.SCRUM_MASTER, "User role should be SCRUM_MASTER");
    }

    @Test
    public void testChangingRoleToDeveloper() {
        instance.setRole(UserRoleSingleton.Role.DEVELOPER);
        Assertions.assertEquals(instance.getRole(), UserRoleSingleton.Role.DEVELOPER, "User role should be DEVELOPER");
    }

    @Test
    public void testChangingRoleToProductOwner() {
        instance.setRole(UserRoleSingleton.Role.PRODUCT_OWNER);
        Assertions.assertEquals(instance.getRole(), UserRoleSingleton.Role.PRODUCT_OWNER, "User role should be PRODUCT_OWNER");
    }
}
