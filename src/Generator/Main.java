package Generator;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		Scanner intkb = new Scanner(System.in);
		int JobAmount = 0;
		int WorkloadForEachJob = 0;
		int M1first = 0;
		int WorkLoadLowerBound = 0;
		int WorkLoadUpperBound = 0;
		
		System.out.println("JobAmount:");
		JobAmount = intkb.nextInt();
		System.out.println("Work load for each job:");
		WorkloadForEachJob = intkb.nextInt();
		System.out.println("How many jobs you want to execute on machine 1 first:");
		M1first = intkb.nextInt();
		System.out.println("Please input the lower bound of workload in seconds:");
		WorkLoadLowerBound = intkb.nextInt();
		System.out.println("Please input the upper bound of workload in seconds:");
		WorkLoadUpperBound = intkb.nextInt();
		
		JobGenerator generator = new JobGenerator(JobAmount, WorkloadForEachJob,M1first,WorkLoadLowerBound,WorkLoadUpperBound);
		generator.StartGenerate();
		System.out.println("Done!");
	}

}
