
public class Lesson {

	private int code, hours;
	private String name;
	private char schoolClass;

	public Lesson(int code, int hours, String name, char schoolClass) {
		this.code = code;
		this.hours = hours;
		this.name = name;
		this.schoolClass = schoolClass;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(char schoolClass) {
		this.schoolClass = schoolClass;
	}

}
