import bwapi.TechType;
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
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) == 2) {
					return true;
				}
				return false;
			}
		});
		super.order(UnitType.Protoss_Citadel_of_Adun, UpgradeType.Leg_Enhancements, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				return true;
			}
		});
		super.order(UnitType.Protoss_Observatory, UpgradeType.Sensor_Array, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				return true;
			}
		});
		super.order(UnitType.Protoss_Forge, UpgradeType.Protoss_Ground_Weapons, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) == 2 && 
						MyBotModule.Broodwar.self().minerals() >= 500 && MyBotModule.Broodwar.self().minerals() >= 500) {
					return true;
				}
				return true;
			}
		});
		super.order(UnitType.Protoss_Forge, UpgradeType.Protoss_Air_Armor, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) == 2 && 
						MyBotModule.Broodwar.self().minerals() >= 500 && MyBotModule.Broodwar.self().minerals() >= 500) {
					return true;
				}
				return true;
			}
		});
		super.order(UnitType.Protoss_Forge, UpgradeType.Protoss_Plasma_Shields, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) == 2 && 
						MyBotModule.Broodwar.self().minerals() >= 500 && MyBotModule.Broodwar.self().minerals() >= 500) {
					return true;
				}
				return true;
			}
		});
//		super.order(UnitType.Protoss_Templar_Archives, TechType.Psionic_Storm, new OrderCondition() {
//			@Override
//			public boolean isActive() {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
//		super.order(UnitType.Protoss_Robotics_Support_Bay, UpgradeType.Gravitic_Drive, new OrderCondition() {
//			@Override
//			public boolean isActive() {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
//		super.order(UnitType.Protoss_Templar_Archives, TechType.Hallucination, new OrderCondition() {
//			@Override
//			public boolean isActive() {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
//		super.order(UnitType.Protoss_Fleet_Beacon, UpgradeType.Carrier_Capacity, new OrderCondition() {
//			@Override
//			public boolean isActive() {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
		super.order(UnitType.Protoss_Fleet_Beacon, TechType.Disruption_Web, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				return true;
			}
		});
		super.order(UnitType.Protoss_Arbiter_Tribunal, TechType.Recall, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				return true;
			}
		});
//		super.order(UnitType.Protoss_Arbiter_Tribunal, TechType.Stasis_Field, new OrderCondition() {
//			@Override
//			public boolean isActive() {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
	}
}
