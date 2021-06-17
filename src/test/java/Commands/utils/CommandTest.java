package Commands.utils;

import Mocks.MockCommand;
import main.utils.SHGame;
import main.utils.ServerGame;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommandTest {

    private Command c;
    private HashMap<Long, SHGame> games;

    @Mock
    private SHGame game;
    @Mock
    private User user;
    @Mock
    private MessageReceivedEvent event;
    @Mock
    private Guild guild;

    private MockedStatic<ServerGame> s;

    @Before
    public void setUp() throws Exception {
        setUpMocks();
        generateData();
        setUpMockReturns();
    }

    private void setUpMockReturns() {
        when(guild.getIdLong()).thenReturn(100L);
        when(event.getGuild()).thenReturn(guild);
        when(ServerGame.getGames()).thenReturn(games);
    }

    private void generateData() {
        games = new HashMap<>();
        games.put(100L,game);
        c = new MockCommand("boop","description");
    }


    private void setUpMocks() {
        s = mockStatic(ServerGame.class);
    }


    @Test
    public void keyMatches() {
        assertFalse(c.keyMatches("not correct"));
        assertTrue(c.keyMatches("boop"));
    }

    @Test
    public void start_withPrivateChannel_withValidGuild() {
          when(event.isFromType(ChannelType.PRIVATE)).thenReturn(true);
          when(event.getAuthor()).thenReturn(user);
          when(ServerGame.findUserServer(user)).thenReturn(guild);
          c.start(event);
          assertEquals(c.getGame(),game);
    }

    @Test
    public void start_withPrivateChannel_withInvalidGuild() {
        when(event.isFromType(ChannelType.PRIVATE)).thenReturn(true);
        when(event.getAuthor()).thenReturn(user);
        when(ServerGame.findUserServer(user)).thenReturn(null);
        c.start(event);
        assertNull(c.getGame());
    }

    @Test
    public void start_withPublicChannel_withInvalidGuild () {
        when(guild.getIdLong()).thenReturn(0L);
        c.start(event);
        assertNull(c.getGame());
    }

    @Test
    public void start_withPublicChannel_withValidGuild () {
        c.start(event);
        assertEquals(c.getGame(),game);
    }

    @Test
    public void getKey() {
        assertEquals(c.getKey(),"boop");
    }

    @Test
    public void getDescription() {
        assertEquals(c.getDescription(),"description");
    }

    @Test
    public void testSHGAME () {
        assertNull(c.getGame());
        c.setGame(game);
        assertEquals(c.getGame(), game);
    }


    @After
    public void cleanup() {
        s.close();
    }

}