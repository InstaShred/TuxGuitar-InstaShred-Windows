package org.herac.tuxguitar.InstaShred;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JTable;

import org.herac.tuxguitar.app.transport.TGTransport;
import org.herac.tuxguitar.app.view.component.tab.TablatureEditor;
import org.herac.tuxguitar.editor.event.TGRedrawEvent;
import org.herac.tuxguitar.event.TGEvent;
import org.herac.tuxguitar.event.TGEventListener;
import org.herac.tuxguitar.player.base.MidiPlayer;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.song.models.TGNote;

// BLE Stuff
import javelin.javelin;

// used to be RedrawListenerExample
public class RedrawListener implements ExtendedTGEventListener {

	public static String ID = "Receiver Example";
	
	private TGContext context;
	private TGBeat currentBeat;
	
	// ArrayList to hold all the notes in a beat
	private ArrayList<TGNote> notes = new ArrayList<TGNote>();
	
	// Create the InstaShredLeds class
	public InstaShredLeds leds = new InstaShredLeds();
	
	// Create the Bluetooth Handler
	public JavelinBluetoothHandler ble = new JavelinBluetoothHandler();
	
	// Create byte array to store data in
	// MSG_MAX because 6 strings * 3 bytes (per LED) = 18
	private int MSG_MAX = 18;
	public int msgLength = 0;
	public byte[] data = new byte[MSG_MAX];
	
	public RedrawListener(TGContext context) {
		this.context = context;
	}
	
	// Will be called every time event "ui-redraw" happens
	public void processEvent(TGEvent event) {
		if( TGRedrawEvent.EVENT_TYPE.equals(event.getEventType()) ) {
			processRedrawEvent(event);
		}
	}
	
	// Called every time there is a redraw
	public void processRedrawEvent(TGEvent event) {
		int type = ((Integer)event.getAttribute(TGRedrawEvent.PROPERTY_REDRAW_MODE)).intValue();
		
		// TGRedrawEvent.NORMAL: When tuxguitar needs redraw editor. 
		// TGRedrawEvent.PLAYING_NEW_BEAT: This event is fired every X time when player is running 
		// 1 = NORMAL, 2 = PLAYING_THREAD, 3 = PLAYING_NEW_BEAT << this is the one we care about
		// System.out.println("Redraw event received. type: " + type);
		
		TGBeat beat = null;
		if( MidiPlayer.getInstance(this.context).isRunning()){
			beat = TGTransport.getInstance(this.context).getCache().getPlayBeat();
		} else{
			beat = TablatureEditor.getInstance(this.context).getTablature().getCaret().getSelectedBeat();
		}
			
		// Only do this if ble module is connected
		if( (beat != null) && (ble.isConnected())) {
			
			// check if beat was changed since last redraw. 
			if( this.currentBeat == null || !this.currentBeat.equals(beat)) {
				this.currentBeat = beat;
				
				// System.out.println("Selected beat: " + this.currentBeat.getStart());
				// this is where we will call the InstaShred LED functions...
				// grabs all notes and puts into the notes ArrayList
				notes = (ArrayList<TGNote>) this.currentBeat.getVoice(0).getNotes();
				
				// if notes array exists
				if (notes.size() > 0) {
					
					boolean isTied = false;
					
					// Check if any of the notes are "tied" which means they'll play twice
//					for (int i = 0; i < notes.size(); i++) {
//						if (notes.get(i).isTiedNote()) {
//							isTied = true;
//						}
//						else {
//							isTied = false;
//						}
//					}
					
					// if no tied notes, only then process and send
					if (!isTied) {
						
						// clear guitar
						ble.clear();
						ble.update();
						
						// iterate over notes
						//System.out.println("New beat");
						for (int i = 0; i < notes.size(); i++) {
							int string = notes.get(i).getString();
							int value = notes.get(i).getValue();
							boolean isDead = notes.get(i).getEffect().isDeadNote();
//							System.out.print("\tNote " + i + " String: " + string);
//							System.out.print(", Value: " + value);
//							//System.out.print(notes.get(i).isTiedNote());
//							System.out.print(", InstaShred Index: " + leds.convertToIndex(string, value));
//							System.out.println("");
							
							// set values in data byte array
							// set index
							data[i*3] = (byte)leds.convertToIndex(string, value);
							
							// set colour bytes
							data[(i*3)+1] = leds.stringAndFretColour(string, value, isDead)[0];
							data[(i*3)+2] = leds.stringAndFretColour(string, value, isDead)[1];
							
							// update length (increment each note)
							msgLength++;
						}
						
						// Debug print
//						System.out.print("\tData: ");
//						for (int i = 0; i < msgLength*3; i++) {
//							System.out.print(String.format("0x%02X", data[i]) + " ");
//						}
//						System.out.println("");
						
						// Send LED bytes to guitar (18 byte chunks)
						ble.sendMusicData(data, msgLength);
						ble.update();
						
						// reset the msgLength counter
						msgLength = 0;
					}
				}
				
				// if no notes exist, clear the guitar
				else if (ble.isConnected()) {
					ble.clear();
					ble.update();
				}
			}
		}
	} 

	@Override
	public void disconnect() {
		System.out.println("RedrawListener.java: Disconnecting the redraw listener");
		// Perform this in a new thread to allow it to update the fretboard while closing the program
		new Thread(new Runnable() {
			public void run() {
				if (ble.isConnected()) {
					ble.clear();
					ble.update();
					ble.disconnect();
				}
				
				else {
					System.out.println("RedrawListener.java: not connected");
				}
			}
		}).start();
		
		// TODO: figure out how to disconnect the peripheral and then reconnect - might have to be done in the cpp file
	}
}
