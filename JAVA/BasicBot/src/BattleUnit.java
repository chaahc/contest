
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class BattleUnit {
	protected int unitId;
	protected Unit unit;
	protected UnitType unitType;
	
	protected BattleUnit(int unitId, Unit unit, UnitType unitType) {
		this.unitId = unitId;
		this.unit = unit;
		this.unitType = unitType;
	}
	
	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	public void move(Position position) {
		this.unit.move(position);
	}
}