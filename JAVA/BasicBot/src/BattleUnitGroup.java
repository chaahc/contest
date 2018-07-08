
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bwapi.UnitType;

public class BattleUnitGroup {
	private UnitType unitType;
	public Map<Integer, BattleUnit> battleUnits = new HashMap<Integer, BattleUnit>();
	private int unitCount;
	private BattleUnit leader;
	
	public BattleUnitGroup(UnitType unitType) {
		this.unitType = unitType;
		this.unitCount = 0;
	}
	
	public void addBattleUnit(BattleUnit battleUnit) {
		this.battleUnits.put(battleUnit.unitId, battleUnit);
		if (this.leader == null) {
			this.leader = battleUnit;
		}
		this.unitCount++;
	}
	
	public void removeBattleUnit(int unitId) {
		this.battleUnits.remove(unitId);
		if (unitCount > 0) {
			this.unitCount--;
		}
		if (unitId == this.leader.getUnitId()) {
			BattleManager.changeReader(this.leader, this);
		}
	}
	
	public int getUnitCount() {
		return this.unitCount;
	}
	
	public void setLeader(BattleUnit unit) {
		this.leader = unit;
	}
	
	public BattleUnit getLeader() {
		return this.leader;
	}
}