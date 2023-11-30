# UNO PROJECT BY Provenzi & Dotto

## Server

The server is implemented in Python. It manages the game logic and maintains the game state. It communicates with the clients through a socket-based protocol (TCP).

### Setup and Usage

1. Navigate to the server directory.
2. Install the required Python dependencies.
3. Run the server script using Python.

## Client

The client is a graphical user interface (GUI) implemented in Java. It communicates with the server (TCP) to send player actions and receive game updates.

### Setup and Usage

1. Navigate to the client directory.
2. Install the required Java dependencies.
3. Run the GUI using Java.

## TCP Protocol

The Transmission Control Protocol (TCP) is a communication protocol used in this project for sending data packets over the network. It is a reliable, ordered, and error-checked delivery of a stream of bytes between applications running on hosts communicating via an IP network.

In the context of the Uno project:

- The server uses TCP to send game updates to the clients and receive player actions.
- The client uses TCP to send player actions to the server and receive game updates.

TCP ensures that all game updates and player actions are delivered in the correct order and without errors, which is crucial for maintaining the game state and providing a smooth gameplay experience.

## Game Logic

The game logic for the Uno project is implemented in the server. It follows the standard rules of Uno with some additional features:

- The game starts with each player being dealt 7 cards.
- Players take turns in a clockwise direction, playing a card that matches the color or number of the top card on the discard pile.
- If a player cannot play a card, they must draw a card from the deck.
- Special cards like Skip, Reverse, and Draw Two introduce additional gameplay mechanics.
- The game ends when a player has no cards left.

The server manages the game state, including the discard pile, and the current direction of play. It also enforces the game rules, such as matching the color or number of the top card on the discard pile and handling special cards.