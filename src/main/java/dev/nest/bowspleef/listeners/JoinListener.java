package dev.nest.bowspleef.listeners;

import dev.nest.bowspleef.enums.GameState;
import dev.nest.bowspleef.objects.BSPlayer;
import dev.nest.bowspleef.managers.BSManager;
import dev.nest.bowspleef.objects.BSSpectator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        BSManager manager = BSManager.getInstance();

        switch (GameState.getState()) {
            case STOPPING:
            case INIT:
            case STOPPED:
                event.getPlayer().kickPlayer(ChatColor.RED +
                        "You cannot join while the game is in INIT, STOPPED or STOPPING phase!");
                break;
            case RUNNING:
            case FORCE_START:
                final BSSpectator spectator = new BSSpectator(event.getPlayer().getName());
                manager.addBSSpectator(spectator);
                event.getPlayer().teleport(new Location(Bukkit.getWorld("flat"), 7, 5, 10));
                spectator.getSpigotPlayer().sendMessage(ChatColor.YELLOW + "You have been placed in spectator mode!");
                break;
            case WAITING:
            case STARTING:
                final BSPlayer player = new BSPlayer(event.getPlayer().getName());
                manager.addBSPlayer(player);
                int size = manager.getPlayerSize();
                player.getSpigotPlayer().sendMessage(ChatColor.YELLOW + "You have joined the waiting queue!");
                event.getPlayer().teleport(new Location(Bukkit.getWorld("flat"), 7, 5, 10));
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Player " + ChatColor.AQUA + "" + player.getName() +
                        ChatColor.YELLOW + " has joined! (" + size + "/10)");
                break;
        }

    }

}
