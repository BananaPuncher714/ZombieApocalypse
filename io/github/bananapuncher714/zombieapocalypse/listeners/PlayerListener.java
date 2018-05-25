package io.github.bananapuncher714.zombieapocalypse.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import io.github.bananapuncher714.zombieapocalypse.ApocalypseManager;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void kickOutOfBed( PlayerBedEnterEvent e ) {
		if ( ApocalypseManager.getInstance().isInApocalypse( e.getPlayer() ) ) {
			e.setCancelled( true );
		}
	}
}
