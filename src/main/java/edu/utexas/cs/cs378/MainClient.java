// 1. Student Name: Sanchana Shanmuga
//    Student UT EID: ss229638

// 2. Student Name: Victoria Reddy
//    Student UT EID: vrr593
//
// ## Course Name: CS378
// ## Unique Number: 12345
// ## Date Created: 2026-09-13
package edu.utexas.cs.cs378;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MainClient {

	static public int portNumber = 33333;
	static public String hostName = "localhost";
	static public int batchSize = 4000;
	private static Socket mySocket;

	/**
	 * A main method to run examples.
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {

		String dataFile = "taxi-data-sorted-small.csv";

       
        if(args.length >= 3) {
            batchSize = Integer.parseInt(args[0]);
            hostName = args[1];
            portNumber = Integer.parseInt(args[2]);
        } 
		else if(args.length > 0) {
            System.err.println("Usage: MainClient    [dataFile]");
            return;
        }

        if(args.length >= 4) {
            dataFile = args[3];
        }

		

		try {
			System.out.println("Reading and cleaning dataset: " + dataFile);
			HashMap<String, DriverStats> map = cleanAndTogether(dataFile);
			List<DataItem> dataItems = convertItems(map);
			

			mySocket = new Socket(hostName, portNumber);
			System.out.println("Waiting for client connection ... ");

			DataInputStream dis = new DataInputStream(mySocket.getInputStream());
			DataOutputStream dos = new DataOutputStream(mySocket.getOutputStream());
			System.out.println("Server is hearing on port " + portNumber);

			// This is a demo data
			// 1. Clean your data
			// 2. Pre-process your data, map it for example to other forms
			// 3. Send it to the server like the following.

			
			List<byte[]> pages = Utils.packageToPages(dataItems);

			// Then we send the pages over to the server.
			for (byte[] bs : pages) {

				// tell the server that we have data to send
				dos.writeInt(1);
				dos.flush();

				System.out.println("Sending a page of data to server");
				// then write the entire page and flush it
				dos.write(bs);
				dos.flush();

				while (dis.readInt() != 1) {
					System.out.println("While true");
					// Here client asks the server if the it can process more data.
					//
					try {
						// !TODO: We sleep here but you can do a lot more thing.s

						Thread.sleep(500);
						System.out.println("Waiting for the server ... ");
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
			}

			// Good bye! We have no more data.
			// Tell the server to terminate.
			dos.writeInt(0);
			dos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static class TaxiRecord {
		public String medallion;
		public String hackLicense;
		public double totalAmount;

		public TaxiRecord (String m, String h, double t) {
			medallion = m;
			hackLicense = h;
			totalAmount = t;
		}
	}

	public static class DriverStats {
		public double totalEarnings = 0.0;
		public HashSet<String> medallionSet = new HashSet<>();
	}

	public static TaxiRecord validate (String line) {
		double fareAmount;
		double surcharge;
		double mtaTax;
		double tipAmount;
		double tollsAmount;
		double totalAmount;
		String[] split = line.split(",", -1);
		if(split.length != 17) {
			return null;
		}
		try {
			fareAmount = Double.parseDouble(split[11]);
        	surcharge = Double.parseDouble(split[12]);
        	mtaTax = Double.parseDouble(split[13]);
        	tipAmount = Double.parseDouble(split[14]);
        	tollsAmount = Double.parseDouble(split[15]);
        	totalAmount = Double.parseDouble(split[16]);
		} 
		catch (NumberFormatException e) {
			return null;
		}
		String medallion = split[0].trim();
		String hackLicense = split[1].trim();
		if(medallion.isEmpty() || hackLicense.isEmpty()) {
			return null;
		}
		double computedTotal = fareAmount + surcharge + mtaTax + tipAmount + tollsAmount;
		if(Math.abs(computedTotal - totalAmount) > .0101) {
			return null;
		}
		if(totalAmount > 500) {
			return null;
		}
		return new TaxiRecord(medallion, hackLicense, totalAmount);
	}

	public static HashMap<String, DriverStats> cleanAndTogether (String file) {
		HashMap<String, DriverStats> result = new HashMap<>();
		ArrayList<String> badLines = new ArrayList<>();
		int rejectedLines = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = reader.readLine();
			while (line != null) {
				TaxiRecord record = validate(line);
				if(record == null) {
					rejectedLines++;
					if(badLines.size() < 5) {
						badLines.add(line);
					}
					line = reader.readLine();
					continue;
				}
				if(!result.containsKey(record.hackLicense)) {
					result.put(record.hackLicense, new DriverStats());
				}
				DriverStats stats = result.get(record.hackLicense);
				stats.totalEarnings += record.totalAmount;
				stats.medallionSet.add(record.medallion);
				line = reader.readLine();
			}

		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Rejected lines: " + rejectedLines);
		if (badLines.isEmpty()) {
        	System.out.println("No erroneous lines found.");
		} 
		else {
        	for (String bad : badLines) {
            	System.out.println(bad);
        	}
    	}
		return result;
	}

	public static List<DataItem> convertItems(HashMap<String, DriverStats> map) {
		List<DataItem> result = new ArrayList<>();
		for (Map.Entry<String, DriverStats> entry : map.entrySet()) {
			String driverId = entry.getKey();
			DriverStats stats = entry.getValue();
			int medallionCount = stats.medallionSet.size();
			double totalEarnings = stats.totalEarnings;

			DataItem item = new DataItem(driverId, medallionCount, totalEarnings);
			result.add(item);
		}
		return result;
	}
}