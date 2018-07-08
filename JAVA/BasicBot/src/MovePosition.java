import bwapi.Position;

public class MovePosition {
	private Position position;
	private MoveStatus moveStatus;
	
	public MovePosition(Position position) {
		this.position = position;
		this.moveStatus = MoveStatus.WAIT;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public MoveStatus getMoveStatus() {
		return this.moveStatus;
	}
	
	public void setMoveStatus(MoveStatus moveStatus) {
		this.moveStatus = moveStatus;
	}
}
