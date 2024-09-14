package DrawData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import BasicBehavior.Seek;
import BasicStructures.ColorVectorRGB;
import BasicStructures.Vector2;
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

public class CharacterDrop  extends PApplet{
	//shape
	//private DropShape nowShape;
	public int money;
	public ColorVectorRGB shapeColor;
	private ColorVectorRGB backgroundColor;
	
	private DropShape[] shape;
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

	
	public Vector2 currentTarget;
	
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
	
	public int currentStore;
	public int closeStore;
	public int needItem[];
	public int visitedStore[];
	public Vector2 itemCoords;
	public int itemIndex;
	public Vector2 previousSeekPosition;

	public CharacterDrop(
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
		money = GlobalSetting.playerMoney;
		//prediction------------------
		myNumber = number;
				
		//End of prediction-----------
		
		
		breadQueue = new  ArrayList<BreadcrumbInfo>();
		//testbreadQueue = new  ArrayList<String>();

		this.breadNumber = NumberOfBread;
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
		
		shape = new DropShape[breadNumber];
		//System.out.println(CurrentPosition.getX()+ ", " +CurrentPosition.getY() );
		
		//nowShape = new DropShape(P, CircleSize, TriangleSize, OriginalPoint, CurrentPosition, CurrentOrientation, Color);
		for(int i = 0; i< breadNumber; i++){
			shape[i] = new DropShape(P, CircleSize, TriangleSize, OriginalPoint, CurrentPosition, CurrentOrientation, Color);
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
				Sys.maxVelocity/2,
				Sys.maxAcceleration/2,
				P
		);

		currentStore = -1;
		closeStore = -1;
		itemCoords = new Vector2(-1, -1);
		
		needItem = new int[PublicGraph.graphGenerator.StoreAreaList.size()];
		visitedStore = new int[PublicGraph.graphGenerator.StoreAreaList.size()];
		for(int i = 0; i <PublicGraph.graphGenerator.StoreAreaList.size(); i++){
			needItem[i] = 0;
			visitedStore[i] = 0;
 		}
		previousSeekPosition = new Vector2(0, 0);
	}


	public void playDecision(){
		//-------decision
		String message;
		
		if(RunTimeBuffer.character.isInStore()){
			//in store or not?
			//System.out.println("1. player is in store " + character.currentStore+" , check shopping list");
			message = "1. player is in store " + RunTimeBuffer.character.currentStore+" , check shopping list";
			if(RunTimeBuffer.character.needThings()){
				//need things in that store
				
				//System.out.println("3. player need things in store, did player see the things?");
				message = "3. player need things in store, did player see the things?";
				if(RunTimeBuffer.character.seeThings()){
					// see needed things
					//System.out.println("7. player see things in store, seek the thing");
					message = "7. player see things in store, seek the thing";
					if(RunTimeBuffer.character.getThings()){
						//System.out.println("11. player get things in store, seek other things or leave");
						message = "11. player get things in store, seek other things or leave";
						RunTimeBuffer.character.itemCoords = new Vector2(-1, -1);
						RunTimeBuffer.character.clearSeek();
						RunTimeBuffer.character.WanderInStore(PublicGraph.graphGenerator.StoreAreaList.get(RunTimeBuffer.character.currentStore).ObstacleMin, PublicGraph.graphGenerator.StoreAreaList.get(RunTimeBuffer.character.currentStore).ObstacleMax);
					}
					else{
						//System.out.println("12. player didn't get things in store, keep seek things");
						message = "12. player didn't get things in store, keep seek things";
						RunTimeBuffer.character.clearWander();
						RunTimeBuffer.characterSeekPosition = RunTimeBuffer.character.itemCoords;
						RunTimeBuffer.character.Seek(RunTimeBuffer.characterSeekPosition);
					}
				}
				else{
					message = "8. player didn't see things in store, wander in store";
					//System.out.println("8. player didn't see things in store, wander in store");
					//character.WanderInStore(PublicGraph.graphGenerator.StoreAreaList.get(character.currentStore).ObstacleMin, PublicGraph.graphGenerator.StoreAreaList.get(character.currentStore).ObstacleMin);
					RunTimeBuffer.character.itemCoords = new Vector2(-1, -1);
					RunTimeBuffer.character.clearSeek();
					RunTimeBuffer.character.WanderInStore(PublicGraph.graphGenerator.StoreAreaList.get(RunTimeBuffer.character.currentStore).ObstacleMin, PublicGraph.graphGenerator.StoreAreaList.get(RunTimeBuffer.character.currentStore).ObstacleMax);
				}
			}
			else{
				message = "4. player doesn't need things in store";
				//System.out.println("4. player doesn't need things in store");
				//character.currentStore = -1;
				RunTimeBuffer.character.clearSeek();
				RunTimeBuffer.character.Wander();
			}
		}
		else{
			message = "2. player is not in store, see whether close to stores?";
			//System.out.println("2. player is not in store, see whether close to stores?");
			
			if(RunTimeBuffer.character.close2Store()){
				// not in store, close to store 
				message = "5. player close to a store, did the player visited this store?";
				//System.out.println("5. player close to a store, did the player visited this store?");
				
				if(RunTimeBuffer.character.visitedStore[RunTimeBuffer.character.closeStore]==1){
					message = "9. this store was visited. keep wander to go another store";
					//System.out.println("9. this store was visited. keep wander to go another store");
					//character.currentStore = -1;
					if(GlobalSetting.playerAIMode == 0){
						//handControl
						RunTimeBuffer.character.clearWander();
						//characterSeekPosition = new Vector2(64 , GlobalSetting.screenHeight-64);
						RunTimeBuffer.characterSeekPosition = previousSeekPosition;
						RunTimeBuffer.character.Seek(RunTimeBuffer.characterSeekPosition);
					}
					else{
						RunTimeBuffer.character.clearSeek();
						RunTimeBuffer.character.Wander();
					}
					// a visited store
				}
				else{
					message = "10. this store was not visited. go into the store";
					//System.out.println("10. this store was not visited. go into the store");
					RunTimeBuffer.character.clearWander();
					RunTimeBuffer.characterSeekPosition = RunTimeBuffer.character.getStorePosition();
					RunTimeBuffer.character.Seek(RunTimeBuffer.characterSeekPosition);
					// not visited store
				}
			}
			else{
				message = "6. player didn't close to a store, keep wander";
				//System.out.println("6. player didn't close to a store, keep wander");
				//character.closeStore = -1;

				if(GlobalSetting.playerAIMode == 0){
					//handControl
					
					RunTimeBuffer.character.clearWander();
					if(previousSeekPosition.x > 0){
						RunTimeBuffer.characterSeekPosition = previousSeekPosition;
					}
					else{
						
					}
					RunTimeBuffer.character.Seek(RunTimeBuffer.characterSeekPosition);
				}
				else{
					RunTimeBuffer.character.clearSeek();
					RunTimeBuffer.character.Wander();
				}
			}
		}
		//-------end decision		
	}
	//decision functions
	public boolean isInStore(){
		Vector2 checkPosition;
		checkPosition = PublicGraph.G.nodeList.get(CommonFunction.findClose(PublicGraph.G.nodeList, getPosition())).coordinate;
		
		for(int i = 0; i < PublicGraph.graphGenerator.StoreAreaList.size(); i++){
			if(checkPosition.x >PublicGraph.graphGenerator.StoreAreaList.get(i).ObstacleMin.x && checkPosition.x <PublicGraph.graphGenerator.StoreAreaList.get(i).ObstacleMax.x){
				if(checkPosition.y >PublicGraph.graphGenerator.StoreAreaList.get(i).ObstacleMin.y && checkPosition.y <PublicGraph.graphGenerator.StoreAreaList.get(i).ObstacleMax.y){
					// in shop
					//visitedStore[i] = 1;
					currentStore = i;
					return true;
				}
			}
		}

		currentStore = -1;
		return false;
	}
	public boolean close2Store(){
		float dis;
		int i =0;
		//System.out.println(PublicGraph.graphGenerator.storeEnterList.size() );

		while(i < PublicGraph.graphGenerator.StoreEnterList.size()){
			//System.out.println("player at "+getPosition().x + ", "+ getPosition().y );

			if(operK.getDisBy2Points(PublicGraph.graphGenerator.StoreEnterList.get(i), getPosition())<=GlobalSetting.closeStore/2){
				closeStore = i;
				return true;
			}
			i++;
		}

		closeStore = -1;
		return false;
	}
	public boolean visitedStore(){
		if(closeStore>=0){
			if(visitedStore[closeStore]==1){
				//this store was visited
				return true;
			}
		}
		return false;
	}
	public boolean needThings(){
		if(needItem[currentStore]<GlobalSetting.numberOfItems){
				//this store was visited
				return true;
		}
		else{
			visitedStore[currentStore]=1;
			return false;
		}
	}
	public boolean seeThings(){
		float dis;
		for(int i = 0; i < PublicGraph.graphGenerator.itemList.size(); i++){
			if(PublicGraph.graphGenerator.itemList.get(i).refer == currentStore){
				if(operK.getDisBy2Points(PublicGraph.graphGenerator.itemList.get(i).coords, getPosition()) < GlobalSetting.closeItem/2){
					//in current store
					itemIndex = PublicGraph.graphGenerator.itemList.get(i).index;
					itemCoords = PublicGraph.graphGenerator.itemList.get(i).coords;
					return true;
				}
			}
		}
		itemCoords = new Vector2(-1, -1);
		return false;
	}
	public boolean getThings(){
		float dis;
		dis = operK.getDisBy2Points(getPosition(), itemCoords);
		
		//System.out.println("current dis to item " +dis);
		//if(dis<=5){
		if(CommonFunction.findClose(PublicGraph.G.nodeList, getPosition()) == CommonFunction.findClose(PublicGraph.G.nodeList,itemCoords)){
			// get the item
			int i = 0;
			while(i < PublicGraph.graphGenerator.itemList.size()){
				if(PublicGraph.graphGenerator.itemList.get(i).refer == currentStore){
					if(PublicGraph.graphGenerator.itemList.get(i).coords.x == itemCoords.x && PublicGraph.graphGenerator.itemList.get(i).coords.y == itemCoords.y){
						needItem[currentStore]++;
						itemCoords = new Vector2(-1, -1);
						PublicGraph.graphGenerator.itemList.remove(i);
						return true;
					}
				}
				i++;
			}
		}
		return false;
	}	
	public Vector2 getStorePosition(){
		Vector2 tempPoint;
		
		tempPoint = new Vector2(
				(PublicGraph.graphGenerator.StoreAreaList.get(closeStore).ObstacleMin.x+PublicGraph.graphGenerator.StoreAreaList.get(closeStore).ObstacleMax.x)/2,
				(PublicGraph.graphGenerator.StoreAreaList.get(closeStore).ObstacleMin.y+PublicGraph.graphGenerator.StoreAreaList.get(closeStore).ObstacleMax.y)/2
				);
		
		tempPoint = PublicGraph.G.nodeList.get(CommonFunction.findClose(PublicGraph.G.nodeList, tempPoint)).coordinate;
		return tempPoint;
	}
	public Vector2 getStoreRandomPosition(){
		Vector2 tempPoint;
		float tempX;
		float tempY;
		
		tempX = (float) (Math.random()*(PublicGraph.graphGenerator.StoreAreaList.get(closeStore).ObstacleMax.x-PublicGraph.graphGenerator.StoreAreaList.get(closeStore).ObstacleMin.x)+PublicGraph.graphGenerator.StoreAreaList.get(closeStore).ObstacleMin.x);
		tempY = (float) (Math.random()*(PublicGraph.graphGenerator.StoreAreaList.get(closeStore).ObstacleMax.y-PublicGraph.graphGenerator.StoreAreaList.get(closeStore).ObstacleMin.y)+PublicGraph.graphGenerator.StoreAreaList.get(closeStore).ObstacleMin.y);
	
		tempPoint = new Vector2(
				tempX,
				tempY
				);
		
		tempPoint = PublicGraph.G.nodeList.get(CommonFunction.findClose(PublicGraph.G.nodeList, tempPoint)).coordinate;
		return tempPoint;
	}
	
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
				updateOrientation(breadQueue.get(i).getOrientation(), i);
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
	public void WanderInStore(Vector2 minPoint, Vector2 maxPoint){


		if(isWandering == false){
			//if(mousePressed){
			isWandering = true;
				//call path finding
				
			currentTarget = new Vector2((float)Math.random()*(maxPoint.x - minPoint.x-20)+minPoint.x , (float)Math.random()*(maxPoint.y - minPoint.y-20)+minPoint.y);

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
				//setK(tempResult.getK());
				//setS(tempResult.getS());
			}
			else{
				isWandering = false;
				wanderTargetQueue.clear();
			}
				
		}
	}	
	public void Wander(){


		if(isWandering == false){
			//if(mousePressed){
			isWandering = true;
				//call path finding
				
			currentTarget = new Vector2((float)Math.random()*(GlobalSetting.screenWidth-100)+50 , (float)Math.random()*(GlobalSetting.screenHeight/2-150)+GlobalSetting.screenHeight/2+50);

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
		
		//System.out.println("");
		targetQueue.clear();
		targetQueue.addAll(A1.result);
		//remove self
		targetQueue.remove(0);
		
		if(targetQueue.size()>0){

			//if(findClose(currentNodeList,character.getK().getPosition())!=currentTargetQueue.get(0)){
			if(operK.getDisBy2Points(PublicGraph.G.nodeList.get(targetQueue.get(0)).coordinate, getK().getPosition())>0){
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
			tempResult = Seek.computeSeek(currentTarget);
			
			setK(tempResult.getK());
			setS(tempResult.getS());
			//updatePosition(currentTarget);
		}
	}
	public void clearSeek(){
		targetQueue.clear();
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
