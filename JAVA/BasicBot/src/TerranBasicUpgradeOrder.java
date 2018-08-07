import bwapi.TechType;
import bwapi.UnitType;
import bwapi.UpgradeType;

public class TerranBasicUpgradeOrder extends UpgradeOrder {
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
				if (BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Carrier).getUnitCount() > 2 &&
						MyBotModule.Broodwar.self().minerals() >= 150 && MyBotModule.Broodwar.self().gas() >= 150) {
					return true;
				}
				return false;
			}
		});
//		super.order(UnitType.Protoss_Templar_Archives, TechType.Psionic_Storm, new OrderCondition() {
//			@Override
//			public boolean isActive() {
//				// TODO Auto-generated method stub
//				if (BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Dark_Templar).getUnitCount() > 3 &&
//						MyBotModule.Broodwar.self().minerals() >= 200 && MyBotModule.Broodwar.self().gas() >= 200) {
//					return true;
//				}
//				return false;
//			}
//		});
		super.order(UnitType.Protoss_Observatory, UpgradeType.Gravitic_Boosters, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) > 2) { 
					return true;
				}
				return false;
			}
		});
		super.order(UnitType.Protoss_Observatory, UpgradeType.Sensor_Array, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) > 2 &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Observatory) != null &&
						BuildingUnitManager.instance().getBuildingUnit(UnitType.Protoss_Observatory).isUpgradeCompleted(UpgradeType.Gravitic_Boosters)) { 
					return true;
				}
				return false;
			}
		});
		super.order(UnitType.Protoss_Forge, UpgradeType.Protoss_Ground_Weapons, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) > 2 && 
						MyBotModule.Broodwar.self().minerals() >= 200 && MyBotModule.Broodwar.self().gas() >= 200) {
					return true;
				}
				return false;
			}
		});
		super.order(UnitType.Protoss_Forge, UpgradeType.Protoss_Ground_Armor, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				if (BuildingUnitManager.instance().getCompletedBuildingUnitCount(UnitType.Protoss_Nexus) > 2 && 
						MyBotModule.Broodwar.self().minerals() >= 250 && MyBotModule.Broodwar.self().gas() >= 250) {
					return true;
				}
				return false;
			}
		});
		super.order(UnitType.Protoss_Arbiter_Tribunal, TechType.Stasis_Field, new OrderCondition() {
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
		super.order(UnitType.Protoss_Fleet_Beacon, UpgradeType.Carrier_Capacity, new OrderCondition() {
			@Override
			public boolean isActive() {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
//				super.order(UnitType.Protoss_Templar_Archives, TechType.Hallucination, new OrderCondition() {
//					@Override
//					public boolean isActive() {
//						// TODO Auto-generated method stub
//						return true;
//					}
//				});

//				super.order(UnitType.Protoss_Fleet_Beacon, TechType.Disruption_Web, new OrderCondition() {
//					@Override
//					public boolean isActive() {
//						// TODO Auto-generated method stub
//						return true;
//					}
//				});
	}
}
