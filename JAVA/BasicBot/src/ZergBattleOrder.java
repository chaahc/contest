import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;

public class ZergBattleOrder extends BattleOrder {
	@Override
	public void execute() {
		if (BattleManager.instance().getBattleMode() != BattleManager.BattleMode.DEFENCE) {
			this.changeBattleMode();
		}
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			System.out.println("battle mode : " + BattleManager.instance().getBattleMode());
		}
		super.changeLeaderPeriodically();
		super.moveStuckDragoon();
		super.observing();
		super.formationAttack();
		super.detectEnemyInSelf();		
		super.enemyExpansionAttack();
		super.onewayAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		super.totalAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		
		super.highTemplarAttack();
		super.archonAttack();
	}
	
	@Override
	protected void changeBattleMode() {
		int enemyHydraliskCount = InformationManager.Instance().getNumUnits(UnitType.Zerg_Hydralisk, MyBotModule.Broodwar.enemy());
		int enemyMutaliskCount = InformationManager.Instance().getNumUnits(UnitType.Zerg_Mutalisk, MyBotModule.Broodwar.enemy());
		int enemyZerglingCount = InformationManager.Instance().getNumUnits(UnitType.Zerg_Zergling, MyBotModule.Broodwar.enemy());
		
		int selfZealotCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Zealot, MyBotModule.Broodwar.self());
		int selfDragoonCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Dragoon, MyBotModule.Broodwar.self());

		if (MyBotModule.Broodwar.self().supplyTotal() > 350 &&
				MyBotModule.Broodwar.self().supplyUsed()+2 >= MyBotModule.Broodwar.self().supplyTotal()) {
			BattleManager.instance().setBattleMode(BattleManager.BattleMode.ELEMINATE);
		} else {
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			if (enemyBaseLocation != null) {
				int enemyCount = 0;
				for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(enemyBaseLocation.getPosition(), TOTAL_RADIUS)) {
					if (unit.getPlayer() == MyBotModule.Broodwar.enemy() &&
							unit.getType().isBuilding()) {
						enemyCount++;
					}
				}
				int selfUnitCount = selfZealotCount + selfDragoonCount;
				int gap = selfUnitCount - (enemyHydraliskCount + enemyMutaliskCount + enemyZerglingCount);
				if (gap > 0 || enemyCount > 0) {
					if (InformationManager.Instance().selfPlayer.supplyUsed() > 300) { 
						BattleManager.instance().setBattleMode(BattleManager.BattleMode.ONEWAY_ATTACK);
					} else if (InformationManager.Instance().selfPlayer.supplyUsed() > 200 || selfUnitCount > 15) {
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
	
//	@Override
//	protected boolean detectEnemyAttack(Position target) {
//		boolean isEnemyAttack = false;
//		List<Unit> list = MyBotModule.Broodwar.getUnitsInRadius(target, BASE_RADIUS);
//		Position enemyPosition = null;
//		int enemyCount = 0;
//		for (Unit unit : list) {
//			if (unit.getPlayer() == InformationManager.Instance().enemyPlayer) {
//				BattleManager.instance().setBattleMode(BattleManager.BattleMode.DEFENCE);
//				isEnemyAttack = true;
//				enemyPosition = unit.getRegion().getPoint();
//				enemyCount++;
//			}
//		}
//		if (isEnemyAttack) {
//			BattleManager.instance().closestAttack(UnitType.Protoss_Zealot, BattleGroupType.DEFENCE_GROUP);
//			BattleManager.instance().closestAttack(UnitType.Protoss_Dragoon, BattleGroupType.DEFENCE_GROUP);
//			BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyPosition, BattleGroupType.SUB_GROUP);
//			BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyPosition, BattleGroupType.SUB_GROUP);
//			int subZealotCount = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.SUB_GROUP.getValue()).getUnitCount();
//			int subDragoonCount = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.SUB_GROUP.getValue()).getUnitCount();
//			if (subZealotCount + subDragoonCount < enemyCount) {
//				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyPosition, BattleGroupType.FRONT_GROUP);
//				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyPosition, BattleGroupType.FRONT_GROUP);
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
}
