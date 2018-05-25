package io.github.bananapuncher714.zombieapocalypse;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;

import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;

public class TimeWatcher {
	ZombieApocalypse plugin;
	ApocalypseManager manager = ApocalypseManager.getInstance();
	Random rand = ThreadLocalRandom.current();
	
	public TimeWatcher( ZombieApocalypse plugin ) {
		this.plugin = plugin;
		Bukkit.getScheduler().scheduleSyncRepeatingTask( plugin, this::update, 1, 20 );
	}
	
	private void update() {
		for ( Apocalypse apocalypse : manager.getApocalypses() ) {
			long worldTime = apocalypse.getWorld().getTime() % 24000;
			if ( worldTime > apocalypse.getStart() && worldTime <= apocalypse.getStart() + 20 ) {
				if ( rand.nextDouble() > apocalypse.getChance() ) {
					continue;
				}
				// Start the apocalypse
			} else if ( worldTime > apocalypse.getEnd() && worldTime <= apocalypse.getEnd() + 20 ) {
				// Stop the apocalypse
			}
			
		}
	}

}
