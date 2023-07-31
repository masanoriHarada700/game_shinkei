package plugin.shinkei.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * ゲームのエンティティ情報を格納するクラスです。
 */
@Getter
@Setter
public class GameEntityInfo {

  public Player player;
  public Location entityLocation;
  public Entity entity;
  public Integer pairId;

  public GameEntityInfo(Player player, Location entityLocation, Entity entity, Integer pairID) {

    this.player = player;
    this.entityLocation = entityLocation;
    this.entity = entity;
    this.pairId = pairID;
  }
}
