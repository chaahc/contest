import java.util.HashMap;
import java.util.Map;

import bwapi.Unit;
import bwapi.UnitType;

public class BuildingUnitGroup {
	private UnitType unitType;
	private Map<Integer, BuildingUnit> buildingUnitGroup = new HashMap<Integer, BuildingUnit>();
	private int buildingUnitCount;
	private int underConstructionBuidlingUnitCount;
	
	public BuildingUnitGroup(UnitType unitType) {
		this.unitType = unitType;
		this.buildingUnitCount = 0;
		this.underConstructionBuidlingUnitCount = 0;
	}
	
	public int getBuildingUnitCount() {
		return this.buildingUnitCount;
	}
	
	public int getCompletedBuildingUnitCount() {
		return this.buildingUnitCount - this.underConstructionBuidlingUnitCount;
	}
	
	public void addBuildingUnit(Unit unit) {
		this.buildingUnitGroup.put(unit.getID(), new BuildingUnit(this.unitType, unit));
		this.buildingUnitCount++;
		this.underConstructionBuidlingUnitCount++;
		if (this.unitType == UnitType.Protoss_Nexus && this.buildingUnitCount == 0) {
			this.underConstructionBuidlingUnitCount = 0;
		}
	}
	
	public void removeBuildingUnit(int unitId) {
		this.buildingUnitGroup.remove(unitId);
		if (this.buildingUnitCount > 0) {
			this.buildingUnitCount--;
		}
	}
	
	public void completeBuildingUnit(int unitId) {
		this.buildingUnitGroup.get(unitId).complete();
		if (this.underConstructionBuidlingUnitCount > 0) {
			this.underConstructionBuidlingUnitCount--;
		}
	}
	
	public void trainBuildingUnit(UnitType unitType) {
		for (int unitId : this.buildingUnitGroup.keySet()) {
			if (this.buildingUnitGroup.get(unitId).getUnit().isTraining()) {
				continue;
			}
			this.buildingUnitGroup.get(unitId).getUnit().train(unitType);
		}
	}
	
	public BuildingUnit getBuildingUnit(int unitId) {
		return this.buildingUnitGroup.get(unitId);
	}
}
