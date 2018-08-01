import java.util.Iterator;
import java.util.List;
import java.util.Map;

import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;

/// 게임 초반에 일꾼 유닛 중에서 정찰 유닛을 하나 지정하고, 정찰 유닛을 이동시켜 정찰을 수행하는 class<br>
/// 적군의 BaseLocation 위치를 알아내는 것까지만 개발되어있습니다
public class ScoutManager {

	private Unit currentScoutUnit;
	private int currentScoutStatus;
	
	public enum ScoutStatus {
		NoScout,						///< 정찰 유닛을 미지정한 상태
		MovingToAnotherBaseLocation,	///< 적군의 BaseLocation 이 미발견된 상태에서 정찰 유닛을 이동시키고 있는 상태
		MoveAroundEnemyBaseLocation   	///< 적군의 BaseLocation 이 발견된 상태에서 정찰 유닛을 이동시키고 있는 상태
	};
	
	private BaseLocation currentScoutTargetBaseLocation = null;
	private Position currentScoutTargetPosition = Position.None;

	private CommandUtil commandUtil = new CommandUtil();
	
	private static ScoutManager instance = new ScoutManager();
	
	/// static singleton 객체를 리턴합니다
	public static ScoutManager Instance() {
		return instance;
	} 

	/// 정찰 유닛을 지정하고, 정찰 상태를 업데이트하고, 정찰 유닛을 이동시킵니다
	public void update()
	{
		// 1초에 4번만 실행합니다
		if (MyBotModule.Broodwar.getFrameCount() % 6 != 0) return;
		
		// scoutUnit 을 지정하고, scoutUnit 의 이동을 컨트롤함. 
		if (BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Probe).getUnitCount() > 7) {
			BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleGroupType.FRONT_GROUP.getValue());
			if (battleUnitGroup.getUnitCount() > 1) {
				assignScoutIfNeeded(UnitType.Protoss_Zealot);
			} else {
				assignScoutIfNeeded(UnitType.Protoss_Probe);	
			}
		}
		
		moveScoutUnit();

		// 참고로, scoutUnit 의 이동에 의해 발견된 정보를 처리하는 것은 InformationManager.update() 에서 수행함
	}

	/// 정찰 유닛을 필요하면 새로 지정합니다
	public void assignScoutIfNeeded(UnitType unitType)
	{
		BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());

		if (enemyBaseLocation == null) {
			if (currentScoutUnit == null || currentScoutUnit.exists() == false || currentScoutUnit.getHitPoints() <= 0) {
				currentScoutUnit = null;
				currentScoutStatus = ScoutStatus.NoScout.ordinal();
				
				if (unitType == UnitType.Protoss_Probe) {
					BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(unitType);
					for (int unitId : battleUnitGroup.battleUnits.keySet()) {
						BattleUnit probe = battleUnitGroup.battleUnits.get(unitId);
						if (probe != null && probe.getUnit().exists() &&
								!probe.getUnit().isCarryingMinerals() &&
								!probe.getUnit().isGatheringMinerals() &&
								(WorkerManager.Instance().initialProbe != null && WorkerManager.Instance().initialProbe.getID() != probe.getUnitId())) {
							// set unit as scout unit
							currentScoutUnit = probe.getUnit();
							WorkerManager.Instance().setScoutWorker(currentScoutUnit);
							break;	
							// 참고로, 일꾼의 정찰 임무를 해제하려면, 다음과 같이 하면 된다
							//WorkerManager::Instance().setIdleWorker(currentScoutUnit);
						}
					}
				} else if (unitType == UnitType.Protoss_Zealot) {
					BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(unitType).get(BattleGroupType.FRONT_GROUP.getValue());
					Iterator<Integer> iterator = battleUnitGroup.battleUnits.keySet().iterator();
					while (iterator.hasNext()) {
						BattleUnit zealot = battleUnitGroup.battleUnits.get(iterator.next());
						BattleUnit leader = battleUnitGroup.getLeader();
						if (leader != null && zealot != null && leader.getUnitId() != zealot.getUnitId()) {
							currentScoutUnit = zealot.getUnit();
							battleUnitGroup.removeBattleUnit(zealot.getUnitId());
							break;
						}
					}
				}
			}
		} else {
			if (currentScoutUnit == null || currentScoutUnit.exists() == false || currentScoutUnit.getHitPoints() <= 0) {
				currentScoutUnit = null;
				currentScoutStatus = ScoutStatus.NoScout.ordinal();
				
				if (unitType == UnitType.Protoss_Zealot) {
					BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(unitType).get(BattleGroupType.FRONT_GROUP.getValue());
					Iterator<Integer> iterator = battleUnitGroup.battleUnits.keySet().iterator();
					while (iterator.hasNext()) {
						BattleUnit zealot = battleUnitGroup.battleUnits.get(iterator.next());
						BattleUnit leader = battleUnitGroup.getLeader();
						if (leader != null && zealot != null && leader.getUnitId() != zealot.getUnitId()) {
							currentScoutUnit = zealot.getUnit();
							battleUnitGroup.removeBattleUnit(zealot.getUnitId());
							break;
						}
					}
				} 
			}
		}
	}


	/// 정찰 유닛을 이동시킵니다
	// 상대방 MainBaseLocation 위치를 모르는 상황이면, StartLocation 들에 대해 아군의 MainBaseLocation에서 가까운 것부터 순서대로 정찰
	// 상대방 MainBaseLocation 위치를 아는 상황이면, 해당 BaseLocation 이 있는 Region의 가장자리를 따라 계속 이동함 (정찰 유닛이 죽을때까지) 
	public void moveScoutUnit()
	{
		if (currentScoutUnit == null || currentScoutUnit.exists() == false || currentScoutUnit.getHitPoints() <= 0 )
		{
			currentScoutUnit = null;
			currentScoutStatus = ScoutStatus.NoScout.ordinal();
			return;
		}

		BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
		
		if (enemyBaseLocation == null)
		{
			// currentScoutTargetBaseLocation 가 null 이거나 정찰 유닛이 currentScoutTargetBaseLocation 에 도착했으면 
			// 아군 MainBaseLocation 으로부터 가장 가까운 미정찰 BaseLocation 을 새로운 정찰 대상 currentScoutTargetBaseLocation 으로 잡아서 이동
			if (currentScoutTargetBaseLocation == null || currentScoutUnit.getDistance(currentScoutTargetBaseLocation.getPosition()) < 100 * Config.TILE_SIZE) 
			{
				currentScoutStatus = ScoutStatus.MovingToAnotherBaseLocation.ordinal();

				double closestDistance = 1000000000;
				double tempDistance = 0;
				BaseLocation closestBaseLocation = null;
				for (BaseLocation startLocation : BWTA.getStartLocations())
				{
					// if we haven't explored it yet (방문했었던 곳은 다시 가볼 필요 없음)
					if (MyBotModule.Broodwar.isExplored(startLocation.getTilePosition()) == false)
					{
						// GroundDistance 를 기준으로 가장 가까운 곳으로 선정
						tempDistance = (double)(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self()).getGroundDistance(startLocation) + 0.5);

						if (tempDistance > 0 && tempDistance < closestDistance) {
							closestBaseLocation = startLocation;
							closestDistance = tempDistance;
						}
					}
				}

				if (closestBaseLocation != null) {
					// assign a scout to go scout it
					CommandUtil.move(currentScoutUnit, closestBaseLocation.getPosition());
					currentScoutTargetBaseLocation = closestBaseLocation;
				}
			}
		}
		// if we know where the enemy region is
		else 
		{
			// if scout is exist, move scout into enemy region
			if (currentScoutUnit != null) {
				
				currentScoutTargetBaseLocation = enemyBaseLocation;
				
				if (MyBotModule.Broodwar.isExplored(currentScoutTargetBaseLocation.getTilePosition()) == false) {
					
					currentScoutStatus = ScoutStatus.MovingToAnotherBaseLocation.ordinal();
					currentScoutTargetPosition = currentScoutTargetBaseLocation.getPosition();
					CommandUtil.move(currentScoutUnit, currentScoutTargetPosition);
					
				}
				else {
					currentScoutStatus = ScoutStatus.MoveAroundEnemyBaseLocation.ordinal();
					
					if (currentScoutUnit.isUnderAttack() || BattleManager.shouldRetreat(currentScoutUnit)) {
						this.scout(true);
					} else {
						boolean isWorkerInRange = false;
						for (Unit unit : currentScoutUnit.getUnitsInRadius(CommandUtil.UNIT_RADIUS)) {
							if (unit.getPlayer() == MyBotModule.Broodwar.enemy() &&
									unit.getType().isWorker()) {
								if (currentScoutUnit.getType() == UnitType.Protoss_Probe && 
										unit.getOrderTarget() != null && unit.getOrderTarget().getID() == currentScoutUnit.getID()) {
									this.scout(true);
									break;
								} else {
									commandUtil.attackMove(currentScoutUnit, unit.getPosition());
									isWorkerInRange = true;
									break;
								}
							}
						}
						if (!isWorkerInRange) {
							this.scout(false);
						}
					}
				}
			}
		}
	}
	
	public void scout(boolean isRunAway) {
		for (String base : ProtossBasicBuildPosition.Instance().getScoutPositions().keySet()) {
			TilePosition tilePosition = ProtossBasicBuildPosition.Instance().getScoutPositions().get(base);
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			if (tilePosition.equals(enemyBaseLocation.getTilePosition())) {
				continue;
			}
			if (currentScoutUnit.getDistance(tilePosition.toPosition()) > 50 && !currentScoutUnit.isMoving()) {
				if (isRunAway) {
					currentScoutUnit.move(tilePosition.toPosition(), true);
				} else {
					currentScoutUnit.attack(tilePosition.toPosition(), true);
				}
			}
		}
	}
	
	/// 정찰 유닛을 리턴합니다
	public Unit getScoutUnit()
	{
		return currentScoutUnit;
	}

	// 정찰 상태를 리턴합니다
	public int getScoutStatus()
	{
		return currentScoutStatus;
	}

	/// 정찰 유닛의 이동 목표 BaseLocation 을 리턴합니다
	public BaseLocation getScoutTargetBaseLocation()
	{
		return currentScoutTargetBaseLocation;
	}
}