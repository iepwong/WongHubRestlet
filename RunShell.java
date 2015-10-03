package WongHubRestlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunShell {
	public static int Run(String cmd) {
		
    try {
      String line;

      Process p = Runtime.getRuntime().exec(cmd);
      System.out.println(cmd);
      
      /* BufferedReader in = new BufferedReader(
              new InputStreamReader(p.getInputStream()) );
      while ((line = in.readLine()) != null) {
        System.out.println(line);
      }
      in.close(); */
    }
    catch (Exception e) {
			e.printStackTrace(System.err);
    }
		return 1;
	}
}