package ru.winlocker.waxes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import ru.winlocker.waxes.events.AxeHitEvent;

public class AxeAnimation extends BukkitRunnable {
	
	public static List<AxeAnimation> TASKS = Collections.synchronizedList(new ArrayList<>());

	
	
	
	
	public static AxeAnimation animation(Player shooter, Axe axe, int speed) {
		return animation(shooter, axe, shooter.getEyeLocation(), speed);
	}
	
	public static AxeAnimation animation(Player shooter, Axe axe, Location loc, int speed) {
		return animation(shooter, axe, loc, shooter.getLocation().getDirection(), speed);
	}
	
	public static AxeAnimation animation(Player shooter, Axe axe, Location loc, Vector vec, int speed) {
		return new AxeAnimation(shooter, axe, shooter.getEyeLocation(), shooter.getLocation().getDirection(), speed);
	}
	
	
	private Player shooter;
	private Axe axe;
	private ArmorStand armorStand;

	public AxeAnimation(Player shooter, Axe axe, Location loc, Vector vec, int speed) {
		this.shooter = shooter;
		this.axe = axe;
		
		ArmorStand stand = loc.getWorld().spawn(loc.subtract(0, 1, 0), ArmorStand.class);
		
		stand.setMetadata("waxes-armorstand", new FixedMetadataValue(AxesPlugin.instance(), "i am sexy"));
		
		stand.setVisible(false);
		stand.setInvulnerable(true);
		stand.setGravity(false);
		stand.setArms(true);
		
		stand.setRightArmPose(stand.getRightArmPose().subtract(3, 0, 0.2));
		stand.setItemInHand(axe.getItem());
		
		stand.setVelocity(vec);
		
		armorStand = stand;
		
		TASKS.add(this);
		
		this.runTaskTimer(AxesPlugin.instance(), 0, speed);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(!AxeAnimation.this.isCancelled()) {
					AxeHitEvent event = new AxeHitEvent(armorStand, axe, shooter, armorStand.getLocation(), null, null);
					Bukkit.getPluginManager().callEvent(event);
					
					AxeAnimation.this.cancel();
				}
				cancel();
			}
		}.runTaskLater(AxesPlugin.instance(), axe.getTime() * 20);
	}

	@Override
	public void run() {
		
		Location loc = this.armorStand.getLocation();
		
		Block block = loc.clone().add(loc.clone().getDirection().multiply(-0.5)).getBlock();

		if(block != null && block.getType().isSolid()) {
			
			AxeHitEvent event = new AxeHitEvent(armorStand, axe, shooter, loc, null, block);
			Bukkit.getPluginManager().callEvent(event);
			
			cancel();
			
			return;
		}
		
		for(Entity entity : this.armorStand.getNearbyEntities(0.5, 0.5, 0.5)) {
			
			if(!entity.equals(this.shooter) && !(entity instanceof ArmorStand) && !entity.isDead() && entity.getType().isAlive()) {
				if(this.armorStand.hasLineOfSight(entity)) {
					
					AxeHitEvent event = new AxeHitEvent(armorStand, axe, shooter, loc, entity, null);
					Bukkit.getPluginManager().callEvent(event);
					
					cancel();
					
					return;
				}
			}
		}
		
		EulerAngle oldAngle = this.armorStand.getRightArmPose();
		EulerAngle newAngle = oldAngle.add(0.4f, 0, 0);

		this.armorStand.setRightArmPose(newAngle);
		this.armorStand.teleport(loc.add(loc.getDirection()));
	}
	
	@Override
	public synchronized void cancel() {
		if(armorStand != null && !armorStand.isDead()) {
			armorStand.remove();
		}
		TASKS.remove(this);
		
		super.cancel();
	}
}
