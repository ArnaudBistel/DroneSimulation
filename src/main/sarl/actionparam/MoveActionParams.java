package actionparam;

import java.util.UUID;

public class MoveActionParams extends ActionParams {

	private UUID droneId;
	private int x;
	private int y;

	public MoveActionParams (int x, int y, UUID droneId) {
		this.x = x;
		this.y = y;
		this.droneId = droneId;
	}

	public UUID getDroneId() {
		return this.droneId;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
}
