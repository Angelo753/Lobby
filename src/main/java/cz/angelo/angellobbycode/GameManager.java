package cz.angelo.angellobbycode;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;

public class GameManager {

	public static GameManager instance;

	public Set<String> cooldownPlayers;

	public GameManager(){
		instance = this;
	}

	public void addCooldown(Player player){
		this.cooldownPlayers.add(player.getName());
		new BukkitRunnable(){
			@Override
			public void run() {
				cooldownPlayers.remove(player.getName());
				this.cancel();
			}
		}.runTaskTimerAsynchronously(Main.instance, 100, 0);
	}

	public static ItemStack createSkull(String url){
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		if (url.isEmpty()) {
			return head;
		}
		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);

		profile.getProperties().put("textures", new Property("textures", url));

		try {
			Field profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
		}catch (NoSuchFieldException | IllegalAccessException ex){
			ex.printStackTrace();
		}
		head.setItemMeta(headMeta);
		return head;
	}

}
