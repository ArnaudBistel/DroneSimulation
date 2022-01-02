package actionparam;

import java.util.UUID;
import gui.DroneBody;

public class DepositActionParams extends ActionParams {

	private int x;
	private int y;
	private DroneBody body;

	public DepositActionParams (int x, int y, DroneBody body) {
		this.x = x;
		this.y = y;
		this.body = body;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}


	public DroneBody getBody() {
		return this.body;
	}
}
