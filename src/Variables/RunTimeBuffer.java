package Variables;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import BasicStructures.ColorVectorRGB;
import BasicStructures.Vector2;
import DecisionTree.Action;
import DrawData.CharacterDrop;
import DrawData.CharacterMonster;
import MovementStructures.KinematicOperations;
import MovementStructures.ResultChange;
import MovementStructures.SystemParameter;
import processing.core.PApplet;

public class RunTimeBuffer {

	public static  List<Integer> currentTargetQueue;
	public static CharacterDrop character;
	public static Vector2 characterSeekPosition;

	
	public static CharacterMonster[] Bot;
	public static List<Integer>[] botsTargetQueue;
	
	public static List<Action> DecisionTreeAction;
	
	public static FileWriter record;
	
	public RunTimeBuffer() throws IOException{
		DecisionTreeAction = new ArrayList<Action>();
		record = new FileWriter("recordData.txt");
		
	}
	public static void recordData(int MonsterIndex){
        try{
        	//if(Bot[MonsterIndex].)
			record.write("IsClose");
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			record.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void closeData(){
        try {
			record.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
