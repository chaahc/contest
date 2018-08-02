import java.util.Iterator;
import java.util.List;
import java.util.Map;

import bwapi.Region;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;

public class ProtossBasicBuildingUnitOrder extends BuildingUnitOrder {

	@Override
	public void execute() {
		int enemyNexusCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Nexus, MyBotModule.Broodwar.enemy());
		int enemyCyberneticsCoreCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Cybernetics_Core, MyBotModule.Broodwar.enemy());
		int enemyAssimilatorCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Assimilator, MyBotModule.Broodwar.enemy());
		int enemyPhotonCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Photon_Cannon, MyBotModule.Broodwar.enemy());
		int enemyGatewayCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Gateway, MyBotModule.Broodwar.enemy());
		int enemyZealotCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Zealot, MyBotModule.Broodwar.enemy());
		int enemyDragoonCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Dragoon, MyBotModule.Broodwar.enemy());
		
//		if (MyBotModule.Broodwar.getFrameCount() % 24 == 0) {
//			System.out.println("[Scout Info] : " + MyBotModule.Broodwar.getFrameCount());
//			System.out.print("Nexus : " + enemyNexusCount);
//			System.out.print(",CyberneticsCore : " + enemyCyberneticsCoreCount);
//			System.out.print(",Assimilator : " + enemyAssimilatorCount);
//			System.out.print(",Photon : " + enemyPhotonCount);
//			System.out.print(",Gateway : " + enemyGatewayCount);
//			System.out.print(",Zealot : " + enemyZealotCount);
//			System.out.println(",Dragoon : " + enemyDragoonCount);
//		}
		
		// TODO Auto-generated method stub
		super.order(UnitType.Protoss_Assimilator, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Assimilator) == 0 &&
						MyBotModule.Broodwar.self().minerals() >= 100) {
					return true;
				}
				return false;
			}
		});

		super.order(UnitType.Protoss_Photon_Cannon, BuildOrderItem.SeedPositionStrategy.SecondChokePoint, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Forge) != null &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Forge).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Photon_Cannon) < 4 &&
						MyBotModule.Broodwar.self().minerals() >= 150) {
					return true;
				}
				return false;
			}
		});
		
		
		super.order(UnitType.Protoss_Nexus, BuildOrderItem.SeedPositionStrategy.FirstExpansionLocation, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) == 1 && 
						MyBotModule.Broodwar.self().minerals() >= 400) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Cybernetics_Core, BuildOrderItem.SeedPositionStrategy.MainBaseBackYard, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) >= 2 &&
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Assimilator) >= 1 &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core) == null &&
						MyBotModule.Broodwar.self().minerals() >= 200) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Gateway, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) >= 2 &&						
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) < 8 &&
						MyBotModule.Broodwar.self().minerals() >= 150) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Gateway, BuildOrderItem.SeedPositionStrategy.FirstExpansionLocation, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 &&						
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) > 7 &&
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) < 9 &&
						MyBotModule.Broodwar.self().minerals() >= 150) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Nexus, BuildOrderItem.SeedPositionStrategy.SecondExpansionLocation, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) == 2 && 
						BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) > 5 &&
						MyBotModule.Broodwar.self().minerals() >= 400) {
					return true;
				}
				return false;
			}
		});
		
		this.orderExpansionDefence();
			
		super.order(UnitType.Protoss_Citadel_of_Adun, BuildOrderItem.SeedPositionStrategy.MainBaseBackYard, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) == 2 &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core) != null &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun) == null &&
						MyBotModule.Broodwar.self().minerals() >= 150 && MyBotModule.Broodwar.self().gas() >= 100) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Robotics_Facility, BuildOrderItem.SeedPositionStrategy.MainBaseBackYard, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) == 2 &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun) != null &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Facility) == null &&
						MyBotModule.Broodwar.self().minerals() >= 200 && MyBotModule.Broodwar.self().gas() >= 200) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Observatory, BuildOrderItem.SeedPositionStrategy.MainBaseBackYard, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Facility) != null &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Facility).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Observatory) == null &&
						MyBotModule.Broodwar.self().minerals() >= 50 && MyBotModule.Broodwar.self().gas() >= 100) {
					return true;
				}
				return false;
			}
		});
		
		this.orderCenterExpansion();
		
		this.orderPylonGateways();
		
		super.order(UnitType.Protoss_Templar_Archives, BuildOrderItem.SeedPositionStrategy.MainBaseBackYard, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun) != null &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives) == null &&
						MyBotModule.Broodwar.self().minerals() >= 150 && MyBotModule.Broodwar.self().gas() >= 200) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Stargate, BuildOrderItem.SeedPositionStrategy.MainBaseBackYard, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core) != null &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Stargate) < 1 &&
						MyBotModule.Broodwar.self().minerals() >= 150 && MyBotModule.Broodwar.self().gas() >= 150) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Arbiter_Tribunal, BuildOrderItem.SeedPositionStrategy.MainBaseBackYard, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives) != null &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Arbiter_Tribunal) == null &&
						MyBotModule.Broodwar.self().minerals() >= 200 && MyBotModule.Broodwar.self().gas() >= 150) {
					return true;
				}
				return false;
			}
		});
	
		
//		super.order(UnitType.Protoss_Robotics_Support_Bay, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
//			@Override
//			public boolean isActive() {
//				// TODO Auto-generated method stub
//				if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Facility) != null &&
//						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Facility).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
//						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Support_Bay) == null &&
//						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) == 2 &&
//						MyBotModule.Broodwar.self().minerals() >= 200 && MyBotModule.Broodwar.self().gas() >= 200) {
//					return true;
//				}
//				return false;
//			}
//		});
		
//		super.order(UnitType.Protoss_Fleet_Beacon, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
//			@Override
//			public boolean isActive() {
//				// TODO Auto-generated method stub
//				if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Stargate) > 0 &&
//						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Fleet_Beacon) == null &&
//						MyBotModule.Broodwar.self().minerals() >= 300 && MyBotModule.Broodwar.self().gas() >= 200) {
//					return true;
//				}
//				return false;
//			}
//		});
	}
}
