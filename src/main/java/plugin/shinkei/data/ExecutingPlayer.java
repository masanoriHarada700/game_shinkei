package plugin.shinkei.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import plugin.shinkei.command.Difficulty;

/**
 * コマンドを実行したプレイヤーのゲーム情報を格納するクラス
 */
@Getter
@Setter
public class ExecutingPlayer {

  private Player player;
  private String playerName;
  private int gameTime;
  private int sumScore;

  /**
   * コマンドを実行したプレイヤーのゲーム情報を格納
   *
   * @param player       コマンドを実行したプレイヤー
   * @param initScore    初期スコア
   * @param isDifficulty 難易度
   */
  public ExecutingPlayer(Player player, int initScore, Difficulty isDifficulty) {
    this.player = player;
    this.playerName = player.getName();
    this.sumScore = initScore;
    this.gameTime = isDifficulty.getGameTime();
  }
}
