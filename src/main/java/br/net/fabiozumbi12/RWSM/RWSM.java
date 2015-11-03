package br.net.fabiozumbi12.RWSM;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class RWSM extends JavaPlugin {
	public static PluginDescriptionFile pdf;
	static Logger logger = new Logger();
	public static Server serv;
	public static Plugin plugin;
	
	public void onDisable() {
        logger.severe(RWSM.pdf.getFullName() + " disabled.");
    }
	
	public void onEnable() {
    	try {
    		serv = getServer();
            pdf = getDescription();
            plugin = this;
            Configs.init(this);
            getCommand("RandomWorldSpawnMob").setExecutor(new Commands());
            serv.getPluginManager().registerEvents(new Listeners(), this);
            logger.sucess(RWSM.pdf.getFullName() + " enabled.");
    	} catch (Exception ex){
    		logger.severe("Errors during plugin load! Disabling "+ RWSM.pdf.getFullName() +"...");
    		ex.printStackTrace();
    	}
    }	
}

class Logger{
	   
	public void sucess(String s) {
    	Bukkit.getConsoleSender().sendMessage(RWSM.pdf.getFullName() + ": [§a§l"+s+"§r]");
    }
	
    public void info(String s) {
    	Bukkit.getConsoleSender().sendMessage(RWSM.pdf.getFullName() + ": ["+s+"]");
    }
    
    public void warning(String s) {
    	Bukkit.getConsoleSender().sendMessage(RWSM.pdf.getFullName() + ": [§6"+s+"§r]");
    }
    
    public void severe(String s) {
    	Bukkit.getConsoleSender().sendMessage(RWSM.pdf.getFullName() + ": [§c§l"+s+"§r]");
    }
    
    public void log(String s) {
    	Bukkit.getConsoleSender().sendMessage(RWSM.pdf.getFullName() + ": ["+s+"]");
    }
    
    public String colorize(String msg){
    	msg = msg.replaceAll("(?i)&([a-f0-9k-or])", "§$1");
    	return msg;
    }
    
    public void debug(String s) {
        if (Configs.debug()) {
        	Bukkit.getConsoleSender().sendMessage(RWSM.pdf.getFullName() + ": [§b"+s+"§r]");
        }  
    }
}