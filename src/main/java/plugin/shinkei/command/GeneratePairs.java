package plugin.shinkei.command;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

/**
 * ゲーム毎、エンティティ毎にエンティティの出現場所とペアIDをランダムに
 * 割り当てるための元になる情報を生成。
 */
@Getter
@Setter
public class GeneratePairs {

  public List<Integer> locationDummyList = new ArrayList<>();
  public List<Integer> idDummyList = new ArrayList<>();
  public List<Integer> pairIDList = new ArrayList<>();
  private int randomLocationNumber;
  private int selectLocationNumber;
  private int randomIdNumber;
  private int selectIdNumber;

  public GeneratePairs(Difficulty isDifficulty) {
    generateDummyList(isDifficulty);
    generatePairIdList();
  }

  /**
   * ゲーム起動ごとにランダムでエンティティの出現場所とペアIDを
   * 決定するために必要な数列リスト。
   * 難易度に応じて数列リストの要素数を変更。
   *
   * @param isDifficulty 難易度
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
  private void generatePairIdList() {
    int i;
    for (i = 1; i <= idDummyList.size() / 2; i++) {
      pairIDList.add(i);
      pairIDList.add(i);
    }
  }

  /**
   * エンティティの出現場所とペアIDをランダムに選択するための番号を取得
   */
  protected void selectNumber() {
    this.randomLocationNumber = new SplittableRandom().nextInt(locationDummyList.size());
    this.selectLocationNumber = locationDummyList.get(randomLocationNumber);
    this.randomIdNumber = new SplittableRandom().nextInt(idDummyList.size());
    this.selectIdNumber = idDummyList.get(randomIdNumber);
  }
}
