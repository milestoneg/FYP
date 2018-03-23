package Generator;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

	public static void main(String[] args) throws IOException {
		// initial logger
		String logPath = "testlog";
		Logger logger = Logger.getLogger("test");
		FileHandler fileHandler = new FileHandler(logPath, true);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
		logger.setUseParentHandlers(false);

		Scanner intkb = new Scanner(System.in);
		int JobAmount = 0;
		int WorkloadForEachJob = 0;
		int M1first = 0;
		int OnlyM1 = 0;
		int OnlyM2 = 0;
		int WorkLoadLowerBound = 0;
		int WorkLoadUpperBound = 0;

		// read input manually(read keyboard input while program running)
		// System.out.println("JobAmount:");
		// JobAmount = intkb.nextInt();
		// WorkloadForEachJob = 2;//set work load amount for each job as 2
		// System.out.println("How many jobs you want to execute on machine 1 first:");
		// M1first = intkb.nextInt();
		// System.out.println("How many jobs you want to only execute on machine 1:");
		// OnlyM1 = intkb.nextInt();
		// System.out.println("How many jobs you want to only execute on machine 2:");
		// OnlyM2 = intkb.nextInt();
		// System.out.println("Please input the lower bound of workload in seconds:");
		// WorkLoadLowerBound = intkb.nextInt();
		// System.out.println("Please input the upper bound of workload in seconds:");
		// WorkLoadUpperBound = intkb.nextInt();

		JobAmount = Integer.valueOf(args[0]);
		WorkloadForEachJob = 2;
		M1first = Integer.valueOf(args[1]);
		OnlyM1 = Integer.valueOf(args[2]);
		OnlyM2 = Integer.valueOf(args[3]);
		WorkLoadLowerBound = Integer.valueOf(args[4]);
		WorkLoadUpperBound = Integer.valueOf(args[5]);

		JobGenerator generator = new JobGenerator(JobAmount, WorkloadForEachJob, M1first, WorkLoadLowerBound,
				WorkLoadUpperBound, OnlyM1, OnlyM2);
		generator.StartGenerate();
		System.out.println("Done!");
		logger.info("workload range: " + (WorkLoadUpperBound - WorkLoadLowerBound));
	}

}
