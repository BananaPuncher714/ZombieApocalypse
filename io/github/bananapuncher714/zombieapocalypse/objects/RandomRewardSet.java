package io.github.bananapuncher714.zombieapocalypse.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import io.github.bananapuncher714.zombieapocalypse.ZombieApocalypse;

public class RandomRewardSet extends RewardSet {
	boolean hideEnchants;
	boolean unbreakable;
	List< String > materials = new ArrayList< String >();
	List< String > requiredLore = new ArrayList< String >();
	List< String > optionalLore = new ArrayList< String >();
	List< String > requiredEnchantments = new ArrayList< String >();
	List< String > optionalEnchantments = new ArrayList< String >();
	List< String > prefixes = new ArrayList< String >();
	List< String > names = new ArrayList< String >();
	List< String > suffixes = new ArrayList< String >();
	int loreMin, loreMax, enchMin, enchMax, preMin, preMax, sufMin, sufMax;
	int perMin, perMax;
	int itemAmounts;
	
	Random random = ThreadLocalRandom.current();
	
	public RandomRewardSet( int itemAmounts ) {
		this.itemAmounts = itemAmounts;
	}
	
	public RandomRewardSet( FileConfiguration config ) {
		itemAmounts = config.getInt( "item-count" );
		materials = config.getStringList( "materials" );
		prefixes = config.getStringList( "names.prefix.names" );
		preMin = config.getInt( "names.prefix.min-amount" );
		preMax = config.getInt( "names.prefix.max-amount" );
		names = config.getStringList( "names.name.names" );
		suffixes = config.getStringList( "names.suffix.names" );
		sufMin = config.getInt( "names.suffix.min-amount" );
		sufMax = config.getInt( "names.suffix.max-amount" );
		requiredLore = config.getStringList( "lore.required" );
		optionalLore = config.getStringList( "lore.optional" );
		loreMin = config.getInt( "lore.optional-min" );
		loreMax = config.getInt( "lore.optional-max" );
		requiredEnchantments = config.getStringList( "enchantments.required" );
		optionalEnchantments = config.getStringList( "enchantments.optional" );
		enchMin = config.getInt( "enchantments.optional-min" );
		enchMax = config.getInt( "enchantments.optional-max" );
		hideEnchants = config.getBoolean( "enchantments.hide" );
		perMin = config.getInt( "durability.percent-min" );
		perMax = config.getInt( "durability.percent-max" );
		unbreakable = config.getBoolean( "durability.unbreakable" );
	}
	
	public RandomRewardSet setNames( List< String > names ) {
		this.names = names;
		return this;
	}
	
	public RandomRewardSet setPrefixes( List< String > prefixes, int preMin, int preMax ) {
		this.prefixes = prefixes;
		this.preMin = preMin;
		this.preMax = preMax;
		return this;
	}
	
	public RandomRewardSet setSuffixes( List< String > suffixes, int sufMin, int sufMax ) {
		this.suffixes = suffixes;
		this.sufMin = sufMin;
		this.sufMax = sufMax;
		return this;
	}
	
	public RandomRewardSet setMaterials( List< String > materials ) {
		this.materials = materials;
		return this;
	}
	
	public RandomRewardSet setRequiredLore( List< String > requiredLore ) {
		this.requiredLore = requiredLore;
		return this;
	}
	
	public RandomRewardSet setOptionalLore( List< String > optionalLore, int loreMin, int loreMax ) {
		this.optionalLore = optionalLore;
		this.loreMin = loreMin;
		this.loreMax = loreMax;
		return this;
	}
	
	public RandomRewardSet setRequiredEnchantments( List< String > requiredEnchantments ) {
		this.requiredEnchantments = requiredEnchantments;
		return this;
	}
	
	public RandomRewardSet setOptionalEnchantments( List< String > optionalEnchantments, int enchMin, int enchMax ) {
		this.optionalEnchantments = optionalEnchantments;
		this.enchMin = enchMin;
		this.enchMax = enchMax;
		return this;
	}
	
	public RandomRewardSet setUnbreakable( boolean unbreakable ) {
		this.unbreakable = unbreakable;
		return this;
	}
	
	public RandomRewardSet setHideEnchants( boolean hideEnchants ) {
		this.hideEnchants = hideEnchants;
		return this;
	}
	
	public RandomRewardSet setDurabilityPercentage( int perMin, int perMax ) {
		this.perMin = perMin;
		this.perMax = perMax;
		return this;
	}
	
	@Override
	public List< ItemStack > getRewards( Player player, double percentKilled, Apocalypse apocalypse ) {
		List< ItemStack > items = new ArrayList< ItemStack >();
		for ( int i = 0; i < itemAmounts; i++ ) {
			items.add( getItem( player ) );
		}
		return items;
	}
	
	public ItemStack getItem( Player player ) {
		String randomMaterial = "STICK";
		String itemName = "Banana";
		// Get random material
		if ( materials.size() > 0 ) {
			randomMaterial = materials.get( random.nextInt( materials.size() ) );
		}
		// Get random name
		if ( names.size() > 0 ) {
			itemName = parseRandom( selectRandom( names ) );
		}
		itemName = ZombieApocalypse.thWord( player, itemName );
		
		String[] rawMaterials = randomMaterial.split( ":" );
		Material material;
		try {
			material = Material.getMaterial( Integer.parseInt( rawMaterials[ 0 ] ) );
		} catch ( Exception e ) {
			material = Material.getMaterial( rawMaterials[ 0 ] );
		}
		int data = 0;
		if ( rawMaterials.length > 1 ) {
			data = Integer.parseInt( rawMaterials[ 1 ] );
		}
		ItemStack item = new ItemStack( material, 1, ( short ) data );
		if ( material == Material.SKULL_ITEM && rawMaterials.length > 2 ) {
			SkullMeta meta = ( SkullMeta ) item.getItemMeta();
			meta.setOwner( rawMaterials[ 2 ] );
			item.setItemMeta( meta );
		}
		ItemMeta meta = item.getItemMeta();
		int preAmount = preMax;
		if ( preMax - preMin > 0 ) preAmount = random.nextInt( preMax - preMin + 1 ) + preMin;
		ArrayList< String > unusedPrefixes = new ArrayList< String >( prefixes );
		for ( int i = 0; i < preAmount; i++ ) {
			if ( unusedPrefixes.size() > 0 ) {
				String chosenPre = parseRandom( selectRandom( unusedPrefixes ) );
				remove( unusedPrefixes, chosenPre );
				itemName = chosenPre + " " + itemName;
			} else {
				break;
			}
		}
		int sufAmount = sufMax;
		if ( sufMax - sufMin > 0 ) sufAmount = random.nextInt( sufMax - sufMin + 1 ) + sufMin;
		List< String > unusedSuffixes = new ArrayList< String >( suffixes );
		for ( int i = 0; i < sufAmount; i++ ) {
			if ( unusedSuffixes.size() > 0 ) {
				String chosenSuf = parseRandom( selectRandom( unusedSuffixes ) );
				remove( unusedSuffixes, chosenSuf );
				itemName = itemName + " " + chosenSuf;
			} else {
				break;
			}
		}
		List< String > lore = new ArrayList< String >();
		for ( String s : requiredLore ) {
			lore.add( ZombieApocalypse.thWord( player, parseRandom( s ) ) );
		}
		
		int loreAmount = random.nextInt( loreMax - loreMin + 1 ) + loreMin;
		List< String > randomLore = new ArrayList< String >( optionalLore );
		for ( int i = 0; i < loreAmount; i++ ) {
			if ( randomLore.size() > 0 ) {
				String rlore = selectRandom( randomLore );
				remove( randomLore, rlore );
				lore.add( parseRandom( rlore ) );
			} else {
				break;
			}
		}
		meta.setDisplayName( itemName );
		
		double durPercent = .01 * ( ( double ) ( random.nextInt( perMax - perMin + 1 ) + perMin ) );
		meta.setLore( lore );
		if ( hideEnchants ) {
			meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
		}
		if ( unbreakable ) {
			meta.setUnbreakable( true );
		}
		item.setItemMeta( meta );
		applyEnchants( item );
		item.setDurability( ( short ) ( material.getMaxDurability() * ( 1.0 - durPercent ) ) );
		return item;
	}
	
	private String parseRandom( String string ) {
		Random r = new Random();
		String parsed = string;
		Pattern pattern = Pattern.compile( "random<(\\d*?),(\\d*?)>" );
		Matcher matcher = pattern.matcher( parsed );
		while ( matcher.find() ) {
			int lower = Integer.parseInt( matcher.group( 1 ) );
			int higher = Integer.parseInt( matcher.group( 2 ) );
			int random = r.nextInt( higher - lower ) + lower;
			parsed = parsed.replaceFirst( "random<(\\d*?),(\\d*?)>", String.valueOf( random ) );
		}
		Pattern tpattern = Pattern.compile( "random<(\\d+),(\\d+),(\\d+)>" );
		Matcher tmatcher = tpattern.matcher( parsed );
		while ( tmatcher.find() ) {
			int lower = Integer.parseInt( tmatcher.group( 1 ) );
			int higher = Integer.parseInt( tmatcher.group( 2 ) );
			int step = Integer.parseInt( tmatcher.group( 3 ) );
			int maxStep = ( higher - lower ) / step;
			int finalNum = ( r.nextInt( maxStep + 1 ) * step ) + lower;
			parsed = parsed.replaceFirst( "random<(\\d+),(\\d+),(\\d+)>", String.valueOf( finalNum ) );
		}
		return parsed;
	}
	
	private String selectRandom( List< String > lore ) {
		HashMap< String, Integer > weightedLore = new HashMap< String, Integer >();
		int totalWeight = 0;
		for ( String l : lore ) {
			Pattern pattern = Pattern.compile( ":(\\d*)$" );
			Matcher matcher = pattern.matcher( l );
			String parsed = l.replaceAll( ":(\\d*)$", "" );
			int weight = 1;
			if ( matcher.find() ) weight = Integer.parseInt( matcher.group( 1 ) );
			weightedLore.put( parsed, weight );
			totalWeight = totalWeight + weight;
		}
		
		if ( totalWeight <= 0 ) {
			return null;
		}
		Random rand = new Random();
        int index = Math.abs( rand.nextInt( totalWeight + 1 ) );
        int sum = 0;
        int i = 0;
        List< String > ores = new ArrayList< String >( weightedLore.keySet() );
        while( sum < index ) {
             sum = sum + weightedLore.get( ores.get( i++ ) );
        }
        return ores.get( Math.max( 0, i-1 ) );
	}
	
	private void applyEnchants( ItemStack item ) {
		for ( String s : requiredEnchantments ) {
			addEnchantment( item, s );
		}
		List< String > opEnch = new ArrayList< String >();
		for ( String s : optionalEnchantments ) {
			String[] enchList = s.split( ":" );
			if ( enchList.length < 4 ) {
				enchList = new String[] { enchList[ 0 ], enchList[ 1 ], enchList[ 2 ], "1" };
			}
			opEnch.add( enchList[ 0 ] + ":" + enchList[ 1 ] + ":" + enchList[ 2 ] + ":" + enchList[ 3 ] );
		}
		int enchAmount = enchMax;
		if ( enchMax - enchMin > 0 ) enchAmount = random.nextInt( enchMax - enchMin + 1 ) + enchMin;
		for ( int i = 0; i < enchAmount; i++ ) {
			if ( opEnch.size() > 0 ) {
				String enchn = selectRandom( opEnch );
				addEnchantment( item, enchn );
			} else {
				break;
			}
		}
	}
	
	private void addEnchantment( ItemStack i, String enchantment ) {
		String[] enchList = enchantment.split( ":" );
		Enchantment enchant;
		try {
			enchant = Enchantment.getByName( enchList[ 0 ] );
		} catch ( Exception e ) {
			enchant = Enchantment.getById( Integer.parseInt( enchList[ 0 ] ) );
		}
		int min = Integer.parseInt( enchList[ 1 ] );
		int max = Integer.parseInt( enchList[ 2 ] );
		int randLevel = max;
		if ( max - min > 0 ) randLevel = random.nextInt( max - min + 1 ) + min;
		i.addUnsafeEnchantment( enchant, randLevel );
	}
	
	private static void remove( List< String > list, String toRemove ) {
		for ( int i = 0; i < list.size(); i++ ) {
			if ( list.get( i ).contains( toRemove ) ) {
				list.remove( i );
				return;
			}
		}
	}

}
