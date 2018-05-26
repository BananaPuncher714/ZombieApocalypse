package io.github.bananapuncher714.zombieapocalypse.objects;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class SpawnSet {
	public abstract Set< Entity > spawn( Player player );
}
