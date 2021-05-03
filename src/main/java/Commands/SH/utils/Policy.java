package Commands.SH.utils;

import Commands.SH.utils.enums.RoleType;

import java.awt.image.BufferedImage;

/**
 * The Policy card
 */
public class Policy {
    private final RoleType role;
    private final BufferedImage image;

    /**
     * Instantiates a Policy with a type
     * @param role the role to be used for the policy
     * @param image the cards image
     */
    public Policy(RoleType role, BufferedImage image) {
        this.role = role;
        this.image = image;
    }

    /**
     * Gets the RoleType as a string
     * @return the RoleType as a string
     */
    public String getRole() {
        return role.name();
    }

    /**
     * Gets the cards image
     * @return the Image
     */
    public BufferedImage getImage() {
        return image;
    }
}
