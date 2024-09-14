package DrawData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import BasicBehavior.Seek;
import BasicStructures.ColorVectorRGB;
import BasicStructures.Vector2;
import BehaviorTree.MonsterBehaviorTree;
import BehaviorTree.MonsterBehaviorTree2;
import DecisionTree.DecisionTree;
import GraphAlgorithm.AStar;
import GraphAlgorithm.H2;
import GraphData.BotVision;
import GraphData.GraphData;
import GraphData.GraphGenerator;
import GraphData.Node;
import MovementStructures.KinematicData;
import MovementStructures.KinematicOperations;
import MovementStructures.ResultChange;
import MovementStructures.SteeringData;
import MovementStructures.SystemParameter;
import Variables.CommonFunction;
import Variables.GlobalSetting;
import Variables.PublicGraph;
import Variables.RunTimeBuffer;
import processing.core.PApplet;

public class CharacterMonster  extends PApplet{
	//shape
	public int money;
	//private DropShape nowShape;
	public ColorVectorRGB shapeColor;
	private ColorVectorRGB backgroundColor;
	
	private MonsterShape[] shape;
	//breadcrumb
	private boolean isBreadcrumb;
	private List<BreadcrumbInfo> breadQueue;
	//private List<String> testbreadQueue;

	private int breadNumber;
	
	//KinematicData
	private KinematicData paraK;
	private KinematicOperations operK;
	
	//SteeringData
	private SteeringData paraS;
	private AStar A;
	private Seek Seek;
	private boolean isSeeking;
	public boolean isWandering;
	public List<Integer> wanderTargetQueue;
	private int count = 0;
	public List<Integer> targetQueue;
	private ResultChange tempResult;
	
	public int stopCount;

	
	Vector2 currentTarget;
	
	int targetIndex;
	AStar A1;

	
	//prediction
	private int myNumber;
	private Vector2 prediction;
	//End of prediction
	//Request
	private boolean beRequest;
	private boolean isSeek;
	//End of Request
	
	public boolean[] statusArray;
	public int monsterAction; 
	
	
	public MonsterBehaviorTree bTree;
	public MonsterBehaviorTree2 bTree2;
	public DecisionTree dTree;
	
	public FileWriter recordData;
	
	
	public CharacterMonster(
			PApplet P,
			float CircleSize,
			float TriangleSize,			
			Vector2 OriginalPoint,
			Vector2 CurrentPosition,
			float CurrentOrientation,
			Vector2 CurrentVelocity,
			float CurrentRotation,
			KinematicOperations K,
			Vector2 LinearAccel,
			float AngularAccel,
			ColorVectorRGB Color,
			ColorVectorRGB BackColor,
			int NumberOfBread,
			SystemParameter Sys,
			int number
	)
	{
		

		statusArray = new boolean[4];
		
		money = 0;
		//prediction------------------
		myNumber = number;

		try {
			recordData = new FileWriter("Data"+myNumber+".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		//End of prediction-----------
		
		breadQueue = new  ArrayList<BreadcrumbInfo>();
		//testbreadQueue = new  ArrayList<String>();

		this.breadNumber = NumberOfBread;
		this.breadNumber = 1;
		this.shapeColor = Color;
		this.backgroundColor = BackColor;
		
		this.operK = K;
		
		this.paraK = new KinematicData(
			CurrentPosition.getX(),
			CurrentPosition.getY(),
			CurrentOrientation,
			CurrentVelocity.getX(),
			CurrentVelocity.getY(),
			CurrentRotation,
			K
		);
		
		updateBreadQueue(paraK.getPosition(),paraK.getOrientation());
		
		this.paraS = new SteeringData(LinearAccel.getX(), LinearAccel.getY(), AngularAccel);

		isBreadcrumb = true;
		
		shape = new MonsterShape[breadNumber];
		//System.out.println(CurrentPosition.getX()+ ", " +CurrentPosition.getY() );
		
		//nowShape = new DropShape(P, CircleSize, TriangleSize, OriginalPoint, CurrentPosition, CurrentOrientation, Color);
		for(int i = 0; i< breadNumber; i++){
			shape[i] = new MonsterShape(P, CircleSize, TriangleSize, OriginalPoint, CurrentPosition, CurrentOrientation, Color);
			//System.out.println(CurrentPosition.getX()+ ", " +CurrentPosition.getY() );
		}
		isSeeking = false;
		isWandering = false;
		count = 0;
		
		targetQueue = new ArrayList<Integer>();
		wanderTargetQueue = new ArrayList<Integer>();
		currentTarget = getPosition();
		
		tempResult = new ResultChange(
				getPosition().getX(),
				getPosition().getY(),
				getK().getOrientation(),
				getK().getVelocity().getX(),
				getK().getVelocity().getY(),
				getK().getRotation(),
				operK,
				getS().getLinearAccel().getX(),
				getS().getLinearAccel().getY(),
				getS().getAngularAccel()
			);			

		Seek = new Seek(
				5.0f,
				100.0f,
				0.1f,
				getPosition(),
				1,
				getPosition(),
				getK().getVelocity(),
				0,
				0,
				getS().getLinearAccel(),
				0,
				operK,
				Sys.maxVelocity/4,
				Sys.maxAcceleration/4,
				P
		);

		bTree = new MonsterBehaviorTree(myNumber);
		bTree2 = new MonsterBehaviorTree2(myNumber);
		dTree = new DecisionTree(myNumber);
		if(myNumber ==2){
			dTree.makeTree();
		}
	}
	public void act(int actionID){
		if(actionID ==1){
			bTree.node14Seek.run();
		}
		else if(actionID ==2){
			bTree.node16Seek.run();
		}
		else if(actionID ==3){
			bTree.node12Wait.run();
		}
		else if(actionID ==4){
			bTree.node11Wander.run();
		}
		else if(actionID ==5){
			bTree.node18Eat.run();
			bTree.node19Back.run();
			
		}

	}
	public boolean check(int attID){
		boolean result = false;
		if(attID == 0){
			if(CommonFunction.OperK.getDisBy2Points(RunTimeBuffer.character.getPosition(), getPosition())<GlobalSetting.closeStore/2){
				//System.out.println("player in sight");
				result = true;
			}
			else{
				//System.out.println("player not in sight");
				result = false;
			}
		}
		else if(attID ==1){
			if(RunTimeBuffer.character.currentStore <0){
				//System.out.println("player not in store");
				result = false;
			}
			else{
				//System.out.println("player is in store");
				result = true;
			}			
		}
		else if(attID ==2){
			if(CommonFunction.OperK.getDisBy2Points(RunTimeBuffer.character.getPosition(), getPosition())<GlobalSetting.closeStore/4){
				//System.out.println("player close");
				result = true;

			}
			else{
				//System.out.println("player not close");
				result = false;

			}			
		}
		else if(attID ==3){
			int storeID = RunTimeBuffer.character.currentStore;
			if(CommonFunction.OperK.getDisBy2Points(PublicGraph.graphGenerator.StoreEnterList.get(storeID),getPosition())<GlobalSetting.closeStore/2){
				//System.out.println("Store close");
				result = true;
	
			}
			else{
				//System.out.println("store not close");
				result = false;

			}			
		}
		return result;
	}
	public void MonsterBehavior(int treeNumber){
		if(treeNumber == 0){
			bTree.run();
		}
		else if(treeNumber == 1){
			bTree2.run();
		}
		else{
			dTree.useTree(dTree.treeNode.get(0));
			act(dTree.currentAction);
		}
	}
	//Request---------------------------------------------------------------------------------------------
	public boolean checkSeekMode(){

		return isSeek;
	}
	
	public void isSeekMode(){
		isSeek = true;
	}
	public void isWanderMode(){
		isSeek = false;
		beRequest = false;
	}
		
	//End of Request---------------------------------------------------------------------------------------
	
	
	
	//update character position
	public void updatePosition(Vector2 NewPosition){
		this.paraK.setPosition(NewPosition);
		//System.out.println("test "+ paraK.getPosition().getX() + ", " + paraK.getPosition().getY());
		//updateBreadQueue(paraK.getPosition(), paraK.getOrientation());
	}
	public void updatePosition(float NewPositionX, float NewPositionY){
		this.paraK.setPosition(NewPositionX, NewPositionY);
		//System.out.println("**test "+ paraK.getPosition().getX() + ", " + paraK.getPosition().getY());

		//updateBreadQueue(paraK.getPosition(), paraK.getOrientation());
	}
	public Vector2 getPosition(){
		return paraK.getPosition();
	}
	
	//orientation
	public float getOrientation(){
		return paraK.getOrientation();
	}
	public void updateOrientation(float NewOrientation){
		paraK.setOrientation(NewOrientation);
	}
	
	
	public KinematicData getK(){
		return paraK;
	}
	public SteeringData getS(){
		return paraS;
	}
	
	public void setK(KinematicData K){
		this.paraK = K;
	}
	public void setS(SteeringData S){
		this.paraS = S;
	}
	
	//update shape color
	public void updateShapeColor(ColorVectorRGB CurrentColor, int Index){
		shape[Index].updateColor(CurrentColor);
	}
	public void updateShapeColor(float CurrentColorR, float CurrentColorG, float CurrentColorB, int Index){
		shape[Index].updateColor(new ColorVectorRGB(CurrentColorR, CurrentColorG, CurrentColorB));
	}
	
	
	//update shape position
	public void updateShapePosition(Vector2 CurrentPosition, int Index){
		shape[Index].updatePosition(CurrentPosition);
	}
	public void updateOrientation(float CurrentOrientation, int Index){
		shape[Index].updateOrientation(CurrentOrientation);
	}
	
	//control breadcrumb
	public void turnOffBread(){
		isBreadcrumb = false;
	}
	public void turnOnBread(){
		isBreadcrumb = true; 

	}
	public void behaviorTree(){
		
	}
	
	//display things
	public void display(){
		//testshape.display();
		if(isBreadcrumb == true){
			//draw shape
			

			
			for(int i = 0; i < breadQueue.size(); i++){
				if(i == breadQueue.size()-1){
					shape[i].parent.pushMatrix();
					shape[i].parent.stroke(255, 255, 0);
					shape[i].parent.fill(0, 0, 0);
					shape[i].parent.text(myNumber, breadQueue.get(i).getPosition().x, breadQueue.get(i).getPosition().y-30);					
					shape[i].parent.popMatrix();
				}
				
				updateShapePosition(breadQueue.get(i).getPosition(), i);
				//updateOrientation(breadQueue.get(i).getOrientation(), i);
				//System.out.println(breadQueue.get(i).getOrientation());
				//System.out.println(breadQueue.get(i).getPosition().getX() +", "+ breadQueue.get(i).getPosition().getY());
				updateShapeColor(
					shapeColor.getR()+(backgroundColor.getR() - shapeColor.getR())* ( breadQueue.size() - i-1)/ breadNumber,
					shapeColor.getG()+(backgroundColor.getG() - shapeColor.getG())* ( breadQueue.size() - i-1)/ breadNumber,
					shapeColor.getB()+(backgroundColor.getB() - shapeColor.getB())* ( breadQueue.size() - i-1)/ breadNumber,
					i
				);
				//display
				shape[i].display();
			}
			
			//nowShape.updatePosition(paraK.getPosition());
			//nowShape.updateColor(shapeColor);
			//nowShape.display();			

			
			//System.out.println("-------------------");
		}
		else{
			// Do nothing here
		}
		
	}
	public void recordData(int treeNumber) throws IOException{
		boolean[] previousArray;
		int previousAction;
		
		previousArray = new boolean[4];
		previousAction = monsterAction;
		
		previousArray = statusArray;
		
		if(treeNumber == 0){
			statusArray[0] = bTree.inSight;
			statusArray[1] = bTree.PlayerInStore;
			statusArray[2] = bTree.isClose;
			statusArray[3] = bTree.reachStore;
			monsterAction = bTree.currentAction;
		}
		else{
			statusArray[0] = bTree2.inSight;
			statusArray[1] = bTree2.PlayerInStore;
			statusArray[2] = bTree2.isClose;
			statusArray[3] = bTree2.reachStore;
			monsterAction = bTree2.currentAction;
			
		}
		int sameCount = 0;
		int i = 0;
		while(i < 4){
			if(statusArray[i] == previousArray[i]){
				sameCount++;
			}
			i++;
		}
		if(previousAction == monsterAction){
			sameCount++;
		}
		
		if(sameCount <5){

			if(statusArray[0] == true){
				recordData.write("InSight              ");
			}
			else{
				recordData.write("NotinSight           ");
			}
			if(statusArray[1] == true){
				recordData.write("PlayerInStore        ");
			}
			else{
				recordData.write("PlayerNotInStore     ");
			}
			if(statusArray[2] == true){
				recordData.write("IsClose              ");
			}
			else{
				recordData.write("NotClose             ");
			}
			
			if(statusArray[3] == true){
				recordData.write("ReachStore           ");
			}
			else{
				recordData.write("NotReachStore        ");
			}

			
			if(monsterAction == 5){
				recordData.write("EatPlayer");
			}
			else if(monsterAction == 1){
				recordData.write("SeekPlayer");
			}
			else if(monsterAction == 2){
				recordData.write("SeekStore");
			}
			else if(monsterAction == 3){
				recordData.write("Wait");
			}
			else{
				recordData.write("Wander");
			}
			
			recordData.write("\r\n");
			recordData.flush();
		}
		else{
			//same status and actions
			
		}
	}
	public void closeData() throws IOException{
		recordData.close();
	}
	//record position
	public void updateBreadQueue(Vector2 CurrentPosition, float CurrentOrientation){
		
		if(breadQueue.size() < breadNumber ){
			//System.out.println("Start to add" + "size = "+ breadQueue.size() + " orientation " + CurrentOrientation);
			breadQueue.add(
					new BreadcrumbInfo(CurrentPosition.getX(), CurrentPosition.getY(), CurrentOrientation)
			);

			Iterator iterator = breadQueue.iterator();

/*			
			int i =0;
			while(iterator.hasNext()) {
			    Object next = iterator.next();
			    System.out.println(next.toString()+"   "+ breadQueue.get(i).getPosition().getX() + ", " +breadQueue.get(i).getPosition().getY());
			}
			System.out.println("------------");
*/
		}
		else{
			breadQueue.remove(0);
			breadQueue.add(
					new BreadcrumbInfo(CurrentPosition.getX(), CurrentPosition.getY(), CurrentOrientation)
			);
/*
			Iterator iterator = breadQueue.iterator();

			int i =0;
			while(iterator.hasNext()) {
			    Object next = iterator.next();
			    System.out.println(next.toString()+"   "+ breadQueue.get(i++).shapeVariables.position.x);
			}
*/			
		}

	}
	public void Wander(){


		if(isWandering == false){
			//if(mousePressed){
			isWandering = true;
				//call path finding
				
			currentTarget = new Vector2((float)Math.random()*(GlobalSetting.screenWidth-100)+50 , (float)Math.random()*(GlobalSetting.screenHeight/2-150)+GlobalSetting.screenHeight/2);

			//currentTarget = new Vector2((float)Math.random()*(GlobalSetting.screenWidth-100)+50, (float)Math.random()*(GlobalSetting.screenHeight-100)+50);
			int targetIndex = CommonFunction.findClose(PublicGraph.G.nodeList, currentTarget);
				
/*
				while(PublicGraph.graphGenerator.ObsOverlapList.get(targetIndex)== 1){
					currentTarget = new Vector2((float)Math.random()*(GlobalSetting.screenWidth-100)+50, (float)Math.random()*(GlobalSetting.screenHeight-100)+50);
					targetIndex = CommonFunction.findClose(PublicGraph.G.nodeList, currentTarget);
				}
*/				
			int closestIndex = CommonFunction.findClose(PublicGraph.G.nodeList, getK().getPosition());
				
			//System.out.println(targetIndex+ ", " + closestIndex);
			H2 h1 = new H2(PublicGraph.G.nodeList, PublicGraph.G.edgeList, targetIndex, closestIndex, operK);
				
			A1 = new AStar(h1, PublicGraph.G.nodeList, PublicGraph.G.edgeList, targetIndex, closestIndex);

			while(A1.openList.size()>0){
				A1.computeAStar(PublicGraph.G.nodeList, PublicGraph.G.edgeList);
				//System.out.println("-----------");
			}
			if(A1.isFind == false){
				System.out.println("Didn't find!!");
			}
			else{
/*
					System.out.print("\r\nAStar with H1 Path: ");
					for(int i = 0 ;i < A1.result.size(); i++){
						System.out.print(" " + A1.result.get(i)+" ");
					}
*/
			}
			//System.out.println("");
			wanderTargetQueue.clear();
			wanderTargetQueue.addAll(A1.result);
	}
		
	//Gathering dots
	
	//make decisions in 0.02 sec frequency
	//make one decision
		if(isWandering == true){
			if(wanderTargetQueue.size()>0){
	
					//if(findClose(currentNodeList,character.getK().getPosition())!=currentTargetQueue.get(0)){
		
				if(operK.getDisBy2Points(PublicGraph.G.nodeList.get(wanderTargetQueue.get(0)).coordinate, getK().getPosition())>5){
					//System.out.println("Current Target = " + targetQueue.get(0));
					currentTarget = PublicGraph.G.nodeList.get(wanderTargetQueue.get(0)).coordinate;
				}
				else{
					wanderTargetQueue.remove(0);
					Wander();
				}
					//currentTarget = PublicGraph.G.nodeList.get(targetQueue.get(0)).coordinate;
					//targetQueue.remove(0);
					tempResult = Seek.computeSeek(currentTarget);
				//tempResult = Seek.stupidSeek(currentTarget);
	
				//System.out.println(tempResult.getK().getPosition().x + ", "+tempResult.getK().getPosition().y);
				setK(tempResult.getK());
				setS(tempResult.getS());
			}
			else{
				isWandering = false;
				wanderTargetQueue.clear();
			}
				
		}
	}
	public void clearWander(){
		isWandering = false;
		wanderTargetQueue.clear();
	}
	public void StopAndSee(){
		
	}
	public void clearSeek(){
		targetQueue.clear();
	}
	public void Seek(Vector2 target){
		currentTarget = target;
		int targetIndex = CommonFunction.findClose(PublicGraph.G.nodeList, currentTarget);
		
		int closestIndex = CommonFunction.findClose(PublicGraph.G.nodeList, getK().getPosition());

		//System.out.println(targetIndex+ ", " + closestIndex);
		H2 h1 = new H2(PublicGraph.G.nodeList, PublicGraph.G.edgeList, targetIndex, closestIndex, operK);
		
		A1 = new AStar(h1, PublicGraph.G.nodeList, PublicGraph.G.edgeList, targetIndex, closestIndex);

		while(A1.openList.size()>0){
			A1.computeAStar(PublicGraph.G.nodeList, PublicGraph.G.edgeList);
			//System.out.println("-----------");
		}
		if(A1.isFind == false){
			System.out.println("Didn't find!!");
		}
		else{
/*
			System.out.print("\r\nAStar with H1 Path: ");
			for(int i = 0 ;i < A1.result.size(); i++){
				System.out.print(" " + A1.result.get(i)+" ");
			}
*/
		}
		//System.out.println("");
		targetQueue.clear();
		targetQueue.addAll(A1.result);
		//remove self
		targetQueue.remove(0);

		if(targetQueue.size()>0){

			//if(findClose(currentNodeList,character.getK().getPosition())!=currentTargetQueue.get(0)){
			if(operK.getDisBy2Points(PublicGraph.G.nodeList.get(targetQueue.get(0)).coordinate, getK().getPosition())>5){
				//System.out.println("Current Target = " + targetQueue.get(0));
				currentTarget = PublicGraph.G.nodeList.get(targetQueue.get(0)).coordinate;
				//System.out.println("----Seek ("+ targetQueue.get(0)+ ") ");
			}
			else{
				targetQueue.remove(0);
			}
			//currentTarget = PublicGraph.G.nodeList.get(targetQueue.get(0)).coordinate;
			//System.out.println("----Seek ("+ targetQueue.get(0)+ ") ");

			//targetQueue.remove(0);
			//tempResult = Seek.computeSeek(currentTarget);
			tempResult = Seek.stupidSeek(currentTarget);
			
			setK(tempResult.getK());
			setS(tempResult.getS());
			//updatePosition(currentTarget);
		}
		else{
		}

	}

	
	
	public float getChangeInOrientation(float or1, float or2)
	{
		float changeInOr;
		changeInOr = or1 - or2;
		
		if (or2 < Math.PI/2 && or1 >= 0)
		{
			if ((or1 > 3*Math.PI/2))
			{
				changeInOr = (float)(-1 * (2*Math.PI - or1 + or2));
			}
			else
			{
				changeInOr = or1 - or2;
			}
		}
		
		else if (or2 > 3*Math.PI/2)
		{
			if (or1 >= 0 && or1 < Math.PI/2)
			{
				changeInOr = or1 + (float)(2*Math.PI - or2);
			}
		}
		
		return changeInOr;
	}
	
	public float getChangeInOrientation2(float or1, float or2)
	{
		float changeInOr;
		changeInOr = or1 - or2;
		
		if(or1*or2 >=0){
			changeInOr = Math.abs(or1-or2);
		}
		else{
			changeInOr = Math.abs(Math.abs(or1)+Math.abs(or2));
		}
		
		return changeInOr;
	}	

}
