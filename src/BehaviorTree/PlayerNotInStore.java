package BehaviorTree;

import Variables.RunTimeBuffer;

public class PlayerNotInStore extends Task {
	public int index;
	
	public PlayerNotInStore(int index){
		this.index = index;
	}
	@Override
	public boolean run() {
		// TODO Auto-generated method stub

		if(RunTimeBuffer.character.currentStore <0){
			//System.out.println("player not in store");
			result = true;
			RunTimeBuffer.Bot[index].bTree.PlayerInStore = false;
			RunTimeBuffer.Bot[index].bTree2.PlayerInStore = false;			
		}
		else{
			//System.out.println("player is in store");
			result = false;
			RunTimeBuffer.Bot[index].bTree.PlayerInStore = true;
			RunTimeBuffer.Bot[index].bTree2.PlayerInStore = true;
		}
		
		return result;
	}
}
