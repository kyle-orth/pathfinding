# pathfinding
## _Start Here:_

Welcome! Below describes what this project is, a comprehensive feature list (including potential updates), and a "ForTheNerds" segment of interesting code design choices. 


### - - - The Project - - -
This is a decently simple pathfinding program with lots of room for expansion. It utilizes java Swing for graphics to give the user access to a simple tile editor, which can be modified before running various pathfinding algorithms on the map. 

To get started, simply download the src folder and run Main.java.

### - - - Feature Overview - - -
1) Editor Menu: Use the tools on the right to highlight tiles, add or remove wall tiles, and move the start and target tiles to create a map
2) Change Map Size: Use the slider bars on the Editor menu to adjust the width and height of the map freely (Not Yet Implemented).
3) Algorithms Menu: In the Algorithms tab, experiment with different pathfinding algorithms such as breadth-first, and A* pathfinding. (Only breadth-first implemented). Visualize the different processes, and explore how they perform with different maps.
4) Save and Load Configurations: Use the save / load menu to save created maps, open an existing save file, and manage your save files. (Not Yet Implemented).
5) AI Showcase: Visualize how paths can be taught to nueral networks through reinforcement learning. Train the AI program to tackle different maps of various complexities (Not Yet Implemented).
   
### - - - ForTheNerds - - -
Throughout this ongoing project there's been several design choices I've found interesting, as well as several cool code segments worth mentioning. So here we are:
1) 
2) Use of HashMaps: Browsing the code, specifically Map, Display, and Algorithms, you'll note I use a lot of HashMaps. These dictionary-esque datatypes appealed for two main reasons. First, ease of comprehension. As an example, checking to see if a tile is a wall tile looks something like this: (CODE SEGMENT). Whearas without the Hashmpas allowing us to use these String keys, it would have to look like this: (UGLY CODE SEGMENT).
