import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class DVS_NEW {

	String path = "/Users/gaoyuan/文档/Eclipse workspace/FYP/Jobs.txt";// indicate file path
	LinkedList<Job> machine_1_JobList = new LinkedList<>();
	LinkedList<Job> machine_2_JobList = new LinkedList<>();

	public LinkedList<Job> getMachine_1_JobList() {
		return machine_1_JobList;
	}

	public LinkedList<Job> getMachine_2_JobList() {
		return machine_2_JobList;
	}

	public void Execute() throws IOException {
		
		LinkedList<Job> JobList = ExtractData();
		// ------------- Category Lists------------//
		LinkedList<Job> J12 = new LinkedList<>();
		LinkedList<Job> J21 = new LinkedList<>();
		LinkedList<Job> J1 = new LinkedList<>();
		LinkedList<Job> J2 = new LinkedList<>();
		// ----------------------------------------//

		// debug
		// for (int index = 0; index < JobList.size(); index++) {
		// System.out.println("id: " + JobList.get(index).getId() + " workload:"
		// + JobList.get(index).getWorkLoadList().get(0).getProcessingTime() + "||"
		// + JobList.get(index).getWorkLoadList().get(1).getProcessingTime());
		//
		// }

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

		long starttime = System.currentTimeMillis();
		double J12_EnergyConsumption = DVS(J12);
		//double J21_EnergyConsumption = DVS(J21);
		System.out.println(J12_EnergyConsumption+"sssss");
		long endtime = System.currentTimeMillis();
		System.out.print(endtime-starttime+" ms");
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

	public double DVS(List<Job> J) {
		//LinkedList<Job> JobQueue = new LinkedList<>();
		double Min_EnergyConsumption = Double.POSITIVE_INFINITY;
		if (J.isEmpty()||J.size()==1) {
			return 0;
		}
		for (int index = 1; index < J.size(); index++) {
			for (int k = 1; k <= index; k++) {
				double Sum_Machine_1_toK = 0;
				double Sum_Machine_1_toI= 0;
				double Sum_Machine_2_toK = 0;
				double Sum_Machine_2_toI = 0;
				//assign values
				for(int sumindex = 1;sumindex<=k;sumindex++) {
					Sum_Machine_1_toK += J.get(sumindex).getWorkLoadList().get(0).getProcessingTime();
				}
				for(int sumindex = 1; sumindex <= index; sumindex++) {
					Sum_Machine_1_toI += J.get(sumindex).getWorkLoadList().get(0).getProcessingTime();
				}
				for(int sumindex = 0; sumindex <= k-1; sumindex++) {
					Sum_Machine_2_toK += J.get(sumindex).getWorkLoadList().get(1).getProcessingTime();
				}
				for(int sumindex = 0; sumindex <= index-1; sumindex++) {
					Sum_Machine_2_toI += J.get(sumindex).getWorkLoadList().get(1).getProcessingTime();
				}
				if((Sum_Machine_1_toK/Sum_Machine_1_toI)>=(Sum_Machine_2_toK/Sum_Machine_2_toI)) {
					double Sum_Machine_1_toK_Mines1 = 0;
					List<Job> subList = J.subList(index,J.size());
					for(int sumindex = 0; sumindex<=k-1;sumindex++) {
						Sum_Machine_1_toK_Mines1 += J.get(sumindex).getWorkLoadList().get(0).getProcessingTime();
					}
					double newMin = DVS(subList)+Math.sqrt(Math.pow(Sum_Machine_1_toK_Mines1, 2)+Math.pow(Sum_Machine_2_toI, 2));
					if(Min_EnergyConsumption>newMin) {
						 Min_EnergyConsumption = newMin;
					}
				}
			}
		}
		return Min_EnergyConsumption;
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

			if (W1FirstEnergyCons > W2FirstEnergyCons) {
				return 1;
			} else if (W1FirstEnergyCons < W2FirstEnergyCons) {
				return -1;
			}
			return 0;
		}

	};
}
