package main.eventListeners;

import Commands.Init;
import Commands.SH.Commands.Dammit;
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

  private static final Dammit d = new Dammit("I LOST THE GAME","WHY WOULD YOU DO THAT!!!");


  private void ownerCommands(MessageReceivedEvent event, String messageContent) {
    if (!event.isFromType(ChannelType.PRIVATE) && event.getMember().isOwner()) {
      Command c = new Init("!init", "");
      if (c.keyMatches(messageContent.toLowerCase(Locale.ROOT))) c.start(event);
    }
  }

  private void privateCommands(MessageReceivedEvent event, String messageContent) {
    if (event.isFromType(ChannelType.PRIVATE)) {
      CommandList.getPrivateCommands().stream()
          .filter(c -> c.keyMatches(messageContent.toLowerCase(Locale.ROOT)))
          .forEach(c -> c.start(event));
    }
  }

  private void publicCommands(MessageReceivedEvent event, String messageContent) {
    if (!event.isFromType(ChannelType.PRIVATE) && isGuildChannel(event) && isTextChannel(event)) {
      CommandList.getCommands().stream()
          .filter(c -> c.keyMatches(messageContent.toLowerCase(Locale.ROOT)))
          .forEach(c -> c.start(event));
    }
  }

  private boolean isTextChannel(MessageReceivedEvent event) {
    return ServerGame.getGames()
        .get(event.getGuild().getIdLong())
        .getConfig()
        .getBot_channel()
        .equals(event.getTextChannel().getIdLong());
  }

  private boolean isGuildChannel(MessageReceivedEvent event) {
    if (ServerGame.getGames().get(event.getGuild().getIdLong()) != null) {
      return true;
    }
    MessageHelper.sendMessage(event.getTextChannel(), "Owner needs to run !init on the channel");
    return false;
  }

  private boolean isBotMessage(MessageReceivedEvent event) {
    return !event.getMessage().isFromType(ChannelType.PRIVATE)
        && event.getAuthor().getIdLong() == BOT_ID
        && !event.getMessage().getEmbeds().isEmpty()
        && event.getMessage().getEmbeds().get(0).getColor().equals(Color.CYAN);
  }

  private void addReaction(Long id, MessageReceivedEvent event) {
    event
        .getMessage()
        .addReaction(Objects.requireNonNull(event.getGuild().getEmoteById(id)))
        .queue();
  }

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

  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    String messageContent = event.getMessage().getContentRaw();
    if (event.isFromGuild() && ServerGame.getGames().get(event.getGuild().getIdLong()) == null) {
      ServerGame.getGames()
          .put(event.getGuild().getIdLong(), new SHGame(new Gamestate(), new PlayerList(), null));
    }
    bruh(messageContent, event);
    if (!event.getAuthor().isBot() && messageContent.startsWith("!")) {
      ownerCommands(event, messageContent);
      privateCommands(event, messageContent);
      publicCommands(event, messageContent);
    } else if (event.getAuthor().isBot()) {
      botResponse(event);
    }
  }

  private void bruh(String messageContent, MessageReceivedEvent event) {
    if (d.keyMatches(messageContent) && !event.isFromType(ChannelType.PRIVATE)) {
      d.start(event);
    }
  }
}
