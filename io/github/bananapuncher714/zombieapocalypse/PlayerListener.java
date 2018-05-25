package io.github.bananapuncher714.zombieapocalypse;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void kickOutOfBed(PlayerBedEnterEvent e) {
		e.getPlayer().sendMessage(ChatColor.RED + "ZA: You're too scared to sleep during the zombie apocalypse.");
		e.setCancelled(true);
	}
}
