package BehaviorTree;

import BasicStructures.Vector2;

public class MonsterBehaviorTree2 {
	public Selector node1Selector;
	
	public Sequence node2Sequence;
	public Sequence node3Sequence;
	public RandomSelector node4Random;
	
	public PlayerInSight node5Sight;
	public PlayerNotInStore node6Store;	
	public Selector node7Selector;

	public PlayerInSight node8Sight;
	public PlayerInStore node9Store;	
	public Selector node10Selector;

	public MonsterWander node11Wander;
	public MonsterWait node12Wait;

	public Sequence node13Sequence;
	public MonsterSeekPlayer node14Seek;
	
	public RandomSelector node14_0Random;
	public MonsterWait node14_1Wait;
	

	public Sequence node15Sequence;
	public MonsterSeekStore node16Seek;

	public PlayerClose node17Close;
	public Eat node18Eat;
	public MonsterBackToStart node19Back;

	public StoreClose node20Close;
	public MonsterWait node21Wait;

	public boolean inSight;
	public boolean PlayerInStore;
	public boolean isClose;
	public boolean reachStore;
	public int currentAction;
	public Vector2 nowTarget;
	
	
	//parameter
	public int monsterIndex;
	
	public MonsterBehaviorTree2(int MonsterIndex){
		monsterIndex = MonsterIndex;
		
		//create the tree
		inSight = false;
		PlayerInStore = false;
		isClose = false;
		reachStore = false;
		currentAction = -1;
		
		
		nowTarget = new Vector2(-1, -1);

		node1Selector = new Selector();
		
		node2Sequence = new Sequence();
		node3Sequence = new Sequence();
		node4Random = new RandomSelector() ;

		node1Selector.children.add(node2Sequence);
		node1Selector.children.add(node3Sequence);
		node1Selector.children.add(node4Random);
		
		node5Sight = new PlayerInSight(MonsterIndex) ;
		node6Store = new PlayerNotInStore(MonsterIndex);	
		node7Selector = new Selector() ;

		node2Sequence.children.add(node5Sight);
		node2Sequence.children.add(node6Store);
		node2Sequence.children.add(node7Selector);

		//node8Sight = new PlayerInSight(MonsterIndex);
		node9Store = new PlayerInStore(MonsterIndex);	
		node10Selector = new Selector();

		//node3Sequence.children.add(node8Sight);
		node3Sequence.children.add(node9Store);
		node3Sequence.children.add(node10Selector);
		
		node11Wander = new MonsterWander(MonsterIndex);
		node12Wait = new MonsterWait(MonsterIndex) ;

		node4Random.children.add(node11Wander);
		//node4Random.children.add(node12Wait);
		
		node13Sequence =  new Sequence() ;
		node14Seek = new MonsterSeekPlayer(MonsterIndex) ;

		node14_0Random = new RandomSelector();
		node14_1Wait = new MonsterWait(MonsterIndex) ;		
		
		node7Selector.children.add(node13Sequence);
		node7Selector.children.add(node14Seek);
		//node7Selector.children.add(node14_0Random);
		
		//node14_0Random.children.add(node14Seek);
		//node14_0Random.children.add(node14_1Wait);
		
		node15Sequence = new Sequence();
		node16Seek = new MonsterSeekStore(MonsterIndex) ;

		node10Selector.children.add(node15Sequence);
		node10Selector.children.add(node16Seek);
		
		node17Close = new PlayerClose(MonsterIndex) ;
		node18Eat = new Eat(MonsterIndex) ;
		node19Back = new MonsterBackToStart(MonsterIndex) ;

		node13Sequence.children.add(node17Close);
		node13Sequence.children.add(node18Eat);
		node13Sequence.children.add(node19Back);
		
		node20Close = new StoreClose(MonsterIndex) ;
		node21Wait = new MonsterWait(MonsterIndex) ;

		node15Sequence.children.add(node20Close);
		node15Sequence.children.add(node21Wait);
		
	}
	public void run(){
		node1Selector.run();
	}
	
}
