import bwapi.UnitType;

public class ZergBasicBattleUnitOrder extends BattleUnitOrder {

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		super.order(UnitType.Protoss_Robotics_Facility, UnitType.Protoss_Observer, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				BuildingUnit observatory = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Observatory);
				if (observatory != null && observatory.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer).getUnitCount() < 5 &&
						MyBotModule.Broodwar.self().minerals() >= 25 && MyBotModule.Broodwar.self().gas() >= 75) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Robotics_Facility, UnitType.Protoss_Shuttle, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				BuildingUnit roboticsFacility = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Facility);
				if (roboticsFacility != null && roboticsFacility.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Shuttle).getUnitCount() < 1 &&
						MyBotModule.Broodwar.self().minerals() >= 200) {
					return true;
				}
				return false;
			}
		});
		
		super.bulkOrder(UnitType.Protoss_Gateway, UnitType.Protoss_High_Templar, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				BuildingUnit templarArchives = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives);
				if (templarArchives != null && templarArchives.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED) {
					int dragoonCount = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount();
					int zealotCount = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount();
					int attackUnitCount= dragoonCount + zealotCount;
					int highTemplarCount = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_High_Templar).getUnitCount();
					BuildingUnitGroup gatewayGroup = BuildingUnitManager.instance().getBuildingUnitGroup(UnitType.Protoss_Gateway);
					for (int unitId : gatewayGroup.buildingUnitGroup.keySet()) {
						BuildingUnit gateway = gatewayGroup.buildingUnitGroup.get(unitId);
						if (gateway.getUnit().isTraining()) {
							if (!gateway.getUnit().getTrainingQueue().isEmpty() &&
									gateway.getUnit().getTrainingQueue().get(0) == UnitType.Protoss_High_Templar) {
								highTemplarCount++;
							} else {
								attackUnitCount++;
							}
						}
					}
					if ((attackUnitCount > 6 && (highTemplarCount == 0 || attackUnitCount >= highTemplarCount * 8)) &&
							MyBotModule.Broodwar.self().minerals() >= 50 && MyBotModule.Broodwar.self().gas() >= 150) {
						return true;
					}
				}
				return false;
			}
		});
		
		super.bulkOrder(UnitType.Protoss_Gateway, UnitType.Protoss_Zealot, new OrderCondition() {
			@Override
			public boolean isActive() {
				if (BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount() <= 3 ||
						(BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) > 5 && 
						BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount() <= 6) &&
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
				int dragoonCount = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount();
				int highTemplarCount = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_High_Templar).getUnitCount();
				if (highTemplarCount == 0) {
					highTemplarCount = 1;
				}
				BuildingUnit cyberneticsCore = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core);
				if (cyberneticsCore != null && cyberneticsCore.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						(dragoonCount < highTemplarCount * 6) &&
						(BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount() <= 12 ||
						BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) > 5) &&
						MyBotModule.Broodwar.self().minerals() >= 125 && MyBotModule.Broodwar.self().gas() >= 50) {
					return true;
				}
				return false;
			}
		});
	}
}
