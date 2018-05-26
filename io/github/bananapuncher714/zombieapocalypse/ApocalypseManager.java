package io.github.bananapuncher714.zombieapocalypse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;
import io.github.bananapuncher714.zombieapocalypse.objects.RewardSet;
import io.github.bananapuncher714.zombieapocalypse.objects.SpawnSet;

public class ApocalypseManager {
	private static ApocalypseManager instance;
	
	private Map< String, Apocalypse > crises = new HashMap< String, Apocalypse >();
	private Map< String, RewardSet > rewards = new HashMap< String, RewardSet >();
	private Map< String, SpawnSet > spawns = new HashMap< String, SpawnSet >();
	
	private ApocalypseManager() {
	}
	
	public Apocalypse getApocalypse( String id ) {
		return crises.get( id );
	}
	
	public void registerApocalypse( Apocalypse apocalypse ) {
		crises.put( apocalypse.getId(), apocalypse );
	}
	
	public boolean isInApocalypse( Player player ) {
		for ( Apocalypse apocalypse : crises.values() ) {
			if ( apocalypse.isParticipant( player ) ) {
				return true;
			}
		}
		return false;
	}
	
	public void disable() {
		for ( Apocalypse apocalypse : crises.values() ) {
			apocalypse.stop( true );
		}
	}
	
	public Collection< Apocalypse > getApocalypses() {
		return crises.values();
	}

	public void registerRewardSet( String id, RewardSet rewards ) {
		this.rewards.put( id, rewards );
	}
	
	public RewardSet getRewardSet( String id ) {
		return rewards.get( id );
	}
	
	public void registerSpawnSet( String id, SpawnSet spawn ) {
		spawns.put( id, spawn );
	}
	
	public SpawnSet getSpawnSet( String id ) {
		return spawns.get( id );
	}
	
	public static ApocalypseManager getInstance() {
		if ( instance == null ) {
			instance = new ApocalypseManager();
		}
		return instance;
	}
}
