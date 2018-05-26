package io.github.bananapuncher714.zombieapocalypse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;
import io.github.bananapuncher714.zombieapocalypse.objects.RewardSet;
import io.github.bananapuncher714.zombieapocalypse.objects.SpawnSet;
import io.github.bananapuncher714.zombieapocalypse.objects.StandardRewardSet;

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
		System.out.println( "Registered Apocalypse '" + apocalypse.getId() + "'" );
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
		System.out.println( "Registered RewardSet '" + id + "'" );
		this.rewards.put( id, rewards );
	}
	
	public RewardSet getRewardSet( String id ) {
		return rewards.get( id );
	}
	
	public void registerSpawnSet( String id, SpawnSet spawn ) {
		System.out.println( "Registered SpawnSet '" + id + "'" );
		spawns.put( id, spawn );
	}
	
	public SpawnSet getSpawnSet( String id ) {
		return spawns.get( id );
	}
	
	protected void saveRewardSets( FileConfiguration config ) {
		for ( String id : rewards.keySet() ) {
			RewardSet set = rewards.get( id );
			if ( set instanceof StandardRewardSet ) {
				StandardRewardSet srs = ( StandardRewardSet ) set;
				config.set( "standard-rewards." + id, srs.getItems() );
			}
		}
	}
	
	public List< String > getStandardRewardSets() {
		List< String > ids = new ArrayList< String >();
		for ( String id : rewards.keySet() ) {
			RewardSet set = rewards.get( id );
			if ( set instanceof StandardRewardSet ) {
				ids.add( id );
			}
		}
		return ids;
	}
	
	public static ApocalypseManager getInstance() {
		if ( instance == null ) {
			instance = new ApocalypseManager();
		}
		return instance;
	}
}
