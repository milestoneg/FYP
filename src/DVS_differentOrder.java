import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class DVS_differentOrder {

	String path = "Jobs.txt";// indicate file path
	int deadline = 0;
	Logger logger;
	LinkedList<Job> machine_1_JobList = new LinkedList<>();
	LinkedList<Job> machine_2_JobList = new LinkedList<>();

	public DVS_differentOrder(int ddl, Logger logger) {
		deadline = ddl;
		this.logger = logger;
	}

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
			int firstMachine = job.getWorkLoadList().get(0).getMachineNo();
			int secondMachine_load = job.getWorkLoadList().get(1).getProcessingTime();
			if (firstMachine == 1 && secondMachine_load!=0) {
				J12.add(job);
				machine_1_JobList.add(job);
			} else if (firstMachine == 2 && secondMachine_load != 0) {
				J21.add(job);
				machine_2_JobList.add(job);
			} else if (firstMachine == 1 && secondMachine_load == 0) {
				J1.add(job);
				machine_1_JobList.add(job);
			} else {
				J2.add(job);
				machine_2_JobList.add(job);
			}
		}

		
		// control code
		long starttime = System.currentTimeMillis();	
		double DVS_M1 = DVS(machine_1_JobList);
		double DVS_M2 = DVS(machine_2_JobList);
		double a = DVS_M1-DVS_M2;
		double b = 2*deadline*DVS_M2;
		double c = -DVS_M2*deadline*deadline;
		double time = (-b+Math.sqrt(Math.pow(b, 2)-4*a*c))/(2*a);
		double time2 = (-b-Math.sqrt(Math.pow(b, 2)-4*a*c))/(2*a);
		double final_result = (Math.pow(machine_1_JobList.getFirst().getWorkLoadList().get(0).getProcessingTime()+DVS_M1+machine_1_JobList.getLast().getWorkLoadList().get(1).getProcessingTime(), 2)/(deadline-time))+(Math.pow(machine_2_JobList.getFirst().getWorkLoadList().get(0).getProcessingTime()+DVS_M2+machine_2_JobList.getLast().getWorkLoadList().get(1).getProcessingTime(), 2)/time);
		System.out.println("time:" +time +" time2:"+ time2);
		System.out.println("DVS Energy Consumption:" + final_result);
		long endtime = System.currentTimeMillis();
		//System.out.print("Execute time: " + (endtime - starttime) + " ms");
		//logger.info("DVS Energy Consumption: " + final_result);
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
		// LinkedList<Job> JobQueue = new LinkedList<>();
		double Min_EnergyConsumption = Double.POSITIVE_INFINITY;
		
		
		
		if (J.isEmpty() ) {
			return 0; // may has problem
		}else if(J.size() == 1) {
			return Math.sqrt(Math.pow(J.get(0).getWorkLoadList().get(0).getProcessingTime(), 2) + Math.pow(J.get(0).getWorkLoadList().get(1).getProcessingTime(), 2));
		}
		for (int index = 1; index < J.size(); index++) {
			boolean flag = true;
			for (int k = 1; k <= index; k++) {
				double Sum_Machine_1_toK = 0.0;
				double Sum_Machine_1_toI = 0.0;
				double Sum_Machine_2_toK = 0.0;
				double Sum_Machine_2_toI = 0.0;
				// assign values
				for (int sumindex = 1; sumindex <= k; sumindex++) {
					Sum_Machine_1_toK += J.get(sumindex).getWorkLoadList().get(0).getProcessingTime();
				}
				for (int sumindex = 1; sumindex <= index; sumindex++) {
					Sum_Machine_1_toI += J.get(sumindex).getWorkLoadList().get(0).getProcessingTime();
				}
				for (int sumindex = 0; sumindex <= k - 1; sumindex++) {
					Sum_Machine_2_toK += J.get(sumindex).getWorkLoadList().get(1).getProcessingTime();
				}
				for (int sumindex = 0; sumindex <= index - 1; sumindex++) {
					Sum_Machine_2_toI += J.get(sumindex).getWorkLoadList().get(1).getProcessingTime();
				}
				if ((Sum_Machine_1_toK / Sum_Machine_1_toI) < (Sum_Machine_2_toK / Sum_Machine_2_toI)) {
					flag = false;
				}
			}
			if (flag == true) {
				double Sum_Machine_1_toK_Mines1 = 0.0;
				double Sum_Machine_2_toI = 0.0;
				for(int k = 1; k<=index; k++) {
					for (int sumindex = 0; sumindex <= k - 1; sumindex++) {
						Sum_Machine_1_toK_Mines1 += J.get(sumindex).getWorkLoadList().get(0).getProcessingTime();
					}
					for (int sumindex = 0; sumindex <= index - 1; sumindex++) {
						Sum_Machine_2_toI += J.get(sumindex).getWorkLoadList().get(1).getProcessingTime();
					}
				}
				List<Job> subList = J.subList(index, J.size());
				
				double newMin = DVS(subList)
						+ Math.sqrt(Math.pow(Sum_Machine_1_toK_Mines1, 2) + Math.pow(Sum_Machine_2_toI, 2));
				if (Min_EnergyConsumption > newMin) {
					Min_EnergyConsumption = newMin;
				}
			}
			
		}
		return Min_EnergyConsumption;
	}

}
