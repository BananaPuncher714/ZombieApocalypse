package io.github.bananapuncher714.zombieapocalypse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bananapuncher714.zombieapocalypse.commands.ZombieCommand;
import io.github.bananapuncher714.zombieapocalypse.commands.ZombieTabCompleter;
import io.github.bananapuncher714.zombieapocalypse.dependencies.ClipsPlaceholder;
import io.github.bananapuncher714.zombieapocalypse.dependencies.MvDWPlaceholder;
import io.github.bananapuncher714.zombieapocalypse.dependencies.ZombieApocalypseExpansion;
import io.github.bananapuncher714.zombieapocalypse.listeners.MobListener;
import io.github.bananapuncher714.zombieapocalypse.listeners.PlayerListener;

public class ZombieApocalypse extends JavaPlugin {
	private static boolean placeholderAPI, mvdwPlaceholderAPI;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();

		placeholderAPI = Bukkit.getPluginManager().getPlugin( "PlaceholderAPI" ) != null;
		if ( placeholderAPI ) {
			getLogger().info( "Registering placeholders" );
			new ZombieApocalypseExpansion().register();
		}
		mvdwPlaceholderAPI = Bukkit.getPluginManager().getPlugin( "MVdWPlaceholderAPI" ) != null;
		
		registerCommands();
		registerListeners();
		
		new TimeWatcher( this );
		
		DemoStarter.init();
	}

	@Override
	public void onDisable() {
		ApocalypseManager.getInstance().disable();
	}
	
	private void registerCommands() {
		getCommand( "zombieapocalypse" ).setExecutor( new ZombieCommand() );
		getCommand( "zombieapocalypse" ).setTabCompleter( new ZombieTabCompleter() );
	}
	
	private void registerListeners() {
		Bukkit.getPluginManager().registerEvents( new PlayerListener(), this );
		Bukkit.getPluginManager().registerEvents( new MobListener(), this );
	}
	
	public static String parse( Player player, String input ) {
		String result = input;
		if ( placeholderAPI ) {
			result = ClipsPlaceholder.parse( player, result );
		}
		if ( mvdwPlaceholderAPI ) {
			result = MvDWPlaceholder.parse( player, result );
		}
		return ChatColor.translateAlternateColorCodes( '&', result );
	}
}
