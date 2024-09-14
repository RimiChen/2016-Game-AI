package DecisionTree;

import BasicStructures.Vector2;
import Variables.PublicGraph;
import Variables.RunTimeBuffer;

public class SeekStore extends Action {
	public int index;
	public Vector2 target;
	public SeekStore(int index){
		this.index = index;
		target = new Vector2(-1, -1);
	}
	public void updateTarget(Vector2 target){
		this.target = target;
	}
	
	
	@Override
	public void run() {
		updateTarget(PublicGraph.graphGenerator.StoreEnterList.get(RunTimeBuffer.character.currentStore));
		System.out.println("Monster Seek Store " + target.x +", "+ target.y);
		// TODO Auto-generated method stub
		RunTimeBuffer.Bot[index].Seek(target);

	}
}
