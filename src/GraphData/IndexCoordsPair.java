package GraphData;

import BasicStructures.Vector2;

public class IndexCoordsPair {
	public int index;
	public  Vector2 coords;
	public int refer;
	public IndexCoordsPair(int Index, int refer, Vector2 Coords){
		this.index = Index;
		this.coords = Coords;
		this.refer = refer;
	}
	
}
