# pathfinding
## _Start Here:_

Welcome! Below describes what this project is, a comprehensive feature list (including potential updates), and a "ForTheNerds" segment of interesting code design choices. 


### - - - The Project - - -
This is a decently simple pathfinding program with lots of room for expansion. It utilizes java Swing for graphics to give the user access to a simple tile editor, which can be modified before running various pathfinding algorithms on the map. 

**To get started, simply download the src folder and run Main.java.**

### - - - Feature Overview - - -
Note that several features are a work in progress.
1) **Editor Menu:** Use the tools on the right to highlight tiles, add or remove wall tiles, and move the start and target tiles to create a map
2) **Change Map Size:**(Not Yet Implemented). Use the slider bars on the Editor menu to adjust the width and height of the map freely.
3) **Algorithms Menu:** In the Algorithms menu, experiment with different pathfinding algorithms such as breadth-first, and A* pathfinding. (Only breadth-first implemented). Visualize the different processes, and explore how they perform with different maps.
4) **Control Algorithm Display Speed** On the bottom of the Algorithms menu, use the dropdown selection box to control the speed of the algorithm showcase.
5) **Save and Load Configurations:** (Not Yet Implemented). Use the save / load menu to save created maps, open an existing save file, and manage your save files.
6) **AI Showcase:** (Not Yet Implemented). Visualize how paths can be taught to nueral networks through reinforcement learning. Train the AI program to tackle different maps of various complexities.
   
### - - - ForTheNerds - - -
Throughout this ongoing project there's been several design choices I've found interesting, as well as several cool code segments worth mentioning. So here we are:

#### Use of Record 
Coordinate is a class with no modifiers, called a Record. It simply holds information that never changes. 
~~~
public record Coordinate(int x, int y) {}
~~~
That is the entire Coordinate file. One line. It comes with built in accessors .x() and .y(). I wasn't familiar with this structure before this, and it's definitely something I will utilize in the future.

#### Interesting Loop 
Design In Display.changeMenu, I was able to save a lot of logic tree usage with this simple loop:
~~~
    public void changeMenu(String menu){
        for(Boolean bool : new boolean[]{false, true}) {
            if (Interface.menuType.equals("editor")) {
                showEditorMenu(bool);
            }
            else if (Interface.menuType.equals("algorithms"))
                showAlgorithmMenu(bool);
            Interface.menuType = menu;
        }
    }
~~~
This is slightly condensed from the source code, but it shows the idea. The first loop hides the current menu, then the menu changes and the next loop displays the new menu. I found this interesting.

#### Use of HashMaps 
Browsing the code, specifically Map, Display, and Algorithms, you'll note I use a lot of HashMaps. These dictionary-esque datatypes appealed for two main reasons. First, ease of comprehension. As an example, checking to see if a tile is a wall tile looks something like this:
~~~
boolean isWall = (map[row][col] == Map.legend.get("wall")); // Easily readable if you recognize HashMap accessors
~~~
Whearas without the Hashmaps allowing us to use these String keys, it would have to look like this:
~~~
boolean isWall = (map[row][col] == 2); // What does 2 represent? What if I switch 2 to mean the start tile instead?
~~~
As you can see, the HashMaps drastically improve quality of life. The second reason, which is more chance than design, is that it (potentially) saves memory. Particularly with large datasets, holding 2D arrays of ints is fairly easy, while holding the same size arrays of Strings would take much more memory. 

#### Java Swing Timer 
The Interface class shows algorithms step by step, with the pause between steps controlled by the user. In Java, a sleeper thread to control time clashes horribly with the way the GUI works. So instead, it is common practice to implement a Timer, which works by delaying for a certain amount of time, then performing a task. The task must eventually end, and call the command to stop itself. Essentially it is a while loop that must be explicitly broken, but has the benefit of specific time control. Here is the high-level code segment I used for the breadth-first algorithm time control:
~~~
timer = new Timer(getAlgorithmTimer(display), e -> {
            if (Algorithms.done) {
                // Increment the shown path until complete
                lastPathShown = showFinalRoute(lastPathShown, display);
                if(lastPathShown.equals(map.getStartCoord()))
                    timer.stop();
            }
            else {
                Algorithms.stepBreadthFirstSearch(map);
                showSearch(display);
                timer.setDelay(getAlgorithmTimer(display));
            }
        });
        timer.setInitialDelay(0);
        timer.start();
~~~
The second parameter of the Timer is called an ActionListener, which essentially is a method that runs every time the Timer "rings", so to speak. In this example, the else block happens first, with the algorithm running once each time step until it is done. Then the if block runs, again once each time step until the path is fully shown. Lastly, the nested if turns the timer off and the code then continues as normal.

#### Hope you enjoyed this section!
