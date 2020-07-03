package dev.nest.bowspleef.enums;

public enum GameState {

    WAITING,
    STARTING,
    RUNNING,
    STOPPING,
    STOPPED,
    INIT,
    FORCE_START;

    private static GameState state;

    public static void setState(GameState state) {
        GameState.state = state;
    }

    public static GameState getState() {
        return state;
    }

}
