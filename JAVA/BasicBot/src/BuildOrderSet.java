
public class BuildOrderSet {
	private StrategyType strategyType;
	private InitialBuildOrder initialBuildOrder;
	private BuildingUnitOrder buildingUnitOrder;
	private BattleUnitOrder battleUnitOrder;
	private UpgradeOrder upgradeOrder;
	
	public BuildOrderSet(StrategyType strategyType,
						InitialBuildOrder initialBuilderOrder,
						BuildingUnitOrder buildingUnitOrder,
						BattleUnitOrder battleUnitOrder,
						UpgradeOrder upgradeOrder) {
		this.strategyType = strategyType;
		this.initialBuildOrder = initialBuilderOrder;
		this.buildingUnitOrder = buildingUnitOrder;
		this.battleUnitOrder = battleUnitOrder;
		this.upgradeOrder = upgradeOrder;
	}
	
	public void executeInitialBuildOrder() {
		this.initialBuildOrder.execute();
	}
	
	public void executeBuildingUnitOrder() {
		this.buildingUnitOrder.execute();
	}
	
	public void executeBattleUnitOrder() {
		this.battleUnitOrder.execute();
	}
	
	public void executeUpgradeOrder() {
		this.upgradeOrder.execute();
	}
	
	public void setBuildingUnitOrder(BuildingUnitOrder buildingUnitOrder) {
		this.buildingUnitOrder = buildingUnitOrder;
	}
	
	public void setBattleUnitOrder(BattleUnitOrder battleUnitOrder) {
		this.battleUnitOrder = battleUnitOrder;
	}
	
	public void setUpgradeOrder(UpgradeOrder upgradeOrder) {
		this.upgradeOrder = upgradeOrder;
	}
}
