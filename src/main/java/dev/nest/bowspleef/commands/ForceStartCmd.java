package dev.nest.bowspleef.commands;

import dev.nest.bowspleef.enums.GameState;
import dev.nest.bowspleef.managers.BSManager;
import dev.nest.bowspleef.objects.BSPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ForceStartCmd implements CommandExecutor {

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
            if (GameState.getState() == GameState.WAITING) {
                BSManager manager = BSManager.getInstance();
                ItemStack bow, arrow;
                ItemMeta bowMeta;
                for (BSPlayer p : manager.getPlayers()) {
                    bow = new ItemStack(Material.BOW, 1);
                    arrow = new ItemStack(Material.ARROW, 1);
                    bowMeta = bow.getItemMeta();
                    bowMeta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
                    bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                    bow.setItemMeta(bowMeta);
                    p.getSpigotPlayer().getInventory().setItem(0, bow);
                    p.getSpigotPlayer().getInventory().setItem(1, arrow);
                    if (p.getSpigotPlayer().getAllowFlight()) {
                        p.getSpigotPlayer().setAllowFlight(false);
                    }
                }
                GameState.setState(GameState.FORCE_START);
                player.sendMessage(ChatColor.YELLOW + "Set game state to RUNNING!");
            } else {
                player.sendMessage(ChatColor.RED + "You can only force start at WAITING state!");
            }
        }
        return false;
    }

}
