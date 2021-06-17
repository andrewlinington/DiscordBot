package Commands;

import Commands.utils.Command;
import Commands.utils.CommandList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HelpTest {

  private Command help;

  @Mock
  private MessageReceivedEvent event;
  @Mock
  private TextChannel mockChannel;
  @Mock
  private MessageAction mockAction;

  @Captor
  private ArgumentCaptor<MessageEmbed> captor;

    @Before
    public void setUp() throws Exception {
      help = new Help("!help","the Description");

      when(event.getTextChannel()).thenReturn(mockChannel);
      when(mockChannel.sendMessage(Mockito.any(MessageEmbed.class))).thenReturn(mockAction);
    }

  @Test
    public void start_helpWithCorrectOutput() {
      ArrayList<Command> commands = CommandList.getInstance().getCommands();
      help.start(event);

      verify(mockChannel).sendMessage(captor.capture());
      assertEquals(captor.getValue().getFields().size(),commands.size());
      assertEquals(captor.getValue().getColor(), Color.GREEN);
      assertEquals(captor.getValue().getTitle(), "Bot Commands:");
      assertEquals(captor.getValue().getFields().stream().findFirst().orElse(null).getName(),commands.stream().findFirst().get().getKey());
    }
}