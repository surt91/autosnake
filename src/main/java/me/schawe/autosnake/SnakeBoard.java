package me.schawe.autosnake;

import javax.swing.*;
import java.awt.*;

public class SnakeBoard extends JPanel {
    private final SnakeLogic snake;
    private final int scale;

    private final Color colorBackground = new Color(0, 0, 0);
    private final Color colorFood = new Color(230, 20, 20);
    private final Color colorHead = new Color(140, 230, 140);
    private final Color colorTail = new Color(80, 230, 80);

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
        g.setColor(colorBackground);
        g.fillRect(0, 0, snake.getWidth() * scale, snake.getHeight() * scale);

        g.setColor(colorFood);
        g.fillRect(snake.getFood().getX() * scale, snake.getFood().getY() * scale, scale, scale);

        g.setColor(colorHead);
        g.fillRect(snake.getHead().getX() * scale, snake.getHead().getY() * scale, scale, scale);

        g.setColor(colorTail);
        for (Coordinate c : snake.getTail()) {
            g.fillRect(c.getX() * scale, c.getY() * scale, scale, scale);
        }
    }
}
