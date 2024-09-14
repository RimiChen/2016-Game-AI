package BehaviorTree;

import java.util.ArrayList;
import java.util.List;

public class Selector extends Task{

	public List<Task> children;
	public Selector(){
		children = new ArrayList<Task>();
	}
	@Override
	public boolean run() {
		// TODO Auto-generated method stub
		//System.out.println("Selector");
		for(Task k:children){
			if(k.run()){
				return true;
			}
		}
		return false;
	}

}
