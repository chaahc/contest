import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import bwapi.Position;
import bwapi.Region;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class BattleManager {
	private static final int UNIT_GAP = 80;
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
	
	public void closestAttack(UnitType unitType, BattleGroupType battleGroupType) {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(unitType)
				.get(battleGroupType.getValue());
		for (int unitId : battleUnitGroup.battleUnits.keySet()) {
			Unit enemyUnit = CommandUtil.getClosestUnit(battleUnitGroup.battleUnits.get(unitId).getUnit());
			if (enemyUnit != null) {
				commandUtil.attackMove(battleUnitGroup.battleUnits.get(unitId).getUnit(), enemyUnit.getPosition());
			}
		}
	}
	
	public void onewayAttack(UnitType unitType, BattleGroupType battleGroupType, Position position) {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(unitType)
				.get(battleGroupType.getValue());
		for (int unitId : battleUnitGroup.battleUnits.keySet()) {
			commandUtil.attackMove(battleUnitGroup.battleUnits.get(unitId).getUnit(), position);
		}
	}
	
	public void leaderAttack(UnitType unitType, Position position, BattleGroupType battleGroupType) {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(unitType).get(battleGroupType.getValue());
		BattleUnit leader = battleUnitGroup.getLeader();
		
		if (leader != null) {
			if (shouldRetreat(leader.getUnit())) {
				BaseLocation selfFirstExpansionLocation = InformationManager.Instance().getFirstExpansionLocation(MyBotModule.Broodwar.self());
				CommandUtil.rightClick(leader.getUnit(), selfFirstExpansionLocation.getPosition());
			} else {
				if (battleGroupType == BattleGroupType.DEFENCE_GROUP) {
					CommandUtil.patrolMove(leader.getUnit(), position);
				} else {
					if (unitType == UnitType.Protoss_Zealot) {
						battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(battleGroupType.getValue());
						BattleUnit dragoon = battleUnitGroup.getLeader();
						if (dragoon != null && dragoon.getUnit().exists() && ((dragoon.getUnit().isUnderAttack() && dragoon.getUnit().isAttacking()) || dragoon.getUnit().getDistance(leader.getUnit()) > 10)) {
							commandUtil.attackMove(leader.getUnit(), dragoon.getUnit().getRegion().getCenter());
						} else {
							commandUtil.attackMove(leader.getUnit(), position);
						}
					} else if (unitType == UnitType.Protoss_Dragoon) {
						commandUtil.attackMove(leader.getUnit(), position);
					}
				}
			}
		}
	}
	
	public static boolean shouldRetreat(Unit battleUnit) {
		int enemyUnitScore = 0;
		int selfUnitScore = 0;
		boolean isEnemyUnitInvisible = false;
		
		List<Unit> targetUnits = battleUnit.getUnitsInRadius(CommandUtil.UNIT_RADIUS);
		BaseLocation selfFirstExpansionLocation = InformationManager.Instance().getFirstExpansionLocation(MyBotModule.Broodwar.self());
		if (battleUnit.getPosition().toTilePosition().getDistance(selfFirstExpansionLocation.getTilePosition())<10) {
			System.out.println(battleUnit.getID() +"-"+battleUnit.getType() + ", retreat - skip score");
			return false;
		}
		for (Unit unit : targetUnits) {
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
			System.out.println(battleUnit.getID() +"-"+battleUnit.getType() + ", pull back : enemy - " + enemyUnitScore + ", self - " + selfUnitScore);
			return true;
		} else {
			System.out.println(battleUnit.getID() +"-"+battleUnit.getType() + ", attack : enemy - " + enemyUnitScore + ", self - " + selfUnitScore);
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
				leftTop = new Position(leader.getUnit().getLeft()-UNIT_GAP, leader.getUnit().getTop()-UNIT_GAP);
				rightTop = new Position(leader.getUnit().getRight()+UNIT_GAP, leader.getUnit().getTop()-UNIT_GAP);
				leftBottom = new Position(leader.getUnit().getLeft()-UNIT_GAP, leader.getUnit().getBottom()+UNIT_GAP);
				rightBottom = new Position(leader.getUnit().getRight()+UNIT_GAP, leader.getUnit().getBottom()+UNIT_GAP);
				top = new Position(leader.getUnit().getX(), leader.getUnit().getTop()-UNIT_GAP);
				left = new Position(leader.getUnit().getLeft()-UNIT_GAP, leader.getUnit().getY());
				right = new Position(leader.getUnit().getRight()+UNIT_GAP, leader.getUnit().getY());
				bottom = new Position(leader.getUnit().getX(), leader.getUnit().getBottom()+UNIT_GAP);	
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
			Chokepoint selfFirstChokepoint = InformationManager.Instance().getFirstChokePoint(MyBotModule.Broodwar.self());
			if (shouldRetreat(battleUnit.getUnit())) {
				CommandUtil.rightClick(battleUnit.getUnit(), selfFirstChokepoint.getCenter());
			} else {
				if (battleUnit.getUnit().isUnderAttack() && battleUnit.getUnit().getShields() + battleUnit.getUnit().getHitPoints() < 100) {
					battleUnit.getUnit().move(selfFirstChokepoint.getCenter());
				} else {
					Unit primaryAttackTarget = null;
					for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(battleUnit.getUnit().getPosition(), UnitType.Protoss_Dragoon.groundWeapon().maxRange())) {
						if (unit.getPlayer() == MyBotModule.Broodwar.enemy() &&
								(unit.getType() == UnitType.Terran_Siege_Tank_Tank_Mode ||
								unit.getType() == UnitType.Protoss_Carrier)) {
							primaryAttackTarget = unit;
							break;
						}
					}
					if (primaryAttackTarget != null) {
						System.out.println("primary attack target detected : " + primaryAttackTarget.getType());
						commandUtil.attackUnit(battleUnit.getUnit(), primaryAttackTarget);
					} else {
						CommandUtil.patrolMove(battleUnit.getUnit(), position);
					}
				}
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
		WAIT, DEFENCE, TOTAL_ATTACK, ONEWAY_ATTACK;
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
}
