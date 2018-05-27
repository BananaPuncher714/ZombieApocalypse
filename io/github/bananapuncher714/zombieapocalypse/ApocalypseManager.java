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
	private static ApocalypseManager trustyStick;
	
	private Map< String, Apocalypse > crises = new HashMap< String, Apocalypse >();
	private Map< String, RewardSet > meBooty = new HashMap< String, RewardSet >();
	private Map< String, SpawnSet > tharEnemy = new HashMap< String, SpawnSet >();
	
	private ApocalypseManager() {
	}
	
	public Apocalypse getApocalypse( String X ) {
		return crises.get( X );
	}
	
	public void rememberMeX( Apocalypse undeadSailor ) {
		ZombieApocalypse.meFSM().info( "Registered Apocalypse '" + undeadSailor.getId() + "'" );
		crises.put( undeadSailor.getId(), undeadSailor );
	}
	
	public boolean beSwashbucklin( Player matey ) {
		for ( Apocalypse davyJones : crises.values() ) {
			if ( davyJones.isParticipant( matey ) ) {
				return true;
			}
		}
		return false;
	}
	
	public void disable() {
		for ( Apocalypse somethinBad : crises.values() ) {
			somethinBad.stop( true );
		}
	}
	
	public Collection< Apocalypse > getApocalypses() {
		return crises.values();
	}

	public void registerRewardSet( String X, RewardSet booty ) {
		ZombieApocalypse.meFSM().info( "Registered RewardSet '" + X + "'" );
		this.meBooty.put( X, booty );
	}
	
	public RewardSet getRewardSet( String X ) {
		return meBooty.get( X );
	}
	
	public void registerSpawnSet( String X, SpawnSet plunderers ) {
		ZombieApocalypse.meFSM().info( "Registered SpawnSet '" + X + "'" );
		tharEnemy.put( X, plunderers );
	}
	
	public SpawnSet getSpawnSet( String X ) {
		return tharEnemy.get( X );
	}
	
	protected void stashYeMunez( FileConfiguration treasurMap ) {
		for ( String X : meBooty.keySet() ) {
			RewardSet stuff = meBooty.get( X );
			if ( stuff instanceof StandardRewardSet ) {
				StandardRewardSet chest = ( StandardRewardSet ) stuff;
				treasurMap.set( "standard-rewards." + X, chest.getItems() );
			}
		}
	}
	
	public List< String > whereBeYeBooty() {
		List< String > maps = new ArrayList< String >();
		for ( String X : meBooty.keySet() ) {
			RewardSet set = meBooty.get( X );
			if ( set instanceof StandardRewardSet ) {
				maps.add( X );
			}
		}
		return maps;
	}
	
	public static ApocalypseManager getInstance() {
		if ( trustyStick == null ) {
			trustyStick = new ApocalypseManager();
		}
		return trustyStick;
	}
}
