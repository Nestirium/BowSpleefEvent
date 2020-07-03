package dev.nest.bowspleef.threads;

import dev.nest.bowspleef.Main;
import dev.nest.bowspleef.enums.GameState;
import dev.nest.bowspleef.managers.BSManager;
import dev.nest.bowspleef.objects.BSPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartHandler {

    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private static int countdown = 30;

    public static void run() {

        executor.scheduleAtFixedRate(() -> {
            BSManager manager = BSManager.getInstance();
            ItemStack bow, arrow;
            ItemMeta bowMeta;
            if (GameState.getState() == GameState.STARTING) {
                if (manager.getPlayerSize() < 5) {
                    GameState.setState(GameState.WAITING);
                    Bukkit.broadcastMessage(ChatColor.RED + "Startup cancelled due to less than 5 players!");
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Game is now in waiting mode!");
                    countdown = 30;
                } else {
                    countdown--;
                    switch (countdown) {
                        case 29:
                        case 20:
                        case 10:
                        case 3:
                        case 2:
                        case 1:
                            Bukkit.broadcastMessage(ChatColor.YELLOW + "Game is starting in " + countdown + " seconds!");
                            break;
                        }
                        if (countdown == 0) {
                            for (BSPlayer p : manager.getPlayers()) {
                                bow = new ItemStack(Material.BOW, 1);
                                arrow = new ItemStack(Material.ARROW, 1);
                                bowMeta = bow.getItemMeta();
                                bowMeta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
                                bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                                bow.setItemMeta(bowMeta);
                                p.getSpigotPlayer().getInventory().setItem(0, bow);
                                p.getSpigotPlayer().getInventory().setItem(1, arrow);
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                                    if (p.getSpigotPlayer().getAllowFlight()) {
                                        p.getSpigotPlayer().setAllowFlight(false);
                                    }
                                }, 1);
                            }
                            Bukkit.broadcastMessage(ChatColor.YELLOW + "Game has started!");
                            GameState.setState(GameState.RUNNING);
                            countdown = 30;
                        }
                }
                System.out.println("StartHandlerThread: " + GameState.getState().name());
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
