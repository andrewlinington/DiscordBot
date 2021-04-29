package Commands.SH.Objects;

import java.util.ArrayList;
import java.util.Collections;

public class RoleList {

    public static Role createHitler (){
        Role hitler = new Role (RoleType.Fascist, SecretRoleType.Hitler);
        return hitler;
    }

    public static Role createLiberal (){
        Role liberal = new Role (RoleType.Liberal, SecretRoleType.Liberal);
        return liberal;
    }

    public static Role createFascist (){
        Role fascist = new Role (RoleType.Fascist, SecretRoleType.Fascist);
        return fascist;
    }

    public static ArrayList<Role> createRoles(int numRoles) {
        ArrayList<Role> roles = new ArrayList<>();
        switch (numRoles) {
            case 10:
                roles.add(createLiberal());
            case 9:
                roles.add(createFascist());
            case 8:
                roles.add(createLiberal());
            case 7:
                roles.add(createFascist());
            case 6:
                roles.add(createLiberal());
            case 5:
                roles.add(createLiberal());
                roles.add(createLiberal());
                roles.add(createLiberal());
                roles.add(createFascist());
                roles.add(createHitler());
        }

        Collections.shuffle(roles);
        return roles;
    }




}
