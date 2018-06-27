import bwapi.TilePosition;
import bwapi.UnitType;

public abstract class BuildingUnitOrder implements BuildOrder{
	protected void order(UnitType buildingUnit, BuildOrderItem.SeedPositionStrategy seedPositionStrategy, OrderCondition orderCondition) {
		if (orderCondition.isActive()) {
			if (BuildManager.Instance().buildQueue.getItemCount(buildingUnit) == 0 &&
					ConstructionManager.Instance().getConstructionQueueItemCount(buildingUnit, null) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(buildingUnit, seedPositionStrategy, true);
			}
		}
	}
	
	protected void order(UnitType buildingUnit, TilePosition tilePosition, OrderCondition orderCondition) {
		if (orderCondition.isActive()) {
			if (BuildManager.Instance().buildQueue.getItemCount(buildingUnit) == 0 &&
					ConstructionManager.Instance().getConstructionQueueItemCount(buildingUnit, null) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(buildingUnit, tilePosition, true);
			}
		}
	}
}
