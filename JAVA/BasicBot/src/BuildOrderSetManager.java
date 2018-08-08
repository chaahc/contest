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
		this.loadZergBasic();
		this.loadTerranBasic();
		this.loadProtossDragoon();
	}
	
	public void loadProtossDragoon() {
		InitialBuildOrder initialBuildOrder = new ProtossBasicInitialBuildOrder();
		
		BuildingUnitOrder buildUnitOrder = new ProtossDragoonBuildingUnitOrder();
		
		BattleUnitOrder battleUnitOrder = new ProtossDragoonBattleUnitOrder();
		
		UpgradeOrder upgradeOrder = new ProtossDragoonUpgradeOrder();
		
		BattleOrder battleOrder = new ProtossDragoonBattleOrder();
		
		BuildOrderSet buildOrderSet = new BuildOrderSet(StrategyType.PROTOSS_DRAGOON, initialBuildOrder, buildUnitOrder, battleUnitOrder, upgradeOrder, battleOrder);
		this.buildOrderSets.put(StrategyType.PROTOSS_DRAGOON, buildOrderSet);
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
	
	public void loadZergBasic() {
		InitialBuildOrder initialBuildOrder = new ZergBasicInitialBuildOrder();
		
		BuildingUnitOrder buildUnitOrder = new ZergBasicBuildingUnitOrder();
		
		BattleUnitOrder battleUnitOrder = new ZergBasicBattleUnitOrder();
		
		UpgradeOrder upgradeOrder = new ZergBasicUpgradeOrder();
		
		BattleOrder battleOrder = new ZergBattleOrder();
		
		BuildOrderSet buildOrderSet = new BuildOrderSet(StrategyType.ZERG_BASIC, initialBuildOrder, buildUnitOrder, battleUnitOrder, upgradeOrder, battleOrder);
		this.buildOrderSets.put(StrategyType.ZERG_BASIC, buildOrderSet);
	}
	
	public void loadTerranBasic() {
		InitialBuildOrder initialBuildOrder = new TerranBasicInitialBuildOrder();
		
		BuildingUnitOrder buildUnitOrder = new TerrranBasicBuildingUnitOrder();
		
		BattleUnitOrder battleUnitOrder = new TerranBasicBattleUnitOrder();
		
		UpgradeOrder upgradeOrder = new TerranBasicUpgradeOrder();
		
		BattleOrder battleOrder = new TerranBattleOrder();
		
		BuildOrderSet buildOrderSet = new BuildOrderSet(StrategyType.TERRAN_BASIC, initialBuildOrder, buildUnitOrder, battleUnitOrder, upgradeOrder, battleOrder);
		this.buildOrderSets.put(StrategyType.TERRAN_BASIC, buildOrderSet);
	}
	
	public void loadFastCarrier() {
		InitialBuildOrder initialBuildOrder = new FastCarrierInitialBuildOrder();
		
		BuildingUnitOrder buildUnitOrder = new FastCarrierBuildingUnitOrder();
		
		BattleUnitOrder battleUnitOrder = new FastCarrierBattleUnitOrder();
		
		UpgradeOrder upgradeOrder = new FastCarrierUpgradeOrder();
		
		BattleOrder battleOrder = new FastCarrierBattleOrder();
		
		BuildOrderSet buildOrderSet = new BuildOrderSet(StrategyType.FAST_CARRIER, initialBuildOrder, buildUnitOrder, battleUnitOrder, upgradeOrder, battleOrder);
		this.buildOrderSets.put(StrategyType.FAST_CARRIER, buildOrderSet);
	}
}
