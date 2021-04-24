import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Utilities {

	static HashMap<Integer, Teacher> readTeachers(String file) {
		HashMap<Integer, Teacher> teachers = new HashMap<Integer, Teacher>();
		Teacher xTeacher;
		try {
			BufferedReader buf = new BufferedReader(new FileReader(file));
			String lineJustFetched = null;
			String[] wordsArray;
			ArrayList<Integer> lessons;
			buf.readLine();
			while (true) {
				lineJustFetched = buf.readLine();
				lessons = new ArrayList<Integer>();
				if (lineJustFetched == null) {
					break;
				} else {
					wordsArray = lineJustFetched.split("\t");
					for (String each : wordsArray[2].substring(1, wordsArray[2].length() - 1).split(",")) {
						lessons.add(Integer.parseInt(each));
					}
					xTeacher = new Teacher(Integer.parseInt(wordsArray[0]), wordsArray[1], lessons,
							Integer.parseInt(wordsArray[3]), Integer.parseInt(wordsArray[4]));
					teachers.put(xTeacher.getCode(), xTeacher);
				}
			}
			buf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return teachers;
	}

	static ArrayList<Lesson> readLessons(String file) {
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		Lesson xLesson;
		try {
			BufferedReader buf = new BufferedReader(new FileReader(file));
			String lineJustFetched = null;
			String[] wordsArray;
			buf.readLine();
			while (true) {
				lineJustFetched = buf.readLine();
				if (lineJustFetched == null) {
					break;
				} else {
					wordsArray = lineJustFetched.split("\t");
					xLesson = new Lesson(Integer.parseInt(wordsArray[0]), Integer.parseInt(wordsArray[3]),
							wordsArray[1], wordsArray[2].toUpperCase().charAt(0));
					lessons.add(xLesson);
				}
			}
			buf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lessons;
	}
}
