package io.github.bananapuncher714.zombieapocalypse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;
import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse.EndState;
import io.github.bananapuncher714.zombieapocalypse.objects.BuildAPocalypse;
import io.github.bananapuncher714.zombieapocalypse.objects.RewardSet;
import io.github.bananapuncher714.zombieapocalypse.objects.SpawnSet;
import io.github.bananapuncher714.zombieapocalypse.util.SpawnUtil;

/**
 * Demo for how to integrate or create addons for this plugin
 * 
 * @author BananaPuncher714
 */
public class DemoStarter {
	
	public static void init() {
		Apocalypse apocalypse = new Apocalypse( "DemoApocalypse", Bukkit.getWorld( "world" ) );
		apocalypse.setChance( 1 );
		
		apocalypse.addSpawnSet( new SpawnSet() {
			@Override
			public Set< Entity > spawn( Player player ) {
				Set< Entity > entities = new HashSet< Entity >();
				for ( int i = 0; i < 10; i++ ) {
					Entity entity = player.getWorld().spawnEntity( SpawnUtil.getRandomSpawn( player.getLocation(), 20, 10 ), EntityType.ZOMBIE );
					if ( entity == null ) {
						continue;
					}
					entity.setCustomName( "Apocalypse Mob" );
					entity.setCustomNameVisible( true );
					entity.setGlowing( true );
					entities.add( entity );
				}
				return entities;
			}
		}, 1 );
		
		apocalypse.addRewardSet( new RewardSet() {
			@Override
			public List< ItemStack > getRewards( Player player, double percentKilled, Apocalypse apocalypse ) {
				List< ItemStack > items = new ArrayList< ItemStack >();
				items.add( new ItemStack( Material.STICK ) );
				return items;
			}
		}, 1 );
		
		apocalypse.setEndState( EndState.KILL_REQUIRED );
		
		ApocalypseManager.getInstance().rememberMeX( apocalypse );
	}

	/**
	 * Something fun
	 */
	public static void something() {
		Apocalypse apocalypse = new BuildAPocalypse( "BuildAPocalypse", Bukkit.getWorld( "world" ) )
				.atNight()
				.thatsNotTooShort()
				.thatAlwaysHappens()
				.givenADecentTry();
	}
}
