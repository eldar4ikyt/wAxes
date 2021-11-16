package ru.winlocker.waxes.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.winlocker.waxes.Axe;

public class AxeHitEvent extends Event {

	private Player shooter;
	private Location location;
	private Entity entity;
	private Block block;
	private Axe axe;
	private ArmorStand armorStand;
	
	public AxeHitEvent(ArmorStand armorStand, Axe axe, Player shooter, Location location, Entity entity, Block block) {
		this.axe = axe;
		this.shooter = shooter;
		this.location = location;
		this.entity = entity;
		this.block = block;
		this.armorStand = armorStand;
	}
	
	public Player getShooter() {
		return shooter;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Entity getEntityAttached() {
		return entity;
	}
	
	public Block getBlockAttached() {
		return block;
	}

	public Axe getAxe() {
		return axe;
	}

	public ArmorStand getArmorStand() {
		return armorStand;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
