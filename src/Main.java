import java.io.IOException;
import java.util.LinkedList;

public class Main {

	public static void main(String[] args) throws IOException {
	JohnsonsRule JohnsonsRule = new JohnsonsRule();
	JohnsonsRule.Execute();
	LinkedList<Job> Johnson_machine1_List = JohnsonsRule.getMachine_1_JobList();
	LinkedList<Job> Johnson_machine2_List = JohnsonsRule.getMachine_2_JobList();
	
	for(Job job : Johnson_machine1_List) {
		System.out.println(job.getId());
	}
	System.out.println("-------------------------------");
	for(Job job : Johnson_machine2_List) {
		System.out.println(job.getId());
	}
	
	Machines Johnson_machines = new Machines(Johnson_machine1_List,Johnson_machine2_List);
	Johnson_machines.startMachines();
//	
//	DVS DVS = new DVS();
//	DVS.Execute();
//	LinkedList<Job> DVS_machine1_List = DVS.getMachine_1_JobList();
//	LinkedList<Job> DVS_machine2_List = DVS.getMachine_2_JobList();
//		for (Job job : DVS_machine1_List) {
//			System.out.println(job.getId());
//		}
//		System.out.println("-------------------------------");
//		for (Job job : DVS_machine2_List) {
//			System.out.println(job.getId());
//		}
//	Machines DVS_machines = new Machines(DVS_machine1_List, DVS_machine2_List);
//	System.out.println("-------------------------------");
//	DVS_machines.startMachines();
	}

}
