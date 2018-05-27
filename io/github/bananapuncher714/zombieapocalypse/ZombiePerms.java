package io.github.bananapuncher714.zombieapocalypse;

import org.bukkit.permissions.Permissible;

public class ZombiePerms {
	public static boolean beYeCapn( Permissible matey ) {
		return matey.hasPermission( "zombieapocalypse.admin" );
	}
	
	public static boolean beYeFirsMate( Permissible matey ) {
		return matey.hasPermission( "zombieapocalypse.manage" ) || beYeCapn( matey );
	}
}
