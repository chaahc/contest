import java.util.Iterator;
import java.util.List;

import bwapi.Position;
import bwapi.Region;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class BattleOrder {
	public static final int TOTAL_RADIUS = 600;
	public static final int BASE_RADIUS = 500;
	public static final int CORSAIR_RADIUS = 100;
	
	public void execute() {
		changeBattleMode();
		
		formationAttack();
		detectEnemyAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self()).getPosition());
		detectEnemyAttack(InformationManager.Instance().getFirstExpansionLocation(MyBotModule.Broodwar.self()).getPosition());
		detectEnemyAttack(InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.self()).getPosition());
		observing();
		defenceExpansion();
		enemyAttack();
		onewayAttack();
		totalAttack();
		
//		highTemplarAttack();
//		dropAttack();
//		corsairAttack();
		arbiterAttack();
	}
	
	protected void changeBattleMode() {
		int enemyCount = 0;
		BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
		if (enemyBaseLocation != null) {
			for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(enemyBaseLocation.getPosition(), TOTAL_RADIUS)) {
				if (unit.getPlayer() == MyBotModule.Broodwar.enemy()) {
					System.out.println(unit.getID() + ", enemy : " + unit.getType() + ", count : " + enemyCount);
					enemyCount++;
				}
			}
		}
		if (enemyCount > 0) {
			if (InformationManager.Instance().selfPlayer.supplyUsed() > 350) { 
				BattleManager.instance().setBattleMode(BattleManager.BattleMode.ONEWAY_ATTACK);
			} else if (InformationManager.Instance().selfPlayer.supplyUsed() > 300) {
				BattleManager.instance().setBattleMode(BattleManager.BattleMode.TOTAL_ATTACK);
			} else {
				BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
			}
		} else {
			BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
		}
	}
	
	protected void formationAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		if (BattleManager.instance().getBattleMode() != BattleManager.BattleMode.ONEWAY_ATTACK) {
			BattleManager.instance().makeFormation(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Zealot, BattleGroupType.DEFENCE_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Dragoon, BattleGroupType.DEFENCE_GROUP);
		}
	}
	
	protected void enemyAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		int dragoonCount = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount();
		if (dragoonCount > 3 && BattleManager.instance().getBattleMode() == BattleManager.BattleMode.WAIT) {
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			if (enemyBaseLocation != null) {
				BaseLocation enemySecondExpansionLocation = InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.enemy());
				BattleUnit subLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.SUB_GROUP.getValue()).getLeader();
				if (subLeader.getUnit().isUnderAttack() && subLeader.getUnit().isAttacking()) {
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, subLeader.getUnit().getPosition(), BattleGroupType.FRONT_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, subLeader.getUnit().getPosition(), BattleGroupType.FRONT_GROUP);
				} else {
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemySecondExpansionLocation.getPosition(), BattleGroupType.FRONT_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemySecondExpansionLocation.getPosition(), BattleGroupType.FRONT_GROUP);
				}
				BattleUnit frontLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
				if (frontLeader.getUnit().isUnderAttack() && subLeader.getUnit().isAttacking()) {
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, frontLeader.getUnit().getPosition(), BattleGroupType.SUB_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, frontLeader.getUnit().getPosition(), BattleGroupType.SUB_GROUP);
				} else if (!this.centerExpansionAttack(BattleGroupType.SUB_GROUP)) {
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, ProtossBasicBuildPosition.mapInfo.get(ProtossBasicBuildPosition.CENTER).toPosition(), BattleGroupType.SUB_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, ProtossBasicBuildPosition.mapInfo.get(ProtossBasicBuildPosition.CENTER).toPosition(), BattleGroupType.SUB_GROUP);	
				}
			}
		}
	}
	
	protected boolean centerExpansionAttack(BattleGroupType battleGroupType) {
		BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
		if (enemyBaseLocation != null) {
			List<TilePosition> centerExpansionNearEnemy = ProtossBasicBuildPosition.Instance().getCenterExpansionNearEnemy(enemyBaseLocation.getTilePosition());
			Position centerExpansionBefore = centerExpansionNearEnemy.get(0).toPosition();
			Position centerExpansionAtfer = centerExpansionNearEnemy.get(1).toPosition();
			if (isEnemyInCenterExpansion(centerExpansionBefore)) {
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, centerExpansionBefore, battleGroupType);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, centerExpansionBefore, battleGroupType);
				return true;
			} else if (isEnemyInCenterExpansion(centerExpansionAtfer)) {
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, centerExpansionAtfer, battleGroupType);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, centerExpansionAtfer, battleGroupType);
				return true;
			}
		}
		return false;
	}
	
	protected boolean isEnemyInCenterExpansion(Position centerExpansion) {
		boolean isEnemyInCenterExpansion = false;
		for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(centerExpansion, CommandUtil.UNIT_RADIUS)) {
			if (unit.getPlayer() == MyBotModule.Broodwar.enemy()) {
				isEnemyInCenterExpansion = true;
				break;
			}
		}
		return isEnemyInCenterExpansion;
	}
	
	protected void detectEnemyAttack(Position target) {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		boolean isEnemyAttack = false;
		List<Unit> list = MyBotModule.Broodwar.getUnitsInRadius(target, BASE_RADIUS);
		Position enemyPosition = null;
		int enemyCount = 0;
		int selfCount = 0;
		for (Unit unit : list) {
			if (unit.getPlayer() == InformationManager.Instance().enemyPlayer) {
				BattleManager.instance().setBattleMode(BattleManager.BattleMode.DEFENCE);
				isEnemyAttack = true;
				enemyPosition = unit.getRegion().getPoint();
				enemyCount++;
			} else if (unit.getPlayer() == InformationManager.Instance().selfPlayer &&
					unit.getType().canAttack()) {
				selfCount++;
			}
		}
		if (isEnemyAttack) {
			BattleManager.instance().closestAttack(UnitType.Protoss_Zealot, BattleGroupType.DEFENCE_GROUP);
			BattleManager.instance().closestAttack(UnitType.Protoss_Dragoon, BattleGroupType.DEFENCE_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyPosition, BattleGroupType.SUB_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyPosition, BattleGroupType.SUB_GROUP);
			if (selfCount <= enemyCount) {
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyPosition, BattleGroupType.FRONT_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyPosition, BattleGroupType.FRONT_GROUP);
			}
		} else {
			this.changeBattleMode();
		}
	}
	
	protected void defenceExpansion() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		if (BattleManager.instance().getBattleMode() != BattleManager.BattleMode.DEFENCE) {
			BaseLocation secondExpantionLocation = InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.self());
			Chokepoint firstChokepoint = InformationManager.Instance().getFirstChokePoint(MyBotModule.Broodwar.self());
			if (BuildingUnitManager.instance().getBuildingUnitCount(UnitType.Protoss_Nexus) > 2) {
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, secondExpantionLocation.getPosition(), BattleGroupType.DEFENCE_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, secondExpantionLocation.getPosition(), BattleGroupType.DEFENCE_GROUP);
			} else {
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, firstChokepoint.getCenter(), BattleGroupType.DEFENCE_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, firstChokepoint.getCenter(), BattleGroupType.DEFENCE_GROUP);
			}
		}
	}
	
	protected void observing() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		BattleUnitGroup observerGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer);	
		BattleUnit observerLeader = observerGroup.getLeader();
		if (observerGroup.getUnitCount() > 0 && observerGroup.getUnitCount() < 5) {
			Iterator<Integer> iterator = observerGroup.battleUnits.keySet().iterator();
			BattleUnit observer = observerGroup.battleUnits.get(iterator.next());
			BattleUnit leader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
			this.unitFollow(observer, leader);
			if (iterator.hasNext()) {
				observer = observerGroup.battleUnits.get(iterator.next());
				leader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.SUB_GROUP.getValue()).getLeader();
				this.unitFollow(observer, leader);
			}
			if (iterator.hasNext()) {
				List<TilePosition> centerExpansionNearEnemy = ProtossBasicBuildPosition.Instance().getCenterExpansionNearEnemy(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()).getTilePosition());
				if (!centerExpansionNearEnemy.isEmpty()) {
					observer = observerGroup.battleUnits.get(iterator.next());
					CommandUtil.move(observer.getUnit(), centerExpansionNearEnemy.get(0).toPosition());
					if (iterator.hasNext()) {
						observer = observerGroup.battleUnits.get(iterator.next());
						CommandUtil.move(observer.getUnit(), centerExpansionNearEnemy.get(1).toPosition());
					}
				}
			}
		} else {
			if (observerLeader != null && observerLeader.getUnit().exists()) {
				for (String base : ProtossBasicBuildPosition.Instance().getScoutPositions().keySet()) {
					TilePosition tilePosition = ProtossBasicBuildPosition.Instance().getScoutPositions().get(base);
					if (observerLeader.getUnit().getDistance(tilePosition.toPosition()) > 50 && !observerLeader.getUnit().isMoving()) {
						observerLeader.getUnit().move(tilePosition.toPosition(), true);
					}
				}
			}
		}
	}
	
	protected void unitFollow(BattleUnit follower, Unit scoutUnit) {
		if (follower != null) {
			if (scoutUnit != null) {
				if (!follower.getUnit().isFollowing() && follower.getUnit().canFollow(scoutUnit)) {
					follower.getUnit().follow(scoutUnit);
				}
			}
		}
	}
	
	protected void unitFollow(BattleUnit follower, BattleUnit battleUnit) {
		if (follower != null) {
			if (battleUnit != null) {
				if (!follower.getUnit().isFollowing() && follower.getUnit().canFollow(battleUnit.getUnit())) {
					follower.getUnit().follow(battleUnit.getUnit());
				}
			}
		}
	}
	
	protected void onewayAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.ONEWAY_ATTACK) {
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			BattleManager.instance().onewayAttack(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP, enemyBaseLocation.getPosition());
			BattleManager.instance().onewayAttack(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP, enemyBaseLocation.getPosition());
			BattleManager.instance().onewayAttack(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP, enemyBaseLocation.getPosition());
			BattleManager.instance().onewayAttack(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP, enemyBaseLocation.getPosition());
		}
	}
	
	protected void totalAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.TOTAL_ATTACK) {
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			Position targetPosition = enemyBaseLocation.getRegion().getCenter();
//			if (targetPosition != null) {
//				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, targetPosition, BattleGroupType.FRONT_GROUP);
//				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, targetPosition, BattleGroupType.FRONT_GROUP);
//				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, targetPosition, BattleGroupType.SUB_GROUP);
//				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, targetPosition, BattleGroupType.SUB_GROUP);
//			} else {
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, targetPosition, BattleGroupType.FRONT_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, targetPosition, BattleGroupType.FRONT_GROUP);
				if (ScoutManager.Instance().getScoutUnit() != null && ScoutManager.Instance().getScoutUnit().exists()) {
					Region region = ScoutManager.Instance().getScoutUnit().getRegion();
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, new Position(region.getBoundsLeft(), region.getBoundsTop()), BattleGroupType.SUB_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, new Position(region.getBoundsRight(), region.getBoundsBottom()), BattleGroupType.SUB_GROUP);	
				} else {
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, targetPosition, BattleGroupType.SUB_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, targetPosition, BattleGroupType.SUB_GROUP);
				}
//			}
		}
	}
	
	protected void trainWeapon(UnitType unitType) {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(unitType);
		if (battleUnitGroup.getUnitCount() > 0) {
			for (int unitId : battleUnitGroup.battleUnits.keySet()) {
				WeaponTrainable battleUnit = (WeaponTrainable) battleUnitGroup.battleUnits.get(unitId);
				battleUnit.train();
			}
		}
	}
	
	
//	protected void highTemplarAttack() {
//		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
//			return;
//		}
//		
//		BattleUnitGroup highTemplarGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_High_Templar);
//		if (highTemplarGroup.getUnitCount() > 0) {
//			for (int unitId : highTemplarGroup.battleUnits.keySet()) {
//				HighTemplar highTemplar = (HighTemplar) highTemplarGroup.battleUnits.get(unitId);
//				boolean isEnemyInWeaponRange = false;
//				if (highTemplar.getUnit().exists()) {
//					for (Unit enemy : highTemplar.getUnit().getUnitsInRadius(WeaponType.Psionic_Storm.maxRange())) {
//						if (enemy.getPlayer() == MyBotModule.Broodwar.enemy() && enemy.exists() && !enemy.isUnderStorm()) {
//							isEnemyInWeaponRange = true;
//							if (enemy.getType().isWorker() || (enemy.getType().canAttack() &&
//									enemy.getType() != UnitType.Protoss_Zealot &&
//									enemy.getType() != UnitType.Zerg_Zergling &&
//									enemy.getType() != UnitType.Terran_Firebat)) {						
//								highTemplar.psionicStorm(enemy.getPosition());
//							}
//						}
//					}
//					if (!isEnemyInWeaponRange) {
//						BattleUnit leader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
//						CommandUtil.rightClick(highTemplar.getUnit(), leader.getUnit().getPosition());
//					}
//				}
//			}

//				if (highTemplar.getUnit().getEnergy() < 50) {
//					BattleManager.instance().addArchonCandidate(highTemplar);
//				}
			
//			while(BattleManager.instance().getArchonCandidatesCount() > 1) {
//				HighTemplar highTemplar = BattleManager.instance().removeArchonCandidate();
//				HighTemplar targetHighTemplar = BattleManager.instance().removeArchonCandidate();
//				highTemplar.archonWarp(targetHighTemplar.getUnit());
//			}
//		}
//	}
	
//	protected void dropAttack() {
//		trainWeapon(UnitType.Protoss_Reaver);
//		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
//			return;
//		}
//		
//		BattleUnitGroup shuttleGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Shuttle);
//		if (shuttleGroup.getUnitCount() > 0) {
//			for (int unitId : shuttleGroup.battleUnits.keySet()) {
//				BattleUnit shuttle = shuttleGroup.battleUnits.get(unitId);
//				if (shuttle.getUnit().getSpaceRemaining() != 0) {
//					for (Unit unit : shuttle.getUnit().getUnitsInRadius(BASE_RADIUS)) {
//						if (unit.getPlayer() == MyBotModule.Broodwar.self()) {
//							if ((unit.getType() == UnitType.Protoss_High_Templar && (unit.isUnderAttack() || unit.getEnergy() < 50)) ||
//								(unit.getType() == UnitType.Protoss_Reaver && (unit.isUnderAttack() || unit.getScarabCount() == 0))) {				
//								System.out.println(unit.getID() + ", hp : " + unit.getShields() + unit.getHitPoints() +", mp : " + unit.getEnergy());
//								shuttle.getUnit().load(unit);
//							} 
//						}
//					}
//				} else {
//					if (!shuttle.getUnit().getPosition().toTilePosition().equals(new TilePosition(60, 120))) {
//						System.out.println("target not reached, current position : " +shuttle.getUnit().getPosition().toTilePosition());
//	
//					}
//					System.out.println("shuttle current position : " +shuttle.getUnit().getPosition().toTilePosition());
//					if (shuttle.getUnit().getPosition().toTilePosition().equals(InformationManager.Instance().getFirstChokePoint(MyBotModule.Broodwar.self()).getCenter().toTilePosition())) {
//						System.out.println("target reached, current position : " + shuttle.getUnit().getPosition().toTilePosition());
//						shuttle.getUnit().unloadAll();
//						CommandUtil.move(shuttle.getUnit(), InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self()).getPosition());
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
//					}
//					
//				}
//			}
//		}
//	}
	
//	protected void corsairAttack() {
//		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
//			return;
//		}
//		
//		BattleUnitGroup corsairGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Corsair);
//		if (corsairGroup.getUnitCount() > 0) {
//			for (int unitId : corsairGroup.battleUnits.keySet()) {
//				Corsair corsair = (Corsair) corsairGroup.battleUnits.get(unitId);
//				if (corsair.getUnit().isUnderAttack()) {
//					for (Unit enemy : corsair.getUnit().getUnitsInRadius(CORSAIR_RADIUS)) {
//						if (enemy != null && !enemy.isUnderDisruptionWeb() && enemy.getPlayer() == MyBotModule.Broodwar.enemy()) {
//							if (enemy.getType() == UnitType.Protoss_Photon_Cannon ||
//									enemy.getType() == UnitType.Terran_Bunker ||
//									enemy.getType() == UnitType.Terran_Missile_Turret ||
//									enemy.getType() == UnitType.Zerg_Sunken_Colony ||
//									enemy.getType() == UnitType.Zerg_Spore_Colony) {
//								corsair.disruptionWeb(enemy);
//							}
//						}
//					}
//				} else {
//					BattleUnitGroup arbiterGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Arbiter);
//					BattleUnit arbiter = arbiterGroup.getLeader();
//					if (arbiter != null && arbiter.getUnit().exists() && arbiter.getUnit().getEnergy() > 150) {
//						//required move logic
//						boolean isAroundArbiter = false;
//						for (Unit unit : corsair.getUnit().getUnitsInRadius(CORSAIR_RADIUS)) {
//							if (unit.getType() == UnitType.Protoss_Arbiter) {
//								isAroundArbiter = true;
//								break;
//							}
//						}
//						if (!isAroundArbiter) {
//							corsair.getUnit().stop();
//						} else {
//							BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
//							CommandUtil.move(corsair.getUnit(), enemyBaseLocation.getPosition());
//						}
//					}
//				}
//			}
//		}
//	}
//	
//	protected void carrierAttack() {
//		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
//			return;
//		}
//		
//		trainWeapon(UnitType.Protoss_Carrier);
//	}
	
	protected void arbiterAttack() {
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
	
	protected void arbiterStatisFieldAttack(BattleUnit arbiter) {
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
	
	protected void arbiterRecallAttack() {
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
}
