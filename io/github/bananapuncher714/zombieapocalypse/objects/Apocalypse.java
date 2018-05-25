package io.github.bananapuncher714.zombieapocalypse.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class Apocalypse {
	World world;
	double possiblity = .2;
	double killPercentRequired = .5;
	Set< UUID > participants = new HashSet< UUID >();
	Map< SpawnSet, Integer > spawns = new HashMap< SpawnSet, Integer >();
	
	public Apocalypse( World world ) {
		this.world = world;
	}
	
	public void removePlayer( Player player ) {
		participants.remove( player.getUniqueId() );
	}
	
	public void addPlayer( Player player ) {
		participants.add( player.getUniqueId() );
	}
	
	public boolean isParticipant( Player player ) {
		return participants.contains( player.getUniqueId() );
	}
}
