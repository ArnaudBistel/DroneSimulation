package agents;

public enum ActionType {
	NEWDRONE("NEWDRONE"),
	MOVE("MOVE"),
	DEPOSIT("DEPOSIT");
	
	private final String type;
	
	ActionType(final String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
}
