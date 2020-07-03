package dev.nest.bowspleef.threads;

import dev.nest.bowspleef.Main;
import dev.nest.bowspleef.enums.GameState;
import dev.nest.bowspleef.managers.BSManager;
import dev.nest.bowspleef.objects.BSPlayer;
import dev.nest.bowspleef.objects.BSSpectator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InitHandler {

    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private static int restartTime = 120;

    public static void run() {

        executor.scheduleAtFixedRate(() -> {
            BSManager manager = BSManager.getInstance();
            BSPlayer p;
            if (GameState.getState() == GameState.INIT) {
                for (BSSpectator s : manager.getSpectators()) {
                    p = new BSPlayer(s.getName());
                    manager.addBSPlayer(p);
                    manager.removeBSSpectator(s);
                    p.getSpigotPlayer().teleport(new Location(Bukkit.getWorld("flat"), 7, 5, 10));
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                    for (BSPlayer pl : manager.getPlayers()) {
                        if (pl.getSpigotPlayer().getLocation().getY() < 4) {
                            pl.getSpigotPlayer().teleport(new Location(Bukkit.getWorld("flat"), 7, 5, 10));
                        }
                        if (!pl.getSpigotPlayer().getAllowFlight()) {
                            pl.getSpigotPlayer().setAllowFlight(true);
                        }
                    }
                    for (int x = -18; x < 33; x++) {
                        for (int z = 33; z > -12; z--) {
                            Block block = Bukkit.getWorld("flat").getBlockAt(x, 3, z);
                            if (block.getType() != Material.TNT) {
                                block.setType(Material.TNT);
                            }
                        }
                    }
                }, 1);
                restartTime--;
                switch (restartTime) {
                    case 119:
                    case 90:
                    case 60:
                    case 30:
                    case 20:
                    case 10:
                    case 3:
                    case 2:
                    case 1:
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "Game is restarting in " + restartTime + " seconds!");
                        break;
                }
                if (restartTime == 0) {
                    GameState.setState(GameState.WAITING);
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Game is now in waiting mode!");
                    restartTime = 120;
                }
                System.out.println("InitHandlerThread: " + GameState.getState().name());
            }
        }, 0, 1, TimeUnit.SECONDS);

    }

    public static boolean isRunning() {
        return !executor.isTerminated();
    }

    public static void stop() {
        executor.shutdown();
    }

}
