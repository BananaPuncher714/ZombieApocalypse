package io.github.bananapuncher714.zombieapocalypse.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.zombieapocalypse.ApocalypseManager;
import io.github.bananapuncher714.zombieapocalypse.ZombieApocalypse;
import io.github.bananapuncher714.zombieapocalypse.ZombiePerms;
import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;

public class ZombieCommand implements CommandExecutor {

	@Override
	public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
		if ( args.length > 0 ) {
			if ( args[ 0 ].equalsIgnoreCase( "start" ) ) {
				start( sender, args );
			} else if ( args[ 0 ].equalsIgnoreCase( "stop" ) ) {
				stop( sender, args );
			} else if ( args[ 0 ].equalsIgnoreCase( "end" ) ) {
				end( sender, args );
			} else if ( args[ 0 ].equalsIgnoreCase( "saveexamples" ) ) {
				save( sender );
			}
		} else {
			showHelp( sender );
		}
		return false;
	}

	private void showHelp( CommandSender sender ) {
		sender.sendMessage( "U n00b" );
	}

	private void start( CommandSender sender, String[] args ) {
		if ( !ZombiePerms.isAdmin( sender ) ) {
			sender.sendMessage( "You do not have permission to run this command!" );
			return;
		}
		List< Apocalypse > starts = new ArrayList< Apocalypse >();
		if ( args.length > 1 ) {
			Apocalypse apocalypse = ApocalypseManager.getInstance().getApocalypse( args[ 1 ] );
			if ( apocalypse != null ) {
				starts.add( apocalypse );
			} else {
				sender.sendMessage( "Invalid apocalypse!" );
				return;
			}
		} else {
			if ( !( sender instanceof Player ) ) {
				sender.sendMessage( "Apocalypse name required for stuff!" );
				return;
			} else {
				Player player = ( Player ) sender;
				for ( Apocalypse apocalypse : ApocalypseManager.getInstance().getApocalypses() ) {
					if ( apocalypse.getWorld() == player.getWorld() ) {
						starts.add( apocalypse );
					}
				}
			}
		}
		
		sender.sendMessage( "Starting apocalypses..." );
		for ( Apocalypse apocalypse : starts ) {
			if ( !apocalypse.isRunning() ) {
				apocalypse.start();
				sender.sendMessage( "Started apocalypse '" + apocalypse.getId() + "'" );
			}
		}
		sender.sendMessage( "Done starting apocalypses!" );
	}
	
	private void stop( CommandSender sender, String[] args ) {
		if ( !ZombiePerms.isAdmin( sender ) ) {
			sender.sendMessage( "You do not have permission to run this command!" );
			return;
		}
		List< Apocalypse > starts = new ArrayList< Apocalypse >();
		if ( args.length > 1 ) {
			Apocalypse apocalypse = ApocalypseManager.getInstance().getApocalypse( args[ 1 ] );
			if ( apocalypse != null ) {
				starts.add( apocalypse );
			} else {
				sender.sendMessage( "Invalid apocalypse!" );
				return;
			}
		} else {
			if ( !( sender instanceof Player ) ) {
				sender.sendMessage( "Apocalypse name required for stuff!" );
				return;
			} else {
				Player player = ( Player ) sender;
				for ( Apocalypse apocalypse : ApocalypseManager.getInstance().getApocalypses() ) {
					if ( apocalypse.getWorld() == player.getWorld() ) {
						starts.add( apocalypse );
					}
				}
			}
		}
		
		sender.sendMessage( "Stopped apocalypses..." );
		for ( Apocalypse apocalypse : starts ) {
			if ( apocalypse.isRunning() ) {
				apocalypse.stop( true );
				sender.sendMessage( "Stopped apocalypse '" + apocalypse.getId() + "' naturally" );
			}
		}
		sender.sendMessage( "Done Stopped apocalypses!" );
	}
	
	private void end( CommandSender sender, String[] args ) {
		if ( !ZombiePerms.isAdmin( sender ) ) {
			sender.sendMessage( "You do not have permission to run this command!" );
			return;
		}
		if ( args.length < 2 ) {
			sender.sendMessage( "Must provide apocalypse name!" );
			return;
		}
		Apocalypse apocalypse = ApocalypseManager.getInstance().getApocalypse( args[ 1 ] );
		if ( apocalypse == null ) {
			sender.sendMessage( "Invalid apocalypse!" );
			return;
		}
		apocalypse.stop( false );
		sender.sendMessage( "Stopped '" + apocalypse + "' forcefully" );
	}
	
	private void save( CommandSender sender ) {
		if ( !ZombiePerms.isAdmin( sender ) ) {
			sender.sendMessage( "You do not have permission to run this command!" );
			return;
		}
		ZombieApocalypse.getPlugin( ZombieApocalypse.class ).saveResources();
	}
}
