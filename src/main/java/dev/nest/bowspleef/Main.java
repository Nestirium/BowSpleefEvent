package dev.nest.bowspleef;

import dev.nest.bowspleef.commands.ForceStartCmd;
import dev.nest.bowspleef.commands.ForceStopCmd;
import dev.nest.bowspleef.enums.GameState;
import dev.nest.bowspleef.listeners.*;
import dev.nest.bowspleef.threads.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new BreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlaceListener(), this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
        getServer().getPluginManager().registerEvents(new ItemPickupListener(), this);
        getCommand("bsforcestart").setExecutor(new ForceStartCmd());
        getCommand("bsforcestop").setExecutor(new ForceStopCmd());
        GameState.setState(GameState.WAITING);
        WaitHandler.run();
        StartHandler.run();
        RunHandler.run();
        StopHandler.run();
        InitHandler.run();
    }


    @Override
    public void onDisable() {
        InitHandler.stop();
        StopHandler.stop();
        RunHandler.stop();
        StartHandler.stop();
        WaitHandler.stop();
    }

}
