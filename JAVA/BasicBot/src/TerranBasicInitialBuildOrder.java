import bwapi.TilePosition;
import bwapi.UnitType;

public class TerranBasicInitialBuildOrder extends InitialBuildOrder {
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		ProtossBasicBuildPosition.Instance().setPosition();
		TilePosition firstPylonPos = new TilePosition(ProtossBasicBuildPosition.firstPylonPosX, ProtossBasicBuildPosition.firstPylonPosY);
		TilePosition secondPylonPos = new TilePosition(ProtossBasicBuildPosition.secondPylonPosX, ProtossBasicBuildPosition.secondPylonPosY);
		TilePosition firstGatewayPos = new TilePosition(ProtossBasicBuildPosition.gatewayPosX, ProtossBasicBuildPosition.gatewayPosY);
		TilePosition forgePos = new TilePosition(ProtossBasicBuildPosition.forgePosX, ProtossBasicBuildPosition.forgePosY);
		TilePosition firstPhotoPos = new TilePosition(ProtossBasicBuildPosition.firstPhotonPosX, ProtossBasicBuildPosition.firstPhotonPosY);
		TilePosition secondPhotoPos = new TilePosition(ProtossBasicBuildPosition.secondPhotonPosX, ProtossBasicBuildPosition.secondPhotonPosY);
		
		//5
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//6
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//7
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//8
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Pylon,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//9
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Assimilator,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//10
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Gateway,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//11
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//12
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//13
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Zealot);
		//14
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
	}
}
