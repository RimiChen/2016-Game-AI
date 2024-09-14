package Learning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DecisionTree.DecisionTree;
import Variables.LookUp;

public class Algorithm {
	public static List<List<Integer>> attributes;
	public static List<List<Integer>> actionTrueCount;
	public static List<List<Integer>> actionFalseCount;
	public static List<Integer> actionCount;
	public int[] trueCount;
	public int[] falseCount;
	
	public static int rootID;
	

	
	
	public Algorithm(){
		attributes = new ArrayList<List<Integer>>();
		for(int i = 0 ; i< 5; i++){
			List<Integer> tempList = new ArrayList<Integer>();
			attributes.add(tempList);
		}
		initialActionList();
	}
	public static void traingProcess(){
		DecisionTree resultTree = null;
		
		//get Training data
		try {
			getTrainingData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//count All data
		getStatisticAction();
		for(int i = 0 ; i < 4; i++){
			getStatisticAttributeAction(i);
		}
		
		float totalEntrophy =  TotalActionEntrophy();
		System.out.println("Total Entrophy = " + totalEntrophy+"\r\n");

		float[] InfoGain;
		InfoGain = new float[4];
		
		for(int i = 0; i < 4; i++){
			InfoGain[i] = 0;
		}
		//public float TotalListEntrophy(List<Integer> input, int AttributeIndex, int value)
		float maxIG = 0;
		int maxIndex = 0;
		for(int i = 0; i < 4; i++){
			//Compute information gain
			int trueTotal = 0;
			int falseTotal =0;
			for(int k = 0; k < actionCount.size(); k++){
				trueTotal = trueTotal +actionTrueCount.get(i).get(k);
				falseTotal = falseTotal +actionFalseCount.get(i).get(k);

			}
			System.out.println("Attribute "+i+"  true: " +trueTotal +" false: "+falseTotal );
			
			float PTrue = (float)trueTotal/(float)(trueTotal+falseTotal);
			float PFalse = (float)falseTotal/(float)(trueTotal+falseTotal);
			
			InfoGain[i] = InfoGain[i]+ PTrue*(TotalListEntrophy(actionTrueCount.get(i), i, 1));
			InfoGain[i] = InfoGain[i]+ PFalse*TotalListEntrophy(actionFalseCount.get(i), i, 0);
			InfoGain[i] = totalEntrophy - InfoGain[i];
			
			if( InfoGain[i] > maxIG){
				maxIG = InfoGain[i];
				maxIndex = i;
			}
			System.out.println("Attribute "+i+"  IG: "+ InfoGain[i] );
			System.out.println("\r\n");
		}
		
		//Decide node 0
		System.out.println("***Root: use attribute "+maxIndex +", IG: "+maxIG);
		rootID = maxIndex;
		
		
		
		//return resultTree;
	}
	public void initialActionList(){
		// att1 = true   action 0: 100, 1: 200, 3:300 ...
		
		actionTrueCount = new ArrayList<List<Integer>>();
		actionFalseCount = new ArrayList<List<Integer>>();
		actionCount = new ArrayList();
		trueCount = new int[4];
		falseCount = new int[4];
		
		
		for(int j = 0; j < 4; j++){
			List<Integer> tempList = new ArrayList<Integer>();
			actionTrueCount.add(tempList);
			
			List<Integer> tempList2 = new ArrayList<Integer>();
			actionFalseCount.add(tempList2);
		}
		
		for(int i = 0 ; i < 5 ; i++){
			for(int j = 0; j < 4; j++){
				actionTrueCount.get(j).add(0);
				actionFalseCount.get(j).add(0);
			}
			actionCount.add(0);
		}
	}
	public static List<Integer> getIDList(int attribute, int value, List<Integer> targetList){
		List<Integer> idList = new ArrayList();
		if(value <0){
			for(int i = 0; i < attributes.get(4).size(); i++){
				
				if(value <0){
					idList.add(i);
				}
				else{
					if(attributes.get(attribute).get(i)== value){
						idList.add(i);
					}
				}
			}
		}
		else{
			for(int i = 0; i < targetList.size(); i++){
				int index = targetList.get(i);
				if(attributes.get(attribute).get(index) == value){
					idList.add(i);
				}
			}
		}
		return idList;
	}

	
	/*
	public float attributEntrophy(List<Integer> idList){
		float answer;
		return answer;
	}
*/	
	public static float getP(List<Integer> targetList, List<Integer> otherList){
		float P =0;
		int targetTotal = 0;
		int otherTotal =0;
		
		for(int i = 0; i < targetList.size(); i++){
			targetTotal = targetTotal+targetList.get(i);
			otherTotal = otherTotal+otherList.get(i);
		}
		P = (float) targetTotal/ (float)(targetTotal+otherTotal);
		return P;
	}
	public static float TotalListActionEntrophy(List<Integer> idList){
		float answer = 0;
		int total =0;
		List<Integer> count = new ArrayList<Integer>();
		for(int i = 0; i < actionCount.size(); i++){
			count.add(0);
		}
		for(int i = 0 ;  i < idList.size();i++){
			count.set(attributes.get(4).get(idList.get(i))-1, count.get(attributes.get(4).get(idList.get(i))-1)+1);
		}
		
		
		for(int i = 0; i < count.size(); i++){
			total = total+count.get(i);
		}
		//System.out.println("---Action : "+total);

		for(int i = 0; i < count.size(); i++){
			float temp = (float)count.get(i)/(float)total;
			if(temp!= 0){
				answer = answer +computeOnePartEntrophy(temp);
				//System.out.println("---P :("+i+", "+temp+") part Entrophy = "+computeOnePartEntrophy(temp));
			}
		}
		
		return answer;
	}	
	public static float TotalActionEntrophy(){
		float answer = 0;
		int total =0;
		for(int i = 0; i < actionCount.size(); i++){
			total = total+actionCount.get(i);
		}
		//System.out.println("---Action : "+total);

		for(int i = 0; i < actionCount.size(); i++){
			float temp = (float)actionCount.get(i)/(float)total;
			if(temp!= 0){
				answer = answer +computeOnePartEntrophy(temp);
				//System.out.println("---P :("+i+", "+temp+") part Entrophy = "+computeOnePartEntrophy(temp));
			}
		}
		
		return answer;
	}
	public static List<Integer> getTargetList(int attID, int value, List<Integer> indexIDList){
		List<Integer> result = new ArrayList<Integer>();
		for(int i = 0; i < actionCount.size(); i++){
			result.add(0);
		}
		int lineIndex;
		int actionIndex;
		for(int i = 0 ; i < indexIDList.size(); i++){
			lineIndex = indexIDList.get(i);
			if(attributes.get(attID).get(lineIndex)== value){
				actionIndex = attributes.get(4).get(lineIndex);
				result.set(actionIndex -1, result.get(actionIndex-1)+1);
			}
		}
		return result;
	}
	public static float TotalListEntrophy(List<Integer> input, int AttributeIndex, int value){
		float answer = 0;
		int total =0;
		for(int i = 0; i < input.size(); i++){
			total = total+input.get(i);
		}
		
		if(value ==1){
			//System.out.println("\n---Attribute "+AttributeIndex+" True  : "+total);
		}
		else{
			//System.out.println("\n---Attribute "+AttributeIndex+" False : "+total);
		}
		if(total == 0){
			
		}
		else{

			for(int i = 0; i < input.size(); i++){
				float temp = (float)input.get(i)/(float)total;
				if(temp!= 0){
					answer = answer +computeOnePartEntrophy(temp);
					//System.out.println("---P :("+i+", "+temp+") part Entrophy = "+computeOnePartEntrophy(temp));
				}
			}
		}
		return answer;
	}	
	public static float computeOnePartEntrophy(float input){
		//compute -PA log(PA)
		float part;
		float Entrophy;
		//initialActionList();
		if(input == 0){
			return 0;
		}
        part= (float) ((-1) * input *(Math.log(input) / Math.log(2)));

		return part;
	}	
	public static void getStatisticAction(){
		//count how many actions happen
		for(int i =0; i < attributes.get(4).size(); i++){
			//System.out.println(attributes.get(4).get(i));
			actionCount.set(attributes.get(4).get(i)-1, actionCount.get(attributes.get(4).get(i)-1)+1);
		}
	}
	public static void getStatisticAttributeAction(int attributeIndex){
		for(int i =0; i < attributes.get(4).size(); i++){
			if(attributes.get(attributeIndex).get(i) ==1){
				actionTrueCount.get(attributeIndex).set(attributes.get(4).get(i)-1, actionTrueCount.get(attributeIndex).get(attributes.get(4).get(i)-1)+1);
			}
			else{
				actionFalseCount.get(attributeIndex).set(attributes.get(4).get(i)-1, actionFalseCount.get(attributeIndex).get(attributes.get(4).get(i)-1)+1);				
			}

		}
		System.out.println("Attribute "+attributeIndex+" True : (1: " + actionTrueCount.get(attributeIndex).get(0)+"), (2: " +actionTrueCount.get(attributeIndex).get(1)+"), (3: " +actionTrueCount.get(attributeIndex).get(2)+"), (4: " +actionTrueCount.get(attributeIndex).get(3)+"), (5: " +actionTrueCount.get(attributeIndex).get(4)+")");
		System.out.println("Attribute "+attributeIndex+" False: (1: " + actionFalseCount.get(attributeIndex).get(0)+"), (2: " +actionFalseCount.get(attributeIndex).get(1)+"), (3: " +actionFalseCount.get(attributeIndex).get(2)+"), (4: " +actionFalseCount.get(attributeIndex).get(3)+"), (5: " +actionFalseCount.get(attributeIndex).get(4)+")");
	}
	public static void getTrainingData() throws IOException{
        FileReader trainData = null;

        trainData = new FileReader("Train.txt");

        BufferedReader br = new BufferedReader(trainData);
        String tempString;
        String[] ArrayString;
        while (br.ready()) {
        	tempString = br.readLine();
        	ArrayString = tempString.split("\\s+");
        	attributes.get(0).add(LookUp.VariableMap.get(ArrayString[0]));
        	attributes.get(1).add(LookUp.VariableMap.get(ArrayString[1]));
        	attributes.get(2).add(LookUp.VariableMap.get(ArrayString[2]));
        	attributes.get(3).add(LookUp.VariableMap.get(ArrayString[3]));
        	attributes.get(4).add(LookUp.ActionMap.get(ArrayString[4]));
/*        	
        	System.out.println(
        			attributes.get(0).get(attributes.get(0).size()-1)+" , "
        			+attributes.get(1).get(attributes.get(1).size()-1)+" , "
        			+attributes.get(2).get(attributes.get(2).size()-1)+" , "
        			+attributes.get(3).get(attributes.get(3).size()-1)+" , "
        			+attributes.get(4).get(attributes.get(4).size()-1)
        	);
*/       	
        	
        }
        trainData.close();

	}
}
