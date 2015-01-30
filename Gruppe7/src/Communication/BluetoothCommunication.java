package Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BluetoothCommunication {
	private DataInputStream dis;
	private DataOutputStream dos;
	private BTConnection connection;

	/**
	 * opens a connection to a server
	 * 
	 * @param server
	 *            name of the server (hope you already paired your device with
	 *            the server)
	 * @return if the connection could be established or not
	 */
	public boolean openConnection(String server) {
		RemoteDevice btrd = Bluetooth.getKnownDevice(server);

		if (btrd == null) {
			// no such device, you should pair your devices first or check the
			// Devices name
			return false;
		}

		connection = Bluetooth.connect(btrd);

		if (connection == null) {
			// connection failed, try again...
			return false;
		}

		// LCD.clear();
		LCD.drawString("Connected", 0, 0);
		dis = connection.openDataInputStream();
		dos = connection.openDataOutputStream();

		return true;
	}

	/**
	 * method for sending an integer to the lift
	 * 
	 * @param value
	 *            integer to send
	 */
	public void writeInt(int value) {
		try {
			dos.writeInt(value);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * method to read a boolean variable
	 * 
	 * @return answer of the lift
	 */
	public boolean readBool() {
		boolean value = false;
		try {
			value = dis.readBoolean();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * this method's name should be self explaining
	 */
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
				dis.close();
				dos.close();
			} catch (IOException e) {
				// ignore
			}
		}
		LCD.drawString("Disconnected", 0, 0);
	}

	/**
	 * well, forces the thread to sleep...
	 * 
	 * @param millis
	 *            time to sleep in milliseconds
	 */
	public void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}