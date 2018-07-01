

import bwapi.Position;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

public class HighTemplar extends BattleUnit {
	public HighTemplar(int unitId, Unit unit, UnitType unitType) {
		super(unitId, unit, unitType);
		// TODO Auto-generated constructor stub
	}

	public void psionicStorm(Position target) {
		// TODO Auto-generated method stub
		CommandUtil.useTech(super.unit, TechType.Psionic_Storm, target);
	}
	
	public void hallucination(Unit target) {
		CommandUtil.useTech(super.unit, TechType.Hallucination, target);
	}
	
	public void archonWarp(Unit target) {
		if (super.unit.canUseTech(TechType.Archon_Warp, target)) {
			super.unit.useTech(TechType.Archon_Warp, target);
			BattleUnitGroupManager.instance().removeUnit(super.unit);
			BattleUnitGroupManager.instance().removeUnit(target);
		}
	}
	
	public void follow(Unit unit) {
		super.unit.follow(unit);
	}
}
