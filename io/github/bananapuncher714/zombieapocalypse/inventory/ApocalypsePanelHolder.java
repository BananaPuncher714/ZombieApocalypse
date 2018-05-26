package io.github.bananapuncher714.zombieapocalypse.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.bananapuncher714.zombieapocalypse.ApocalypseManager;
import io.github.bananapuncher714.zombieapocalypse.ngui.inventory.BananaHolder;
import io.github.bananapuncher714.zombieapocalypse.ngui.items.ItemBuilder;
import io.github.bananapuncher714.zombieapocalypse.ngui.items.SkullBuilder;
import io.github.bananapuncher714.zombieapocalypse.ngui.util.NBTEditor;
import io.github.bananapuncher714.zombieapocalypse.objects.Apocalypse;

public class ApocalypsePanelHolder extends BananaHolder {
	Inventory inventory;
	int page = 0;
	
	public ApocalypsePanelHolder() {
		inventory = Bukkit.createInventory( this, 36, "Apocalypse Panel" );
	}
	
	@Override
	public Inventory getInventory() {
		inventory.clear();
		List< Apocalypse > apocolypses = new ArrayList< Apocalypse >( ApocalypseManager.getInstance().getApocalypses() );
		int maxPages = ( int ) Math.ceil( apocolypses.size() / ( double ) ( inventory.getSize() - 9 ) );
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
			if ( i + startIndex >= apocolypses.size() ) {
				break;
			}
			Apocalypse apocalypse = apocolypses.get( i + startIndex );
			ItemStack item = new ItemStack( apocalypse.isRunning() ? Material.GREEN_RECORD : Material.RECORD_3 );
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName( ChatColor.WHITE + apocalypse.getId() );
			List< String > lore = new ArrayList<String >();
			if ( apocalypse.isRunning() ) {
				lore.add( ChatColor.GRAY + "Apocalypse is " + ChatColor.GREEN + "active" );
				lore.add( ChatColor.GRAY + ChatColor.BOLD.toString() + "Left-click" + ChatColor.GRAY + " to stop" );
				lore.add( ChatColor.GRAY + ChatColor.BOLD.toString() + "Shift left-click" + ChatColor.GRAY + " to stop forcefully" );
			} else {
				lore.add( ChatColor.GRAY + "Apocalypse is " + ChatColor.RED + "inactive" );
				lore.add( ChatColor.GRAY + ChatColor.BOLD.toString() + "Left-click" + ChatColor.GRAY + " to start" );
			}
			meta.setLore( lore );
			item.setItemMeta( meta );
			item = NBTEditor.setItemTag( item, ( byte ) 1, "zombieapocalypse", "inventory", "custom" );
			item = NBTEditor.setItemTag( item, "manage", "zombieapocalypse", "inventory", "meta-1" );
			item = NBTEditor.setItemTag( item, apocalypse.getId(), "zombieapocalypse", "inventory", "meta-2" );
			inventory.setItem( i, item );
		}
		return inventory;
	}

	@Override
	public void onInventoryClick( InventoryClickEvent event ) {
		ClickType click = event.getClick();
		if ( event.getSlot() != event.getRawSlot() ) {
			if ( !click.isKeyboardClick() && !click.isShiftClick() ) {
				return;
			}
		}
		event.setCancelled( true );
		ItemStack item = event.getCurrentItem();
		if ( item != null && NBTEditor.getItemTag( item, "zombieapocalypse", "inventory", "custom" ) != null ) {
			String meta = ( String ) NBTEditor.getItemTag( item, "zombieapocalypse", "inventory", "meta-1" );
			if ( meta == null ) {
				return;
			}
			if ( meta.equalsIgnoreCase( "next page" ) ) {
				page++;
			} else if ( meta.equalsIgnoreCase( "prev page" ) ) {
				page = Math.max( 0, page - 1 );
			} else if ( meta.equalsIgnoreCase( "manage" ) ) {
				String id = ( String ) NBTEditor.getItemTag( item, "zombieapocalypse", "inventory", "meta-2" );
				Apocalypse apocalypse = ApocalypseManager.getInstance().getApocalypse( id );
				if ( apocalypse.isRunning() ) {
					if ( click == ClickType.LEFT ) {
						apocalypse.stop( false );
					} else if ( click == ClickType.SHIFT_LEFT ) {
						apocalypse.stop( true );
					}
				} else {
					if ( click == ClickType.LEFT ) {
						apocalypse.start();
					}
				}
			}
		}
		getInventory();
	}

}
