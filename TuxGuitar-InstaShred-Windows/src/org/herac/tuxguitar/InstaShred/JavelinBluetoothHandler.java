package org.herac.tuxguitar.InstaShred;

import javelin.javelin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.util.ArrayList;
import java.util.Arrays;

public class JavelinBluetoothHandler {
	
	// CONSTANT FW VALUES
	private byte UPDATE = (byte) 0xff;
	private byte SPECIAL = (byte) 0x8a;
	private byte OFF = (byte) 0x02;
	private byte CONNECTED = (byte) 0x03;
	
	// Bluetooth Device Discovery variables
	// peripheralMap is going to be "device_ID" and MAC_ADDRESS
	private static final Map<String, String> peripheralMap = new HashMap<String, String>();
	private static final BLETableModel model = new BLETableModel(peripheralMap);
	private static int discoveredDevices = 0;
	
	private static String selectedPeripheral = null;
	private static String[] DEVICE_SERVICES;
	private static String[] SERVICE_CHARACTERISTICS;
	private static String dataService;
	private static String dataCharacteristic;
	
	// Begin scanning for peripherals
	void startScanning() {
		System.out.println("Scanning");
		
		// Create a JFrame to hold the JTable
		final JFrame frame = new JFrame("Discovered Devices");
		
		// Create a JTable that is linked to the peripheralMap
		final JTable bleTable = new JTable(model);
		bleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Add the JTable to a JScroll Pane
		JScrollPane scrollPane = new JScrollPane(bleTable);
		
		// Create the button panel for both buttons
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		
		// Create a button to pair
		JButton connectButton = new JButton("Connect");
		// Button click listener
				connectButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// Get the peripheral in the row that is selected
						int selectedRow = bleTable.getSelectedRow();
						
						// Check if a row is selected before connecting
						if (selectedRow != -1) {
							String selectedDevice = (String) bleTable.getValueAt(selectedRow, 0);
							selectedPeripheral = peripheralMap.get(selectedDevice);
							
							// This connects to the peripheral
							DEVICE_SERVICES = javelin.listBLEDeviceServices(selectedPeripheral);
							SERVICE_CHARACTERISTICS = javelin.listBLEServiceCharacteristics(selectedPeripheral, DEVICE_SERVICES[2]);
							
							System.out.print("DEVICE_SERVICES: ");
							System.out.println(Arrays.toString(DEVICE_SERVICES));
							// If SERVICE_CHARACTERISTICS is null then something has gone wrong
							System.out.print("SERVICE_CHARACTERISTICS: ");
							System.out.println(Arrays.toString(SERVICE_CHARACTERISTICS));
							
							// TODO: handle this error...
							if (SERVICE_CHARACTERISTICS == null) {
								// Error!
								System.out.println("Error connecting, please try again");
								// Run listBLEDevices again to re-jig the state (if you don't do this it crashes!)
								javelin.listBLEDevices();
								// Try again?
								// Seems to work but if not can remove below and just hope the user will press connect again :P
								DEVICE_SERVICES = javelin.listBLEDeviceServices(selectedPeripheral);
								SERVICE_CHARACTERISTICS = javelin.listBLEServiceCharacteristics(selectedPeripheral, DEVICE_SERVICES[2]);
								dataService = DEVICE_SERVICES[2];
								dataCharacteristic = SERVICE_CHARACTERISTICS[1];
								frame.dispose();
								sendConnected();
							}
							
							// close the search and send connected lightshow
							else {
								// This sets the correct service and characteristic to use
								// DEVICE_SERVICES[2] = 65333333-A115-11E2-9E9A-0800200CA100
								// SERVICE_CHARACTERISTICS[1] = 65333333-A115-11E2-9E9A-0800200CA102
								dataService = DEVICE_SERVICES[2];
								dataCharacteristic = SERVICE_CHARACTERISTICS[1];
								frame.dispose();
								sendConnected();
							}
						}
					}
				});
		
		// Create a button to scan
		JButton scanButton = new JButton("Scan for Devices");
		scanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String Devices[] = javelin.listBLEDevices();
				// Update the peripheralMap
				// TODO: only add if name starts with InstaShred- ?
				for (String device : Devices) {
					// device is in format [BluetoothType][DeviceID]-[MACAddress]
					// Address will always be the 1st index
					String deviceAddress = device.split("-", 2)[1];
					peripheralMap.put(deviceAddress, device);
					
					// If number of devices has grown, update
					int currentDiscoveredDevices = peripheralMap.size();
					if (currentDiscoveredDevices > discoveredDevices) {
						// Update table
						model.fireTableDataChanged();
						discoveredDevices = currentDiscoveredDevices;
					}
				}	
			}
		});
		
		// Add buttons to the buttonPanel
		buttonPanel.add(scanButton, BorderLayout.EAST);
		buttonPanel.add(connectButton, BorderLayout.WEST);
		
		// Add elements to frame
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.setSize(600, 300);
		frame.setVisible(true);
		// Puts frame in the middle of the screen
		frame.setLocationRelativeTo(null);
		// Puts frame on top of screen
		frame.setAlwaysOnTop(true);
	}
	
	// Lightshow commands
	public void sendConnected() {
		System.out.println("JavelinBluetoothHandler.java: Connected!");
		// first clear the fretboard
		this.clear();
		this.update();
		byte[] value = new byte[]{SPECIAL, CONNECTED, UPDATE};
		javelin.setBLECharacteristicValue(selectedPeripheral, dataService, dataCharacteristic, value);
	}
	
	public void sendData(byte[] data) {
		//System.out.println("Sending data");
		javelin.setBLECharacteristicValue(selectedPeripheral, dataService, dataCharacteristic, data);
		//myBluetoothPeripheral.writeCharacteristic(dataCharacteristic, data, WriteType.WITHOUT_RESPONSE);
	}
	
	public void sendMusicData(byte[] dataBuf, int len) {
		//System.out.println("JavelinBluetoothHandler.java: Sending note data");
		// bufLen is full length of msg, len is the check byte to send to guitar
		int bufLen = len*3;
		
		// data to send
		byte[] data = new byte[bufLen+1];
		
		// create the data buffer
		for (int i = 0; i <= bufLen; i++) {
			// all the note data
			if (i < bufLen) {
				data[i] = dataBuf[i];
			}
			// one byte length value
			else {
				data[i] = (byte) len;
			}
		}
		
		// send the data+len
		javelin.setBLECharacteristicValue(selectedPeripheral, dataService, dataCharacteristic, data);
		//myBluetoothPeripheral.writeCharacteristic(dataCharacteristic, data, WriteType.WITHOUT_RESPONSE);
	}
	
	public void clear() {
		//System.out.println("JavelinBluetoothHandler.java: Clearing the fretboard");
		byte[] data = new byte[]{SPECIAL, OFF};
		javelin.setBLECharacteristicValue(selectedPeripheral, dataService, dataCharacteristic, data);
	}
	
	public void update(){
		//System.out.println("JavelinBluetoothHandler.java: Updating the fretboard");
		byte[] data = new byte[]{UPDATE};
		javelin.setBLECharacteristicValue(selectedPeripheral, dataService, dataCharacteristic, data);
	}
	
	public boolean isConnected() {
		//System.out.println("checking if connected");
		if (dataCharacteristic != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void disconnect() {
		System.out.println("JavelinBluetoothHandler.java: setting peripheral to null");
		selectedPeripheral = null;
		// TODO: write something in the DLL which closes the connection with the peripheral and resets the discovered device list
	}
	
	// Constructor, create a BluetoothCentral Manager, begin scanning
	// TODO: ? maybe make this more compliant with Java "rules", I am not sure logic/actions are meant to be performed in the constructor
	public JavelinBluetoothHandler() {
		System.out.println("JavelinBluetoothHandler.java: Initialising Bluetooth"); 
		System.out.println("JavelinBluetoothHandler.java: JavelinBluetoothHandler Initialised");
		javelin.listBLEDevices();
		startScanning();	
	}
}
