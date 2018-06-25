

import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class Shuttle extends BattleUnit{

	public Shuttle(int unitId, Unit unit, UnitType unitType) {
		super(unitId, unit, unitType);
		// TODO Auto-generated constructor stub
	}
	
	public void load(Unit target) {
		super.unit.load(target);
	}
	
	public void unload(Position position) {
		super.unit.unloadAll(position);
	}
}
