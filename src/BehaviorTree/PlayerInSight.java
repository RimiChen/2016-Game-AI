package BehaviorTree;

import Variables.CommonFunction;
import Variables.GlobalSetting;
import Variables.RunTimeBuffer;

public class PlayerInSight extends Task {
	public int index;
	
	public PlayerInSight(int index){
		this.index = index;
	}
	@Override
	public boolean run() {
		// TODO Auto-generated method stub

		if(CommonFunction.OperK.getDisBy2Points(RunTimeBuffer.character.getPosition(), RunTimeBuffer.Bot[index].getPosition())<GlobalSetting.closeStore/2){
			//System.out.println("player in sight");
			result = true;
			RunTimeBuffer.Bot[index].bTree.inSight = true;
			RunTimeBuffer.Bot[index].bTree2.inSight = true;
		}
		else{
			//System.out.println("player not in sight");
			result = false;
			RunTimeBuffer.Bot[index].bTree.inSight = false;
			RunTimeBuffer.Bot[index].bTree2.inSight = false;

		}
		return result;
	}
}
