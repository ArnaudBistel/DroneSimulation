package actionparam;

import gui.DroneBody;

public class BackToWarehouseActionParams extends ActionParams {

	private DroneBody droneBody;

	public BackToWarehouseActionParams (DroneBody dBody) {
		this.droneBody = dBody;
	}

	public DroneBody getDroneBody() {
		return this.droneBody;
	}
	
}
