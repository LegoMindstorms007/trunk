package Communication;

import java.io.IOException;

public class TurnTable {
	private BluetoothCommunication bluetooth;

	private enum TurnTableCommand {
		HELLO, TURN, DONE, CYA, UNKNOWN;

		public static TurnTableCommand getByOrdinal(int commandOrdinal) {
			if (commandOrdinal >= values().length) {
				return UNKNOWN;
			}
			return values()[commandOrdinal];
		}
	}

	public TurnTable() {
		bluetooth = new BluetoothCommunication();
	}

	public boolean connect() {
		String deviceName = "TurnTablePTOP";
		bluetooth.connect(deviceName, "00165304779A");
		return true;
	}

	public boolean turn() {
		try {
			sendCommand(TurnTableCommand.TURN);
			TurnTableCommand command = receiveCommand();
			assertCommand(command, TurnTableCommand.DONE);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean register() {
		try {
			TurnTableCommand command = receiveCommand();
			assertCommand(command, TurnTableCommand.HELLO);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean deregister() {
		try {
			sendCommand(TurnTableCommand.CYA);
		} catch (IOException e) {
			return false;
		}
		bluetooth.disconnect();
		return true;
	}

	private void assertCommand(TurnTableCommand command,
			TurnTableCommand assertetedCommand) throws IOException {
		if (command != assertetedCommand) {
			log("Invalid command:");
			log("Expected:" + assertetedCommand);
			throw new IOException("Invalid Command");
		}
	}

	private TurnTableCommand receiveCommand() throws IOException {
		int commandOrdinal = bluetooth.readInt();
		TurnTableCommand command = TurnTableCommand
				.getByOrdinal(commandOrdinal);
		log("Receive:" + command.name());
		return command;
	}

	private void sendCommand(TurnTableCommand command) throws IOException {
		bluetooth.writeInt(command.ordinal());
		log("Send: " + command.name());
	}

	private void log(String message) {
		System.out.println(message);
	}

	public boolean isConnected() {
		return bluetooth.isConnected();
	}

}