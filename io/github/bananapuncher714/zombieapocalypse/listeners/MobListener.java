package io.github.bananapuncher714.zombieapocalypse.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import io.github.bananapuncher714.zombieapocalypse.ApocalypseManager;
import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;

public class MobListener implements Listener {
	ApocalypseManager manager = ApocalypseManager.getInstance();
	
	@EventHandler
	public void onPlayerDeathEvent( PlayerDeathEvent event ) {
		for ( Apocalypse apocalypse : manager.getApocalypses() ) {
			if ( !apocalypse.isRunning() || apocalypse.isParticipant( event.getEntity() ) ) {
				continue;
			}
			apocalypse.onPlayerDeathEvent( event );
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent( PlayerQuitEvent event ) {
		for ( Apocalypse apocalypse : manager.getApocalypses() ) {
			if ( !apocalypse.isRunning() || apocalypse.isParticipant( event.getPlayer() ) ) {
				continue;
			}
			apocalypse.onPlayerQuitEvent( event );
		}
	}
	
	@EventHandler
	public void onPlayerTeleportEvent( PlayerTeleportEvent event ) {
		for ( Apocalypse apocalypse : manager.getApocalypses() ) {
			if ( !apocalypse.isRunning() || apocalypse.isParticipant( event.getPlayer() ) ) {
				continue;
			}
			apocalypse.onPlayerTeleportEvent( event );
		}
	}
	
	@EventHandler
	public void onPlayerChangeWorldEvent( PlayerChangedWorldEvent event ) {
		for ( Apocalypse apocalypse : manager.getApocalypses() ) {
			if ( !apocalypse.isRunning() || apocalypse.isParticipant( event.getPlayer() ) ) {
				continue;
			}
			apocalypse.onPlayerChangeWorldEvent( event );
		}
	}
	
	@EventHandler
	public void onEntityDamageEvent( EntityDamageByEntityEvent event ) {
		for ( Apocalypse apocalypse : manager.getApocalypses() ) {
			if ( !apocalypse.isRunning() ) {
				continue;
			}
			apocalypse.onEntityDamageEvent( event );
		}
	}
	
	@EventHandler
	public void onEntityDeathEvent( EntityDeathEvent event ) {
		for ( Apocalypse apocalypse : manager.getApocalypses() ) {
			if ( !apocalypse.isRunning() ) {
				continue;
			}
			apocalypse.onEntityDeathEvent( event );
		}
	}
	
	@EventHandler
	public void onEntityExplodeEvent( EntityExplodeEvent event ) {
		for ( Apocalypse apocalypse : manager.getApocalypses() ) {
			if ( !apocalypse.isRunning() ) {
				continue;
			}
			apocalypse.onEntityExplodeEvent( event );
		}
	}
}
