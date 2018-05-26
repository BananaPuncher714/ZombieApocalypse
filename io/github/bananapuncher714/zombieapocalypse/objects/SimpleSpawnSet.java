package io.github.bananapuncher714.zombieapocalypse.objects;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.zombieapocalypse.util.SpawnUtil;

public class SimpleSpawnSet extends SpawnSet {
	EntityType type = EntityType.ZOMBIE;
	int minRange = 10, maxRange = 20;
	int spawnAmount = 10, health = 40;
	boolean glowing = true;
	String name = null;
	
	public SimpleSpawnSet( FileConfiguration config ) {
		EntityType whatType = EntityType.valueOf( config.getString( "entity-type" ).toUpperCase() );
		if ( whatType != null ) {
			type = whatType;
		}
		glowing = config.getBoolean( "glowing" );
		name = config.getString( "name" );
		spawnAmount = config.getInt( "spawn.amount" );
		minRange = config.getInt( "spawn.min-range" );
		maxRange = config.getInt( "spawn.max-range" );
		health = config.getInt( "health" );
	}

	@Override
	public Set< Entity > spawn( Player player ) {
		Set< Entity > entities = new HashSet< Entity >();
		for ( int i = 0; i < spawnAmount; i++ ) {
			Entity entity = player.getWorld().spawnEntity( SpawnUtil.getRandomSpawn( player.getLocation(), maxRange, minRange ), type );
			if ( entity instanceof LivingEntity ) {
				LivingEntity living = ( LivingEntity ) entity;
				living.setMaxHealth( health );
				living.setHealth( 40 );
			}
			entity.setGlowing( glowing );
			if ( name != null ) {
				entity.setCustomName( name );
				entity.setCustomNameVisible( true );
			}
			entities.add( entity );
		}
		return entities;
	}

}
