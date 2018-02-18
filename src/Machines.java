import java.util.LinkedList;

public class Machines {
	static LinkedList<Job> Machine1_processed = new LinkedList<>();
	static LinkedList<Job> Machine2_processed = new LinkedList<>();

	public static LinkedList<Job> getMachine_processed(int machineNo) {
		if (machineNo == 1) {
			return Machine1_processed;
		} else {
			return Machine2_processed;
		}
	}
	
	public static LinkedList<Job> getAnotherMachine_processed(int machineNo) {
		if (machineNo == 1) {
			return Machine2_processed;
		} else {
			return Machine1_processed;
		}
	}
}
