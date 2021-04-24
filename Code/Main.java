
public class Main {

	public static void main(String[] args) {

		int default_tmimata = 3;

		long start = System.currentTimeMillis();
		if (args.length == 2) {
			if (args[0].toUpperCase().contains("TEACHERS") && args[1].toUpperCase().contains("LESSONS")) {
				SpaceSearcher.solve(args[0], args[1], default_tmimata, default_tmimata, default_tmimata);
			} else if (args[1].toUpperCase().contains("TEACHERS") && args[0].toUpperCase().contains("LESSONS")) {
				SpaceSearcher.solve(args[1], args[0], default_tmimata, default_tmimata, default_tmimata);
			} else {
				System.out.println("Wrong command line input");
			}
		} else if (args.length == 5) {
			if (args[0].toUpperCase().contains("TEACHERS") && args[1].toUpperCase().contains("LESSONS")) {
				SpaceSearcher.solve(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]),
						Integer.parseInt(args[4]));
			} else if (args[1].toUpperCase().contains("TEACHERS") && args[0].toUpperCase().contains("LESSONS")) {
				SpaceSearcher.solve(args[1], args[0], Integer.parseInt(args[2]), Integer.parseInt(args[3]),
						Integer.parseInt(args[4]));
			} else {
				System.out.println("Wrong command line input");
			}
		} else {
			System.out.println("Wrong number of arguments");
		}
		long end = System.currentTimeMillis();
		long res = end - start;
		System.out.println("Time it took: " + (double) res / 1000.0 + " seconds");

	}

}
