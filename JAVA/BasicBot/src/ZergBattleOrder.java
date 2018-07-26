import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

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
		super.enemyAttack();
		super.onewayAttack();
		super.totalAttack();
		super.moveStuckUnit();
		
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
								int enemyUnitGapCount = enemy.getUnitsInRadius(ENEMY_RADIUS).size();
								if (maxEnemyCount <= enemyUnitGapCount) {
									maxEnemyCount= enemyUnitGapCount;
									targetEnemy = enemy;
								}
							}
						}
					}
					if (maxEnemyCount == 0) {
						BattleUnit leader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
						super.unitFollow(highTemplar, leader);
//						CommandUtil.rightClick(highTemplar.getUnit(), leader.getUnit().getPosition());
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
