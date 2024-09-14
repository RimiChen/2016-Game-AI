package DecisionTree;

import java.util.ArrayList;
import java.util.List;

public class Decision implements DecisionTreeNode{
	public int index;
	public int actionNumber;
	public List<Boolean> currentChoice; 
	public int TrueNode;
	public int FalseNode;
	public List<Integer> actionTrueCount;
	public List<Integer> actionFalseCount;
	public List<Integer> trueIndex;
	public List<Integer> falseIndex;
	public int variableIndex;
	public int parentIndex;
	public int parentValue;
	public int[] usedAttribute;

	
	public Decision(int variableIndex, int parentIndex, int parentValue){
		actionNumber = -1;
		this.variableIndex = variableIndex;
		this.parentIndex = parentIndex;
		this.parentValue = parentValue;
		actionTrueCount = new ArrayList<Integer>();
		actionFalseCount = new ArrayList<Integer>();
		trueIndex = new ArrayList<Integer>();
		falseIndex = new ArrayList<Integer>();

		TrueNode = -1;
		FalseNode = -1;
		usedAttribute = new int[4];
		
		for(int i = 0; i< 4;i++){
			usedAttribute[i]= 0;
		}
	}
	
	@Override
	public int makeDecision(int index) {
		// TODO Auto-generated method stub
		actionNumber = 0;
		return actionNumber;
	}

}
