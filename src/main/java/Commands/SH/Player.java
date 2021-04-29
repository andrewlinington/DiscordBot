package Commands.SH;


public class Player {
    //TODO:
    // Role -> required for the game needs to have the role card type and specialization at minimum
    private String name;
    private String id;
    private SecretHitlerStatus status;


    public Player (String name, String id){
        this.name = name;
        status = SecretHitlerStatus.JOINED;
        this.id = id;
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

        }
        return "Joined";
    }

    public String getName() {
        return name;
    }
}
