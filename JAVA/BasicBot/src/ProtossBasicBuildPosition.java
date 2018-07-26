import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;


public class ProtossBasicBuildPosition {
	
	/// 건물과 건물간 띄울 최소한의 간격 - 일반적인 건물의 경우
	private static int BuildingSpacingOld = Config.BuildingSpacing;
	/// 건물과 건물간 띄울 최소한의 간격 - ResourceDepot 건물의 경우 (Nexus, Hatchery, Command Center)
	private static int BuildingResourceDepotSpacingOld = Config.BuildingResourceDepotSpacing;
	
	
	public int startingX = 0;
	public int startingY = 0;
	
	public static int firstPylonPosX = 0;
	public static int firstPylonPosY = 0;
	
	public static int secondPylonPosX = 0;
	public static int secondPylonPosY = 0;	
	
	public static int pylonPosX = 0;
	public static int pylonPosY = 0;
	
	public static int gatewayPosX = 0;
	public static int gatewayPosY = 0;
	
	public static int forgePosX = 0;
	public static int forgePosY = 0;
	
	public static int firstPhotonPosX = 0;
	public static int firstPhotonPosY = 0;
	
	public static int secondPhotonPosX = 0;
	public static int secondPhotonPosY = 0;
	
	private static int[] firstPylonPosXX = null;
	private static int[] firstPylonPosYY = null;
	
	private static int[] secondPylonPosXX = null;
	private static int[] secondPylonPosYY = null;
	
	private static int[] pylonPosXX = null;
	private static int[] pylonPosYY = null;
	
	private static int[] gatewayPosXX = null;
	private static int[] gatewayPosYY = null;
	
	private static int[] forgePosXX = null;
	private static int[] forgePosYY = null;
	
	private static int[] firstPhotonPosXX = null;
	private static int[] firstPhotonPosYY = null;
	
	private static int[] secondPhotonPosXX = null;
	private static int[] secondPhotonPosYY = null;
	
	private static ProtossBasicBuildPosition instance = new ProtossBasicBuildPosition();
	
	public static ProtossBasicBuildPosition Instance() {
		return instance;
	}
	
	public void setPosition() {
		Config.BuildingSpacing = 0;
		Config.BuildingResourceDepotSpacing = 0;
		

		System.out.println(InformationManager.Instance().getMapSpecificInformation().getMap());
		
		//서킷브레이커
		if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.CircuitBreaker) {
			System.out.println("1");
			firstPylonPosXX = new int[]{7,117,117,7};
			firstPylonPosYY = new int[]{20,20,105,105};
			secondPylonPosXX = new int[]{18,110,110,16};
			secondPylonPosYY = new int[]{31,31,92,90};
			
			pylonPosXX = new int[]{17,108,108,18};
			pylonPosYY = new int[]{37,37,96,96};
			forgePosXX = new int[]{17,108,107,108};
			forgePosYY = new int[]{35,35,94,94};
			gatewayPosXX = new int[]{17,108,106,18};
			gatewayPosYY = new int[]{32,32,91,91};
			firstPhotonPosXX = new int[]{15,111,110,16};
			firstPhotonPosYY = new int[]{33,33,96,94};
			secondPhotonPosXX = new int[]{15,111,110,16};
			secondPhotonPosYY = new int[]{35,35,94,92};
		} 
		//투혼
		else if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.Spirit) {
			firstPylonPosXX = new int[]{7,117,117,7};
			firstPylonPosYY = new int[]{20,20,105,105};
			secondPylonPosXX = new int[]{18,110,110,16};
			secondPylonPosYY = new int[]{31,31,92,90};
			
			pylonPosXX = new int[]{22,94,105,38};
			pylonPosYY = new int[]{40,19,93,106};
			forgePosXX = new int[]{22,89,104,33};
			forgePosYY = new int[]{38,17,91,108};
			gatewayPosXX = new int[]{22,91,104,35};
			gatewayPosYY = new int[]{35,19,88,105};
			firstPhotonPosXX = new int[]{20,92,107,36};
			firstPhotonPosYY = new int[]{40,17,89,108};
			secondPhotonPosXX = new int[]{20,94,107,38};
			secondPhotonPosYY = new int[]{38,17,91,108};
		}

		
		for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
			if(unit.getType() == UnitType.Protoss_Nexus){
				//System.out.println("unit.getTilePosition().getX() ==>> " + unit.getTilePosition().getX() + "  //  unit.getTilePosition().getY() ==>> " +unit.getTilePosition().getY());
				startingX = unit.getTilePosition().getX(); //unit.getPosition().getX();// getTilePosition().getX();
				startingY = unit.getTilePosition().getY();
			}
		}
		
		//서킷브레이커
		if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.CircuitBreaker) {
			System.out.println("2");
			if(startingX == 7 && startingY ==9) {			
//				return new TilePosition(secondPylonPosXX[0], secondPylonPosYY[0]);
				firstPylonPosX = firstPylonPosXX[0];
				firstPylonPosY = firstPylonPosYY[0];
				secondPylonPosX = secondPylonPosXX[0];
				secondPylonPosY = secondPylonPosYY[0];
				
				pylonPosX = pylonPosXX[0];
				pylonPosY = pylonPosYY[0];
				forgePosX = forgePosXX[0];
				forgePosY = forgePosYY[0];
				gatewayPosX = gatewayPosXX[0];
				gatewayPosY = gatewayPosYY[0];
				firstPhotonPosX = firstPhotonPosXX[0];
				firstPhotonPosY = firstPhotonPosYY[0];
				secondPhotonPosX = secondPhotonPosXX[0];
				secondPhotonPosY = secondPhotonPosYY[0];
			}
			else if(startingX == 117 && startingY ==9) {
//				return new TilePosition(secondPylonPosXX[1], secondPylonPosYY[1]);
				firstPylonPosX = firstPylonPosXX[1];
				firstPylonPosY = firstPylonPosYY[1];
				secondPylonPosX = secondPylonPosXX[1];
				secondPylonPosY = secondPylonPosYY[1];
				
				pylonPosX = pylonPosXX[1];
				pylonPosY = pylonPosYY[1];
				forgePosX = forgePosXX[1];
				forgePosY = forgePosYY[1];
				gatewayPosX = gatewayPosXX[1];
				gatewayPosY = gatewayPosYY[1];
				firstPhotonPosX = firstPhotonPosXX[1];
				firstPhotonPosY = firstPhotonPosYY[1];
				secondPhotonPosX = secondPhotonPosXX[1];
				secondPhotonPosY = secondPhotonPosYY[1];
			}
			else if(startingX == 117 && startingY ==118) {
//				return new TilePosition(secondPylonPosXX[2], secondPylonPosYY[2]);
				firstPylonPosX = firstPylonPosXX[2];
				firstPylonPosY = firstPylonPosYY[2];
				secondPylonPosX = secondPylonPosXX[2];
				secondPylonPosY = secondPylonPosYY[2];
				
				pylonPosX = pylonPosXX[2];
				pylonPosY = pylonPosYY[2];
				forgePosX = forgePosXX[2];
				forgePosY = forgePosYY[2];
				gatewayPosX = gatewayPosXX[2];
				gatewayPosY = gatewayPosYY[2];
				firstPhotonPosX = firstPhotonPosXX[2];
				firstPhotonPosY = firstPhotonPosYY[2];
				secondPhotonPosX = secondPhotonPosXX[2];
				secondPhotonPosY = secondPhotonPosYY[2];
			}
			else if(startingX == 7 && startingY ==118) {
//				return new TilePosition(secondPylonPosXX[3], secondPylonPosYY[3]);
				firstPylonPosX = firstPylonPosXX[3];
				firstPylonPosY = firstPylonPosYY[3];
				secondPylonPosX = secondPylonPosXX[3];
				secondPylonPosY = secondPylonPosYY[3];
				
				pylonPosX = pylonPosXX[3];
				pylonPosY = pylonPosYY[3];
				forgePosX = forgePosXX[3];
				forgePosY = forgePosYY[3];
				gatewayPosX = gatewayPosXX[3];
				gatewayPosY = gatewayPosYY[3];
				firstPhotonPosX = firstPhotonPosXX[3];
				firstPhotonPosY = firstPhotonPosYY[3];
				secondPhotonPosX = secondPhotonPosXX[3];
				secondPhotonPosY = secondPhotonPosYY[3];
			}
		} 
		//투혼
		else if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.Spirit) {
			if(startingX == 7 && startingY ==6) {			
//				return new TilePosition(secondPylonPosXX[0], secondPylonPosYY[0]);
				firstPylonPosX = firstPylonPosXX[0];
				firstPylonPosY = firstPylonPosYY[0];
				secondPylonPosX = secondPylonPosXX[0];
				secondPylonPosY = secondPylonPosYY[0];
				
				pylonPosX = pylonPosXX[0];
				pylonPosY = pylonPosYY[0];
				forgePosX = forgePosXX[0];
				forgePosY = forgePosYY[0];
				gatewayPosX = gatewayPosXX[0];
				gatewayPosY = gatewayPosYY[0];
				firstPhotonPosX = firstPhotonPosXX[0];
				firstPhotonPosY = firstPhotonPosYY[0];
				secondPhotonPosX = secondPhotonPosXX[0];
				secondPhotonPosY = secondPhotonPosYY[0];
			}
			else if(startingX == 117 && startingY ==7) {
//				return new TilePosition(secondPylonPosXX[1], secondPylonPosYY[1]);
				firstPylonPosX = firstPylonPosXX[1];
				firstPylonPosY = firstPylonPosYY[1];
				secondPylonPosX = secondPylonPosXX[1];
				secondPylonPosY = secondPylonPosYY[1];
				
				pylonPosX = pylonPosXX[1];
				pylonPosY = pylonPosYY[1];
				forgePosX = forgePosXX[1];
				forgePosY = forgePosYY[1];
				gatewayPosX = gatewayPosXX[1];
				gatewayPosY = gatewayPosYY[1];
				firstPhotonPosX = firstPhotonPosXX[1];
				firstPhotonPosY = firstPhotonPosYY[1];
				secondPhotonPosX = secondPhotonPosXX[1];
				secondPhotonPosY = secondPhotonPosYY[1];
			}
			else if(startingX == 117 && startingY ==117) {
//				return new TilePosition(secondPylonPosXX[2], secondPylonPosYY[2]);
				firstPylonPosX = firstPylonPosXX[2];
				firstPylonPosY = firstPylonPosYY[2];
				secondPylonPosX = secondPylonPosXX[2];
				secondPylonPosY = secondPylonPosYY[2];
				
				pylonPosX = pylonPosXX[2];
				pylonPosY = pylonPosYY[2];
				forgePosX = forgePosXX[2];
				forgePosY = forgePosYY[2];
				gatewayPosX = gatewayPosXX[2];
				gatewayPosY = gatewayPosYY[2];
				firstPhotonPosX = firstPhotonPosXX[2];
				firstPhotonPosY = firstPhotonPosYY[2];
				secondPhotonPosX = secondPhotonPosXX[2];
				secondPhotonPosY = secondPhotonPosYY[2];
			}
			else if(startingX == 7 && startingY ==116) {
//				return new TilePosition(secondPylonPosXX[3], secondPylonPosYY[3]);
				firstPylonPosX = firstPylonPosXX[3];
				firstPylonPosY = firstPylonPosYY[3];
				secondPylonPosX = secondPylonPosXX[3];
				secondPylonPosY = secondPylonPosYY[3];
				
				pylonPosX = pylonPosXX[3];
				pylonPosY = pylonPosYY[3];
				forgePosX = forgePosXX[3];
				forgePosY = forgePosYY[3];
				gatewayPosX = gatewayPosXX[3];
				gatewayPosY = gatewayPosYY[3];
				firstPhotonPosX = firstPhotonPosXX[3];
				firstPhotonPosY = firstPhotonPosYY[3];
				secondPhotonPosX = secondPhotonPosXX[3];
				secondPhotonPosY = secondPhotonPosYY[3];
			}
		}
		
	} 	
	
}
