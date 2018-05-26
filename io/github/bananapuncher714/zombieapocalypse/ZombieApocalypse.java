package io.github.bananapuncher714.zombieapocalypse;

import java.io.File;

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
import io.github.bananapuncher714.zombieapocalypse.util.ApocalypseDeserializer;
import io.github.bananapuncher714.zombieapocalypse.util.FileUtil;

public class ZombieApocalypse extends JavaPlugin {
	public static final int MOB_CAP = 200;
	
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
		
		Bukkit.getScheduler().scheduleSyncDelayedTask( this, this::loadFiles, 2 );
	}
	
	private void loadFiles() {
		ApocalypseDeserializer.registerRandomRewards( new File( getDataFolder() + "/rewards" ) );
		ApocalypseDeserializer.registerSimpleSpawns( new File( getDataFolder() + "/spawns" ) );
		ApocalypseDeserializer.registerApocalypses( new File( getDataFolder() + "/apocalypses" ) );
	}

	@Override
	public void onDisable() {
		ApocalypseManager.getInstance().disable();
	}
	
	public void saveResources() {
		FileUtil.saveToFile( getResource( "data/apocalypse/example-apocalypse.yml" ), new File( getDataFolder() + "/apocalypses" + "/example-apocalypse.yml" ), false );
		FileUtil.saveToFile( getResource( "data/rewards/example-reward.yml" ), new File( getDataFolder() + "/rewards" + "/example-reward.yml" ), false );
		FileUtil.saveToFile( getResource( "data/spawns/example-spawn.yml" ), new File( getDataFolder() + "/spawns" + "/example-spawn.yml" ), false );
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
