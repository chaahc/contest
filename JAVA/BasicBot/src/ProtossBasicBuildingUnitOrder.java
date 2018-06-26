import bwapi.UnitType;

public class ProtossBasicBuildingUnitOrder extends BuildingUnitOrder{

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) == 1 && MyBotModule.Broodwar.self().minerals() >= 400) {
			if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Protoss_Nexus) == 0 && ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Protoss_Nexus, null) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Nexus, BuildOrderItem.SeedPositionStrategy.FirstExpansionLocation, true);
			}
		}
		if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) == 2 && MyBotModule.Broodwar.self().minerals() >= 300 && BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) < 4) {
			if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Protoss_Gateway) == 0 && ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Protoss_Gateway, null) < 2) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Gateway, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, false);
			}
		}
		if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) == 2 && MyBotModule.Broodwar.self().minerals() >= 300 && BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) ==4) {
			if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Protoss_Gateway) == 0 && ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Protoss_Gateway, null) < 2) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Gateway, BuildOrderItem.SeedPositionStrategy.MainBaseBackYard, false);
			}
		}
		
		BuildingUnit assimilator = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Assimilator);
		if (assimilator == null && BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) == 2 && MyBotModule.Broodwar.self().minerals() >= 100) {
			if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Protoss_Assimilator) == 0 && ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Protoss_Assimilator, null) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Assimilator, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			}
		}
		
		BuildingUnit cyberneticsCore = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core);
		if (assimilator != null && cyberneticsCore == null && MyBotModule.Broodwar.self().minerals() >= 200) {
			if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Protoss_Cybernetics_Core) == 0 && ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Protoss_Cybernetics_Core, null) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Cybernetics_Core, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			}
		}
		
		BuildingUnit forge = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Forge);
		if (forge != null && forge.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
				MyBotModule.Broodwar.self().minerals() >= 150) {
			if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Photon_Cannon) < 4 && BuildManager.Instance().buildQueue.getItemCount(UnitType.Protoss_Photon_Cannon) == 0 && ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Protoss_Photon_Cannon, null) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Photon_Cannon, BuildOrderItem.SeedPositionStrategy.SecondChokePoint, true);
			}
		}
		
		if (cyberneticsCore != null && cyberneticsCore.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
				MyBotModule.Broodwar.self().minerals() >= 150 && MyBotModule.Broodwar.self().gas() >= 100) {
			if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) < 3) {
				if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Protoss_Gateway) == 0 && ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Protoss_Gateway, null) == 0) {
					BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Gateway, BuildOrderItem.SeedPositionStrategy.MainBaseBackYard, true);
				}
			}
			if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun) == null && BuildManager.Instance().buildQueue.getItemCount(UnitType.Protoss_Citadel_of_Adun) == 0 && ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Protoss_Citadel_of_Adun, null) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Citadel_of_Adun, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, false);
			}
		}
		
		BuildingUnit citadelOfAdun = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun);
		if (citadelOfAdun != null && citadelOfAdun.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
				MyBotModule.Broodwar.self().minerals() >= 200 && MyBotModule.Broodwar.self().gas() >= 200) {
			if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Facility) == null && BuildManager.Instance().buildQueue.getItemCount(UnitType.Protoss_Robotics_Facility) == 0 && ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Protoss_Robotics_Facility, null) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Robotics_Facility, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, false);
			}
		}
		
		BuildingUnit roboticsFacility = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Facility);
		if (roboticsFacility != null && roboticsFacility.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
				MyBotModule.Broodwar.self().minerals() >= 50 && MyBotModule.Broodwar.self().gas() >= 100) {
			if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Observatory) == null && BuildManager.Instance().buildQueue.getItemCount(UnitType.Protoss_Observatory) == 0 && ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Protoss_Observatory, null) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Observatory, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, false);
			}
		}
	}

}
