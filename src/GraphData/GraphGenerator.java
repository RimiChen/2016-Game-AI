package GraphData;

import java.util.*;
import BasicStructures.*;
import MovementStructures.*;
import Variables.CommonFunction;
import Variables.GlobalSetting;
import Variables.PublicGraph;
import processing.core.PApplet;

public class GraphGenerator {
	public List<Vector2> nodeList;
	public int numberOfTarget;
	public int numberOfRoad;
	
	public KinematicOperations operK;
	public List<Edge> edgeList;
	
	public List<ObstacleArea> ObstacleAreaList;
	public List<Integer> ObsOverlapList;
	
	public List<ObstacleArea> StoreAreaList;
	public List<Vector2> StoreEnterList;
	public List<Vector2> storeList;
	
	
	public List<Vector2> obsList;
	public List<Float> weightList;
	
	PApplet parent;
	public int[] obsCheck;
	public boolean isDifferent;
	
	public List<IndexCoordsPair> itemList;
	
	
	public GraphGenerator(MapGenerator Map, KinematicOperations operK, PApplet P){
		this.operK = operK;
		this.parent = P;
		
		edgeList = new ArrayList<Edge>();
		weightList = new ArrayList<Float>();
		obsList = new ArrayList<Vector2>();
		storeList = new ArrayList<Vector2>();
		
		
		//numberOfTarget = Map.targetNode.size();
		numberOfRoad = Map.roadNode.size();
		//wall = Map.level1.size();
		//level2 = Map.level2.size();
		//level3 = Map.level3.size();
		
		nodeList = new ArrayList<Vector2>();
		//nodeList.addAll(Map.targetNode);
		nodeList.addAll(Map.roadNode);
		itemList = new ArrayList<IndexCoordsPair>();
/*
		for(int i= 0; i< nodeList.size(); i++){
			System.out.println(nodeList.get(i).x +", " +nodeList.get(i).y);
		}
*/		
		obsList.addAll(Map.obstacleNode);
		ObstacleAreaList = new ArrayList<ObstacleArea>() ;
		ObsOverlapList = new ArrayList<Integer>();

		storeList.addAll(Map.storeNode);
		StoreAreaList = new ArrayList<ObstacleArea>() ;
		StoreEnterList = new ArrayList<Vector2>() ;

		
		//obsList.addAll(Map.level2);
		//obsList.addAll(Map.level3);
		
		//System.out.println( weightList.size());
	}
	public void getStoreList(List<Node> input){
		// call after G
		Vector2 tempPoint;

		tempPoint = new Vector2(23.0f, 402.0f);
		tempPoint = input.get(CommonFunction.findClose(input, tempPoint)).coordinate;
		StoreEnterList.add(tempPoint);

		tempPoint = new Vector2(240.0f, 411.0f);
		tempPoint = input.get(CommonFunction.findClose(input, tempPoint)).coordinate;
		StoreEnterList.add(tempPoint);
		
		tempPoint = new Vector2(754.0f, 430.0f);
		tempPoint = input.get(CommonFunction.findClose(input, tempPoint)).coordinate;
		StoreEnterList.add(tempPoint);
		
		tempPoint = new Vector2(983.0f, 412.0f);
		tempPoint = input.get(CommonFunction.findClose(input, tempPoint)).coordinate;
		StoreEnterList.add(tempPoint);
		//System.out.println(StoreEnterList.size()+" ***stores");
	}
	public void displayEnterStore(){
		//System.out.println(StoreEnterList.size()+" stores");
		for(int i = 0; i < StoreEnterList.size(); i++){
			parent.pushMatrix();
			parent.fill(0, 0, 255);
			parent.ellipse(StoreEnterList.get(i).x, StoreEnterList.get(i).y, 5, 5);
			parent.fill(0, 0, 255, 110);
			parent.ellipse(StoreEnterList.get(i).x, StoreEnterList.get(i).y, GlobalSetting.closeStore, GlobalSetting.closeStore);
			parent.popMatrix();
		}
	}
	public boolean wantShopping(){
		return false;
	}
	public void createEdge(){
		//int numberLine = GlobalSetting.NeededPath;
		
		int edgeNumber = 0;
		
		getObstacleArea();
		getStoreArea();
		
		for(int i=0; i < nodeList.size(); i++){

			if(checkObstacle(nodeList.get(i))){
				ObsOverlapList.add(1);
			}
			else{
				ObsOverlapList.add(0);	
			}
			//System.out.println(smallIndex.size());
			int currentIndex = i;
			float distance;
			
			// *2 to prevent truncate
			float testDistance;
			testDistance = 2*GlobalSetting.screenWidth/GlobalSetting.tileNumber;
			float slopeDistance;
			slopeDistance = (float) Math.sqrt(
					Math.pow(GlobalSetting.screenWidth/GlobalSetting.tileNumber,2)+
					Math.pow(GlobalSetting.screenWidth/GlobalSetting.tileNumber,2)
			);
			slopeDistance = 2.0f* slopeDistance;
			
			if(ObsOverlapList.get(i) == 1){
				
			}
			else{
			
				//Left up i- GlobalSetting.tileNumber -1
				if((currentIndex-GlobalSetting.tileNumber-1)>=0 ){
					distance = operK.getDisBy2Points(nodeList.get(currentIndex-GlobalSetting.tileNumber-1), nodeList.get(currentIndex));
					if( distance - slopeDistance<= 0.1 && ObsOverlapList.get(currentIndex-GlobalSetting.tileNumber-1) == 0){
	
						edgeList.add(new Edge(i, currentIndex-GlobalSetting.tileNumber-1, distance));
						edgeList.get(edgeNumber).updateWeight(distance);
						edgeNumber++;
					}
				}

				// up i- GlobalSetting.tileNumber
				if((currentIndex-GlobalSetting.tileNumber)>=0 ){
					distance = operK.getDisBy2Points(nodeList.get(currentIndex-GlobalSetting.tileNumber), nodeList.get(currentIndex));
					if( distance -testDistance <=0.1 && ObsOverlapList.get(currentIndex-GlobalSetting.tileNumber) == 0){
						edgeList.add(new Edge(i, currentIndex-GlobalSetting.tileNumber, distance));
						edgeList.get(edgeNumber).updateWeight(distance);
						edgeNumber++;
					}
				}
	

				// right up  i- GlobalSetting.tileNumber +1
				if((currentIndex-GlobalSetting.tileNumber+1)>=0 ){
					distance = operK.getDisBy2Points(nodeList.get(currentIndex-GlobalSetting.tileNumber+1), nodeList.get(currentIndex));
					if( distance - slopeDistance<= 0.1 && ObsOverlapList.get(currentIndex-GlobalSetting.tileNumber+1) == 0){
						edgeList.add(new Edge(i, currentIndex-GlobalSetting.tileNumber+1, distance));
						edgeList.get(edgeNumber).updateWeight(distance);
						edgeNumber++;
					}
				}			

				//left  i-1
				if((currentIndex-1)>=0 ){
					distance = operK.getDisBy2Points(nodeList.get(currentIndex-1), nodeList.get(currentIndex));
					if( distance -testDistance <=0.1 && ObsOverlapList.get(currentIndex-1) == 0){
						edgeList.add(new Edge(i, currentIndex-1, distance));
						edgeList.get(edgeNumber).updateWeight(distance);
						edgeNumber++;
					}
				}
			
			// right i +1
			//left down i+ GlobalSetting.tileNumber -1
			// down i+ GlobalSetting.tileNumber
			//right down  i+ GlobalSetting.tileNumber +1
			}
		}//end out for

		//System.out.println("before: "+ edgeList.size());
		//remove duplicate
		HashSet h  =   new  HashSet(edgeList);
		edgeList.clear();
		edgeList.addAll(h);

		//System.out.println("after: "+ edgeList.size());
		
	}
	
	public void edgeDraw(){
		for(int i = 0; i < edgeList.size(); i++){
			//System.out.println(edgeList.get(i).upIndex + ", " + edgeList.get(i).downIndex+ "     " + weightList.get(i));
			
			//if(weightList.get(i)>1){
			//if(edgeList.get(i).upIndex == 8 && weightList.get(i)==1){
				parent.pushMatrix();
				parent.stroke(0, 0, 0, 255);
				//parent.fill(255, 255, 255);
				parent.line(
						nodeList.get(edgeList.get(i).upIndex).x,
						nodeList.get(edgeList.get(i).upIndex).y,
						nodeList.get(edgeList.get(i).downIndex).x,
						nodeList.get(edgeList.get(i).downIndex).y
				);
				parent.popMatrix();
/*
				parent.pushMatrix();	
					parent.stroke(255, 255, 255);
					parent.fill(255, 255, 255);
					parent.text(
							edgeList.get(i).weight,
							(nodeList.get((edgeList.get(i).upIndex)).x + nodeList.get((edgeList.get(i).downIndex)).x)/2,
							(nodeList.get((edgeList.get(i).upIndex)).y + nodeList.get((edgeList.get(i).downIndex)).y)/2
					);
			 	parent.popMatrix();
*/				
		}
		
	}
	//public void
	public float distanceP2L(Vector2 L1, Vector2 L2, Vector2 P){
		float resultDis = 0 ;
		Vector2 V1 = new Vector2(P.x - L1.x, P.y - L1.y);
		Vector2 V2 = new Vector2(L2.x - L1.x, L2.y - L1.y);
		Vector2 V3 = new Vector2(P.x - L2.x, P.y - L2.y);
		Vector2 V4 = new Vector2(L1.x - L2.x, L1.y - L2.y);

		
		float cosThata1 = (V1.x*V2.x+V1.y*V2.y)/(operK.getLengthByVector2(V1)*operK.getLengthByVector2(V2));
		float cosThata2 = (V3.x*V4.x+V3.y*V4.y)/(operK.getLengthByVector2(V3)*operK.getLengthByVector2(V4));
		
		resultDis = operK.getLengthByVector2(V1)*(float)Math.sin(Math.acos(cosThata1));
		
		if(cosThata1 <0 || cosThata2 <0){
			return -1;
		}
		else{
			return resultDis;
		}
	}
	public void generateItems(){
		int i = 0;
		Vector2 tempPoint;
		IndexCoordsPair tempPair;
		//System.out.println(StoreAreaList.size());

		for(i = 0; i < StoreAreaList.size(); i++){
			for(int k = 0; k < GlobalSetting.numberOfItems; k++){
				tempPoint = new Vector2(
						(float)Math.random()*(StoreAreaList.get(i).ObstacleMax.x - StoreAreaList.get(i).ObstacleMin.x-50)+StoreAreaList.get(i).ObstacleMin.x,
						(float)Math.random()*(StoreAreaList.get(i).ObstacleMax.y - StoreAreaList.get(i).ObstacleMin.y-50)+StoreAreaList.get(i).ObstacleMin.y
						);
				tempPoint = PublicGraph.G.nodeList.get(CommonFunction.findClose(PublicGraph.G.nodeList, tempPoint)).coordinate;
				tempPair = new IndexCoordsPair(i*GlobalSetting.numberOfItems+k, i, tempPoint);
				
				itemList.add(tempPair);
			}
		}
		//System.out.println(itemList.size());

	}
	public void displayItems(){
		
		
		//System.out.println(itemList.size());
		
		for(int i = 0; i < itemList.size(); i++){
			parent.pushMatrix();
			parent.stroke(0,0, 0, 0);
			parent.fill(255, 158, 1, 125);
			parent.ellipse(itemList.get(i).coords.x, itemList.get(i).coords.y, GlobalSetting.closeItem, GlobalSetting.closeItem);

			parent.stroke(0,0, 0, 255);
			parent.fill(255, 158, 1, 255);
			parent.ellipse(itemList.get(i).coords.x, itemList.get(i).coords.y, 10, 10);

			parent.fill(0, 0, 0, 255);

			parent.text(itemList.get(i).index, itemList.get(i).coords.x, itemList.get(i).coords.y-10);					

			parent.popMatrix();
		}
	}
	public List<Vector2> getNodeList(){
		return nodeList;
	}
	public List<Edge> getEdgeList(){
		return edgeList;
	}
	public void getStoreArea(){
		Vector2[] fourCorner;
		fourCorner = new Vector2[4];
		List<Float> xCoords;
		List<Float> yCoords;
		
		ObstacleArea tempArea;
		Vector2 tempMax = new Vector2(0, 0);
		Vector2 tempMin = new Vector2(0, 0);
		tempArea = new ObstacleArea(0, 0, 0, 0);
		
		xCoords = new ArrayList<Float>();
		yCoords = new ArrayList<Float>();
		
		//initial
		for(int i = 0; i < 4; i++){
			fourCorner[i] = new Vector2(0, 0);
		}
		//System.out.println("------");		
		while(storeList.size()>0){
			for(int i = 0; i < 4; i++){
				xCoords.add(storeList.get(0).x);
				yCoords.add(storeList.get(0).y);
				storeList.remove(0);
				if(i >0){
					//tempMax = new Vector2(Math.max(xCoords.get(i), tempMax.x),Math.max(yCoords.get(i), tempMax.y));
					//tempMin = new Vector2(Math.min(xCoords.get(i), tempMax.x),Math.min(yCoords.get(i), tempMax.y));
					
					if(tempMax.x < xCoords.get(i)){
						tempMax.x = xCoords.get(i);
					}
					if(tempMin.x > xCoords.get(i)){
						tempMin.x = xCoords.get(i);
					}
					
					if(tempMax.y < yCoords.get(i)){
						tempMax.y = yCoords.get(i);
					}
					if(tempMin.y > yCoords.get(i)){
						tempMin.y = yCoords.get(i);
					}
					
				
				}
				else{
					tempMax = new Vector2(xCoords.get(i), yCoords.get(i));
					tempMin = new Vector2(xCoords.get(i), yCoords.get(i));
				}
				tempArea = new ObstacleArea(tempMin.x, tempMin.y, tempMax.x, tempMax.y);
			}
			xCoords.clear();
			yCoords.clear();
			tempArea = new ObstacleArea(tempMin.x, tempMin.y, tempMax.x, tempMax.y);
			StoreAreaList.add(tempArea);
			int size = StoreAreaList.size();
			//System.out.println(ObstacleAreaList.get(size-1).ObstacleMax.x +", "+ObstacleAreaList.get(size-1).ObstacleMax.y);
			//System.out.println(ObstacleAreaList.get(size-1).ObstacleMin.x +", "+ObstacleAreaList.get(size-1).ObstacleMin.y);
			//System.out.println("------");
		}
	}
	public void displayDirichletDomain(){
		for(int i = 0; i < PublicGraph.G.nodeList.size(); i++){
			parent.pushMatrix();
			parent.stroke(255,255,255, 255);

			parent.line(
					PublicGraph.G.nodeList.get(i).coordinate.x - (GlobalSetting.screenWidth/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.y - (GlobalSetting.screenHeight/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.x- (GlobalSetting.screenWidth/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.y + (GlobalSetting.screenHeight/GlobalSetting.tileNumber)/2
			);
			parent.line(
					PublicGraph.G.nodeList.get(i).coordinate.x - (GlobalSetting.screenWidth/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.y - (GlobalSetting.screenHeight/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.x + (GlobalSetting.screenWidth/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.y - (GlobalSetting.screenHeight/GlobalSetting.tileNumber)/2
			);
			parent.line(
					PublicGraph.G.nodeList.get(i).coordinate.x + (GlobalSetting.screenWidth/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.y - (GlobalSetting.screenHeight/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.x+ (GlobalSetting.screenWidth/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.y + (GlobalSetting.screenHeight/GlobalSetting.tileNumber)/2
			);

			parent.line(
					PublicGraph.G.nodeList.get(i).coordinate.x - (GlobalSetting.screenWidth/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.y + (GlobalSetting.screenHeight/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.x + (GlobalSetting.screenWidth/GlobalSetting.tileNumber)/2,
					PublicGraph.G.nodeList.get(i).coordinate.y + (GlobalSetting.screenHeight/GlobalSetting.tileNumber)/2
			);
			parent.popMatrix();	
			}
			
	}
	public void displayStore(){
		
		for(int i = 0; i < StoreAreaList.size(); i++){
			parent.pushMatrix();
			parent.stroke(0);
			parent.fill(255, 245, 142, 125);

			parent.rect(
					StoreAreaList.get(i).ObstacleMin.x,
					StoreAreaList.get(i).ObstacleMin.y,
					StoreAreaList.get(i).ObstacleMax.x - StoreAreaList.get(i).ObstacleMin.x,
					StoreAreaList.get(i).ObstacleMax.y - StoreAreaList.get(i).ObstacleMin.y
					);


			parent.popMatrix();	

		}
	}

	public void getObstacleArea(){
		Vector2[] fourCorner;
		fourCorner = new Vector2[4];
		List<Float> xCoords;
		List<Float> yCoords;
		
		ObstacleArea tempArea;
		Vector2 tempMax = new Vector2(0, 0);
		Vector2 tempMin = new Vector2(0, 0);
		tempArea = new ObstacleArea(0, 0, 0, 0);
		
		xCoords = new ArrayList<Float>();
		yCoords = new ArrayList<Float>();
		
		//initial
		for(int i = 0; i < 4; i++){
			fourCorner[i] = new Vector2(0, 0);
		}
		
		while(obsList.size()>0){
			for(int i = 0; i < 4; i++){
				xCoords.add(obsList.get(0).x);
				yCoords.add(obsList.get(0).y);
				obsList.remove(0);
				if(i >0){
					//tempMax = new Vector2(Math.max(xCoords.get(i), tempMax.x),Math.max(yCoords.get(i), tempMax.y));
					//tempMin = new Vector2(Math.min(xCoords.get(i), tempMax.x),Math.min(yCoords.get(i), tempMax.y));
					
					if(tempMax.x < xCoords.get(i)){
						tempMax.x = xCoords.get(i);
					}
					if(tempMin.x > xCoords.get(i)){
						tempMin.x = xCoords.get(i);
					}
					
					if(tempMax.y < yCoords.get(i)){
						tempMax.y = yCoords.get(i);
					}
					if(tempMin.y > yCoords.get(i)){
						tempMin.y = yCoords.get(i);
					}
					
				
				}
				else{
					tempMax = new Vector2(xCoords.get(i), yCoords.get(i));
					tempMin = new Vector2(xCoords.get(i), yCoords.get(i));
				}
				tempArea = new ObstacleArea(tempMin.x, tempMin.y, tempMax.x, tempMax.y);
			}
			xCoords.clear();
			yCoords.clear();
			tempArea = new ObstacleArea(tempMin.x, tempMin.y, tempMax.x, tempMax.y);
			ObstacleAreaList.add(tempArea);
			int size = ObstacleAreaList.size();
			//System.out.println(ObstacleAreaList.get(size-1).ObstacleMax.x +", "+ObstacleAreaList.get(size-1).ObstacleMax.y);
			//System.out.println(ObstacleAreaList.get(size-1).ObstacleMin.x +", "+ObstacleAreaList.get(size-1).ObstacleMin.y);
			//System.out.println("------");

		}
	}
	public void displayObstacle(){
		
		for(int i = 0; i < ObstacleAreaList.size(); i++){
			parent.pushMatrix();
			parent.stroke(0);
			parent.fill(150, 0, 0, 125);

			parent.rect(
					ObstacleAreaList.get(i).ObstacleMin.x,
					ObstacleAreaList.get(i).ObstacleMin.y,
					ObstacleAreaList.get(i).ObstacleMax.x - ObstacleAreaList.get(i).ObstacleMin.x,
					ObstacleAreaList.get(i).ObstacleMax.y - ObstacleAreaList.get(i).ObstacleMin.y
					);


			parent.popMatrix();	

		}
	}
	public boolean checkObstacle(Vector2 targetPosition){
		boolean result = false;
		for(int i = 0 ; i <ObstacleAreaList.size(); i++){
			if(targetPosition.x >= ObstacleAreaList.get(i).ObstacleMin.x -GlobalSetting.obstacleMargin && targetPosition.x <= ObstacleAreaList.get(i).ObstacleMax.x +GlobalSetting.obstacleMargin){
				if(targetPosition.y >= ObstacleAreaList.get(i).ObstacleMin.y -GlobalSetting.obstacleMargin && targetPosition.y <= ObstacleAreaList.get(i).ObstacleMax.y +GlobalSetting.obstacleMargin){
					// overlap with obstacle
					result = true;
					return result;
				}
			}
		}
		return result;
	}
	 public void nodeDisplay(PApplet P){
		 PApplet parent = P;

		 // Node

		 for(int i = 0 ; i < nodeList.size(); i++){
			 if(ObsOverlapList.get(i) ==1){
				 	parent.pushMatrix();
						parent.stroke(0);
						parent.fill(255, 255, 0);
						parent.ellipse(nodeList.get(i).x, nodeList.get(i).y, GlobalSetting.nodeSize, GlobalSetting.nodeSize);
					parent.popMatrix();
			 }
			 else{
			 	parent.pushMatrix();
					parent.stroke(0);
					parent.fill(255, 0, 255);
					parent.ellipse(nodeList.get(i).x, nodeList.get(i).y, GlobalSetting.nodeSize, GlobalSetting.nodeSize);
				parent.popMatrix();
			 }
			 
/*			 	
				parent.pushMatrix();	
					parent.stroke(255, 255, 0);
					parent.fill(255, 255, 0);
					parent.text(i+targetNode.size(), roadNode.get(i).x, roadNode.get(i).y);
			 	parent.popMatrix();
*/
		 }		 

	 }
}
