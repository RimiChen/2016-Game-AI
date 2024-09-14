package DecisionTree;

import BasicStructures.Vector2;
import Variables.PublicGraph;
import Variables.RunTimeBuffer;

public class SeekPlayer extends Action {
	public int index;
	public Vector2 target;
	public SeekPlayer(int index){
		this.index = index;
		target = new Vector2(-1, -1);
	}
	public void updateTarget(Vector2 target){
		this.target = target;
	}
	
	
	@Override
	public void run(int MonsterIndex) {
		updateTarget(RunTimeBuffer.character.getPosition());
		System.out.println("Monster Seek Player" + target.x +", "+ target.y);
		// TODO Auto-generated method stub
		RunTimeBuffer.Bot[MonsterIndex].Seek(target);

	}
}
