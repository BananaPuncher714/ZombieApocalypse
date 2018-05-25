package io.github.bananapuncher714.zombieapocalypse;

import java.util.HashMap;
import java.util.Map;

import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;

public class ApocalypseManager {
	private static ApocalypseManager instance;
	
	private Map< String, Apocalypse > crises = new HashMap< String, Apocalypse >();
	
	private ApocalypseManager() {
	}
	
	public Apocalypse getApocalypse( String id ) {
		return crises.get( id );
	}

	public static ApocalypseManager getInstance() {
		if ( instance == null ) {
			instance = new ApocalypseManager();
		}
		return instance;
	}
}
