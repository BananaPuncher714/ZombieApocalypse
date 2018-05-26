package io.github.bananapuncher714.zombieapocalypse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bananapuncher714.zombieapocalypse.commands.ZombieCommand;
import io.github.bananapuncher714.zombieapocalypse.commands.ZombieTabCompleter;
import io.github.bananapuncher714.zombieapocalypse.dependencies.ClipsPlaceholder;
import io.github.bananapuncher714.zombieapocalypse.dependencies.MvDWPlaceholder;
import io.github.bananapuncher714.zombieapocalypse.dependencies.ZombieApocalypseExpansion;
import io.github.bananapuncher714.zombieapocalypse.listeners.MobListener;
import io.github.bananapuncher714.zombieapocalypse.listeners.PlayerListener;
import io.github.bananapuncher714.zombieapocalypse.ngui.ClickListener;
import io.github.bananapuncher714.zombieapocalypse.ngui.NGui;
import io.github.bananapuncher714.zombieapocalypse.util.ApocalypseDeserializer;
import io.github.bananapuncher714.zombieapocalypse.util.FileUtil;

public class ZombieApocalypse extends JavaPlugin {
	public static int MOB_CAP = 200;
	
	private static Logger LOGGER;
	private static boolean placeholderAPI, mvdwPlaceholderAPI;
	
	private static Map< String, String > messages = new HashMap< String, String >();
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		loadConfig();
		
		LOGGER = getLogger();

		placeholderAPI = Bukkit.getPluginManager().getPlugin( "PlaceholderAPI" ) != null;
		if ( placeholderAPI ) {
			getLogger().info( "Registering placeholders" );
			new ZombieApocalypseExpansion().register();
		}
		mvdwPlaceholderAPI = Bukkit.getPluginManager().getPlugin( "MVdWPlaceholderAPI" ) != null;
		
		registerCommands();
		registerListeners();
		
		new TimeWatcher( this );
		
//		DemoStarter.init();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask( this, this::loadFiles, 2 );
	}
	
	private void loadConfig() {
		FileConfiguration config = getConfig();
		MOB_CAP = config.getInt( "mob-cap-per-apocalypse" );
		for ( String string : config.getConfigurationSection( "messages" ).getKeys( true ) ) {
			messages.put( string, config.getString( "messages." + string ) );
		}
		for ( String set : config.getConfigurationSection( "standard-rewards" ).getKeys( false ) ) {
			// TODO do this later
		}
	}
	
	private void loadFiles() {
		ApocalypseDeserializer.registerRandomRewards( new File( getDataFolder() + "/rewards" ) );
		ApocalypseDeserializer.registerSimpleSpawns( new File( getDataFolder() + "/spawns" ) );
		ApocalypseDeserializer.registerApocalypses( new File( getDataFolder() + "/apocalypses" ) );
	}

	@Override
	public void onDisable() {
		NGui.disable();
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
		
		Bukkit.getPluginManager().registerEvents( new ClickListener(), this );
	}
	
	public static String parse( CommandSender player, String input ) {
		String result = input;
		if ( placeholderAPI && player != null && player instanceof Player ) {
			result = ClipsPlaceholder.parse( ( Player ) player, result );
		}
		if ( mvdwPlaceholderAPI && player != null && player instanceof Player  ) {
			result = MvDWPlaceholder.parse( ( Player ) player, result );
		}
		return ChatColor.translateAlternateColorCodes( '&', result );
	}
	
	public static String parse( String key, CommandSender player, String... args ) {
		String message = parse( player, messages.get( key ) );
		for ( int i = 0; i < args.length; i++ ) {
			message = message.replace( "%" + i, args[ i ] );
		}
		return message;
	}
	
	public static Logger getConsoleLogger() {
		return LOGGER;
	}
}
