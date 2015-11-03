package br.net.fabiozumbi12.RWSM;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class Listeners implements Listener {
	
	private int task;

	@EventHandler
	public void onSpawnMob(CreatureSpawnEvent e ){
		if (e.isCancelled()){
			return;
		}
		
		//check spawn reasons allowed to spawn
		List<String> reasons = Configs.getStringList("spawn-reason-allowed");
		if (!reasons.contains(e.getSpawnReason().name())){
			return;
		}
		
		Entity ent = e.getEntity();
		Creature nent = null;
		String eType = e.getEntityType().name();
		String replaceType = Configs.getString("mobs." + eType + ".mob-type.entity");
		Location loc = ent.getLocation();
		World w = ent.getWorld();		
		
		if (Configs.getString("mobs." + eType) != null){
			//check chance
			if (randInt(0,100) < Configs.getInt("mobs." + eType + ".spawn.chance")){								
				for (int i = 1; i <= Configs.getInt("mobs." + eType + ".spawn.amount"); i++){
					
					//set position for new entity
					if (Configs.getString("mobs." + eType + ".mode").equalsIgnoreCase("replace")){
						ent.remove();
					} else if (Configs.getString("mobs." + eType + ".mode").equalsIgnoreCase("add")){
						int radius = Configs.getInt("mobs." + eType + ".spawn.radius");
						loc.setX(loc.getX()+randInt(0,radius));
						loc.setZ(loc.getZ()+randInt(0,radius));
					}
					
					nent = (Creature) w.spawnEntity(loc, EntityType.valueOf(replaceType));					
					
					//set custom name
					if (!Configs.getString("mobs." + eType + ".custom-name").equalsIgnoreCase("none")){
						nent.setCustomName(Configs.getString("mobs." + eType + ".custom-name"));
					}
					
					//set hostile
	            	if (Configs.getBool("mobs." + eType + ".hostile")){
	            		nent.setMetadata("RWSMHostile", new FixedMetadataValue(RWSM.plugin,Configs.getInt("mobs." + eType + ".damage")));
	            	}
										
					//set age
					if (nent instanceof Animals){						
						Animals age = (Animals) nent;

						if (Configs.getString("mobs." + eType + ".mob-type.age").equalsIgnoreCase("random")){
							if (randInt(0,5) == 5){
								age.setBaby();
							} else {
								age.setAdult();
							}
						} else {
							age.setAge(Configs.getInt("mobs." + eType + ".mob-type.age"));
						}						
					}
					
					if (replaceType.equals("HORSE")){
						Horse horse = (Horse)nent;
						if (Configs.getBool("mobs." + eType + ".as-passanger") && horse.isAdult() && !ent.isInsideVehicle()){
							horse.setPassenger(ent);
						}
						horse.setVariant(Variant.valueOf(Configs.getString("mobs." + eType + ".mob-type.variant")));
						horse.setTamed(Configs.getBool("mobs." + eType + ".tameable"));
					}
					
					if (replaceType.equals("OCELOT")){
						Ocelot ocelot = (Ocelot)nent;
						ocelot.setCatType(Type.valueOf(Configs.getString("mobs." + eType + ".mob-type.variant")));
						ocelot.setTamed(Configs.getBool("mobs." + eType + ".tameable"));
					}					
				}				
			}
		}		
	}
	
	@EventHandler
	public void onUseEgg(PlayerInteractEvent e){
		if (e.isCancelled()){
			return;
		}
		Player p = e.getPlayer();		
		if (p.getItemInHand().getType().equals(Material.MONSTER_EGG) && (p.getGameMode().equals(GameMode.SURVIVAL) || p.getGameMode().equals(GameMode.ADVENTURE))) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            	RWSM.logger.debug("Right click block OK");
            	ItemStack hand = p.getItemInHand();
            	if (hand.getAmount() > 1){
            		hand.setAmount(hand.getAmount()-1);            		
            	} else {
            		hand.setType(Material.AIR);
            	}
            	p.setItemInHand(hand);
            	
            }
		}
	}
	
	
	@EventHandler
	public void onInteractOtherHorse(PlayerInteractEntityEvent e){
		if (e.isCancelled()){
			return;
		}
		Player p = e.getPlayer();
		Entity entCliked = e.getRightClicked();
		
		if (p.getItemInHand().getType().equals(Material.MONSTER_EGG)) {
			String eggType = p.getItemInHand().getData().toString();			
			Matcher pat = Pattern.compile("\\{(.*?)\\}").matcher(eggType);
			while(pat.find()) {
				eggType = pat.group(1);
			}
            RWSM.logger.debug("Right click: " + eggType);
            
            if (Configs.getString("mobs." + eggType) != null && 
            		entCliked.getType().name().equals(Configs.getString("mobs." + eggType + ".mob-type.entity"))){
            					
            	Creature oent = (Creature) p.getWorld().spawnEntity(entCliked.getLocation(), entCliked.getType());
            	
            	//set hostile
            	if (Configs.getBool("mobs." + eggType + ".hostile")){
            		oent.setMetadata("RWSMHostile", new FixedMetadataValue(RWSM.plugin,Configs.getInt("mobs." + eggType + ".damage")));
            	}
            	
            	if (oent instanceof Animals){
            		((Animals)oent).setBaby();
            	}
            	
				if (entCliked.getType().name().equals("OCELOT")){
					Ocelot ocelot = (Ocelot)entCliked;
					if (ocelot.getCatType().equals(Type.valueOf(Configs.getString("mobs." + eggType + ".mob-type.variant")))){
						((Ocelot)oent).setCatType(ocelot.getCatType());
						((Ocelot)oent).setTamed(Configs.getBool("mobs." + eggType + ".tameable"));
					} else {
						oent.remove();
						return;
					}
				}
				
				if (entCliked.getType().name().equals("HORSE")){
					Horse horse = (Horse)entCliked;
					if (horse.getVariant().equals(Variant.valueOf(Configs.getString("mobs." + eggType + ".mob-type.variant")))){						
						((Horse)oent).setVariant(horse.getVariant());
						((Horse)oent).setTamed(Configs.getBool("mobs." + eggType + ".tameable"));
					} else {
						oent.remove();
						return;
					}					
				}
				
				ItemStack hand = p.getItemInHand();
            	if (hand.getAmount() > 1){
            		hand.setAmount(hand.getAmount()-1);            		
            	} else {
            		hand.setType(Material.AIR);
            	}
            	p.setItemInHand(hand);
            }            	
		}
	}
	
	@EventHandler
	public void onPlayerLook(PlayerMoveEvent e){
		if (e.isCancelled()){
			return;
		}
		
		Player p = e.getPlayer();
		if (p.hasPermission("rwsm.bypass.hostile")){
			return;
		}		
		List<Entity> nearbyE = p.getNearbyEntities(40, 40, 40);
		for (Entity ent:nearbyE){
			if (ent.hasMetadata("RWSMHostile")){
				int dmg = ent.getMetadata("RWSMHostile").get(0).asInt();
				StartDamage(ent, p, dmg);
    			((Creature)ent).getTarget();
    			((Creature)ent).setTarget((LivingEntity)p);
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e){
		if (e.isCancelled()){
			return;
		}
		Entity ent = e.getDamager();
		if (ent.hasMetadata("RWSMHostile") && e.getEntity().hasPermission("rwsm.bypass.hostile")){
			e.setCancelled(true);
		}
	}
	
	private void StartDamage(final Entity ent, final Player p, final int dmg) {
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(RWSM.plugin, new Runnable() {
            public void run() {
            	if (!ent.isDead() && p.getLocation().distance(ent.getLocation()) < 2){
            		p.damage(dmg, ent);
            	} else {
            		Bukkit.getScheduler().cancelTask(task);        			
        		}                
            }
		}
        , 40, 40);
	}	

	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
}
