import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import bwapi.Position;
import bwapi.Region;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class BattleOrder {
	private static final int BASE_RADIUS = 500;
	private static final int CORSAIR_RADIUS = 100;
	
	public void execute() {
		changeBattleMode();
		
		formationAttack();
		detectEnemyAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self()).getPosition());
		detectEnemyAttack(InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.self()).getPosition());
		detectEnemyAttack(InformationManager.Instance().getFirstChokePoint(MyBotModule.Broodwar.self()).getCenter());
		observing();
		defenceExpansion();
		
		executeTotalAttack();
//		highTemplarAttack();
//		dropAttack();
//		corsairAttack();
		arbiterAttack();
	}
	
	public void changeBattleMode() {
		if (InformationManager.Instance().selfPlayer.supplyUsed() > 350) {
			BattleManager.instance().setBattleMode(BattleManager.BattleMode.TOTAL_ATTACK);
		} else {
			BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
		}
	}
	
	public void formationAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.WAIT) {
			BattleManager.instance().makeFormation(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP);
			int dragoonCount = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount();
			if (dragoonCount > 5) {
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, ProtossBasicBuildPosition.mapInfo.get(ProtossBasicBuildPosition.CENTER).toPosition(), BattleGroupType.SUB_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, ProtossBasicBuildPosition.mapInfo.get(ProtossBasicBuildPosition.CENTER).toPosition(), BattleGroupType.SUB_GROUP);
				
				BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
				if (enemyBaseLocation != null) {
					List<TilePosition> centerExpansionNearEnemy = ProtossBasicBuildPosition.Instance().getCenterExpansionNearEnemy(enemyBaseLocation.getTilePosition());
					Position centerExpansionBefore = centerExpansionNearEnemy.get(0).toPosition();
					Position centerExpansionAtfer = centerExpansionNearEnemy.get(1).toPosition();
					if (isEnemyInCenterExpansion(centerExpansionBefore)) {
						BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, centerExpansionBefore, BattleGroupType.SUB_GROUP);
						BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, centerExpansionBefore, BattleGroupType.SUB_GROUP);
					} else if (isEnemyInCenterExpansion(centerExpansionAtfer)) {
						BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, centerExpansionAtfer, BattleGroupType.SUB_GROUP);
						BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, centerExpansionAtfer, BattleGroupType.SUB_GROUP);
					}
					BaseLocation enemySecondExpansionLocation = InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.enemy());				
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemySecondExpansionLocation.getPosition(), BattleGroupType.FRONT_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemySecondExpansionLocation.getPosition(), BattleGroupType.FRONT_GROUP);
				}
			}
		}
	}
	
	private boolean isEnemyInCenterExpansion(Position centerExpansion) {
		boolean isEnemyInCenterExpansion = false;
		for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(centerExpansion, CommandUtil.UNIT_RADIUS)) {
			if (unit.getPlayer() == MyBotModule.Broodwar.enemy()) {
				isEnemyInCenterExpansion = true;
			}
		}
		return isEnemyInCenterExpansion;
	}
	
	public void highTemplarAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		BattleUnitGroup highTemplarGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_High_Templar);
		if (highTemplarGroup.getUnitCount() > 0) {
			for (int unitId : highTemplarGroup.battleUnits.keySet()) {
				HighTemplar highTemplar = (HighTemplar) highTemplarGroup.battleUnits.get(unitId);
				boolean isEnemyInWeaponRange = false;
				if (highTemplar.getUnit().exists()) {
					for (Unit enemy : highTemplar.getUnit().getUnitsInRadius(WeaponType.Psionic_Storm.maxRange())) {
						if (enemy.getPlayer() == MyBotModule.Broodwar.enemy() && enemy.exists() && !enemy.isUnderStorm()) {
							isEnemyInWeaponRange = true;
							if (enemy.getType().isWorker() || (enemy.getType().canAttack() &&
									enemy.getType() != UnitType.Protoss_Zealot &&
									enemy.getType() != UnitType.Zerg_Zergling &&
									enemy.getType() != UnitType.Terran_Firebat)) {						
								highTemplar.psionicStorm(enemy.getPosition());
							}
						}
					}
					if (!isEnemyInWeaponRange) {
						BattleUnit leader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
						CommandUtil.rightClick(highTemplar.getUnit(), leader.getUnit().getPosition());
					}
				}
			}

//				if (highTemplar.getUnit().getEnergy() < 50) {
//					BattleManager.instance().addArchonCandidate(highTemplar);
//				}
			
//			while(BattleManager.instance().getArchonCandidatesCount() > 1) {
//				HighTemplar highTemplar = BattleManager.instance().removeArchonCandidate();
//				HighTemplar targetHighTemplar = BattleManager.instance().removeArchonCandidate();
//				highTemplar.archonWarp(targetHighTemplar.getUnit());
//			}
		}
	}
	
	public void dropAttack() {
		trainWeapon(UnitType.Protoss_Reaver);
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		BattleUnitGroup shuttleGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Shuttle);
		if (shuttleGroup.getUnitCount() > 0) {
			for (int unitId : shuttleGroup.battleUnits.keySet()) {
				BattleUnit shuttle = shuttleGroup.battleUnits.get(unitId);
				if (shuttle.getUnit().getSpaceRemaining() != 0) {
					for (Unit unit : shuttle.getUnit().getUnitsInRadius(BASE_RADIUS)) {
						if (unit.getPlayer() == MyBotModule.Broodwar.self()) {
							if ((unit.getType() == UnitType.Protoss_High_Templar && (unit.isUnderAttack() || unit.getEnergy() < 50)) ||
								(unit.getType() == UnitType.Protoss_Reaver && (unit.isUnderAttack() || unit.getScarabCount() == 0))) {				
								System.out.println(unit.getID() + ", hp : " + unit.getShields() + unit.getHitPoints() +", mp : " + unit.getEnergy());
								shuttle.getUnit().load(unit);
							} 
						}
					}
				} else {
					if (!shuttle.getUnit().getPosition().toTilePosition().equals(new TilePosition(60, 120))) {
						System.out.println("target not reached, current position : " +shuttle.getUnit().getPosition().toTilePosition());
						BattleManager.instance().addBattleSingleOrder(
								new ShuttleMove(shuttle.getUnit(), Arrays.asList(
										new MovePosition(InformationManager.Instance().getFirstChokePoint(MyBotModule.Broodwar.self()).getCenter())
										)
									)
								);		
					}
					System.out.println("shuttle current position : " +shuttle.getUnit().getPosition().toTilePosition());
					if (shuttle.getUnit().getPosition().toTilePosition().equals(InformationManager.Instance().getFirstChokePoint(MyBotModule.Broodwar.self()).getCenter().toTilePosition())) {
						System.out.println("target reached, current position : " + shuttle.getUnit().getPosition().toTilePosition());
						shuttle.getUnit().unloadAll();
						CommandUtil.move(shuttle.getUnit(), InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self()).getPosition());
//						for (Unit unit : shuttle.getUnit().getLoadedUnits()) {
//							if (unit.getType() == UnitType.Protoss_High_Templar && 
//									unit.getEnergy() > TechType.Psionic_Storm.energyCost()) {
//								System.out.println(unit.getID() + ", enegy : " + unit.getEnergy());
//								shuttle.getUnit().unload(unit);
//							} else if (unit.getType() == UnitType.Protoss_Reaver &&
//									unit.getScarabCount() > 3) {
//								System.out.println(unit.getID() + ", scrap : " + unit.getScarabCount());
//								shuttle.getUnit().unload(unit);
//							}
//						}
					}
					
				}
			}
		}
	}
	
	public void corsairAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		BattleUnitGroup corsairGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Corsair);
		if (corsairGroup.getUnitCount() > 0) {
			for (int unitId : corsairGroup.battleUnits.keySet()) {
				Corsair corsair = (Corsair) corsairGroup.battleUnits.get(unitId);
				if (corsair.getUnit().isUnderAttack()) {
					for (Unit enemy : corsair.getUnit().getUnitsInRadius(CORSAIR_RADIUS)) {
						if (enemy != null && !enemy.isUnderDisruptionWeb() && enemy.getPlayer() == MyBotModule.Broodwar.enemy()) {
							if (enemy.getType() == UnitType.Protoss_Photon_Cannon ||
									enemy.getType() == UnitType.Terran_Bunker ||
									enemy.getType() == UnitType.Terran_Missile_Turret ||
									enemy.getType() == UnitType.Zerg_Sunken_Colony ||
									enemy.getType() == UnitType.Zerg_Spore_Colony) {
								corsair.disruptionWeb(enemy);
							}
						}
					}
				} else {
					BattleUnitGroup arbiterGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Arbiter);
					BattleUnit arbiter = arbiterGroup.getLeader();
					if (arbiter != null && arbiter.getUnit().exists() && arbiter.getUnit().getEnergy() > 150) {
						//required move logic
						boolean isAroundArbiter = false;
						for (Unit unit : corsair.getUnit().getUnitsInRadius(CORSAIR_RADIUS)) {
							if (unit.getType() == UnitType.Protoss_Arbiter) {
								isAroundArbiter = true;
								break;
							}
						}
						if (!isAroundArbiter) {
							corsair.getUnit().stop();
						} else {
							BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
							CommandUtil.move(corsair.getUnit(), enemyBaseLocation.getPosition());
						}
					}
				}
			}
		}
	}
	
	public void carrierAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		trainWeapon(UnitType.Protoss_Carrier);
	}
	
	public void arbiterAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		BattleUnit frontDragoon = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
		BattleUnit subDragoon = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.SUB_GROUP.getValue()).getLeader();
		BattleUnitGroup arbiterGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Arbiter);
		if (arbiterGroup.getUnitCount() > 0) {
			Iterator<Integer> iterator = arbiterGroup.battleUnits.keySet().iterator();
			while (iterator.hasNext()) {
				BattleUnit arbiter = arbiterGroup.battleUnits.get(iterator.next());
				this.arbiterStatisFieldAttack(arbiter);
				this.unitFollow(arbiter, frontDragoon);
				if (iterator.hasNext()) {
					arbiter = arbiterGroup.battleUnits.get(iterator.next());
					this.arbiterStatisFieldAttack(arbiter);
					this.unitFollow(arbiter, subDragoon);
				}
			}
		}
	}
	
	private void arbiterStatisFieldAttack(BattleUnit arbiter) {
		if (arbiter.getUnit().getEnergy() > 100) {
			for (Unit enemy : arbiter.getUnit().getUnitsInRadius(WeaponType.Stasis_Field.maxRange())) {
				if (enemy.getPlayer() == MyBotModule.Broodwar.enemy() && enemy.exists() && !enemy.isStasised()) {
					if (enemy.getType() == UnitType.Terran_Siege_Tank_Tank_Mode ||
						enemy.getType() == UnitType.Terran_Siege_Tank_Siege_Mode ||
						enemy.getType() == UnitType.Terran_Battlecruiser ||
						enemy.getType() == UnitType.Protoss_Carrier ||
						enemy.getType() == UnitType.Protoss_Dragoon ||
						enemy.getType() == UnitType.Zerg_Ultralisk ||
						enemy.getType() == UnitType.Zerg_Hydralisk) {
						((Arbiter)arbiter).stasisField(enemy.getPosition());
					}
				}
			}
		} 
	}
	
	public void arbiterRecallAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		BattleUnitGroup arbiterGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Arbiter);
		if (arbiterGroup.getUnitCount() > 0) {
			BattleUnit arbiter = arbiterGroup.getLeader();
			if (!arbiter.getUnit().exists()) {
				arbiter = BattleManager.changeReader(arbiter, arbiterGroup);
			}
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			BattleUnitGroup corsairGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Corsair);
			BattleUnit corsair = corsairGroup.getLeader();
			if (corsair != null && corsair.getUnit().exists() &&
					!arbiter.getUnit().isFollowing() && arbiter.getUnit().canFollow(corsair.getUnit()) &&
							(arbiter.getUnit().getDistance(enemyBaseLocation.getPosition()) > 300)) {
				arbiter.getUnit().follow(corsair.getUnit());
			} else {
				corsair = BattleManager.changeReader(corsair, corsairGroup);
			}
			
			BattleUnitGroup dragoonGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue());
			if (arbiter.getUnit().getEnergy() > 150 &&
					(arbiter.getUnit().getDistance(enemyBaseLocation.getPosition()) < 300 ||
					(arbiter.getUnit().isUnderAttack() &&
							arbiter.getUnit().getShields() + arbiter.getUnit().getHitPoints() < 200))
					) {
				((Arbiter)arbiter).recall(dragoonGroup.getLeader().getUnit().getPosition());
			} 
		}
	}
	
	public void executeTotalAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.TOTAL_ATTACK) {
			List<BaseLocation> baseLocations = InformationManager.Instance().getOccupiedBaseLocations(MyBotModule.Broodwar.enemy());
			BaseLocation enemyBaseLocation = null;
			if (baseLocations.isEmpty()) {
				enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
				
			} else {
				for (BaseLocation baseLocation : baseLocations) {
					enemyBaseLocation = baseLocation;
				}
			}
			BattleManager.instance().makeFormation(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, new Position(enemyBaseLocation.getRegion().getX(), enemyBaseLocation.getRegion().getY()), BattleGroupType.FRONT_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, new Position(enemyBaseLocation.getRegion().getX(), enemyBaseLocation.getRegion().getY()), BattleGroupType.FRONT_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, new Position(enemyBaseLocation.getRegion().getX(), enemyBaseLocation.getRegion().getY()), BattleGroupType.SUB_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, new Position(enemyBaseLocation.getRegion().getX(), enemyBaseLocation.getRegion().getY()), BattleGroupType.SUB_GROUP);
		}
	}
	
	public void detectEnemyAttack(Position target) {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		boolean isEnemyAttack = false;
		List<Unit> list = MyBotModule.Broodwar.getUnitsInRadius(target, BASE_RADIUS);
		for (Unit unit : list) {
			if (unit.getPlayer() == InformationManager.Instance().enemyPlayer) {
				BattleManager.instance().setBattleMode(BattleManager.BattleMode.DEFENCE);
				isEnemyAttack = true;
				break;
			}
		}
		if (isEnemyAttack) {
			BattleManager.instance().closestAttack(UnitType.Protoss_Zealot, BattleGroupType.DEFENCE_GROUP);
			BattleManager.instance().closestAttack(UnitType.Protoss_Dragoon, BattleGroupType.DEFENCE_GROUP);
			BattleManager.instance().closestAttack(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().closestAttack(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP);
		} else {
			if (BattleManager.instance().getBattleMode() != BattleManager.BattleMode.TOTAL_ATTACK) {
				BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
			}
		}
	}
	
	public void defenceExpansion() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		BaseLocation secondExpantionLocation = InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.self());
		Chokepoint firstChokepoint = InformationManager.Instance().getFirstChokePoint(MyBotModule.Broodwar.self());
		BattleManager.instance().makeFormation(UnitType.Protoss_Zealot, BattleGroupType.DEFENCE_GROUP);
		BattleManager.instance().makeFormation(UnitType.Protoss_Dragoon, BattleGroupType.DEFENCE_GROUP);
		if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) > 2) {
			BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, secondExpantionLocation.getPosition(), BattleGroupType.DEFENCE_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, secondExpantionLocation.getPosition(), BattleGroupType.DEFENCE_GROUP);
		} else {
			BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, firstChokepoint.getCenter(), BattleGroupType.DEFENCE_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, firstChokepoint.getCenter(), BattleGroupType.DEFENCE_GROUP);
		}
	}
	
	public void observing() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		BattleUnitGroup observerGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer);		
		if (observerGroup.getUnitCount() > 0) {
			Iterator<Integer> iterator = observerGroup.battleUnits.keySet().iterator();
			BattleUnit observer = observerGroup.battleUnits.get(iterator.next());
			BattleUnit zealot = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
			this.unitFollow(observer, zealot);
			if (iterator.hasNext()) {
				observer = observerGroup.battleUnits.get(iterator.next());
				BattleUnit dragoon = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
				this.unitFollow(observer, dragoon);
			}
			List<TilePosition> centerExpansionNearEnemy = ProtossBasicBuildPosition.Instance().getCenterExpansionNearEnemy(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()).getTilePosition());
			if (!centerExpansionNearEnemy.isEmpty()) {
				if (iterator.hasNext()) {
					observer = observerGroup.battleUnits.get(iterator.next());
					CommandUtil.move(observer.getUnit(), centerExpansionNearEnemy.get(0).toPosition());
				}
				if (iterator.hasNext()) {
					observer = observerGroup.battleUnits.get(iterator.next());
					CommandUtil.move(observer.getUnit(), centerExpansionNearEnemy.get(1).toPosition());
				}
			}
			if (iterator.hasNext()) {
				observer = observerGroup.battleUnits.get(iterator.next());
				BaseLocation secondExpantionLocation = InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.self());
				CommandUtil.move(observer.getUnit(), secondExpantionLocation.getPosition());
			}
		}
	}
	
	private void unitFollow(BattleUnit follower, BattleUnit battleUnit) {
		if (follower != null) {
			if (battleUnit != null) {
				if (!follower.getUnit().isFollowing() && follower.getUnit().canFollow(battleUnit.getUnit())) {
					follower.getUnit().follow(battleUnit.getUnit());
				}
			}
		}
	}
	
	private void trainWeapon(UnitType unitType) {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(unitType);
		if (battleUnitGroup.getUnitCount() > 0) {
			for (int unitId : battleUnitGroup.battleUnits.keySet()) {
				WeaponTrainable battleUnit = (WeaponTrainable) battleUnitGroup.battleUnits.get(unitId);
				battleUnit.train();
			}
		}
	}
}
