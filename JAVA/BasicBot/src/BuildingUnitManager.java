import java.util.HashMap;
import java.util.Map;

import bwapi.Unit;
import bwapi.UnitType;

public class BuildingUnitManager {
	private static BuildingUnitManager instance = new BuildingUnitManager();
	
	public static BuildingUnitManager instance() {
		return instance;
	}
	
	private Map<UnitType, BuildingUnit> buildingUnits = new HashMap<UnitType, BuildingUnit>();
	private Map<UnitType, BuildingUnitGroup> buildingUnitGroups = new HashMap<UnitType, BuildingUnitGroup>();
	
	public BuildingUnitManager() {
		this.buildingUnitGroups.put(UnitType.Protoss_Nexus, new BuildingUnitGroup(UnitType.Protoss_Nexus));
		this.buildingUnitGroups.put(UnitType.Protoss_Assimilator, new BuildingUnitGroup(UnitType.Protoss_Assimilator));
		this.buildingUnitGroups.put(UnitType.Protoss_Photon_Cannon, new BuildingUnitGroup(UnitType.Protoss_Photon_Cannon));
		this.buildingUnitGroups.put(UnitType.Protoss_Gateway, new BuildingUnitGroup(UnitType.Protoss_Gateway));
		this.buildingUnitGroups.put(UnitType.Protoss_Stargate, new BuildingUnitGroup(UnitType.Protoss_Stargate));
	}
	
	public void addBuildingUnit(UnitType unitType, Unit unit) {
		buildingUnits.put(unitType, new BuildingUnit(unitType, unit));
	}
	
	public void removeBuildingUnit(UnitType unitType) {
		buildingUnits.remove(unitType);
	}
	
	public BuildingUnit getBuildingUnit(UnitType unitType) {
		return buildingUnits.get(unitType);
	}
	
	public int getBuildingUnitCount(UnitType unitType) {
		return this.buildingUnitGroups.get(unitType).getBuildingUnitCount();
	}
	
	public int getCompletedBuildingUnitCount(UnitType unitType) {
		return this.buildingUnitGroups.get(unitType).getCompletedBuildingUnitCount();
	}
	
	public void addBuildingUnitIntoGroup(UnitType unitType, Unit unit) {
		if (this.buildingUnitGroups.get(unitType) == null) {
			return;
		}
		this.buildingUnitGroups.get(unitType).addBuildingUnit(unit);
	}
	
	public void removeBuildingUnitFromGroup(UnitType unitType, int unitId) {
		if (this.buildingUnitGroups.get(unitType) == null) {
			return;
		}
		this.buildingUnitGroups.get(unitType).removeBuildingUnit(unitId);
	}
	
	public void completeBuildingUnitInGroup(UnitType unitType, int unitId) {
		if (this.buildingUnitGroups.get(unitType) == null) {
			return;
		}
		this.buildingUnitGroups.get(unitType).completeBuildingUnit(unitId);
	}
	
	public void trainBuildingUnit(UnitType buildingUnitType, UnitType trainUnitType) {
		if (this.buildingUnitGroups.get(buildingUnitType) == null) {
			return;
		}
		this.buildingUnitGroups.get(buildingUnitType).trainBuildingUnit(trainUnitType);
	}
}
