package dev.nest.bowspleef.threads;

import dev.nest.bowspleef.Main;
import dev.nest.bowspleef.enums.GameState;
import dev.nest.bowspleef.managers.BSManager;
import dev.nest.bowspleef.objects.BSPlayer;
import org.bukkit.Bukkit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WaitHandler {

    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public static void run() {

        executor.scheduleAtFixedRate(() -> {
            BSManager manager = BSManager.getInstance();
            if (GameState.getState() == GameState.WAITING) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                    for (BSPlayer p : manager.getPlayers()) {
                        if (!p.getSpigotPlayer().getAllowFlight()) {
                            p.getSpigotPlayer().setAllowFlight(true);
                        }
                    }
                }, 1);
                if (manager.getPlayerSize() >= 5) {
                    GameState.setState(GameState.STARTING);
                }
                System.out.println("WaitHandleThread: " + GameState.getState().name());
            }
        }, 0, 50, TimeUnit.MILLISECONDS);

    }

    public static boolean isRunning() {
        return !executor.isTerminated();
    }

    public static void stop() {
        executor.shutdown();
    }

}
