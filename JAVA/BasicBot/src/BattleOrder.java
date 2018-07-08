import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class BattleOrder {
	private static final int BASE_RADIUS = 500;
	
	public void execute() {
		changeBattleMode();
		
		formationAttack();
		defenseAttack();
		observing();
		
		executeTotalAttack();
		highTemplarAttack();
		dropAttack();
	}
	
	public void changeBattleMode() {
		if (InformationManager.Instance().selfPlayer.supplyUsed() > 300) {
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
			int dragoonCount = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount();
			if (dragoonCount > 11) {
				Chokepoint enemySecondChokePoint = InformationManager.Instance().getSecondChokePoint(MyBotModule.Broodwar.enemy());
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemySecondChokePoint.getCenter());
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemySecondChokePoint.getCenter());
			}
		}
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
					System.out.println(shuttle.getUnitId() + ",(spacious) loaded units : " + shuttle.getUnit().getLoadedUnits().size());
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
					System.out.println(shuttle.getUnitId() + ",(full) loaded units : " + shuttle.getUnit().getLoadedUnits().size());
					if (!shuttle.getUnit().getPosition().toTilePosition().equals(new TilePosition(60, 120))) {
						System.out.println("target not reached, current position : " +shuttle.getUnit().getPosition().toTilePosition());
						BattleManager.instance().addBattleSingleOrder(
								new ShuttleMove(shuttle.getUnit(), Arrays.asList(
										new MovePosition(new TilePosition(50, 120).toPosition()),
										new MovePosition(new TilePosition(60, 120).toPosition())
										)
									)
								);		
					}
					System.out.println("shuttle current position : " +shuttle.getUnit().getPosition().toTilePosition());
					if (shuttle.getUnit().getPosition().toTilePosition().equals(new TilePosition(60, 120))) {
						System.out.println("target reached, current position : " + shuttle.getUnit().getPosition().toTilePosition());
						shuttle.getUnit().unloadAll();
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
//		BattleUnitGroup corsairGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Corsair);
//		if (corsairGroup.getUnitCount() > 0) {
//			for (int unitId : corsairGroup.battleUnits.keySet()) {
//				Corsair corsair = (Corsair) corsairGroup.battleUnits.get(unitId);
//				Unit enemyUnit = CommandUtil.getClosestUnit(corsair.getUnit());
//				if (enemyUnit != null) {
//					if (enemyUnit.getType() == UnitType.Protoss_Photon_Cannon ||
//							enemyUnit.getType() == UnitType.Terran_Bunker ||
//							enemyUnit.getType() == UnitType.Terran_Missile_Turret ||
//							enemyUnit.getType() == UnitType.Zerg_Sunken_Colony ||
//							enemyUnit.getType() == UnitType.Zerg_Spore_Colony) {
//						corsair.disruptionWeb(enemyUnit);
//					}
//				}
//			}
//		}
	}
	
	public void carrierAttack() {
		trainWeapon(UnitType.Protoss_Carrier);
	}
	
	public void arbiterAttack() {
//		BattleUnitGroup arbiterGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Arbiter);
//		if (arbiterGroup.getUnitCount() > 0) {
//			Chokepoint enemySecondChokePoint = InformationManager.Instance().getSecondChokePoint(MyBotModule.Broodwar.enemy());
//			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
//			BaseLocation enemyFirstExpansionLocation = InformationManager.Instance().getFirstExpansionLocation(MyBotModule.Broodwar.enemy());
//			Chokepoint selfSecondChokePoint = InformationManager.Instance().getSecondChokePoint(MyBotModule.Broodwar.self());
//			Unit leader = arbiterGroup.getLeader();
//			Arbiter arbiter = (Arbiter) arbiterGroup.battleUnits.get(leader.getID());
//			if (arbiter.unit.getEnergy() > 150) {
//				arbiter.recall(selfSecondChokePoint.getCenter());
//			}
//		}
	}
	
	public void executeTotalAttack() {
		if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.TOTAL_ATTACK) {
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			BattleManager.instance().makeFormation(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().makeFormation(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyBaseLocation.getPosition());
			BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyBaseLocation.getPosition());
//			BattleManager.instance().totalAttack(enemyBaseLocation.getPosition());
		}
	}
	
	
	public void defenseAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		boolean isCloseToEnemy = false;
		List<Unit> list = MyBotModule.Broodwar.getUnitsInRadius(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self()).getPosition(), BASE_RADIUS);
		for (Unit unit : list) {
			if (unit.getPlayer() == InformationManager.Instance().enemyPlayer) {
				BattleManager.instance().setBattleMode(BattleManager.BattleMode.DEFENCE);
				BattleManager.instance().closestAttack(UnitType.Protoss_Zealot);
				BattleManager.instance().closestAttack(UnitType.Protoss_Dragoon);
				isCloseToEnemy = true;
				System.out.println("DEFENCE MODE : " + InformationManager.Instance().selfPlayer.getStartLocation().getDistance(unit.getPosition().toTilePosition()));
				break;
			}
		}
		if (!isCloseToEnemy) {
			BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
		}
	}
	
	public void observing() {
		BattleUnit zealot = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
		BattleUnit dragoon = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
		BattleUnitGroup observerGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer);
		if (observerGroup.getUnitCount() > 0) {
			Iterator<Integer> iterator = observerGroup.battleUnits.keySet().iterator();
			while (iterator.hasNext()) {
				BattleUnit observer = observerGroup.battleUnits.get(iterator.next());
				this.observerFollow(observer, zealot);
				if (iterator.hasNext()) {
					observer = observerGroup.battleUnits.get(iterator.next());
					this.observerFollow(observer, dragoon);
				}
			}
		}
	}
	
	private void observerFollow(BattleUnit observer, BattleUnit battleUnit) {
		if (observer != null) {
			if (battleUnit != null) {
				if (!observer.getUnit().isFollowing() && observer.getUnit().canFollow(battleUnit.getUnit())) {
					observer.getUnit().follow(battleUnit.getUnit());
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
