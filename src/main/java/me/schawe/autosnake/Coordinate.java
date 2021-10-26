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

    public Coordinate add(Coordinate other){
        return new Coordinate(
                this.x + other.x,
                this.y + other.y
        );
    }

    public Coordinate left(Move direction){
        Coordinate left = direction.rLeft().toCoord();
        return this.add(left);
    }

    public Coordinate straight(Move direction){
        Coordinate straight = direction.toCoord();
        return this.add(straight);
    }

    public Coordinate right(Move direction){
        Coordinate right = direction.rRight().toCoord();
        return this.add(right);
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
