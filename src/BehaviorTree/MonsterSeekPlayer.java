package BehaviorTree;

import BasicStructures.Vector2;
import Variables.PublicGraph;
import Variables.RunTimeBuffer;

public class MonsterSeekPlayer extends Task {
	public int index;
	public Vector2 target;
	public MonsterSeekPlayer(int index){
		this.index = index;
		target = new Vector2(-1, -1);
	}
	public void updateTarget(Vector2 target){
		this.target = target;
	}
	
	
	@Override
	public boolean run() {
		RunTimeBuffer.Bot[index].bTree.currentAction = 1;
		RunTimeBuffer.Bot[index].bTree2.currentAction = 1;
		updateTarget(RunTimeBuffer.character.getPosition());
		//System.out.println("Monster Seek Player" + target.x +", "+ target.y);
		// TODO Auto-generated method stub
		RunTimeBuffer.Bot[index].Seek(target);

		result = true;
		return result;
	}
}
