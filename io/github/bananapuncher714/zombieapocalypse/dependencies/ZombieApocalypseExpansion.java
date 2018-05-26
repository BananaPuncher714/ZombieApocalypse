package io.github.bananapuncher714.zombieapocalypse.dependencies;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.zombieapocalypse.ApocalypseManager;
import io.github.bananapuncher714.zombieapocalypse.ZombieApocalypse;
import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class ZombieApocalypseExpansion extends PlaceholderExpansion {

	@Override
	public String onPlaceholderRequest( Player player, String identifier ) {
		for ( Apocalypse apocalypse : ApocalypseManager.getInstance().getApocalypses() ) {
			if ( identifier.equalsIgnoreCase( apocalypse.getId() + "_running" ) ) {
				return apocalypse.isRunning() + "";
			} else if ( identifier.equalsIgnoreCase( apocalypse.getId() + "_kill_percent_required" ) ) {
				return apocalypse.getKillPercentRequired() + "";
			} else if ( identifier.equalsIgnoreCase( apocalypse.getId() + "_percent_killed" ) ) {
				return apocalypse.getPercentCleared() + "";
			} else if ( identifier.equalsIgnoreCase( apocalypse.getId() + "_amount_cleared" ) ) {
				return apocalypse.getAmountCleared() + "";
			} else if ( identifier.equalsIgnoreCase( apocalypse.getId() + "_amount_total" ) ) {
				return apocalypse.getAmountTotal() + "";
			}
		}
		return null;
	}

	@Override
	public String getAuthor() {
		return "BananaPuncher714";
	}

	@Override
	public String getIdentifier() {
		return "zombieapocalypse";
	}

	@Override
	public String getPlugin() {
		return ZombieApocalypse.getPlugin( ZombieApocalypse.class ).getName();
	}

	@Override
	public String getVersion() {
		return "42";
	}
}
