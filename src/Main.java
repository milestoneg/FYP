import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

	public static void main(String[] args) throws IOException {
		//initial logger
		String logPath = "testlog";
		Logger logger = Logger.getLogger("test");
		FileHandler fileHandler = new FileHandler(logPath, true);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
		logger.setUseParentHandlers(false);

		Scanner intkb = new Scanner(System.in);

		// code used to read keyboard input
		System.out.println("Please input the deadline(ms):");
		int ddl = intkb.nextInt();

		// code used to read parameters from command line
		// int ddl = Integer.valueOf(args[0]);

		// JohnsonsRule JohnsonsRule = new JohnsonsRule();
		// JohnsonsRule.Execute();
		// LinkedList<Job> Johnson_machine1_List = JohnsonsRule.getMachine_1_JobList();
		// LinkedList<Job> Johnson_machine2_List = JohnsonsRule.getMachine_2_JobList();
		//
		// for(Job job : Johnson_machine1_List) {
		// System.out.println(job.getId());
		// }
		// System.out.println("-------------------------------");
		// for(Job job : Johnson_machine2_List) {
		// System.out.println(job.getId());
		// }
		//
		// Machines Johnson_machines = new
		// Machines(Johnson_machine1_List,Johnson_machine2_List);
		// Johnson_machines.startMachines();
		//
		logger.info("<-------- program start ---------->");
		logger.info("Deadline:" + ddl);
		JohnsonsRule johnsonsRule = new JohnsonsRule(ddl, logger);
		johnsonsRule.Execute();
		
		JohnsonsRule_withSpeedScaling johnsonsRule_withSpeedScaling = new JohnsonsRule_withSpeedScaling(ddl, logger);
		johnsonsRule_withSpeedScaling.Execute();
//		DVS_NEW DVS = new DVS_NEW(ddl, logger);
//		DVS.Execute();
//		
//		
		DVS_differentOrder DVS_differentOrder = new DVS_differentOrder(ddl, logger);
		DVS_differentOrder.Execute();
//		
		DVS_Interleaved dvs_Interleaved = new DVS_Interleaved(ddl, logger);
		dvs_Interleaved.Execute();
		logger.info("<---------- program end ---------->");
	}

}
