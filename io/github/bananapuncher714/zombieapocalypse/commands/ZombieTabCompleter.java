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
		
		if ( !ZombiePerms.isAdmin( arg0 ) ) {
			return completions;
		}
		if ( arg3.length == 1 ) {
			aos.add( "start" );
			aos.add( "stop" );
			aos.add( "end" );
			aos.add( "saveexamples" );
		} else if ( arg3.length == 2 ) {
			for ( Apocalypse apocalypse : ApocalypseManager.getInstance().getApocalypses() ) {
				aos.add( apocalypse.getId() );
			}
		}
		
		StringUtil.copyPartialMatches( arg3[ arg3.length - 1 ], aos, completions );
		Collections.sort( completions );
		return completions;
		
	}

}
