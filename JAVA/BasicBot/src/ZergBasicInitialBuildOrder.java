import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;

public class ZergBasicInitialBuildOrder extends InitialBuildOrder {
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
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Pylon,
				firstPylonPos, true);
		//8
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Forge,
				forgePos, true);
		// 2.16 complete
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Photon_Cannon,
				firstPhotoPos, true);
		// 2.29 complete
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Photon_Cannon,
				secondPhotoPos, true);
		//9
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);		
		//10
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//11
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//12
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Gateway,
				firstGatewayPos, true);
		//13
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		//14
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Zealot);
		//15
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Probe,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
		BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Protoss_Pylon,
				BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
	}
}
