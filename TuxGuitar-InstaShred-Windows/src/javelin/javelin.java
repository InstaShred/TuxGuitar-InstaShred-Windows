package javelin;

import java.io.File;

// From: https://github.com/hdonk/javelin
public class javelin {
	public static native String[] listBLEDevices();
	public static native String getBLEDeviceName(String a_dev_id);
	public static native String[] listBLEDeviceServices(String a_dev_id);
	public static native String[] listBLEServiceCharacteristics(String a_dev_id, String a_service_uuid);
	public static native byte[] getBLECharacteristicValue(String a_dev_id, String a_service_uuid, String a_characterics_uuid);
	public static native boolean setBLECharacteristicValue(String a_dev_id, String a_service_uuid, String a_characterics_uuid, byte[] a_value);
	
	public static native boolean watchBLECharacteristicChanges(String a_dev_id, String a_service_uuid, String a_characterics_uuid);
	public static native boolean clearBLECharacteristicChanges(String a_dev_id, String a_service_uuid, String a_characterics_uuid);
	public static native byte[] waitForBLECharacteristicChanges(String a_dev_id, String a_service_uuid, String a_characterics_uuid,
			int a_timeout_ms);
	public static native boolean unWatchBLECharacteristicChanges(String a_dev_id, String a_service_uuid, String a_characterics_uuid);
	
	static {
		
		// From: https://stackoverflow.com/questions/10691718/portable-statement-to-load-jni-library-from-a-different-directory-using-relative
		File lib = new File("share/plugins/" + System.mapLibraryName("javelin_x32"));
		System.out.println("javelin.java: Loading: " + lib.getAbsolutePath());
		System.load(lib.getAbsolutePath());
		System.out.println("javelin.java: javelin_x32 loaded");
	}
	
	public javelin() {
		System.out.println("Javelin JNI C++ BLE Bridge Init");
	}
}