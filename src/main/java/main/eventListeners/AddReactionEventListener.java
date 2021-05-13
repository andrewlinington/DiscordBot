package main.eventListeners;

import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import main.utils.SHGame;
import main.utils.ServerGame;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
// TODO: REFACTOR
public class AddReactionEventListener extends ListenerAdapter {

  private int breadCount = 0;

  // TODO: implement batch reaction removal by storing all reactions in a hash map with a timer
  // lambda of sorts where they will be batch deleted after the last reaction has occurred
  // use clear Reactions unicode and clear reactions emote

  @Override
  public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
    if (event.getMember() == null || event.getUser() == null) return;
    if (isReactionable(event.retrieveMessage().complete(), event.getUser())) {
      if (event.getReactionEmote().isEmoji()) {
        breadReact(event);
      } else {
        boolean voted = voting(GameStage.Voting, event);
        voted = voted || voting(GameStage.Veto, event);
        if (!voted || !event.getReaction().retrieveUsers().complete().contains(event.getUser())) {
          event.getReaction().removeReaction(event.getUser()).queue();
        }
      }
    }
  }

  @Override
  public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
    if (event.getMember() == null || event.getUser() == null) return;
    if (isReactionable(event.retrieveMessage().complete(), event.getUser())) {
      checkCanRemove(event);
    }
  }

  private void checkCanRemove(MessageReactionRemoveEvent event) {
    SHGame game = ServerGame.getGames().get(event.getGuild().getIdLong());
    Player p = game.getGamestate().findPlayer(event.getUser().getId());
    if (p != null) {
      if (p.isFailedToVote()) {
        p.setFailedToVote(false);
      } else if (p.isVoted()
          && (game.getGamestate().getGameStage().equals(GameStage.Voting)
              || game.getGamestate().getGameStage().equals(GameStage.Veto))) {
        reactionAction(
            game.getConfig().getYeet_emote(),
            event.getReactionEmote().getEmote(),
            p,
            game.getGamestate());
        reactionAction(
            game.getConfig().getYeetnt_emote(),
            event.getReactionEmote().getEmote(),
            p,
            game.getGamestate());
      }
    }
  }

  private boolean isReactionable(Message m, User user) {
    return m.getAuthor().isBot()
        && m.getEmbeds().get(0).getColor().equals(Color.CYAN)
        && !user.isBot();
  }

  // TODO: break up into smaller parts
  private boolean voting(GameStage gs, MessageReactionAddEvent event) {
    SHGame game = ServerGame.getGames().get(event.getGuild().getIdLong());
    Player p = game.getGamestate().findPlayer(event.getUser().getId());
    if (game.getGamestate().getGameStage().equals(gs)) {
      if (p != null) {
        if (isLegislative(gs, p) && !p.isVoted()) {
          boolean ifAction =
              reactionAction(game.getConfig().getYeet_emote(), event, p, game.getGamestate());
          ifAction =
              ifAction
                  || reactionAction(
                      game.getConfig().getYeetnt_emote(), event, p, game.getGamestate());
          return ifAction;
        }
        p.setFailedToVote(true);
      }
    }
    return false;
  }

  private boolean reactionAction(
      long emoteID, MessageReactionAddEvent event, Player p, Gamestate game) {
    if (event.getReactionEmote().getEmote().getIdLong() == emoteID) {
      p.toggleVote();
      p.setFailedToVote(false);
      game.toggleVote(1, emoteID, event);
      return true;
    }
    return false;
  }

  private boolean reactionAction(long emoteID, Emote e, Player p, Gamestate game) {
    if (e.getIdLong() == emoteID) {
      p.toggleVote();
      p.setFailedToVote(false);
      game.toggleVote(-1, e);
      return true;
    }
    return false;
  }

  private boolean isLegislative(GameStage gs, Player p) {
    return (gs.equals(GameStage.Voting))
        ? !compareStatus(SecretHitlerStatus.Dead, SecretHitlerStatus.Waiting, p)
        : compareStatus(SecretHitlerStatus.Chancellor, SecretHitlerStatus.President, p);
  }

  private boolean compareStatus(
      SecretHitlerStatus statusOne, SecretHitlerStatus statusTwo, Player p) {
    return p.getStatus().equals(statusOne) || p.getStatus().equals(statusTwo);
  }

  private void breadReact(@NotNull MessageReactionAddEvent event) {
    if (event.getReactionEmote().getEmoji().equals(new String(Character.toChars(0x1F35E)))) {
      breadCount++;
      MessageHelper.sendMessage(event.getChannel(), "Bread count = " + breadCount);
    }
    event.getReaction().removeReaction(event.getUser()).queue();
  }
}
