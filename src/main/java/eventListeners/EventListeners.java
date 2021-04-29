package eventListeners;

import com.google.common.collect.ImmutableList;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListeners {

    private EventListeners () {}

    public static final ImmutableList<ListenerAdapter> ALL = ImmutableList.of(new CommandEventListener(), new DisconnectEventListener());

}
