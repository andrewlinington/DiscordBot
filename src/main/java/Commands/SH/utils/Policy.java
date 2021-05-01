package Commands.SH.utils;

import Commands.SH.utils.enums.RoleType;

public class Policy {
    private RoleType role;
    public Policy(RoleType role) {
        this.role = role;
    }

    public String getRole() {
        switch (role) {
            case Fascist:
                return "Fascist";
            case Liberal:
                return  "Liberal";
            default:
                return "Error";
        }
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
