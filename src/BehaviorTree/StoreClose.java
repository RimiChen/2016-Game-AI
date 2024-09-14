package BehaviorTree;

import Variables.CommonFunction;
import Variables.GlobalSetting;
import Variables.PublicGraph;
import Variables.RunTimeBuffer;

public class StoreClose extends Task{
	public int index;
	
	public StoreClose(int index){
		this.index = index;
	}
	@Override
	public boolean run() {
		// TODO Auto-generated method stub

		int storeID = RunTimeBuffer.character.currentStore;
		if(CommonFunction.OperK.getDisBy2Points(PublicGraph.graphGenerator.StoreEnterList.get(storeID), RunTimeBuffer.Bot[index].getPosition())<GlobalSetting.closeStore/2){
			//System.out.println("Store close");
			result = true;
			RunTimeBuffer.Bot[index].bTree.reachStore = true;
			RunTimeBuffer.Bot[index].bTree2.reachStore = true;		
		}
		else{
			//System.out.println("store not close");
			result = false;
			RunTimeBuffer.Bot[index].bTree.reachStore = false;
			RunTimeBuffer.Bot[index].bTree2.reachStore = false;		
		}
		return result;

	}
}
