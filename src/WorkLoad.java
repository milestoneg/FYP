


public class WorkLoad {
	private int MachineNo;
	private int ProcessingTime;
	
	public WorkLoad() {
		
	}
	public WorkLoad(int MachineNo, int ProcessingTime) {
		this.MachineNo = MachineNo;
		this.ProcessingTime = ProcessingTime;
	}

	public int getMachineNo() {
		return MachineNo;
	}

	public int getProcessingTime() {
		return ProcessingTime;
	}

	public void setProcessingTime(int processingTime) {
		ProcessingTime = processingTime;
	}
	
	public WorkLoad Copy() {
		WorkLoad newWorkload = new WorkLoad();
		newWorkload.MachineNo = this.MachineNo;
		newWorkload.ProcessingTime = this.ProcessingTime;
		return newWorkload;
	}
	
}
