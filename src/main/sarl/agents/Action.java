package agents;

import java.util.List;
import java.util.UUID;

import actionparam.ActionParams;
import actionparam.MoveActionParams;

public class Action {
	
	ActionType type;
	ActionParams params;
	
	Action(ActionType type, List<Object> params){
		this.type = type;
		this.processParams(params);
	}
	
	public void processParams(List<Object> params) {
		switch (this.type) {
			case MOVE :
				int x = (int) params.get(0);
				int y = (int) params.get(1);
				UUID droneId = (UUID) params.get(2);
				this.params = new MoveActionParams(x, y, droneId);
				break;
		}
	}
	
}