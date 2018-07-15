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
	private static final int CORSAIR_RADIUS = 100;
	
	public void execute() {
		changeBattleMode();
		
		formationAttack();
		defenseAttack();
		observing();
		
		executeTotalAttack();
		highTemplarAttack();
		dropAttack();
		corsairAttack();
		arbiterAttack();
	}
	
	public void changeBattleMode() {
		if (InformationManager.Instance().selfPlayer.supplyUsed() > 200) {
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
						if (enemy != null && !enemy.isUnderDisruptionWeb()) {
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
		
		BattleUnitGroup arbiterGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Arbiter);
		if (arbiterGroup.getUnitCount() > 0) {
			BattleUnit arbiter = arbiterGroup.getLeader();
			if (!arbiter.getUnit().exists()) {
				arbiter = BattleManager.changeReader(arbiter, arbiterGroup);
			}
			BattleUnitGroup corsairGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Corsair);
			BattleUnit corsair = corsairGroup.getLeader();
			if (corsair != null && corsair.getUnit().exists() &&
					!arbiter.getUnit().isFollowing() && arbiter.getUnit().canFollow(corsair.getUnit())) {
				arbiter.getUnit().follow(corsair.getUnit());
			} else {
				corsair = BattleManager.changeReader(corsair, corsairGroup);
			}
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			BattleUnitGroup dragoonGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue());
			if (arbiter.getUnit().getEnergy() > 150 &&
					(arbiter.getUnit().getDistance(enemyBaseLocation.getPosition()) < 200 ||
					(arbiter.getUnit().isUnderAttack() &&
							arbiter.getUnit().getShields() + arbiter.getUnit().getHitPoints() < 100))
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
		Chokepoint selfFirstChokepoint = InformationManager.Instance().getFirstChokePoint(MyBotModule.Broodwar.self());
		boolean isCloseToEnemy = false;
		List<Unit> list = MyBotModule.Broodwar.getUnitsInRadius(selfFirstChokepoint.getCenter(), BASE_RADIUS);
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
		if (!isCloseToEnemy && BattleManager.instance().getBattleMode() != BattleManager.BattleMode.TOTAL_ATTACK) {
			BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
		}
	}
	
	public void observing() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		BattleUnit zealot = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
		BattleUnit dragoon = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
		BattleUnitGroup observerGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer);
		if (observerGroup.getUnitCount() > 0) {
			Iterator<Integer> iterator = observerGroup.battleUnits.keySet().iterator();
			while (iterator.hasNext()) {
				BattleUnit observer = observerGroup.battleUnits.get(iterator.next());
				if (observer.getUnitId() != observerGroup.getLeader().getUnitId()) {
					this.observerFollow(observer, zealot);
				}
				if (iterator.hasNext()) {
					observer = observerGroup.battleUnits.get(iterator.next());
					if (observer.getUnitId() != observerGroup.getLeader().getUnitId()) {
						this.observerFollow(observer, dragoon);
					}
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
