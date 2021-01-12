package cz.angelo.angellobbycode;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Menu {

	public static Menu instance;

	public Menu(){
		instance = this;
	}

	public static void open(String menu, Player player){
		Inventory inv = Bukkit.createInventory(null, Config.get().getInt("menus." + menu + ".size"), Main.instance.color(Config.get().getString("menus." + menu + ".title")));
		//Get items
		ConfigurationSection items = Config.get().getConfigurationSection("menus." + menu + ".items");
		for (String item : items.getKeys(false)){
			if (Config.get().getBoolean("menus." + menu + ".items." + item + ".custom_head")){
				ItemStack itemStack = GameManager.createSkull(Config.get().getString("menus." + menu + ".items." + item + ".data"));
				ItemMeta itemMeta = itemStack.getItemMeta();
				itemMeta.setDisplayName(Main.instance.color(PlaceholderAPI.setPlaceholders(player, Config.get().getString("menus." + menu + ".items." + item + ".name"))));
				List<String> lore = new ArrayList<>();
				for (String loree : Config.get().getStringList("menus." + menu + ".items." + item + ".lore")){
					lore.add(Main.instance.color(PlaceholderAPI.setPlaceholders(player, loree)));
				}
				itemMeta.setLore(lore);
				itemStack.setItemMeta(itemMeta);
				inv.setItem(Config.get().getInt("menus." + menu + ".items." + item + ".slot"), itemStack);
			}else {
				ItemStack itemStack = new ItemStack(Material.valueOf(Config.get().getString("menus." + menu + ".items." + item + ".data")));
				ItemMeta itemMeta = itemStack.getItemMeta();
				itemMeta.setDisplayName(Main.instance.color(PlaceholderAPI.setPlaceholders(player, Config.get().getString("menus." + menu + ".items." + item + ".name"))));
				List<String> lore = new ArrayList<>();
				for (String loree : Config.get().getStringList("menus." + menu + ".items." + item + ".lore")){
					lore.add(Main.instance.color(PlaceholderAPI.setPlaceholders(player, loree)));
				}
				itemMeta.setLore(lore);
				itemStack.setItemMeta(itemMeta);
				inv.setItem(Config.get().getInt("menus." + menu + ".items." + item + ".slot"), itemStack);
			}
		}
		player.openInventory(inv);
	}

}
