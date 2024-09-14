package BehaviorTree;

import BasicStructures.Vector2;
import Variables.PublicGraph;
import Variables.RunTimeBuffer;

public class MonsterSeekStore extends Task {
	public int index;
	public Vector2 target;
	public MonsterSeekStore(int index){
		this.index = index;
		target = new Vector2(-1, -1);
	}
	public void updateTarget(Vector2 target){
		this.target = target;
	}
	
	
	@Override
	public boolean run() {
		RunTimeBuffer.Bot[index].bTree.currentAction = 2;
		RunTimeBuffer.Bot[index].bTree2.currentAction = 2;
		updateTarget(PublicGraph.graphGenerator.StoreEnterList.get(RunTimeBuffer.character.currentStore));
		//System.out.println("Monster Seek Store " + target.x +", "+ target.y);
		// TODO Auto-generated method stub
		RunTimeBuffer.Bot[index].Seek(target);

		result = true;
		return result;
	}
}
