/*
 * License information:
 * 
 * ===================
 * Project Information
 * ===================
 * Name: CSC 584, Assignments
 * 
 * Topic:
 * This pr1ogram is created for 2016 spring, CSC 584 Assignments
 * Assignment 1: test various movement algorithms.
 * Assignment 2: test path finding and path following.
 * Assignment 3: decision and behavior trees.
 *  
 * ==================
 * Author information
 * ==================
 * Name: Yi-Chun Chen
 * UnityID: ychen74
 * Student ID:200110436
 * 
 * ==========
 * References
 * ==========
 * 1. textbook.
 * 
 */


/*
 * Program Descriptions
 * =================
 * Coding Convention
 * =================
 * - global: Pascal casing.
 * - local: Camel casing
 * - function input: Pascal casing
 * - function output: Pascal casing
 * - function name: Camel casing
 * - class name: Pascal casing  
 *
 *=====
 *Logic
 *=====
 *- Each basic behavior will be called as function, and return new acceleration or velocity
 *
 *
 */

/*
 * ==============
 * Import Library
 * ==============
 */

import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import BasicBehavior.*;
import BasicStructures.*;
import DrawData.*;
import GraphAlgorithm.AStar;
import GraphAlgorithm.Dijkstra;
import GraphAlgorithm.H1;
import GraphAlgorithm.H2;
import GraphAlgorithm.H3;
import GraphData.BotVision;
import GraphData.Edge;
import GraphData.GraphData;
import GraphData.GraphGenerator;
import GraphData.MapGenerator;
import GraphData.Node;
import Learning.Algorithm;
import MovementStructures.*;
import Variables.RunTimeBuffer;
import Variables.CommonFunction;
//import OldFile.*;
import Variables.GlobalSetting;
import Variables.LookUp;
import Variables.PublicGraph;
import processing.core.*;
import MovementStructures.KinematicOperations.*;

/*
 * =============
 * Class Declare
 * =============
 */
public class MainProgram extends PApplet{
/*
 * ============================
 * Variables for Shared Setting
 * ============================
 */

	private KinematicOperations OperK;
	private TimeControler botDecisionTimer;
	private TimeControler playerDecisionTimer;
	private TimeControler breadTimer;
	
	//Setting for environment
	private ColorVectorRGB backgroundColor;
/*
	private List<Integer> currentTargetQueue;
	private CharacterDrop character;
	private Vector2 characterSeekPosition;

	
	private CharacterMonster[] Bot;
	private List<Integer>[] botsTargetQueue;
*/	
	private Vector2 originalPoint;
	
	//Seek function
	private ResultChange tempResult;
	private Vector2 initialTarget;
	int targetIndex;
	int closestIndex;
	
	private List<Node> currentNodeList;
	private List<Edge> currentEdgeList;
	
	private PImage img;

	private Algorithm algo;
	/*
 * ====================
 * Variables for Others
 * ====================
 */

/*
 * (non-Javadoc)
 * @see processing.core.PApplet#settings()
 * ===========================
 * Setting and Initializations
 * ===========================
 */
	public PublicGraph publicG;
	public CommonFunction CF;
	
	public LookUp lookup;
	public static SystemParameter Sys;
	public static GlobalSetting globalS;
	
	public static RunTimeBuffer buffer;

	public void settings(){


		
		lookup = new LookUp();
		globalS = new GlobalSetting();
		// set system parameter: max V, max Acceleration
		Sys = new SystemParameter(20, 20*0.1f, PI/2.0f);
		// input this operations for each kinematics
		OperK = new KinematicOperations(this, Sys);
		CF = new CommonFunction(OperK);
		publicG = new PublicGraph(this, OperK);
		
		//buffer = new RunTimeBuffer(Sys, OperK, this);
		try {
			buffer = new RunTimeBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		img = loadImage("room.PNG");
		backgroundColor = new ColorVectorRGB(255, 255, 255);

		size(GlobalSetting.screenWidth, GlobalSetting.screenHeight);
		

		//Initial settings
		originalPoint = new Vector2(0, 0);

		//the decision rate
		botDecisionTimer = new TimeControler();
		botDecisionTimer.initialTimer();
		playerDecisionTimer = new TimeControler();
		playerDecisionTimer.initialTimer();

		// the time slat for record breadcrumbs
		breadTimer = new TimeControler();
		breadTimer.initialTimer();

		algo = new Algorithm();

		//What should be redo 
		InitilizeAll();
		//RunTimeBuffer.Bot[0].bTree.run();
		//algo.traingProcess();
		System.out.println("");
	}
	
/*
 * 	(non-Javadoc)
 * @see processing.core.PApplet#draw()
 * =========
 * Draw Loop
 * =========
 */

	int count =0;
	boolean end = false;
	
	public void draw(){
		
		int endCount = 0;
		for(int i = 0; i< 4; i++){
			if(RunTimeBuffer.character.needItem[i]==GlobalSetting.numberOfItems){
				endCount ++;
			}
		}
/*		
		if(endCount>=4 || RunTimeBuffer.character.money <=0){
			//System.out.println("record Data end.");
			//return;
			end = true;
		}
*/
		if(RunTimeBuffer.character.money <=0){
			//System.out.println("record Data end.");
			//return;
			end = true;
		}
		
		
		// fix this part to ensure there is 
		boolean checkGunExist = false;
		int checkGunIter = 0;
		
		background(backgroundColor.getR(), backgroundColor.getG(), backgroundColor.getB());
		image(img,0, 0);
		//assign data
		

		
		RunTimeBuffer.character.updatePosition(RunTimeBuffer.character.getK().getPosition());
		RunTimeBuffer.character.updateOrientation(RunTimeBuffer.character.getK().getOrientation());		
		
		for(int i = 0; i < GlobalSetting.numberOfbots; i++){
			RunTimeBuffer.Bot[i].updatePosition(RunTimeBuffer.Bot[i].getK().getPosition());
			RunTimeBuffer.Bot[i].updateOrientation(RunTimeBuffer.Bot[i].getK().getOrientation());
		}

		
		if(mousePressed){
			if(GlobalSetting.playerAIMode == 0){
				//handControl
				RunTimeBuffer.characterSeekPosition = new Vector2(mouseX, mouseY);
				RunTimeBuffer.character.previousSeekPosition = RunTimeBuffer.characterSeekPosition;
			}
		}

		
		//display 
		//previousSeekPosition = characterSeekPosition;

		if(RunTimeBuffer.character.wanderTargetQueue.size()>0){
			pushMatrix();
			fill(0,0,0, 255);
			ellipse(PublicGraph.G.nodeList.get(RunTimeBuffer.character.wanderTargetQueue.get(RunTimeBuffer.character.wanderTargetQueue.size()-1)).coordinate.x,
					PublicGraph.G.nodeList.get(RunTimeBuffer.character.wanderTargetQueue.get(RunTimeBuffer.character.wanderTargetQueue.size()-1)).coordinate.y,
					50, 50);
			popMatrix();
		}
		else{
			pushMatrix();
			//previousSeekPosition = characterSeekPosition;
			fill(0,255,0, 255);
			ellipse(RunTimeBuffer.characterSeekPosition.x,
					RunTimeBuffer.characterSeekPosition.y,
					50, 50);
			popMatrix();
		}
		

		
		//Gathering dots
		String message = "";
		if(playerDecisionTimer.checkTimeSlot(100)){

			RunTimeBuffer.character.playDecision();
		}

		
		//make decisions in 0.02 sec frequency
		if(botDecisionTimer.checkTimeSlot(100)){
			//bot decision cycle
			count = (count +1)%100;
			//For testing safe spots
			int[] otherbots;//
			//--------------
			for(int botIter = 0 ; botIter < GlobalSetting.numberOfbots; botIter++){
				RunTimeBuffer.Bot[botIter].MonsterBehavior(botIter);
				try {
					if(end ==false){
						RunTimeBuffer.Bot[botIter].recordData(botIter);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}


		for(int i = 0; i < GlobalSetting.numberOfbots; i++){
			pushMatrix();
			fill(255, 255, 255, 125);
			ellipse(RunTimeBuffer.Bot[i].getPosition().x, RunTimeBuffer.Bot[i].getPosition().y, GlobalSetting.closeStore/2, GlobalSetting.closeStore/2);
			popMatrix();
		}
		
		
		//record
		if(breadTimer.checkTimeSlot(200)){
			RunTimeBuffer.character.updateBreadQueue(RunTimeBuffer.character.getPosition(), RunTimeBuffer.character.getOrientation());

			for(int i = 0; i < GlobalSetting.numberOfbots; i++){
				RunTimeBuffer.Bot[i].updateBreadQueue(RunTimeBuffer.Bot[i].getPosition(), RunTimeBuffer.Bot[i].getOrientation());
			}
		}
		//display		
		//PublicGraph.graphGenerator.edgeDraw();
		//PublicGraph.graphGenerator.displayDirichletDomain();
		
		//PublicGraph.graphGenerator.displayStore();
		//PublicGraph.graphGenerator.displayObstacle();
		PublicGraph.graphGenerator.displayEnterStore();
		//PublicGraph.graphGenerator.nodeDisplay(this);
		
		PublicGraph.graphGenerator.displayItems();
/*		
		for(int i = 0; i < PublicGraph.graphGenerator.StoreAreaList.size(); i++){
			pushMatrix();
			fill(255, 0, 0, 255);
			text("Finding progress: store" + i +" = "+RunTimeBuffer.character.needItem[i],50, 50*i+50 );
			text("store visited " + i +" = "+RunTimeBuffer.character.visitedStore[i],250, 50*i+50 );
			popMatrix();
		}
		pushMatrix();
		fill(255, 0, 0, 255);
		text("close store: " + RunTimeBuffer.character.currentStore,50, 50*5+50 );
		text("current store: " + RunTimeBuffer.character.currentStore,50, 50*6+50 );
		textSize(16);
		text(message,50, 50*7+50 );
		popMatrix();
*/	
		pushMatrix();
		fill(0, 0, 0, 255);
		textSize(16);
		text("player money = "+RunTimeBuffer.character.money ,50, 50*0+100 );
		popMatrix();
		
		for(int i = 0; i < GlobalSetting.numberOfbots; i++){
			pushMatrix();
			fill(0, 0, 0, 255);
			textSize(16);
			text("Robber " + i +" money = "+RunTimeBuffer.Bot[i].money,50, 50*(i+1)+100 );
			popMatrix();
		}

		
		//mapCreate.nodeDisplay(this);
		RunTimeBuffer.character.display();
		for(int i = 0; i < GlobalSetting.numberOfbots; i++){
			RunTimeBuffer.Bot[i].display();
		}
		
	}
	
/*
 * ==========================
 * Start Point of the Program
 * ==========================	
 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(" This is new program.");
		PApplet.main(new String[] { "--present", "MainProgram" });
	
	}
	public void mouseReleased(){

		stroke(0);
		ellipse( mouseX, mouseY, 5, 5 );
		text( "x: " + mouseX + " y: " + mouseY, mouseX + 2, mouseY );	
		PublicGraph.mapCreate.markObstacles(new Vector2(mouseX, mouseY));
	}
	public void InitilizeAll(){

/*		
		//setLevel = true;
		PublicGraph.mapCreate.readObstacle(this, 1);
		PublicGraph.graphGenerator = new GraphGenerator(PublicGraph.mapCreate, OperK, this);
		PublicGraph.graphGenerator.createEdge();
		
		PublicGraph.G = new GraphData(PublicGraph.graphGenerator.nodeList, PublicGraph.graphGenerator.edgeList, this);			
		
		PublicGraph.graphGenerator.getStoreList(PublicGraph.G.nodeList);
		PublicGraph.graphGenerator.generateItems();
*/
		
		Vector2 currentShapePosition = new Vector2(64 , GlobalSetting.screenHeight-64);
		currentShapePosition = PublicGraph.G.nodeList.get(CommonFunction.findClose(PublicGraph.G.nodeList, currentShapePosition)).coordinate;
		Vector2 initialVelocity = new Vector2(0, 0);
		Vector2 initialAccel = new Vector2(0, 0);
		ColorVectorRGB tempColor = new ColorVectorRGB(23, 228, 119);

	

		currentNodeList = new ArrayList<Node>();
		currentNodeList = PublicGraph.G.nodeList;
		
		currentEdgeList = new ArrayList<Edge>();
		currentEdgeList = PublicGraph.G.edgeList;

		RunTimeBuffer.characterSeekPosition = currentShapePosition;
	
		
		//set character
		RunTimeBuffer.character = new CharacterDrop(
				this,
				20,
				20,
				originalPoint,
				currentShapePosition,
				0,
				initialVelocity,
				0,
				OperK,				
				initialAccel,
				0,
				tempColor,
				backgroundColor,
				GlobalSetting.numberOfBread,
				Sys,
				0
		);
		
		initialTarget = currentShapePosition;
		tempResult = new ResultChange(
				RunTimeBuffer.character.getPosition().getX(),
				RunTimeBuffer.character.getPosition().getY(),
				RunTimeBuffer.character.getK().getOrientation(),
				RunTimeBuffer.character.getK().getVelocity().getX(),
				RunTimeBuffer.character.getK().getVelocity().getY(),
				RunTimeBuffer.character.getK().getRotation(),
				OperK,
				RunTimeBuffer.character.getS().getLinearAccel().getX(),
				RunTimeBuffer.character.getS().getLinearAccel().getY(),
				RunTimeBuffer.character.getS().getAngularAccel()
		);	
		

		RunTimeBuffer.Bot = new CharacterMonster[GlobalSetting.numberOfbots];
		//botsTargetQueue = new ArrayList<Integer>[NumberOfBots]();

		//prediction------------------------------------------------------------------------------------------		
		//test Prediction is OK
		//Vector2 a = new Vector2(20, 10);
		//character.updateMyPrediction(a);
		//System.out.println(character.getMyPrediction().getX());
		//End of prediction------------------------------------------------------------------------------------

				
		
		for(int i = 0; i<GlobalSetting.numberOfbots ;i ++ ){
			Vector2 botPosition;
			
			botPosition = new Vector2((float)Math.random()*(GlobalSetting.screenWidth-100)+50 , (float)Math.random()*(GlobalSetting.screenHeight/2-100)+GlobalSetting.screenHeight/2);
			botPosition = PublicGraph.G.nodeList.get(CommonFunction.findClose(PublicGraph.G.nodeList, botPosition)).coordinate;
/*
			while(PublicGraph.graphGenerator.ObsOverlapList.get(CommonFunction.findClose(currentNodeList, botPosition))==1){
				botPosition = new Vector2((float)Math.random()*(windowWidth-00)+50 , (float)Math.random()*(windowHeight-100)+50);
			}
			
*/			
			ColorVectorRGB botTempColor;
			
			if(i%3 ==0){
				botTempColor = new ColorVectorRGB(88, 195, 255);
	
			}
			else if(i%3 ==1){
				botTempColor = new ColorVectorRGB(255, 121, 118);

			}
			else{
				botTempColor = new ColorVectorRGB(255, 255, 0);
			}

			RunTimeBuffer.Bot[i] = new CharacterMonster(
					this,
					20,
					20,
					originalPoint,
					botPosition,
					0,
					initialVelocity,
					0,
					OperK,				
					initialAccel,
		
					0,
					//new ColorVectorRGB((float)Math.random()*255, (float)Math.random()*255, (float)Math.random()*255),
					//new ColorVectorRGB((float)255*(i%2), (float)255*(i%2), (float)255*(i%2)),
					botTempColor,
					backgroundColor,
					GlobalSetting.numberOfBread,
					Sys,
					i
			);
		}		
		
		
		RunTimeBuffer.currentTargetQueue = new ArrayList<Integer>();
	}
}