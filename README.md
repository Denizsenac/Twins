# Twins 🎮

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Console](https://img.shields.io/badge/Interface-Console-4D4D4D?style=for-the-badge&logo=windows-terminal&logoColor=white)

## Description
'Twins' is a 2D text-based maze game developed in Java. The core mechanics involve controlling a primary character ('A') with the ability to spawn a temporary clone ('B') at the current location. Gameplay consists of navigating procedurally generated mazes, collecting treasures for points, and avoiding hostile robots ('X'). Additionally, laser packs can be collected to fire powerful beams at enemies. The project features save/load functionality, dual game modes, and a dynamic scoring system. The game interface is rendered entirely within the console using the Enigma library.

## Table of Contents 📋
- [Features](#features-)
- [Tech Stack](#tech-stack-)
- [Installation & Usage](#installation--usage-)
- [How to Play](#how-to-play-️)
- [Project Structure](#project-structure-)
- [License](#license-)

## Features ✨
- **Maze Navigation:** Procedurally generated mazes with dynamic walls and pathways are explored.
- **Player and Clone Mechanics:** The primary player ('A') and a temporary clone ('B') are controlled for strategic gameplay.
- **Enemy Encounters:** Hostile 'X' robots that patrol the maze must be evaded or engaged.
- **Treasure Collection:** Treasures (represented by '1', '2', '3') are gathered to increase the score.
- **Laser Weapon System:** Laser packs ('@') can be collected to fire lasers at enemies.
- **Dual Game Modes:** Gameplay can be initiated via a randomized map or by loading a previously saved game.
- **Save/Load Functionality:** Game progress can be saved and resumed later.
- **Dynamic HUD:** Score, health, and laser ammunition are displayed in real-time.
- **Text-Based Console Interface:** A classic console gaming experience is provided via the Enigma library.

## Tech Stack 💻
- **Language:** Java
- **Console Interface:** Enigma Library (`enigma.core.Enigma`, `enigma.console.*`)
- **Core Libraries:** Standard Java Libraries (`java.awt.*`, `java.io.*`, `java.util.*`)

## Installation & Usage 🚀
The game is built entirely in Java. The Enigma console library (`Enigma-Edited2.jar`) is required for compilation and execution.

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/Denizsenac/Twins.git
   cd Twins
   ```

2. **Compile the Source Code:**
   Ensure `Enigma-Edited2.jar` is located in the `lib` folder, then compile the project from the root directory:
   ```bash
   javac -cp ".;lib/Enigma-Edited2.jar" src/Game/*.java
   ```
   *(Note: Use `:` instead of `;` for the classpath separator on Linux/macOS)*

3. **Run the Game:**
   ```bash
   java -cp ".;lib/Enigma-Edited2.jar" src/Game/Main
   ```
   *(Upon starting, a prompt will request the console window to be resized to fit the game area. Press ENTER when ready.)*

## How to Play 🕹️
- **Movement:** Arrow keys (UP, DOWN, LEFT, RIGHT) are used to move the character ('A').
- **Toggle Mode:** The 'M' key is pressed to switch between normal mode and clone mode. In clone mode, the clone ('B') attempts to mirror movements.
- **Fire Laser:** The SPACEBAR is used to fire a laser beam from character 'A' towards clone 'B'.
- **Save and Quit:** The ESC key saves current game progress and exits the application.
- **Navigate Menu:** UP/DOWN arrow keys are used to select a game mode; ENTER confirms the selection.

## Project Structure 📂
All game logic is contained within the `src/Game` directory.

```text
Twins/
├── src/
│   └── Game/
│       ├── BCharacter.java
│       ├── CollisionControl.java
│       ├── EnemyManager.java
│       ├── GameBoard.java
│       ├── GameEngine.java
│       ├── LaserManager.java
│       ├── Main.java
│       ├── MazeGenerator.java
│       ├── SaveLoad.java
│       ├── ScoreManager.java
│       └── (Other game classes...)
├── lib/
│   └── Enigma-Edited2.jar
└── README.md
```

## License 📄
This project is for educational purposes.

---
*Developed by Denizsenac.*
