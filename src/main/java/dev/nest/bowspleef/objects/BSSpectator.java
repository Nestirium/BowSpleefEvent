package dev.nest.bowspleef.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BSSpectator {

    private String name;

    public BSSpectator(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Player getSpigotPlayer() {
        return Bukkit.getPlayer(name);
    }

}
