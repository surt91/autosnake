package me.schawe.autosnake;

public class Coordinate {
    public int x;
    public int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinate(int xi, int yi) {
        x = xi;
        y = yi;
    }

    public Coordinate copy() {
        return new Coordinate(x, y);
    }

    public void addAssign(Coordinate other){
        this.x += other.x;
        this.y += other.y;
    }

    public Coordinate add(Coordinate other){
        return new Coordinate(
                this.x + other.x,
                this.y + other.y
        );
    }

    public boolean equals(Coordinate other) {
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
