package agents;

public enum AgentType {

	DRONE("DRONE"),
	WAREHOUSE("WAREHOUSE");
	
	private final String type;
	
	AgentType(final String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
}
