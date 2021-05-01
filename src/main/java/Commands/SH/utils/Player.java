package Commands.SH.utils;


import net.dv8tion.jda.api.entities.User;

public class Player {
    private Role role;
    private String name;
    private String id;
    private User user;
    private SecretHitlerStatus status;
    private boolean voted = false;
    private boolean failedToVote = false;


    public Player (String name, String id, User user){
        this.name = name;
        status = SecretHitlerStatus.Waiting;
        this.id = id;
        this.user = user;
    }

    public boolean hasID (String id){
        return this.id.equals(id);
    }

    public String userStatus () {
        switch (status) {
            case Waiting:
                return "Waiting";
            case Alive:
                return "Alive";
            case Dead:
                return "Dead";
            case President:
                return "President";
            case Chancellor:
                return "Chancellor";

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


    public void vote() {
        voted = true;
    }

    public void removeVote() {
        voted = false;
    }

    public boolean hasVoted() {
        return voted;
    }

    public boolean isFailedToVote() {
        return failedToVote;
    }

    public void setFailedToVote(boolean failedToVote) {
        this.failedToVote = failedToVote;
    }
}
