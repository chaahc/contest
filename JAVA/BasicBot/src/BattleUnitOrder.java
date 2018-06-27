import bwapi.UnitType;

public abstract class BattleUnitOrder implements BuildOrder {
	//FOR HIGH TEMPLAR, DARK TEMPLAR, OBSERVER, REAVER, SHUTTLE, CORSAIR, CARRIER, ABITOR
	protected void order(UnitType buildingUnitType, UnitType battleUnitType, OrderCondition orderCondition) {
		if (MyBotModule.Broodwar.self().supplyUsed() < MyBotModule.Broodwar.self().supplyTotal()) {
			if (orderCondition.isActive()) {
				BuildingUnit buildingUnit = BuildingUnitManager.instance().getBuildingUnit(buildingUnitType);
				if (buildingUnit != null && buildingUnit.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED && !buildingUnit.getUnit().isTraining()) {				
					buildingUnit.getUnit().train(battleUnitType);
				}
			}
		}
	}
	
	//FOR GATEWAY, STARGATE, PHOTON CANNON
	protected void bulkOrder(UnitType buildingUnitType, UnitType battleUnitType, OrderCondition orderCondition) {
		if (MyBotModule.Broodwar.self().supplyUsed() < MyBotModule.Broodwar.self().supplyTotal()) {
			if (orderCondition.isActive()) {
				BuildingUnitManager.instance().trainBuildingUnit(buildingUnitType, battleUnitType);
			}
		}
	}
}
