
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class JohnsonsRule {

	String path = "/Users/gaoyuan/文档/Eclipse workspace/FYP/Jobs.txt";// indicate file path

	public void Execute() throws IOException {

		ArrayList<Job> JobList = ExtractData();
		// ------------- Category Lists------------//
		ArrayList<Job> J12 = new ArrayList<>();
		ArrayList<Job> J21 = new ArrayList<>();
		ArrayList<Job> J1 = new ArrayList<>();
		ArrayList<Job> J2 = new ArrayList<>();
		// ----------------------------------------//
		
		//debug
		for (int index = 0; index < JobList.size(); index++) {
			System.out.println("id: " + JobList.get(index).getId() + " workload:"
					+ JobList.get(index).getWorkLoadList().get(0).getMachineNo() + "||"
					+ JobList.get(index).getWorkLoadList().get(1).getMachineNo());

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
		
		System.out.println(J12.size());
		System.out.println(J21.size());//adfadsfasdfasdfsdf
	}

	public ArrayList<Job> ExtractData() throws IOException {

		File file = new File(path);
		Reader reader = new InputStreamReader(new FileInputStream(file));
		ArrayList<Job> JobList = new ArrayList<Job>();
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

}
