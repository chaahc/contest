import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import bwapi.Position;
import bwapi.Region;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class BattleManager {
	private static BattleManager instance = new BattleManager();

	public static BattleManager instance() {
		return instance;
	}
	private CommandUtil commandUtil = new CommandUtil();
	
	private static Map<UnitType, Integer> unitScore = new HashMap<UnitType, Integer>();
	
	static {
		unitScore.put(UnitType.Protoss_Zealot, 2);
		unitScore.put(UnitType.Protoss_Dragoon, 4);
		unitScore.put(UnitType.Protoss_High_Templar, 3);
		unitScore.put(UnitType.Protoss_Dark_Templar, 3);
		unitScore.put(UnitType.Protoss_Archon, 6);
		unitScore.put(UnitType.Protoss_Carrier, 8);
		unitScore.put(UnitType.Protoss_Reaver, 6);
		unitScore.put(UnitType.Protoss_Photon_Cannon, 4);
		unitScore.put(UnitType.Terran_Marine, 1);
		unitScore.put(UnitType.Terran_Firebat, 2);
		unitScore.put(UnitType.Terran_Medic, 2);
		unitScore.put(UnitType.Terran_Vulture, 2);
		unitScore.put(UnitType.Terran_Siege_Tank_Tank_Mode, 4);
		unitScore.put(UnitType.Terran_Siege_Tank_Siege_Mode, 6);
		unitScore.put(UnitType.Terran_Goliath, 3);
		unitScore.put(UnitType.Terran_Wraith, 2);
		unitScore.put(UnitType.Terran_Battlecruiser, 8);
		unitScore.put(UnitType.Terran_Bunker, 6);
		unitScore.put(UnitType.Zerg_Zergling, 1);
		unitScore.put(UnitType.Zerg_Hydralisk, 2);
		unitScore.put(UnitType.Zerg_Mutalisk, 4);
		unitScore.put(UnitType.Zerg_Lurker, 4);
		unitScore.put(UnitType.Zerg_Ultralisk, 6);
		unitScore.put(UnitType.Zerg_Guardian, 8);
		unitScore.put(UnitType.Zerg_Sunken_Colony, 4);
	}
	
	private BattleMode battleMode = BattleMode.WAIT;
	
	private static Queue<HighTemplar> archonCandidates = new LinkedList<HighTemplar>();
	
	private static Map<Integer, BattleSingleOrder> battleSingleOrderMap = new HashMap<Integer, BattleSingleOrder>();
	
	public void totalAttack(Unit unit) {
		Position target = unit.getPosition();
		this.totalAttack(target);
	}
	
	public void totalAttack(Position position) {
		BattleUnitGroup archonGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Archon);
		for (int unitId : archonGroup.battleUnits.keySet()) {
			Unit archon = archonGroup.battleUnits.get(unitId).getUnit();
			Unit enemy = CommandUtil.getClosestUnit(archon);
			CommandUtil.patrolMove(archon, enemy.getPosition());
		}
		BattleUnitGroup reaverGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Reaver);
		for (int unitId : reaverGroup.battleUnits.keySet()) {
			Unit reaver = reaverGroup.battleUnits.get(unitId).getUnit();
			Unit enemy = CommandUtil.getClosestUnit(reaver);
			CommandUtil.patrolMove(reaver, enemy.getPosition());
		}
		BattleUnitGroup corsairGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Corsair);
		for (int unitId : corsairGroup.battleUnits.keySet()) {
			Unit corsair = corsairGroup.battleUnits.get(unitId).getUnit();
			Unit enemy = CommandUtil.getClosestUnit(corsair);
			CommandUtil.patrolMove(corsair, enemy.getPosition());
		}
		BattleUnitGroup carrierGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Carrier);
		for (int unitId : carrierGroup.battleUnits.keySet()) {
			Unit carrier = carrierGroup.battleUnits.get(unitId).getUnit();
			Unit enemy = CommandUtil.getClosestUnit(carrier);
			CommandUtil.patrolMove(carrier, enemy.getPosition());
		}
	}
	
	public void closestAttack(UnitType unitType) {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(unitType)
				.get(BattleGroupType.FRONT_GROUP.getValue());
		for (int unitId : battleUnitGroup.battleUnits.keySet()) {
			Unit enemyUnit = CommandUtil.getClosestUnit(battleUnitGroup.battleUnits.get(unitId).getUnit());
			if (enemyUnit != null) {
				commandUtil.attackMove(battleUnitGroup.battleUnits.get(unitId).getUnit(), enemyUnit.getPosition());
			}
		}
	}
	
	public void leaderAttack(UnitType unitType, Position position) {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(unitType).get(BattleGroupType.FRONT_GROUP.getValue());
		BattleUnit leader = battleUnitGroup.getLeader();
		
		if (shouldRetreat(leader.getUnit())) {
			BaseLocation selfFirstExpansionLocation = InformationManager.Instance().getFirstExpansionLocation(MyBotModule.Broodwar.self());
			CommandUtil.rightClick(leader.getUnit(), selfFirstExpansionLocation.getPosition());
		} else {
			commandUtil.attackMove(leader.getUnit(), position);
		}
	}
	
	public static boolean shouldRetreat(Unit battleUnit) {
		int enemyUnitScore = 0;
		int selfUnitScore = 0;
		boolean isEnemyUnitInvisible = false;
		for (Unit unit : battleUnit.getRegion().getUnits()) {
			if (unit.getType() == UnitType.Protoss_Dark_Templar || 
					unit.getType() == UnitType.Zerg_Lurker) {
				if (unit.isDetected()) {
					isEnemyUnitInvisible = false;
				} else {
					isEnemyUnitInvisible = true;
				}
			}
			Integer score = unitScore.get(unit.getType());
			if (score != null) {
				if (unit.getPlayer() == MyBotModule.Broodwar.enemy()) {
					enemyUnitScore += score;
				} else if (unit.getPlayer() == MyBotModule.Broodwar.self()) {
					selfUnitScore += score;
				}
			}
		}
		if (enemyUnitScore > selfUnitScore || isEnemyUnitInvisible) {
			return true;
		} else {
			return false;
		}
	}
	
	public void makeFormation(UnitType unitType, BattleGroupType groupType) {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(unitType).get(groupType.getValue());
		BattleUnit leader = battleUnitGroup.getLeader();
		if (leader != null) {
			leader = changeReader(leader, battleUnitGroup);
			Iterator<Integer> unitIds = battleUnitGroup.battleUnits.keySet().iterator();
			Region leaderRegion = leader.getUnit().getRegion();
			Position leftTop = null;
			Position rightTop = null;
			Position leftBottom = null;
			Position rightBottom = null;
			Position top = null;
			Position left = null;
			Position right = null;
			Position bottom = null;	
			if (unitType == UnitType.Protoss_Zealot) {
				leftTop = new Position(leader.getUnit().getLeft()-40, leader.getUnit().getTop()-40);
				rightTop = new Position(leader.getUnit().getRight()+40, leader.getUnit().getTop()-40);
				leftBottom = new Position(leader.getUnit().getLeft()-40, leader.getUnit().getBottom()+40);
				rightBottom = new Position(leader.getUnit().getRight()+40, leader.getUnit().getBottom()+40);
				top = new Position(leader.getUnit().getX(), leader.getUnit().getTop()-40);
				left = new Position(leader.getUnit().getLeft()-40, leader.getUnit().getY());
				right = new Position(leader.getUnit().getRight()+40, leader.getUnit().getY());
				bottom = new Position(leader.getUnit().getX(), leader.getUnit().getBottom()+40);	
			} else if (unitType == UnitType.Protoss_Dragoon){
				leftTop = new Position(leaderRegion.getBoundsLeft(), leaderRegion.getBoundsTop());
				rightTop = new Position(leaderRegion.getBoundsRight(), leaderRegion.getBoundsTop());
				leftBottom = new Position(leaderRegion.getBoundsLeft(), leaderRegion.getBoundsBottom());
				rightBottom = new Position(leaderRegion.getBoundsRight(), leaderRegion.getBoundsBottom());
				top = new Position(leaderRegion.getX(), leaderRegion.getBoundsTop());
				left = new Position(leaderRegion.getBoundsLeft(), leaderRegion.getY());
				right = new Position(leaderRegion.getBoundsRight(), leaderRegion.getY());
				bottom = new Position(leaderRegion.getX(), leaderRegion.getBoundsBottom());	
			}
			
			while (unitIds.hasNext()) {
				if (unitIds.hasNext()) {
					formationMove(unitIds.next(), leader, battleUnitGroup, leftTop);
				}
				if (unitIds.hasNext()) {
					formationMove(unitIds.next(), leader, battleUnitGroup, rightTop);
				}
				if (unitIds.hasNext()) {
					formationMove(unitIds.next(), leader, battleUnitGroup, leftBottom);
				}
				if (unitIds.hasNext()) {
					formationMove(unitIds.next(), leader, battleUnitGroup, rightBottom);
				}
				if (unitIds.hasNext()) {
					formationMove(unitIds.next(), leader, battleUnitGroup, top);
				}
				if (unitIds.hasNext()) {
					formationMove(unitIds.next(), leader, battleUnitGroup, left);
				}
				if (unitIds.hasNext()) {
					formationMove(unitIds.next(), leader, battleUnitGroup, right);
				}
				if (unitIds.hasNext()) {
					formationMove(unitIds.next(), leader, battleUnitGroup, bottom);
				}
			}
		}
	}
	
	public static BattleUnit changeReader(BattleUnit leader, BattleUnitGroup battleUnitGroup) {
		if (!leader.getUnit().exists()) {
			Iterator<Integer> iterator = battleUnitGroup.battleUnits.keySet().iterator();
			while (iterator.hasNext()) {
				BattleUnit battleUnit = battleUnitGroup.battleUnits.get(iterator.next());
				if (battleUnit.getUnit().exists()) {
					battleUnitGroup.setLeader(battleUnit);
					return battleUnit;
				}
			}
		}
		return leader;
	}
	
	private void formationMove(int unitId, BattleUnit leader, BattleUnitGroup battleUnitGroup, Position position) {
		if (leader.getUnitId() == unitId) {
			return;
		}
		BattleUnit battleUnit = battleUnitGroup.battleUnits.get(unitId);
		if (battleUnit.getUnit().exists()) {
			if (battleUnit.getUnit().isUnderAttack() && battleUnit.getUnit().getShields() + battleUnit.getUnit().getHitPoints() < 100) {
				Chokepoint selfFirstChokepoint = InformationManager.Instance().getFirstChokePoint(MyBotModule.Broodwar.self());
				//TODO choose better one
//				CommandUtil.rightClick(battleUnit.getUnit(), selfFirstChokepoint.getCenter());
				battleUnit.getUnit().move(selfFirstChokepoint.getCenter());
			} else {
				CommandUtil.patrolMove(battleUnit.getUnit(), position);
			}
		}
	}
	
	public BattleMode getBattleMode() {
		return this.battleMode;
	}
	
	public void setBattleMode(BattleMode battleMode) {
		this.battleMode = battleMode;
	}
	
	public enum BattleMode {
		WAIT, DEFENCE, TOTAL_ATTACK, RECALL_ATTACK, DROP_ATTACK;
	}
	
	public int getArchonCandidatesCount() {
		return archonCandidates.size();
	}
	
	public void addArchonCandidate(HighTemplar highTemplar) {
		archonCandidates.add(highTemplar);
	}
	
	public HighTemplar removeArchonCandidate() {
		return archonCandidates.remove();
	}
	
	public void addBattleSingleOrder(BattleSingleOrder battleSingleOrder) {
		if (!battleSingleOrderMap.containsKey(battleSingleOrder.getUnitId())) {
			battleSingleOrderMap.put(battleSingleOrder.getUnitId(), battleSingleOrder);
		}
	}
	
	public void removeBattleSingleOrder(int unitId) {
		battleSingleOrderMap.remove(unitId);
	}
	
	public void onExecuteBattleSingleOrder() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		Iterator<Integer> iterator = battleSingleOrderMap.keySet().iterator();
		while (iterator.hasNext()) {
			BattleSingleOrder battleSingleOrder = battleSingleOrderMap.get(iterator.next());
			battleSingleOrder.execute();
		}
	}
}
