package Commands.SH.utils;

import Commands.SH.utils.enums.RoleType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class PolicyTest {
   private Policy p;

   @Mock
   private BufferedImage b;

    @Before
    public void setUp() throws Exception {
      RoleType role = RoleType.Fascist;
      p = new Policy(role, b);
    }

    @Test
    public void getRoleName() {
        assertEquals(p.getRoleName(), "Fascist");
    }

    @Test
    public void getRole() {
        assertEquals(p.getRole(), RoleType.Fascist);
    }

    @Test
    public void getImage() {
        assertEquals(p.getImage(), b);
    }

    @Test
    public void nullImage() {
        p = new Policy(RoleType.Fascist, null);
        assertNull(p.getImage());
    }

}