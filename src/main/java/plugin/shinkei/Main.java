package plugin.shinkei;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.shinkei.command.GameStartCommand;


public final class Main extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    // Plugin startup logic
    GameStartCommand gameStartCommand = new GameStartCommand(this);
    Bukkit.getPluginManager().registerEvents(gameStartCommand, this);
    getCommand("gamestart").setExecutor(gameStartCommand);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
