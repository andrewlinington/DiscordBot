package Commands.SH.utils;

import net.dv8tion.jda.api.entities.TextChannel;

public interface WinCondition {
    boolean canWin(TextChannel channel, Gamestate gs);
}
