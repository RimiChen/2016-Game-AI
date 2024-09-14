package BehaviorTree;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends Task{

	public List<Task> children;
	public Sequence(){
		children = new ArrayList<Task>();
	}
	@Override
	public boolean run() {
		// TODO Auto-generated method stub
		//System.out.println("Sequence");
		for(TaskInterface k: children){

			if(!k.run()){
				return false;
			}
		}
		return true;
	}

}
