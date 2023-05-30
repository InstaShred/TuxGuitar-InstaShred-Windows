package org.herac.tuxguitar.InstaShred;

import javax.swing.JOptionPane;
import java.util.Arrays;

import org.herac.tuxguitar.editor.event.TGRedrawEvent;
//import org.herac.tuxguitar.event.TGEventListener;
import org.herac.tuxguitar.event.TGEventManager;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.plugin.TGPlugin;
import org.herac.tuxguitar.util.plugin.TGPluginException;

// Maybe this is where we can check to see if BLE is available and if not then just exit the plugin load?
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBus;
import org.freedesktop.dbus.interfaces.Introspectable;

public class InstaShredPlugin implements TGPlugin {

	private ExtendedTGEventListener listener;
	
	@Override
	public String getModuleId() {
		return "tuxguitar-instashred";
	}

	@Override
	public void connect(TGContext context) throws TGPluginException {
		
		System.out.println("Connecting tuxguitar-instashred");
		
		if( this.listener == null) {
			this.listener = new RedrawListener(context);
			// addListener arguments is the event to fire on (TGRedrawEvent.EVENT_TYPE == "ui-redraw") and then the listener ID?
			TGEventManager.getInstance(context).addListener(TGRedrawEvent.EVENT_TYPE, this.listener);
		}
		
		// TODO: Do a Bluetooth Adapter check (similar to Linux plugin) before connecting
		
//		try {
//			// Creates a new DBus connection, attempts to get a remote object of org.bluez
//			DBusConnection conn = DBusConnection.getConnection(DBusConnection.DEFAULT_SYSTEM_BUS_ADDRESS);
//			DBus dbus = conn.getRemoteObject("org.freedesktop.DBus", "/org/freedesktop/DBus", DBus.class);
//			
//			// If org.bluez is in names then there is a bluez service available
//			String[] names = dbus.ListNames();
//			if (Arrays.asList(names).contains("org.bluez")) {
//				String bluezObject = conn.getRemoteObject("org.bluez", "/org/bluez/hci0", Introspectable.class).Introspect();
//				// Check if introspect string contains the bluez Adapter
//				if (bluezObject.contains("org.bluez.Adapter1")) {
//					System.out.println("Adapter Available! :)");
//					if( this.listener == null) {
//						this.listener = new RedrawListener(context);
//						// addListener arguments is the event to fire on (TGRedrawEvent.EVENT_TYPE == "ui-redraw") and then the listener ID?
//						TGEventManager.getInstance(context).addListener(TGRedrawEvent.EVENT_TYPE, this.listener);
//					}
//				}
//				else {
//					System.out.println("Adapter Not Available! :(");
//					JOptionPane.showMessageDialog(null, "tuxguitar-instashred: Bluetooth Adapter Not Available!");
//					//this.listener.disconnect();
//				}
//			} 
//			else {
//				System.out.println("Bluez Not Available :(");
//				JOptionPane.showMessageDialog(null, "tuxguitar-instashred: BlueZ Not Available!");
//			}	
//		} catch (DBusException e1) {
//			e1.printStackTrace();
//			this.listener.disconnect();
//		}
	}

	@Override
	public void disconnect(TGContext context) throws TGPluginException {
		if( this.listener != null ) {
			System.out.println("InstaShredPlugin.java: disconnect");
			this.listener.disconnect();
			System.out.println("Disconnecting tuxguitar-instashred");
			
			TGEventManager.getInstance(context).removeListener(TGRedrawEvent.EVENT_TYPE, this.listener);	
			this.listener = null;
		}
	}
}
