This file contains two things.

1. descriptions of files
2. execution note  and data (parameters and how to demo)

============
Description of files
============
The source codes are contains in "src" folder.
Including:

In default package:

*MainProgram: the starting point of this program.
*TimeControler: the timer function which used to compute execution time and decision rate.

In BasicStructures package:

* Vector2: 2D float vector.
* Vector3: 3D float vector.
* ColorVectorRGB: 3D float vector, used to record the RGB valuse of a color.

In BasicBehavior package:

*Seek: the seek behavior in assignment  1. 
*Pursue: contains prediction function for AI bots to predict player's movement

In DrawData package:

*BreadcrumbIndo: the data structure to record the position and orientation data.
*DropShape: this class will generate a Drop shape.
*CharacterDrop: this class will generate a character with assigned shape and with a breadcrumb queue. (used for player)
*HumanShape: this class will generate a Human shape.
*CharacterHuman: this class will generate a character with assigned shape and with a breadcrumb queue. (used for AI bots)
*HeartShape: display player's live

In GraphAlgorithm package:

*Dijkstra: the Dijkstra algorithm is implemented in here.
*AStar: AStar algorithm is implemented in here.
*Heuristic: the interface of heuristic functions.
*H1: Heuristic 1, the Euclidean distance.
*H2: Heuristic 2, the Manhattan distance.
*H3: Heuristic 3, the random guess.

In Graph Dat package
*Node: the data structure to record nodes.
*Edge: the data structure to record edges.
*IndexWeightPair: the data structure contains index and weight, used in node
*TwoIndexCosetPair: the data structure contains two indeices and weight, used in edge
*MapGenerator:  This class is used to get dots about obstacles and nodes.
*GraphGenerator: This class generate the edges between nodes, and also reduce duplicated edges.
*GraphData: This class contains the graph information which will further used in graph algorithms.
*BotVision: This class implement the vision area of AI bots.

In MovementStructures

*KiematicData: this class contains the kinematic variables.
*SteeringData: this class contains the Steering variables.
*KinematicOperations: this class contains some basic operation of kinematic variables, such as computing distance, update velocity.
*SystemParameter: set the max Speed and max Acceleration.
*ResultChange: this class is used to record the computation result.


In Variables package
*GlobalSetting: this class contains the parameter for graph generate and demo.
*CommonFunction: This class contains some common function for the whole program.
*Predictions: This class is the data strcuture for recording AI bots prediction about player movement.
*PublicGraph: This class contains the data of the tile map.

========
Assignment
========

In Behavior Tree package:
*Selector: the selector node of behavior tree.
*Sequence: the sequence node of behavior tree.
*Random Selector: the random selector node of behavior tree.
* others: the monster actions and decisions which inherit the Behavior Tree nodes.

In Decision Tree package:
*Decision: decision tree nodes.
*Decision Tree: the function of use an entire tree.
*Decision Tree Node: the interface for nodes in decision tree.
*others: the character and monsters behavior.

In Learning Package:
* Algorithm: contains ID3 algorithm and recursive function for creating decision tree.


================
Execution Note and Data
================
Execution:

This assignment didn't have any special for execution, because all of character and monsters work with AI.

If you want to test the player's decisino tree:
change AIPlayerMode in GlobalSetting. java to 0, the character can be controled with mouse.

playerAIMode = 0; (mouse control)
playerAIMode = 1; (AI control)


Data:

The recorded training data used in this assignment are in "Train.txt"

