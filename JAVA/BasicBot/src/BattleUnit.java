
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class BattleUnit {
	protected int unitId;
	protected Unit unit;
	protected UnitType unitType;
	protected BattleUnitStatus battleUnitStatus;
	protected Position lastTargetPosition;
	protected Unit lastTargetUnit;
	
	protected BattleUnit(int unitId, Unit unit, UnitType unitType) {
		this.unitId = unitId;
		this.unit = unit;
		this.unitType = unitType;
		this.battleUnitStatus = BattleUnitStatus.MOVE;
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
		CommandUtil.move(this.unit, position);
	}
	
	public void setBattleUnitStatus(BattleUnitStatus battleUnitStatus) {
		this.battleUnitStatus = battleUnitStatus;
	}
	
	public BattleUnitStatus getBattleUnitStatus() {
		return this.battleUnitStatus;
	}
	
	public void setLastTargetPosition(Position lastTargetPosition) {
		this.lastTargetPosition = lastTargetPosition;
	}
	
	public Position getLastTargetPosition() {
		return this.lastTargetPosition;
	}
	
	public void setLastTargetUnit(Unit lastTargetUnit) {
		this.lastTargetUnit = lastTargetUnit;
	}
	
	public Unit getLastTargetUnit() {
		return this.lastTargetUnit;
	}
}