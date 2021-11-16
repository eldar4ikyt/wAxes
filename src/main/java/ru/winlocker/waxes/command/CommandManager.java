package ru.winlocker.waxes.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import ru.winlocker.waxes.utils.Utils;

public class CommandManager implements CommandExecutor, TabCompleter {

	private List<CommandSub> commands = new ArrayList<>();
	
	public void registerCommand(CommandSub sub) {
		this.commands.remove(sub);
		this.commands.add(sub);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
			if(!getAllowedCommands(sender).isEmpty()) {
				getAllowedCommands(sender).forEach(x -> Utils.sendMessage(sender, x.description()));
			}
			else {
				Utils.sendMessage(sender, Utils.getMessage("no-allowed"));
			}
			return true;
		}
		
		CommandSub command = getCommand(args[0]);
		
		if(command != null) {
			
			if(command.onlyPlayers() && !(sender instanceof Player)) {
				Utils.sendMessage(sender, Utils.getString("only-players"));
			}
			else if(command.permission() != null && !sender.hasPermission("waxes." + command.permission())) {
				Utils.sendMessage(sender, Utils.getString("no-permission"));
			}
			else {
				List<String> argsList = new ArrayList<>(Arrays.asList(args));
				argsList.remove(0);
				
				try {
					if(!command.execute(sender, argsList.toArray(new String[argsList.size()]))) {
						Utils.sendMessage(sender, command.description());
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					Utils.sendMessage(sender, "Произошла ошибка при выполнении данной команды. Обратитесь к администратору");
				}
			}
		}
		else {
			Utils.sendMessage(sender, Utils.getString("unknown"));
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 1) {
			return filter(getAllowedCommands(sender)
					.stream()
					.map(x -> x.command()).collect(Collectors.toList()), args);
		}
		
		if(args.length >= 2) {
			CommandSub command = getCommand(args[0]);
			
			if(command != null) {
				
				if(command.onlyPlayers() && !(sender instanceof Player)) return null;
				if(command.permission() != null && !sender.hasPermission("waxes." + command.permission())) return null;
				
				List<String> argsList = new ArrayList<>(Arrays.asList(args));
				argsList.remove(0);
				
				return filter(command.tab(sender, argsList.toArray(new String[argsList.size()])), args);
			}
		}
		return null;
	}
	
	private List<String> filter(List<String> list, String[] args) {
		if(list == null) return null;
		
		String last = args[args.length - 1].toLowerCase();
		List<String> result = new ArrayList<>();
		
		for(String s : list) 
			if(s.startsWith(last))
				result.add(s);
		
		return result;
	}

	public CommandSub getCommand(String command) {
		return this.commands.stream().filter(x -> x.command().equalsIgnoreCase(command)).findAny().orElse(null);
	}
	
	public List<CommandSub> getAllowedCommands(CommandSender sender) {
		return this.commands.stream()
				.filter(x -> (x.onlyPlayers() ? sender instanceof Player : true) &&
						(x.permission() == null ? true : sender.hasPermission(x.permission())))
				.collect(Collectors.toList());
	}
}
