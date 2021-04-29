package Commands.SH.Objects;


import Commands.SH.SecretHitlerStatus;
import net.dv8tion.jda.api.entities.User;

public class Player {
    //TODO:
    // Role -> required for the game needs to have the role card type and specialization at minimum
    private Role role;
    private String name;
    private String id;
    private User user;
    private SecretHitlerStatus status;


    public Player (String name, String id, User user){
        this.name = name;
        status = SecretHitlerStatus.JOINED;
        this.id = id;
        this.user = user;
    }

    public boolean hasID (String id){
        return this.id.equals(id);
    }

    public String userStatus () {
        switch (status) {
            case JOINED:
                return "Joined";
            case ALIVE:
                return "Alive";
            case DEAD:
                return "Dead";
            case PRESIDENT:
                return "President";

        }
        return "Error";
    }

    public String getName() {
        return name;
    }

    public void setStatus(SecretHitlerStatus status) {
        this.status = status;
    }

    public SecretHitlerStatus getStatus() {
        return status;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
