package plugin.shinkei.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import plugin.shinkei.command.GameStartCommand;

@Getter
@Setter

/**
  コマンドを実行したプレイヤーのゲーム情報を格納するクラス
 */
public class ExecutingPlayer {

  private Player player;
  private String playerName;
  private int gameTime;
  private int sumScore;

  /**
   * コマンドを実行したプレイヤーのゲーム情報を格納
   * @param player　コマンドを実行したプレイヤー
   * @param initScore　初期スコア
   * @param difficulty　難易度
   */
  public ExecutingPlayer(Player player, int initScore, String difficulty){
    this.player = player;
    this.playerName = player.getName();
    this.sumScore = initScore;
    GameTime(difficulty);
  }

  /**
   * 難易度に応じたゲーム時間を設定
   * @param difficulty　難易度
   */
  public void GameTime(String difficulty){
    switch (difficulty) {
      case GameStartCommand.NORMAL -> {
        this.gameTime = 60;
      }
      case GameStartCommand.HARD -> {
        this.gameTime = 120;
      }
      default -> {
        this.gameTime = 15;
      }
    }
  }
}
