import java.util.HashMap;
import java.util.Map;

import bwapi.Position;
import bwapi.Unit;

public class EnemyMap {
	private static EnemyMap instance = new EnemyMap();
	
	public static EnemyMap instance() {
		return instance;
	}
	
	private Map<Integer, Position> enemyMap = new HashMap<Integer, Position>();
	
	public void addEnemyPosition(Unit unit) {
		this.enemyMap.put(unit.getID(), unit.getPosition());
	}
	 
	public Position getEnemyPosition(int unitId) {
		return this.enemyMap.get(unitId);
	}
}
