import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

public class ZergBattleOrder extends BattleOrder {
	@Override
	public void execute() {
		super.changeBattleMode();
		
		super.formationAttack();
		super.detectEnemyAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self()).getPosition());
		super.detectEnemyAttack(InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.self()).getPosition());
		super.detectEnemyAttack(InformationManager.Instance().getFirstChokePoint(MyBotModule.Broodwar.self()).getCenter());
		super.observing();
		super.defenceExpansion();
		super.executeTotalAttack();
		
		highTemplarAttack();
		archonAttack();
		corsairAttack();
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
					CommandUtil.patrolMove(archon.getUnit(), zealot.getUnit().getPosition());
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
					BattleUnit zealot = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.DEFENCE_GROUP.getValue()).getLeader();
					if (zealot != null && zealot.getUnit().exists()) {
						super.unitFollow(corsair, zealot);
					}
				}
			}
		}
	}
}
