import java.util.HashMap;
import java.util.Map;

public class BuildOrderSetManager {
	private static BuildOrderSetManager instance = new BuildOrderSetManager();
	
	public static BuildOrderSetManager instance() {
		return instance;
	}
	
	private Map<StrategyType, BuildOrderSet> buildOrderSets = new HashMap<StrategyType, BuildOrderSet>();
	
	public BuildOrderSet getBuildOrderSet(StrategyType strategyType) {
		return buildOrderSets.get(strategyType);
	}
	
	public BuildOrderSetManager() {
		this.loadProtossBasic();
	}
	
	public void loadProtossBasic() {
		InitialBuildOrder initialBuildOrder = new ProtossBasicInitialBuildOrder();
		
		BuildingUnitOrder buildUnitOrder = new ProtossBasicBuildingUnitOrder();
		
		BattleUnitOrder battleUnitOrder = new ProtossBasicBattleUnitOrder();
		
		UpgradeOrder upgradeOrder = new ProtossBasicUpgradeOrder();
		
		BattleOrder battleOrder = new BattleOrder();
		
		BuildOrderSet buildOrderSet = new BuildOrderSet(StrategyType.PROTOSS_BASIC, initialBuildOrder, buildUnitOrder, battleUnitOrder, upgradeOrder, battleOrder);
		this.buildOrderSets.put(StrategyType.PROTOSS_BASIC, buildOrderSet);
	}
}
