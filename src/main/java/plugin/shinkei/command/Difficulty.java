package plugin.shinkei.command;

/**
 * 難易度の設定と各難易度に応じたゲーム時間、エンティティ出現位置とペアのランダム化リスト作成に
 * 必要な数値を定義するクラスです。
 */
public enum Difficulty {

    EASY("easy", 15, 1, 3),
    NORMAL("normal", 60, 3, 15),
    HARD("hard", 120, 5, 35),
    NONE("none", 0, 0, 0);


    private String difficulty;
    private int gameTime;
    private int loopCount_entityPosition;
    private int loopCount_dummyList;

    Difficulty(String difficulty, int gameTime, int loopCount_entityPosition, int loopCount_dummyList) {
        this.difficulty = difficulty;
        this.gameTime = gameTime;
        this.loopCount_entityPosition = loopCount_entityPosition;
        this.loopCount_dummyList = loopCount_dummyList;
    }

    public String preparedDifficulty() {
        return difficulty;
    }

    public int getGameTime() {
        return gameTime;
    }

    public int getLoopCount_entityPosition() {
        return loopCount_entityPosition;
    }

    public int getLoopCount_dummyList() {
        return loopCount_dummyList;
    }
}
