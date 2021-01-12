package cz.angelo.angellobbycode;

//UPDATE `triplhub` SET `fly` = '1' WHERE `triplhub`.`UUID` = 'Angelo753'

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

	public static Main instance;

	public Location spawn;

	public static Mysql mysql = null;

	@Override
	public void onEnable() {
		instance = this;
		Config.reload();
		String host = Config.get().getString("settings.mysql.host");
		String port = Config.get().getString("settings.mysql.port");
		String user = Config.get().getString("settings.mysql.user");
		String password = Config.get().getString("settings.mysql.password");
		String database = Config.get().getString("settings.mysql.database");
		mysql = new Mysql(host, port, user, password, database);
		mysql.openConnection();
		Mysql.result("CREATE TABLE IF NOT EXISTS triplhub (UUID varchar(120) NOT NULL,fly int NOT NULL, speed int NOT NULL, PRIMARY KEY(UUID));");
		checkPlugins();
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		if (Config.get().getString("settings.spawn") != null){
			try {
				double x = Config.get().getDouble("settings.spawn.x");
				double y = Config.get().getDouble("settings.spawn.y");
				double z = Config.get().getDouble("settings.spawn.z");
				float yaw = Config.get().getInt("settings.spawn.yaw");
				float pitch = Config.get().getInt("settings.spawn.pitch");
				World world = Bukkit.getWorld(Config.get().getString("settings.spawn.world"));
				this.spawn = new Location(world, x, y, z, yaw, pitch);
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
		this.getServer().getPluginManager().registerEvents(new GameMechanics(), this);
	}

	@Override
	public void onDisable() {
	}

	public void checkPlugins(){
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null){
			System.out.println(ChatColor.RED + "PlaceholderAPI not found, some features may not work.");
		}else {
			new PlaceholderAPI(this).register();
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("menu")){
			if (sender.hasPermission("*") || sender.isOp() || sender.hasPermission("triplhub.adminpanel")) {
				if (args.length == 2){
					//Hrac je online?
					if (Config.get().getString("menus." + args[0]) != null){
						if (Bukkit.getPlayer(args[1]) != null){
							Menu.open(args[0], Bukkit.getPlayer(args[1]));
						}else {
							//toDo Hrac je offline
						}
					}else {
						//toDo zprava menu neexistuje
					}
					//Argument je nazev menu
				}else if (args.length == 1){
					Bukkit.broadcastMessage("33");
					if (sender instanceof Player){
						//Menu existuje?
						if (Config.get().getString("menus." + args[0]) != null){
							Bukkit.broadcastMessage(args[0]);
							Menu.open(args[0], (Player) sender);
						}else {
							//toDo zprava menu neexistuje
						}
					}else {
						//toDo zprava jenom hraci
					}
				}else {
					//toDo argument zprava
				}
			}else {
				//toDo zprava
			}
		}
		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("triplhub")){
				if (p.hasPermission("*") || p.isOp() || p.hasPermission("triplhub.buildmode")) {

					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("reload")) {
							Config.reload();
							//toDo reload zprava
						} else {
							//toDo Argumenty zprava

						}
					} else {
						//toDo Argumenty zprava
					}
				}
			}

			if (cmd.getName().equalsIgnoreCase("setspawn")){
				if (p.hasPermission("*") || p.isOp() || p.hasPermission("triplhub.setspawn")) {
					Config.get().set("settings.spawn.x", p.getLocation().getX());
					Config.get().set("settings.spawn.y", p.getLocation().getY());
					Config.get().set("settings.spawn.z", p.getLocation().getZ());
					Config.get().set("settings.spawn.yaw", p.getLocation().getYaw());
					Config.get().set("settings.spawn.pitch", p.getLocation().getPitch());
					Config.get().set("settings.spawn.world", p.getLocation().getWorld().getName());
					Config.save();
					Config.reload();
				}
			}
			//Buildmode
			if (cmd.getName().equalsIgnoreCase("buildmode")) {
				if (p.hasPermission("*") || p.isOp() || p.hasPermission("triplhub.buildmode")) {
					if (GameMechanics.instance.buildmode.contains(p.getName())) {
						GameMechanics.instance.buildmode.remove(p.getName());
						//toDo BuildMode zprava
					} else {
						GameMechanics.instance.buildmode.add(p.getName());
						//toDo BuildMode zprava
					}
				}
			}
			if (cmd.getName().equalsIgnoreCase("fly")) {
				if (p.hasPermission("*") || p.isOp() || p.hasPermission("triplhub.fly")) {
					if (p.getAllowFlight()) {
						p.setAllowFlight(false);
						p.setFlying(false);
						//toDo Fly zprava
					} else {
						p.setAllowFlight(true);
						p.setFlying(true);
						//toDo Fly zprava
					}
				}
			} else {
				//toDo
			}
		}
		return false;
	}

	public String color(String text){
		return ChatColor.translateAlternateColorCodes('&', text);
	}

}
