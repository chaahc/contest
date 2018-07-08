public enum BattleGroupType {
	FRONT_GROUP(0), SUB_GROUP(1), DEFENCE_GROUP(2), SCOUT_GROUP(3);
	
	private int groupType;
	
	BattleGroupType(int groupType) {
		this.groupType = groupType;
	}
	
	public int getValue() {
		return this.groupType;
	}
}
