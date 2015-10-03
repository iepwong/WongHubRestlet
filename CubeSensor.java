package WongHubRestlet;

import java.util.List;
import com.w3asel.cubesensors.*;
import com.w3asel.cubesensors.api.*;
import com.w3asel.cubesensors.api.v1.*;
import com.w3asel.cubesensors.api.v1.format.*;
import com.w3asel.cubesensors.api.v1.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.scribe.model.Token;
import org.scribe.model.Verifier;

public class CubeSensor {
	public void getDevices(){
	Token accessToken = new Token("7rQtGAve5orOUubmih9l","EPLa43qdHGWSFZaTVuQrUYCF");
	// key: 7rQtGAve5orOUubmih9l, secret: EPLa43qdHGWSFZaTVuQrUYCF
	System.out.println("Getting accessToken");
	
	System.out.println("Creating api instance");
	//create the API instance for a given accessToken
	CubeSensorsApiV1 api = new CubeSensorsApiV1(accessToken);
	
	System.out.println("api instance created");
	
	//get the list of devices
	List<Device> devices = api.getDevices();
	//print current state information for the first device
	System.out.println(api.getCurrent(devices.get(0).getUid()));
	}
}
