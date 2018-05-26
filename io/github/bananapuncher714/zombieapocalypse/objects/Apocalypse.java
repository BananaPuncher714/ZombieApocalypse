package io.github.bananapuncher714.zombieapocalypse.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import io.github.bananapuncher714.zombieapocalypse.ApocalypseManager;
import io.github.bananapuncher714.zombieapocalypse.ZombieApocalypse;
import io.github.bananapuncher714.zombieapocalypse.util.Util;

public class Apocalypse {
	boolean isRunning = false;
	final String id;

	World world; // This is a worldwide event
	int timeStart = 12567, timeEnd = 22917; // The time in ticks of start and stop, 0-24000
	double possibility = .2; // Chance of this occuring each day when it is timeStart
	double killPercentRequired = .5; // How many mobs must be killed to win?
	EndState endOnFinish = EndState.END_TIME;
	int playersRequired = 1;
	Set< UUID > participants = new HashSet< UUID >(); // Who's playing; Only useful when apocalypse is running
	Map< SpawnSet, Integer > spawns = new HashMap< SpawnSet, Integer >(); // The different spawnSets to spawn mobs
	Map< RewardSet, Integer > rewards = new HashMap< RewardSet, Integer >(); // The rewards for winning, one will be chosen at random

	Map< UUID, Boolean > monsters = new HashMap< UUID, Boolean >(); // Keep track of mobs

	public Apocalypse( String id, World world ) {
		this.world = world;
		this.id = id.replaceAll( "\\s+", "" );
	}
	
	public Apocalypse( String id, FileConfiguration config ) {
		this.id = id;
		world = Bukkit.getWorld( config.getString( "world" ) );
		if ( world == null ) {
			throw new IllegalArgumentException( "World provided does not exist! '" + config.getString( "world" ) + "'" );
		}
		timeStart = config.getInt( "time.start" );
		timeEnd = config.getInt( "time.end" );
		possibility = config.getDouble( "time.possibility" );
		killPercentRequired = config.getDouble( "kill-percent-required" );
		playersRequired = config.getInt( "players-required" );
		endOnFinish = EndState.valueOf( config.getString( "end-state" ).toUpperCase() );
		for ( String string : config.getStringList( "rewards" ) ) {
			String[] data = string.split( ":" );
			RewardSet set = ApocalypseManager.getInstance().getRewardSet( data[ 0 ] );
			if ( set == null ) {
				ZombieApocalypse.getConsoleLogger().warning( "Invalid RewardSet! '" + data[ 0 ] + "'" );
			}
			int weight = 1;
			if ( data.length == 2 ) {
				try {
					weight = Integer.parseInt( data[ 1 ] );
				} catch ( Exception exception ) {
					exception.printStackTrace();
				}
			}
			rewards.put( set, weight );
		}
		for ( String string : config.getStringList( "spawns" ) ) {
			String[] data = string.split( ":" );
			SpawnSet set = ApocalypseManager.getInstance().getSpawnSet( data[ 0 ] );
			if ( set == null ) {
				ZombieApocalypse.getConsoleLogger().warning( "Invalid SpawnSet! '" + data[ 0 ] + "'"  );
			}
			int weight = 1;
			if ( data.length == 2 ) {
				try {
					weight = Integer.parseInt( data[ 1 ] );
				} catch ( Exception exception ) {
					exception.printStackTrace();
				}
			}
			spawns.put( set, weight );
		}
	}

	public Apocalypse setStartAndStop( int start, int stop ) {
		timeStart = start % 24000;
		timeEnd = stop % 24000;
		return this;
	}

	public World getWorld() {
		return world;
	}

	public int getStart() {
		return timeStart;
	}

	public int getEnd() {
		return timeEnd;
	}

	public EndState getEndState() {
		return endOnFinish;
	}
	
	public Apocalypse setEndState( EndState state ) {
		endOnFinish = state;
		return this;
	}
	
	public double getChance() {
		return possibility;
	}

	public Apocalypse setChance( double chance ) {
		possibility = Math.max( 0, chance );
		return this;
	}

	public String getId() {
		return id;
	}

	public double getKillPercentRequired() {
		return killPercentRequired;
	}

	public Apocalypse setKillPercentRequired( double percent ) {
		killPercentRequired = percent;
		return this;
	}

	public Apocalypse removePlayer( Player player ) {
		participants.remove( player.getUniqueId() );
		return this;
	}

	public boolean isParticipant( Player player ) {
		return participants.contains( player.getUniqueId() );
	}
	
	public Apocalypse addSpawnSet( SpawnSet set, int weight ) {
		spawns.put( set, weight );
		return this;
	}
	
	public boolean removeSpawnSet( SpawnSet set ) {
		boolean contains = spawns.containsKey( set );
		if ( contains ) {
			spawns.remove( set );
		}
		return contains;
	}

	public Apocalypse addRewardSet( RewardSet set, int weight ) {
		rewards.put( set, weight );
		return this;
	}
	
	public boolean removeRewardSet( RewardSet set ) {
		boolean contains = rewards.containsKey( set );
		if ( contains ) {
			rewards.remove( set );
		}
		return contains;
	}
	
	public void start() {
		participants.clear();
		for ( Player player : world.getPlayers() ) {
			participants.add( player.getUniqueId() );
		}
		if ( participants.isEmpty() ) {
			ZombieApocalypse.getConsoleLogger().warning( "'" + id + "' failed to start!" );
			return;
		}
		isRunning = true;
		ZombieApocalypse.getConsoleLogger().info( "Starting apocalypse '" + id + "'" );
		
		SpawnSet set = Util.getRandom( spawns );
		monsters.clear();
		for ( UUID uuid : participants ) {
			Player player = Bukkit.getPlayer( uuid );
			player.sendMessage( ZombieApocalypse.parse( "notifications.started-apocalypse", player ) );
			player.playSound( player.getLocation(), Sound.AMBIENT_CAVE, 1, 1 );
			spawnMobs( set, player );
		}
	}

	private void update() {
		if ( participants.size() < playersRequired ) {
			ZombieApocalypse.getConsoleLogger().warning( "No participants left in '" + id + "'. Stopping..." );
			stop( false );
			return;
		}
		if ( endOnFinish == EndState.KILL_ALL ) {
			if ( getPercentCleared() == 1 ) {
				for ( UUID uuid : participants ) {
					Player player = Bukkit.getPlayer( uuid );
					player.sendMessage( ZombieApocalypse.parse( "notifications.completed-apocalypse", player ) );
				}
				stop( false );
			}
		} else if ( endOnFinish == EndState.KILL_REQUIRED ) {
			if ( getPercentCleared() >= killPercentRequired ) {
				for ( UUID uuid : participants ) {
					Player player = Bukkit.getPlayer( uuid );
					player.sendMessage( ZombieApocalypse.parse( "notifications.completed-apocalypse", player ) );
				}
				stop( false );
			}
		}
	}

	public void stop( boolean cancel ) {
		isRunning = false;
		ZombieApocalypse.getConsoleLogger().info( "Stopped apocalypse '" + id + "'" );
		for ( UUID monster : monsters.keySet() ) {
			Entity mob = Bukkit.getEntity( monster );
			if ( mob != null ) {
				mob.remove();
			}
		}
		double percentCleared = getPercentCleared();
		monsters.clear();
		// See if the apocalypse stopped because everyone quit or something
		if ( cancel ) {
			return;
		}
		
		if ( participants.size() < playersRequired ) {
			for ( UUID uuid : participants ) {
				Player player = Bukkit.getPlayer( uuid );
				player.sendMessage( ZombieApocalypse.parse( "notifications.not-enough-players", player ) );
			}
			return;
		}

		// See if the kill percent is reached
		if ( percentCleared >= killPercentRequired ) {
			RewardSet reward = Util.getRandom( rewards );

			for ( UUID uuid : participants ) {
				Player player = Bukkit.getPlayer( uuid );
				player.sendMessage( ZombieApocalypse.parse( "notifications.win-stuff", player ) );
				player.playSound( player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1 );
				giveRewards( reward, player, percentCleared );
			}
		} else {
			for ( UUID uuid : participants ) {
				Player player = Bukkit.getPlayer( uuid );
				player.sendMessage( ZombieApocalypse.parse( "notifications.lost-stuff", player ) );
				player.playSound( player.getLocation(), Sound.ENTITY_ENDERMEN_DEATH, 1, 1 );
			}
		}
	}

	private void spawnMobs( SpawnSet set, Player player ) {
		if ( monsters.size() > ZombieApocalypse.MOB_CAP ) {
			return;
		}
		Set< Entity > mobsSpawned = set.spawn( player );
		for ( Entity mob : mobsSpawned ) {
			if ( monsters.size() > ZombieApocalypse.MOB_CAP ) {
				mob.remove();
			} else {
				monsters.put( mob.getUniqueId(), true );
			}
		}
	}

	private void giveRewards( RewardSet set, Player player, double percentKilled ) {
		List< ItemStack > items = set.getRewards( player, percentKilled, this );
		for ( ItemStack item : player.getInventory().addItem( items.toArray( new ItemStack[ items.size() ] ) ).values() ) {
			player.getLocation().getWorld().dropItem( player.getLocation(), item );
		}
	}

	public Set< UUID > getMobs() {
		return monsters.keySet();
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public double getPercentCleared() {
		return getAmountCleared() / ( double ) monsters.size();
	}
	
	public int getAmountCleared() {
		int i = 0;
		for ( boolean value : monsters.values() ) {
			if ( !value ) {
				i++;
			}
		}
		return i;
	}
	
	public int getAmountTotal() {
		return monsters.size();
	}
	
	// Run things depending on events
	public void onPlayerDeathEvent( PlayerDeathEvent event ) {
		participants.remove( event.getEntity().getUniqueId() );
		event.getEntity().sendMessage( ZombieApocalypse.parse( "notifications.died", event.getEntity() ) );
		update();
	}

	public void onPlayerQuitEvent( PlayerQuitEvent event ) {
		participants.remove( event.getPlayer().getUniqueId() );
	}

	public void onPlayerTeleportEvent( PlayerTeleportEvent event ) {
		if ( event.getTo().getWorld() != world ) {
			event.setCancelled( true );
			event.getPlayer().sendMessage( ZombieApocalypse.parse( "notifications.no-tp", event.getPlayer() ) );
		}
		update();
	}

	public void onPlayerChangeWorldEvent( PlayerChangedWorldEvent event ) {
		participants.remove( event.getPlayer().getUniqueId() );
		event.getPlayer().sendMessage( ZombieApocalypse.parse( "notifications.escaped", event.getPlayer() ) );
		update();
	}

	public void onEntityDamageEvent( EntityDamageByEntityEvent event ) {
	}

	public void onEntityDeathEvent( EntityDeathEvent event ) {
		Entity entity = event.getEntity();
		if ( monsters.containsKey( entity.getUniqueId() ) ) {
			monsters.put( event.getEntity().getUniqueId(), false );
			for ( UUID uuid : participants ) {
				Player player = Bukkit.getPlayer( uuid );
				player.playSound( entity.getLocation(), Sound.BLOCK_NOTE_PLING, 1, ( float ) getPercentCleared() + 1f );
			}
			update();
		}
	}
	
	public void onEntityExplodeEvent( EntityExplodeEvent event ) {
		Entity entity = event.getEntity();
		if ( monsters.containsKey( entity.getUniqueId() ) ) {
			monsters.remove( entity.getUniqueId() );
		}
	}
	
	public enum EndState {
		KILL_ALL, KILL_REQUIRED, END_TIME;
	}
}
