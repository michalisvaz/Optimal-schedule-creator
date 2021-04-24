import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SpaceSearcher {

	static HashMap<Integer, Teacher> teachers;
	static ArrayList<Lesson> lessons;
	static ArrayList<State> states;
	static final int maxK = 100, maxReps = 500, howManyChildren = 10;

	public static void solve(String tchers, String tmima, int tmimataA, int tmimataB, int tmimataC) {
		teachers = Utilities.readTeachers(tchers);
		// we add a blank teacher in order to better handle free periods
		teachers.put(0, new Teacher(0, "", new ArrayList<Integer>(), 123456, 123456));
		lessons = Utilities.readLessons(tmima);
		State.tmimataA = tmimataA;
		State.tmimataB = tmimataB;
		State.tmimataC = tmimataC;
		State.dimenZ = tmimataA + tmimataB + tmimataC;
		if (!quickCheck()) {
			System.out.println("It is impossible to find a solution");
			return;
		}
		initialize();
		State solution = BeamSearch();
		if (solution == null || solution.score >= State.hardConstraintWeight) {
			System.out.println("Could not find solution");
			System.out.println("Best attempt with score " + states.get(0).score + " is printed in file schedule.html");
			solution.printToFile("schedule.html");
		} else {
			System.out.println("Solution with score " + states.get(0).score + " is printed in file schedule.html");
			solution.printToFile("schedule.html");
		}
	}

	private static State BeamSearch() {
		for (int i = 0; i < maxReps; i++) {

			ArrayList<State> forehead = new ArrayList<State>();

			// for every state generate its children
			for (State curnt : states) {
				forehead.add(curnt);
				forehead.addAll(curnt.getSomeChildren(howManyChildren));
			}

			// Sort the states in ascending order
			Collections.sort(forehead);

			// keep the maxK states with the least score
			states = new ArrayList<State>();
			for (int j = 0; j < maxK; j++) {
				states.add(forehead.get(j));
			}

			// check if we have a terminal state
			if (states.get(0).score == 0) {
				return states.get(0);
			}
		}
		return states.get(0);
	}

	private static void initialize() {
		states = new ArrayList<State>();
		for (int i = 0; i < maxK; i++) {
			State xState = new State();
			xState.calculateScore();
			states.add(xState);
		}
	}

	/*
	 * Check if the teachers' hours are enough to cover for the teaching hours that
	 * we need if it returns false, we certainly can't make a schedule if it returns
	 * true, we MAY be able to make a schedule
	 */
	static boolean quickCheck() {

		int demand, offer, totalDemand = 0, totalOffer = 0;

		for (Lesson l : lessons) {
			offer = 0;
			switch (l.getSchoolClass()) {
			case 'A':
				demand = l.getHours() * State.tmimataA;
				break;
			case 'B':
				demand = l.getHours() * State.tmimataB;
				break;
			default:
				demand = l.getHours() * State.tmimataC;
				break;
			}
			for (Teacher teach : teachers.values()) {
				if (teach.getLessons().contains(l.getCode())) {
					offer += teach.getMaxHoursPerWeek();
				}
			}
			if (offer < demand) {
				return false;
			}
			totalDemand += demand;
			totalOffer += offer;
		}

		return totalDemand < totalOffer;
	}

}
