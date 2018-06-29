import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;

public class ProtossBasicInitialBuildOrder extends InitialBuildOrder {
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		ProtossBasicBuildPosition.Instance().setPosition();
		TilePosition firstPos = new TilePosition(ProtossBasicBuildPosition.Instance().firstPylonPosX, ProtossBasicBuildPosition.Instance().firstPylonPosY);
		TilePosition secondPylonPos = new TilePosition(ProtossBasicBuildPosition.Instance().secondPylonPosX, ProtossBasicBuildPosition.Instance().secondPylonPosY);
		
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Pylon,
				firstPos, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Gateway,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Pylon,
				secondPylonPos, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Forge,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation);
	}
}
