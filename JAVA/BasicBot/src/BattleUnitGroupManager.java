

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Unit;
import bwapi.UnitType;
import bwapi.Unitset;

public class BattleUnitGroupManager {
	public static final int FRONT_GROUP = 0;
	public static final int SUB_GROUP = 1;
	public static final int SCOUT_GROUP = 2;
	public static final int DEFENSE_GROUP =3;
	
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
		singleGroups.put(UnitType.Protoss_Scout, new BattleUnitGroup(UnitType.Protoss_Scout));
		singleGroups.put(UnitType.Protoss_Corsair, new BattleUnitGroup(UnitType.Protoss_Corsair));
		singleGroups.put(UnitType.Protoss_Carrier, new BattleUnitGroup(UnitType.Protoss_Carrier));
		singleGroups.put(UnitType.Protoss_Arbiter, new BattleUnitGroup(UnitType.Protoss_Arbiter));
	}
	
	public BattleUnitGroup getBattleUnitGroup(UnitType unitType) {
		return singleGroups.get(unitType);
	}
	
	public List<BattleUnitGroup> getBattleUnitGroups(UnitType unitType) {
		return multiGroups.get(unitType);
	}
	
	public void addUnit(Unit unit) {
		int unitId = unit.getID();
		UnitType unitType = unit.getType();
		if (UnitType.Protoss_Zealot == unitType) {
			BattleUnit battleUnit = new Zealot(unitId, unit, unitType);
			multiGroups.get(unitType).get(FRONT_GROUP).addBattleUnit(battleUnit);
		} else if (UnitType.Protoss_Dragoon == unitType) {
			BattleUnit battleUnit = new Dragoon(unitId, unit, unitType);
			multiGroups.get(unitType).get(FRONT_GROUP).addBattleUnit(battleUnit);
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
		} else if (UnitType.Protoss_Scout == unitType) {
			BattleUnit battleUnit = new Scout(unitId, unit, unitType);
			singleGroups.get(unitType).addBattleUnit(battleUnit);
		}
	}
	
	public void removeUnit(Unit unit) {
		UnitType unitType = unit.getType();
		if (UnitType.Protoss_Zealot == unitType || UnitType.Protoss_Dragoon == unitType) {
			multiGroups.get(unitType).get(FRONT_GROUP).removeBattleUnit(unit.getID());
		}else {
			singleGroups.get(unitType).removeBattleUnit(unit.getID());
		}
	}
}
