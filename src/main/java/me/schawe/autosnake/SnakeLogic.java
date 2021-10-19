package me.schawe.autosnake;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeLogic {
    int width;
    int height;
    Coordinate head;
    Move headDirection;
    ArrayDeque<Coordinate> tail;
    int length;
    Coordinate food;
    boolean gameOver;
    private final Random random;
    Autopilot autopilot = null;

    public SnakeLogic(int width, int height) {
        this(width, height, null);
    }

    public SnakeLogic(int width, int height, String autopilotModelPath) {
        this.width = width;
        this.height = height;
        head = new Coordinate(-1, -1);
        this.tail = new ArrayDeque<>();
        this.random = new Random();
        if (autopilotModelPath != null) {
            autopilot = new Autopilot(autopilotModelPath);
        }
        reset();
    }

    // TODO: replace by a cheaper method (hashmap or bitmap of occupied sites?)
    //   But probably does not matter for performance yet
    public boolean isOccupied(Coordinate site) {
        return tail.stream().anyMatch(c -> c.equals(site)) || head.equals(site);
    }

    public boolean isWall(Coordinate coordinate) {
        return coordinate.x < 0 || coordinate.x >= width || coordinate.y < 0 || coordinate.y >= height;
    }

    public boolean isEating() {
        return head.equals(food);
    }

    // FIXME: this will become an infinite loop after a perfect game
    private Coordinate randomSite() {
        Coordinate site;
        do {
            site = new Coordinate((int) (random.nextFloat() * width), (int) (random.nextFloat() * height));
        } while (isOccupied(site));
        return site;
    }

    public void add_food() {
        food = randomSite();
    }

    public int danger(Coordinate c) {
        if(isOccupied(c) || isWall(c)) {
            return 1;
        }
        return 0;
    }

    double angle(Coordinate subject, Move direction, Coordinate object) {
        double rad;
        double dx = object.getX() - subject.getX();
        double dy = object.getY() - subject.getY();

        // apply coordinate rotation, such that the snake always looks to the right
        // from the point of view of the atan
        // also note that our coordinate system grows down, so up points to lower values of y
        switch (direction) {
            case right:
                rad = -Math.atan2(dy, dx);
                break;
            case up:
                rad = Math.atan2(-dx, -dy);
                break;
            case left:
                rad = -Math.atan2(-dy, -dx);
                break;
            case down:
                rad = Math.atan2(dx, dy);
                break;
            default:
                throw new RuntimeException("unreachable!");
        }

        return rad;
    }

    public List<Integer> trainingState() {
        ArrayList<Integer> state = new ArrayList<>();

        double rad = angle(head, headDirection, food);
        double eps = 1e-6;

        // is food in front?
        if (Math.abs(rad) < Math.PI / 2 - eps) {
            state.add(1);
        } else {
            state.add(0);
        }

        // is food left?
        if (rad > eps && rad < Math.PI - eps) {
            state.add(1);
        } else {
            state.add(0);
        }

        // is food right?
        if (rad < -eps && rad > -Math.PI + eps) {
            state.add(1);
        } else {
            state.add(0);
        }

        // is food behind?
        if (Math.abs(rad) > Math.PI/2. + eps) {
            state.add(1);
        } else {
            state.add(0);
        }

        Coordinate straight = headDirection.toCoord();
        Coordinate left = headDirection.rLeft().toCoord();
        Coordinate right = headDirection.rRight().toCoord();

        // first neighbors
        state.add(danger(head.add(left)));
        state.add(danger(head.add(straight)));
        state.add(danger(head.add(right)));
        // omit back, its always occupied

        return state;
    }

    // this takes an int, which is the output of the neural network
    public void turnRelative(int direction) {
        switch(direction) {
            case 0:
                // left
                headDirection = headDirection.rLeft();
                break;
            case 1:
                // straight -> direction does not change
                break;
            case 2:
                // right
                headDirection = headDirection.rRight();
                break;
            default:
                throw new RuntimeException("invalid relative direction: " + direction);
        }
    }

    public void kill() {
        gameOver = true;
    }

    public void reset() {
        headDirection = Move.random(random);
        head = randomSite();
        tail.clear();
        length = 2;
        gameOver = false;

        add_food();
    }

    public void update() {
        if(gameOver) {
            return;
        }

        if (autopilot != null) {
            int action = autopilot.nextMove(trainingState());
            turnRelative(action);
        }

        Coordinate offset = headDirection.toCoord();

        tail.add(head.copy());

        if (isEating()) {
            length += 1;
            add_food();
        }

        while (tail.size() >= length + 1) {
            tail.remove();
        }

        Coordinate next = head.add(offset);
        if (isWall(next) || isOccupied(next)) {
            kill();
        }

        head = next;
    }

    public Coordinate getHead() {
        return head;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Coordinate getFood() {
        return food;
    }

    public Move getHeadDirection() {
        return headDirection;
    }

    public ArrayDeque<Coordinate> getTail() {
        return tail;
    }

    public int getLength() {
        return length;
    }
}
