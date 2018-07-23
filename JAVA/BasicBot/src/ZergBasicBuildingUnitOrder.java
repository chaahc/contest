import java.util.Iterator;
import java.util.List;
import java.util.Map;

import bwapi.Region;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;

public class ZergBasicBuildingUnitOrder extends BuildingUnitOrder {

	@Override
	public void execute() {
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
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Photon_Cannon) < 3 &&
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
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) < 6 &&
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
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) > 5 &&
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) < 8 &&
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
		
//		super.order(UnitType.Protoss_Gateway, BuildOrderItem.SeedPositionStrategy.SecondExpansionLocation, new OrderCondition() {
//			@Override
//			public boolean isActive() {
//				// TODO Auto-generated method stub
//				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 &&						
//						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) > 7 &&
//						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) < 12 &&
//						MyBotModule.Broodwar.self().minerals() >= 150) {
//					return true;
//				}
//				return false;
//			}
//		});
		

		
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
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 2 &&
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
		
//		super.order(UnitType.Protoss_Arbiter_Tribunal, BuildOrderItem.SeedPositionStrategy.MainBaseBackYard, new OrderCondition() {
//			@Override
//			public boolean isActive() {
//				// TODO Auto-generated method stub
//				if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives) != null &&
//						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
//						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Arbiter_Tribunal) == null &&
//						MyBotModule.Broodwar.self().minerals() >= 200 && MyBotModule.Broodwar.self().gas() >= 150) {
//					return true;
//				}
//				return false;
//			}
//		});
	
		
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
	
	private void orderCenterExpansion() {
		BuildingUnitGroup nexusGroup = BuildingUnitManager.instance().getBuildingUnitGroup(UnitType.Protoss_Nexus);
		List<TilePosition> centerExpansionNearSelf = ProtossBasicBuildPosition.Instance().getCenterExpansionNearSelf();
		for (TilePosition centerExpansion : centerExpansionNearSelf) {
			boolean isNexusInCenterExpansion = false;
			Iterator<Integer> iterator = nexusGroup.buildingUnitGroup.keySet().iterator();
			while (iterator.hasNext()) {
				BuildingUnit nexus = nexusGroup.buildingUnitGroup.get(iterator.next());
				if (nexus.getUnit().getTilePosition().getDistance(centerExpansion) < 10) {
					isNexusInCenterExpansion = true;
				}
			}
			if (!isNexusInCenterExpansion) {
				super.order(UnitType.Protoss_Nexus, centerExpansion, new OrderCondition() {
					@Override
					public boolean isActive() {
						// TODO Auto-generated method stub
						if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 && 
								BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) > 5 &&
								MyBotModule.Broodwar.self().minerals() >= 1000) {
								
							return true;
						}
						return false;
					}
				});
			} 
		}
	}
	
	private void orderExpansionDefence() {
		BuildingUnitGroup nexusGroup = BuildingUnitManager.instance().getBuildingUnitGroup(UnitType.Protoss_Nexus);
		Iterator<Integer> iterator = nexusGroup.buildingUnitGroup.keySet().iterator();
		while (iterator.hasNext()) {
			BuildingUnit nexus = nexusGroup.buildingUnitGroup.get(iterator.next());
			if (nexus.getUnit().getTilePosition().equals(ProtossBasicBuildPosition.mapInfo.get(ProtossBasicBuildPosition.START_BASE))) {
				continue;
			}
			int pylonCount = 0;
			int photoCount = 0;
			boolean isAssimilatorBuildable = true;
			boolean isVespeneGeyserAvailable = false;
			for (Unit unit : nexus.getUnit().getUnitsInRadius(CommandUtil.DEFENCE_RADIUS)) {
				if (unit.getType() == UnitType.Protoss_Pylon) {
					pylonCount++;
				} else if (unit.getType() == UnitType.Protoss_Photon_Cannon) {
					photoCount++;
				} else if (unit.getType() == UnitType.Resource_Vespene_Geyser) {
					isVespeneGeyserAvailable = true;
				} else if (unit.getType() == UnitType.Protoss_Assimilator) {
					isAssimilatorBuildable = false;
				}
			}
			if (pylonCount < 3) {
				super.order(UnitType.Protoss_Pylon, nexus.getUnit().getTilePosition(), new OrderCondition() {
					@Override
					public boolean isActive() {
						// TODO Auto-generated method stub
						if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) > 2 &&
								MyBotModule.Broodwar.self().minerals() >= 100) {
							return true;
						}
						return false;
					}
				});
			}
			if (pylonCount > 1 && photoCount < 4) {
				super.order(UnitType.Protoss_Photon_Cannon, nexus.getUnit().getTilePosition(), new OrderCondition() {
					@Override
					public boolean isActive() {
						// TODO Auto-generated method stub
						if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Forge) != null &&
								BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Forge).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
								BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) > 2 &&
								MyBotModule.Broodwar.self().minerals() >= 100) {
							return true;
						}
						return false;
					}
				});
			}
			if (isVespeneGeyserAvailable && isAssimilatorBuildable) {
				super.order(UnitType.Protoss_Assimilator, nexus.getUnit().getTilePosition(), new OrderCondition() {
					@Override
					public boolean isActive() {
						// TODO Auto-generated method stub
						if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 2 && 
								MyBotModule.Broodwar.self().minerals() >= 100) {
							return true;
						}
						return false;
					}
				});
			}
		}
	}
	
	public void orderPylonGateways() {
		TilePosition pylonPosition = ProtossBasicBuildPosition.mapInfo.get("P"+ProtossBasicBuildPosition.START_BASE);
		BattleUnit zealot =BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.DEFENCE_GROUP.getValue()).getLeader();
		if (zealot != null && zealot.getUnit().exists()) {
			if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 &&
					!zealot.getUnit().isUnderAttack() &&
					BattleManager.instance().getBattleMode() != BattleManager.BattleMode.DEFENCE) {
				CommandUtil.move(zealot.getUnit(), pylonPosition.toPosition());
			}
			if (zealot.getUnit().getTilePosition().getDistance(pylonPosition) < 50) {
				boolean isPylon = false;
				for (Unit unit : zealot.getUnit().getUnitsInRadius(50)) {
					if (unit.getType() == UnitType.Protoss_Pylon) {
						isPylon = true;
						break;
					}
				}
				if (isPylon) {
					super.order(UnitType.Protoss_Gateway, pylonPosition, new OrderCondition() {
						@Override
						public boolean isActive() {
							// TODO Auto-generated method stub
							if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 &&						
									BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) > 11 &&
									BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) < 15 &&
									MyBotModule.Broodwar.self().minerals() >= 150) {
								return true;
							}
							return false;
						}
					});
				} else {
					super.order(UnitType.Protoss_Pylon, pylonPosition, new OrderCondition() {
						@Override
						public boolean isActive() {
							// TODO Auto-generated method stub
							if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 &&	
									MyBotModule.Broodwar.self().minerals() >= 100) {
								return true;
							}
							return false;
						}
					});
				}
			}
		}
	}
}
