package DecisionTree;

import Variables.RunTimeBuffer;

public class DecisionWander extends Action{
	public int index;
	
	public DecisionWander(int index){
		this.index = index;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Monster Wander");
		RunTimeBuffer.Bot[index].clearSeek();
		RunTimeBuffer.Bot[index].Wander();
	}
}
