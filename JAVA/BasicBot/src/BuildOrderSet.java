
public class BuildOrderSet {
	private StrategyType strategyType;
	private InitialBuildOrder initialBuildOrder;
	private BuildingUnitOrder buildingUnitOrder;
	private BattleUnitOrder battleUnitOrder;
	private UpgradeOrder upgradeOrder;
	private BattleOrder battleOrder;
	
	public BuildOrderSet(StrategyType strategyType,
						InitialBuildOrder initialBuilderOrder,
						BuildingUnitOrder buildingUnitOrder,
						BattleUnitOrder battleUnitOrder,
						UpgradeOrder upgradeOrder,
						BattleOrder battleOrder) {
		this.strategyType = strategyType;
		this.initialBuildOrder = initialBuilderOrder;
		this.buildingUnitOrder = buildingUnitOrder;
		this.battleUnitOrder = battleUnitOrder;
		this.upgradeOrder = upgradeOrder;
		this.battleOrder = battleOrder;
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
	
	public void executeBattleOrder() {
		this.battleOrder.execute();
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
	
	public void setBattleOrder(BattleOrder battleOrder) {
		this.battleOrder = battleOrder;
	}
}
