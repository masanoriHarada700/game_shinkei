package plugin.shinkei.command;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import plugin.shinkei.Main;
import plugin.shinkei.PlayerScoreData;
import plugin.shinkei.data.executingPlayer;
import plugin.shinkei.mapper.data.PlayerScore;

/**
 * 制限時間内にペアとなるエンティティを見つけて、スコアを獲得するゲームを起動するコマンドです。
 * 結果はプレイヤー名、点数、日時などで保存されます。
 */
public class GameStartCommand extends BaseCommand implements Listener{

  public static final int initScore = 0;
  public static final int getPoint = 10;
  public static final String LIST = "list";

  private final Main main;
  private final List<GameEntityInfo> gameEntityInfoList = new ArrayList<>();
  private final List<Integer> getPairIdList = new ArrayList<>();
  private final List<Integer> getEntityIdList = new ArrayList<Integer>();
  public List<Entity> entityList = new ArrayList<>();
  private final PlayerScoreData playerScoreData = new PlayerScoreData();
  private executingPlayer nowPlayer;


  public GameStartCommand(Main main) {
    this.main = main;
  }

  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label,
      String[] args) {
    if(args.length == 1 && LIST.equals(args[0])){
      playerSendMessage(player);
      return false;
    }

    initPlayerStatus(player);

    Difficulty isDifficulty = checkDifficulty(player, args);
    if (isDifficulty == Difficulty.NONE) {
      return false;
    }

    this.nowPlayer = new executingPlayer(player, initScore, isDifficulty);

    registerEntityInfo(player,isDifficulty);

    gamePlay(nowPlayer, isDifficulty);

    return true;
  }
  @Override
  public boolean onExecuteNPCCommand(CommandSender sender, Command command, String label,
      String[] args) {
    return false;
  }

  /**
   * プレイヤー画面にスコア情報を表示する。
   * @param player　コマンドを実行したプレイヤー
   */
  private void playerSendMessage(Player player) {
    List<PlayerScore> playerScoreList = playerScoreData.selectList();
    for(PlayerScore playerScore : playerScoreList){
        player.sendMessage(playerScore.getId() + " | "
            + playerScore.getPlayerName() + " | "
            + playerScore.getDifficulty() + " | "
            + playerScore.getScore() + " | "
            + playerScore.getRegisteredDt()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      }
  }
  /**
   *  ゲームを始める前にプレイヤーの状態を設定する。 体力と空腹度を最大にする。
   * @param player　コマンドを実行したプレイヤー
   */
  private void initPlayerStatus(Player player) {
    player.setHealth(20);
    player.setFoodLevel(20);
  }

  /**
   * ゲームの難易度を取得する
   * @param player　コマンドを実行したプレイヤー
   * @param args　コマンド引数
   * @return  ゲーム難易度
   *　
   */
  public Difficulty checkDifficulty(Player player, String[] args) {
    if(args.length == 1 &&
        (Difficulty.EASY.preparedDifficulty().equals(args[0]) ||
            Difficulty.NORMAL.preparedDifficulty().equals(args[0]) ||
            Difficulty.HARD.preparedDifficulty().equals(args[0]))){

      return receiveDifficulty(args);

      }
      player.sendMessage("引数の入力に誤りがあります。「easy」,「normal」,「hard」のいずれかを入力してください");
      return Difficulty.NONE;
    }

  /**
   * eunm定数のdifficultyと引数（ゲーム難易度）の文言が同じ場合,そのenum定数を返す。
   * @param args　プレイヤーが入力したゲーム難易度
   * @return  ゲーム難易度
   */
  private Difficulty receiveDifficulty(String[] args) {
    Difficulty receiveDifficulty = Arrays.stream(Difficulty.values())
        .filter(p -> p.preparedDifficulty().equals(args[0]))
        .findFirst()
        .orElse(Difficulty.NONE);
    return receiveDifficulty;
  }


  /**
   * 出現させたEntityに対して出現位置、Entity、ペア判定のためのIDを指定する。
   * 上記をインスタンス化して、リストに登録する。
   *
   * @param player　コマンドを実行したプレイヤー
   */
  private void registerEntityInfo(Player player, Difficulty isDifficulty) {
    GameEntityPosition entityPosition = new GameEntityPosition(player,isDifficulty);
    entityPosition.setEntityPositionList();

    GeneratePairs generatePairs = new GeneratePairs(isDifficulty);

    while(generatePairs.locationDummyList.size() > 0 && generatePairs.idDummyList.size() > 0 ) {

      generatePairs.selectNumber();

      Location selectEntityPosition =entityPosition.gameEntityPositionList.get(generatePairs.SelectLocationNumber);
      Entity entity = player.getWorld().spawnEntity(selectEntityPosition, EntityType.ENDER_CRYSTAL);
      entityList.add(entity);

      setGameEntityInfoList(player, generatePairs, selectEntityPosition, entity);

      removeDummyNumber(generatePairs);
    }
  }

  /**
   * プレイヤー・エンティティの位置情報・エンティティ情報・ペアIDの情報を
   * GameEntityInfoインスタンスに格納し、そのインスタンスをリストに追加。
   * @param player　コマンドを実行したプレイヤー
   * @param generatePairs　ペア生成用のインスタンス
   * @param selectEntityPosition　ランダムに選ばれたエンティティの出現場所
   * @param entity　上記のエンティティ情報
   */
  private void setGameEntityInfoList(Player player, GeneratePairs generatePairs, Location selectEntityPosition,
      Entity entity) {
    Integer pairID = generatePairs.pairIDList.get(generatePairs.SelectIdNumber);
    GameEntityInfo gameEntityInfo = new GameEntityInfo(player, selectEntityPosition, entity, pairID);
    gameEntityInfoList.add(gameEntityInfo);
  }

  /**
   * 割り当て済みのランダムナンバーをDummyListから取り除く
   * @param generatePairs　ペア生成用のインスタンス
   */
  private void removeDummyNumber(GeneratePairs generatePairs) {
    generatePairs.locationDummyList.remove(generatePairs.RandomLocationNumber);
    generatePairs.idDummyList.remove(generatePairs.RandomIdNumber);
  }

  /**
   * ゲームに時間制限を設ける。所定のゲーム時間を経過すると
   * プレイヤーの画面にゲーム終了のメッセージと獲得スコアを表示。
   * また、データベースへスコア情報を登録する。
   * @param nowPlayer　コマンドを実行したプレイヤー
   */
  private void gamePlay(executingPlayer nowPlayer, Difficulty isDifficulty) {
    Bukkit.getScheduler().runTaskTimer(main,
        Runnable -> {
          if (nowPlayer.getGameTime() <= 0) {
            Runnable.cancel();
            nowPlayer.getPlayer().sendTitle("ゲームが終了しました！",
                nowPlayer.getPlayerName() + "の合計スコアは" + nowPlayer.getSumScore() + "点！",
                0, 100, 0);

            playerScoreData.insert(new PlayerScore(nowPlayer.getPlayerName(), isDifficulty.preparedDifficulty(), nowPlayer.getSumScore()));

            entityList.forEach(Entity::remove);
            getPairIdList.clear();
            gameEntityInfoList.clear();

          }
          nowPlayer.setGameTime(0);
          //スコア登録情報
        },
        0, nowPlayer.getGameTime() * 20L);
  }

  /**
   * プレイヤーがエンティティに対して右クリックした際の基底メソッド。
   * @param e　プレイヤーがエンティティに対して右クリックした際のイベント情報
   * @return onEntityContactメソッドの遂行結果。遂行完了した場合、true。
   */
  @EventHandler
  public void onEntityContact(PlayerInteractEntityEvent e) {
    Player player = e.getPlayer();
    if (e.getHand() == EquipmentSlot.HAND) {
      setIdToList(e);

      checkSameEntity(player);

      checkMatching(player);
    }
  }

  /**
   * プレイヤーが右クリックしたエンティティのpairIdとentityIdを
   * それぞれ異なるリストに格納する。
   * @param e　プレイヤーがエンティティに対して右クリックした時のイベント情報
   */
  private void setIdToList(PlayerInteractEntityEvent e) {
    gameEntityInfoList.stream()
        .filter(p -> p.getEntity().getEntityId() == e.getRightClicked().getEntityId())
        .findFirst()
        .ifPresent(p -> {
          getPairIdList.add(p.getPairId());
          getEntityIdList.add(p.getEntity().getEntityId());
        });
  }

  /**
   * 2回目に触れたエンティティが１回目に触れたエンティティと同じ場合、
   * プレイヤーに異なるエンティティに触れるようにメッセージを出す。
   * @param player　コマンドを実行したプレイヤー
   * @return  同じエンティティならtrue, 異なるエンティティならfalse
   */
  private void checkSameEntity(Player player) {
    if (getEntityIdList.size() == 2 && getEntityIdList.get(0).equals(getEntityIdList.get(1))) {
      player.sendMessage("同じエンティティを選択しています。他のエンティティを選択してください");
      getPairIdList.remove(1);
      getEntityIdList.remove(1);
    }
  }

  /**
   * プレイヤーがエンティティに対して右クリックした回数が２回の場合、
   * ペアが成立可否を判定する。
   * ペア成立：成立ペアのエンティティをゲームから除外し、点数加算。
   *　ペア不成立：エンティティへの右クリックの回数がリセットされ、振り出しに戻る。
   * プレイヤーがエンティティに対して右クリックした回数が１回の場合、
   * プレイヤーに右クリックしたエンティティのIdを伝える。
   * @param player コマンドを実行したプレイヤー
   * @return  メソッドの完遂可否。完遂した場合、true。
   */
  private void checkMatching(Player player) {
    switch (getPairIdList.size()){
      case 1 -> player.sendMessage("ペア番号" + getPairIdList.get(0));
      case 2 -> {
        if(getPairIdList.get(0).equals(getPairIdList.get(1))){
          removeMatchingEntity();

          nowPlayer.setSumScore(nowPlayer.getSumScore() + getPoint);
          player.sendMessage("ペア成立！" + "合計スコア： " + nowPlayer.getSumScore() + "点");

        } else {
          player.sendMessage("ペア番号" + getPairIdList.get(1) + "\nペア不成立！" + "　改めてペアを探してください");
        }
        clearMatchingList();
      }
      default -> {
      }
    }
  }

  /**
   * マッチングした２つのエンティティをゲームから取り除く。
   */
  private void removeMatchingEntity() {
    gameEntityInfoList.stream()
        .filter(s -> s.getPairId() == getPairIdList.get(0))
        .map(GameEntityInfo::getEntity)
        .forEach(Entity::remove);
  }

  /**
   * プレイヤーが直近、連続で触れた2つのエンティティとそのPairIdが
   * 格納されたリストの中身を抹消する。
   */
  private void clearMatchingList() {
    getPairIdList.clear();
    getEntityIdList.clear();
  }
}
