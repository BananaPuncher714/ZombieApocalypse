package io.github.bananapuncher714.zombieapocalypse.objects;

import org.bukkit.World;

public class BuildAPocalypse extends Apocalypse {

	public BuildAPocalypse( String id, World world ) {
		super( id, world );
	}

	// Time methods
	public BuildAPocalypse atNight() {
		setStartAndStop( 12567, 22917 );
		return this;
	}

	public BuildAPocalypse atDay() {
		setStartAndStop( 0, 12567 );
		return this;
	}

	public BuildAPocalypse wheneverIFeelLikeIt() {
		setStartAndStop( 0, 23900 );
		return this;
	}

	// EndState methods
	public BuildAPocalypse thatsNotTooShort() {
		setEndState( EndState.KILL_ALL );
		return this;
	}

	public BuildAPocalypse asShortAsPossible() {
		setEndState( EndState.KILL_REQUIRED );
		return this;
	}

	public BuildAPocalypse thatsKindaLong() {
		setEndState( EndState.KILL_ALL );
		return this;
	}

	// Possibility methods
	public BuildAPocalypse thatAlwaysHappens() {
		setChance( 1 );
		return this;
	}

	public BuildAPocalypse withAPossibility() {
		setChance( .5 );
		return this;
	}

	public BuildAPocalypse whenPigsFly() {
		setChance( .001 );
		return this;
	}
	
	// Kill percent
	public BuildAPocalypse withMinimalEffort() {
		setKillPercentRequired( .1 );
		return this;
	}
	
	public BuildAPocalypse givenADecentTry() {
		setKillPercentRequired( .5 );
		return this;
	}
	
	public BuildAPocalypse andGottaKillEmAll() {
		setKillPercentRequired( 1 );
		return this;
	}
}
