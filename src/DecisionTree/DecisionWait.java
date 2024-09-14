package DecisionTree;

import Variables.RunTimeBuffer;

public class DecisionWait extends Action{
	public int index;
	
	public DecisionWait(int index){
		this.index = index;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Monster Wait");
	}
}
