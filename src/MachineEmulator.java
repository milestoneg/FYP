import java.util.LinkedList;
import java.util.List;

public class MachineEmulator extends Thread {
	private LinkedList<Job> JobList;
	private int MachineNo;
	long processing_time;
	double total_workload = 0;
	double EnergyConsumption;
	LinkedList<Double> EachJob_EnergyConsumption = new LinkedList<>();

	public MachineEmulator(int MachineNo, List<Job> JobList) {
		this.JobList = (LinkedList<Job>) JobList;
		this.MachineNo = MachineNo;
	}

	@Override
	public void run() {
		long start_time = System.currentTimeMillis();
		for (Job job : JobList) {
			if (job.getWorkLoadList().get(0).getMachineNo() == MachineNo) {
				try {
					Thread.sleep(job.getWorkLoadList().get(0).getProcessingTime());
					total_workload += job.getWorkLoadList().get(0).getProcessingTime();
					Machines.getMachine_processed(MachineNo).add(job);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if (Machines.getAnotherMachine_processed(MachineNo).contains(job)) {
					try {
						Thread.sleep(job.getWorkLoadList().get(1).getProcessingTime());
						total_workload += job.getWorkLoadList().get(1).getProcessingTime();
						Machines.getMachine_processed(MachineNo).add(job);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					while (!Machines.getAnotherMachine_processed(MachineNo).contains(job)) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					try {
						Thread.sleep(job.getWorkLoadList().get(1).getProcessingTime());
						total_workload += job.getWorkLoadList().get(1).getProcessingTime();
						Machines.getMachine_processed(MachineNo).add(job);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		long finish_time = System.currentTimeMillis();
		processing_time = finish_time - start_time;
		EnergyConsumption = Math.pow(total_workload / processing_time, 2)*processing_time;
		System.out.println("machine " + MachineNo + " finished in:" + processing_time + "ms");
		System.out.println("machine " + MachineNo + " energy consumtion is:" + EnergyConsumption);
	}

}
