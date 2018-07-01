import bwapi.TechType;
import bwapi.UnitType;
import bwapi.UpgradeType;

public abstract class UpgradeOrder implements BuildOrder{
	protected void order(UnitType unitType, UpgradeType upgradeType, OrderCondition orderCondition) {
		if (orderCondition.isActive()) {
			BuildingUnit buildingUnit = BuildingUnitManager.instance().getBuildingUnit(unitType);
			if (buildingUnit != null && buildingUnit.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED) {
				if (buildingUnit.getUnit().canUpgrade(upgradeType)) {
					buildingUnit.getUnit().upgrade(upgradeType);
					buildingUnit.completeUpgrade(upgradeType);
				}
			}
		}
	}
	protected void order(UnitType unitType, TechType techType, OrderCondition orderCondition) {
		if (orderCondition.isActive()) {
			BuildingUnit buildingUnit = BuildingUnitManager.instance().getBuildingUnit(unitType);
			if (buildingUnit != null && buildingUnit.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED) {
				if (buildingUnit.getUnit().canResearch(techType)) {
					buildingUnit.getUnit().research(techType);
					buildingUnit.completeTech(techType);
				}
			}
		}
	}
}
