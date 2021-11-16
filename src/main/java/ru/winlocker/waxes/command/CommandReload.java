package ru.winlocker.waxes.command;

import java.util.List;

import org.bukkit.command.CommandSender;

import ru.winlocker.waxes.AxesPlugin;
import ru.winlocker.waxes.utils.Utils;

public class CommandReload implements CommandSub {

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		AxesPlugin.instance().reloadConfig();
		
		Utils.sendMessage(sender, Utils.getMessage("reload.reloaded"));
		
		return true;
	}

	@Override
	public List<String> tab(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public String command() {
		return "reload";
	}

	@Override
	public String permission() {
		return "reload";
	}

	@Override
	public String description() {
		return Utils.getMessage("reload.usage");
	}

	@Override
	public boolean onlyPlayers() {
		return false;
	}

}
