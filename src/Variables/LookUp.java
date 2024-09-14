package Variables;

import java.util.Map;
import java.util.TreeMap;

public class LookUp {
	public static Map<String, Integer> ActionMap;
	public static Map<String, Integer> VariableMap;
	public LookUp(){
		ActionMap = new TreeMap<String, Integer>();
		VariableMap = new TreeMap<String, Integer>();
		
		ActionMap.put("SeekPlayer", 1);
		ActionMap.put("SeekStore", 2);
		ActionMap.put("Wait", 3);
		ActionMap.put("Wander", 4);
		ActionMap.put("EatPlayer", 5);

		VariableMap.put("InSight", 1);
		VariableMap.put("NotinSight", 0);

		VariableMap.put("PlayerInStore", 1);
		VariableMap.put("PlayerNotInStore", 0);
		
		VariableMap.put("IsClose", 1);
		VariableMap.put("NotClose", 0);
		
		VariableMap.put("ReachStore", 1);
		VariableMap.put("NotReachStore", 0);

	}

}
