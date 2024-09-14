package BehaviorTree;

import Variables.RunTimeBuffer;

public class MonsterWander extends Task {
	public int index;
	
	public MonsterWander(int index){
		this.index = index;
	}

	@Override
	public boolean run() {
		// TODO Auto-generated method stub
		RunTimeBuffer.Bot[index].bTree.currentAction = 4;
		RunTimeBuffer.Bot[index].bTree2.currentAction = 4;
		//System.out.println("Monster Wander");
		RunTimeBuffer.Bot[index].clearSeek();
		RunTimeBuffer.Bot[index].Wander();
		result = true;
		return result;
	}
}
