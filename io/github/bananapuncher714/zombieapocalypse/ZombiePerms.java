package io.github.bananapuncher714.zombieapocalypse;

import org.bukkit.permissions.Permissible;

public class ZombiePerms {
	public static boolean isAdmin( Permissible user ) {
		return user.hasPermission( "zombieapocalypse.admin" );
	}
}
