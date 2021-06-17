package main.utils;

import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServerGameTest {

    private HashMap<Long,SHGame> games;

    @Mock
    private SHGame mockSHGame;
    @Mock
    private SHGame mockSHGameTwo;
    @Mock
    private Gamestate mockGameState;
    @Mock
    private User mockAuthor;
    @Mock
    private Guild mockGuild;
    @Mock
    private Guild second;
    private Player player;
    @Mock
    private Gamestate mockGameStateTwo;

    @Before
    public void setup () {
        games = new HashMap<>();
        games.put(100L, mockSHGame);
        ServerGame.setGames(games);
        player = new Player(mockAuthor);

        when(mockGameState.getGameStage()).thenReturn(GameStage.LegislationChancellor);
        when(mockSHGame.getGamestate()).thenReturn(mockGameState);
        when(mockAuthor.getId()).thenReturn("id");
    }



    @Test
    public void findUserServer_withValidGuild() {
        ArrayList<Guild> arrayList = new ArrayList<>();
        arrayList.add(mockGuild);
        arrayList.add(second);
        games.put(30L, mockSHGameTwo);

        Player playerTwo = new Player(mockAuthor);
        playerTwo.setStatus(SecretHitlerStatus.President);

        when(mockAuthor.getMutualGuilds()).thenReturn(arrayList);
        when(mockGameState.findPlayer(anyString())).thenReturn(player);

        when(mockGuild.getIdLong()).thenReturn(100L);
        when(second.getIdLong()).thenReturn(30L);

        when(mockSHGameTwo.getGamestate()).thenReturn(mockGameStateTwo);
        when(mockGameStateTwo.getGameStage()).thenReturn(GameStage.LegislationChancellor);
        when(mockGameStateTwo.findPlayer(anyString())).thenReturn(playerTwo);

        playerTwo.setStatus(SecretHitlerStatus.Chancellor);
        player.setStatus(SecretHitlerStatus.Chancellor);
        assertEquals(mockGuild, ServerGame.findUserServer(mockAuthor));

        playerTwo.setStatus(SecretHitlerStatus.Chancellor);
        player.setStatus(SecretHitlerStatus.President);
        assertEquals(second, ServerGame.findUserServer(mockAuthor));


        playerTwo.setStatus(SecretHitlerStatus.President);
        player.setStatus(SecretHitlerStatus.President);
        assertNull(ServerGame.findUserServer(mockAuthor));

    }


    @Test
    public void findUserServer_withValidGuilds() {

        ArrayList<Guild> arrayList = new ArrayList<>();
        arrayList.add(mockGuild);
        arrayList.add(second);

        when(mockAuthor.getMutualGuilds()).thenReturn(arrayList);
        when(mockGuild.getIdLong()).thenReturn(100L);

        when(mockGameState.findPlayer(anyString())).thenReturn(player);

        player.setStatus(SecretHitlerStatus.Chancellor);
        assertEquals(mockGuild, ServerGame.findUserServer(mockAuthor));

        player.setStatus(SecretHitlerStatus.President);
        assertNull(ServerGame.findUserServer(mockAuthor));
    }

    @Test
    public void findUserServer_withNoValidUser() {
        assertNull(ServerGame.findUserServer(null));
    }

    @Test
    public void findUserServer_withNoValidMutualGuilds() {
        when(mockAuthor.getMutualGuilds()).thenReturn(new ArrayList<>());
        assertNull( ServerGame.findUserServer(mockAuthor));
    }

    @Test
    public void findUserServer_withNoValidGameForGuild() {
        ArrayList<Guild> arrayList = new ArrayList<>();
        arrayList.add(mockGuild);
        when(mockAuthor.getMutualGuilds()).thenReturn(arrayList);
        when(mockGuild.getIdLong()).thenReturn(20L);
        assertNull(ServerGame.findUserServer(mockAuthor));
    }

    @Test
    public void findUserServer_withNoValidRoleMatch() {
        ArrayList<Guild> arrayList = new ArrayList<>();
        arrayList.add(mockGuild);

        when(mockAuthor.getMutualGuilds()).thenReturn(arrayList);
        when(mockGuild.getIdLong()).thenReturn(100L);
        when(mockGameState.findPlayer(anyString())).thenReturn(player);
        assertNull(ServerGame.findUserServer(mockAuthor));
    }


    @Test
    public void manageGames() {
        ServerGame.setGames(null);
        assertNull(ServerGame.getGames());
        ServerGame.setGames(games);
        assertEquals(ServerGame.getGames(), games);
    }

}