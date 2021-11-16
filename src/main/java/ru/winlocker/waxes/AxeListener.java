package ru.winlocker.waxes;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import ru.winlocker.waxes.events.AxeHitEvent;
import ru.winlocker.waxes.utils.Utils;

public class AxeListener implements Listener {
	
	@EventHandler
	public void onHit(AxeHitEvent e) {
		if(e.getShooter() == null) return;
		
		if(e.getEntityAttached() != null) {
			if(e.getEntityAttached() instanceof LivingEntity) {
				
				LivingEntity len = (LivingEntity) e.getEntityAttached();
				len.damage(e.getAxe().getDamage());

				if(e.getAxe().getEffects() != null) 
					e.getAxe().getEffects().forEach(x -> len.addPotionEffect(x));
				
				Utils.sendMessage(e.getShooter(), Utils.getMessage("axe-hit").replace("%name%", len.getName()));
			}
		}
		else {
			if(e.getAxe().isDiminish()) 
				Utils.giveItem(e.getShooter(), e.getAxe().getItem());
			
			Utils.sendMessage(e.getShooter(), Utils.getMessage("axe-nohit"));
		}
		
		if(e.getAxe().getParticle() != null) {
			e.getLocation().getWorld().spawnParticle(e.getAxe().getParticle(), e.getLocation(), 5);
		}
		
		if(e.getAxe().getSound() != null) {
			e.getLocation().getWorld().playSound(e.getLocation(), e.getAxe().getSound(), 10, 1);
		}
	}
	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		if(Utils.getStringList("disable-worlds").contains(e.getPlayer().getWorld().getName())) return;
		
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if(e.getHand() == EquipmentSlot.OFF_HAND) return;
			
			Axe axe = AxesPlugin.instance().getAxesManager().getAxe(e.getItem());
			
			if(axe != null) {
				
				if(axe.isDiminish()) {
					ItemStack item = e.getItem();
					item.setAmount(item.getAmount() - 1);

					e.getPlayer().getInventory().setItem(e.getHand(), item);
				}
				
				Bukkit.getScheduler().runTask(AxesPlugin.instance(), () -> AxeAnimation.animation(e.getPlayer(), axe, 2));
				
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onArmorStand(PlayerArmorStandManipulateEvent e) {
		if(e.getRightClicked().hasMetadata("waxes-armorstand")) {
			e.setCancelled(true);
		}
	}
}
