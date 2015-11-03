package br.net.fabiozumbi12.RWSM;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "------------ " + RWSM.pdf.getFullName() + " ------------");
            sender.sendMessage(ChatColor.GREEN + "Developed by " + ChatColor.GOLD + RWSM.pdf.getAuthors() + ChatColor.GREEN + ".");
            sender.sendMessage(ChatColor.GREEN + "---------------------------------------------------");
            return true;
        }
		
		if (!(sender instanceof Player)){			
			if (args.length == 1){				
				if (args[0].equalsIgnoreCase("reload")) {
					RWSM.plugin.getServer().getPluginManager().disablePlugin((Plugin)RWSM.plugin);
					RWSM.plugin.getServer().getPluginManager().enablePlugin((Plugin)RWSM.plugin);
            		RWSM.logger.sucess(RWSM.pdf.getFullName() + " reloaded!");
            		return true;
				}
			}
		} 
		else {			
			if (args.length == 1){				
				if (args[0].equalsIgnoreCase("reload")) {
					RWSM.plugin.getServer().getPluginManager().disablePlugin((Plugin)RWSM.plugin);
					RWSM.plugin.getServer().getPluginManager().enablePlugin((Plugin)RWSM.plugin);
					sender.sendMessage(ChatColor.GREEN + RWSM.pdf.getFullName() + " reloaded!");
            		return true;
				}
			}
		}
		return true;
	}

}
