package plugin.shinkei.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import plugin.shinkei.mapper.data.PlayerScore;

import java.util.List;

public interface PlayerScoreMapper {

  @Select("select * from player_score;")
  List<PlayerScore> selectList();

  @Insert("insert player_score(player_name, difficulty, score, registered_dt) values(#{playerName}, #{difficulty}, #{score}, now())")
  int insert(PlayerScore playerScore);
}


