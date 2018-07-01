import java.util.HashMap;
import java.util.Map;

import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;

public class BuildingUnit {
	private UnitType unitType;
	private Unit unit;
	private BuildingStatus buildingStatus;
	private Map<UpgradeType, Boolean> upgradeStatus = new HashMap<UpgradeType, Boolean>();	
	private Map<TechType, Boolean> techStatus = new HashMap<TechType, Boolean>();
	
	public BuildingUnit(UnitType unitType, Unit unit) {
		this.unitType = unitType;
		this.unit = unit;
		this.buildingStatus = BuildingStatus.UNDER_CONSTRUCTION;
	}
	
	public Unit getUnit() {
		return this.unit;
	}
	
	public void completeUpgrade(UpgradeType upgradeType) {
		upgradeStatus.put(upgradeType, true);
	}
	
	public void completeTech(TechType techType) {
		techStatus.put(techType, true);
	}
	
	public boolean isUpgradeCompleted(UpgradeType upgradeType) {
		if (upgradeStatus.get(upgradeType) != null) {
			return true;
		}
		return false;
	}
	
	public boolean isTechCompleted(TechType techType) {
		if (techStatus.get(techType) != null) {
			return true;
		}
		return false;
	}
	
	public BuildingStatus getBuildingStatus() {
		return this.buildingStatus;
	}
	
	public void underConstruction() {
		this.buildingStatus = BuildingStatus.UNDER_CONSTRUCTION;
	}
	
	public void complete() {
		this.buildingStatus = BuildingStatus.COMPLETED;
	}
	
	public static enum BuildingStatus {
		UNDER_CONSTRUCTION, COMPLETED
	}
}