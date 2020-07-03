package dev.nest.bowspleef.commands;

import dev.nest.bowspleef.enums.GameState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceStopCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command cannot be executed by console!");
            return false;
        }
        final Player player = (Player) sender;
        if (player.hasPermission("bs.admin")) {
            if (args.length > 0) {
                player.sendMessage(ChatColor.RED + "Incorrect Usage!");
                return false;
            }
            switch (GameState.getState()) {
                case RUNNING:
                case FORCE_START:
                    GameState.setState(GameState.STOPPING);
                    player.sendMessage(ChatColor.YELLOW + "Force set state to state STOPPING!");
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "You can only force stop in RUNNING state!");
                    break;
            }
        }
        return false;
    }

}
