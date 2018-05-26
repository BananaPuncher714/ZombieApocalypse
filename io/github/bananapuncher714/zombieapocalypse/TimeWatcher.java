package io.github.bananapuncher714.zombieapocalypse;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;

import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;

public class TimeWatcher {
	private static boolean STATE = true;
	
	ZombieApocalypse plugin;
	ApocalypseManager manager = ApocalypseManager.getInstance();
	Random rand = ThreadLocalRandom.current();
	
	public TimeWatcher( ZombieApocalypse plugin ) {
		this.plugin = plugin;
		Bukkit.getScheduler().scheduleSyncRepeatingTask( plugin, this::update, 1, 20 );
	}
	
	private void update() {
		if ( !STATE ) {
			return;
		}
		for ( Apocalypse apocalypse : manager.getApocalypses() ) {
			long worldTime = apocalypse.getWorld().getTime() % 24000;
			if ( worldTime > apocalypse.getStart() && worldTime <= apocalypse.getStart() + 20 ) {
				if ( apocalypse.isRunning() ) {
					continue;
				}
				if ( rand.nextDouble() > apocalypse.getChance() ) {
					continue;
				}
				// Start the apocalypse
				apocalypse.start();
			} else if ( worldTime > apocalypse.getEnd() && worldTime <= apocalypse.getEnd() + 100 ) {
				// Stop the apocalypse
				if ( !apocalypse.isRunning() ) {
					continue;
				}
				apocalypse.stop( false );
			}
			
		}
	}
	
	public static void setState( boolean state ) {
		STATE = state;
	}
}
