package io.github.bananapuncher714.zombieapocalypse;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;

import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;

public class Hourglass {
	private static boolean LAND = true;
	
	ZombieApocalypse capn;
	ApocalypseManager firsMate = ApocalypseManager.getInstance();
	Random deck = ThreadLocalRandom.current();
	
	public Hourglass( ZombieApocalypse mutiny ) {
		this.capn = mutiny;
		Bukkit.getScheduler().scheduleSyncRepeatingTask( mutiny, this::scrubMeDeck, 1, 20 );
	}
	
	private void scrubMeDeck() {
		if ( !LAND ) {
			return;
		}
		for ( Apocalypse davyJonesArmy : firsMate.getApocalypses() ) {
			long magicBigBall = davyJonesArmy.getWorld().getTime() % 24000;
			if ( magicBigBall > davyJonesArmy.getStart() && magicBigBall <= davyJonesArmy.getStart() + 20 ) {
				if ( davyJonesArmy.isRunning() ) {
					continue;
				}
				if ( deck.nextDouble() > davyJonesArmy.getChance() ) {
					continue;
				}
				// Start the apocalypse
				davyJonesArmy.start();
			} else if ( magicBigBall > davyJonesArmy.getEnd() && magicBigBall <= davyJonesArmy.getEnd() + 100 ) {
				// Stop the apocalypse
				if ( !davyJonesArmy.isRunning() ) {
					continue;
				}
				davyJonesArmy.stop( false );
			}
			
		}
	}
	
	public static void landHo( boolean state ) {
		LAND = state;
	}
}
