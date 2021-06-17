package Commands.SH.utils;

import Commands.SH.utils.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.image.BufferedImage;

/**
 * The Policy card
 */
@Getter
@AllArgsConstructor
public class Policy {
    private RoleType role;
    private final BufferedImage image;

    /**
     * Gets the RoleType as a string
     * @return the RoleType as a string
     */
    public String getRoleName() {
        return role.name();
    }

}
