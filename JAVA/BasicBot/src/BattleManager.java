import java.util.LinkedList;
import java.util.Queue;

import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class BattleManager {
	private static BattleManager instance = new BattleManager();

	public static BattleManager instance() {
		return instance;
	}
	private CommandUtil commandUtil = new CommandUtil();
	
	private BattleMode battleMode = BattleMode.DEFENCE;

	private Queue<HighTemplar> archonCandidates = new LinkedList<HighTemplar>();
	
	public void totalMove(Position position) {
		BattleUnitGroup zealotGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot)
				.get(BattleUnitGroupManager.FRONT_GROUP);
		for (int i : zealotGroup.battleUnits.keySet()) {
			CommandUtil.move(zealotGroup.battleUnits.get(i).getUnit(), position);
		}
		BattleUnitGroup dragoonGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon)
				.get(BattleUnitGroupManager.FRONT_GROUP);
		for (int i : dragoonGroup.battleUnits.keySet()) {
			CommandUtil.move(dragoonGroup.battleUnits.get(i).getUnit(), position);
		}
		BattleUnitGroup archonGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Archon);
		for (int i : archonGroup.battleUnits.keySet()) {
			CommandUtil.move(archonGroup.battleUnits.get(i).getUnit(), position);
		}
		BattleUnitGroup reaverGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Reaver);
		for (int i : reaverGroup.battleUnits.keySet()) {
			CommandUtil.move(reaverGroup.battleUnits.get(i).getUnit(), position);
		}
		BattleUnitGroup corsairGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Corsair);
		for (int i : corsairGroup.battleUnits.keySet()) {
			CommandUtil.move(corsairGroup.battleUnits.get(i).getUnit(), position);
		}
		BattleUnitGroup carrierGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Carrier);
		for (int i : carrierGroup.battleUnits.keySet()) {
			CommandUtil.move(carrierGroup.battleUnits.get(i).getUnit(), position);
		}
	}
	
	public void totalAttack(Position position) {
		BattleUnitGroup zealotGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot)
				.get(BattleUnitGroupManager.FRONT_GROUP);
		for (int i : zealotGroup.battleUnits.keySet()) {
			commandUtil.attackMove(zealotGroup.battleUnits.get(i).getUnit(), position);
		}
		BattleUnitGroup dragoonGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon)
				.get(BattleUnitGroupManager.FRONT_GROUP);
		for (int i : dragoonGroup.battleUnits.keySet()) {
			commandUtil.attackMove(dragoonGroup.battleUnits.get(i).getUnit(), position);
		}
		BattleUnitGroup archonGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Archon);
		for (int i : archonGroup.battleUnits.keySet()) {
			commandUtil.attackMove(archonGroup.battleUnits.get(i).getUnit(), position);
		}
		BattleUnitGroup reaverGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Reaver);
		for (int i : reaverGroup.battleUnits.keySet()) {
			commandUtil.attackMove(reaverGroup.battleUnits.get(i).getUnit(), position);
		}
		BattleUnitGroup corsairGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Corsair);
		for (int i : corsairGroup.battleUnits.keySet()) {
			commandUtil.attackMove(corsairGroup.battleUnits.get(i).getUnit(), position);
		}
		BattleUnitGroup carrierGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Carrier);
		for (int i : carrierGroup.battleUnits.keySet()) {
			commandUtil.attackMove(carrierGroup.battleUnits.get(i).getUnit(), position);
		}
	}
	
	public void totalAttack(Unit unit) {
		Position target = unit.getPosition();
		this.totalAttack(target);
	}
	
	public void closestAttack() {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot)
				.get(BattleUnitGroupManager.FRONT_GROUP);
		for (int unitId : battleUnitGroup.battleUnits.keySet()) {
			Unit enemyUnit = commandUtil.getClosestUnit(battleUnitGroup.battleUnits.get(unitId).getUnit());
			if (enemyUnit != null) {
				commandUtil.attackMove(battleUnitGroup.battleUnits.get(unitId).getUnit(), enemyUnit.getPosition());
			}
		}
		battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon)
				.get(BattleUnitGroupManager.FRONT_GROUP);
		for (int unitId : battleUnitGroup.battleUnits.keySet()) {
			Unit enemyUnit = commandUtil.getClosestUnit(battleUnitGroup.battleUnits.get(unitId).getUnit());
			if (enemyUnit != null) {
				commandUtil.attackMove(battleUnitGroup.battleUnits.get(unitId).getUnit(), enemyUnit.getPosition());
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
		DEFENCE, TOTAL_ATTACK, WAIT;
	}
	
	public int getArchonCandidatesCount() {
		return this.archonCandidates.size();
	}
	
	public void addArchonCandidate(HighTemplar highTemplar) {
		this.archonCandidates.add(highTemplar);
	}
	
	public HighTemplar removeArchonCandidate() {
		return this.archonCandidates.remove();
	}
}
