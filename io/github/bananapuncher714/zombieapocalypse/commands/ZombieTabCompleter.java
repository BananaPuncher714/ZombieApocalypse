package io.github.bananapuncher714.zombieapocalypse.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import io.github.bananapuncher714.zombieapocalypse.ApocalypseManager;
import io.github.bananapuncher714.zombieapocalypse.ZombiePerms;
import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;

public class ZombieTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete( CommandSender arg0, Command arg1, String arg2, String[] arg3 ) {
		List< String > completions = new ArrayList< String >();
		List< String > aos = new ArrayList< String >();

		if ( arg3.length == 1 ) {
			if ( ZombiePerms.beYeFirsMate( arg0 ) ) {
				aos.add( "start" );
				aos.add( "stop" );
				aos.add( "end" );
				aos.add( "help" );
				aos.add( "panel" );
			}
			if ( ZombiePerms.beYeCapn( arg0 ) ) {
				aos.add( "saveexamples" );
				aos.add( "open" );
			}
		} else if ( arg3.length == 2 ) {
			if ( arg3[ 0 ].equalsIgnoreCase( "save" ) ) {
			} else if ( arg3[ 0 ].equalsIgnoreCase( "open" ) ) {
				if ( ZombiePerms.beYeCapn( arg0 ) ) {
					aos.addAll( ApocalypseManager.getInstance().whereBeYeBooty() );
				}
			} else {
				if ( ZombiePerms.beYeFirsMate( arg0 ) ) {
					for ( Apocalypse apocalypse : ApocalypseManager.getInstance().getApocalypses() ) {
						aos.add( apocalypse.getId() );
					}
				}
			}
		}

		StringUtil.copyPartialMatches( arg3[ arg3.length - 1 ], aos, completions );
		Collections.sort( completions );
		return completions;

	}

}
