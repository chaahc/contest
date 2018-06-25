

import bwapi.Unit;
import bwapi.UnitType;

public class Observer extends BattleUnit{

	public Observer(int unitId, Unit unit, UnitType unitType) {
		super(unitId, unit, unitType);
		// TODO Auto-generated constructor stub
	}

	public void follow(Unit unit) {
		super.unit.follow(unit);
	}
}
