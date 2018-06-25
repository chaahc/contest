
import java.util.HashMap;
import java.util.Map;

import bwapi.Unit;
import bwapi.UnitType;

public class BattleUnitGroup {
	private UnitType unitType;
	public Map<Integer, BattleUnit> battleUnits = new HashMap<Integer, BattleUnit>();
	private int unitCount;
	private Unit leader;
	
	public BattleUnitGroup(UnitType unitType) {
		this.unitType = unitType;
		this.unitCount = 0;
	}
	
	public void addBattleUnit(BattleUnit battleUnit) {
		battleUnits.put(battleUnit.unitId, battleUnit);
		this.leader = battleUnit.getUnit();
		this.unitCount++;
	}
	
	public void removeBattleUnit(int unitId) {
		battleUnits.remove(unitId);
		if (unitCount > 0) {
			this.unitCount--;
		}
	}
	
	public int getUnitCount() {
		return this.unitCount;
	}
	
	public void setLeader(Unit unit) {
		this.leader = unit;
	}
	
	public Unit getLeader() {
		return this.leader;
	}
}