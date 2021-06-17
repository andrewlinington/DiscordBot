package Commands;

import Commands.utils.Command;
import main.utils.FileConfig;
import main.utils.SHGame;
import main.utils.ServerGame;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InitTest {
  private Init init;

  private MockedStatic<ServerGame> s;

  @Mock
  private MessageReceivedEvent event;
  @Mock
  private TextChannel channel;
  @Mock
  private Guild guild;

  private FileConfig config;
  private SHGame game;

    @Before
    public void setUp() throws Exception {
      config = new FileConfig();
      game = new SHGame();
      config.setBot_channel(0L);
      game.setConfig(config);
      s = mockStatic(ServerGame.class);
      init = Mockito.spy(new Init("!init","description"));
      Mockito.doNothing().when((Command) init).setSHGame(event);


//      when(event.getTextChannel()).thenReturn(channel);
//      when(channel.getIdLong()).thenReturn(10L);
//      when(event.getGuild()).thenReturn(guild);
    }

    @After
    public void tearDown() throws Exception {
      s.close();
    }

    @Test (expected = NullPointerException.class)
    public void testConfigGenerated_withNoSHGame_throwsNullPointerException() {
        init.setGame(null);
        init.start(event);
        verify(init, never()).setSHGame(event);
    }

}