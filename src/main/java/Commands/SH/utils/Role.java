package Commands.SH.utils;

import Commands.SH.utils.enums.RoleType;

/**
 * the Role of a specific Player
 */
public class Role {
    //TODO: add images for secret and public roles
    private final RoleType publicRole;
    private final RoleType secretRole;

    /**
     * creates a new Role type
     * @param role the public role
     * @param secretRole the private/secret role
     */
    public Role(RoleType role, RoleType secretRole) {
        publicRole = role;
        this.secretRole = secretRole;
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

}
