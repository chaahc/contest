import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;

public class TerranBattleOrder extends BattleOrder {
	@Override
	public void execute() {
		this.changeBattleMode();
		
		super.formationAttack();
		super.detectEnemyInSelf();
		super.observing();
		this.enemyExpansionAttack();
		super.onewayAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		super.totalAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		super.moveStuckDragoon();
		super.arbiterAttack();
	}
	
	@Override
	protected void changeBattleMode() {
		System.out.println("battle mode : " + BattleManager.instance().getBattleMode());
		if (BattleManager.instance().getBattleMode() != BattleManager.BattleMode.DEFENCE) {
			int enemyCount = 0;
			int siegeCount = 0;
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			if (enemyBaseLocation != null) {
				BattleUnit leader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
				if (leader != null) {
					for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(leader.getUnit().getPosition(), TOTAL_RADIUS)) {
						if (unit.getPlayer() == MyBotModule.Broodwar.enemy()) {
							if (unit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode) {
								siegeCount++;
							}
		//					System.out.println(unit.getID() + ", enemy : " + unit.getType() + ", count : " + enemyCount);
							enemyCount++;
						}
					}
					if (enemyCount > 0) {
						if (InformationManager.Instance().selfPlayer.supplyUsed() > 350) { 
							BattleManager.instance().setBattleMode(BattleManager.BattleMode.ONEWAY_ATTACK);
						} else if (InformationManager.Instance().selfPlayer.supplyUsed() > 300) {
							if (siegeCount > 3) {
								BattleManager.instance().setBattleMode(BattleManager.BattleMode.ONEWAY_ATTACK);
							} else {
								BattleManager.instance().setBattleMode(BattleManager.BattleMode.TOTAL_ATTACK);
							}
						} else {
							if (siegeCount > 3) {
								BattleManager.instance().setBattleMode(BattleManager.BattleMode.ONEWAY_ATTACK);
							} else {
								BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
							}
						}
					} else {
						leader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
						if (leader != null && leader.getUnit().getDistance(enemyBaseLocation.getPosition()) < 50) {
//							System.out.println("in enemy base");
							BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
						}
					}
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
		if (dragoonCount > 3 && BattleManager.instance().getBattleMode() == BattleManager.BattleMode.WAIT) {
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			if (enemyBaseLocation != null) {
				BattleUnit subLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.SUB_GROUP.getValue()).getLeader();
				if (subLeader != null && subLeader.getUnit().isUnderAttack() && subLeader.getUnit().isAttacking()) {
//					System.out.println("[enemyExpantionAttack] sub dangerous");
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, subLeader.getUnit().getPosition(), BattleGroupType.FRONT_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, subLeader.getUnit().getPosition(), BattleGroupType.FRONT_GROUP);
				} else if (!super.centerExpansionAttack(BattleGroupType.FRONT_GROUP)){
//					System.out.println("[enemyExpantionAttack] front enemy second");
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, ProtossBasicBuildPosition.mapInfo.get(ProtossBasicBuildPosition.CENTER).toPosition(), BattleGroupType.FRONT_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, ProtossBasicBuildPosition.mapInfo.get(ProtossBasicBuildPosition.CENTER).toPosition(), BattleGroupType.FRONT_GROUP);
				}
				BattleUnit frontLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
				if (frontLeader != null && frontLeader.getUnit().isUnderAttack() && subLeader.getUnit().isAttacking()) {
//					System.out.println("[enemyExpantionAttack] front dangerous");
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, frontLeader.getUnit().getPosition(), BattleGroupType.SUB_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, frontLeader.getUnit().getPosition(), BattleGroupType.SUB_GROUP);
				} else if (!super.centerExpansionAttack(BattleGroupType.SUB_GROUP)) {
//					System.out.println("[enemyExpantionAttack] sub center");
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, ProtossBasicBuildPosition.mapInfo.get(ProtossBasicBuildPosition.CENTER).toPosition(), BattleGroupType.SUB_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, ProtossBasicBuildPosition.mapInfo.get(ProtossBasicBuildPosition.CENTER).toPosition(), BattleGroupType.SUB_GROUP);	
				}
			}
		}
	}
}