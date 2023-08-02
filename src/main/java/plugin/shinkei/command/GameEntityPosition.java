package plugin.shinkei.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 出現させるエンティティの出現位置を決定するクラスです。
 */
public class GameEntityPosition {

  public Player player;
  public List<Integer> entityPositionListX = new ArrayList<>();
  public List<Integer> entityPositionListZ = new ArrayList<>();
  public final List<Location> gameEntityPositionList = new ArrayList<>();


  public GameEntityPosition(Player player, Difficulty isDifficulty) {
    this.player = player;

    int i;
    for (i = 0; i <= isDifficulty.getLoopCount_entityPosition(); i++) {
      entityPositionListX.add(i);
      entityPositionListZ.add(i);
    }
  }

  /**
   * 出現するEntityの出現場所をリストに格納する。
   */
  public void setEntityPositionList() {
    for (Integer variableX : entityPositionListX) {
      for (Integer variableZ : entityPositionListZ) {
        Integer positionX = variableX * 3 + 3;
        Integer positionZ = variableZ * 3 + 3;
        gameEntityPositionList.add(newEntityPosition(positionX, positionZ));
      }
    }
  }

  /**
   * プレイヤーを起点としたエンティティの出現場所を指定。
   *
   * @param positionX プレイヤー位置からX軸方向の相対距離
   * @param positionZ 　プレイヤー位置からZ軸方向の相対距離
   * @return エンティティの出現場所
   */
  private Location newEntityPosition(Integer positionX, Integer positionZ) {
    Location playerLocation = player.getLocation();
    double x = playerLocation.getX() + positionX;
    double y = playerLocation.getY() + 1;
    double z = playerLocation.getZ() + positionZ;
    return new Location(player.getWorld(), x, y, z);
  }
}
