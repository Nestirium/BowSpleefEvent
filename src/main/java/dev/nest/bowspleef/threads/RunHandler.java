package dev.nest.bowspleef.threads;

import dev.nest.bowspleef.Main;
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

public class RunHandler {

    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public static void run() {

        executor.scheduleAtFixedRate(() -> {
            BSManager manager = BSManager.getInstance();
            if (GameState.getState() == GameState.RUNNING
                    || GameState.getState() == GameState.FORCE_START) {
                BSSpectator spectator;
                for (BSPlayer p : manager.getPlayers()) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                        if (p.getSpigotPlayer().getAllowFlight()) {
                            p.getSpigotPlayer().setAllowFlight(false);
                        }
                    }, 1);
                    if (p.getSpigotPlayer().getLocation().getY() <= 0) {
                        spectator = new BSSpectator(p.getName());
                        manager.addBSSpectator(spectator);
                        manager.removeBSPlayer(p);
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "Player " + ChatColor.AQUA + "" + p.getName() +
                                ChatColor.YELLOW + " has fallen to their death! (" + manager.getPlayerSize() + "/10)");
                        p.getSpigotPlayer().teleport(new Location(Bukkit.getWorld("flat"), 7, 5, 10));
                    }
                    if (manager.getPlayerSize() <= 1) {
                        if (GameState.getState() != GameState.FORCE_START) {
                            GameState.setState(GameState.STOPPING);
                        } else {
                            try {
                                Thread.sleep(60000);
                                BSPlayer winner = manager.getPlayers().poll();
                                if (winner != null) {
                                    BSSpectator s = new BSSpectator(winner.getName());
                                    manager.addBSSpectator(s);
                                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Player " + ChatColor.AQUA + "" + s.getName()
                                            + ChatColor.YELLOW + " has won the game!");
                                    s.getSpigotPlayer().teleport(new Location(Bukkit.getWorld("flat"), 7, 5, 10));
                                    s.getSpigotPlayer().getInventory().clear();
                                    GameState.setState(GameState.INIT);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
                System.out.println("RunHandlerThread: " + GameState.getState().name());
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
