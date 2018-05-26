package io.github.bananapuncher714.zombieapocalypse.objects;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class RewardSet {
	public abstract List< ItemStack > getRewards( Player player, double percentKilled, Apocalypse apocalypse );
}
