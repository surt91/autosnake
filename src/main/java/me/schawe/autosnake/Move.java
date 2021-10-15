package me.schawe.autosnake;

import java.util.Random;

public enum Move {
    left,
    right,
    up,
    down;

    public Coordinate toCoord() {
        Coordinate offset;
        switch(this) {
            case up:
                offset = new Coordinate(0, -1);
                break;
            case down:
                offset = new Coordinate(0, 1);
                break;
            case left:
                offset = new Coordinate(-1, 0);
                break;
            case right:
                offset = new Coordinate(1, 0);
                break;
            default:
                throw new RuntimeException("unreachable!");
        }
        return offset;
    }

    public Move rLeft() {
        Move next;
        switch(this) {
            case up:
                next = Move.left;
                break;
            case down:
                next = Move.right;
                break;
            case left:
                next = Move.down;
                break;
            case right:
                next = Move.up;
                break;
            default:
                throw new RuntimeException("unreachable!");
        }
        return next;
    }

    public Move rRight() {
        Move next;
        switch(this) {
            case up:
                next = Move.right;
                break;
            case down:
                next = Move.left;
                break;
            case left:
                next = Move.up;
                break;
            case right:
                next = Move.down;
                break;
            default:
                throw new RuntimeException("unreachable!");
        }
        return next;
    }

    public Move straight() {
        return this;
    }

    public static Move random(Random rand) {
        double r = 4. * rand.nextFloat();
        if(r < 1) {
            return Move.up;
        } else if (r < 2) {
            return Move.down;
        } else if (r < 3) {
            return Move.left;
        } else if (r < 4) {
            return Move.right;
        }
        // this will not happen
        throw new RuntimeException("unreachable!");
    }
}
