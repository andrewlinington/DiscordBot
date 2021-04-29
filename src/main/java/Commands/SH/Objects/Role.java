package Commands.SH.Objects;

public class Role {
    private RoleType publicRole;
    private SecretRoleType secretRole;

    public Role(RoleType role, SecretRoleType secretRole) {
        publicRole = role;
        this.secretRole = secretRole;
    }

    public void setPublicRole(RoleType publicRole) {
        this.publicRole = publicRole;
    }

    public void setSecretRole(SecretRoleType secretRole) {
        this.secretRole = secretRole;
    }


    public String getPublicRole () {
        switch (publicRole) {
            case Liberal:
                return "Liberal";
            case Fascist:
                return "Fascist";
        }
        return "Error";
    }

    public String getSecretRole () {
        switch (secretRole) {
            case Hitler:
                return "Hitler";
            case Liberal:
                return "Liberal";
            case Fascist:
                return "Fascist";

        }
        return "Error";
    }

}
