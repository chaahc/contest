import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;

public class FastCarrierBattleUnitOrder extends BattleUnitOrder {
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		super.bulkOrder(UnitType.Protoss_Gateway, UnitType.Protoss_Dark_Templar, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BattleManager.createdDarkTemplarCount < 6) {
					int darkTemplarCount = 0;
					BuildingUnitGroup gatewayGroup = BuildingUnitManager.instance().getBuildingUnitGroup(UnitType.Protoss_Gateway);
					for (int unitId : gatewayGroup.buildingUnitGroup.keySet()) {
						BuildingUnit gateway = gatewayGroup.buildingUnitGroup.get(unitId);
						if (gateway.getUnit().isTraining()) {
							if (!gateway.getUnit().getTrainingQueue().isEmpty() &&
									gateway.getUnit().getTrainingQueue().get(0) == UnitType.Protoss_Dark_Templar) {
								darkTemplarCount++;
							} 
						}
					}
					BuildingUnit templarArchives = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives);
					if (templarArchives != null && templarArchives.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
							(BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Dark_Templar).getUnitCount() + darkTemplarCount < 4) &&
							MyBotModule.Broodwar.self().minerals() >= 125 && MyBotModule.Broodwar.self().gas() >= 100) {
						return true;
					}
				}
				return false;
			}
		});
		
		super.bulkOrder(UnitType.Protoss_Stargate, UnitType.Protoss_Carrier, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				BuildingUnit fleetBeacon = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Fleet_Beacon);
				if (fleetBeacon != null && fleetBeacon.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
				        BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Carrier).getUnitCount() < 12 &&
						MyBotModule.Broodwar.self().minerals() >= 350 && MyBotModule.Broodwar.self().gas() >= 250) {
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
						BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer).getUnitCount() < 4 &&
						MyBotModule.Broodwar.self().minerals() >= 25 && MyBotModule.Broodwar.self().gas() >= 75) {
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
					if ((attackUnitCount > 7 && (highTemplarCount == 0 || attackUnitCount >= highTemplarCount * 8)) &&
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
				// TODO Auto-generated method stub
				if (BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.DEFENCE_GROUP.getValue()).getUnitCount() == 0 ||
						(BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) > 3 && 
						BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount() <= 6 &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun) != null &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun).isUpgradeCompleted(UpgradeType.Leg_Enhancements) &&
						BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount() <= 4) &&
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
				boolean isTraining = false;
				BuildingUnitGroup stargateGroup = BuildingUnitManager.instance().getBuildingUnitGroup(UnitType.Protoss_Stargate);
				for (int unitId : stargateGroup.buildingUnitGroup.keySet()) {
					BuildingUnit stargate = stargateGroup.buildingUnitGroup.get(unitId);
					isTraining = stargate.getUnit().isTraining();
					if (isTraining) {
						break;
					}
				}
				BuildingUnit cyberneticsCore = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core);
				if (cyberneticsCore != null && cyberneticsCore.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						(BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Carrier).getUnitCount() > 1 &&
						isTraining &&
						MyBotModule.Broodwar.self().minerals() >= 500 && MyBotModule.Broodwar.self().gas() >= 350) ||
						(BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Carrier).getUnitCount() == 0 &&
						!isTraining &&
						BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount() <= 6 &&
						MyBotModule.Broodwar.self().minerals() >= 125 && MyBotModule.Broodwar.self().gas() >= 50) ||
						(BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Carrier).getUnitCount() == 0 &&
						isTraining &&
						BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount() <= 6 &&
						MyBotModule.Broodwar.self().minerals() >= 700 && MyBotModule.Broodwar.self().gas() >= 400)) {
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
	}
}
