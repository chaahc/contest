import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class BattleManager {
	private static BattleManager instance = new BattleManager();
	
	private CommandUtil commandUtil = new CommandUtil();
	
	public static BattleManager instance() {
		return instance;
	}
	
	public void totalAttack(Position position) {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot)
				.get(BattleUnitGroupManager.FRONT_GROUP);
		for (int i : battleUnitGroup.battleUnits.keySet()) {
			commandUtil.attackMove(battleUnitGroup.battleUnits.get(i).getUnit(), position);
		}
		battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon)
				.get(BattleUnitGroupManager.FRONT_GROUP);
		for (int i : battleUnitGroup.battleUnits.keySet()) {
			commandUtil.attackMove(battleUnitGroup.battleUnits.get(i).getUnit(), position);
		}
	}
	
	public void totalAttack(Unit unit) {
		Position target = unit.getPosition();
		this.totalAttack(target);
	}
	
	public void closestAttack() {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot)
				.get(BattleUnitGroupManager.FRONT_GROUP);
		for (int i : battleUnitGroup.battleUnits.keySet()) {
			Unit enemyUnit = commandUtil.GetClosestUnitTypeToTarget(battleUnitGroup.battleUnits.get(i).getUnit().getPosition());
			commandUtil.attackMove(battleUnitGroup.battleUnits.get(i).getUnit(), enemyUnit.getPosition());
		}
		battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon)
				.get(BattleUnitGroupManager.FRONT_GROUP);
		for (int i : battleUnitGroup.battleUnits.keySet()) {
			Unit enemyUnit = commandUtil.GetClosestUnitTypeToTarget(battleUnitGroup.battleUnits.get(i).getUnit().getPosition());
			commandUtil.attackMove(battleUnitGroup.battleUnits.get(i).getUnit(), enemyUnit.getPosition());
		}
	}
}
