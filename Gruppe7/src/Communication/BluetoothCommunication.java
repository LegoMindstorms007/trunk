package Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/**
 * Bluetooth communication class
 * 
 * @author Dominik Muth
 * 
 */
public class BluetoothCommunication {
	private DataInputStream dis;
	private DataOutputStream dos;
	private BTConnection connection;
	private BTConnector btConnector;

	/**
	 * opens a connection to a server
	 * 
	 * @param server
	 *            name of the server (hope you already paired your device with
	 *            the server)
	 */
	public void connect(String server, String address) {
		btConnector = new BTConnector(this, server, address);
		btConnector.start();
		while (!btConnector.isRunning()) {
			sleep(10);
		}
	}

	/**
	 * 
	 * @return whether a bluetooth connection is held or not
	 */
	public boolean isConnected() {
		return btConnector != null && btConnector.isConnected();
	}

	private boolean openConnection(String server, String address) {
		RemoteDevice btrd = new RemoteDevice(server, address, 0);
		if (btrd == null) {
			LCD.drawString("No such device", 0, 2);
			return false;
		}

		connection = Bluetooth.connect(btrd);
		if (connection == null) {
			LCD.drawString("Connection failed", 0, 2);
			// connection failed, try again...
			return false;
		}

		// LCD.clear();
		LCD.drawString("Connected", 0, 0);
		dis = connection.openDataInputStream();
		dos = connection.openDataOutputStream();

		return (dis != null && dos != null);
	}

	/**
	 * method for sending an integer to the lift
	 * 
	 * @param value
	 *            integer to send
	 */
	public void writeInt(int value) {
		if (dos != null)
			try {
				dos.writeInt(value);
				dos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/**
	 * method for sending a boolean to the lift
	 * 
	 * @param value
	 *            boolean to send
	 */
	public void writeBool(boolean value) {
		if (dos != null)
			try {
				dos.writeBoolean(value);
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
		if (dis != null)
			try {
				value = dis.readBoolean();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return value;
	}

	/**
	 * method to read an integer variable
	 * 
	 * @return answer of the lift
	 */
	public int readInt() {
		int value = 0;
		if (dis != null)
			try {
				value = dis.readInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return value;
	}

	/**
	 * this method's name should be self explaining
	 */
	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
				if (dis != null)
					dis.close();
				if (dos != null)
					dos.close();
			} catch (IOException e) {
				// ignore
			}
		}

		if (btConnector != null)
			btConnector.setDisconnected();
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

	/**
	 * halts a connection try, if a connector is trying to astablish a
	 * connection right now
	 */
	public void halt() {
		if (btConnector != null)
			btConnector.halt();
	}

	/**
	 * inner class, made for connecting to the lift while not blocking the
	 * program
	 * 
	 * @author Dominik Muth
	 * 
	 */
	private class BTConnector extends Thread {
		private BluetoothCommunication com;
		private boolean running;
		private boolean connected;
		private String serverName;
		private String address;

		public BTConnector(BluetoothCommunication com, String serverName,
				String address) {
			this.com = com;
			this.serverName = serverName;
			this.address = address;
			connected = false;
		}

		@Override
		public void run() {
			running = true;
			connected = com.openConnection(serverName, address);
			while (running && !connected) {
				com.sleep(100);
				connected = com.openConnection(serverName, address);
			}
			running = false;
		}

		/**
		 * stops the bluetooth connector
		 */
		public void halt() {
			running = false;
		}

		/**
		 * 
		 * @return whether the connector is running or not
		 */
		public boolean isRunning() {
			return running;
		}

		/**
		 * 
		 * @return should be self explaining...
		 */
		public boolean isConnected() {
			return connected;
		}

		/**
		 * set this class as disconnected
		 */
		public void setDisconnected() {
			this.connected = false;
		}
	}
}
