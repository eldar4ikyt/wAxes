package ru.winlocker.waxes;

import org.bukkit.plugin.java.JavaPlugin;

import ru.winlocker.waxes.command.CommandGive;
import ru.winlocker.waxes.command.CommandManager;
import ru.winlocker.waxes.command.CommandReload;
import ru.winlocker.waxes.utils.Utils;

public class AxesPlugin extends JavaPlugin {
	
	private static AxesPlugin instance;
	private CommandManager commandManager;
	private AxesManager axesManager;
	
	public static AxesPlugin instance() {
		return instance;
	}
	
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public AxesManager getAxesManager() {
		return axesManager;
	}
	
	@Override
	public void onEnable() {
		instance = this;

		getServer().getPluginManager().registerEvents(new AxeListener(), this);

		reloadConfig();
		
		commandManager = new CommandManager();
		commandManager.registerCommand(new CommandGive());
		commandManager.registerCommand(new CommandReload());
		
		getCommand("waxe").setExecutor(commandManager);
		
		getLogger().info("Плагин успешно включён. Создатель плагина WinLocker - vk.com/winlocker02");
	}
	
	@Override
	public void onDisable() {
		axesManager.disableManager();
		AxeAnimation.TASKS.forEach(x -> x.cancel());
	}
	
	@Override
	public void reloadConfig() {
		Utils.reloadConfig();
		axesManager = new AxesManager();
	}
}
