package DecisionTree;

import java.util.ArrayList;
import java.util.List;

import Learning.Algorithm;
import Variables.RunTimeBuffer;

public class DecisionTree {
	Decision root;
	int index;
	public List<Decision> treeNode;
	public int currentAction = -1;
	
	public DecisionTree(int MonsterNumber){
		treeNode = new ArrayList<Decision>();
		root = new Decision(-1, -1, 1);
		treeNode.add(root);
		this.index = MonsterNumber;
	}
	
	public void makeTree(){
		getRoot();
	}
	public void useTree(Decision input){
		if(input.actionNumber>0){
			//do action
			currentAction = input.actionNumber;
		}
		else{
			boolean tempResult;
			tempResult = RunTimeBuffer.Bot[index].check(input.variableIndex);
			
			if(tempResult == true){
				//System.out.println("node index:"+ input.index+ " true, go to next "+input.TrueNode);
				int next = input.TrueNode;
				useTree(treeNode.get(next));
			}
			else{
				//System.out.println("node index:"+ input.index+ " false, go to next "+input.FalseNode);

				int next = input.FalseNode;
				useTree(treeNode.get(next));
			}
			
		}
		
	}
	public void getRoot(){
		Algorithm.traingProcess();
		treeNode.get(0).index = 0;
		treeNode.get(0).variableIndex = Algorithm.rootID;
		List<Integer> allIndex = new ArrayList<Integer>();
		allIndex = Algorithm.getIDList(treeNode.get(0).variableIndex, -1, null);
		treeNode.get(0).trueIndex = Algorithm.getIDList(treeNode.get(0).variableIndex, 1, allIndex);
		treeNode.get(0).falseIndex = Algorithm.getIDList(treeNode.get(0).variableIndex, 0, allIndex);
		treeNode.get(0).actionTrueCount = Algorithm.getTargetList(treeNode.get(0).variableIndex, 1, treeNode.get(0).trueIndex);
		treeNode.get(0).actionFalseCount = Algorithm.getTargetList(treeNode.get(0).variableIndex, 0, treeNode.get(0).falseIndex);
		System.out.println("Action Count:   (1: " + treeNode.get(0).actionTrueCount.get(0)+"), (2: " +treeNode.get(0).actionTrueCount.get(1)+"), (3: " +treeNode.get(0).actionTrueCount.get(2)+"), (4: " +treeNode.get(0).actionTrueCount.get(3)+"), (5: " +treeNode.get(0).actionTrueCount.get(4)+")");
		treeNode.get(0).usedAttribute[treeNode.get(0).variableIndex ] =1;
		createNode(treeNode.get(0));
	}
	public void createNode(Decision input){
		//recursive function
		//trueNode
		Decision newTrueDecision = new Decision(-1, -1, 1);
		
		if(input.actionNumber >=0){
			//this is root or an action
		}
		else{
			newTrueDecision.parentIndex = input.index;
			newTrueDecision.parentValue = 1;
			for(int i = 0; i < 4; i++){
				newTrueDecision.usedAttribute[i] = input.usedAttribute[i];
			}
			float[] trueEntrophy;
			float[] falseEntrophy;
			float[] entrophy;

			
			trueEntrophy = new float[4];
			falseEntrophy = new float[4];
			entrophy = new float[4];

			for(int i = 0; i < 4; i++){
				trueEntrophy[i] = 0;
				falseEntrophy[i] =0;
				entrophy[i] =0;
			}
			List<Integer> tempTrueCount = new ArrayList<Integer>();
			List<Integer> tempFalseCount = new ArrayList<Integer>();
			//compute total entrophy
			float totalEntrophy;
			totalEntrophy = Algorithm.TotalListActionEntrophy(input.trueIndex);
			
			System.out.println("Total Entrophy = " +totalEntrophy);
			if(totalEntrophy ==0){
				int maxCount = 0;
				int countIndex =4; //default wander
				for(int i =0; i< treeNode.get(input.variableIndex).actionTrueCount.size(); i++){
					if(treeNode.get(input.variableIndex).actionTrueCount.get(i)>maxCount){
						maxCount =treeNode.get(input.variableIndex).actionTrueCount.get(i);
						countIndex =i+1;
					}
				}
				
				newTrueDecision.index = treeNode.size();
				newTrueDecision.actionNumber = countIndex; 
				treeNode.add(newTrueDecision);
				//treeNode.get(newTrueDecision.parentIndex).TrueNode = treeNode.get(newTrueDecision.index);
				treeNode.get(newTrueDecision.parentIndex).TrueNode = treeNode.size()-1;
				System.out.println("***choose action "+ countIndex +" as true node"+"   "+treeNode.size());	
				System.out.println("parent "+newTrueDecision.parentIndex+", true node "+treeNode.get(newTrueDecision.parentIndex).TrueNode);
			}
			else{
				float maxIG =0;
				int maxIndex = -1;
				for(int i = 0; i < 4; i++){
					tempTrueCount = Algorithm.getTargetList(i, 1, input.trueIndex);
					tempFalseCount = Algorithm.getTargetList(i, 0, input.trueIndex);
					//System.out.println("--Action True  Count:   (1: " + tempTrueCount.get(0)+"), (2: " +tempTrueCount.get(1)+"), (3: " +tempTrueCount.get(2)+"), (4: " +tempTrueCount.get(3)+"), (5: " +tempTrueCount.get(4)+")");
					//System.out.println("--Action False Count:   (1: " + tempFalseCount.get(0)+"), (2: " +tempFalseCount.get(1)+"), (3: " +tempFalseCount.get(2)+"), (4: " +tempFalseCount.get(3)+"), (5: " +tempFalseCount.get(4)+")");
	
					trueEntrophy[i] =Algorithm.TotalListEntrophy(tempTrueCount, i, 1);
					falseEntrophy[i] =Algorithm.TotalListEntrophy(tempFalseCount, i, 0);
					
					float trueP = Algorithm.getP(tempTrueCount, tempFalseCount);
					float falseP = Algorithm.getP(tempFalseCount, tempTrueCount);
	
					entrophy[i] = trueP*trueEntrophy[i]+falseP*falseEntrophy[i]; 
					//entrophy[i] = trueEntrophy[i]+falseEntrophy[i]; 
					float IG= totalEntrophy - entrophy[i];
					if(IG > maxIG){
						maxIG = IG;
						maxIndex = i;
					}
					//System.out.println("Entrophy = " +entrophy[i]);
				}
				if(maxIndex <0){
					//no information gain
					int maxCount = 0;
					int countIndex =4; //default wander
					for(int i =0; i< tempTrueCount.size(); i++){
						if(tempTrueCount.get(i)>maxCount){
							maxCount =tempTrueCount.get(i);
							countIndex =i+1;
						}
					}
					newTrueDecision.index = treeNode.size();
					newTrueDecision.actionNumber = countIndex; 
					treeNode.add(newTrueDecision);
					//treeNode.get(newTrueDecision.parentIndex).TrueNode = treeNode.get(newTrueDecision.index);					
					treeNode.get(newTrueDecision.parentIndex).TrueNode = treeNode.size()-1;
					System.out.println("***choose action "+ countIndex +" as true node"+"   "+treeNode.size());
					System.out.println("parent "+newTrueDecision.parentIndex+", true node "+ treeNode.get(newTrueDecision.parentIndex).TrueNode);


				}
				else{
					if(newTrueDecision.usedAttribute[maxIndex]==1){
						int maxCount = 0;
						int countIndex =4; //default wander
						for(int i =0; i< tempTrueCount.size(); i++){
							if(tempTrueCount.get(i)>maxCount){
								maxCount =tempTrueCount.get(i);
								countIndex =i+1;
							}
						}
						
						newTrueDecision.actionNumber = countIndex; 
						treeNode.add(newTrueDecision);
						treeNode.get(newTrueDecision.parentIndex).TrueNode = treeNode.size()-1;
						System.out.println("***choose action "+ countIndex +" as true node"+"   "+treeNode.size());
						System.out.println("parent "+newTrueDecision.parentIndex+", true node "+ treeNode.get(newTrueDecision.parentIndex).TrueNode);

					}
					else{
						System.out.println("***choose attribute "+ maxIndex +", IG: "+maxIG +" as true node"+"   "+treeNode.size());
						
						
						newTrueDecision.index = treeNode.size();
						newTrueDecision.variableIndex = maxIndex;
						treeNode.add(newTrueDecision);
						//treeNode.get(newTrueDecision.parentIndex).TrueNode = treeNode.get(newTrueDecision.index);
						treeNode.get(newTrueDecision.parentIndex).TrueNode = treeNode.size()-1;
						System.out.println("parent "+newTrueDecision.parentIndex+", true node "+treeNode.get(newTrueDecision.parentIndex).TrueNode);

						
						treeNode.get(newTrueDecision.index).trueIndex = Algorithm.getIDList(treeNode.get(newTrueDecision.index).variableIndex, 1, input.trueIndex);
						treeNode.get(newTrueDecision.index).falseIndex = Algorithm.getIDList(treeNode.get(newTrueDecision.index).variableIndex, 0, input.trueIndex);
	
						
						treeNode.get(newTrueDecision.index).actionTrueCount = Algorithm.getTargetList(treeNode.get(newTrueDecision.index).variableIndex, 1, treeNode.get(newTrueDecision.index).trueIndex);
						treeNode.get(newTrueDecision.index).actionFalseCount = Algorithm.getTargetList(treeNode.get(newTrueDecision.index).variableIndex, 0, treeNode.get(newTrueDecision.index).falseIndex);
						System.out.println("Action Count:   (1: " + treeNode.get(newTrueDecision.index).actionTrueCount.get(0)+"), (2: " +treeNode.get(newTrueDecision.index).actionTrueCount.get(1)+"), (3: " +treeNode.get(newTrueDecision.index).actionTrueCount.get(2)+"), (4: " +treeNode.get(newTrueDecision.index).actionTrueCount.get(3)+"), (5: " +treeNode.get(newTrueDecision.index).actionTrueCount.get(4)+")");
						treeNode.get(newTrueDecision.index).usedAttribute[maxIndex] =1;
						createNode(treeNode.get(newTrueDecision.index));
					}
				}
			}
			
			
		}

		//False Node
		Decision newFalseDecision = new Decision(-1, -1, 0);
		
		if(input.actionNumber >=0){
			//this is root or an action
		}
		else{
			newFalseDecision.parentIndex = input.index;
			newFalseDecision.parentValue = 0;
			for(int i = 0; i < 4; i++){
				newFalseDecision.usedAttribute[i] = input.usedAttribute[i];
			}			
			//newFalseDecision.parentIndex = input.variableIndex;
			//newFalseDecision.parentValue = 1;
			float[] trueEntrophy;
			float[] falseEntrophy;
			float[] entrophy;

			
			trueEntrophy = new float[4];
			falseEntrophy = new float[4];
			entrophy = new float[4];

			for(int i = 0; i < 4; i++){
				trueEntrophy[i] = 0;
				falseEntrophy[i] =0;
				entrophy[i] =0;
			}
			List<Integer> tempTrueCount = new ArrayList<Integer>();
			List<Integer> tempFalseCount = new ArrayList<Integer>();
			//compute total entrophy
			float totalEntrophy;
			totalEntrophy = Algorithm.TotalListActionEntrophy(input.falseIndex);
			
			System.out.println("Total Entrophy = " +totalEntrophy);
			if(totalEntrophy ==0){
				int maxCount = 0;
				int countIndex =4; //default wander
				for(int i =0; i< treeNode.get(input.variableIndex).actionFalseCount.size(); i++){
					if(treeNode.get(input.variableIndex).actionFalseCount.get(i)>maxCount){
						maxCount =treeNode.get(input.variableIndex).actionFalseCount.get(i);
						countIndex =i+1;
					}
				}

				
				newFalseDecision.index = treeNode.size();
				newFalseDecision.actionNumber = countIndex; 
				treeNode.add(newFalseDecision);
				treeNode.get(newFalseDecision.parentIndex).FalseNode = treeNode.size()-1;
				//treeNode.get(newFalseDecision.parentIndex).FalseNode = treeNode.get(newFalseDecision.index);
				System.out.println("***choose action "+ countIndex +" as false node"+"   "+treeNode.size());	
				System.out.println("parent "+newFalseDecision.parentIndex+", false node "+ treeNode.get(newFalseDecision.parentIndex).FalseNode);

			}
			else{
				float maxIG =0;
				int maxIndex = -1;
				for(int i = 0; i < 4; i++){
					tempTrueCount = Algorithm.getTargetList(i, 1, input.falseIndex);
					tempFalseCount = Algorithm.getTargetList(i, 0, input.falseIndex);
					//System.out.println("--Action True  Count:   (1: " + tempTrueCount.get(0)+"), (2: " +tempTrueCount.get(1)+"), (3: " +tempTrueCount.get(2)+"), (4: " +tempTrueCount.get(3)+"), (5: " +tempTrueCount.get(4)+")");
					//System.out.println("--Action False Count:   (1: " + tempFalseCount.get(0)+"), (2: " +tempFalseCount.get(1)+"), (3: " +tempFalseCount.get(2)+"), (4: " +tempFalseCount.get(3)+"), (5: " +tempFalseCount.get(4)+")");
	
					trueEntrophy[i] =Algorithm.TotalListEntrophy(tempTrueCount, i, 1);
					falseEntrophy[i] =Algorithm.TotalListEntrophy(tempFalseCount, i, 0);
					
					float trueP = Algorithm.getP(tempTrueCount, tempFalseCount);
					float falseP = Algorithm.getP(tempFalseCount, tempTrueCount);
	
					entrophy[i] = trueP*trueEntrophy[i]+falseP*falseEntrophy[i]; 
					//entrophy[i] = trueEntrophy[i]+falseEntrophy[i]; 
					float IG= totalEntrophy - entrophy[i];
					if(IG > maxIG){
						maxIG = IG;
						maxIndex = i;
					}
					//System.out.println("Entrophy = " +entrophy[i]);
				}
				if(maxIndex <0){
					int maxCount = 0;
					int countIndex =4; //default wander
					for(int i =0; i< treeNode.get(input.variableIndex).actionFalseCount.size(); i++){
						if(treeNode.get(input.variableIndex).actionFalseCount.get(i)>maxCount){
							maxCount =treeNode.get(input.variableIndex).actionFalseCount.get(i);
							countIndex =i+1;
						}
					}
					
					newFalseDecision.index = treeNode.size();
					newFalseDecision.actionNumber = countIndex; 
					treeNode.add(newFalseDecision);
					//treeNode.get(newFalseDecision.parentIndex).FalseNode = treeNode.get(newFalseDecision.index);
					treeNode.get(newFalseDecision.parentIndex).FalseNode = treeNode.size()-1;
					System.out.println("***choose action "+ countIndex +" as false node"+"   "+treeNode.size());
					System.out.println("parent "+newFalseDecision.parentIndex+", false node "+treeNode.get(newFalseDecision.parentIndex).FalseNode);					

				}
				else{
					if(newFalseDecision.usedAttribute[maxIndex]==1){
						int maxCount = 0;
						int countIndex =4; //default wander
						for(int i =0; i< treeNode.get(input.variableIndex).actionFalseCount.size(); i++){
							if(treeNode.get(input.variableIndex).actionFalseCount.get(i)>maxCount){
								maxCount =treeNode.get(input.variableIndex).actionFalseCount.get(i);
								countIndex =i+1;
							}
						}
						
						newFalseDecision.actionNumber = countIndex; 
						treeNode.add(newFalseDecision);
						treeNode.get(newFalseDecision.parentIndex).FalseNode = treeNode.size()-1;
						System.out.println("***choose action "+ countIndex +" as false node"+"   "+treeNode.size());
						System.out.println("parent "+newFalseDecision.parentIndex+", false node "+ treeNode.get(newFalseDecision.parentIndex).FalseNode);
					}
					else{
						System.out.println("***choose attribute "+ maxIndex +", IG: "+maxIG +" as false node"+"   "+treeNode.size());
						newFalseDecision.index = treeNode.size();
						newFalseDecision.variableIndex = maxIndex;
						treeNode.add(newFalseDecision);
						//treeNode.get(newFalseDecision.parentIndex).FalseNode = newFalseDecision;
						treeNode.get(newFalseDecision.parentIndex).FalseNode = treeNode.size()-1;
						System.out.println("parent "+newFalseDecision.parentIndex+", false node "+ treeNode.get(newFalseDecision.parentIndex).FalseNode);
						
						treeNode.get(newFalseDecision.index).trueIndex = Algorithm.getIDList(treeNode.get(newFalseDecision.index).variableIndex, 1, input.falseIndex);
						treeNode.get(newFalseDecision.index).falseIndex = Algorithm.getIDList(treeNode.get(newFalseDecision.index).variableIndex, 0, input.falseIndex);
	
						
						treeNode.get(newFalseDecision.index).actionTrueCount = Algorithm.getTargetList(treeNode.get(newFalseDecision.index).variableIndex, 1, treeNode.get(newFalseDecision.index).trueIndex);
						treeNode.get(newFalseDecision.index).actionFalseCount = Algorithm.getTargetList(treeNode.get(newFalseDecision.index).variableIndex, 0, treeNode.get(newFalseDecision.index).falseIndex);
						System.out.println("Action Count:   (1: " + treeNode.get(newFalseDecision.index).actionTrueCount.get(0)+"), (2: " +treeNode.get(newFalseDecision.index).actionTrueCount.get(1)+"), (3: " +treeNode.get(newFalseDecision.index).actionTrueCount.get(2)+"), (4: " +treeNode.get(newFalseDecision.index).actionTrueCount.get(3)+"), (5: " +treeNode.get(newTrueDecision.index).actionTrueCount.get(4)+")");
						treeNode.get(newFalseDecision.index).usedAttribute[maxIndex] =1;
						createNode(treeNode.get(newFalseDecision.index));
					}
				}
			}
			
			
		}		
		
	}
}
