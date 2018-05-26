package io.github.bananapuncher714.zombieapocalypse.util;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.bananapuncher714.zombieapocalypse.ApocalypseManager;
import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;
import io.github.bananapuncher714.zombieapocalypse.objects.RandomRewardSet;
import io.github.bananapuncher714.zombieapocalypse.objects.SimpleSpawnSet;

public class ApocalypseDeserializer {
	
	public static void registerRandomRewards( File baseDir ) {
		if ( !baseDir.exists() ) {
			return;
		}
		for ( File file : baseDir.listFiles() ) {
			FileConfiguration config = YamlConfiguration.loadConfiguration( file );
			RandomRewardSet set = new RandomRewardSet( config );
			String id = file.getName().replaceFirst( "\\.yml$", "" );
			ApocalypseManager.getInstance().registerRewardSet( id, set );
		}
	}
	
	public static void registerSimpleSpawns( File baseDir ) {
		if ( !baseDir.exists() ) {
			return;
		}
		for ( File file : baseDir.listFiles() ) {
			FileConfiguration config = YamlConfiguration.loadConfiguration( file );
			SimpleSpawnSet set = new SimpleSpawnSet( config );
			String id = file.getName().replaceFirst( "\\.yml$", "" );
			ApocalypseManager.getInstance().registerSpawnSet( id, set );
		}
	}

	public static void registerApocalypses( File baseDir ) {
		if ( !baseDir.exists() ) {
			return;
		}
		for ( File file : baseDir.listFiles() ) {
			FileConfiguration config = YamlConfiguration.loadConfiguration( file );
			Apocalypse apocalypse = new Apocalypse( config );
			ApocalypseManager.getInstance().registerApocalypse( apocalypse );
		}
	}
}
