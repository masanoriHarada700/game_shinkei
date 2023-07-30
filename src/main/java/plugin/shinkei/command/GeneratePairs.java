package plugin.shinkei.command;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

/**
 * ゲーム毎、エンティティ毎にエンティティの出現場所とペアIDをランダムに
 * 割り当てるための元になる情報を生成。
 */

public class GeneratePairs {

  public List<Integer> locationDummyList = new ArrayList<>();
  public List<Integer> idDummyList = new ArrayList<>();
  public List<Integer> pairIDList = new ArrayList<>();
  protected int RandomLocationNumber;
  protected int SelectLocationNumber;
  protected int RandomIdNumber;
  protected int SelectIdNumber;

  public GeneratePairs(Difficulty isDifficulty) {
    generateDummyList(isDifficulty);
    generatePairIdList();
  }

  /**
   * ゲーム起動ごとにランダムでエンティティの出現場所とペアIDを
   * 決定するために必要な数列リスト。
   * 難易度に応じて数列リストの要素数を変更。
   * @param isDifficulty　難易度
   */
  private void generateDummyList(Difficulty isDifficulty) {
    int i;
    for (i = 0; i <= isDifficulty.getLoopCount_dummyList(); i++) {
      locationDummyList.add(i);
      idDummyList.add(i);
    }
  }

  /**
   * ペアIDの一覧をリストに格納。
   * idDummyListの要素数の半分の数だけペアとなるIDを生成する
   */
  private void generatePairIdList(){
    int i;
    for(i = 1; i <= idDummyList.size()/2 ; i++){
      pairIDList.add(i);
      pairIDList.add(i);
    }
  }

  /**
   * エンティティの出現場所とペアIDをランダムに選択するための番号を取得
   */
  protected void selectNumber(){
    this.RandomLocationNumber = new SplittableRandom().nextInt(locationDummyList.size());
    this.SelectLocationNumber = locationDummyList.get(RandomLocationNumber);
    this.RandomIdNumber = new SplittableRandom().nextInt(idDummyList.size());
    this.SelectIdNumber = idDummyList.get(RandomIdNumber);
  }
}
