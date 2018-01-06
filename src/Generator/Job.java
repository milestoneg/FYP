package Generator;
import java.util.ArrayList;

public class Job {
	private int id;
	private ArrayList<WorkLoad> WorkLoadList = new ArrayList<>();
	
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
	

}
