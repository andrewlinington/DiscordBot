package main.eventListeners;

import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DisconnectEventListener extends ListenerAdapter {

    @Override
    public void onDisconnect(@NotNull DisconnectEvent event) {
        System.out.println("Attempting reconnect at:" + event.getTimeDisconnected());

    }
}
