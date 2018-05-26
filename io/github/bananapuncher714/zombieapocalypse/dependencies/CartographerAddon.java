package io.github.bananapuncher714.zombieapocalypse.dependencies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCursor.Type;

import io.github.bananapuncher714.cartographer.MapManager;
import io.github.bananapuncher714.cartographer.api.map.Minimap;
import io.github.bananapuncher714.cartographer.api.map.addon.CursorSelector;
import io.github.bananapuncher714.cartographer.api.map.addon.Module;
import io.github.bananapuncher714.cartographer.api.objects.RealWorldCursor;
import io.github.bananapuncher714.zombieapocalypse.ApocalypseManager;
import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;

public class CartographerAddon extends Module {

	public static void init() {
		for ( Minimap minimap : MapManager.getInstance().getMinimaps().values() ) {
			minimap.loadModule( "zombieapocalypse", new CartographerAddon() );
		}
	}
	
	@Override
	public void load( Minimap arg0, File arg1 ) {
		arg0.registerCursorSelector( new ZombieHighlighter() );
	}

	@Override
	public void unload() {

	}

	public class ZombieHighlighter implements CursorSelector {
		@Override
		public List< RealWorldCursor > getCursors( Player player ) {
			List< RealWorldCursor > cursors = new ArrayList< RealWorldCursor >();
			for ( Apocalypse apocalypse : ApocalypseManager.getInstance().getApocalypses() ) {
				if ( !apocalypse.isRunning() || !apocalypse.isParticipant( player ) ) {
					continue;
				}
				for ( UUID uuid : apocalypse.getMobs() ) {
					Entity entity = Bukkit.getEntity( uuid );
					if ( entity == null || entity.isDead() ) {
						continue;
					}
					cursors.add( new RealWorldCursor( entity.getLocation(), Type.RED_POINTER, false ) );
				}
			}
			return cursors;
		}
		
	}
}
