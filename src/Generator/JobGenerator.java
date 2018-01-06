package Generator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import com.google.gson.Gson;

public class JobGenerator {

	String path = "/Users/gaoyuan/文档/Eclipse workspace/FYP/Jobs.txt";
	File file;
	FileWriter FileWriter;
	BufferedWriter BufferedWriter;
	int JobAmount;
	int WorkloadForEachJob;
	int M1first;
	int WorkloadLowerBound;
	int WorkloadUpperBound;

	int M1first_Count = 0;
	Random random = new Random();

	public JobGenerator(int JobAmount, int WorkloadForEachJob, int M1first, int WorkloadLowerBound,
			int WorkloadUpperBound) throws IOException {
		this.JobAmount = JobAmount;
		this.WorkloadForEachJob = WorkloadForEachJob;
		this.M1first = M1first;
		this.WorkloadLowerBound = WorkloadLowerBound;
		this.WorkloadUpperBound = WorkloadUpperBound;
		file = new File(path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		file.createNewFile();

		FileWriter = new FileWriter(file, true);
		BufferedWriter = new BufferedWriter(FileWriter);
	}

	public void StartGenerate() throws IOException {
		for (int Job_index = 0; Job_index < JobAmount; Job_index++) {
			
			Job job = new Job(Job_index);
			
			int previousFlag = 9;
			for (int WorkLoad_index = 0; WorkLoad_index < WorkloadForEachJob; WorkLoad_index++) {

				if (M1first_Count < M1first) {
					if (previousFlag == 9) {

						int ProcessingTime = random.nextInt(WorkloadUpperBound - WorkloadLowerBound) + WorkloadLowerBound;
						WorkLoad workLoad = new WorkLoad(1, ProcessingTime);
						job.addWorkLoad(workLoad);
						//FileWriter.write("1/" + workload);// machine number and workload
						previousFlag = 1;
					} else {
						int ProcessingTime = random.nextInt(WorkloadUpperBound - WorkloadLowerBound) + WorkloadLowerBound;						
						WorkLoad workLoad = new WorkLoad(2, ProcessingTime);
						job.addWorkLoad(workLoad);
						//FileWriter.write("2/" + workload);// machine number and workload
						previousFlag = 2;
					}
				} else {
					if (previousFlag == 9) {
						int ProcessingTime = random.nextInt(WorkloadUpperBound - WorkloadLowerBound) + WorkloadLowerBound;
						WorkLoad workLoad = new WorkLoad(2, ProcessingTime);
						job.addWorkLoad(workLoad);
						//FileWriter.write("2/" + workload);// machine number and workload
						previousFlag = 2;
					} else {
						int ProcessingTime = random.nextInt(WorkloadUpperBound - WorkloadLowerBound) + WorkloadLowerBound;
						WorkLoad workLoad = new WorkLoad(1, ProcessingTime);
						job.addWorkLoad(workLoad);
						//FileWriter.write("1/" + workload);// machine number and workload
						previousFlag = 1;
					}
				}
				//FileWriter.write(",");// machine number and workload
			}
			Gson Gson_Builder = new Gson();
			String Gson_Job = Gson_Builder.toJson(job);
			FileWriter.write(Gson_Job);
			M1first_Count++;
			//FileWriter.write("|");
		}
		FileWriter.flush();
	}

}
