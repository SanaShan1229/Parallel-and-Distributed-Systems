// 1. Student Name: Sanchana Shanmuga
//    Student UT EID: ss229638

// 2. Student Name: Victoria Reddy
//    Student UT EID: vrr593
//
// ## Course Name: CS378
// ## Unique Number: 12345
// ## Date Created: 2026-09-13
package edu.utexas.cs.cs378;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * This class represents a data item with a string and two float values.
 * 
 * @author kiat
 *
 */
public class DataItem {

	private String driverId;
	private int medallionCount;
	private double totalEarnings;

	public DataItem() {

	}

	public DataItem(String driverId, int medallionCount, double totalEarnings) {
		super();
		this.driverId = driverId;
		this.medallionCount = medallionCount;
		this.totalEarnings = totalEarnings;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public int getMedallionCount() {
		return medallionCount;
	}

	public void setMedallionCount(int medallionCount) {
		this.medallionCount = medallionCount;
	}

	public double getTotalEarnings() {
		return totalEarnings;
	}

	public void setTotalEarnings(double totalEarnings) {
		this.totalEarnings = totalEarnings;
	}

	/**
	 * This method manually serializes a data object of this type into a byte array.
	 * Order of writing the data into byte array matters and should be de-serialized
	 * in the same order.
	 * 
	 * @return
	 */
	public byte[] handSerializationWithByteBuffer() {

		byte[] driverIdBytes = driverId.getBytes(Charset.forName("UTF-8"));
		// 8 bytes for each float number (two float numbers 16)
		// 4 byte for an integer to write the length of the string
		// lineBytes.length for the legth of the string.

		ByteBuffer byteBuffer = ByteBuffer.allocate(4 + driverIdBytes.length + 4 + 8);

		// 1. driverId
		// First length of it and then its bytes
		// Each string value might be of different size. We have to write down its
		// length.
		byteBuffer.putInt(driverIdBytes.length);
		byteBuffer.put(driverIdBytes);

		// 2. medallion
		byteBuffer.putInt(medallionCount);

		// 2. totalEarnings
		byteBuffer.putDouble(totalEarnings);

		return byteBuffer.array();
	}

	/**
	 * This method manually de-serializes an object of DataItem from a given byte
	 * array.
	 * 
	 * @param buf
	 * @return
	 */
	public DataItem deserializeFromBytes(byte[] buf) {

		ByteBuffer byteBuffer = ByteBuffer.wrap(buf);

		// 1. Read the line string back from the byte array.

		int stringSize = byteBuffer.getInt(); // 4 bytes
		String tmpLine = extractString(byteBuffer, stringSize);

		// 2. read a float from the given byte array
		int valueMedallionCount = byteBuffer.getInt();

		// 3. read the last float byte array back.
		double valueTotalEarnings = byteBuffer.getDouble();

		return new DataItem(tmpLine, valueMedallionCount, valueTotalEarnings);

	}

	/**
	 * This method reads a string from a buteBuffer.
	 * 
	 * @param byteBuffer
	 * @param stringSize
	 * @return
	 */
	String extractString(ByteBuffer byteBuffer, int stringSize) {
		byte[] stringBytes = new byte[stringSize];
		byteBuffer.get(stringBytes, 0, stringSize);

		String mystring = new String(stringBytes, Charset.forName("UTF-8"));
		return mystring;
	}

	@Override
	public String toString() {
		return "DataItem [driverId=" + driverId + ", medallionCount=" + medallionCount + ", totalEarnings=" + totalEarnings + "]";
	}

}
