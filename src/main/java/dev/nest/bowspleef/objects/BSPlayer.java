package dev.nest.bowspleef.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BSPlayer {

    private String name;

    public BSPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Player getSpigotPlayer() {
        return Bukkit.getPlayer(name);
    }

}
