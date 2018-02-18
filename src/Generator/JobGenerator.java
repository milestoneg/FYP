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
	int OnlyM1;
	int OnlyM2;
	int WorkloadLowerBound;
	int WorkloadUpperBound;

	int M1first_Count = 0;
	int OnlyM1_Count = 0;
	int OnlyM2_Count = 0;
	Random random = new Random();

	public JobGenerator(int JobAmount, int WorkloadForEachJob, int M1first, int WorkloadLowerBound,
			int WorkloadUpperBound, int OnlyM1, int OnlyM2) throws IOException {
		this.JobAmount = JobAmount;
		this.WorkloadForEachJob = WorkloadForEachJob;
		this.M1first = M1first;
		this.WorkloadLowerBound = WorkloadLowerBound;
		this.WorkloadUpperBound = WorkloadUpperBound;
		this.OnlyM1 = OnlyM1;
		this.OnlyM2 = OnlyM2;
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

			if (OnlyM1_Count < OnlyM1) {
				int ProcessingTime = random.nextInt(WorkloadUpperBound - WorkloadLowerBound) + WorkloadLowerBound;
				WorkLoad workLoad = new WorkLoad(1, ProcessingTime);
				job.addWorkLoad(workLoad);
				OnlyM1_Count++;
			} else if (OnlyM2_Count < OnlyM2) {
				int ProcessingTime = random.nextInt(WorkloadUpperBound - WorkloadLowerBound) + WorkloadLowerBound;
				WorkLoad workLoad = new WorkLoad(2, ProcessingTime);
				job.addWorkLoad(workLoad);
				OnlyM2_Count++;
			} else if (M1first_Count < M1first) {

				int ProcessingTime_1 = random.nextInt(WorkloadUpperBound - WorkloadLowerBound) + WorkloadLowerBound;
				WorkLoad workLoad_1 = new WorkLoad(1, ProcessingTime_1);
				job.addWorkLoad(workLoad_1);
				// FileWriter.write("1/" + workload);// machine number and workload

				int ProcessingTime_2 = random.nextInt(WorkloadUpperBound - WorkloadLowerBound) + WorkloadLowerBound;
				WorkLoad workLoad_2 = new WorkLoad(2, ProcessingTime_2);
				job.addWorkLoad(workLoad_2);
				// FileWriter.write("2/" + workload);// machine number and workload

				M1first_Count++;
			} else {

				int ProcessingTime_1 = random.nextInt(WorkloadUpperBound - WorkloadLowerBound) + WorkloadLowerBound;
				WorkLoad workLoad_1 = new WorkLoad(2, ProcessingTime_1);
				job.addWorkLoad(workLoad_1);
				// FileWriter.write("2/" + workload);// machine number and workload

				int ProcessingTime_2 = random.nextInt(WorkloadUpperBound - WorkloadLowerBound) + WorkloadLowerBound;
				WorkLoad workLoad_2 = new WorkLoad(1, ProcessingTime_2);
				job.addWorkLoad(workLoad_2);
				// FileWriter.write("1/" + workload);// machine number and workload

			}

			// FileWriter.write(",");// machine number and workload

			Gson Gson_Builder = new Gson();
			String Gson_Job = Gson_Builder.toJson(job);
			FileWriter.write(Gson_Job);
			FileWriter.flush();
			// FileWriter.write("|");
		}

	}

}
