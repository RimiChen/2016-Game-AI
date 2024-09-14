package BehaviorTree;

import java.util.ArrayList;
import java.util.List;

public class RandomSelector extends Task{

	public List<Task> children;
	public RandomSelector(){
		children = new ArrayList<Task>();
	}
	@Override
	public boolean run() {
		// TODO Auto-generated method stub
		int childIndex = (int) (Math.random()*children.size());
		for(int i =0; i <children.size(); i++){
			if(i == childIndex){
				//System.out.println("Random Selector" + childIndex);
				children.get(i).setResult(children.get(i).run());
				result = children.get(i).run();
			}
			else{
				children.get(i);
				children.get(i).setResult(false);
			}
		}
		
		return result;
	}

}
