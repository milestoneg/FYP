
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class JohnsonsRule {

	String path = "/Users/gaoyuan/文档/Eclipse workspace/FYP/Jobs.txt";// indicate file path

	public void Execute() throws IOException {

		LinkedList<Job> JobList = ExtractData();
		// ------------- Category Lists------------//
		LinkedList<Job> J12 = new LinkedList<>();
		LinkedList<Job> J21 = new LinkedList<>();
		LinkedList<Job> J1 = new LinkedList<>();
		LinkedList<Job> J2 = new LinkedList<>();
		// ----------------------------------------//

		// debug
		for (int index = 0; index < JobList.size(); index++) {
			System.out.println("id: " + JobList.get(index).getId() + " workload:"
					+ JobList.get(index).getWorkLoadList().get(0).getProcessingTime() + "||"
					+ JobList.get(index).getWorkLoadList().get(1).getProcessingTime());

		}

		// classify jobs
		for (Job job : JobList) {
			int workLoad_NUM = job.getWorkLoadList().size();
			int firstMachine = job.getWorkLoadList().get(0).getMachineNo();
			if (workLoad_NUM == 2 && firstMachine == 1) {
				J12.add(job);
			} else if (workLoad_NUM == 2 && firstMachine == 2) {
				J21.add(job);
			} else if (workLoad_NUM == 1 && firstMachine == 1) {
				J1.add(job);
			} else {
				J2.add(job);
			}
		}

		Job[] ordered_J12 = JohnsonAlogrithm(J12);
		Job[] ordered_J21 = JohnsonAlogrithm(J21);
		Job[] ordered_J1 = JohnsonAlogrithm(J1);
		Job[] ordered_J2 = JohnsonAlogrithm(J2);
		
		LinkedList<Job> Machine_1_JobList = new LinkedList<>();
		LinkedList<Job> Machine_2_JobList = new LinkedList<>();
		
		//combine J12 J1 J21 to Machine_1_JobList
		for(Job job : ordered_J12) {
			Machine_1_JobList.add(job);
		}
		for(Job job : ordered_J1) {
			Machine_1_JobList.add(job);
		}
		for(Job job : ordered_J21) {
			Machine_1_JobList.add(job);
		}
		
		for(Job job : Machine_1_JobList) {
			System.out.println("id"+job.getId());
		}
		
		//conbine J21 J2 J12 to Machine_2_JobList
		for(Job job : ordered_J21) {
			Machine_2_JobList.add(job);
		}
		for(Job job : ordered_J2) {
			Machine_2_JobList.add(job);
		}
		for(Job job : ordered_J12) {
			Machine_2_JobList.add(job);
		}
		
		System.out.println("------------------------------------");
		//sorted job list content
		for(Job job : Machine_1_JobList) {
			System.out.println("id" + job.getId());
		}
	}

	public LinkedList<Job> ExtractData() throws IOException {

		File file = new File(path);
		Reader reader = new InputStreamReader(new FileInputStream(file));
		LinkedList<Job> JobList = new LinkedList<Job>();
		StringBuffer sBuffer = new StringBuffer();

		Gson gson = new Gson();
		int tempchar;
		while ((tempchar = reader.read()) != -1) {
			sBuffer.append((char) tempchar);
		}
		String data = sBuffer.toString();
		Pattern pattern = Pattern.compile("\\}\\{");

		String[] data_splited = pattern.split(data);

		data_splited[0] = data_splited[0] + "}";// format the first json object
		data_splited[data_splited.length - 1] = "{" + data_splited[data_splited.length - 1];// format last json object
		// format other json object
		for (int index = 1; index < data_splited.length - 1; index++) {
			data_splited[index] = "{" + data_splited[index] + "}";
		}

		for (String data_extracted : data_splited) {
			Job job = gson.fromJson(data_extracted, Job.class);
			JobList.add(job);
		}
		return JobList;
	}

	public Job[] JohnsonAlogrithm(LinkedList<Job> J) {
		LinkedList<Job> Job_temp = new LinkedList<>(J);

		for (Job job : Job_temp) {
			System.out.println("id" + job.getId() + "workload:" + job.getWorkLoadList().get(0).getProcessingTime()
					+ "||" + job.getWorkLoadList().get(1).getProcessingTime());
		}

		Job[] ordered_Jobs = new Job[Job_temp.size()];
		int LeftPointer = 0;
		int RigthPointer = Job_temp.size() - 1;
		while (LeftPointer <= RigthPointer) {
			int MinWorkload = Job_temp.get(0).getWorkLoadList().get(0).getProcessingTime();
			int Min_Job_index = 0;
			int Min_workload_index = 0;

			// go through all workloads and find the smallest one. record the job index and
			// the min workload.
			for (int Job_index = 0; Job_index < Job_temp.size(); Job_index++) {
				for (int WorkLoad_index = 0; WorkLoad_index < Job_temp.get(Job_index).getWorkLoadList()
						.size(); WorkLoad_index++) {
					if (Job_temp.get(Job_index).getWorkLoadList().get(WorkLoad_index)
							.getProcessingTime() < MinWorkload) {
						MinWorkload = Job_temp.get(Job_index).getWorkLoadList().get(WorkLoad_index).getProcessingTime();
						Min_Job_index = Job_index;
						Min_workload_index = WorkLoad_index;
					}
				}

			}

			System.out.println("workload:" + MinWorkload);
			System.out.println("jobindex:" + Min_Job_index);
			System.out.println("Workloadindex:" + Min_workload_index);
			System.out.println("-------------------------");

			if (Job_temp.get(Min_Job_index).getWorkLoadList().get(Min_workload_index).getMachineNo() == 1) {
				for (Job job : Job_temp) {
					System.out.println("before:" + job.getId());
				}
				System.out.println("Min job index:::" + Min_Job_index);

				ordered_Jobs[LeftPointer] = Job_temp.remove(Min_Job_index);
				for (Job job : Job_temp) {
					System.out.println("after:" + job.getId());
				}
				LeftPointer++;

			} else {

				ordered_Jobs[RigthPointer] = Job_temp.remove(Min_Job_index);
				RigthPointer--;

			}
			try {
				MinWorkload = Job_temp.get(0).getWorkLoadList().get(0).getProcessingTime();
				Min_Job_index = 0;
				Min_workload_index = 0;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return ordered_Jobs;
	}

}
