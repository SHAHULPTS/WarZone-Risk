package Utils;
import Models.GameState;

public class ExceptionLogHandler implements Thread.UncaughtExceptionHandler{

    GameState d_gameState;

    public ExceptionLogHandler(GameState p_gameState){
        d_gameState = p_gameState;
    }

    @Override
    public void uncaughtException(Thread p_t, Throwable p_e) {
        d_gameState.updateLog(p_e.getMessage(), "effect");
    }
}
