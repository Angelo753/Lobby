package cz.angelo.angellobbycode;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderAPI extends PlaceholderExpansion {

		private Main plugin;

		/**
		 * Since we register the expansion inside our own plugin, we
		 * can simply use this method here to get an instance of our
		 * plugin.
		 *
		 * @param plugin
		 *        The instance of our plugin.
		 */
    public PlaceholderAPI(Main plugin){
			this.plugin = plugin;
		}

		@Override
		public boolean persist(){
			return true;
		}

		@Override
		public boolean canRegister(){
			return true;
		}

		@Override
		public String getAuthor(){
			return plugin.getDescription().getAuthors().toString();
		}

		@Override
		public String getIdentifier(){
			return "triplhub";
		}

		@Override
		public String getVersion(){
			return plugin.getDescription().getVersion();
		}

		@Override
		public String onPlaceholderRequest(Player player, String identifier){

			if(player == null){
				return "";
			}

			if(identifier.equals("placeholder1")){
				return plugin.getConfig().getString("placeholder1", "value doesnt exist");
			}

			if(identifier.equals("placeholder2")){
				return plugin.getConfig().getString("placeholder2", "value doesnt exist");
			}

			return null;
		}
}
