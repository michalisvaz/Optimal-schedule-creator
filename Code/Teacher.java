import java.util.ArrayList;

public class Teacher {

	private int code, maxHoursPerWeek, maxHoursPerDay;
	private String name;
	private ArrayList<Integer> lessons;

	public Teacher(int code, String name, ArrayList<Integer> lessons, int maxHoursPerWeek, int maxHoursPerDay) {
		this.code = code;
		this.name = name;
		this.lessons = lessons;
		this.maxHoursPerWeek = maxHoursPerWeek;
		this.maxHoursPerDay = maxHoursPerDay;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getMaxHoursPerWeek() {
		return maxHoursPerWeek;
	}

	public void setMaxHoursPerWeek(int maxHoursPerWeek) {
		this.maxHoursPerWeek = maxHoursPerWeek;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Integer> getLessons() {
		return lessons;
	}

	public void setLessons(ArrayList<Integer> lessons) {
		this.lessons = lessons;
	}

	public int getMaxHoursPerDay() {
		return maxHoursPerDay;
	}

	public void setMaxHoursPerDay(int maxHoursPerDay) {
		this.maxHoursPerDay = maxHoursPerDay;
	}

}
