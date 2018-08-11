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
	
	public static final String DEFENCE11 = "D11";
	public static final String DEFENCE1 = "D1";
	public static final String DEFENCE5 = "D5";
	public static final String DEFENCE7 = "D7";
	
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
			firstPylonPosXX = new int[]{17,108,108,18};
			firstPylonPosYY = new int[]{37,37,96,96};
			secondPylonPosXX = new int[]{11,114,114,13};
			secondPylonPosYY = new int[]{37,37,89,89};
			
			forgePosXX = new int[]{17,107,107,18};
			forgePosYY = new int[]{35,35,94,94};
			gatewayPosXX = new int[]{17,106,107,18};
			gatewayPosYY = new int[]{32,32,90,90};
			firstPhotonPosXX = new int[]{15,110,110,16};
			firstPhotonPosYY = new int[]{33,35,96,94};
			secondPhotonPosXX = new int[]{15,110,110,16};
			secondPhotonPosYY = new int[]{35,37,94,96};
			
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
			
			mapInfo.put(DEFENCE11, new TilePosition(41, 44));
			mapInfo.put(DEFENCE1, new TilePosition(86, 45));
			mapInfo.put(DEFENCE5, new TilePosition(86, 82));
			mapInfo.put(DEFENCE7, new TilePosition(41, 81));
		} 
		//투혼
		else if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.Spirit) {
			firstPylonPosXX = new int[]{22,94,105,38};
			firstPylonPosYY = new int[]{40,19,93,106};
			secondPylonPosXX = new int[]{18,110,110,16};
			secondPylonPosYY = new int[]{31,31,92,90};
			
			forgePosXX = new int[]{22,89,104,33};
			forgePosYY = new int[]{38,17,91,108};
			gatewayPosXX = new int[]{22,91,104,35};
			gatewayPosYY = new int[]{35,19,88,105};
			firstPhotonPosXX = new int[]{20,92,107,36};
			firstPhotonPosYY = new int[]{40,17,89,108};
			secondPhotonPosXX = new int[]{20,94,107,38};
			secondPhotonPosYY = new int[]{38,17,91,108};
			
			//required repositioning
			mapInfo.put(BASE11, new TilePosition(7, 6));
			mapInfo.put(BASE12, new TilePosition(51, 7));
			mapInfo.put(BASE1, new TilePosition(117, 7));
			mapInfo.put(BASE3, new TilePosition(118, 53));
			mapInfo.put(BASE5, new TilePosition(117, 117));
			mapInfo.put(BASE6, new TilePosition(76, 119));
			mapInfo.put(BASE7, new TilePosition(7, 116));
			mapInfo.put(BASE9, new TilePosition(8, 73));
			mapInfo.put(CENTER, new TilePosition(64, 64));
			mapInfo.put(PYLON11, new TilePosition(32, 22));
			mapInfo.put(PYLON1, new TilePosition(111, 32));
			mapInfo.put(PYLON5, new TilePosition(94, 105));
			mapInfo.put(PYLON7, new TilePosition(17, 94));
			
			mapInfo.put(DEFENCE11, new TilePosition(40, 46));
			mapInfo.put(DEFENCE1, new TilePosition(83, 32));
			mapInfo.put(DEFENCE5, new TilePosition(87, 80));
			mapInfo.put(DEFENCE7, new TilePosition(44, 94));
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
		else if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.Spirit) {
			if(startingX == 7 && startingY ==6) {			
//				return new TilePosition(secondPylonPosXX[0], secondPylonPosYY[0]);
				firstPylonPosX = firstPylonPosXX[0];
				firstPylonPosY = firstPylonPosYY[0];
				secondPylonPosX = secondPylonPosXX[0];
				secondPylonPosY = secondPylonPosYY[0];
				
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
			else if(startingX == 117 && startingY ==7) {
//				return new TilePosition(secondPylonPosXX[1], secondPylonPosYY[1]);
				firstPylonPosX = firstPylonPosXX[1];
				firstPylonPosY = firstPylonPosYY[1];
				secondPylonPosX = secondPylonPosXX[1];
				secondPylonPosY = secondPylonPosYY[1];
				
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
			else if(startingX == 117 && startingY ==117) {
//				return new TilePosition(secondPylonPosXX[2], secondPylonPosYY[2]);
				firstPylonPosX = firstPylonPosXX[2];
				firstPylonPosY = firstPylonPosYY[2];
				secondPylonPosX = secondPylonPosXX[2];
				secondPylonPosY = secondPylonPosYY[2];
				
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
			else if(startingX == 7 && startingY ==116) {
//				return new TilePosition(secondPylonPosXX[3], secondPylonPosYY[3]);
				firstPylonPosX = firstPylonPosXX[3];
				firstPylonPosY = firstPylonPosYY[3];
				secondPylonPosX = secondPylonPosXX[3];
				secondPylonPosY = secondPylonPosYY[3];
				
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
		//투혼
		else if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.Spirit) {
			if(startingX == 7 && startingY ==6) {			
//				return new TilePosition(secondPylonPosXX[0], secondPylonPosYY[0]);
				firstPylonPosX = firstPylonPosXX[0];
				firstPylonPosY = firstPylonPosYY[0];
				secondPylonPosX = secondPylonPosXX[0];
				secondPylonPosY = secondPylonPosYY[0];
				
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
		System.out.println("build position end");
	}
	
	public boolean isEnemyOpposite(TilePosition enemyMainBasePosition) {
		if (mapInfo.get(BASE1).equals(enemyMainBasePosition) &&
				START_BASE == BASE7) {
			return true;
		} else if (mapInfo.get(BASE5).equals(enemyMainBasePosition) &&
				START_BASE == BASE11) {
			return true;
		} else if (mapInfo.get(BASE7).equals(enemyMainBasePosition) &&
				START_BASE == BASE1) {
			return true;
		} else if (mapInfo.get(BASE11).equals(enemyMainBasePosition) &&
				START_BASE == BASE5){
			return true;
		}
		return false;
	}
	
	public TilePosition getEnemyMineralPosition(TilePosition enemyMainBasePosition) {
		TilePosition enemyMineralPosition = null;
		if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.CircuitBreaker) {
			if (mapInfo.get(BASE1).equals(enemyMainBasePosition)) {
				enemyMineralPosition = new TilePosition(126, 6);
			} else if (mapInfo.get(BASE5).equals(enemyMainBasePosition)) {
				enemyMineralPosition = new TilePosition(126, 122);
			} else if (mapInfo.get(BASE7).equals(enemyMainBasePosition)) {
				enemyMineralPosition = new TilePosition(1, 123);
			} else if (mapInfo.get(BASE11).equals(enemyMainBasePosition)){
				enemyMineralPosition = new TilePosition(1, 7);
			}
		}
		else if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.Spirit) {
			if (mapInfo.get(BASE1).equals(enemyMainBasePosition)) {
				enemyMineralPosition = new TilePosition(126, 5);
			} else if (mapInfo.get(BASE5).equals(enemyMainBasePosition)) {
				enemyMineralPosition = new TilePosition(126, 121);
			} else if (mapInfo.get(BASE7).equals(enemyMainBasePosition)) {
				enemyMineralPosition = new TilePosition(1, 121);
			} else if (mapInfo.get(BASE11).equals(enemyMainBasePosition)){
				enemyMineralPosition = new TilePosition(1, 3);
			}
		}
		return enemyMineralPosition;
	}
	
	public TilePosition getEnemyDefencePosition(TilePosition enemy) {
		TilePosition enemyPosition = null;
		if (mapInfo.get(BASE1).equals(enemy)) {
			enemyPosition = mapInfo.get(DEFENCE1);
		} else if (mapInfo.get(BASE5).equals(enemy)) {
			enemyPosition = mapInfo.get(DEFENCE5);
		} else if (mapInfo.get(BASE7).equals(enemy)) {
			enemyPosition = mapInfo.get(DEFENCE7);
		} else if (mapInfo.get(BASE11).equals(enemy)){
			enemyPosition = mapInfo.get(DEFENCE11);
		}
		return enemyPosition;
	}
	
	public TilePosition getEnemyPosition(TilePosition enemy) {
		TilePosition enemyPosition = null;
		if (mapInfo.get(BASE1).equals(enemy)) {
			enemyPosition = mapInfo.get(PYLON1);
		} else if (mapInfo.get(BASE5).equals(enemy)) {
			enemyPosition = mapInfo.get(PYLON5);
		} else if (mapInfo.get(BASE7).equals(enemy)) {
			enemyPosition = mapInfo.get(PYLON7);
		} else if (mapInfo.get(BASE11).equals(enemy)){
			enemyPosition = mapInfo.get(PYLON11);
		}
		return enemyPosition;
	}
	
	public TilePosition getEnemyBase(TilePosition enemy) {
		TilePosition enemyBaseLocation = null;
		if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.CircuitBreaker) {
			if (mapInfo.get(BASE1).equals(enemy)) {
				enemyBaseLocation = new TilePosition(110, 3);
			} else if (mapInfo.get(BASE5).equals(enemy)) {
				enemyBaseLocation = new TilePosition(110, 123);
			} else if (mapInfo.get(BASE7).equals(enemy)) {
				enemyBaseLocation = new TilePosition(18, 123);
			} else if (mapInfo.get(BASE11).equals(enemy)){
				enemyBaseLocation = new TilePosition(18, 3);
			}
		}
		else if (InformationManager.Instance().getMapSpecificInformation().getMap() == MAP.Spirit) {
			if (mapInfo.get(BASE1).equals(enemy)) {
				enemyBaseLocation = new TilePosition(123, 24);
			} else if (mapInfo.get(BASE5).equals(enemy)) {
				enemyBaseLocation = new TilePosition(110, 122);
			} else if (mapInfo.get(BASE7).equals(enemy)) {
				enemyBaseLocation = new TilePosition(7, 104);
			} else if (mapInfo.get(BASE11).equals(enemy)){
				enemyBaseLocation = new TilePosition(15, 4);
			}
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
