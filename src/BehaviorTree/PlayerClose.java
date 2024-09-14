package BehaviorTree;

import Variables.CommonFunction;
import Variables.GlobalSetting;
import Variables.RunTimeBuffer;

public class PlayerClose extends Task{
	public int index;
	
	public PlayerClose(int index){
		this.index = index;
	}
	@Override
	public boolean run() {
		// TODO Auto-generated method stub

		if(CommonFunction.OperK.getDisBy2Points(RunTimeBuffer.character.getPosition(), RunTimeBuffer.Bot[index].getPosition())<GlobalSetting.closeStore/4){
			//System.out.println("player close");
			result = true;
			RunTimeBuffer.Bot[index].bTree.isClose = true;
			RunTimeBuffer.Bot[index].bTree2.isClose = true;
		}
		else{
			//System.out.println("player not close");
			result = false;
			RunTimeBuffer.Bot[index].bTree.isClose = false;
			RunTimeBuffer.Bot[index].bTree2.isClose = false;
		}
		return result;
	}
}
