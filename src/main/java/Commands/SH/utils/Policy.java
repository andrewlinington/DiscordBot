package Commands.SH.utils;

import Commands.SH.utils.enums.RoleType;

/**
 * The Policy card
 * TODO: add images to cards
 */
public class Policy {
    private final RoleType role;

    /**
     * Instantiates a Policy with a type
     * @param role the role to be used for the policy
     */
    public Policy(RoleType role) {
        this.role = role;
    }

    /**
     * Gets the RoleType as a string
     * @return the RoleType as a string
     */
    public String getRole() {
        return role.name();
    }

}
