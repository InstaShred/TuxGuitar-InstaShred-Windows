//package org.herac.tuxguitar.InstaShred;
//
//import com.welie.blessed.*;
//import com.welie.blessed.BluetoothGattCharacteristic.WriteType;
//
//import javelin.javelin;
//
//import java.awt.BorderLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.ListSelectionModel;
//
//// from the example https://github.com/weliem/blessed-bluez/blob/master/src/main/java/testapp/BluetoothHandler.java
//// Handles the bluetooth connection
//// connects based on the CYBT service UUID's so should be universal << incorrect as of 2023
//// once connected it then sends a connected lightshow and sets the peripheral to myBluetoothPeripheral
//public class BluetoothHandler {
//	
//	// Definitions
//	private final BluetoothCentralManager central;
//	private BluetoothPeripheral myBluetoothPeripheral;
//	private BluetoothGattCharacteristic dataCharacteristic;
//	private BluetoothGattCharacteristic writeWithResponseCharacteristic;
//	
//	// CONSTANT FW VALUES
//	private byte UPDATE = (byte) 0xff;
//	private byte SPECIAL = (byte) 0x8a;
//	private byte OFF = (byte) 0x02;
//	private byte CONNECTED = (byte) 0x03;
//	
//	// UUID for data "service"
//	private static final UUID CYBT_SERVICE_UUID = UUID.fromString("65333333-a115-11e2-9e9a-0800200ca100");
//	// UUID for write with response characteristic
//	private static final UUID DATA_WRITE_WITH_RESPONSE_UUID = UUID.fromString("65333333-a115-11e2-9e9a-0800200ca101");
//	// UUID for write characteristic
//	private static final UUID DATA_WRITE_UUID = UUID.fromString("65333333-A115-11E2-9E9A-0800200CA102");
//	
//	// Bluetooth Device Discovery variables
//	private static final Map<String, BluetoothPeripheral> peripheralMap = new HashMap<String, BluetoothPeripheral>();
//	private static final BLETableModel model = new BLETableModel(peripheralMap);
//	private static int discoveredDevices = 0;
//	
//	private final BluetoothPeripheralCallback peripheralCallback = new BluetoothPeripheralCallback() {
//		
//		public void onServicesDiscovered() {}
//		
//		public void onNotificationStateUpdate() {}
//		 
//		public void onCharacteristicUpdate() {}
//		
//	};
//	
//	private final BluetoothCentralManagerCallback bluetoothCentralManagerCallback = new BluetoothCentralManagerCallback() {
//
//		@Override
//		// Will run when a peripheral is discovered, update the peripheralMap Map
//        public void onDiscoveredPeripheral(BluetoothPeripheral peripheral, ScanResult scanResult) {
//			
//			// TODO: Implement logic that only adds peripherals to the Map if they begin with "InstaShred-" ?
//			
//            // Add it to the map
//            peripheralMap.put(peripheral.getAddress(), peripheral);
//            
//            // Logic to determine whether or not to update JTable
//            int currentDiscoveredDevices = peripheralMap.size();
//            if (currentDiscoveredDevices > discoveredDevices) {
//            	model.fireTableDataChanged();
//            	discoveredDevices = currentDiscoveredDevices;
//            }
//        }
//		
//		@Override
//		public void onConnectedPeripheral(BluetoothPeripheral peripheral) {
//			
//			// Set the peripheral and characteristic variables
//			myBluetoothPeripheral = peripheral;
//			dataCharacteristic = myBluetoothPeripheral.getCharacteristic(CYBT_SERVICE_UUID, DATA_WRITE_UUID);
//			writeWithResponseCharacteristic = myBluetoothPeripheral.getCharacteristic(CYBT_SERVICE_UUID, DATA_WRITE_WITH_RESPONSE_UUID);
//			
//			// Send the connected lightshow
//			sendConnected();
//		}
//	};
//	
//	// Begin scanning for peripherals
//	void startScanning() {
//		System.out.println("Scanning");
//		
//		// Create a JFrame to hold the JTable
//		final JFrame frame = new JFrame("Discovered Devices");
//		
//		// Create a JTable that is linked to the peripheralMap
//		final JTable bleTable = new JTable(model);
//		bleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		
//		// Add the JTable to a JScroll Pane
//		JScrollPane scrollPane = new JScrollPane(bleTable);
//		
//		// Create a button to pair
//		JButton connectButton = new JButton("Connect");
//		// Button click listener
//		connectButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				// Get the peripheral in the row that is selected
//				int selectedRow = bleTable.getSelectedRow();
//				String selectedDevice = (String) bleTable.getValueAt(selectedRow, 0);
//				BluetoothPeripheral selectedPeripheral = peripheralMap.get(selectedDevice);
//				central.stopScan();
//				central.connectPeripheral(selectedPeripheral, peripheralCallback);
//				// Close the search table on connection
//				frame.dispose();
//			}
//		});
//		
//		// Add elements to frame
//		frame.add(scrollPane, BorderLayout.CENTER);
//		frame.add(connectButton, BorderLayout.SOUTH);
//		frame.setSize(600, 300);
//		frame.setVisible(true);
//		// Puts frame in the middle of the screen
//		frame.setLocationRelativeTo(null);
//		// Puts frame on top of screen
//		frame.setAlwaysOnTop(true);
//		
//		// Scan for peripherals with a certain service UUID (this is unique to the CYBT EZ-Serial FW)
//		//central.scanForPeripheralsWithServices(new UUID[]{CYBT_SERVICE_UUID});	
//		central.scanForPeripherals();
//	}
//	
//	// Lightshow commands
//	public void sendConnected() {
//		System.out.println("Connected!");
//		byte[] value = new byte[]{SPECIAL, CONNECTED, UPDATE};
//		myBluetoothPeripheral.writeCharacteristic(dataCharacteristic, value, WriteType.WITHOUT_RESPONSE);
//	}
//	
//	public void sendData(byte[] data) {
//		//System.out.println("Sending data");
//		myBluetoothPeripheral.writeCharacteristic(dataCharacteristic, data, WriteType.WITHOUT_RESPONSE);
//	}
//	
//	public void sendMusicData(byte[] dataBuf, int len) {
//		//System.out.println("Sending note data");
//		// bufLen is full length of msg, len is the check byte to send to guitar
//		int bufLen = len*3;
//		
//		// data to send
//		byte[] data = new byte[bufLen+1];
//		
//		// create the data buffer
//		for (int i = 0; i <= bufLen; i++) {
//			// all the note data
//			if (i < bufLen) {
//				data[i] = dataBuf[i];
//			}
//			// one byte length value
//			else {
//				data[i] = (byte) len;
//			}
//		}
//		
//		// send the data+len
//		myBluetoothPeripheral.writeCharacteristic(dataCharacteristic, data, WriteType.WITHOUT_RESPONSE);
//	}
//	
//	public void clear() {
//		//System.out.println("Clearing the fretboard");
//		byte[] data = new byte[]{SPECIAL, OFF};
//		myBluetoothPeripheral.writeCharacteristic(dataCharacteristic, data, WriteType.WITHOUT_RESPONSE);
//	}
//	
//	public void update(){
//		//System.out.println("Updating the fretboard");
//		byte[] data = new byte[]{UPDATE};
//		myBluetoothPeripheral.writeCharacteristic(dataCharacteristic, data, WriteType.WITHOUT_RESPONSE);
//	}
//	
//	public boolean isConnected() {
//		if (myBluetoothPeripheral != null) {
//			return true;
//		}
//		else {
//			return false;
//		}
//	}
//	
//	public void disconnect() {
//		System.out.println("Disconnecting");
//		myBluetoothPeripheral.cancelConnection();
//	}
//	
//	// Constructor, create a BluetoothCentral Manager, begin scanning
//	// TODO: ? maybe make this more compliant with Java "rules", I am not sure logic/actions are meant to be performed in the constructor
//	public BluetoothHandler() {
//		System.out.println("Initialising Bluetooth"); 
//		central = new BluetoothCentralManager(bluetoothCentralManagerCallback);
//		System.out.println("BluetoothCentralManagerCallback Initialised");
//		startScanning();	
//	}
//}
