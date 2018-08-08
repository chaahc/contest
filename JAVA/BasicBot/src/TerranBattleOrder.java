import java.util.List;

import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class TerranBattleOrder extends BattleOrder {
	public static final int TOTAL_RADIUS = 1000;
	public static final int BASE_RADIUS = 500;
	public static final int CORSAIR_RADIUS = 100;
	public static final int STUCK_RADIUS = 50;
	
	@Override
	public void execute() {
		if (BattleManager.instance().getBattleMode() != BattleManager.BattleMode.DEFENCE) {
			this.changeBattleMode();
		}
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			System.out.println("battle mode : " + BattleManager.instance().getBattleMode());
		}
		super.moveStuckDragoon();
		super.observing();
		super.formationAttack();
		super.detectEnemyInSelf();
		this.enemyExpansionAttack();
		super.onewayAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		super.totalAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		super.trainWeaponAttack(UnitType.Protoss_Carrier);
		super.arbiterAttack();
		super.darkTemplarAttack();
		super.highTemplarAttack();
		super.archonAttack();
	}
	
	@Override
	protected void changeBattleMode() {
		int enemyCommandCenterCount = InformationManager.Instance().getNumUnits(UnitType.Terran_Command_Center, MyBotModule.Broodwar.enemy());
//		int enemyCyberneticsCoreCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Cybernetics_Core, MyBotModule.Broodwar.enemy());
//		int enemyAssimilatorCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Assimilator, MyBotModule.Broodwar.enemy());
//		int enemyPhotonCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Photon_Cannon, MyBotModule.Broodwar.enemy());
//		int enemyGatewayCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Gateway, MyBotModule.Broodwar.enemy());
		int enemyTankCount = InformationManager.Instance().getNumUnits(UnitType.Terran_Siege_Tank_Tank_Mode, MyBotModule.Broodwar.enemy());
		int enemyGoliathCount = InformationManager.Instance().getNumUnits(UnitType.Terran_Goliath, MyBotModule.Broodwar.enemy());
		
		int selfZealotCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Zealot, MyBotModule.Broodwar.self());
		int selfDragoonCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Dragoon, MyBotModule.Broodwar.self());
		int selfCarrierCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Carrier, MyBotModule.Broodwar.self());
		
		if (MyBotModule.Broodwar.self().supplyTotal() > 350 &&
				MyBotModule.Broodwar.self().supplyUsed()+2 >= MyBotModule.Broodwar.self().supplyTotal()) {
			BattleManager.instance().setBattleMode(BattleManager.BattleMode.ELEMINATE);
		} else {
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			if (enemyBaseLocation != null) {
				if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.CARRIER_ATTACK) {
					BattleManager.instance().setBattleMode(BattleManager.BattleMode.ONEWAY_ATTACK);
					return;
				}
				int enemyCount = 0;
				Position target = null;
				for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(enemyBaseLocation.getPosition(), TOTAL_RADIUS)) {
					if (unit.getPlayer() == MyBotModule.Broodwar.enemy() &&
							unit.getType().isBuilding()) {
						enemyCount++;
						target = unit.getPosition();
					}
				}
				int gap = (selfZealotCount + selfDragoonCount + selfCarrierCount) - (enemyTankCount + enemyGoliathCount);
				if (gap > 0 || enemyCount > 0) {
					if (InformationManager.Instance().selfPlayer.supplyUsed() > 300) { 
						BattleManager.instance().setBattleMode(BattleManager.BattleMode.ONEWAY_ATTACK);
					} else if (InformationManager.Instance().selfPlayer.supplyUsed() > 200) {
						BattleManager.instance().setBattleMode(BattleManager.BattleMode.TOTAL_ATTACK);
					} else {
						BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
					}
				} else {
					BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
				}
			}
		}
	}
	
	@Override
	protected void enemyExpansionAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		int dragoonCount = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getUnitCount();
		if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.WAIT) {
			if (dragoonCount > 8) {
				BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
				if (enemyBaseLocation != null) {
					BaseLocation enemyFirstExpansionLocation = InformationManager.Instance().getFirstExpansionLocation(MyBotModule.Broodwar.enemy());
					BaseLocation enemySecondExpansionLocation = InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.enemy());
					BattleUnit subLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.SUB_GROUP.getValue()).getLeader();
					if (subLeader != null && subLeader.getUnit().isUnderAttack() && subLeader.getUnit().isAttacking()) {
						BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, subLeader.getUnit().getPosition(), BattleGroupType.FRONT_GROUP);
						BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, subLeader.getUnit().getPosition(), BattleGroupType.FRONT_GROUP);
					} else {
						BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemySecondExpansionLocation.getPosition(), BattleGroupType.FRONT_GROUP);
						BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemySecondExpansionLocation.getPosition(), BattleGroupType.FRONT_GROUP);
					}
					BattleUnit frontLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
					if (frontLeader != null && frontLeader.getUnit().isUnderAttack() && subLeader.getUnit().isAttacking()) {
						BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, frontLeader.getUnit().getPosition(), BattleGroupType.SUB_GROUP);
						BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, frontLeader.getUnit().getPosition(), BattleGroupType.SUB_GROUP);
					} else {
						BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyFirstExpansionLocation.getPosition(), BattleGroupType.SUB_GROUP);
						BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyFirstExpansionLocation.getPosition(), BattleGroupType.SUB_GROUP);	
					}
				}
			} else {
				Chokepoint selfSecondChokepoint = InformationManager.Instance().getSecondChokePoint(MyBotModule.Broodwar.self());
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, selfSecondChokepoint.getCenter(), BattleGroupType.FRONT_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, selfSecondChokepoint.getCenter(), BattleGroupType.FRONT_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, selfSecondChokepoint.getCenter(), BattleGroupType.SUB_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, selfSecondChokepoint.getCenter(), BattleGroupType.SUB_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, selfSecondChokepoint.getCenter(), BattleGroupType.DEFENCE_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, selfSecondChokepoint.getCenter(), BattleGroupType.DEFENCE_GROUP);
			}
		} 
	}
	
	@Override
	protected boolean detectEnemyAttack(Position target) {
		boolean isEnemyAttack = false;
		List<Unit> list = MyBotModule.Broodwar.getUnitsInRadius(target, BASE_RADIUS);
		Position enemyPosition = null;
		for (Unit unit : list) {
			if (unit.getPlayer() == InformationManager.Instance().enemyPlayer &&
				!unit.getType().isWorker()) {
				isEnemyAttack = true;
				enemyPosition = unit.getPosition();
				break;
			} 
		}
		if (isEnemyAttack) {
//			System.out.println("[detectEnemyAttack] enemy : " + enemyCount + ", self : " + selfCount);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyPosition, BattleGroupType.DEFENCE_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyPosition, BattleGroupType.DEFENCE_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyPosition, BattleGroupType.SUB_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyPosition, BattleGroupType.SUB_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyPosition, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyPosition, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().closestAttack(UnitType.Protoss_Zealot, BattleGroupType.DEFENCE_GROUP);
			BattleManager.instance().closestAttack(UnitType.Protoss_Dragoon, BattleGroupType.DEFENCE_GROUP);
			BattleManager.instance().closestAttack(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP);
			BattleManager.instance().closestAttack(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP);
			BattleManager.instance().closestAttack(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP);
			BattleManager.instance().closestAttack(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP);
			return true;
		} else {
			return false;
		}
	}
}