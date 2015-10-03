package WongHubRestlet;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GetMacAddress {

    public void Run() {
        try {

            InetAddress add = InetAddress.getByName("10.1.1.45");


            NetworkInterface ni = NetworkInterface.getByInetAddress(add);
            if (ni != null) {
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {

                    for (int k = 0; k < mac.length; k++) {
                        System.out.format("%02X%s", mac[k], (k < mac.length - 1) ? "-" : "");
                    }
                } else {
                    System.out.println("Address doesn't exist ");
                }
            } else {
                System.out.println("address is not found.");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}