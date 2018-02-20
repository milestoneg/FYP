import java.util.LinkedList;

public class Machines {
	LinkedList<Job> Machine_1_JobList;
	LinkedList<Job> Machine_2_JobList;
	static LinkedList<Job> Machine1_processed = new LinkedList<>();
	static LinkedList<Job> Machine2_processed = new LinkedList<>();
	
	public Machines(LinkedList<Job> Machine_1_JobList, LinkedList<Job> Machine_2_JobList) {
		this.Machine_1_JobList = new LinkedList<>(Machine_1_JobList);
		this.Machine_2_JobList = new LinkedList<>(Machine_2_JobList);
	}

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
	
	public void startMachines() {
		Thread machine_1 = new Thread(new MachineEmulator(1, Machine_1_JobList));
		Thread machine_2 = new Thread(new MachineEmulator(2, Machine_2_JobList));
		machine_1.start();
		machine_2.start();
	}
}
