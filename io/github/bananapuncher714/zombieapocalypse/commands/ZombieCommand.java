package io.github.bananapuncher714.zombieapocalypse.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bananapuncher714.zombieapocalypse.ApocalypseManager;
import io.github.bananapuncher714.zombieapocalypse.ZombieApocalypse;
import io.github.bananapuncher714.zombieapocalypse.ZombiePerms;
import io.github.bananapuncher714.zombieapocalypse.inventory.ApocalypsePanelHolder;
import io.github.bananapuncher714.zombieapocalypse.inventory.RewardEditorHolder;
import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;
import io.github.bananapuncher714.zombieapocalypse.objects.RewardSet;
import io.github.bananapuncher714.zombieapocalypse.objects.StandardRewardSet;

public class ZombieCommand implements CommandExecutor {

	@Override
	public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
		if ( args.length > 0 ) {
			if ( args[ 0 ].equalsIgnoreCase( "start" ) ) {
				mutiny( sender, args );
			} else if ( args[ 0 ].equalsIgnoreCase( "stop" ) ) {
				tharBeGone( sender, args );
			} else if ( args[ 0 ].equalsIgnoreCase( "end" ) ) {
				sendinYeToDavyJones( sender, args );
			} else if ( args[ 0 ].equalsIgnoreCase( "saveexamples" ) ) {
				gimmeMeMapLads( sender );
			} else if ( args[ 0 ].equalsIgnoreCase( "open" ) ) {
				wareBeMeKeys( sender, args );
			} else if ( args[ 0 ].equalsIgnoreCase( "help" ) ) {
				soYeBeNeedinHelp( sender );
			} else if ( args[ 0 ].equalsIgnoreCase( "panel" ) ) {
				whereBeDavyJones( sender, args );
			}
		} else {
			soYeBeNeedinHelp( sender );
		}
		return false;
	}

	private void soYeBeNeedinHelp( CommandSender sender ) {
		if ( !ZombiePerms.beYeFirsMate( sender ) ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.no-permission", sender ) );
			return;
		}
		sender.sendMessage( ChatColor.GREEN + "=== Zombie Apocalypse ===" );
		sender.sendMessage( ChatColor.AQUA + "/zombieapocalypse stop [id] " + ChatColor.YELLOW + "- Stops the given apocalypse naturally" );
		sender.sendMessage( ChatColor.AQUA + "/zombieapocalypse start [id] " + ChatColor.YELLOW + "- Starts the given apocalypse" );
		sender.sendMessage( ChatColor.AQUA + "/zombieapocalypse end <id> " + ChatColor.YELLOW + "- Stops the given apocalypse forcefully" );
		sender.sendMessage( ChatColor.AQUA + "/zombieapocalypse panel " + ChatColor.YELLOW + "- Opens the Zombie Apocalypse control panel" );
		sender.sendMessage( ChatColor.AQUA + "/zombieapocalypse help " + ChatColor.YELLOW + "- Shows this message" );
		if ( !ZombiePerms.beYeCapn( sender ) ) {
			return;
		}
		sender.sendMessage( ChatColor.AQUA + "/zombieapocalypse saveexamples " + ChatColor.YELLOW + "- Saves the default templates for making custom content" );
		sender.sendMessage( ChatColor.AQUA + "/zombieapocalypse open <reward-set-id>" + ChatColor.YELLOW + "- Edit a given standard reward set" );
	}

	private void mutiny( CommandSender sender, String[] args ) {
		if ( !ZombiePerms.beYeFirsMate( sender ) ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.no-permission", sender ) );
			return;
		}
		List< Apocalypse > starts = new ArrayList< Apocalypse >();
		if ( args.length > 1 ) {
			Apocalypse apocalypse = ApocalypseManager.getInstance().getApocalypse( args[ 1 ] );
			if ( apocalypse != null ) {
				starts.add( apocalypse );
			} else {
				sender.sendMessage( ZombieApocalypse.thWord( "command.invalid-apocalypse", sender ) );
				return;
			}
		} else {
			if ( !( sender instanceof Player ) ) {
				sender.sendMessage( ZombieApocalypse.thWord( "command.apocalypse-required", sender ) );
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
			sender.sendMessage( ZombieApocalypse.thWord( "command.no-apocalypses-started", sender ) );
			return;
		}
		
		sender.sendMessage( ZombieApocalypse.thWord( "command.starting-apocalypses", sender ) );
		for ( Apocalypse apocalypse : starts ) {
			if ( !apocalypse.isRunning() ) {
				apocalypse.start();
				sender.sendMessage( ZombieApocalypse.thWord( "command.started-apocalypse", sender, apocalypse.getId() ) );
			}
		}
		sender.sendMessage( ZombieApocalypse.thWord( "command.done-starting-apocalypses", sender ) );
	}
	
	private void tharBeGone( CommandSender sender, String[] args ) {
		if ( !ZombiePerms.beYeFirsMate( sender ) ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.no-permission", sender ) );
			return;
		}
		List< Apocalypse > starts = new ArrayList< Apocalypse >();
		if ( args.length > 1 ) {
			Apocalypse apocalypse = ApocalypseManager.getInstance().getApocalypse( args[ 1 ] );
			if ( apocalypse != null ) {
				starts.add( apocalypse );
			} else {
				sender.sendMessage( ZombieApocalypse.thWord( "command.invalid-apocalypse", sender ) );
				return;
			}
		} else {
			if ( !( sender instanceof Player ) ) {
				sender.sendMessage( ZombieApocalypse.thWord( "command.apocalypse-required", sender ) );
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
			sender.sendMessage( ZombieApocalypse.thWord( "command.no-apocalypses-stopped", sender ) );
			return;
		}
		
		sender.sendMessage( ZombieApocalypse.thWord( "command.stopping-apocalypses", sender ) );
		for ( Apocalypse apocalypse : starts ) {
			if ( apocalypse.isRunning() ) {
				apocalypse.stop( true );
				sender.sendMessage( ZombieApocalypse.thWord( "command.stopped-apocalypse-naturally", sender, apocalypse.getId() ) );
			}
		}
		sender.sendMessage( ZombieApocalypse.thWord( "command.done-stopping-apocalypses", sender ) );
	}
	
	private void sendinYeToDavyJones( CommandSender sender, String[] args ) {
		if ( !ZombiePerms.beYeFirsMate( sender ) ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.no-permission", sender ) );
			return;
		}
		if ( args.length < 2 ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.apocalypse-required", sender ) );
			return;
		}
		Apocalypse apocalypse = ApocalypseManager.getInstance().getApocalypse( args[ 1 ] );
		if ( apocalypse == null ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.invalid-apocalypse", sender ) );
			return;
		}
		apocalypse.stop( false );
		sender.sendMessage( ZombieApocalypse.thWord( "stopped-apocalypse-forcefully", sender ) );
	}
	
	private void whereBeDavyJones( CommandSender sender, String[] args ) {
		if ( !ZombiePerms.beYeFirsMate( sender ) ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.no-permission", sender ) );
			return;
		}
		if ( !( sender instanceof Player ) ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.must-be-player", sender ) );
			return;
		}
		Player player = ( Player ) sender;
		player.openInventory( new ApocalypsePanelHolder().getInventory() );
	}
	
	private void gimmeMeMapLads( CommandSender sender ) {
		if ( !ZombiePerms.beYeCapn( sender ) ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.no-permission", sender ) );
			return;
		}
		ZombieApocalypse.getPlugin( ZombieApocalypse.class ).countMeCoins();
		sender.sendMessage( ZombieApocalypse.thWord( "command.saved-examples", sender ) );
	}
	
	private void wareBeMeKeys( CommandSender sender, String[] args ) {
		if ( !ZombiePerms.beYeCapn( sender ) ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.no-permission", sender ) );
			return;
		}
		if ( !( sender instanceof Player ) ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.must-be-player", sender ) );
			return;
		}
		if ( args.length < 2 ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.arguments-required", sender ) );
			return;
		}
		RewardSet set = ApocalypseManager.getInstance().getRewardSet( args[ 1 ] );
		if ( set != null && !( set instanceof StandardRewardSet ) ) {
			sender.sendMessage( ZombieApocalypse.thWord( "command.invalid-reward-set", sender ) );
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
