

import bwapi.Unit;
import bwapi.UnitType;

public class Carrier extends BattleUnit implements WeaponTrainable{

	public Carrier(int unitId, Unit unit, UnitType unitType) {
		super(unitId, unit, unitType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void train() {
		// TODO Auto-generated method stub
		super.unit.train(UnitType.Protoss_Interceptor);
	}

}
