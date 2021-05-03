package Commands.SH.utils;

import Commands.SH.utils.enums.RoleType;

/**
 * the Role of a specific Player
 */
public class Role {
    //TODO: add images for secret and public roles
    private final RoleType publicRole;
    private final RoleType secretRole;

    private String publicImage;
    private String privateImage;

    /**
     * creates a new Role type
     * @param role the public role
     * @param secretRole the private/secret role
     */
    public Role(RoleType role, RoleType secretRole, String publicImage, String privateImage) {
        this.publicRole = role;
        this.secretRole = secretRole;
        this.privateImage = privateImage;
        this.publicImage = publicImage;
    }

    /**
     * Acquires the public role of a player
     * @return the public role as a string
     */
    public String getPublicRole () {
        return publicRole.name();
    }

    /**
     * Acquires the private role of a player
     * @return the private role as a string
     */
    public String getSecretRole () {
         return secretRole.name();
    }

    public String getPublicImage() {
        return publicImage;
    }

    public String getPrivateImage() {
        return privateImage;
    }

}
