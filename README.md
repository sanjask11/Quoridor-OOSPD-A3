# CS611 - Assignment III
## Quoridor

------------------------------------------------------------  
- **Group Member 1**
  - **Name:** Sanjana
  - **Email:** sanjask@bu.edu
  - **Student ID:** U33564045


- **Group Member 2**
  - **Name:** Alessandro
  - **Email:** al6723@bu.edu
  - **Student ID:** U63309114

------------------------------------------------------------  
# Assignment Overview

This assignment **Terminal Game Arcade** was developed as part of the *Object-Oriented Software Principles and Design (OOSPD)* course.  
It demonstrates clean Object Oriented design, modular architecture, and extensible gameplay logic through a **multi game terminal arcade** implemented in Java.

The arcade currently supports three games:

| Game | Type | Description |
|------|------|-------------|
| **Sliding Puzzle** | Single-Player | Rearrange tiles into their correct order using moves until the board is solved. |
| **Dots & Boxes** | Two-Player | Claim edges on a grid to complete boxes and score points. |
| **Quoridor** | Two-Player | Strategic path-blocking game where players race to reach the opposite side of the board. |


## Core Features
### Multi-Game Arcade
- Choose from **three games**: *Sliding Puzzle*, *Dots and Boxes*, and *Quoridor*.
- Each game is encapsulated and runs independently within the same application.

### Player Modes
- **Single-player**: Sliding Puzzle  
- **Two-player**: Dots and Boxes, Quoridor  
- Supports unique player names based on user preference.

### Pause & Resume System
- Press **`p`** at any time to pause the current game.
- Choose to:
  1. **Resume** the ongoing game  
  2. **Save and return** to main menu which leads to the in-session save  
  3. **Quit** the game entirely  
- Saved games are kept in memory during the session and can be resumed from the main menu.

### In Session Save System
- Each game maintains **one saved state per session**.
- No external files are used here.  
- Automatically clears when the application closes allowing it to only be saved while the terminal is running.

### Universal Board Rendering
- Consistent ASCII-style board for all games.
- Supports both grid based and tile based visuals.  
- Dynamically adapts to the user preferred board dimensions.

### Unified Input Handling
- Clean and consistent player prompts via the `InputHandler` class.  
- Supports flexible commands (e.g., **`H 1 2`** for Horizontal bar for row 1 and col 2, **`V 3 4`**, **`M U`** for Move upwards, **`p`** for pause, **`q`** for quit).  
- Handles invalid input by throwing an exception and letting the user know their mistake without breaking gameplay.

### Intelligent Initialization
- All games validate board dimensions and generate solvable or valid configurations.  
- Sliding Puzzle ensures generated puzzles are **always solvable** and that the user isn't presented with a board which can't be solved.



## How to Compile & Run the assignment
- Clone the repository
  - **`git clone https://github.com/sanjask11/Quoridor-OOSPD-A3.git`**  
- Compile the code in Java version 8
  - **`cd Quoridor-OOSPD-A3/src`**   
  - **`javac -source 1.8 -target 1.8 *.java`**
- Run the java file
  - **`java App`**

## Sample Input & Output
```text
/Users/sanjanasanjeevkumar/Library/Java/JavaVirtualMachines/openjdk-24.0.2+12-54/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=59290 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /Users/sanjanasanjeevkumar/Downloads/src 3/out/production/src 3 App

=== Welcome to Terminal Game Arcade !!!! ===
Please follow the instructions in order to proceed! Press q to exit.

Which game would you like to play?
0. Sliding Puzzle
1. Dots And Boxes
2. Quoridor

Your choice: 0
What is your name?: sana

Choose your board dimensions M x N
(Each dimension has to be greater than 0 and at most 10)
Rows: 2
Columns: 2
+---+---+
| 3 | 1 |
+---+---+
| 2 |   |
+---+---+
(Enter move or 'p' to pause): 1

+---+---+
| 3 |   |
+---+---+
| 2 | 1 |
+---+---+
(Enter move or 'p' to pause): 3

+---+---+
|   | 3 |
+---+---+
| 2 | 1 |
+---+---+
(Enter move or 'p' to pause): 2

+---+---+
| 2 | 3 |
+---+---+
|   | 1 |
+---+---+
(Enter move or 'p' to pause): 1

+---+---+
| 2 | 3 |
+---+---+
| 1 |   |
+---+---+
(Enter move or 'p' to pause): 3

+---+---+
| 2 |   |
+---+---+
| 1 | 3 |
+---+---+
(Enter move or 'p' to pause): 2

+---+---+
|   | 2 |
+---+---+
| 1 | 3 |
+---+---+
(Enter move or 'p' to pause): 1

+---+---+
| 1 | 2 |
+---+---+
|   | 3 |
+---+---+
(Enter move or 'p' to pause): 3

+---+---+
| 1 | 2 |
+---+---+
| 3 |   |
+---+---+

Congratulations, sana! You solved the puzzle!

What would you like to do?
1. Play again (same game)
2. Play a different game
3. Quit
Enter your choice (1-3): 2

Which game would you like to play?
0. Sliding Puzzle
1. Dots And Boxes
2. Quoridor

Your choice: 2
What is Player 1's name?: sana
What is Player 2's name?: alex

Quoridor (9x9) Instructions:
Move Pawn: M [U/D/L/R] → Move your pawn one square: Up, Down, Left, or Right.
Wall (1-indexed): H r c   → Place horizontal wall starting at (r, c) (spans c and c+1)
Wall (1-indexed): V r c   → Place vertical wall starting at (r, c) (spans r and r+1)
Q                 → Quit

Walls Remaining:
sana (S): 10 | alex (A): 10
It is sana's turn.
+---+---+---+---+---+---+---+---+---+
|                 A                 |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                 S                 |
+---+---+---+---+---+---+---+---+---+
(Enter move or 'p' to pause): m u

Walls Remaining:
sana (S): 10 | alex (A): 10
It is alex's turn.
+---+---+---+---+---+---+---+---+---+
|                 A                 |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                 S                 |
+   +   +   +   +   +   +   +   +   +
|                                   |
+---+---+---+---+---+---+---+---+---+
(Enter move or 'p' to pause): m u

Error: Move is out of board bounds.


Walls Remaining:
sana (S): 10 | alex (A): 10
It is alex's turn.
+---+---+---+---+---+---+---+---+---+
|                 A                 |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                 S                 |
+   +   +   +   +   +   +   +   +   +
|                                   |
+---+---+---+---+---+---+---+---+---+
(Enter move or 'p' to pause): m l

Walls Remaining:
sana (S): 10 | alex (A): 10
It is sana's turn.
+---+---+---+---+---+---+---+---+---+
|             A                     |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                 S                 |
+   +   +   +   +   +   +   +   +   +
|                                   |
+---+---+---+---+---+---+---+---+---+
(Enter move or 'p' to pause): p

=== Game Paused ===
1. Resume
2. Save and return to main menu
3. Quit game
Choice: 2
Game saved. Returning to main menu...

What would you like to do?
1. Play again (same game)
2. Play a different game
3. Quit
Enter your choice (1-3): 2

Which game would you like to play?
0. Sliding Puzzle
1. Dots And Boxes
2. Quoridor

Your choice: 2
What is Player 1's name?: sana
What is Player 2's name?: alex

A saved Quoridor game was found. Resume it? (y/n): y
Resuming your previous game...


Walls Remaining:
sana (S): 10 | alex (A): 10
It is sana's turn.
+---+---+---+---+---+---+---+---+---+
|             A                     |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                 S                 |
+   +   +   +   +   +   +   +   +   +
|                                   |
+---+---+---+---+---+---+---+---+---+
(Enter move or 'p' to pause): h 1 1

Walls Remaining:
sana (S): 9 | alex (A): 10
It is alex's turn.
+---+---+---+---+---+---+---+---+---+
|             A                     |
+---+---+   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                 S                 |
+   +   +   +   +   +   +   +   +   +
|                                   |
+---+---+---+---+---+---+---+---+---+
(Enter move or 'p' to pause): q

Walls Remaining:
sana (S): 9 | alex (A): 10
It is alex's turn.
+---+---+---+---+---+---+---+---+---+
|             A                     |
+---+---+   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                                   |
+   +   +   +   +   +   +   +   +   +
|                 S                 |
+   +   +   +   +   +   +   +   +   +
|                                   |
+---+---+---+---+---+---+---+---+---+
Game ended prematurely. Final positions:
sana at (8, 5)
alex at (1, 4)

What would you like to do?
1. Play again (same game)
2. Play a different game
3. Quit
Enter your choice (1-3): 3

Thank you for playing!

Process finished with exit code 0

```
