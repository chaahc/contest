

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Unit;
import bwapi.UnitType;

public class BattleUnitGroupManager {
	private static BattleUnitGroupManager instance = new BattleUnitGroupManager();
	
	public static BattleUnitGroupManager instance() {
		return instance;
	}
	
	private Map<UnitType, List<BattleUnitGroup>> multiGroups = new HashMap<UnitType, List<BattleUnitGroup>>();
	private Map<UnitType, BattleUnitGroup> singleGroups = new HashMap<UnitType, BattleUnitGroup>();
	
	public BattleUnitGroupManager() {
		List<BattleUnitGroup> zealotGroup = new ArrayList<BattleUnitGroup>();
		zealotGroup.add(new BattleUnitGroup(UnitType.Protoss_Zealot));
		multiGroups.put(UnitType.Protoss_Zealot, zealotGroup);
		List<BattleUnitGroup> dragoonGroup = new ArrayList<BattleUnitGroup>();
		dragoonGroup.add(new BattleUnitGroup(UnitType.Protoss_Dragoon));
		multiGroups.put(UnitType.Protoss_Dragoon, dragoonGroup);
		singleGroups.put(UnitType.Protoss_High_Templar, new BattleUnitGroup(UnitType.Protoss_High_Templar));
		singleGroups.put(UnitType.Protoss_Dark_Templar, new BattleUnitGroup(UnitType.Protoss_Dark_Templar));
		singleGroups.put(UnitType.Protoss_Archon, new BattleUnitGroup(UnitType.Protoss_Archon));
		singleGroups.put(UnitType.Protoss_Observer, new BattleUnitGroup(UnitType.Protoss_Observer));
		singleGroups.put(UnitType.Protoss_Reaver, new BattleUnitGroup(UnitType.Protoss_Reaver));
		singleGroups.put(UnitType.Protoss_Shuttle, new BattleUnitGroup(UnitType.Protoss_Shuttle));
		singleGroups.put(UnitType.Protoss_Corsair, new BattleUnitGroup(UnitType.Protoss_Corsair));
		singleGroups.put(UnitType.Protoss_Carrier, new BattleUnitGroup(UnitType.Protoss_Carrier));
		singleGroups.put(UnitType.Protoss_Arbiter, new BattleUnitGroup(UnitType.Protoss_Arbiter));
	}
	
	//FOR ZEALOT, DRAGOON
	public List<BattleUnitGroup> getBattleUnitGroups(UnitType unitType) {
		return multiGroups.get(unitType);
	}

	//FOR OTHERS
	public BattleUnitGroup getBattleUnitGroup(UnitType unitType) {
		return singleGroups.get(unitType);
	}
	
	public void addUnit(Unit unit) {
		int unitId = unit.getID();
		UnitType unitType = unit.getType();
		if (UnitType.Protoss_Zealot == unitType) {
			BattleUnit battleUnit = new Zealot(unitId, unit, unitType);
			multiGroups.get(unitType).get(BattleGroupType.FRONT_GROUP.getValue()).addBattleUnit(battleUnit);
		} else if (UnitType.Protoss_Dragoon == unitType) {
			BattleUnit battleUnit = new Dragoon(unitId, unit, unitType);
			multiGroups.get(unitType).get(BattleGroupType.FRONT_GROUP.getValue()).addBattleUnit(battleUnit);
		} else if (UnitType.Protoss_High_Templar == unitType) {
			BattleUnit battleUnit = new HighTemplar(unitId, unit, unitType);
			singleGroups.get(unitType).addBattleUnit(battleUnit);
		} else if (UnitType.Protoss_Dark_Templar == unitType) {
			BattleUnit battleUnit = new DarkTemplar(unitId, unit, unitType);
			singleGroups.get(unitType).addBattleUnit(battleUnit);
		} else if (UnitType.Protoss_Observer == unitType) {
			BattleUnit battleUnit = new Observer(unitId, unit, unitType);
			singleGroups.get(unitType).addBattleUnit(battleUnit);
		} else if (UnitType.Protoss_Shuttle == unitType) {
			BattleUnit battleUnit = new Shuttle(unitId, unit, unitType);
			singleGroups.get(unitType).addBattleUnit(battleUnit);
		} else if (UnitType.Protoss_Reaver == unitType) {
			BattleUnit battleUnit = new Reaver(unitId, unit, unitType);
			singleGroups.get(unitType).addBattleUnit(battleUnit);
		} else if (UnitType.Protoss_Corsair == unitType) {
			BattleUnit battleUnit = new Corsair(unitId, unit, unitType);
			singleGroups.get(unitType).addBattleUnit(battleUnit);
		} else if (UnitType.Protoss_Carrier == unitType) {
			BattleUnit battleUnit = new Carrier(unitId, unit, unitType);
			singleGroups.get(unitType).addBattleUnit(battleUnit);
		} else if (UnitType.Protoss_Arbiter == unitType) {
			BattleUnit battleUnit = new Arbiter(unitId, unit, unitType);
			singleGroups.get(unitType).addBattleUnit(battleUnit);
		} else if (UnitType.Protoss_Archon == unitType) {
			BattleUnit battleUnit = new Archon(unitId, unit, unitType);
			singleGroups.get(unitType).addBattleUnit(battleUnit);
		}
	}
	
	public void removeUnit(Unit unit) {
		UnitType unitType = unit.getType();
		if (UnitType.Protoss_Zealot == unitType || UnitType.Protoss_Dragoon == unitType) {
			multiGroups.get(unitType).get(BattleGroupType.FRONT_GROUP.getValue()).removeBattleUnit(unit.getID());
		}else {
			singleGroups.get(unitType).removeBattleUnit(unit.getID());
		}
	}
}
