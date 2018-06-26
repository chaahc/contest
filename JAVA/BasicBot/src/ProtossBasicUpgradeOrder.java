import bwapi.UnitType;
import bwapi.UpgradeType;

public class ProtossBasicUpgradeOrder extends UpgradeOrder {
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		BuildingUnit cyberneticsCore = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Cybernetics_Core);
		if (cyberneticsCore != null && cyberneticsCore.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED) {
			if (cyberneticsCore.getUnit().canUpgrade(UpgradeType.Singularity_Charge)) {
				cyberneticsCore.getUnit().upgrade(UpgradeType.Singularity_Charge);
				cyberneticsCore.completeUpgrade(UpgradeType.Singularity_Charge);
			}
		}
		
		BuildingUnit citadelOfAdun = BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Citadel_of_Adun);
		if (citadelOfAdun != null && citadelOfAdun.getBuildingStatus() == BuildingUnit.BuildingStatus.COMPLETED) {
			if (citadelOfAdun.getUnit().canUpgrade(UpgradeType.Leg_Enhancements)) {
				citadelOfAdun.getUnit().upgrade(UpgradeType.Leg_Enhancements);
				citadelOfAdun.completeUpgrade(UpgradeType.Leg_Enhancements);
			}
		}
	}

}
