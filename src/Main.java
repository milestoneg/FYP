import java.io.IOException;
import java.util.LinkedList;

public class Main {

	public static void main(String[] args) throws IOException {
	JohnsonsRule test = new JohnsonsRule();
	test.Execute();
	LinkedList<Job> ss = test.getMachine_1_JobList();
	LinkedList<Job> ss2 = test.getMachine_2_JobList();
	for(Job job : ss) {
		System.out.println(job.getId());
	}
	System.out.println("-------------------------------");
	for(Job job : ss2) {
		System.out.println(job.getId());
	}
	
	Machines go = new Machines(ss, ss2);
	System.out.println("-------------------------------");
	go.startMachines();
//    DVS test2 = new DVS();
//    test2.Execute();
	}

}
