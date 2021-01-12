package cz.angelo.angellobbycode;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameMechanics implements Listener {

	public Set<String> buildmode = new HashSet<>();
	public static GameMechanics instance;

	public GameMechanics(){
		instance = this;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		event.setJoinMessage(null);

		//Mysql
		Mysql.result("INSERT IGNORE INTO triplhub (`UUID`, `fly`, `speed`) VALUES('" + player.getUniqueId() + "', '0', '0')");
		//teleport
		if (Main.instance.spawn != null){
			player.teleport(Main.instance.spawn);
		}

		//JoinMessage
		List<String> joinMsg = Config.get().getStringList("messages.join");
		for (String message : joinMsg){
			player.sendMessage(Main.instance.color(message));
		}

		player.getInventory().clear();
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		Player player = event.getPlayer();
		event.setQuitMessage(null);
	}

	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent event){
		String cmd = event.getMessage();
		Bukkit.broadcastMessage(cmd);
		ConfigurationSection cmds = Config.get().getConfigurationSection("menus");
		for (String command : cmds.getKeys(false)){
			if (Config.get().getStringList("menus." + command + ".commands").contains(cmd.split(" ")[0])){
				Menu.open(command, event.getPlayer());
				event.setCancelled(true);
			}

		}
	}

	@EventHandler
	public void blockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		Bukkit.broadcastMessage(player.getName());
		if (!this.buildmode.contains(player.getName())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void blockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if (!this.buildmode.contains(player.getName())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onUse(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if (!this.buildmode.contains(player.getName())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void starving(FoodLevelChangeEvent event){
		event.setCancelled(true);
	}

	@EventHandler
	public void weatherChange(WeatherChangeEvent event){
		event.setCancelled(true);
	}

	@EventHandler
	public void entityDamage(EntityDamageByEntityEvent event){
		Entity damager = event.getDamager();
		if (damager instanceof Player){
			if (!this.buildmode.contains(damager.getName())){
				event.setCancelled(true);
			}
		}else {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void invClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		ConfigurationSection menus = Config.get().getConfigurationSection("menus");
		for (String menu : menus.getKeys(false)){
			if (inv.getName().equals(Main.instance.color(Config.get().getString("menus." + menu + ".title")))){
				ConfigurationSection items = Config.get().getConfigurationSection("menus." + menu + ".items");
				for (String item : items.getKeys(false)){
					if (Config.get().getInt("menus." + menu + ".items." + item + ".slot") == event.getSlot()){
						for (String command : Config.get().getStringList("menus." + menu + ".items." + item + ".actions")){
							String[] cmd = command.split(";");
							if (cmd[0].equalsIgnoreCase("MESSAGE")){
								player.sendMessage(Main.instance.color(PlaceholderAPI.setPlaceholders(player, cmd[1])));
							}
							if (cmd[0].equalsIgnoreCase("CONSOLE")){
								Main.instance.getServer().dispatchCommand(Main.instance.getServer().getConsoleSender(), cmd[1]);
							}
							if (cmd[0].equalsIgnoreCase("PLAYER")){
								player.chat(PlaceholderAPI.setPlaceholders(player, cmd[1]));
							}
							if (cmd[0].equalsIgnoreCase("SERVER")){
								ByteArrayOutputStream b = new ByteArrayOutputStream();
								DataOutputStream out = new DataOutputStream(b);
								try {
									out.writeUTF("Connect");
									out.writeUTF(cmd[1]);
								} catch (IOException eee) {
								}
								Bukkit.getPlayer(player.getName()).sendPluginMessage(Main.instance, "BungeeCord", b.toByteArray());
							}
						}
					}
				}
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void voidDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
				entity.teleport(Main.instance.spawn);
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickUp(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if (!this.buildmode.contains(player.getName())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		if (!this.buildmode.contains(player.getName())){
			event.setCancelled(true);
		}
	}

}
