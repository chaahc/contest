import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;
import bwta.Chokepoint;

/// 상황을 판단하여, 정찰, 빌드, 공격, 방어 등을 수행하도록 총괄 지휘를 하는 class <br>
/// InformationManager 에 있는 정보들로부터 상황을 판단하고, <br>
/// BuildManager 의 buildQueue에 빌드 (건물 건설 / 유닛 훈련 / 테크 리서치 / 업그레이드) 명령을 입력합니다.<br>
/// 정찰, 빌드, 공격, 방어 등을 수행하는 코드가 들어가는 class
public class StrategyManager {

	private static StrategyManager instance = new StrategyManager();

	/// static singleton 객체를 리턴합니다
	public static StrategyManager Instance() {
		return instance;
	}
	
	private CommandUtil commandUtil = new CommandUtil();
	
	private BuildOrderSet buildOrderSet;
	private boolean isInitialBuildOrderFinished;

	public StrategyManager() {
		isInitialBuildOrderFinished = false;
		buildOrderSet = BuildOrderSetManager.instance().getBuildOrderSet(this.chooseStrategy());
	}
	
	private StrategyType chooseStrategy() {
//		required to choose strategy
		return StrategyType.PROTOSS_BASIC;
	}

	/// 경기가 시작될 때 일회적으로 전략 초기 세팅 관련 로직을 실행합니다
	public void onStart() {
		this.buildOrderSet.executeInitialBuildOrder();
	}

	///  경기가 종료될 때 일회적으로 전략 결과 정리 관련 로직을 실행합니다
	public void onEnd(boolean isWinner) {
		
	}

	/// 경기 진행 중 매 프레임마다 경기 전략 관련 로직을 실행합니다
	public void update() {
		if (BuildManager.Instance().buildQueue.isEmpty()) {
			isInitialBuildOrderFinished = true;
		}
		
		if (isInitialBuildOrderFinished == false) {
			return;
		}
		executeWorkerTraining();
		executeSupplyManagement();
		
		this.buildOrderSet.executeBuildingUnitOrder();
		this.buildOrderSet.executeBattleUnitOrder();
		this.buildOrderSet.executeUpgradeOrder();
		executeBattle();
		executeDefense();
		executeObservatoring();
		trainWeapon(UnitType.Protoss_Carrier);
		trainWeapon(UnitType.Protoss_Reaver);
	}
	
	public void executeBattle() {
		if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.WAIT) {
			int zealotCount = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleUnitGroupManager.FRONT_GROUP).getUnitCount();
			int dragoonCount = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleUnitGroupManager.FRONT_GROUP).getUnitCount();
			if (zealotCount >= 12 && dragoonCount <= 10) {
				Chokepoint enemySecondChokePoint = InformationManager.Instance().getSecondChokePoint(MyBotModule.Broodwar.enemy());
				BattleManager.instance().totalAttack(enemySecondChokePoint.getCenter());
			}
			if (dragoonCount > 10) {
				BaseLocation enemyFirstExpansionLocation = InformationManager.Instance().getFirstExpansionLocation(MyBotModule.Broodwar.enemy());
				BattleManager.instance().totalAttack(enemyFirstExpansionLocation.getPosition());
			}
			BattleUnitGroup highTemplarGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_High_Templar);
			if (highTemplarGroup.getUnitCount() > 0) {
				for (int unitId : highTemplarGroup.battleUnits.keySet()) {
					HighTemplar highTemplar = (HighTemplar) highTemplarGroup.battleUnits.get(unitId);
					Unit enemyUnit = CommandUtil.getClosestUnit(highTemplar.getUnit());
					if (enemyUnit != null) {
						System.out.println("enemyUnit : " + enemyUnit.getType());
						if (enemyUnit.getType() == UnitType.Protoss_Probe ||
								enemyUnit.getType() == UnitType.Protoss_Zealot || 
								enemyUnit.getType() == UnitType.Protoss_Dragoon ||
								enemyUnit.getType() == UnitType.Protoss_Dark_Templar ||
								enemyUnit.getType() == UnitType.Protoss_Archon ||
								enemyUnit.getType() == UnitType.Terran_SCV ||
								enemyUnit.getType() == UnitType.Terran_Marine ||
								enemyUnit.getType() == UnitType.Terran_Firebat ||
								enemyUnit.getType() == UnitType.Terran_Medic ||
								enemyUnit.getType() == UnitType.Terran_Goliath ||
								enemyUnit.getType() == UnitType.Terran_Siege_Tank_Tank_Mode ||
								enemyUnit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode ||
								enemyUnit.getType() == UnitType.Terran_Wraith ||
								enemyUnit.getType() == UnitType.Zerg_Drone ||
								enemyUnit.getType() == UnitType.Zerg_Zergling ||
								enemyUnit.getType() == UnitType.Zerg_Hydralisk ||
								enemyUnit.getType() == UnitType.Zerg_Mutalisk ||
								enemyUnit.getType() == UnitType.Zerg_Guardian ||
								enemyUnit.getType() == UnitType.Zerg_Ultralisk) {
							highTemplar.psionicStorm(enemyUnit.getPosition());
						}
					}
					if (highTemplar.getUnit().getEnergy() < 50) {
						BattleManager.instance().addArchonCandidate(highTemplar);
					}
				}
				while(BattleManager.instance().getArchonCandidatesCount() > 1) {
					HighTemplar highTemplar = BattleManager.instance().removeArchonCandidate();
					HighTemplar targetHighTemplar = BattleManager.instance().removeArchonCandidate();
					highTemplar.archonWarp(targetHighTemplar.getUnit());
				}
			}
			BattleUnitGroup arbiterGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Arbiter);
			if (arbiterGroup.getUnitCount() > 1) {
				Chokepoint enemySecondChokePoint = InformationManager.Instance().getSecondChokePoint(MyBotModule.Broodwar.enemy());
				BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
				BaseLocation enemyFirstExpansionLocation = InformationManager.Instance().getFirstExpansionLocation(MyBotModule.Broodwar.enemy());
				Unit leader = arbiterGroup.getLeader();
				Arbiter arbiter = (Arbiter) arbiterGroup.battleUnits.get(leader.getID());
				arbiter.move(enemySecondChokePoint.getCenter());
				if (arbiter.unit.getEnergy() > 150) {
					arbiter.move(enemyBaseLocation.getPosition());
					if (arbiter.getUnit().getPosition() == enemyBaseLocation.getPosition()) {
						arbiter.recall(enemyFirstExpansionLocation.getPosition());
					}
				}
			}
//			BattleUnitGroup corsairGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Corsair);
//			if (corsairGroup.getUnitCount() > 0) {
//				for (int unitId : corsairGroup.battleUnits.keySet()) {
//					Corsair corsair = (Corsair) corsairGroup.battleUnits.get(unitId);
//					Unit enemyUnit = CommandUtil.getClosestUnit(corsair.getUnit());
//					if (enemyUnit != null) {
//						if (enemyUnit.getType() == UnitType.Protoss_Photon_Cannon ||
//								enemyUnit.getType() == UnitType.Terran_Bunker ||
//								enemyUnit.getType() == UnitType.Terran_Missile_Turret ||
//								enemyUnit.getType() == UnitType.Zerg_Sunken_Colony ||
//								enemyUnit.getType() == UnitType.Zerg_Spore_Colony) {
//							corsair.disruptionWeb(enemyUnit);
//						}
//					}
//				}
//			}
		}
	}
	
	
	public void executeDefense() {
		boolean isCloseToEnemy = false;
		for (Unit unit : MyBotModule.Broodwar.enemy().getUnits()) {
			if (InformationManager.Instance().selfPlayer.getStartLocation().getDistance(unit.getPosition().toTilePosition()) < 50) {
				BattleManager.instance().setBattleMode(BattleManager.BattleMode.DEFENCE);
				BattleManager.instance().closestAttack();
				isCloseToEnemy = true;
				System.out.println("DEFENCE MODE : " + InformationManager.Instance().selfPlayer.getStartLocation().getDistance(unit.getPosition().toTilePosition()));
			}
		}
		if (!isCloseToEnemy) {
			BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
		}
	}
	
	public void executeObservatoring() {
		Unit zealot = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Zealot).get(BattleUnitGroupManager.FRONT_GROUP).getLeader();
		if (zealot != null) {
			BattleUnitGroup observerGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer);
			if (observerGroup.getUnitCount() > 0) {
				for (int unitId : observerGroup.battleUnits.keySet()) {
					BattleUnit observer = observerGroup.battleUnits.get(unitId);
					if (observer != null) {
						if (!observer.getUnit().isFollowing() && observer.getUnit().canFollow(zealot)) {
							observer.getUnit().follow(zealot);
						}
					}					
				}
			}
		}
	}
	
	public void trainWeapon(UnitType unitType) {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(unitType);
		if (battleUnitGroup.getUnitCount() > 0) {
			for (int unitId : battleUnitGroup.battleUnits.keySet()) {
				WeaponTrainable battleUnit = (WeaponTrainable) battleUnitGroup.battleUnits.get(unitId);
				battleUnit.train();
			}
		}
	}

	// 일꾼 계속 추가 생산
	public void executeWorkerTraining() {
		if (MyBotModule.Broodwar.self().minerals() >= 50) {
			// workerCount = 현재 일꾼 수 + 생산중인 일꾼 수
			int workerCount = MyBotModule.Broodwar.self().allUnitCount(InformationManager.Instance().getWorkerType());

			for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
				if (unit.getType().isResourceDepot()) {
					if (unit.isTraining()) {
						workerCount += unit.getTrainingQueue().size();
					}
				}
			}

			if (workerCount < 30) {
				BuildingUnitManager.instance().trainBuildingUnit(UnitType.Protoss_Nexus, UnitType.Protoss_Probe);
			}
		}
	}

	// Supply DeadLock 예방 및 SupplyProvider 가 부족해질 상황 에 대한 선제적 대응으로서<br>
	// SupplyProvider를 추가 건설/생산한다
	public void executeSupplyManagement() {

		// BasicBot 1.1 Patch Start ////////////////////////////////////////////////
		// 가이드 추가 및 콘솔 출력 명령 주석 처리

		// InitialBuildOrder 진행중 혹은 그후라도 서플라이 건물이 파괴되어 데드락이 발생할 수 있는데, 이 상황에 대한 해결은 참가자께서 해주셔야 합니다.
		// 오버로드가 학살당하거나, 서플라이 건물이 집중 파괴되는 상황에 대해  무조건적으로 서플라이 빌드 추가를 실행하기 보다 먼저 전략적 대책 판단이 필요할 것입니다

		// BWAPI::Broodwar->self()->supplyUsed() > BWAPI::Broodwar->self()->supplyTotal()  인 상황이거나
		// BWAPI::Broodwar->self()->supplyUsed() + 빌드매니저 최상단 훈련 대상 유닛의 unit->getType().supplyRequired() > BWAPI::Broodwar->self()->supplyTotal() 인 경우
		// 서플라이 추가를 하지 않으면 더이상 유닛 훈련이 안되기 때문에 deadlock 상황이라고 볼 수도 있습니다.
		// 저그 종족의 경우 일꾼을 건물로 Morph 시킬 수 있기 때문에 고의적으로 이런 상황을 만들기도 하고, 
		// 전투에 의해 유닛이 많이 죽을 것으로 예상되는 상황에서는 고의적으로 서플라이 추가를 하지 않을수도 있기 때문에
		// 참가자께서 잘 판단하셔서 개발하시기 바랍니다.
		
		// 1초에 한번만 실행
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}

		// 게임에서는 서플라이 값이 200까지 있지만, BWAPI 에서는 서플라이 값이 400까지 있다
		// 저글링 1마리가 게임에서는 서플라이를 0.5 차지하지만, BWAPI 에서는 서플라이를 1 차지한다
		if (MyBotModule.Broodwar.self().supplyTotal() <= 400) {

			// 서플라이가 다 꽉찼을때 새 서플라이를 지으면 지연이 많이 일어나므로, supplyMargin (게임에서의 서플라이 마진 값의 2배)만큼 부족해지면 새 서플라이를 짓도록 한다
			// 이렇게 값을 정해놓으면, 게임 초반부에는 서플라이를 너무 일찍 짓고, 게임 후반부에는 서플라이를 너무 늦게 짓게 된다
			int supplyMargin = 12;

			// currentSupplyShortage 를 계산한다
			int currentSupplyShortage = MyBotModule.Broodwar.self().supplyUsed() + supplyMargin - MyBotModule.Broodwar.self().supplyTotal();

			if (currentSupplyShortage > 0) {
				
				// 생산/건설 중인 Supply를 센다
				int onBuildingSupplyCount = 0;
				onBuildingSupplyCount += ConstructionManager.Instance().getConstructionQueueItemCount(
						InformationManager.Instance().getBasicSupplyProviderUnitType(), null)
						* InformationManager.Instance().getBasicSupplyProviderUnitType().supplyProvided();

				if (currentSupplyShortage > onBuildingSupplyCount) {
					// BuildQueue 최상단에 SupplyProvider 가 있지 않으면 enqueue 한다
					boolean isToEnqueue = true;
					if (!BuildManager.Instance().buildQueue.isEmpty()) {
						BuildOrderItem currentItem = BuildManager.Instance().buildQueue.getHighestPriorityItem();
						if (currentItem.metaType.isUnit() 
							&& currentItem.metaType.getUnitType() == UnitType.Protoss_Pylon) 
						{
							isToEnqueue = false;
						}
					}
					if (isToEnqueue) {
						// 주석처리
						BuildManager.Instance().buildQueue.queueAsHighestPriority(UnitType.Protoss_Pylon, true);
					}
				}
			}
		}

		// BasicBot 1.1 Patch End ////////////////////////////////////////////////		
	}
}