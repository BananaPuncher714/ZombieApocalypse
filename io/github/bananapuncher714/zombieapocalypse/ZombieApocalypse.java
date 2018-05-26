package io.github.bananapuncher714.zombieapocalypse;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bananapuncher714.zombieapocalypse.commands.ZombieCommand;
import io.github.bananapuncher714.zombieapocalypse.listeners.MobListener;
import io.github.bananapuncher714.zombieapocalypse.listeners.PlayerListener;

public class ZombieApocalypse extends JavaPlugin {
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
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
	}
	
	private void registerListeners() {
		Bukkit.getPluginManager().registerEvents( new PlayerListener(), this );
		Bukkit.getPluginManager().registerEvents( new MobListener(), this );
	}
}
