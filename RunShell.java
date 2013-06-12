package WongHubRestlet;


public class RunShell {
	public static int Run(String cmd) {

		try {
			// Run ls command
			Runtime.getRuntime().exec(cmd);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return 1;
	}
}