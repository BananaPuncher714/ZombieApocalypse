package io.github.bananapuncher714.zombieapocalypse.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bananapuncher714.zombieapocalypse.ApocalypseManager;
import io.github.bananapuncher714.zombieapocalypse.ZombieApocalypse;
import io.github.bananapuncher714.zombieapocalypse.ZombiePerms;
import io.github.bananapuncher714.zombieapocalypse.inventory.RewardEditorHolder;
import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;
import io.github.bananapuncher714.zombieapocalypse.objects.RewardSet;
import io.github.bananapuncher714.zombieapocalypse.objects.StandardRewardSet;

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
			} else if ( args[ 0 ].equalsIgnoreCase( "open" ) ) {
				open( sender, args );
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
		if ( !ZombiePerms.canStartAndStop( sender ) ) {
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
		if ( !ZombiePerms.canStartAndStop( sender ) ) {
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
		if ( !ZombiePerms.canStartAndStop( sender ) ) {
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
	
	private void open( CommandSender sender, String[] args ) {
		if ( !ZombiePerms.isAdmin( sender ) ) {
			sender.sendMessage( ZombieApocalypse.parse( "command.no-permission", sender ) );
			return;
		}
		if ( !( sender instanceof Player ) ) {
			sender.sendMessage( ZombieApocalypse.parse( "command.must-be-player", sender ) );
			return;
		}
		if ( args.length < 2 ) {
			sender.sendMessage( ZombieApocalypse.parse( "command.arguments-required", sender ) );
			return;
		}
		RewardSet set = ApocalypseManager.getInstance().getRewardSet( args[ 1 ] );
		if ( set != null && !( set instanceof StandardRewardSet ) ) {
			sender.sendMessage( ZombieApocalypse.parse( "command.invalid-reward-set", sender ) );
			return;
		}
		if ( set == null ) {
			set = new StandardRewardSet( new ArrayList< ItemStack >() );
			ApocalypseManager.getInstance().registerRewardSet( args[ 1 ], set );
		}
		Player player = ( Player ) sender;
		player.openInventory( new RewardEditorHolder( args[ 1 ], ( StandardRewardSet ) set ).getInventory() );
	}
}
