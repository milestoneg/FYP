
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class JohnsonsRule_withSpeedScaling {

	String path = "Jobs.txt";// indicate file path
	double deadline = 0;
	Logger logger;
	LinkedList<Job> Machine_1_JobList = new LinkedList<>();
	LinkedList<Job> Machine_2_JobList = new LinkedList<>();
	// ------------- Category Lists------------//
	LinkedList<Job> J12 = new LinkedList<>();
	LinkedList<Job> J21 = new LinkedList<>();
	LinkedList<Job> J1 = new LinkedList<>();
	LinkedList<Job> J2 = new LinkedList<>();
	// ----------------------------------------//

	public JohnsonsRule_withSpeedScaling(int ddl, Logger logger) {
		deadline = ddl * 1.0;
		this.logger = logger;
	}

	public void Execute() throws IOException {

		LinkedList<Job> JobList = ExtractData();

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
			if (firstMachine == 1 && secondMachine_load != 0) {
				J12.add(job);
			} else if (firstMachine == 2 && secondMachine_load != 0) {
				J21.add(job);
			} else if (firstMachine == 1 && secondMachine_load == 0) {
				J1.add(job);
			} else {
				J2.add(job);
			}
		}

		Job[] ordered_J12 = JohnsonAlogrithm(J12);
		Job[] ordered_J21 = JohnsonAlogrithm(J21);
		Job[] ordered_J1 = JohnsonAlogrithm(J1);
		Job[] ordered_J2 = JohnsonAlogrithm(J2);

		// combine J12 J1 J21 to Machine_1_JobList
		for (Job job : ordered_J12) {
			Machine_1_JobList.add(job);
		}
		for (Job job : ordered_J1) {
			Machine_1_JobList.add(job);
		}
		for (Job job : ordered_J21) {
			Machine_1_JobList.add(job);
		}

		// conbine J21 J2 J12 to Machine_2_JobList
		for (Job job : ordered_J21) {
			Machine_2_JobList.add(job);
		}
		for (Job job : ordered_J2) {
			Machine_2_JobList.add(job);
		}
		for (Job job : ordered_J12) {
			Machine_2_JobList.add(job);
		}

		// print energy consumption
		double Energy = EnergyConsumption();
		System.out.println("Johnsons_speedScaling Energy Consumption:" + Energy);
		logger.info("Johonsons_speedScaling Energy Consumption:" + Energy);
	}

	public LinkedList<Job> getMachine_1_JobList() {
		return Machine_1_JobList;
	}

	public LinkedList<Job> getMachine_2_JobList() {
		return Machine_2_JobList;
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

			// System.out.println("workload:" + MinWorkload);
			// System.out.println("jobindex:" + Min_Job_index);
			// System.out.println("Workloadindex:" + Min_workload_index);
			// System.out.println("-------------------------");

			if (Job_temp.get(Min_Job_index).getWorkLoadList().get(Min_workload_index).getMachineNo() == 1) {
				// for (Job job : Job_temp) {
				// System.out.println("before:" + job.getId());
				// }
				// System.out.println("Min job index:::" + Min_Job_index);

				ordered_Jobs[LeftPointer] = Job_temp.remove(Min_Job_index);
				// for (Job job : Job_temp) {
				// System.out.println("after:" + job.getId());
				// }
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

	public double EnergyConsumption() {
		int workload_J12_J1 = 0;
		int workload_J21_J2 = 0;
		int workload_J12 = 0;
		int workload_J21 = 0;
		int workload_J1 = 0;
		int workload_J2 = 0;
		int workload_J12_M2 = 0;
		int workload_J21_M1 = 0;
		int totalWork_M1 = 0;
		int totalWork_M2 = 0;
		double EnergyCons = 0.0;
		try {
			for (Job job : J12) {
				workload_J12_J1 += job.getWorkLoadList().get(0).getProcessingTime();
				workload_J12 += job.getWorkLoadList().get(0).getProcessingTime();
				workload_J12_M2 += job.getWorkLoadList().get(1).getProcessingTime();
				totalWork_M2 += job.getWorkLoadList().get(1).getProcessingTime();
			}
			for (Job job : J1) {
				workload_J12_J1 += job.getWorkLoadList().get(0).getProcessingTime();
				workload_J1 += job.getWorkLoadList().get(0).getProcessingTime();
			}
			for (Job job : J21) {
				workload_J21_J2 += job.getWorkLoadList().get(0).getProcessingTime();
				workload_J21 += job.getWorkLoadList().get(0).getProcessingTime();
				workload_J21_M1 += job.getWorkLoadList().get(1).getProcessingTime();
				totalWork_M1 += job.getWorkLoadList().get(1).getProcessingTime();
			}
			for (Job job : J2) {
				workload_J21_J2 += job.getWorkLoadList().get(0).getProcessingTime();
				workload_J2 += job.getWorkLoadList().get(0).getProcessingTime();
			}
			totalWork_M1 = totalWork_M1 + workload_J12_J1;
			totalWork_M2 = totalWork_M2 + workload_J21_J2;
		} catch (Exception e) {
			// TODO: handle exception
		}
//		System.out.println("-----------------");
//		System.out.println(workload_J12_J1);
//		System.out.println(workload_J21_J2);
//		System.out.println(workload_J12_M2);
//		System.out.println(totalWork_M1);
//		System.out.println(totalWork_M2);
//		System.out.println("-----------------");
		if (workload_J21_J2 == 0 && !J1.isEmpty()) {
			if (workload_J1 < workload_J12_M2) {
				double factor = 0.25;
				double clip = factor * deadline;
				double random_time = new Random().nextDouble()*(deadline-2*clip)+clip;
				double c = 1E-6;
				int decreaseCount = 1;
				while (random_time == 0.0) {
					random_time = new Random().nextDouble() * deadline;
				}
				double time = random_time - c * ((-Math.pow(workload_J12, 2) / Math.pow(random_time, 2))
						+ (Math.pow(workload_J12_M2, 2)) / Math.pow(deadline - random_time, 2));

				while (decreaseCount < 300) {
					time = gradientDescent(time, random_time, c, workload_J12, workload_J12_M2);
					c = c * Math.pow(0.99,decreaseCount/20 );
					decreaseCount++;

				}

				EnergyCons = Math.pow((workload_J12) / time, 2) * time
						+ Math.pow((workload_J12_M2) / (deadline - time), 2) * (deadline - time)
						+ Math.pow((workload_J1) / (deadline - time), 2) * (deadline - time);
			} else {
				double factor = 0.25;
				double clip = factor * deadline;
				double random_time = new Random().nextDouble()*(deadline-2*clip)+clip;
				double c = 1E-6;
				int decreaseCount = 1;
				while (random_time == 0.0) {
					random_time = new Random().nextDouble() * deadline;
				}
				double time = random_time - c * ((-Math.pow(workload_J12, 2) / Math.pow(random_time, 2))
						+ (Math.pow(workload_J1, 2)) / Math.pow(deadline - random_time, 2));

				while (decreaseCount < 300) {
					time = gradientDescent(time, random_time, c, workload_J12, workload_J1);
					c = c * Math.pow(0.99,decreaseCount/20 );
					decreaseCount++;

				}

				EnergyCons = Math.pow((workload_J12) / time, 2) * time
						+ Math.pow((workload_J1) / (deadline - time), 2) * (deadline - time)
						+ Math.pow((workload_J12_M2) / (deadline - time), 2) * (deadline - time);
			}

		} else if (workload_J12_J1 == 0 && !J2.isEmpty()) {
			if (workload_J2 < workload_J21_M1) {
				double factor = 0.25;
				double clip = factor * deadline;
				double random_time = new Random().nextDouble()*(deadline-2*clip)+clip;
				double c = 1E-6;
				int decreaseCount = 1;
				while (random_time == 0.0) {
					random_time = new Random().nextDouble() * deadline;
				}
				double time = random_time - c * ((-Math.pow(workload_J21, 2) / Math.pow(random_time, 2))
						+ (Math.pow(workload_J21_M1, 2)) / Math.pow(deadline - random_time, 2));

				while (decreaseCount < 300) {
					time = gradientDescent(time, random_time, c, workload_J21, workload_J21_M1);
					c = c * Math.pow(0.99,decreaseCount/20 );
					decreaseCount++;

				}

				EnergyCons = Math.pow((workload_J21) / time, 2) * time
						+ Math.pow((workload_J21_M1) / (deadline - time), 2) * (deadline - time)
						+ Math.pow((workload_J2) / (deadline - time), 2) * (deadline - time);

			} else {
				double factor = 0.25;
				double clip = factor * deadline;
				double random_time = new Random().nextDouble()*(deadline-2*clip)+clip;
				double c = 1E-6;
				int decreaseCount = 1;
				while (random_time == 0.0) {
					random_time = new Random().nextDouble() * deadline;
				}
				double time = random_time - c * ((-Math.pow(workload_J21, 2) / Math.pow(random_time, 2))
						+ (Math.pow(workload_J2, 2)) / Math.pow(deadline - random_time, 2));

				while (decreaseCount < 300) {
					time = gradientDescent(time, random_time, c, workload_J21, workload_J2);
					c = c * Math.pow(0.99,decreaseCount/20 );
					decreaseCount++;

				}

				EnergyCons = Math.pow((workload_J21) / time, 2) * time
						+ Math.pow((workload_J21_M1) / (deadline - time), 2) * (deadline - time)
						+ Math.pow((workload_J2) / (deadline - time), 2) * (deadline - time);

			}
		} else {
			if (workload_J12_J1 > workload_J21_J2) {
				if (workload_J12_M2 > workload_J21_M1) {
					double factor = 0.25;
					double clip = factor * deadline;
					double random_time = new Random().nextDouble()*(deadline-2*clip)+clip;
					double c = 1E-6;
					int decreaseCount = 1;
					while (random_time == 0.0) {
						random_time = new Random().nextDouble() * deadline;
					}
					double time = random_time - c * ((-Math.pow(workload_J12_J1, 2) / Math.pow(random_time, 2))
							+ (Math.pow(workload_J12_M2, 2)) / Math.pow(deadline - random_time, 2));
					
					while (decreaseCount < 300) {
						time = gradientDescent(time, random_time, c, workload_J12_J1, workload_J12_M2);
						//System.out.println(energy);
						c = c * Math.pow(0.99,decreaseCount/20 );
						decreaseCount++;
					}
					//System.out.println("random" + random_time);
					//System.out.println(time);
					EnergyCons = Math.pow((workload_J12_J1) / time, 2) * time
							+ Math.pow((workload_J21_J2) / time, 2) * time
							+ Math.pow((workload_J12_M2) / (deadline - time), 2) * (deadline - time)
							+ Math.pow((workload_J21_M1) / (deadline - time), 2) * (deadline - time);
				} else {
					
					double factor = 0.25;
					double clip = factor * deadline;
					double random_time = new Random().nextDouble()*(deadline-2*clip)+clip;
					double c = 1E-6;
					int decreaseCount = 1;
					while (random_time == 0.0) {
						random_time = new Random().nextDouble() * deadline;
					}
					double time = random_time - c * ((-Math.pow(workload_J12_J1, 2) / Math.pow(random_time, 2))
							+ (Math.pow(workload_J21_M1, 2)) / Math.pow(deadline - random_time, 2));

					while (decreaseCount < 300) {
						time = gradientDescent(time, random_time, c, workload_J12_J1, workload_J21_M1);
						c = c * Math.pow(0.99,decreaseCount/20 );
						decreaseCount++;

					}

					EnergyCons = Math.pow((workload_J12_J1) / time, 2) * time
							+ Math.pow((workload_J21_J2) / time, 2) * time
							+ Math.pow((workload_J12_M2) / (deadline - time), 2) * (deadline - time)
							+ Math.pow((workload_J21_M1) / (deadline - time), 2) * (deadline - time);
				}
			} else {
				if (workload_J12_M2 > workload_J21_M1) {
					// double random_time = new Random().nextDouble()*deadline;
					
					double factor = 0.25;
					double clip = factor * deadline;
					double random_time = new Random().nextDouble()*(deadline-2*clip)+clip;
					double c = 1E-6;
					int decreaseCount = 1;
					while (random_time == 0.0) {
						random_time = new Random().nextDouble() * deadline;
					}
					double time = random_time - c * ((-Math.pow(workload_J21_J2, 2) / Math.pow(random_time, 2))
							+ (Math.pow(workload_J12_M2, 2)) / Math.pow(deadline - random_time, 2));

					while (decreaseCount < 300) {
						time = gradientDescent(time, random_time, c, workload_J21_J2, workload_J12_M2);
						c = c * Math.pow(0.99,decreaseCount/20 );
						decreaseCount++;

					}

					EnergyCons = Math.pow((workload_J12_J1) / time, 2) * time
							+ Math.pow((workload_J21_J2) / time, 2) * time
							+ Math.pow((workload_J12_M2) / (deadline - time), 2) * (deadline - time)
							+ Math.pow((workload_J21_M1) / (deadline - time), 2) * (deadline - time);

				} else {
					double factor = 0.25;
					double clip = factor * deadline;
					double random_time = new Random().nextDouble()*(deadline-2*clip)+clip;
					double c = 1E-6;
					int decreaseCount = 1;
					while (random_time == 0.0) {
						random_time = new Random().nextDouble() * deadline;
					}
					double time = random_time - c * ((-Math.pow(workload_J21_J2, 2) / Math.pow(random_time, 2))
							+ (Math.pow(workload_J21_M1, 2)) / Math.pow(deadline - random_time, 2));

					while (decreaseCount < 300) {
						time = gradientDescent(time, random_time, c, workload_J21_J2, workload_J21_M1);
						c = c * Math.pow(0.99,decreaseCount/20 );
						decreaseCount++;

					}

					EnergyCons = Math.pow((workload_J12_J1) / time, 2) * time
							+ Math.pow((workload_J21_J2) / time, 2) * time
							+ Math.pow((workload_J12_M2) / (deadline - time), 2) * (deadline - time)
							+ Math.pow((workload_J21_M1) / (deadline - time), 2) * (deadline - time);
				}
			}

		}
		return EnergyCons;
	}

	public double gradientDescent(double t, double random_time, double c, int workload_1, int workload_2) {
		return t - c * ((-Math.pow(workload_1, 2) / Math.pow(random_time, 2))
				+ (Math.pow(workload_2, 2)) / Math.pow(deadline - random_time, 2));
	}
}
