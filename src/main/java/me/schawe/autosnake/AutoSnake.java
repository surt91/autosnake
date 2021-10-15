package me.schawe.autosnake;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class AutoSnake extends JFrame {
    SnakeLogic snakeLogic;

    public AutoSnake() {
        int w = 10;
        int h = 10;
        this.snakeLogic = new SnakeLogic(w, h);
        initUI(w, h);
    }

    private void initUI(int width, int height) {
        int scale = 20;

        add(new SnakeBoard(snakeLogic, scale));

        setSize(width * scale, height * scale);

        setTitle("AutoSnake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        PythonEntry.run();

        EventQueue.invokeLater(() -> {
            AutoSnake ex = new AutoSnake();
            ex.setVisible(true);
        });
    }
}