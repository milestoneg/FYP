import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class DVS {

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
//		for (int index = 0; index < JobList.size(); index++) {
//			System.out.println("id: " + JobList.get(index).getId() + " workload:"
//					+ JobList.get(index).getWorkLoadList().get(0).getProcessingTime() + "||"
//					+ JobList.get(index).getWorkLoadList().get(1).getProcessingTime());
//
//		}

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

		Queue<Job> ordered_J12 = DVS(J12);
		Queue<Job> ordered_J21 = DVS(J21);
		//Queue<Job> ordered_J1 = DVS(J1);
		//Queue<Job> ordered_J2 = DVS(J2);
		
//		System.out.println("---------------------------");
//		while(!ordered_J12.isEmpty()) {
//			System.out.println("id"+ordered_J12.poll().getId());
//		}
		
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
	
	public Queue DVS(LinkedList<Job> J) {
		
		
		Queue<Job> JobQueue = new PriorityQueue<>(DVSComparator);
		for(Job job : J) {
			JobQueue.add(job);
		}
		
		return JobQueue;
	}
	
	public static Comparator<Job> DVSComparator = new Comparator<Job>() {

		@Override
		public int compare(Job o1, Job o2) {
			int W11 = o1.getWorkLoadList().get(0).getProcessingTime();
			int W12 = o1.getWorkLoadList().get(1).getProcessingTime();
			int W21 = o2.getWorkLoadList().get(0).getProcessingTime();
			int W22 = o2.getWorkLoadList().get(1).getProcessingTime();
			double W1FirstEnergyCons = W11 + Math.sqrt(Math.pow(W12, 2) + Math.pow(W21, 2)) + W22;
			double W2FirstEnergyCons = W12 + Math.sqrt(Math.pow(W11, 2) + Math.pow(W22, 2)) + W21;
			
			if(W1FirstEnergyCons > W2FirstEnergyCons) {
				return 1;
			}else if(W1FirstEnergyCons < W2FirstEnergyCons) {
				return -1;
			}
			return 0;
		}
		
	};
}
