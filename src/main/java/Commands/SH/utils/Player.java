package Commands.SH.utils;

import Commands.SH.utils.enums.SecretHitlerStatus;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

//TODO: REFACTOR
@Getter
@Setter
public class Player {
    private Role role;
    private final User user;
    private SecretHitlerStatus status;
    private boolean voted = false;
    private boolean failedToVote = false;

    /**
     * initializes a player type
     * @param user the User object
     */
    public Player ( User user){
        status = SecretHitlerStatus.Waiting;
        this.user = user;
    }

    /**
     * gets if  the targets User id matches
     * @param id the target id
     * @return if the user matches by id
     */
    public boolean hasID (String id){
        return user.getId().equals(id);
    }

    /**
     * Gets the users name
     * @return the Users name
     */
    public String getName() {
        return user.getName();
    }

    /**
     * gets the users status from the enum
     * @return  the users status as a string
     */
    public String userStatus () {
         return status.name().replace("_", " ");
    }

    /**
     * Toggles the vote flag
     */
    public void toggleVote() {
        voted = !voted;
    }

    /**
     * Resets a player with default settings
     */
    public void reset () {
        status = SecretHitlerStatus.Waiting;
        resetVote();
    }

    public void resetVote() {
        voted = false;
        failedToVote = false;
    }


    /**
     * Gets the color associated with the public role
     * @return the role color
     */
    public Color getPublicRoleColor () {
        if(role.getPublicRole().equals("Fascist")) {
            return Color.RED;
        } else {
            return  Color.BLUE;
        }
    }


    /**
     * Gets the color associated with the private role
     * @return the role color
     */
    public Color getSecretRoleColor() {
        if(role.getSecretRole().equals("Fascist") || role.getSecretRole().equals("Hitler")) {
            return Color.RED;
        } else {
            return  Color.BLUE;
        }
    }

}
