import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;
import bwta.BaseLocation;

public class ZergBattleOrder extends BattleOrder {
	private static final int ENEMY_RADIUS = 50;
	
	@Override
	public void execute() {
		super.changeBattleMode();
		
		super.formationAttack();
		super.detectEnemyAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self()).getPosition());
		super.detectEnemyAttack(InformationManager.Instance().getFirstExpansionLocation(MyBotModule.Broodwar.self()).getPosition());
		super.detectEnemyAttack(InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.self()).getPosition());
		super.observing();
		super.defenceExpansion();
		super.enemyExpansionAttack();
		super.onewayAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		super.totalAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		super.moveStuckDragoon();
		
		highTemplarAttack();
		archonAttack();
	}
	
	public void highTemplarAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		BattleUnitGroup highTemplarGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_High_Templar);
		if (highTemplarGroup.getUnitCount() > 0) {
			for (int unitId : highTemplarGroup.battleUnits.keySet()) {
				HighTemplar highTemplar = (HighTemplar) highTemplarGroup.battleUnits.get(unitId);
				if (highTemplar.getUnit().exists()) {
					int maxEnemyCount = 0;
					Unit targetEnemy = null;
					for (Unit enemy : highTemplar.getUnit().getUnitsInRadius(WeaponType.Psionic_Storm.maxRange())) {
						if (enemy.getPlayer() == MyBotModule.Broodwar.enemy() && enemy.exists() && !enemy.isUnderStorm()) {
							if (enemy.getType().isWorker() || (enemy.getType().canAttack() &&
									enemy.getType() != UnitType.Protoss_Zealot &&
									enemy.getType() != UnitType.Zerg_Zergling &&
									enemy.getType() != UnitType.Terran_Firebat &&
									!enemy.getType().isBuilding())) {
								int enemyUnitCount = 0;
								int selfUnitCount = 0;
								for (Unit unit : enemy.getUnitsInRadius(ENEMY_RADIUS)) {
									if (unit.getPlayer() == MyBotModule.Broodwar.enemy()) {
										enemyUnitCount++;
									} else if (unit.getPlayer() == MyBotModule.Broodwar.self()) {
										selfUnitCount++;
									}
								}
								int gapCount = enemyUnitCount - selfUnitCount;
								if (gapCount > 0 && maxEnemyCount <= gapCount) {
									maxEnemyCount= gapCount;
									targetEnemy = enemy;
								}
							}
						}
					}
					if (maxEnemyCount == 0) {
						BattleUnit leader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
						BattleUnit highTemplarLeader = BattleManager.changeReader(highTemplarGroup.getLeader(), highTemplarGroup);
						if (highTemplar.getUnitId() == highTemplarLeader.getUnitId()) {
							BaseLocation firstExpansionLocation = InformationManager.Instance().getFirstExpansionLocation(MyBotModule.Broodwar.self());
							CommandUtil.move(highTemplarLeader.getUnit(), firstExpansionLocation.getPosition());
						} else {
							super.unitFollow(highTemplar, leader);
						}
					} else {
						if (highTemplar.getUnit().getEnergy() < 75 && BattleManager.shouldRetreat(highTemplar.getUnit())) {
							CommandUtil.move(highTemplar.getUnit(), ProtossBasicBuildPosition.mapInfo.get(ProtossBasicBuildPosition.START_BASE).toPosition());
						} else if (targetEnemy != null){						
							System.out.println("psionic target enemy count : " + maxEnemyCount);
							highTemplar.psionicStorm(targetEnemy.getPosition());
						}
					}
				}
				if (highTemplar.isSkillUsed() && highTemplar.getUnit().getEnergy() < 50) {
					BattleManager.instance().addArchonCandidate(highTemplar);
				}
			}
			
			while(BattleManager.instance().getArchonCandidatesCount() > 1) {
				HighTemplar highTemplar = BattleManager.instance().removeArchonCandidate();
				HighTemplar targetHighTemplar = BattleManager.instance().removeArchonCandidate();
				highTemplar.archonWarp(targetHighTemplar.getUnit());
			}
		}
	}
	
	public void archonAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		BattleUnitGroup archonGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Archon);
		if (archonGroup.getUnitCount() > 0) {
			for (int unitId : archonGroup.battleUnits.keySet()) {
				BattleUnit archon = archonGroup.battleUnits.get(unitId);
				BattleUnit zealot = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
				if (zealot != null && zealot.getUnit().exists()) {
					CommandUtil.patrolMove(archon.getUnit(), zealot.getUnit().getRegion().getCenter());
				}
			}
		}
	}
}
