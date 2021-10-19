package me.schawe.autosnake;

import javax.swing.*;
import java.awt.*;

public class SnakeBoard extends JPanel {
    private final SnakeLogic snake;
    private final int scale;

    public SnakeBoard(SnakeLogic snake, int scale) {
        this.snake = snake;
        this.scale = scale;

        this.setPreferredSize(new Dimension(snake.getWidth() * scale, snake.getHeight() * scale));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawSnake(g);
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, snake.width * scale, snake.height * scale);

        g.setColor(Color.RED);
        g.fillRect(snake.food.getX() * scale, snake.food.getY() * scale, scale, scale);

        g.setColor(Color.GREEN);
        g.fillRect(snake.head.getX() * scale, snake.head.getY() * scale, scale, scale);

        g.setColor(Color.GREEN);
        for (Coordinate c : snake.tail) {
            g.fillRect(c.getX() * scale, c.getY() * scale, scale, scale);
        }
    }
}
