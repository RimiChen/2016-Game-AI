package BehaviorTree;

import Variables.RunTimeBuffer;

public class MonsterWait extends Task {
	public int index;
	
	public MonsterWait(int index){
		this.index = index;
	}

	@Override
	public boolean run() {
		// TODO Auto-generated method stub
		RunTimeBuffer.Bot[index].bTree.currentAction = 3;
		RunTimeBuffer.Bot[index].bTree2.currentAction = 3;
		//System.out.println("Monster Wait");
		return result;
	}
}
