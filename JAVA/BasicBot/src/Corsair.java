

import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

public class Corsair extends BattleUnit {

	public Corsair(int unitId, Unit unit, UnitType unitType) {
		super(unitId, unit, unitType);
		// TODO Auto-generated constructor stub
	}

	public void disruptionWeb(Unit target) {
		// TODO Auto-generated method stub
		super.unit.useTech(TechType.Disruption_Web, target);
	}
}
