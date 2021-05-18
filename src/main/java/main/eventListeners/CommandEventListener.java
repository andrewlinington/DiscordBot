package main.eventListeners;

import Commands.Init;
import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.PlayerList;
import Commands.utils.Command;
import Commands.utils.CommandList;
import main.utils.SHGame;
import main.utils.ServerGame;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Locale;
import java.util.Objects;

import static main.DiscordBot.BOT_ID;
// TODO: REFACTOR
// TODO: comment code and maybe implement rate limiting commands or the bot so it doesnt spam
public class CommandEventListener extends ListenerAdapter {

  /**
   * the owners commands
   * @param event the message event
   * @param messageContent the string for the message
   */
  private void ownerCommands(MessageReceivedEvent event, String messageContent) {
    if (!event.isFromType(ChannelType.PRIVATE) && event.getMember().isOwner()) {
      Command c = new Init("!init", "");
      if (c.keyMatches(messageContent.toLowerCase(Locale.ROOT))) c.start(event);
    }
  }

  /**
   * The Private message commands
   * @param event the message from the channel
   * @param messageContent the content of the channel
   */
  private void privateCommands(MessageReceivedEvent event, String messageContent) {
    if (event.isFromType(ChannelType.PRIVATE)) {
      CommandList.getPrivateCommands().stream()
          .filter(c -> c.keyMatches(messageContent.toLowerCase(Locale.ROOT)))
          .forEach(c -> c.start(event));
    }
  }

  /**
   * The public message commands for the user
   * @param event the users event
   * @param messageContent the message to be checked
   */
  private void publicCommands(MessageReceivedEvent event, String messageContent) {
    if (!event.isFromType(ChannelType.PRIVATE) && isGuildChannel(event) && isTextChannel(event)) {
      CommandList.getCommands().stream()
          .filter(c -> c.keyMatches(messageContent.toLowerCase(Locale.ROOT)))
          .forEach(c -> c.start(event));
    }
  }

  /**
   * Checks if the text channel is the bot channel
   * @param event the event message
   * @return the message that was sent
   */
  private boolean isTextChannel(MessageReceivedEvent event) {
    return ServerGame.getGames()
        .get(event.getGuild().getIdLong())
        .getConfig()
        .getBot_channel()
        .equals(event.getTextChannel().getIdLong());
  }

  /**
   * Check if the command is on a guild with a valid credentials/ emotes
   * @param event the Message event
   * @return true if the game is tied to a config
   */
  private boolean isGuildChannel(MessageReceivedEvent event) {
    if (ServerGame.getGames().get(event.getGuild().getIdLong()).getConfig() != null) {
      return true;
    }
    MessageHelper.sendMessage(event.getTextChannel(), "Owner needs to run !init on the channel");
    return false;
  }

  /**
   * Checks if the current message is from a bot
   * @deprecated plans to introduce JDA Utilities may make this unnecessary
   * @param event the message event
   * @return if the bot made a message to be reacted to
   */
  private boolean isBotMessage(MessageReceivedEvent event) {
    return !event.getMessage().isFromType(ChannelType.PRIVATE)
        && event.getAuthor().getIdLong() == BOT_ID
        && !event.getMessage().getEmbeds().isEmpty()
        && event.getMessage().getEmbeds().get(0).getColor().equals(Color.CYAN);
  }

  /**
   * Adds a reaction to a given message
   * @deprecated to be removed asd new methods have been implemented in its stead
   * @param id the Emote id
   * @param event the message to respond to
   */
  private void addReaction(Long id, MessageReceivedEvent event) {
    event
        .getMessage()
        .addReaction(Objects.requireNonNull(event.getGuild().getEmoteById(id)))
        .queue();
  }

  /**
   * the response a bot has to it sending a particular message
   * @deprecated to be removed due to new code
   * @param event the message event
   */
  private void botResponse(MessageReceivedEvent event) {
    if (isBotMessage(event)) {
      addReaction(
          ServerGame.getGames().get(event.getGuild().getIdLong()).getConfig().getYeet_emote(),
          event);
      addReaction(
          ServerGame.getGames().get(event.getGuild().getIdLong()).getConfig().getYeetnt_emote(),
          event);
    }
  }

  /**
   * Checks if a message is a commmand or from an unregistered server and interacts based on the contents of the message
   * @param event the message being sent event
   */
  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    generateServerGame(event);
    checkCommands(event, event.getMessage().getContentRaw());
  }

  /**
   * Checks if the command is valid and gives a proper response
   * @param event the message event
   * @param messageContent the content of the message
   */
  private void checkCommands (MessageReceivedEvent event, String messageContent) {
    if (!event.getAuthor().isBot() && messageContent.startsWith("!")) {
      ownerCommands(event, messageContent);
      privateCommands(event, messageContent);
      publicCommands(event, messageContent);
    }
  }

  /**
   * Generates a new game for a server if it doesnt already exist
   * @param event the message from the server
   */
  private void generateServerGame(MessageReceivedEvent event) {
    if (event.isFromGuild() && ServerGame.getGames().get(event.getGuild().getIdLong()) == null) {
      ServerGame.getGames()
              .put(event.getGuild().getIdLong(), new SHGame(new Gamestate(), new PlayerList(), null));
    }
  }

}
