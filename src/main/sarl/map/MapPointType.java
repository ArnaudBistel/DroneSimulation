package map;

public enum MapPointType {
	
	CLIENT("CLIENT"),
	WAREHOUSE("WAREHOUSE");
	
	private final String type;
	
	MapPointType(final String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
}
