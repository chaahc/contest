import java.util.List;

import bwapi.Position;
import bwapi.Region;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class FastCarrierBattleOrder extends BattleOrder {
	public static final int TOTAL_RADIUS = 1000;
	public static final int BASE_RADIUS = 500;
	public static final int CORSAIR_RADIUS = 100;
	public static final int STUCK_RADIUS = 50;
	
	@Override
	public void execute() {
		if (BattleManager.instance().getBattleMode() != BattleManager.BattleMode.DEFENCE) {
			this.changeBattleMode();
		}
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			System.out.println("battle mode : " + BattleManager.instance().getBattleMode());
		}
		super.moveStuckDragoon();
		this.observing();
		super.detectEnemyInSelf();
		this.enemyExpansionAttack();
		this.onewayAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		this.totalAttack(InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy()));
		this.trainWeaponAttack(UnitType.Protoss_Carrier);
		super.darkTemplarAttack();
		super.highTemplarAttack();
		super.archonAttack();
		super.arbiterAttack();
	}
	
	@Override
	protected void changeBattleMode() {
		int marineUnitCount = InformationManager.Instance().getNumUnits(UnitType.Terran_Marine, MyBotModule.Broodwar.enemy());
		int medicUnitCount = InformationManager.Instance().getNumUnits(UnitType.Terran_Medic, MyBotModule.Broodwar.enemy());
		int vultureUnitCount = InformationManager.Instance().getNumUnits(UnitType.Terran_Vulture, MyBotModule.Broodwar.enemy());
		if (marineUnitCount > 4 || medicUnitCount > 0 || vultureUnitCount == 0) {
			BattleManager.instance().setTerranType(BattleManager.TerranType.BIONIC);
		} else {
			BattleManager.instance().setTerranType(BattleManager.TerranType.MECHANIC);
		}
		
		int enemyTankCount = InformationManager.Instance().getNumUnits(UnitType.Terran_Siege_Tank_Tank_Mode, MyBotModule.Broodwar.enemy());
		int enemySiegeCount = InformationManager.Instance().getNumUnits(UnitType.Terran_Siege_Tank_Siege_Mode, MyBotModule.Broodwar.enemy());
		int enemyGoliathCount = InformationManager.Instance().getNumUnits(UnitType.Terran_Goliath, MyBotModule.Broodwar.enemy());
		
		int selfZealotCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Zealot, MyBotModule.Broodwar.self());
		int selfDragoonCount = InformationManager.Instance().getNumUnits(UnitType.Protoss_Dragoon, MyBotModule.Broodwar.self());
		
		if (MyBotModule.Broodwar.self().supplyTotal() > 380 &&
				MyBotModule.Broodwar.self().supplyUsed()+10 >= MyBotModule.Broodwar.self().supplyTotal()) {
			BattleManager.instance().setBattleMode(BattleManager.BattleMode.ELEMINATE);
		} else {
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			if (enemyBaseLocation != null) {
				int enemyBuildingCount = 0;
				for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(enemyBaseLocation.getPosition(), TOTAL_RADIUS)) {
					if (unit.getPlayer() == MyBotModule.Broodwar.enemy() &&
							unit.getType().isBuilding()) {
						enemyBuildingCount++;
					}
				}
				int selfUnitCount = selfZealotCount + selfDragoonCount;
				int gap = selfUnitCount - (enemyTankCount + enemySiegeCount + enemyGoliathCount + marineUnitCount + medicUnitCount);
				if (gap > 0 || enemyBuildingCount > 0) {
					if (InformationManager.Instance().selfPlayer.supplyUsed() > 300) { 
						BattleManager.instance().setBattleMode(BattleManager.BattleMode.ONEWAY_ATTACK);
					} else if (InformationManager.Instance().selfPlayer.supplyUsed() > 200 || selfUnitCount > 15 || 
							BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Dark_Templar).getUnitCount() > 1) {
						BattleManager.instance().setBattleMode(BattleManager.BattleMode.TOTAL_ATTACK);
					} else {
						BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
					}
				} else {
					BattleManager.instance().setBattleMode(BattleManager.BattleMode.WAIT);
				}
			}
		}
	}
	
	@Override
	protected void observing() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		BattleUnitGroup observerGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(UnitType.Protoss_Observer);	
		if (observerGroup.getUnitCount() > 0) {
			boolean needDragoonFollower = true;
			BattleUnit observerLeader = observerGroup.getLeader();
			for (int unitId : observerGroup.battleUnits.keySet()) {
				BattleUnit observer = observerGroup.battleUnits.get(unitId);
				if (observerLeader != null && observerLeader.getUnitId() == observer.getUnitId()) {
					BaseLocation enemySecondExpansionLocation = InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.self());
					observerLeader.move(enemySecondExpansionLocation.getPosition());
				} else if (needDragoonFollower){
					BattleUnit dragoonLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
					this.unitFollow(observer, dragoonLeader);
					needDragoonFollower = false;
				} else {
					for (String base : ProtossBasicBuildPosition.Instance().getScoutPositions().keySet()) {
						TilePosition tilePosition = ProtossBasicBuildPosition.Instance().getScoutPositions().get(base);
						if (observer.getUnit().getDistance(tilePosition.toPosition()) > 50 && !observer.getUnit().isMoving()) {
							observer.getUnit().move(tilePosition.toPosition(), true);
						}
					}
				}
			}
		}
	}
	
	private void doByBattleUnitStatus(UnitType unitType, BattleGroupType battleGroupType, Position leaderTargetPosition) {
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(unitType).get(battleGroupType.getValue());
		if (battleUnitGroup.getUnitCount() > 0) {
			for (int unitId : battleUnitGroup.battleUnits.keySet()) {
				BattleUnit battleUnit = battleUnitGroup.battleUnits.get(unitId);
				BattleUnit leader = battleUnitGroup.getLeader();
				if (leader != null) {
					leader = BattleManager.changeReader(leader, battleUnitGroup);
				}
				battleUnit.setLastTargetPosition(leaderTargetPosition);
				battleUnit.setBattleUnitStatus(BattleUnitStatus.ATTACK);
				
				Unit tank = null;
				int closestDistance = Integer.MAX_VALUE;
				int selfUnitCount = 0;
				int enemyUnitCount = 0;
				int siegeUnitCount = 0;
				int tankUnitCount = 0;
				int vultureUnitCount = 0;
				int goliathUnitCount = 0;
				int marineUnitCount = 0;
				int medicUnitCount = 0;
				int zealotUnitCount = 0;
				int dragoonUnitCount = 0;
				for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(battleUnit.getUnit().getPosition(), CommandUtil.UNIT_RADIUS)) {
					if (unit.getPlayer() == MyBotModule.Broodwar.enemy()) {
						enemyUnitCount++;
						if (unit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode ||
								unit.getType() == UnitType.Terran_Siege_Tank_Tank_Mode) {
							int distance = battleUnit.getUnit().getDistance(unit.getPosition());
							if (tank == null || closestDistance >= distance) {
								closestDistance = distance;
								tank = unit;
								battleUnit.setBattleUnitStatus(BattleUnitStatus.UNIT_ATTACK);
								battleUnit.setLastTargetUnit(tank);
							}
							if (unit.isSieged()) {
								siegeUnitCount++;
							} else {
								tankUnitCount++;
							}
						} else if (unit.getType() == UnitType.Terran_Vulture) {
							vultureUnitCount++;
						} else if (unit.getType() == UnitType.Terran_Goliath) {
							goliathUnitCount++;
						} else if (unit.getType() == UnitType.Terran_Marine) {
							marineUnitCount++;
						} else if (unit.getType() == UnitType.Terran_Medic) {
							medicUnitCount++;
						}
					} else {
						selfUnitCount++;
						if (unit.getType() == UnitType.Protoss_Zealot) {
							zealotUnitCount++;
						} else if (unit.getType() == UnitType.Protoss_Dragoon) {
							dragoonUnitCount++;
						}
						
					}
				}
				
				if (enemyUnitCount >= selfUnitCount) {
					if (siegeUnitCount > 3 ||
							(goliathUnitCount + vultureUnitCount + tankUnitCount > (zealotUnitCount + dragoonUnitCount)*1.5) ||
							(marineUnitCount+medicUnitCount > (zealotUnitCount + dragoonUnitCount)*2.5)) {
						BaseLocation selfMainBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self());
						battleUnit.setLastTargetPosition(selfMainBaseLocation.getPosition());
						battleUnit.setBattleUnitStatus(BattleUnitStatus.MOVE);
					}
				} else {
					if (siegeUnitCount > 3 && selfUnitCount < enemyUnitCount * 1.5) {
						BaseLocation selfMainBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self());
						battleUnit.setLastTargetPosition(selfMainBaseLocation.getPosition());
						battleUnit.setBattleUnitStatus(BattleUnitStatus.MOVE);
					} else {
						if (unitType == UnitType.Protoss_Dragoon) {
							if (vultureUnitCount >= 5 ||
								(goliathUnitCount != 0 && goliathUnitCount > 0)) {
									battleUnit.setLastTargetPosition(leaderTargetPosition);
									battleUnit.setBattleUnitStatus(BattleUnitStatus.ATTACK);
							} 
						} 
					}
				}
				
				switch (battleUnit.getBattleUnitStatus()) {
					case HOLD:
						battleUnit.getUnit().holdPosition();
						break;
					case MOVE:
						CommandUtil.rightClick(battleUnit.getUnit(), battleUnit.getLastTargetPosition());
						break;
					case ATTACK:
						if (battleUnit.unitType == UnitType.Protoss_Dragoon) {
							CommandUtil.patrolMove(battleUnit.getUnit(), new Position(battleUnit.getLastTargetPosition().getX()+10, battleUnit.getLastTargetPosition().getY()+10));
							CommandUtil.patrolMove(battleUnit.getUnit(), battleUnit.getLastTargetPosition());
						} else {
							CommandUtil.attackMove(battleUnit.getUnit(), battleUnit.getLastTargetPosition());
						}
						break;
					case UNIT_ATTACK:
						if (battleUnit.unitType == UnitType.Protoss_Dragoon) {
							int distance = battleUnit.getUnit().getDistance(battleUnit.getLastTargetUnit());
							if (distance < UnitType.Terran_Siege_Tank_Siege_Mode.groundWeapon().minRange()) {
								CommandUtil.attackUnit(battleUnit.getUnit(), battleUnit.getLastTargetUnit());
							} else {
								if (battleUnit.getLastTargetUnit() != null) {
									CommandUtil.rightClick(battleUnit.getUnit(), battleUnit.getLastTargetUnit().getPosition());
								}
							}
						} else {
							CommandUtil.attackUnit(battleUnit.getUnit(), battleUnit.getLastTargetUnit());
						}
						break;
					case PATROL:
						CommandUtil.patrolMove(battleUnit.getUnit(), battleUnit.getLastTargetPosition());
						break;
				}
			}
		}
	}
	
	@Override
	protected void enemyExpansionAttack() {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.WAIT) {
			BaseLocation enemyBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
			if (enemyBaseLocation != null) {
				if (MyBotModule.Broodwar.getFrameCount() < 7200) {
					this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP, enemyBaseLocation.getPosition());
					this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP, enemyBaseLocation.getPosition());
					this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP, enemyBaseLocation.getPosition());
					this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP, enemyBaseLocation.getPosition());
					this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.DEFENCE_GROUP, enemyBaseLocation.getPosition());
					this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.DEFENCE_GROUP, enemyBaseLocation.getPosition());
				} else {
					BattleUnit subLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.SUB_GROUP.getValue()).getLeader();
					if (subLeader != null && subLeader.getUnit().isUnderAttack() && subLeader.getUnit().isAttacking()) {
						this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP, subLeader.getUnit().getPosition());
						this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP, subLeader.getUnit().getPosition());
					} else {
						this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP, ProtossBasicBuildPosition.Instance().getEnemyDefencePosition(enemyBaseLocation.getTilePosition()).toPosition());
						this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP, ProtossBasicBuildPosition.Instance().getEnemyDefencePosition(enemyBaseLocation.getTilePosition()).toPosition());
					}
					BattleUnit frontLeader = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue()).getLeader();
					if (frontLeader != null && frontLeader.getUnit().isUnderAttack() && frontLeader.getUnit().isAttacking()) {
						this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP, frontLeader.getUnit().getPosition());
						this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP, frontLeader.getUnit().getPosition());
					} else {
						this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP, ProtossBasicBuildPosition.Instance().getEnemyDefencePosition(enemyBaseLocation.getTilePosition()).toPosition());
						this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP, ProtossBasicBuildPosition.Instance().getEnemyDefencePosition(enemyBaseLocation.getTilePosition()).toPosition());
					}
					
					Chokepoint selfChokepoint = InformationManager.Instance().getSecondChokePoint(MyBotModule.Broodwar.self());					
					this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.DEFENCE_GROUP, selfChokepoint.getCenter());
					this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.DEFENCE_GROUP, selfChokepoint.getCenter());
				}
			} else {
				Chokepoint selfChokepoint = InformationManager.Instance().getSecondChokePoint(MyBotModule.Broodwar.self());
				this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP, selfChokepoint.getCenter());
				this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP, selfChokepoint.getCenter());
				this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP, selfChokepoint.getCenter());
				this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP, selfChokepoint.getCenter());
				this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.DEFENCE_GROUP, selfChokepoint.getCenter());
				this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.DEFENCE_GROUP, selfChokepoint.getCenter());
			}
		} 
	}
	
	@Override
	protected void onewayAttack(BaseLocation enemyLocation) {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.ONEWAY_ATTACK) {
			if (enemyLocation != null) {
				TilePosition enemyBasePosition = ProtossBasicBuildPosition.Instance().getEnemyBase(enemyLocation.getTilePosition());
				if (enemyBasePosition == null) {
					enemyBasePosition = enemyLocation.getTilePosition();
				}
				Position targetUnit = null;
				for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(enemyBasePosition.toPosition(), TOTAL_RADIUS)) {
					if (unit.getPlayer() == MyBotModule.Broodwar.enemy() &&
							unit.getType().isBuilding()) {
						targetUnit = unit.getPosition();
						break;
					}
				}
				if (targetUnit == null) {
					this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP, enemyBasePosition.toPosition());
					this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP, enemyBasePosition.toPosition());
					this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP, enemyBasePosition.toPosition());
					this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP, enemyBasePosition.toPosition());
				} else {
					this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP, targetUnit);
					this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP, targetUnit);
					this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP, targetUnit);
					this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP, targetUnit);
				}
			}
		}
	}
	
	@Override
	protected void totalAttack(BaseLocation enemyLocation) {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;	
		}
		
		if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.TOTAL_ATTACK) {
			if (enemyLocation != null) {
				BaseLocation selfMainBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self());
				TilePosition enemyBasePosition = ProtossBasicBuildPosition.Instance().getEnemyBase(enemyLocation.getTilePosition());
				if (enemyBasePosition == null) {
					enemyBasePosition = enemyLocation.getTilePosition();
				}
				Position closestEnemyBase = null;
				double closestDistance = Integer.MAX_VALUE;
				for (BaseLocation baseLocation : InformationManager.Instance().getOccupiedBaseLocations(MyBotModule.Broodwar.enemy())) {
					if (baseLocation != null) {
						boolean isEnemyInOccupied = false;
						List<Unit> list = MyBotModule.Broodwar.getUnitsInRadius(baseLocation.getPosition(), BASE_RADIUS);
						for (Unit unit : list) {
							if (unit.getPlayer() == InformationManager.Instance().enemyPlayer) {
								isEnemyInOccupied = true;
								break;
							} 
						}
						double distance = baseLocation.getDistance(selfMainBaseLocation.getPosition());
						if (isEnemyInOccupied) {
							if (closestEnemyBase == null || closestDistance > distance) {
								closestEnemyBase = baseLocation.getPosition();
								closestDistance = distance;
							}
						}
					}
				}
				if (closestEnemyBase == null) {
					closestEnemyBase = enemyBasePosition.toPosition();
				}
				this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.FRONT_GROUP, closestEnemyBase.getPoint());
				this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.FRONT_GROUP, closestEnemyBase.getPoint());
				this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.SUB_GROUP, closestEnemyBase.getPoint());
				this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.SUB_GROUP, closestEnemyBase.getPoint());
				if (ScoutManager.Instance().getScoutUnit() != null && ScoutManager.Instance().getScoutUnit().exists()) {
					Region region = ScoutManager.Instance().getScoutUnit().getRegion();
					this.doByBattleUnitStatus(UnitType.Protoss_Zealot, BattleGroupType.DEFENCE_GROUP, new Position(region.getBoundsLeft(), region.getBoundsTop()));
					this.doByBattleUnitStatus(UnitType.Protoss_Dragoon, BattleGroupType.DEFENCE_GROUP, new Position(region.getBoundsRight(), region.getBoundsBottom()));	
				}
			}
		}
	}
	
	@Override
	protected void trainWeaponAttack(UnitType unitType) {
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		BattleUnitGroup battleUnitGroup = BattleUnitGroupManager.instance().getBattleUnitGroup(unitType);
		if (battleUnitGroup.getUnitCount() > 0) {
			for (int unitId : battleUnitGroup.battleUnits.keySet()) {
				BattleUnit battleUnit = battleUnitGroup.battleUnits.get(unitId);
				
				BaseLocation enemyMainBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.enemy());
				if (enemyMainBaseLocation != null) {
					TilePosition enemyMineralPosition = ProtossBasicBuildPosition.Instance().getEnemyMineralPosition(enemyMainBaseLocation.getTilePosition());
					if (enemyMineralPosition != null) {
						if (battleUnit.getUnit().getTilePosition().getDistance(enemyMineralPosition) < 5 &&
								BattleManager.instance().getBattleMode() != BattleManager.BattleMode.TOTAL_ATTACK) {
							boolean isMainBaseAlive = false;
							for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(battleUnit.getUnit().getPosition(), CommandUtil.UNIT_RADIUS)) {
								if (unit.getPlayer() == MyBotModule.Broodwar.enemy() && 
										(unit.getType() == UnitType.Terran_SCV ||
										unit.getType() == UnitType.Terran_Command_Center) &&
										unit.exists()) {
									System.out.println(unitId + "main base alive");
									isMainBaseAlive = true;
									break;
								}
							}
							
							if (isMainBaseAlive) {
								System.out.println(unitId + "hold");
								if (!battleUnit.getUnit().isHoldingPosition()) {
									battleUnit.getUnit().holdPosition();			
								}
							} else {
								System.out.println(unitId + "total attack");
								BattleManager.instance().setBattleMode(BattleManager.BattleMode.TOTAL_ATTACK);
							}
						} else {
							if (BattleManager.instance().getBattleMode() == BattleManager.BattleMode.DEFENCE || BattleManager.instance().getBattleMode() == BattleManager.BattleMode.WAIT) {
								System.out.println(unitId + "move to mineral");
								if (BattleManager.instance().getTerranType() == BattleManager.TerranType.BIONIC) {
									if (battleUnit.getUnit().isUnderAttack()) {
										BaseLocation selfMainBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self());
										CommandUtil.rightClick(battleUnit.getUnit(), selfMainBaseLocation.getPosition());
									} else {
										Unit enemy = null;
										for (Unit unit : battleUnit.getUnit().getUnitsInRadius(UnitType.Protoss_Carrier.groundWeapon().maxRange())) {
											if (unit.getPlayer() == MyBotModule.Broodwar.enemy()) {
												if (unit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode ||
														unit.getType() == UnitType.Terran_Siege_Tank_Tank_Mode ||
														unit.getType() == UnitType.Terran_SCV) {
													enemy = unit;
													break;
												}
											}
										}
										if (enemy != null) {
											if (battleUnitGroup.getUnitCount() > 3 && battleUnit.getUnit().getInterceptorCount() > 4) {
												CommandUtil.attackUnit(battleUnit.getUnit(), enemy);
											}
										} else {
											if (battleUnitGroup.getUnitCount() > 3 && battleUnit.getUnit().getInterceptorCount() > 4) {
												System.out.println(unitId + "attack following dragoon");
												BattleUnitGroup dragoonGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue());
												BattleUnit leader = dragoonGroup.getLeader();
												if (leader != null) {
													battleUnit.getUnit().attack(leader.getUnit().getPosition());
												}
											}
										}
									}
								} else {
									if (ProtossBasicBuildPosition.Instance().isEnemyOpposite(enemyMainBaseLocation.getTilePosition())) {
										System.out.println(unitId + "opposite");
										BaseLocation enemySecondExpansionLocation = InformationManager.Instance().getSecondExpansionLocation(MyBotModule.Broodwar.enemy());
										battleUnit.getUnit().move(enemySecondExpansionLocation.getPosition(), true);
										battleUnit.getUnit().move(enemyMineralPosition.toPosition(), true);
									} else {
										System.out.println(unitId + "close");
										CommandUtil.move(battleUnit.getUnit(), enemyMineralPosition.toPosition());
									}
								}
							} else {
								System.out.println(unitId + "attack following dragoon");
								BattleUnitGroup dragoonGroup = BattleUnitGroupManager.instance().getBattleUnitGroups(UnitType.Protoss_Dragoon).get(BattleGroupType.FRONT_GROUP.getValue());
								BattleUnit leader = dragoonGroup.getLeader();
								if (leader != null) {
									battleUnit.getUnit().attack(leader.getUnit().getPosition());
								}
							}
						}
					} 
					((WeaponTrainable) battleUnit).train();
				}
			}
		} 
	}
}