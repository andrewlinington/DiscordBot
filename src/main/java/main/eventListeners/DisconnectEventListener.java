package main.eventListeners;

import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @version 1.0
 */
public class DisconnectEventListener extends ListenerAdapter {

  /**
   * If the bot disconnects reconnect
   * @param event the disconnection
   */
  @Override
  public void onDisconnect(@NotNull DisconnectEvent event) {
    System.out.println("Attempting reconnect at:" + event.getTimeDisconnected());
  }
}
