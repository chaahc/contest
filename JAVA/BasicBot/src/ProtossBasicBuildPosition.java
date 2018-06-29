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
	
	private static int[] firstPylonPosXX = null;
	private static int[] firstPylonPosYY = null;
	
	private static int[] secondPylonPosXX = null;
	private static int[] secondPylonPosYY = null;
	
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
		} 
		//오버워치
		else if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.OverWatch) {
			
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
			}
			else if(startingX == 117 && startingY ==9) {
//				return new TilePosition(secondPylonPosXX[1], secondPylonPosYY[1]);
				firstPylonPosX = firstPylonPosXX[1];
				firstPylonPosY = firstPylonPosYY[1];
				secondPylonPosX = secondPylonPosXX[1];
				secondPylonPosY = secondPylonPosYY[1];
			}
			else if(startingX == 117 && startingY ==118) {
//				return new TilePosition(secondPylonPosXX[2], secondPylonPosYY[2]);
				firstPylonPosX = firstPylonPosXX[2];
				firstPylonPosY = firstPylonPosYY[2];
				secondPylonPosX = secondPylonPosXX[2];
				secondPylonPosY = secondPylonPosYY[2];
			}
			else if(startingX == 7 && startingY ==118) {
//				return new TilePosition(secondPylonPosXX[3], secondPylonPosYY[3]);
				firstPylonPosX = firstPylonPosXX[3];
				firstPylonPosY = firstPylonPosYY[3];
				secondPylonPosX = secondPylonPosXX[3];
				secondPylonPosY = secondPylonPosYY[3];
			}
		} 
		//오버워치
		else if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.OverWatch) {
			
		}
		
	} 	
	
}
