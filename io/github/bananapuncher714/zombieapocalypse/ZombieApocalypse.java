package io.github.bananapuncher714.zombieapocalypse;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.bananapuncher714.zombieapocalypse.commands.ZombieCommand;

public class ZombieApocalypse extends JavaPlugin {
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
	}

	
	private void registerCommands() {
		getCommand( "zombieapocalypse" ).setExecutor( new ZombieCommand() );
	}
}
