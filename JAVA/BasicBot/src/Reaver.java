

import bwapi.Unit;
import bwapi.UnitType;

public class Reaver extends BattleUnit implements WeaponTrainable{
	public Reaver(int unitId, Unit unit, UnitType unitType) {
		super(unitId, unit, unitType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void train() {
		// TODO Auto-generated method stub
		if (super.unit.canTrain(UnitType.Protoss_Scarab)) {
			super.unit.train(UnitType.Protoss_Scarab);
		}
	}

}
