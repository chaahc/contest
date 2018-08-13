import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;

public class FastCarrierBuildingUnitOrder extends BuildingUnitOrder {
	@Override
	public void execute() {
		super.order(UnitType.Protoss_Cybernetics_Core, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Assimilator) >= 1 &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core) == null &&
						MyBotModule.Broodwar.self().minerals() >= 200) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Nexus, BuildOrderItem.SeedPositionStrategy.SecondExpansionLocation, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				boolean isNexusInExpansion = false;
				for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.self()).getPosition(), 50)) {
					if (unit.getType() == UnitType.Protoss_Nexus) {
						isNexusInExpansion = true;
					}
				}
				if (!isNexusInExpansion &&
						BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 && 
						BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Stargate) > 0 &&
						MyBotModule.Broodwar.self().minerals() >= 400) {
					return true;
				}
				return false;
			}
		});
		
		if (BattleManager.instance().getTerranType() == BattleManager.TerranType.BIONIC) {
			super.order(UnitType.Protoss_Gateway, BuildOrderItem.SeedPositionStrategy.FirstChokePoint, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) < 3 &&
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
							BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) >= 3 &&
							MyBotModule.Broodwar.self().minerals() >= 400) {
						return true;
					}
					return false;
				}
			});
			
			super.order(UnitType.Protoss_Citadel_of_Adun, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core) != null &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun) == null &&
							MyBotModule.Broodwar.self().minerals() >= 150 && MyBotModule.Broodwar.self().gas() >= 100) {
						return true;
					}
					return false;
				}
			});
			
			super.order(UnitType.Protoss_Templar_Archives, BuildOrderItem.SeedPositionStrategy.MainBaseBackYard, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun) != null &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives) == null &&
							MyBotModule.Broodwar.self().minerals() >= 150 && MyBotModule.Broodwar.self().gas() >= 200) {
						return true;
					}
					return false;
				}
			});
			
			super.order(UnitType.Protoss_Robotics_Facility, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Facility) == null &&
							BattleManager.createdDarkTemplarCount > 4 &&
							MyBotModule.Broodwar.self().minerals() >= 200 && MyBotModule.Broodwar.self().gas() >= 200) {
						return true;
					}
					return false;
				}
			});
			
			super.order(UnitType.Protoss_Observatory, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
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
			
			super.order(UnitType.Protoss_Stargate, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core) != null &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives) != null &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
							BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Stargate) < 1 &&
							BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) >= 4 &&
							MyBotModule.Broodwar.self().minerals() >= 150 && MyBotModule.Broodwar.self().gas() >= 150) {
						return true;
					}
					return false;
				}
			});
			
			super.order(UnitType.Protoss_Arbiter_Tribunal, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives) != null &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Templar_Archives).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
							BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Stargate) > 0 &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Arbiter_Tribunal) == null &&
							MyBotModule.Broodwar.self().minerals() >= 200 && MyBotModule.Broodwar.self().gas() >= 150) {
						return true;
					}
					return false;
				}
			});
		} else {
			super.order(UnitType.Protoss_Stargate, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core) != null &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
							BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Stargate) < 1 &&
							MyBotModule.Broodwar.self().minerals() >= 150 && MyBotModule.Broodwar.self().gas() >= 150) {
						return true;
					}
					return false;
				}
			});
				
			super.order(UnitType.Protoss_Fleet_Beacon, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Stargate) > 0 &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Fleet_Beacon) == null &&
							MyBotModule.Broodwar.self().minerals() >= 300 && MyBotModule.Broodwar.self().gas() >= 200) {
						return true;
					}
					return false;
				}
			});
			
			super.order(UnitType.Protoss_Gateway, BuildOrderItem.SeedPositionStrategy.FirstChokePoint, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Fleet_Beacon) != null &&	
							BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) < 2 &&
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
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Fleet_Beacon) != null &&
							BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Carrier).getUnitCount() > 0 &&
							MyBotModule.Broodwar.self().minerals() >= 400) {
						return true;
					}
					return false;
				}
			});
			
			super.order(UnitType.Protoss_Robotics_Facility, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Robotics_Facility) == null &&
							BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) > 2 &&
							BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Carrier).getUnitCount() > 2 &&
							MyBotModule.Broodwar.self().minerals() >= 200 && MyBotModule.Broodwar.self().gas() >= 200) {
						return true;
					}
					return false;
				}
			});
			
			super.order(UnitType.Protoss_Observatory, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
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
			
			super.order(UnitType.Protoss_Citadel_of_Adun, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core) != null &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core).getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED &&
							BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun) == null &&
									BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Observatory) != null &&
							MyBotModule.Broodwar.self().minerals() >= 150 && MyBotModule.Broodwar.self().gas() >= 100) {
						return true;
					}
					return false;
				}
			});
			
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
		}
		
		super.order(UnitType.Protoss_Gateway, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 &&	
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) > 1 &&
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) < 8 &&
						MyBotModule.Broodwar.self().minerals() >= 150) {
					return true;
				}
				return false;
			}
		});
		
		super.order(UnitType.Protoss_Forge, BuildOrderItem.SeedPositionStrategy.MainBaseLocation, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 2 &&	
						BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Gateway) >= 2 &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Forge) == null &&
						MyBotModule.Broodwar.self().minerals() >= 150) {
					return true;
				}
				return false;
			}
		});
		
		this.orderStartBaseExpansion();
		
		super.orderExpansionDefence();
		
		super.orderCenterExpansion();
		
		super.orderPylonGateways();
	}

	public void orderStartBaseExpansion() {
		BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
		BaseLocation selfBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self());
		double closestDistance = Integer.MAX_VALUE;
		BaseLocation closestBaseLocation = null;
		for (BaseLocation baseLocation : BWTA.getStartLocations()) {
			if (baseLocation.getTilePosition().equals(selfBaseLocation.getTilePosition())) {
				continue;
			}
			if (enemyBaseLocation != null && baseLocation.getTilePosition().equals(enemyBaseLocation.getTilePosition())) {
				continue;
			}
			
			for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(baseLocation.getTilePosition().toPosition(), 50)) {
				if (unit.getPlayer() == MyBotModule.Broodwar.enemy()) {
					continue;
				}
			}
			
			double distance = baseLocation.getDistance(selfBaseLocation.getPosition());
			if (closestBaseLocation == null || closestDistance >= distance) {
				closestBaseLocation = baseLocation;
				closestDistance = distance;
			}
		}	
		
		boolean isNexusInExpansion = false;
		BuildingUnitGroup nexusGroup = BuildingUnitManager.instance().getBuildingUnitGroup(UnitType.Protoss_Nexus);
		for (int unitId : nexusGroup.buildingUnitGroup.keySet()) {
			BuildingUnit nexus = nexusGroup.buildingUnitGroup.get(unitId);
			if (nexus.getUnit().getTilePosition().getDistance(closestBaseLocation.getTilePosition()) < 50) {
				isNexusInExpansion = true;
			}
		}
		
		if (!isNexusInExpansion) {
			this.order(UnitType.Protoss_Nexus, closestBaseLocation.getTilePosition(), new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					int dragoonUnitCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Dragoon, MyBotModule.Broodwar.self());
					if (((BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 2 &&
							BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) < 3) ||
						(BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 &&
							BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) < 4 &&
							dragoonUnitCount > 12)) &&
							BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) >= 2 &&
							MyBotModule.Broodwar.self().minerals() >= 400) {
						return true;
					}
					return false;
				}
			});
		}
	}
}
