import java.util.LinkedList;
import java.util.List;

public class MachineEmulator extends Thread{
	private LinkedList<Job> JobList;
	private int MachineNo;
	
	public MachineEmulator(int MachineNo, List<Job> JobList) {
		this.JobList = (LinkedList<Job>) JobList;
		this.MachineNo = MachineNo;
	}
	
	@Override
	public void run() {
		for(Job job : JobList) {
			if(job.getWorkLoadList().get(0).getMachineNo() == MachineNo) {
				try {
					Thread.sleep(job.getWorkLoadList().get(0).getProcessingTime());
					Machines.getMachine_processed(MachineNo).add(job);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				if(Machines.getAnotherMachine_processed(MachineNo).contains(job)) {
					try {
						Thread.sleep(job.getWorkLoadList().get(1).getProcessingTime());
						Machines.getMachine_processed(MachineNo).add(job);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					while(!Machines.getAnotherMachine_processed(MachineNo).contains(job)) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
	}
}
