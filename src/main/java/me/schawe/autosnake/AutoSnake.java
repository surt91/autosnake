package me.schawe.autosnake;

import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class AutoSnake extends JFrame {
    SnakeLogic snakeLogic;
    JPanel panel;

    public AutoSnake() {
        int w = 10;
        int h = 10;
        this.snakeLogic = new SnakeLogic(w, h, "models/snakeAC_e1200.h5");
        initUI(w, h);
        mainLoop(50);
    }

    void mainLoop(int period) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("update!");
                if (snakeLogic.isGameOver()) {
                    snakeLogic.reset();
                }
                snakeLogic.update();
                panel.repaint();
            }
        }, 0, period);
    }

    private void initUI(int width, int height) {
        int scale = 20;

        panel = new SnakeBoard(snakeLogic, scale);
        add(panel);

        setSize(width * scale, height * scale);

        setTitle("AutoSnake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            AutoSnake ex = new AutoSnake();
            ex.setVisible(true);
        });
    }
}