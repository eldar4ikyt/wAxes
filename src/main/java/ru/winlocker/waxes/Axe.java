package ru.winlocker.waxes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import ru.winlocker.waxes.item.ItemNBTWrapper;

public class Axe {

	private String name;
	private ItemStack item;
	private UUID uuid;
	
	private double damage;
	private int time;
	
	private Particle particle;
	private Sound sound;
	
	private boolean diminish;
	
	private List<PotionEffect> effects = new ArrayList<>();
	
	public Axe(String name, ItemStack item, double damage, int time, Particle particle, Sound sound, boolean diminish, List<PotionEffect> effects, UUID uuid) {
		this.name = name;
		this.particle = particle;
		this.sound = sound;
		this.damage = damage;
		this.time = time;
		this.diminish = diminish;
		this.uuid = uuid;
		this.effects = effects;
		
		ItemNBTWrapper wrapper = new ItemNBTWrapper(item);
		wrapper.setNBT("waxes-uuid", this.uuid.toString());
		
		this.item = wrapper.getItem();
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public String getDisplayName() {
		if(!getItem().hasItemMeta() || !getItem().getItemMeta().hasDisplayName()) 
			return getItem().getType().name();
		
		return getItem().getItemMeta().getDisplayName();
	}
	
	public List<PotionEffect> getEffects() {
		return effects;
	}
	
	public void setEffects(List<PotionEffect> effects) {
		this.effects = effects;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public boolean isDiminish() {
		return diminish;
	}
	
	public void setDiminish(boolean diminish) {
		this.diminish = diminish;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public void setDamage(double damage) {
		this.damage = damage;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public Particle getParticle() {
		return particle;
	}
	
	public void setParticle(Particle particle) {
		this.particle = particle;
	}
	
	public Sound getSound() {
		return sound;
	}
	
	public void setSound(Sound sound) {
		this.sound = sound;
	}
}
