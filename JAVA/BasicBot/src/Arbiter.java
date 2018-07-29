

import bwapi.Position;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

public class Arbiter extends BattleUnit {
	public Arbiter(int unitId, Unit unit, UnitType unitType) {
		super(unitId, unit, unitType);
		// TODO Auto-generated constructor stub
	}
	
	public void recall(Unit target) {
		CommandUtil.useTech(super.unit, TechType.Recall, target);
	}
	
	public void stasisField(Position target) {
		CommandUtil.useTech(super.unit, TechType.Stasis_Field, target);
	}

}
