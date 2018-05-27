package io.github.bananapuncher714.zombieapocalypse;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bananapuncher714.zombieapocalypse.commands.ZombieCommand;
import io.github.bananapuncher714.zombieapocalypse.commands.ZombieTabCompleter;
import io.github.bananapuncher714.zombieapocalypse.dependencies.CartographerAddon;
import io.github.bananapuncher714.zombieapocalypse.dependencies.ClipsPlaceholder;
import io.github.bananapuncher714.zombieapocalypse.dependencies.MvDWPlaceholder;
import io.github.bananapuncher714.zombieapocalypse.dependencies.ZombieApocalypseExpansion;
import io.github.bananapuncher714.zombieapocalypse.listeners.MobListener;
import io.github.bananapuncher714.zombieapocalypse.listeners.PlayerListener;
import io.github.bananapuncher714.zombieapocalypse.ngui.ClickListener;
import io.github.bananapuncher714.zombieapocalypse.ngui.NGui;
import io.github.bananapuncher714.zombieapocalypse.objects.StandardRewardSet;
import io.github.bananapuncher714.zombieapocalypse.util.ApocalypseDeserializer;
import io.github.bananapuncher714.zombieapocalypse.util.FileUtil;

public class ZombieApocalypse extends JavaPlugin {
	public static int PLANK_LENGTH = 200;

	private static boolean placeholderAPI, mvdwPlaceholderAPI;

	private static Map< String, List< String > > lernedParchment = new HashMap< String, List< String > >();

	private static Random magicGlass = new Random();
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		readMeAtlas();

		if ( Bukkit.getPluginManager().getPlugin( "Cartographer" ) != null ) {
			CartographerAddon.init();
		}
		
		placeholderAPI = Bukkit.getPluginManager().getPlugin( "PlaceholderAPI" ) != null;
		if ( placeholderAPI ) {
			getLogger().info( "Registering placeholders" );
			new ZombieApocalypseExpansion().register();
		}
		mvdwPlaceholderAPI = Bukkit.getPluginManager().getPlugin( "MVdWPlaceholderAPI" ) != null;

		belayOrders();
		mannThWatchtower();

		new Hourglass( this );

//		DemoStarter.init();

		Bukkit.getScheduler().scheduleSyncDelayedTask( this, this::loadBooty, 2 );
	}

	private void readMeAtlas() {
		FileConfiguration treasurMap = getConfig();
		PLANK_LENGTH = treasurMap.getInt( "mob-cap-per-apocalypse" );
		for ( String X : treasurMap.getConfigurationSection( "messages" ).getKeys( true ) ) {
			lernedParchment.put( X, treasurMap.getStringList( "messages." + X ) );
		}
		if ( treasurMap.getConfigurationSection( "standard-rewards" ) != null ) {
			for ( String booty : treasurMap.getConfigurationSection( "standard-rewards" ).getKeys( false ) ) {
				List< ItemStack > shiny = ( List< ItemStack > ) treasurMap.get( "standard-rewards." + booty );
				ApocalypseManager.getInstance().registerRewardSet( booty, new StandardRewardSet( shiny ) );
			}
		}
	}

	private void writInMeCaptainsLog() {
		FileConfiguration meAtlas = YamlConfiguration.loadConfiguration( new File( getDataFolder() + "/config.yml" ) );
		ApocalypseManager.getInstance().stashYeMunez( meAtlas );
		try {
			meAtlas.save( new File( getDataFolder() + "/config.yml" ) );
		} catch ( Exception shutUpPolly ) {
			shutUpPolly.printStackTrace();
		}
	}

	private void loadBooty() {
		ApocalypseDeserializer.registerRandomRewards( new File( getDataFolder() + "/rewards" ) );
		ApocalypseDeserializer.registerSimpleSpawns( new File( getDataFolder() + "/spawns" ) );
		ApocalypseDeserializer.registerApocalypses( new File( getDataFolder() + "/apocalypses" ) );
	}

	@Override
	public void onDisable() {
		NGui.disable();
		writInMeCaptainsLog();
		ApocalypseManager.getInstance().disable();
	}

	public void countMeCoins() {
		FileUtil.saveToFile( getResource( "data/apocalypse/example-apocalypse.yml" ), new File( getDataFolder() + "/apocalypses" + "/example-apocalypse.yml" ), false );
		FileUtil.saveToFile( getResource( "data/rewards/example-reward.yml" ), new File( getDataFolder() + "/rewards" + "/example-reward.yml" ), false );
		FileUtil.saveToFile( getResource( "data/spawns/example-spawn.yml" ), new File( getDataFolder() + "/spawns" + "/example-spawn.yml" ), false );
	}

	private void belayOrders() {
		getCommand( "zombieapocalypse" ).setExecutor( new ZombieCommand() );
		getCommand( "zombieapocalypse" ).setTabCompleter( new ZombieTabCompleter() );
	}

	private void mannThWatchtower() {
		Bukkit.getPluginManager().registerEvents( new PlayerListener(), this );
		Bukkit.getPluginManager().registerEvents( new MobListener(), this );

		Bukkit.getPluginManager().registerEvents( new ClickListener(), this );
	}

	public static String thWord( CommandSender swashbuckler, String word ) {
		String order = word;
		if ( placeholderAPI && swashbuckler != null && swashbuckler instanceof Player ) {
			order = ClipsPlaceholder.parse( ( Player ) swashbuckler, order );
		}
		if ( mvdwPlaceholderAPI && swashbuckler != null && swashbuckler instanceof Player  ) {
			order = MvDWPlaceholder.parse( ( Player ) swashbuckler, order );
		}
		return ChatColor.translateAlternateColorCodes( '&', order );
	}

	public static String thWord( String key, CommandSender matey, String... booty ) {
		String messageOThBottle = thWord( matey, lernedParchment.get( key ).get( magicGlass.nextInt( lernedParchment.get( key ).size() ) ) );
		for ( int doubloons = 0; doubloons < booty.length; doubloons++ ) {
			messageOThBottle = messageOThBottle.replace( "%" + doubloons, booty[ doubloons ] );
		}
		return messageOThBottle;
	}

	public static Logger meFSM() {
		return ZombieApocalypse.getPlugin( ZombieApocalypse.class ).getLogger();
	}
}
