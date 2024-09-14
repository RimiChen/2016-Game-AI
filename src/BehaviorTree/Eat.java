package BehaviorTree;

import Variables.RunTimeBuffer;

public class Eat extends Task{
	public int index;
	
	public Eat(int index){
		this.index = index;
	}
	@Override
	public boolean run() {
		// TODO Auto-generated method stub
		RunTimeBuffer.Bot[index].bTree.currentAction = 5;
		RunTimeBuffer.Bot[index].bTree2.currentAction = 5;
		if(RunTimeBuffer.character.money>0){
			//System.out.println("Rob the Character");
			result = true;
			RunTimeBuffer.character.money = RunTimeBuffer.character.money -10;
			RunTimeBuffer.Bot[index].money = RunTimeBuffer.Bot[index].money+10;
			
		}
		else{
			//result = false;
			result = true;
		}
		return result;
	}
}
