package Commands.SH;

import Commands.SH.Objects.GameStage;

public class Gamestate {

    private Gamestate () { }

    private static boolean isStarted = false;
    private static GameStage gameStage;

    public static boolean isStarted() {
        return isStarted;
    }

    public static void setStarted(boolean started) {
        isStarted = started;
    }

    public static void setState(GameStage stage) {
        gameStage = stage;
    }

    public static GameStage getGameStage() {
        return gameStage;
    }

}
