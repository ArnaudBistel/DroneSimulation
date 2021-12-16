package actionparam;

import java.util.UUID;

public class DepositActionParams extends ActionParams {

	private int x;
	private int y;

	public DepositActionParams (int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
}
