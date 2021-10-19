"""
This script is very close to https://keras.io/examples/rl/actor_critic_cartpole/
"""

import sys
from glob import glob

import numpy as np
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers

from snake import Snake

# hyper-parameters
basename = "snakeAC"
gamma = 0.9
max_steps_per_episode = 10000
adam_lr = 0.001


vis = "vis" in sys.argv
if not vis:
    print(f"If you want to see visualizations, call this script like `{sys.argv[0]} vis`")

env = Snake()  # Create the environment
eps = np.finfo(np.float32).eps.item()  # Smallest number such that 1.0 + eps != 1.0

"""
## Implement Actor Critic network

This network learns two functions:

1. Actor: This takes as input the state of our environment and returns a
probability value for each action in its action space.
2. Critic: This takes as input the state of our environment and returns
an estimate of total rewards in the future.

In our implementation, they share the initial layer.
"""

num_inputs = env.state_size()
num_actions = env.action_size()
num_hidden = 256

inputs = layers.Input(shape=(num_inputs,))
common = layers.Dense(num_hidden, activation="relu")(inputs)
action = layers.Dense(num_actions, activation="softmax")(common)
critic = layers.Dense(1)(common)

# load a snapshot, if we have one
saves = glob(f"{basename}_e*.h5")
if saves:
    latest = sorted(saves, key=lambda x: int(x.split(".")[0].split("_e")[1]))[-1]
    start = int(latest.split(".")[0].split("_e")[1])
    print(f"load `{latest}`")
    model = keras.models.load_model(latest)
else:
    start = 0
    model = keras.Model(inputs=inputs, outputs=[action, critic])

"""
## Train
"""

optimizer = keras.optimizers.Adam(learning_rate=adam_lr)
huber_loss = keras.losses.Huber()
action_probs_history = []
critic_value_history = []
rewards_history = []
running_reward = 0
episode_count = start

while True:  # Run until solved
    state = env.reset()
    state = np.asarray(state)
    episode_reward = 0
    with tf.GradientTape() as tape:
        for timestep in range(max_steps_per_episode):
            if vis:
                env.render()

            state = tf.convert_to_tensor([state])

            # Predict action probabilities and estimated future rewards
            # from environment state
            action_probs, critic_value = model(state)
            critic_value_history.append(critic_value[0, 0])

            # Sample action from action probability distribution
            action = np.random.choice(num_actions, p=np.squeeze(action_probs))
            action_probs_history.append(tf.math.log(action_probs[0, action]))

            # Apply the sampled action in our environment
            state, reward, done = env.step(action)
            rewards_history.append(reward)
            episode_reward += reward

            if done:
                break

        # Update running reward to check condition for solving
        running_reward = 0.05 * episode_reward + (1 - 0.05) * running_reward

        # Calculate expected value from rewards
        # - At each timestep what was the total reward received after that timestep
        # - Rewards in the past are discounted by multiplying them with gamma
        # - These are the labels for our critic
        returns = []
        discounted_sum = 0
        for r in rewards_history[::-1]:
            discounted_sum = r + gamma * discounted_sum
            returns.insert(0, discounted_sum)

        # Normalize
        returns = np.array(returns)
        returns = (returns - np.mean(returns)) / (np.std(returns) + eps)
        returns = returns.tolist()

        # Calculating loss values to update our network
        history = zip(action_probs_history, critic_value_history, returns)
        actor_losses = []
        critic_losses = []
        for log_prob, value, ret in history:
            # At this point in history, the critic estimated that we would get a
            # total reward = `value` in the future. We took an action with log probability
            # of `log_prob` and ended up recieving a total reward = `ret`.
            # The actor must be updated so that it predicts an action that leads to
            # high rewards (compared to critic's estimate) with high probability.
            diff = ret - value
            actor_losses.append(-log_prob * diff)  # actor loss

            # The critic must be updated so that it predicts a better estimate of
            # the future rewards.
            critic_losses.append(
                huber_loss(tf.expand_dims(value, 0), tf.expand_dims(ret, 0))
            )

        # Backpropagation
        loss_value = sum(actor_losses) + sum(critic_losses)
        grads = tape.gradient(loss_value, model.trainable_variables)
        optimizer.apply_gradients(zip(grads, model.trainable_variables))

        # Clear the loss and reward history
        action_probs_history.clear()
        critic_value_history.clear()
        rewards_history.clear()

    # Log details
    episode_count += 1
    if episode_count % 10 == 0:
        template = "running reward: {:.2f} at episode {}"
        print(template.format(running_reward, episode_count))

    # save snapshots to continue training
    if episode_count % 300 == 0:
        model.save(f"{basename}_e{episode_count}.h5")

    # Condition to consider the task solved
    # our local information is not enough to get really high scores, so 20 is already good
    if running_reward > 20:
        print("Solved at episode {}!".format(episode_count))
        model.save(f"{basename}.h5")
        break

