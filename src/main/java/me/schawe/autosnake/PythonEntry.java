package me.schawe.autosnake;

import py4j.GatewayServer;

// This class enables external calls from python to the SnakeLogic object.
// This is especially useful to apply the vast machine learning ecosystem
// of Python to train an AI to steer the snake.
public class PythonEntry {

    public PythonEntry() {
    }

    public SnakeLogic getSnakeLogic() {
        return new SnakeLogic(10, 10);
    }

    public static void run() {
        GatewayServer gatewayServer = new GatewayServer(new PythonEntry());
        gatewayServer.start();
        System.out.println("Python Gateway Server Started");
    }
}
