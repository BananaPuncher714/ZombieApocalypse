package io.github.bananapuncher714.zombieapocalypse.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class Apocalypse {
	World world;
	int timeStart, timeEnd;
	double possiblity = .2;
	double killPercentRequired = .5;
	Set< UUID > participants = new HashSet< UUID >();
	Map< SpawnSet, Integer > spawns = new HashMap< SpawnSet, Integer >();
	Map< RewardSet, Integer > rewards = new HashMap< RewardSet, Integer >();
	
	public Apocalypse( World world ) {
		this.world = world;
	}
	
	public void setStartAndStop( int start, int stop ) {
		timeStart = start % 24000;
		timeEnd = stop % 24000;
	}
	
	public World getWorld() {
		return world;
	}
	
	public int getStart() {
		return timeStart;
	}
	
	public int getEnd() {
		return timeEnd;
	}
	
	public double getChance() {
		return possiblity;
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
	
	public void start() {
		
	}
	
	public void stop() {
		
	}
}
