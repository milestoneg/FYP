package Generator;

import java.util.ArrayList;

public class Job {
	private int id;
	private ArrayList<WorkLoad> WorkLoadList = new ArrayList<>();
	
	public Job() {
		
	}
	public Job(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public ArrayList<WorkLoad> getWorkLoadList() {
		return WorkLoadList;
	}
	
	public void addWorkLoad(WorkLoad workLoad) {
		WorkLoadList.add(workLoad);
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public void setWorkLoadList(ArrayList<WorkLoad> workLoadList) {
		WorkLoadList = workLoadList;
	}
	public Job Copy() {
		Job job = new Job();
		job.setId(this.id); 
		job.setWorkLoadList(new ArrayList<>(this.WorkLoadList));
		return job;
	}

}
