

import bwapi.Position;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

public class Arbiter extends BattleUnit {

	public Arbiter(int unitId, Unit unit, UnitType unitType) {
		super(unitId, unit, unitType);
		// TODO Auto-generated constructor stub
	}
	
	public void recall(Position target) {
		super.unit.useTech(TechType.Recall, target);
		
	}
	
	public void stasisField(Position target) {
		super.unit.useTech(TechType.Stasis_Field, target);
	}

}
