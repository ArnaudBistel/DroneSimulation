package utils.variables;

public class SimulationParameters {

	public static final double mapRatio = 15;
	public static final int mapHeight = 791;
	public static final int mapWidth = 1020;
	public static final int perceptionMapCellSize = 50;
	public static final double DRONE_MAX_ENERGY = 1628.0;
	
	//en france le prix moyen du wh chez le particulier est de 0,1893€
	public static final double COUT_WH = 0.0001893;
	public static final double COUT_DRONE = 5000; //a déterminer selon la réponse de la boite
	
	//multi test
	public static final int MT_NB_STEP = 25;
	public static final int MT_NB_TEST_BY_STEP = 100;
	public static final double MT_TEMP_TRESHOLD = 0.1;
	public static final int MT_NB_EPISODE_Q_LEARNING = 20;
}
