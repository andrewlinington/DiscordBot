package Commands;

import Commands.utils.Command;
import Commands.utils.CommandList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrivateHelpTest {
  @Mock
  private MessageReceivedEvent event;
  @Mock
  private PrivateChannel mockChannel;
  @Mock
  private MessageAction mockAction;

  @Captor
  private ArgumentCaptor<MessageEmbed> captor;

  private Command privateHelp;

  @Before
  public void setUp() throws Exception {
    privateHelp = new PrivateHelp("!help","the Description");
    when(event.getPrivateChannel()).thenReturn(mockChannel);
    when(mockChannel.sendMessage(Mockito.any(MessageEmbed.class))).thenReturn(mockAction);
  }


  @Test
    public void start() {
      ArrayList<Command> commands = CommandList.getInstance().getPrivateCommands();

      privateHelp.start(event);

      verify(mockChannel).sendMessage(captor.capture());
      assertEquals(captor.getValue().getFields().size(),commands.size() );
      assertEquals(captor.getValue().getColor(), Color.GREEN);
      assertEquals(captor.getValue().getTitle(), "Bot Commands:");
      assertEquals(captor.getValue().getFields().stream().findFirst().orElse(null).getName(),"!help");
      assertEquals(captor.getValue().getFields().get(1).getValue(), commands.get(1).getDescription());
    }
}