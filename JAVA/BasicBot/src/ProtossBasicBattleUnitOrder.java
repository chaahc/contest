import bwapi.UnitType;
import bwapi.UpgradeType;

public class ProtossBasicBattleUnitOrder extends BattleUnitOrder {

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		if (MyBotModule.Broodwar.self().supplyUsed() < MyBotModule.Broodwar.self().supplyTotal()) {
			if (MyBotModule.Broodwar.self().minerals() >= 100) {
				if (BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleUnitGroupManager.FRONT_GROUP).getUnitCount() <= 12) {
					BuildingUnitManager.instance().trainBuildingUnit(UnitType.Protoss_Gateway, UnitType.Protoss_Zealot);
				}
			}
			
			BuildingUnit cyberneticsCore = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core);
			if (cyberneticsCore != null && cyberneticsCore.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED) {
				if (MyBotModule.Broodwar.self().minerals() >= 125 && MyBotModule.Broodwar.self().gas() >= 50) {
					if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) > 2) {
						BuildingUnitManager.instance().trainBuildingUnit(UnitType.Protoss_Gateway, UnitType.Protoss_Dragoon);
					}
				}
			}
			
			BuildingUnit observatory = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Observatory);
			if (observatory != null && observatory.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED) {
				if (MyBotModule.Broodwar.self().minerals() >= 25 && MyBotModule.Broodwar.self().gas() >= 75) {
					BuildingUnit roboticsFacility = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Facility);
					if (!roboticsFacility.getUnit().isTraining() && BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer).getUnitCount() < 2) {
						roboticsFacility.getUnit().train(UnitType.Protoss_Observer);
					}
				}
			}
		}
	}

}
