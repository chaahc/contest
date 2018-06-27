import bwapi.UnitType;

public class ProtossBasicBattleUnitOrder extends BattleUnitOrder {

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		super.bulkOrder(UnitType.Protoss_Gateway, UnitType.Protoss_Zealot, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleUnitGroupManager.FRONT_GROUP).getUnitCount() <= 12 &&  
						MyBotModule.Broodwar.self().minerals() >= 100) {
					return true;
				}
				return false;
			}
		});
		
		super.bulkOrder(UnitType.Protoss_Gateway, UnitType.Protoss_Dragoon, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				BuildingUnit cyberneticsCore = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core);
				if (cyberneticsCore != null && cyberneticsCore.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) > 2 &&
						MyBotModule.Broodwar.self().minerals() >= 125 && MyBotModule.Broodwar.self().gas() >= 50) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Robotics_Facility, UnitType.Protoss_Observer, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				BuildingUnit observatory = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Observatory);
				if (observatory != null && observatory.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer).getUnitCount() < 2 &&
						MyBotModule.Broodwar.self().minerals() >= 25 && MyBotModule.Broodwar.self().gas() >= 75) {
					return true;
				}
				return false;
			}
			
		});
	}
}
