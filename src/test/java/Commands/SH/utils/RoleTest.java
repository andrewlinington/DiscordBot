package Commands.SH.utils;

import Commands.SH.utils.enums.RoleType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoleTest {
    private Role r;

    @Before
    public void setUp() {
        RoleType publicRole = RoleType.Liberal;
        RoleType privateRole = RoleType.Fascist;
        r = new Role(publicRole,privateRole,"public","private");
    }

    @Test
    public void testGetPublicRoleName_hasProperName() {
        assertEquals(r.getPublicRoleName(),"Liberal");
    }

    @Test
    public void testGetSecretRoleName_hasProperName() {
        assertEquals(r.getSecretRoleName(),"Fascist");
    }

    @Test
    public void testGetPublicImage_hasProperFilePath() {
        assertEquals(r.getPublicImage(),"public");
    }

    @Test
    public void testGetSecretImage_hasProperFilePath() {
        assertEquals(r.getSecretImage(),"private");
    }

    @Test
    public void testGetPublicRole_hasProperValue() {
        assertEquals(r.getPublicRole(),RoleType.Liberal);
    }

    @Test
    public void testGetSecretRole_hasProperValue() {
        assertEquals(r.getSecretRole(),RoleType.Fascist);
    }

}