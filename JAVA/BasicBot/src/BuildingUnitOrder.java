import java.util.Iterator;
import java.util.List;

import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;

public abstract class BuildingUnitOrder implements BuildOrder{
	protected void order(UnitType buildingUnit, BuildOrderItem.SeedPositionStrategy seedPositionStrategy, OrderCondition orderCondition) {
		if (orderCondition.isActive()) {
			if (BuildManager.Instance().buildQueue.getItemCount(buildingUnit) == 0 &&
					ConstructionManager.Instance().getConstructionQueueItemCount(buildingUnit, null) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(buildingUnit, seedPositionStrategy, false);
			}
		}
	}
	
	protected void order(UnitType buildingUnit, TilePosition tilePosition, OrderCondition orderCondition) {
		if (orderCondition.isActive()) {
			if (MyBotModule.Broodwar.self().minerals() > 500 && 
					(buildingUnit == UnitType.Protoss_Photon_Cannon ||
					buildingUnit == UnitType.Protoss_Gateway)) {
				if (BuildManager.Instance().buildQueue.getItemCount(buildingUnit) == 0 &&
						ConstructionManager.Instance().getConstructionQueueItemCount(buildingUnit, null) < 2) {
					BuildManager.Instance().buildQueue.queueAsLowestPriority(buildingUnit, tilePosition, false);
				}
			} else {
				if (BuildManager.Instance().buildQueue.getItemCount(buildingUnit) == 0 &&
						ConstructionManager.Instance().getConstructionQueueItemCount(buildingUnit, null) == 0) {
					BuildManager.Instance().buildQueue.queueAsLowestPriority(buildingUnit, tilePosition, false);
				}
			}
		}
	}
	
	
	protected void orderCenterExpansion() {
		BuildingUnitGroup nexusGroup = BuildingUnitManager.instance().getBuildingUnitGroup(UnitType.Protoss_Nexus);
		List<TilePosition> centerExpansionNearSelf = ProtossBasicBuildPosition.Instance().getCenterExpansionNearSelf();
		BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
		if (enemyBaseLocation != null) {
			List<TilePosition> centerExpansionNearEnemy = ProtossBasicBuildPosition.Instance().getCenterExpansionNearEnemy(enemyBaseLocation.getTilePosition());
			int nexusCountInCenterExpansion = 0;
			int enemyCloseCenterExpansion = 3;
			for (TilePosition centerExpansion : centerExpansionNearSelf) {
				boolean isNexusInCenterExpansion = false;
				Iterator<Integer> iterator = nexusGroup.buildingUnitGroup.keySet().iterator();
				while (iterator.hasNext()) {
					BuildingUnit nexus = nexusGroup.buildingUnitGroup.get(iterator.next());
					if (nexus.getUnit().getTilePosition().getDistance(centerExpansion) < 10) {
						isNexusInCenterExpansion = true;
						nexusCountInCenterExpansion++;
					}
				}
				boolean isEnemyClose = false;
				if (centerExpansion.equals(centerExpansionNearEnemy.get(0))) {
					isEnemyClose = true;
					enemyCloseCenterExpansion = 0;
				} else if (centerExpansion.equals(centerExpansionNearEnemy.get(1))) {
					isEnemyClose = true;
					enemyCloseCenterExpansion = 1;
				}
				
				if (!isNexusInCenterExpansion && 
						!isEnemyClose) {
					this.order(UnitType.Protoss_Nexus, centerExpansion, new OrderCondition() {
						@Override
						public boolean isActive() {
							// TODO Auto-generated method stub
							if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 && 
									BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) > 5 &&
									MyBotModule.Broodwar.self().minerals() >= 500) {
									
								return true;
							}
							return false;
						}
					});
				} 
			}
			if (nexusCountInCenterExpansion == 1 && enemyCloseCenterExpansion != 3) {
				boolean isEnemyInCenterExpansion = false;
				for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(centerExpansionNearEnemy.get(enemyCloseCenterExpansion).toPosition(), 50)) {
					if (unit.getPlayer() == MyBotModule.Broodwar.enemy()) {
						isEnemyInCenterExpansion = true;
						break;
					}
				}
				if (!isEnemyInCenterExpansion) {
					this.order(UnitType.Protoss_Nexus, centerExpansionNearEnemy.get(enemyCloseCenterExpansion), new OrderCondition() {
						@Override
						public boolean isActive() {
							// TODO Auto-generated method stub
							if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 && 
									BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Gateway) > 5 &&
									MyBotModule.Broodwar.self().minerals() >= 500) {
									
								return true;
							}
							return false;
						}
					});
				}
			}
		}
	}
	
	protected void orderExpansionDefence() {
		BuildingUnitGroup nexusGroup = BuildingUnitManager.instance().getBuildingUnitGroup(UnitType.Protoss_Nexus);
		Iterator<Integer> iterator = nexusGroup.buildingUnitGroup.keySet().iterator();
		while (iterator.hasNext()) {
			BuildingUnit nexus = nexusGroup.buildingUnitGroup.get(iterator.next());
			int pylonCount = 0;
			int photoCount = 0;
			boolean isAssimilatorBuildable = true;
			boolean isVespeneGeyserAvailable = false;
			TilePosition assimilatorPosition = null;
			for (Unit unit : nexus.getUnit().getUnitsInRadius(CommandUtil.UNIT_RADIUS)) {
				if (unit.getType() == UnitType.Protoss_Pylon) {
					pylonCount++;
				} else if (unit.getType() == UnitType.Protoss_Photon_Cannon) {
					photoCount++;
				} else if (unit.getType() == UnitType.Resource_Vespene_Geyser) {
					isVespeneGeyserAvailable = true;
					assimilatorPosition = unit.getPosition().toTilePosition();
				} else if (unit.getType() == UnitType.Protoss_Assimilator) {
					isAssimilatorBuildable = false;
				}
			}
			if (pylonCount < 2) {
				this.order(UnitType.Protoss_Pylon, nexus.getUnit().getRegion().getCenter().toTilePosition(), new OrderCondition() {
					@Override
					public boolean isActive() {
						// TODO Auto-generated method stub
						if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) > 1 &&
								MyBotModule.Broodwar.self().minerals() >= 100) {
							return true;
						}
						return false;
					}
				});
			}
			if (pylonCount > 0 && photoCount < 4) {
				this.order(UnitType.Protoss_Photon_Cannon, nexus.getUnit().getRegion().getCenter().toTilePosition(), new OrderCondition() {
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
				this.order(UnitType.Protoss_Assimilator, assimilatorPosition, new OrderCondition() {
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
	
	protected void orderPylonGateways() {
		TilePosition pylonPosition = ProtossBasicBuildPosition.mapInfo.get("P"+ProtossBasicBuildPosition.START_BASE);
		boolean isPylon = false;
		int gatewayCount = 0;
		for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(pylonPosition.toPosition(), UnitType.Protoss_Pylon.sightRange())) {
			if (unit.getType() == UnitType.Protoss_Pylon) {
				isPylon = true;
			} else if (unit.getType() == UnitType.Protoss_Gateway) {
				gatewayCount++;
			}
		}
		if (isPylon &&
				gatewayCount < 4) {
			this.order(UnitType.Protoss_Gateway, pylonPosition, new OrderCondition() {
				@Override
				public boolean isActive() {
					// TODO Auto-generated method stub
					if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) >= 3 &&
							MyBotModule.Broodwar.self().minerals() >= 150) {
						return true;
					}
					return false;
				}
			});
		} else if (!isPylon){
			this.order(UnitType.Protoss_Pylon, pylonPosition, new OrderCondition() {
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
	
	protected void orderStartBaseExpansion() {
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
