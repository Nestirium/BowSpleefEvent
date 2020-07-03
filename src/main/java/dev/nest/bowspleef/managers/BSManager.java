package dev.nest.bowspleef.managers;

import dev.nest.bowspleef.Main;
import dev.nest.bowspleef.objects.BSPlayer;
import dev.nest.bowspleef.objects.BSSpectator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import java.util.concurrent.ConcurrentLinkedQueue;

public class BSManager {

    private static BSManager instance = new BSManager();

    private BSManager() {}

    private static final ConcurrentLinkedQueue<BSPlayer> bsPlayers = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<BSSpectator> bsSpectators = new ConcurrentLinkedQueue<>();

    public void addBSPlayer(BSPlayer bsPlayer) {
        if (!bsPlayers.contains(bsPlayer)) {
            bsPlayers.offer(bsPlayer);
        }
    }

    public void removeBSPlayer(BSPlayer bsPlayer) {
        bsPlayers.remove(bsPlayer);
    }

    public BSPlayer getBSPlayer(String name) {
        for (BSPlayer p : bsPlayers) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public void addBSSpectator(BSSpectator spectator) {
        if (!bsSpectators.contains(spectator)) {
            bsSpectators.offer(spectator);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                spectator.getSpigotPlayer().setGameMode(GameMode.SPECTATOR);
                spectator.getSpigotPlayer().getInventory().clear();
            }, 1);

        }
    }

    public boolean isSpectating(String name) {
        for (BSSpectator spectator : bsSpectators) {
            if (spectator.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlaying(String name) {
        for (BSPlayer p : bsPlayers) {
            if (p.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void removeBSSpectator(BSSpectator spectator) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            spectator.getSpigotPlayer().setGameMode(GameMode.SURVIVAL);
        }, 1);
        bsSpectators.remove(spectator);
    }

    public BSSpectator getBSSpectator(String name) {
        for (BSSpectator spectator : bsSpectators) {
            if (spectator.getName().equalsIgnoreCase(name)) {
                return spectator;
            }
        }
        return null;
    }

    public int getSpectatorSize() {
        return bsSpectators.size();
    }

    public int getPlayerSize() {
        return bsPlayers.size();
    }

    public ConcurrentLinkedQueue<BSSpectator> getSpectators() {
        return bsSpectators;
    }

    public ConcurrentLinkedQueue<BSPlayer> getPlayers() {
        return bsPlayers;
    }

    public static BSManager getInstance() {
        return instance;
    }

}
