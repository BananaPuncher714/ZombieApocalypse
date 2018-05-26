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
			sender.sendMessage( ZombieApocalypse.parse( "command.no-permission", sender ) );
			return;
		}
		List< Apocalypse > starts = new ArrayList< Apocalypse >();
		if ( args.length > 1 ) {
			Apocalypse apocalypse = ApocalypseManager.getInstance().getApocalypse( args[ 1 ] );
			if ( apocalypse != null ) {
				starts.add( apocalypse );
			} else {
				sender.sendMessage( ZombieApocalypse.parse( "command.invalid-apocalypse", sender ) );
				return;
			}
		} else {
			if ( !( sender instanceof Player ) ) {
				sender.sendMessage( ZombieApocalypse.parse( "command.apocalypse-required", sender ) );
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
		
		if ( starts.isEmpty() ) {
			sender.sendMessage( ZombieApocalypse.parse( "command.no-apocalypses-started", sender ) );
			return;
		}
		
		sender.sendMessage( ZombieApocalypse.parse( "command.starting-apocalypses", sender ) );
		for ( Apocalypse apocalypse : starts ) {
			if ( !apocalypse.isRunning() ) {
				apocalypse.start();
				sender.sendMessage( ZombieApocalypse.parse( "command.started-apocalypse", sender, apocalypse.getId() ) );
			}
		}
		sender.sendMessage( ZombieApocalypse.parse( "command.done-starting-apocalypses", sender ) );
	}
	
	private void stop( CommandSender sender, String[] args ) {
		if ( !ZombiePerms.isAdmin( sender ) ) {
			sender.sendMessage( ZombieApocalypse.parse( "command.no-permission", sender ) );
			return;
		}
		List< Apocalypse > starts = new ArrayList< Apocalypse >();
		if ( args.length > 1 ) {
			Apocalypse apocalypse = ApocalypseManager.getInstance().getApocalypse( args[ 1 ] );
			if ( apocalypse != null ) {
				starts.add( apocalypse );
			} else {
				sender.sendMessage( ZombieApocalypse.parse( "command.invalid-apocalypse", sender ) );
				return;
			}
		} else {
			if ( !( sender instanceof Player ) ) {
				sender.sendMessage( ZombieApocalypse.parse( "command.apocalypse-required", sender ) );
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
		
		if ( starts.isEmpty() ) {
			sender.sendMessage( ZombieApocalypse.parse( "command.no-apocalypses-stopped", sender ) );
			return;
		}
		
		sender.sendMessage( ZombieApocalypse.parse( "command.stopping-apocalypses", sender ) );
		for ( Apocalypse apocalypse : starts ) {
			if ( apocalypse.isRunning() ) {
				apocalypse.stop( true );
				sender.sendMessage( ZombieApocalypse.parse( "command.stopped-apocalypse-naturally", sender, apocalypse.getId() ) );
			}
		}
		sender.sendMessage( ZombieApocalypse.parse( "command.done-stopping-apocalypses", sender ) );
	}
	
	private void end( CommandSender sender, String[] args ) {
		if ( !ZombiePerms.isAdmin( sender ) ) {
			sender.sendMessage( ZombieApocalypse.parse( "command.no-permission", sender ) );
			return;
		}
		if ( args.length < 2 ) {
			sender.sendMessage( ZombieApocalypse.parse( "command.apocalypse-required", sender ) );
			return;
		}
		Apocalypse apocalypse = ApocalypseManager.getInstance().getApocalypse( args[ 1 ] );
		if ( apocalypse == null ) {
			sender.sendMessage( ZombieApocalypse.parse( "command.invalid-apocalypse", sender ) );
			return;
		}
		apocalypse.stop( false );
		sender.sendMessage( ZombieApocalypse.parse( "stopped-apocalypse-forcefully", sender ) );
	}
	
	private void save( CommandSender sender ) {
		if ( !ZombiePerms.isAdmin( sender ) ) {
			sender.sendMessage( ZombieApocalypse.parse( "command.no-permission", sender ) );
			return;
		}
		ZombieApocalypse.getPlugin( ZombieApocalypse.class ).saveResources();
		sender.sendMessage( ZombieApocalypse.parse( "command.saved-examples", sender ) );
	}
}
