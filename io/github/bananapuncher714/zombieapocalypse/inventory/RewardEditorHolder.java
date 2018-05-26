package io.github.bananapuncher714.zombieapocalypse.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import io.github.bananapuncher714.zombieapocalypse.ngui.inventory.BananaHolder;
import io.github.bananapuncher714.zombieapocalypse.ngui.items.ItemBuilder;
import io.github.bananapuncher714.zombieapocalypse.ngui.items.SkullBuilder;
import io.github.bananapuncher714.zombieapocalypse.ngui.util.NBTEditor;
import io.github.bananapuncher714.zombieapocalypse.objects.StandardRewardSet;

public class RewardEditorHolder extends BananaHolder {
	StandardRewardSet set;
	Inventory inventory;
	int page = 0;
	
	public RewardEditorHolder( String id, StandardRewardSet set ) {
		this.set = set;
		inventory = Bukkit.createInventory( this, 36, "Editing reward set " + id );
	}

	@Override
	public Inventory getInventory() {
		inventory.clear();
		int maxPages = ( int ) Math.ceil( set.getItems().size() / ( double ) ( inventory.getSize() - 9 ) );
		page = Math.min( maxPages, page );
		for ( int i = 0; i < 9; i++ ) {
			ItemStack item;
			if ( i == 0 && page > 0 ) {
				item = new SkullBuilder( ChatColor.WHITE + "Previous page", "http://textures.minecraft.net/texture/eed78822576317b048eea92227cd85f7afcc44148dcb832733baccb8eb56fa1", true ).addFlags( ItemFlag.values() ).getItem();
				item = NBTEditor.setItemTag( item, "prev page", "zombieapocalypse", "inventory", "meta-1" );
			} else if ( i == 8 && page < maxPages - 1 ) {
				item = new SkullBuilder( ChatColor.WHITE + "Next page", "http://textures.minecraft.net/texture/715445da16fab67fcd827f71bae9c1d2f90c73eb2c1bd1ef8d8396cd8e8", true ).addFlags( ItemFlag.values() ).getItem();
				item = NBTEditor.setItemTag( item, "next page", "zombieapocalypse", "inventory", "meta-1" );
			} else {
				item  = new ItemBuilder( Material.STAINED_GLASS_PANE, 1, ( byte ) 7, " ", false ).addFlags( ItemFlag.values() ).getItem();
			}
			item = NBTEditor.setItemTag( item, ( byte ) 1, "zombieapocalypse", "inventory", "custom" );
			inventory.setItem( inventory.getSize() - ( 9 - i ), item );
		}
		int startIndex = page * ( inventory.getSize() - 9 );
		for ( int i = 0; i < inventory.getSize() - 9; i++ ) {
			if ( i + startIndex >= set.getItems().size() ) {
				break;
			}
			ItemStack item = set.getItems().get( i + startIndex );
			item = NBTEditor.setItemTag( item, ( byte ) 1, "zombieapocalypse", "inventory", "custom" );
			item = NBTEditor.setItemTag( item, "get", "zombieapocalypse", "inventory", "meta-1" );
			item = NBTEditor.setItemTag( item, i + startIndex, "zombieapocalypse", "inventory", "meta-2" );
			inventory.setItem( i, item );
		}
		return inventory;
	}

	@Override
	public void onInventoryClick( InventoryClickEvent event ) {
		boolean isGui = true;
		ClickType click = event.getClick();
		if ( event.getSlot() != event.getRawSlot() ) {
			isGui = false;
			if ( !click.isKeyboardClick() && !click.isShiftClick() ) {
				return;
			}
		}
		event.setCancelled( true );
		ItemStack item = event.getCurrentItem();
		ItemStack cursor = event.getCursor();
		if ( !isGui ) {
			if ( !click.isShiftClick() || item == null ) {
				return;
			}
			if ( item.getType() == Material.AIR ) {
				return;
			}
			event.setCurrentItem( new ItemStack( Material.AIR ) );
			set.getItems().add( item );
		} else if ( cursor != null && cursor.getType() != Material.AIR ) {
			event.setCursor( new ItemStack( Material.AIR ) );
			set.getItems().add( cursor );
		} else if ( item != null && NBTEditor.getItemTag( item, "zombieapocalypse", "inventory", "custom" ) != null ) {
			String meta = ( String ) NBTEditor.getItemTag( item, "zombieapocalypse", "inventory", "meta-1" );
			if ( meta == null ) {
				return;
			}
			if ( meta.equalsIgnoreCase( "next page" ) ) {
				page++;
			} else if ( meta.equalsIgnoreCase( "prev page" ) ) {
				page = Math.max( 0, page - 1 );
			} else if ( meta.equalsIgnoreCase( "get" ) ) {
				int index = ( int ) NBTEditor.getItemTag( item, "zombieapocalypse", "inventory", "meta-2" );
				event.setCursor( set.getItems().remove( index ) );
			}
		}
		getInventory();
	}

}
