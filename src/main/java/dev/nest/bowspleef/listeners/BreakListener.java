package dev.nest.bowspleef.listeners;

import dev.nest.bowspleef.enums.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        switch (GameState.getState()) {
            case INIT:
            case WAITING:
            case STARTING:
            case RUNNING:
            case STOPPING:
            case STOPPED:
            case FORCE_START:
                event.setCancelled(true);
                break;
        }
    }

}
