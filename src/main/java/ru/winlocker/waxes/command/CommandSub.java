package ru.winlocker.waxes.command;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface CommandSub {

	boolean execute(CommandSender sender, String[] args);
	List<String> tab(CommandSender sender, String[] args);
	
	String command();
	String permission();
	String description();
	boolean onlyPlayers();
}
