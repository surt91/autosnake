import pygame_sdl2 as pygame

import jpype
import jpype.imports
from jpype.types import *

# Launch the JVM
jpype.startJVM(classpath=['../../../target/autosnake-1.0-SNAPSHOT.jar'])

# import the Java module
from me.schawe.autosnake import SnakeLogic


class Snake:
    def __init__(self, vis):
        self.vis = vis
        if vis:
            pygame.init()

        width, height = 10, 10
        self.snakeLogic = SnakeLogic(width, height)

        self.state = []

    def reset(self):
        self.snakeLogic.reset()

        return self.snakeLogic.trainingState()

    def render(self):
        if not self.vis:
            return
        scale = 20
        w = self.snakeLogic.getWidth()
        h = self.snakeLogic.getHeight()

        screen = pygame.display.set_mode((w * scale, h * scale))
        pygame.draw.rect(
            screen,
            [0, 0, 0],
            [0, 0, w * scale, h * scale]
        )

        food = self.snakeLogic.getFood()
        pygame.draw.rect(
            screen,
            [230, 20, 20],
            [scale * food.getX(), scale * food.getY(), scale, scale]
        )

        pygame.draw.rect(
            screen,
            [140, 230, 140],
            [scale * self.snakeLogic.getHead().getX(), scale * self.snakeLogic.getHead().getY(), scale, scale]
        )

        for i in self.snakeLogic.getTail():
            pygame.draw.rect(
                screen,
                [80, 230, 80],
                [scale * i.getX(), scale * i.getY(), scale, scale]
            )

        pygame.display.update()

    def step(self, action):
        self.snakeLogic.turnRelative(action)
        self.snakeLogic.update()

        state = self.snakeLogic.trainingState()
        self.state = state

        done = False
        reward = 0
        if self.snakeLogic.isGameOver():
            reward = -1
            done = True
            if self.vis:
                print("dead")
        elif self.snakeLogic.isEating():
            reward = 1
            if self.vis:
                print("nom", end=" ")

        return state, reward, done

    def state_size(self):
        return len(self.snakeLogic.trainingState())

    def action_size(self):
        return 3

    def max_reward(self):
        return 150
