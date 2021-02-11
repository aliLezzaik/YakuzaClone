import java.util.Random;

/**
 * <code>LevelGen</code> class generates the waves inside the gameplay.
 * @version 1.00
 * @since 1.00
 */
public class LevelGen {
    /**
     * The level of the game
     */
    private int level;
    /**
     * The counter of the current wave in the game.
     */
    private int curWave;

    /**
     * The constructor.
     * @param level the level of the mission.
     */
    public LevelGen(int level) {
        this.level = level;
        curWave = 0;
    }

    /**
     * The current wave counter getter
     * @return the current wave counter.
     */
    public int getCurWave() {
        return curWave;
    }

    /**
     * This method is responsible for passing the random object.
     * @return random object.
     */
    private Random getRandom() {
        return Execute.getRandom();
    }

    /**
     * This method is responsible for making a wave according to the wave counter.
     * @param elementGenerator is the elementgenrator for adding the enemies.
     */
    public void makeWave(ElementGenerator elementGenerator) {
        curWave++;
        if (curWave<3) {
            for(int i =0;i<2;i++) {
                ElementGenerator.EnemyType type = null;
                while (type == null||(type == ElementGenerator.EnemyType.BOSS_LEVEL_1 || type == ElementGenerator.EnemyType.BOSS_LEVEL_2)) {
                    type = ElementGenerator.EnemyType.values()[getRandom().nextInt(ElementGenerator.EnemyType.values().length)];
                }
                elementGenerator.addEnemy(type,10,getRandom().nextInt(768));
            }
        } else if (curWave<=5) {
            for (int i =0;i<3;i++) {
                ElementGenerator.EnemyType type = null;
                while (type == null||(type == ElementGenerator.EnemyType.BOSS_LEVEL_1 || type == ElementGenerator.EnemyType.BOSS_LEVEL_2)) {
                    type = ElementGenerator.EnemyType.values()[getRandom().nextInt(ElementGenerator.EnemyType.values().length)];
                }
                elementGenerator.addEnemy(type,10,getRandom().nextInt(768));
            }
        } else {
            if (level == 1) {
                elementGenerator.addEnemy(ElementGenerator.EnemyType.BOSS_LEVEL_1, 10,500);
            } else {
                elementGenerator.addEnemy(ElementGenerator.EnemyType.BOSS_LEVEL_2, 10,500);
            }
        }
    }
}
