package DecisionTree;

import BasicStructures.Vector2;
import Variables.GlobalSetting;
import Variables.RunTimeBuffer;

public class EatAndBack extends Action {
	public int index;

	
	public EatAndBack(int index){
		this.index = index;
	}
	@Override
	public void run(int MonsterIndex) {
		// TODO Auto-generated method stub
		System.out.println("Monster Back");
		RunTimeBuffer.Bot[index].clearWander();
		RunTimeBuffer.Bot[index].clearSeek();
		Vector2 tempPosition = 
				new Vector2((float)Math.random()*(GlobalSetting.screenWidth-100)+50 , (float)Math.random()*(GlobalSetting.screenHeight/2-150)+GlobalSetting.screenHeight/2);
				
		RunTimeBuffer.Bot[index].updatePosition(tempPosition);

	}
}
