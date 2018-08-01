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
				firstPylonPos, true);
		//9
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//10
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Gateway,
				firstGatewayPos, true);
		//11
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Forge,
				forgePos, true);
		//12
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//13
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Zealot);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Photon_Cannon,
				firstPhotoPos, true);
		//14
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//15
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Pylon,
				secondPylonPos, true);
		
		StrategyManager.Instance().isInitialBuildOrderStarted = true;
	}

}
