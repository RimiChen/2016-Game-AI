package Variables;

import BasicStructures.Vector2;

public class GlobalSetting {
	public static int screenWidth;
	public static int screenHeight;

	public static int HeuristicMode;

	public static int numberOfBread;
	public static int tileNumber;
	public static int nodeSize;
	public static int obstacleMargin;
	
	public static int numberOfbots;
	
	public static int numberOfItems;

	
	public static int wanderTimeBound;
	public static int closeStore;
	public static int closeItem;
	
	public static int playerAIMode;
	
	public static int playerMoney;
	
	public static String dataPath;
	

	//learning 
	public static int numberOfAttribute;
	
	public GlobalSetting(){
		screenWidth = 1024;
		screenHeight = 768;
		
		HeuristicMode = 1;

		numberOfBread = 10;
		
		numberOfbots = 3;
		wanderTimeBound = 40;
		
		numberOfItems = 2;
		
		//End of prediction------------------------------------------------------------------------------------

		tileNumber = 40;
		nodeSize = 10;
		obstacleMargin = 3;
		
		closeStore = 200;
		closeItem = 150;

		playerAIMode = 1;
		playerMoney = 200;
		
		dataPath = "";
		
		
		//maxVisionAngle = 0.6;

	}
}
