import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class State implements Comparable<State> {

	static int tmimataA, tmimataB, tmimataC;
	static int dimenX = 5, dimenY = 7, dimenZ;
	static int hardConstraintWeight = 1048576; // 2^20

	/*
	 * Inner class that represents the pairing of a teacher with a lesson in the
	 * schedule
	 */
	private class ProgramHour {

		int teacherCode, lessonCode;

		public ProgramHour(int teacherCode, int lessonCode) {
			this.teacherCode = teacherCode;
			this.lessonCode = lessonCode;
		}

	}

	/*
	 * Inner class that represents a teacher and the total hours that he teaches
	 * each day
	 */
	private class TeacherWithHours {

		int teacherCode;
		int[] hoursPerDay;

		public TeacherWithHours(int teacherCode) {
			this.teacherCode = teacherCode;
			this.hoursPerDay = new int[dimenX];
			for (int i = 0; i < dimenX; i++) {
				this.hoursPerDay[i] = 0;
			}
		}

		public TeacherWithHours(TeacherWithHours other) {
			this.teacherCode = other.teacherCode;
			this.hoursPerDay = new int[dimenX];
			for (int i = 0; i < dimenX; i++) {
				this.hoursPerDay[i] = other.hoursPerDay[i];
			}
		}
	}

	ProgramHour[][][] program;
	int score;
	boolean someoneIsOverworking;
	HashMap<Integer, TeacherWithHours> hoursInfoForTeachers;

	public State() {
		this.program = new ProgramHour[dimenX][dimenY][dimenZ];
		this.score = Integer.MAX_VALUE; // initialise the state with a maximum score
		// Arraylist with an arraylist for every group which contains the required
		// lesson codes
		ArrayList<ArrayList<Integer>> listOLists = new ArrayList<ArrayList<Integer>>();
		this.hoursInfoForTeachers = new HashMap<Integer, TeacherWithHours>();
		this.someoneIsOverworking = false;
		/*
		 * Put tmimataA lists in the listoflists. Each of them will have each lesson
		 * code for the lessons of each group. It will have it as many times as the
		 * hours per week that the lesson should be taught
		 */
		for (int iter = 0; iter < tmimataA; iter++) {
			ArrayList<Integer> currentList = new ArrayList<Integer>();
			for (int j = 0; j < SpaceSearcher.lessons.size(); j++) {
				Lesson temp = SpaceSearcher.lessons.get(j);
				if (temp.getSchoolClass() == 'A') {
					for (int k = 0; k < temp.getHours(); k++) {
						currentList.add(temp.getCode());
					}
				}
			}
			listOLists.add(currentList);
		}
		// same for groups of grade B
		for (int iter = 0; iter < tmimataB; iter++) {
			ArrayList<Integer> currentList = new ArrayList<Integer>();
			for (int j = 0; j < SpaceSearcher.lessons.size(); j++) {
				Lesson temp = SpaceSearcher.lessons.get(j);
				if (temp.getSchoolClass() == 'B') {
					for (int k = 0; k < temp.getHours(); k++) {
						currentList.add(temp.getCode());
					}
				}
			}
			listOLists.add(currentList);
		}
		// same for groups of grade C
		for (int iter = 0; iter < tmimataC; iter++) {
			ArrayList<Integer> currentList = new ArrayList<Integer>();
			for (int j = 0; j < SpaceSearcher.lessons.size(); j++) {
				Lesson temp = SpaceSearcher.lessons.get(j);
				if (temp.getSchoolClass() == 'C') {
					for (int k = 0; k < temp.getHours(); k++) {
						currentList.add(temp.getCode());
					}
				}
			}
			listOLists.add(currentList);
		}

		Random rand = new Random();
		int ind = 0, codeOfLesson;
		// built hashmap with teacher codes as keys and maximum working hours as values
		HashMap<Integer, Integer> tempHashMap = new HashMap<>();
		for (HashMap.Entry<Integer, Teacher> entry : SpaceSearcher.teachers.entrySet()) {
			tempHashMap.put(entry.getKey(), entry.getValue().getMaxHoursPerWeek());
			// initialise hoursInfoForTeachers hashmap with teacher codes as keys and
			// objects TeachersWithHours as values
			this.hoursInfoForTeachers.put(entry.getKey(), new TeacherWithHours(entry.getKey()));
		}

		// Initialize 3d table
		for (int i = 0; i < dimenX; i++) {
			for (int j = 0; j < dimenY; j++) {
				for (int k = 0; k < dimenZ; k++) {
					// if there are no more lessons left add a free period
					if (listOLists.get(k).size() == 0) {
						program[i][j][k] = new ProgramHour(0, 0);
						continue;
					}
					// get random lesson
					ind = rand.nextInt(listOLists.get(k).size());
					codeOfLesson = listOLists.get(k).get(ind);
					ArrayList<Integer> tchrs = new ArrayList<Integer>();
					ArrayList<Integer> hrs = new ArrayList<Integer>();
					/*
					 * Check the teachers that can teach the lesson and their maximum working hours
					 * and choose a random one according to how many hours they have available. If
					 * none is available choose a ramdom one of those that can teach the lesson
					 */
					int sum = 0;
					int curVal;
					for (int l : tempHashMap.keySet()) {
						if (SpaceSearcher.teachers.get(l).getLessons().contains(codeOfLesson)) {
							tchrs.add(l);
							curVal = tempHashMap.get(l);
							hrs.add(curVal);
							sum += curVal;
						}
					}
					int x;
					if (sum == 0) { // no teacher left with enough hours to teach the lesson
						x = rand.nextInt(tchrs.size());
						// assign teaching hour in schedule
						program[i][j][k] = new ProgramHour(tchrs.get(x), codeOfLesson);
						this.someoneIsOverworking = true;
					} else {
						// choose ramdom teacher according to how many hours they have available
						x = rand.nextInt(sum);
						sum = 0;
						for (int l = 0; l < tchrs.size(); l++) {
							sum += hrs.get(l);
							if (x < sum) {
								// assign teaching hour in schedule
								program[i][j][k] = new ProgramHour(tchrs.get(l), codeOfLesson);
								this.hoursInfoForTeachers.get(tchrs.get(l)).hoursPerDay[i]++;
								tempHashMap.put(tchrs.get(l), hrs.get(l) - 1);// replaces as well
								break;
							}
						}
					}
					listOLists.get(k).remove(ind); // remove the lesson that we used
				}
			}
		}
	}

	public State(State other) {
		this.program = new ProgramHour[dimenX][dimenY][dimenZ];
		this.someoneIsOverworking = other.someoneIsOverworking;
		for (int i = 0; i < dimenX; i++) {
			for (int j = 0; j < dimenY; j++) {
				for (int k = 0; k < dimenZ; k++) {
					this.program[i][j][k] = new ProgramHour(other.program[i][j][k].teacherCode,
							other.program[i][j][k].lessonCode);
				}
			}
		}
		this.hoursInfoForTeachers = new HashMap<Integer, TeacherWithHours>();
		for (int x : other.hoursInfoForTeachers.keySet()) {
			this.hoursInfoForTeachers.put(x, new TeacherWithHours(other.hoursInfoForTeachers.get(x)));
		}
	}

	// Generate children
	public ArrayList<State> getSomeChildren(int howmany) {
		Random random = new Random();
		ArrayList<State> children = new ArrayList<State>();
		int x1, x2, y1, y2, z1;
		// if someone is working more than he can, generate random children
		if (this.someoneIsOverworking) {
			for (int it = 0; it < howmany; it++) {
				State current = new State();
				current.calculateScore();
				children.add(current);
			}
		} else {
			for (int it = 0; it < howmany; it++) {
				x1 = random.nextInt(dimenX); // random day1
				y1 = random.nextInt(dimenY); // random hour1
				z1 = random.nextInt(dimenZ); // random group
				x2 = random.nextInt(dimenX); // random day2
				y2 = random.nextInt(dimenY); // random hour2
				State current = new State(this);
				current.program[x1][y1][z1] = this.program[x2][y2][z1];
				current.program[x2][y2][z1] = this.program[x1][y1][z1];

				current.hoursInfoForTeachers.get(current.program[x1][y1][z1].teacherCode).hoursPerDay[x1]++;
				current.hoursInfoForTeachers.get(current.program[x1][y1][z1].teacherCode).hoursPerDay[x2]--;
				current.hoursInfoForTeachers.get(current.program[x2][y2][z1].teacherCode).hoursPerDay[x2]++;
				current.hoursInfoForTeachers.get(current.program[x2][y2][z1].teacherCode).hoursPerDay[x1]--;
				current.calculateScore();
				children.add(current);
			}
		}
		return children;

	}

	/* Print schedule in command line */
	public void print() {

		System.out.println("\n#############################################################################\n");
		for (int i = 0; i < dimenZ; i++) { // for each tmima
			if (i < tmimataA) {
				System.out.println("Tmima " + "A" + (i + 1));
			} else if (i < tmimataA + tmimataB) {
				System.out.println("Tmima " + "B" + (i + 1 - tmimataA));
			} else {
				System.out.println("Tmima " + "C" + (i + 1 - tmimataA - tmimataB));
			}
			System.out.println(" Hours |    Monday    |    Tuesday   |   Wednesday  |   Thursday   |     Friday   |\n");
			for (int j = 0; j < dimenY; j++) { // for each hour
				System.out.print("Hour " + j + " |");
				for (int k = 0; k < dimenX; k++) { // for each day
					if (program[k][j][i].lessonCode == 0) {
						System.out.print("|     keno     |");
					} else
						System.out.print(
								"| (" + program[k][j][i].lessonCode + ", " + program[k][j][i].teacherCode + ") |");
				}
				System.out.print("\n");
			}
			System.out.println("\n*************************************************************************\n");
		}

	}

	/* Print schedule in html file */
	public void printToFile(String fileName) {
		PrintWriter writer;
		String lessName = "";
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("<!DOCTYPE html>");
			writer.println("<html>");
			writer.println("<head>");
			writer.println("<meta charset=\"utf-8\">");
			writer.println("<title>Schedule</title>");
			writer.println("</head>");
			writer.println("<body>");
			writer.println("<h1>School Program</h1>");
			for (int i = 0; i < dimenZ; i++) { // for each tmima
				if (i < tmimataA) {
					writer.println("<table bgcolor=\"#00cc00\">");
					writer.println("<th>Tmima " + "A" + (i + 1) + "</th>");
				} else if (i < tmimataA + tmimataB) {
					writer.println("<table bgcolor=\"#e68a00\">");
					writer.println("<th>Tmima " + "B" + (i + 1 - tmimataA) + "</th>");
				} else {
					writer.println("<table bgcolor=\"#b3b300\">");
					writer.println("<th>Tmima " + "C" + (i + 1 - tmimataA - tmimataB) + "</th>");
				}
				writer.println(
						" <tr> <td> Hours </td> <td> Monday </td> <td> Tuesday </td> <td> Wednesday </td> <td> Thursday </td> <td> Friday </td> </tr>");
				for (int j = 0; j < dimenY; j++) { // for each hour
					writer.println("<tr>\n <td>Hour " + (j + 1) + "</td>");
					for (int k = 0; k < dimenX; k++) { // for each day
						if (program[k][j][i].lessonCode == 0) {
							writer.println("<td>-</td>");
						} else {
							for (Lesson xLesson : SpaceSearcher.lessons) {
								if (xLesson.getCode() == program[k][j][i].lessonCode) {
									lessName = xLesson.getName();
									break;
								}
							}
							writer.println("<td>" + lessName + "<br/>"
									+ SpaceSearcher.teachers.get(program[k][j][i].teacherCode).getName() + "</td>");
						}
					}
					writer.println("</tr>");
				}
				writer.println("</table> <br/>");
			}

			writer.println("</body>");
			writer.println("</html>");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/* Calculate state's score according to the restrictions that are violated */
	public void calculateScore() {

		score = 0;
		for (int i = 0; i < dimenX; i++) {
			for (int j = 0; j < dimenY; j++) {
				// Check if someone works the same hour in two classes
				ArrayList<Integer> temp = new ArrayList<Integer>();
				for (int k = 0; k < dimenZ; k++) {
					if (temp.contains(program[i][j][k].teacherCode) && program[i][j][k].teacherCode != 0) {
						this.score += hardConstraintWeight;
					} else {
						temp.add(program[i][j][k].teacherCode);
					}
				}

			}
			// for each teacher,for each day check if he/she overworks
			for (TeacherWithHours entry : hoursInfoForTeachers.values()) {
				if (entry.hoursPerDay[i] > SpaceSearcher.teachers.get(entry.teacherCode).getMaxHoursPerDay()) {
					this.score += hardConstraintWeight * (entry.hoursPerDay[i]
							- SpaceSearcher.teachers.get(entry.teacherCode).getMaxHoursPerDay());
				}
			}
		}

		// for each teacher check if he/she overworks over the week
		for (TeacherWithHours entry : hoursInfoForTeachers.values()) {
			int count = 0;
			for (int i = 0; i < dimenX; i++) {
				count += entry.hoursPerDay[i];
			}
			if (count > SpaceSearcher.teachers.get(entry.teacherCode).getMaxHoursPerWeek()) {
				this.score += hardConstraintWeight
						* (count - SpaceSearcher.teachers.get(entry.teacherCode).getMaxHoursPerWeek());
			}
		}

		// check for each group if it has free periods between lessons
		int space, begin, end, max, min;
		boolean start;
		for (int k = 0; k < dimenZ; k++) {
			max = -1;
			min = 8;
			int c1 = 2;
			for (int i = 0; i < dimenX; i++) {
				space = 0;
				start = false;
				begin = end = 0;
				for (int j = 0; j < dimenY; j++) {
					if (program[i][j][k].teacherCode != 0 && !start) {
						start = true;
						begin = j;
					}
					if (start) {
						if (program[i][j][k].teacherCode == 0) {
							space++;
						} else {
							score += c1 * space;
							space = 0;
							end = j;
						}
					}
				}
				// check for each group if school hours are evenly spread in the week
				if ((end - begin + 1) > max) {
					max = end - begin + 1;
				}
				if ((end - begin + 1) < min) {
					min = end - begin + 1;
				}
			}
			int c2 = 2;
			if (max - min > 2) {
				score += c2 * (max - min);
			}
		}

		// check for each teacher if he has more than two consecutive lesson hours
		int count;
		for (Teacher teacher : SpaceSearcher.teachers.values()) {
			if (teacher.getCode() == 0) {
				continue;
			}
			for (int i = 0; i < dimenX; ++i) {
				count = 0;
				for (int j = 0; j < dimenY; ++j) {
					start = false;
					for (int k = 0; k < dimenZ; ++k) {
						if (program[i][j][k].teacherCode == teacher.getCode()) {
							start = true;
							break;
						}
					}
					if (start) {
						++count;
					} else {
						count = 0;
					}
					if (count > 2) {
						score += count - 2;
					}
				}
			}
		}

		// check for each lesson if it is taught more than two hours per day
		for (Lesson lesson : SpaceSearcher.lessons) {
			for (int i = 0; i < dimenX; ++i) {
				for (int k = 0; k < dimenZ; ++k) {
					count = 0;
					for (int j = 0; j < dimenY; ++j) {
						if (program[i][j][k].lessonCode == lesson.getCode()) {
							count++;
						}
					}
					if (count > 2) {
						score += count - 2;
					}
				}
			}
		}

		// check if teaching hours are evenly spread between teachers
		double nums[] = new double[this.hoursInfoForTeachers.size() - 1];
		int i = 0;
		for (TeacherWithHours x : this.hoursInfoForTeachers.values()) {
			if (x.teacherCode != 0) {
				for (int j = 0; j < dimenX; j++) {
					nums[i] += x.hoursPerDay[j];
				}
				i++;
			}
		}
		int c = 1;
		score += c * (int) calculateSD(nums);

		// EXTRA CONSTRAINTS
		// for each group and day check if the lesson starts in the morning
		for (int k = 0; k < dimenZ; ++k) {
			for (i = 0; i < dimenX; ++i) {
				for (int j = 0; j < dimenY; ++j) {
					if (program[i][0][k].lessonCode == 0) {
						score += 1;
					} else {
						break;
					}
				}
			}
		}
	}

	@Override
	public int compareTo(State other) {
		return Integer.compare(this.score, other.score);
	}

	/* Calculate standard deviation */
	public static double calculateSD(double numArray[]) {
		double sum = 0.0, standardDeviation = 0.0;
		int length = numArray.length;
		for (double num : numArray) {
			sum += num;
		}
		double mean = sum / length;
		for (double num : numArray) {
			standardDeviation += Math.pow(num - mean, 2);
		}
		return Math.sqrt(standardDeviation / length);
	}

}
