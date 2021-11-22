package gui;

public enum DroneState {
	DRONE_EMPTY("EMPTY"),
	DRONE_LOADED("LOADED");
	
	private final String state;
	
	DroneState(final String state) {
		this.state = state;
	}
	
	public String getPath() {
		return this.state;
	}
}
