package plugin.shinkei.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * コマンドを実行して動かすプラグイン処理の基底クラスです。
 */

public abstract class BaseCommand implements CommandExecutor {
  private Player player;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
    if (sender instanceof Player player) {
      this.player = player;
      return onExecutePlayerCommand(player, command, label, args);

    } else {
      return onExecuteNPCCCommand(sender, command, label, args);
    }
  }

  /**
   * コマンド実行者がプレイヤーだった場合に実行します。
   *
   * @param player コマンドを実行したプレイヤー
   * @param command　コマンド
   * @param label　ラベル
   * @param args　コマンド引数
   * @return  処理の実行有無
   */
  public abstract boolean onExecutePlayerCommand(Player player,Command command, String label, String[] args);


  /**
   * コマンド実行者がプレイヤー以外だった場合に実行します。
   *
   * @param sender コマンド実行者　
   * @param command　コマンド
   * @param label　ラベル
   * @param args　コマンド引数
   * @return  処理の実行有無
   */
  public abstract boolean onExecuteNPCCCommand(CommandSender sender, Command command, String label, String[] args);

  /**
   * プレイヤーがエンティティに対して右クリックした際の基底メソッド。
   * @param e　プレイヤーがエンティティに対して右クリックした際のイベント情報
   * @return onEntityContactメソッドの遂行結果。遂行完了した場合、true。
   */
  @EventHandler
  public boolean onBaseEntityContact(PlayerInteractEntityEvent e) {

    if (e.getPlayer().equals(player) && e.getHand() == EquipmentSlot.HAND) {
      return isMatching(e);
    }
    return false;
  }
  public abstract boolean isMatching(PlayerInteractEntityEvent e);
}


