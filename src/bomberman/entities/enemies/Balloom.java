package bomberman.entities.enemies;

import bomberman.entities.Bomber;
import bomberman.entities.Direction;
import bomberman.entities.Entity;
import bomberman.entities.Flame;
import bomberman.graphics.Sprite;
import bomberman.map.TileMap;
import bomberman.render.RenderWindow;
import bomberman.utilities.Vector2i;

import java.util.Random;

public class Balloom extends Enemy {
    // Thinh thoang doi huong
    private int BALLOOM_SCORE = 100;

    private Random random = new Random();

    public Balloom(int xUnit, int yUnit, TileMap map) {
        super(xUnit, yUnit, map);
    }

    @Override
    public void render(RenderWindow renderWindow) {
        if (this.animationDir == Direction.LEFT) {
            renderWindow.render(Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3,
                    (long) System.currentTimeMillis(), 400).getFxImage(), this.x, this.y);
        } else if (this.animationDir == Direction.RIGHT) {
            renderWindow.render(Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3,
                    (long) System.currentTimeMillis(), 400).getFxImage(), this.x, this.y);
        }
    }

    @Override
    protected Vector2i getNextMove() {
        /*List<Vector2i> freeCells = getFreeCell();
        if (freeCells.isEmpty()) {
            return new Vector2i(getRowIndex(), getColIndex());
        }
        int index = (int) (Math.random() * freeCells.size());
        return freeCells.get(index);*/
        int r = this.getRowIndex();
        int c = this.getColIndex();

        if (this.canDetectedPlayer()) {
            this.movingVector = this.randomDirection();
        }

        // Try to move current dir
        if (canMove(c + movingVector.x, r + movingVector.y)) {
            return new Vector2i(c + movingVector.x, r + movingVector.y);
            //return new Vector2i(0, 0);
        } else if (canMove(c - movingVector.x, r - movingVector.y)) {
            return new Vector2i(c - movingVector.x, r - movingVector.y);
            //return new Vector2i(0, 0);
        }

        return new Vector2i(c, r);
        //return new Vector2i(0, 0);
    }

    @Override
    public void onCollision(Entity other) {
        if (other instanceof Flame) {
            this.setActive(false);
            if (this.map.getCurrentEnemy() > 0 && enemyTimeDelay < 0) {
                enemyTimeDelay = ENEMY_TIME_DELAY;
                this.map.descreaseEnemy();
            }
            // ????
            this.map.addAnimation(Sprite.balloom_dead, Sprite.balloom_dead, Sprite.balloom_dead, this.getColIndex(), this.getRowIndex());
            this.map.increaseScore(BALLOOM_SCORE);
        }
        if (other instanceof Bomber bomber) {
            bomber.die();
        }
    }

    private Vector2i randomDirection() {
        if (this.random.nextBoolean()) {
            return new Vector2i(1, 0);
        } else {
            return new Vector2i(0, 1);
        }
    }

    private boolean canDetectedPlayer() {
        Vector2i playerPos = this.map.getPlayerPosition();
        boolean m = Math.abs(getColIndex() - playerPos.x) <= 1;
        boolean n = Math.abs(getRowIndex() - playerPos.y) <= 1;
        return m && n;
    }
}
