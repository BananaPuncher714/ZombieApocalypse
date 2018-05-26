package io.github.bananapuncher714.zombieapocalypse.objects;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StandardRewardSet extends RewardSet {
	private List< ItemStack > items;
	
	public StandardRewardSet( List< ItemStack > items ) {
		this.items = items;
	}
	
	@Override
	public List< ItemStack > getRewards( Player player, double percentKilled, Apocalypse apocalypse ) {
		return items;
	}

}
