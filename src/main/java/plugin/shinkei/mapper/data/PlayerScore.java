package plugin.shinkei.mapper.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * プレイヤーのスコア情報を扱うオブジェクト
 * データベースへの登録データも兼ねる。
 */
@Getter
@Setter
@NoArgsConstructor
public class PlayerScore {

  private int id;
  private String playerName;
  private String difficulty;
  private int score;
  private LocalDateTime registeredDt;

  public PlayerScore(String playerName, String difficulty, int score) {
    this.playerName = playerName;
    this.score = score;
    this.difficulty = difficulty;
  }
}
