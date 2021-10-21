import sys

import jpype
import jpype.imports
from jpype.types import *

# we expect that the current dir is py/

# Launch the JVM
jpype.startJVM(classpath=['../target/classes'])

# import the Java module
try:
    from me.schawe.autosnake import SnakeLogic
except ModuleNotFoundError:
    print("Please compile the Java project before calling this script")
    print("You can compile it by pasting the following into your terminal")
    print("cd .. ; ./mvnw clean package ; cd -")
    sys.exit(1)


class Snake:
    def __init__(self):
        width, height = 10, 10
        self.snakeLogic = SnakeLogic(width, height)

    def reset(self):
        self.snakeLogic.reset()

        return self.snakeLogic.trainingState()

    def step(self, action):
        self.snakeLogic.turnRelative(action)
        self.snakeLogic.update()

        state = self.snakeLogic.trainingState()

        done = False
        reward = 0
        if self.snakeLogic.isGameOver():
            reward = -1
            done = True
        elif self.snakeLogic.isEating():
            reward = 1

        return state, reward, done, {}

    # simple visualization to watch the training
    def render(self):
        import pygame_sdl2 as pygame

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
