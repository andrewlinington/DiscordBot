package Commands.SH.utils;

import Commands.SH.utils.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @version 1.1
 */
@Getter
@AllArgsConstructor
public class Role {
    private final RoleType publicRole;
    private final RoleType secretRole;

    private final String publicImage;
    private final String secretImage;

    /**
     * Acquires the public role of a player
     * @return the public role as a string
     */
    public String getPublicRoleName() {
        return publicRole.name();
    }

    /**
     * Acquires the private role of a player
     * @return the private role as a string
     */
    public String getSecretRoleName() {
         return secretRole.name();
    }


}
