package br.net.fabiozumbi12.RWSM;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Configs {

	private static Plugin plugin;
	static FileConfiguration configs = new RWSMYaml();
	static String pathMain = "plugins" + File.separator + RWSM.pdf.getName() + File.separator;
	static String pathConfig = "plugins" + File.separator + RWSM.pdf.getName() + File.separator + "config.yml";
	
	public static boolean debug() {
		return plugin.getConfig().getBoolean("debug-messages");
	}

	public static void init(Plugin plugin) {
		Configs.plugin = plugin;
		
		/*-------------------- Create Files ----------------------*/
		File main = new File(pathMain);
		File config = new File(pathConfig);		
		
		//create main folder
		if (!main.exists()) {
            main.mkdir();
            RWSM.logger.info("Created folder: " + pathMain);
        }
		
		//create config file 
		if (!config.exists()) {
        	plugin.saveResource("config.yml", false);   	            	
        	RWSM.logger.info("Created config file: " + pathConfig);
        } 
		
		/*-------------------- Load Configs from file ----------------------*/	
		try {
			plugin.getConfig().load(config);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
        configs = RWSMYaml.inputLoader(plugin.getResource("config.yml"));  
        for (String key:configs.getKeys(true)){                        	
        	configs.set(key, plugin.getConfig().get(key));    	            	   	            	
        }                        
        for (String key:configs.getKeys(false)){    
        	plugin.getConfig().set(key, configs.get(key));
        	RWSM.logger.debug("Set key: "+key);
        } 
        
        /*-------------------- End of Config file ----------------------*/
        plugin.saveConfig();
	}
	
    public static String getString(String key){		
		return plugin.getConfig().getString(key);
	}
    
    public static Integer getInt(String key){		
		return plugin.getConfig().getInt(key);
	}
    
    public static List<String> getStringList(String key){		
		return plugin.getConfig().getStringList(key);
	}
    
    public static Boolean getBool(String key){		
		return plugin.getConfig().getBoolean(key, false);
	}
    
}
