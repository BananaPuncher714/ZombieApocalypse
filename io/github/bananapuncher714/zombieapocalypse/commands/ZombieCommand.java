package io.github.bananapuncher714.zombieapocalypse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ZombieCommand implements CommandExecutor {

	@Override
	public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
		if ( args.length == 0 ) {
			showHelp( sender );
		}
		return false;
	}

	private void showHelp( CommandSender sender ) {
		sender.sendMessage( "U n00b" );
	}
}
