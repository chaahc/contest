import java.util.List;

import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class FastCarrierBattleOrder extends BattleOrder {
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
		this.observing();
		super.formationAttack();
		super.detectEnemyInSelf();
		this.enemyExpansionAttack();
		super.onewayAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		super.totalAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		this.trainWeaponAttack(UnitType.Protoss_Carrier);
		super.highTemplarAttack();
		super.archonAttack();
	}
	
	@Override
	protected void observing() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		BattleUnitGroup observerGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer);	
		if (observerGroup.getUnitCount() > 0) {
			boolean needDragoonFollower = true;
			BattleUnit observerLeader = observerGroup.getLeader();
			for (int unitId : observerGroup.battleUnits.keySet()) {
				BattleUnit observer = observerGroup.battleUnits.get(unitId);
				if (observerLeader != null && observerLeader.getUnitId() == observer.getUnitId()) {
					BaseLocation enemySecondExpansionLocation = InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.self());
					observerLeader.move(enemySecondExpansionLocation.getPosition());
				} else if (needDragoonFollower){
					BattleUnit dragoonLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
					this.unitFollow(observer, dragoonLeader);
					needDragoonFollower = false;
				} else {
					for (String base : ProtossBasicBuildPosition.Instance().getScoutPositions().keySet()) {
						TilePosition tilePosition = ProtossBasicBuildPosition.Instance().getScoutPositions().get(base);
						if (observer.getUnit().getDistance(tilePosition.toPosition()) > 50 && !observer.getUnit().isMoving()) {
							observer.getUnit().move(tilePosition.toPosition(), true);
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void trainWeaponAttack(UnitType unitType) {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(unitType);
		if (battleUnitGroup.getUnitCount() > 0) {
			for (int unitId : battleUnitGroup.battleUnits.keySet()) {
				BattleUnit battleUnit = battleUnitGroup.battleUnits.get(unitId);
				
				BaseLocation enemyMainBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
				if (enemyMainBaseLocation != null) {
					TilePosition enemyMineralPosition = ProtossBasicBuildPosition.Instance().getEnemyMineralPosition(enemyMainBaseLocation.getTilePosition());
					if (enemyMineralPosition != null) {
						if (battleUnit.getUnit().getTilePosition().getDistance(enemyMineralPosition) < 5 &&
								BattleManager.instance().getBattleMode() != BattleManager.BattleMode.TOTAL_ATTACK) {
							boolean isMainBaseAlive = false;
							for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(battleUnit.getUnit().getPosition(), CommandUtil.UNIT_RADIUS)) {
								if (unit.getPlayer() == MyBotModule.Broodwar.enemy() && 
										(unit.getType() == UnitType.Terran_SCV ||
										unit.getType() == UnitType.Terran_Command_Center) &&
										unit.exists()) {
									System.out.println(unitId + "main base alive");
									isMainBaseAlive = true;
									break;
								}
							}
							
							if (isMainBaseAlive) {
								System.out.println(unitId + "hold");
								if (!battleUnit.getUnit().isHoldingPosition()) {
									battleUnit.getUnit().holdPosition();			
								}
							} else {
								System.out.println(unitId + "total attack");
								BattleManager.instance().setBattleMode(BattleManager.BattleMode.TOTAL_ATTACK);
							}
						} else {
							if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.DEFENCE || BattleManager.instance().getBattleMode() == BattleManager.BattleMode.WAIT) {
								System.out.println(unitId + "move to mineral");
								if (ProtossBasicBuildPosition.Instance().isEnemyOpposite(enemyMainBaseLocation.getTilePosition())) {
									System.out.println(unitId + "opposite");
									BaseLocation enemySecondExpansionLocation = InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.enemy());
									battleUnit.getUnit().move(enemySecondExpansionLocation.getPosition(), true);
									battleUnit.getUnit().move(enemyMineralPosition.toPosition(), true);
								} else {
									System.out.println(unitId + "close");
									CommandUtil.move(battleUnit.getUnit(), enemyMineralPosition.toPosition());
								}
							} else {
								System.out.println(unitId + "attack following dragoon");
								BattleUnitGroup dragoonGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue());
								BattleUnit leader = dragoonGroup.getLeader();
								if (leader != null) {
									battleUnit.getUnit().attack(leader.getUnit().getPosition());
								}
							}
						}
					} 
					((WeaponTrainable) battleUnit).train();
				}
			}
		} 
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
		if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.WAIT) {
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			if (enemyBaseLocation != null) {
				if (MyBotModule.Broodwar.getFrameCount() < 7200) {
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyBaseLocation.getPosition(), BattleGroupType.FRONT_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyBaseLocation.getPosition(), BattleGroupType.FRONT_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyBaseLocation.getPosition(), BattleGroupType.SUB_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyBaseLocation.getPosition(), BattleGroupType.SUB_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, enemyBaseLocation.getPosition(), BattleGroupType.DEFENCE_GROUP);
					BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, enemyBaseLocation.getPosition(), BattleGroupType.DEFENCE_GROUP);
				} else {
					BattleUnit subLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.SUB_GROUP.getValue()).getLeader();
					if (subLeader != null && subLeader.getUnit().isUnderAttack() && subLeader.getUnit().isAttacking()) {
						BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, subLeader.getUnit().getPosition(), BattleGroupType.FRONT_GROUP);
						BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, subLeader.getUnit().getPosition(), BattleGroupType.FRONT_GROUP);
					} else {
						BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, ProtossBasicBuildPosition.Instance().getEnemyDefencePosition(enemyBaseLocation.getTilePosition()).toPosition(), BattleGroupType.FRONT_GROUP);
						BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, ProtossBasicBuildPosition.Instance().getEnemyDefencePosition(enemyBaseLocation.getTilePosition()).toPosition(), BattleGroupType.FRONT_GROUP);
					}
					BattleUnit frontLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
					if (frontLeader != null && frontLeader.getUnit().isUnderAttack() && subLeader.getUnit().isAttacking()) {
						BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, frontLeader.getUnit().getPosition(), BattleGroupType.SUB_GROUP);
						BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, frontLeader.getUnit().getPosition(), BattleGroupType.SUB_GROUP);
					} else {
						BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, ProtossBasicBuildPosition.Instance().getEnemyDefencePosition(enemyBaseLocation.getTilePosition()).toPosition(), BattleGroupType.SUB_GROUP);
						BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, ProtossBasicBuildPosition.Instance().getEnemyDefencePosition(enemyBaseLocation.getTilePosition()).toPosition(), BattleGroupType.SUB_GROUP);
					}
					
					Chokepoint selfChokepoint = InformationManager.Instance().getSecondChokePoint(MyBotModule.Broodwar.self());					
					BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, selfChokepoint.getCenter(), BattleGroupType.DEFENCE_GROUP);

					BattleUnitGroup observerGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer);
					BattleUnit observerLeader = observerGroup.getLeader();
					if (observerLeader != null) {
						BaseLocation secondExpansionLocation = InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.self());
						BattleUnit defenceLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.DEFENCE_GROUP.getValue()).getLeader();
						CommandUtil.patrolMove(defenceLeader.getUnit(), secondExpansionLocation.getPosition());
					} else {
						BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, selfChokepoint.getCenter(), BattleGroupType.DEFENCE_GROUP);	
					}
				}
			} else {
				Chokepoint selfChokepoint = InformationManager.Instance().getSecondChokePoint(MyBotModule.Broodwar.self());
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, selfChokepoint.getCenter(), BattleGroupType.FRONT_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, selfChokepoint.getCenter(), BattleGroupType.FRONT_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, selfChokepoint.getCenter(), BattleGroupType.SUB_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, selfChokepoint.getCenter(), BattleGroupType.SUB_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Zealot, selfChokepoint.getCenter(), BattleGroupType.DEFENCE_GROUP);
				BattleManager.instance().leaderAttack(UnitType.Protoss_Dragoon, selfChokepoint.getCenter(), BattleGroupType.DEFENCE_GROUP);
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