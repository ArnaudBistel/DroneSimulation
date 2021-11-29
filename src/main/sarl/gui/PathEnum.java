package gui;

public enum PathEnum {
	DRONE_EMPTY("/resources/img/drone_green.png"),
	DRONE_LOADED("/resources/img/drone_blue.png"),
	WAREHOUSE("/resources/img/warehouse_rose.png");
	
	private final String path;
	
	PathEnum(final String path) {
		this.path = path;
	}
	
	public String getPath() {
		return this.path;
	}
}
