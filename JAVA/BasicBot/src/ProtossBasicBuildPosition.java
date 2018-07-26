import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;


public class ProtossBasicBuildPosition {
	
	/// 건물과 건물간 띄울 최소한의 간격 - 일반적인 건물의 경우
	private static int BuildingSpacingOld = Config.BuildingSpacing;
	/// 건물과 건물간 띄울 최소한의 간격 - ResourceDepot 건물의 경우 (Nexus, Hatchery, Command Center)
	private static int BuildingResourceDepotSpacingOld = Config.BuildingResourceDepotSpacing;
	
	public static Map<String, TilePosition> mapInfo = new HashMap<String, TilePosition>();
	public static final String BASE11 = "11";
	public static final String BASE12 = "12";
	public static final String BASE1 = "1";
	public static final String BASE3 = "3";
	public static final String BASE5 = "5";
	public static final String BASE6 = "6";
	public static final String BASE7 = "7";
	public static final String BASE9 = "9";
	public static final String CENTER = "center";
	public static final String PYLON11 = "P11";
	public static final String PYLON1 = "P1";
	public static final String PYLON5 = "P5";
	public static final String PYLON7 = "P7";
	private static List<TilePosition> centerExpansionNearSelf = new ArrayList<TilePosition>();
	private static List<TilePosition> centerExpansionNearEnemy = new ArrayList<TilePosition>();
	private static Map<String, TilePosition> scoutPositions = new HashMap<String, TilePosition>();
	
	public static String START_BASE = "";
	
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
			firstPylonPosXX = new int[]{18,108,108,17};
			firstPylonPosYY = new int[]{31,31,96,96};
			secondPylonPosXX = new int[]{7,117,117,7};
			secondPylonPosYY = new int[]{20,20,105,105};
			
			pylonPosXX = new int[]{17,108,108,18};
			pylonPosYY = new int[]{37,37,96,96};
			forgePosXX = new int[]{17,108,106,19};
			forgePosYY = new int[]{35,35,91,92};
			gatewayPosXX = new int[]{20,104,104,19};
			gatewayPosYY = new int[]{30,30,95,95};
			firstPhotonPosXX = new int[]{18,108,108,17};
			firstPhotonPosYY = new int[]{33,33,94,94};
			secondPhotonPosXX = new int[]{16,111,110,16};
			secondPhotonPosYY = new int[]{33,35,94,92};
			
			mapInfo.put(BASE11, new TilePosition(7, 9));
			mapInfo.put(BASE12, new TilePosition(63, 2));
			mapInfo.put(BASE1, new TilePosition(117, 9));
			mapInfo.put(BASE3, new TilePosition(116, 64));
			mapInfo.put(BASE5, new TilePosition(117, 118));
			mapInfo.put(BASE6, new TilePosition(63, 124));
			mapInfo.put(BASE7, new TilePosition(7, 118));
			mapInfo.put(BASE9, new TilePosition(12, 64));
			mapInfo.put(CENTER, new TilePosition(64, 64));
			mapInfo.put(PYLON11, new TilePosition(28, 28));
			mapInfo.put(PYLON1, new TilePosition(98, 28));
			mapInfo.put(PYLON5, new TilePosition(98, 98));
			mapInfo.put(PYLON7, new TilePosition(28, 98));
		} 
		//투혼
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
				
				START_BASE = BASE11;
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
				
				START_BASE = BASE1;
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
				
				START_BASE = BASE5;
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
				
				START_BASE = BASE7;
			}
		} 
		//오버워치
		else if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.OverWatch) {
			
		}
	} 	
	
	public TilePosition getEnemyBase(TilePosition enemy) {
		TilePosition enemyBaseLocation = null;
		if (mapInfo.get(BASE1).equals(enemy)) {
			enemyBaseLocation = new TilePosition(107, 3);
		} else if (mapInfo.get(BASE5).equals(enemy)) {
			enemyBaseLocation = new TilePosition(107, 123);
		} else if (mapInfo.get(BASE7).equals(enemy)) {
			enemyBaseLocation = new TilePosition(22, 123);
		} else if (mapInfo.get(BASE11).equals(enemy)){
			enemyBaseLocation = new TilePosition(22, 3);
		}
		return enemyBaseLocation;
	}
	
	public List<TilePosition> getCenterExpansionNearEnemy(TilePosition enemy) {
		return getCenterExpansionNearBase(centerExpansionNearEnemy, enemy);
	}
	
	public List<TilePosition> getCenterExpansionNearSelf() {
		return getCenterExpansionNearBase(centerExpansionNearSelf, mapInfo.get(START_BASE));
	}
	
	public List<TilePosition> getCenterExpansionNearBase(List<TilePosition> centerExpansionNearBase, TilePosition base) {
		if (centerExpansionNearBase.isEmpty()) {
			if (mapInfo.get(BASE1).equals(base)) {
				centerExpansionNearBase.add(mapInfo.get(BASE12));
				centerExpansionNearBase.add(mapInfo.get(BASE3));
			} else if (mapInfo.get(BASE5).equals(base)) {
				centerExpansionNearBase.add(mapInfo.get(BASE3));
				centerExpansionNearBase.add(mapInfo.get(BASE6));
			} else if (mapInfo.get(BASE7).equals(base)) {
				centerExpansionNearBase.add(mapInfo.get(BASE6));
				centerExpansionNearBase.add(mapInfo.get(BASE9));
			} else if (mapInfo.get(BASE11).equals(base)){
				centerExpansionNearBase.add(mapInfo.get(BASE9));
				centerExpansionNearBase.add(mapInfo.get(BASE12));
			}
		}
		return centerExpansionNearBase;
	}
	
	public Map<String, TilePosition> getScoutPositions() {
		if (scoutPositions.isEmpty()) {
			if (START_BASE.equals(BASE1)) {
				scoutPositions.put(BASE11, mapInfo.get(BASE11));
				scoutPositions.put(BASE9, mapInfo.get(BASE9));
				scoutPositions.put(BASE7, mapInfo.get(BASE7));
				scoutPositions.put(BASE6, mapInfo.get(BASE6));
				scoutPositions.put(BASE5, mapInfo.get(BASE5));
			} else if (START_BASE.equals(BASE5)) {
				scoutPositions.put(BASE1, mapInfo.get(BASE1));
				scoutPositions.put(BASE12, mapInfo.get(BASE12));
				scoutPositions.put(BASE11, mapInfo.get(BASE11));
				scoutPositions.put(BASE9, mapInfo.get(BASE9));
				scoutPositions.put(BASE7, mapInfo.get(BASE7));
			} else if (START_BASE.equals(BASE7)) {
				scoutPositions.put(BASE5, mapInfo.get(BASE5));
				scoutPositions.put(BASE3, mapInfo.get(BASE3));
				scoutPositions.put(BASE1, mapInfo.get(BASE1));
				scoutPositions.put(BASE12, mapInfo.get(BASE12));
				scoutPositions.put(BASE11, mapInfo.get(BASE11));
			} else if (START_BASE.equals(BASE11)) {
				scoutPositions.put(BASE7, mapInfo.get(BASE7));
				scoutPositions.put(BASE6, mapInfo.get(BASE6));
				scoutPositions.put(BASE5, mapInfo.get(BASE5));
				scoutPositions.put(BASE3, mapInfo.get(BASE3));
				scoutPositions.put(BASE1, mapInfo.get(BASE1));
			}
		}
		return scoutPositions;
	}
}
