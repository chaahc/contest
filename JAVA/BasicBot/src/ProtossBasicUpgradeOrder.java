import bwapi.UnitType;
import bwapi.UpgradeType;

public class ProtossBasicUpgradeOrder extends UpgradeOrder {
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		super.order(UnitType.Protoss_Cybernetics_Core, UpgradeType.Singularity_Charge, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				return true;
			}
		});
		super.order(UnitType.Protoss_Citadel_of_Adun, UpgradeType.Leg_Enhancements, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

}
