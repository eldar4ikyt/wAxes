package ru.winlocker.waxes.command;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.winlocker.waxes.Axe;
import ru.winlocker.waxes.AxesPlugin;
import ru.winlocker.waxes.utils.Utils;

public class CommandGive implements CommandSub {

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(args.length < 2) return false;
		
		Player player = Bukkit.getPlayer(args[0]);
		
		if(player == null) {
			
			Utils.sendMessage(sender, Utils.getMessage("player-null"));
			
			return true;
		}
		
		Axe axe = AxesPlugin.instance().getAxesManager().getAxe(args[1]);
		
		if(axe == null) {
			Utils.sendMessage(sender, Utils.getMessage("axe-null"));
			
			return true;
		}
		
		
		Utils.giveItem(player, axe.getItem());
		
		Utils.sendMessage(sender, Utils.getMessage("give.gived"));
		Utils.sendMessage(player, Utils.getMessage("give.player-gived").replace("%name%", axe.getDisplayName()));
		
		return true;
	}

	@Override
	public List<String> tab(CommandSender sender, String[] args) {
		
		if(args.length == 2) 
			return AxesPlugin.instance().getAxesManager().getAxes()
					.stream().map(x -> x.getName()).collect(Collectors.toList());
		
		return null;
	}

	@Override
	public String command() {
		return "give";
	}

	@Override
	public String permission() {
		return "give";
	}

	@Override
	public String description() {
		return Utils.getMessage("give.usage");
	}

	@Override
	public boolean onlyPlayers() {
		return false;
	}
}
