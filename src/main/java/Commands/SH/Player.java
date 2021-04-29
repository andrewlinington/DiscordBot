package Commands.SH;

public class Player {
    //TODO:
    // Role -> required for the game needs to have the role card type and specialization at minimum
    String name;
    SecretHitlerStatus status;


    public Player (String name){
        this.name = name;
        status = SecretHitlerStatus.JOINED;
    }

}
