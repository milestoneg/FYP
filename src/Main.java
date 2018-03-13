import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
	Scanner intkb = new Scanner(System.in);
	System.out.println("Please input the deadline(ms):");
	int ddl = intkb.nextInt();
//	JohnsonsRule JohnsonsRule = new JohnsonsRule();
//	JohnsonsRule.Execute();
//	LinkedList<Job> Johnson_machine1_List = JohnsonsRule.getMachine_1_JobList();
//	LinkedList<Job> Johnson_machine2_List = JohnsonsRule.getMachine_2_JobList();
//	
//	for(Job job : Johnson_machine1_List) {
//		System.out.println(job.getId());
//	}
//	System.out.println("-------------------------------");
//	for(Job job : Johnson_machine2_List) {
//		System.out.println(job.getId());
//	}
//	
//	Machines Johnson_machines = new Machines(Johnson_machine1_List,Johnson_machine2_List);
//	Johnson_machines.startMachines();
//	
	JohnsonsRule johnsonsRule = new JohnsonsRule(ddl);
	johnsonsRule.Execute();
	DVS_NEW DVS = new DVS_NEW(ddl);
	DVS.Execute();
	
	}

}
