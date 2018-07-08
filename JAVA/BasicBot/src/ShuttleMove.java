import java.util.List;

import bwapi.Unit;

public class ShuttleMove implements BattleSingleOrder{
	private int unitId;
	private Unit unit;
	private List<MovePosition> movePositions;
	
	public ShuttleMove(Unit unit, List<MovePosition> movePositions) {
		this.unitId = unit.getID();
		this.unit = unit;
		this.movePositions = movePositions;
	}
	
	public int getUnitId() {
		return this.unitId;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		int remainMovePosition = this.movePositions.size();
		for (MovePosition movePosition : this.movePositions) {
			if (movePosition.getMoveStatus() == MoveStatus.WAIT) {
				CommandUtil.move(this.unit, movePosition.getPosition());
				movePosition.setMoveStatus(MoveStatus.ON_THE_WAY);
				break;
			} else if (movePosition.getMoveStatus() == MoveStatus.ON_THE_WAY) {
				System.out.println("ontheway, current position : " + this.unit.getPosition().toTilePosition() + ", target : " + movePosition.getPosition().toTilePosition());
				if (this.unit.isIdle()) {
					System.out.println("idle, current position : " + this.unit.getPosition().toTilePosition() + ", target : " + movePosition.getPosition().toTilePosition());
					CommandUtil.move(this.unit, movePosition.getPosition());
				}
				if (this.unit.getPosition().toTilePosition().equals(movePosition.getPosition().toTilePosition())) {
					System.out.println("reached, current position : " + this.unit.getPosition().toTilePosition() + ", target : " + movePosition.getPosition().toTilePosition());
					movePosition.setMoveStatus(MoveStatus.REACHED);
					remainMovePosition--;
				}
				break;
			} else if (movePosition.getMoveStatus() == MoveStatus.REACHED) {
				continue;
			}
		}
		if (remainMovePosition == 0) {
			System.out.println("removed");
			BattleManager.instance().removeBattleSingleOrder(this.unitId);
		}
	}
}
