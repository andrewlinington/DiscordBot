package main.eventListeners;

import com.google.common.collect.ImmutableList;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * @version 1.0
 */
public class EventListeners {
  public static final ImmutableList<ListenerAdapter> ALL =
      ImmutableList.of(
          new CommandEventListener(),
          new DisconnectEventListener(),
          new AddReactionEventListener());
  private EventListeners() {}
}
