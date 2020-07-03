package dev.nest.bowspleef.listeners;

import dev.nest.bowspleef.enums.GameState;
import dev.nest.bowspleef.managers.BSManager;
import dev.nest.bowspleef.objects.BSPlayer;
import dev.nest.bowspleef.objects.BSSpectator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        BSManager manager = BSManager.getInstance();

        switch (GameState.getState()) {
            case RUNNING:
            case FORCE_START:
            case STARTING:
            case STOPPED:
            case INIT:
                if (manager.isPlaying(event.getPlayer().getName())) {
                    final BSPlayer p = manager.getBSPlayer(event.getPlayer().getName());
                    p.getSpigotPlayer().getInventory().clear();
                    manager.removeBSPlayer(p);
                    int size = manager.getPlayerSize();
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Player " + ChatColor.AQUA + "" + p.getName() +
                            ChatColor.YELLOW + " has left! (" + size + "/10)");
                } else if (manager.isSpectating(event.getPlayer().getName())) {
                    final BSSpectator spectator = manager.getBSSpectator(event.getPlayer().getName());
                    manager.removeBSSpectator(spectator);
                }
                break;
            case WAITING:
                final BSPlayer p = manager.getBSPlayer(event.getPlayer().getName());
                manager.removeBSPlayer(p);
                int size = manager.getPlayerSize();
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Player " + ChatColor.AQUA + "" + p.getName() +
                        ChatColor.YELLOW + " has left! (" + size + "/10)");
                break;
        }
    }

}
