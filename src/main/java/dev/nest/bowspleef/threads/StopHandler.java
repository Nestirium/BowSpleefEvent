package dev.nest.bowspleef.threads;

import dev.nest.bowspleef.enums.GameState;
import dev.nest.bowspleef.managers.BSManager;
import dev.nest.bowspleef.objects.BSPlayer;
import dev.nest.bowspleef.objects.BSSpectator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StopHandler {

    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public static void run() {

        executor.scheduleAtFixedRate(() -> {
            BSManager manager = BSManager.getInstance();
            if (GameState.getState() == GameState.STOPPING) {
                BSPlayer winner = manager.getPlayers().poll();
                if (winner != null) {
                    BSSpectator spectator = new BSSpectator(winner.getName());
                    manager.addBSSpectator(spectator);
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Player " + ChatColor.AQUA + "" + spectator.getName()
                            + ChatColor.YELLOW + " has won the game!");
                    spectator.getSpigotPlayer().teleport(new Location(Bukkit.getWorld("flat"), 7, 5, 10));
                    spectator.getSpigotPlayer().getInventory().clear();
                    System.out.println("StopHandleThread: " + GameState.getState().name());
                    GameState.setState(GameState.INIT);
                }
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
