package org.herac.tuxguitar.InstaShred;

// Class that handles the conversion from string/fret data to LED data that works with the firmware
public class InstaShredLeds {
	
	// Data buffer
	public byte[] data;
	
	// Guitar Constants
	private int NO_LEDS = 137;
	private int NO_FRETS = 22;
	private int NO_STRINGS = 5;
	
	private int[][] FRET_ARRAY = {
		{137, 136, 135, 134, 133, 132},
		{126, 127, 128, 129, 130, 131},
		{125, 124, 123, 122, 121, 120}, 
		{114, 115, 116, 117, 118, 119}, 
		{113, 112, 111, 110, 109, 108},
		{102, 103, 104, 105, 106, 107},
		{101, 100, 99, 98, 97, 96},
		{90, 91, 92, 93, 94, 95},
		{89, 88, 87, 86, 85, 84},
		{78, 79, 80, 81, 82, 83}, 
		{77, 76, 75, 74, 73, 72}, 
		{66, 67, 68, 69, 70, 71},
		{65, 64, 63, 62, 61, 60},
		{54, 55, 56, 57, 58, 59},
		{53, 52, 51, 50, 49, 48},
		{42, 43, 44, 45, 46, 47},
		{41, 40, 39, 38, 37, 36},
		{30, 31, 32, 33, 34, 35},
		{29, 28, 27, 26, 25, 24},
		{18, 19, 20, 21, 22, 23},
		{17, 16, 15, 14, 13, 12},
		{6, 7, 8, 9, 10, 11},
		{5, 4, 3, 2, 1, 0}
	};
	
	// take int 0-255 and return byte 0-7
	public byte mapToByte(int RGB) {
		if (RGB < 32) {
			return 0x00;
		}
		else if (RGB < 64) {
			return 0x01;
		}
		else if (RGB < 96) {
			return 0x02;
		}
		else if (RGB < 128) {
			return 0x03;
		}
		else if (RGB < 160) {
			return 0x04;
		}
		else if (RGB < 192) {
			return 0x05;
		}
		else  if (RGB < 224) {
			return 0x06;
		}
		else {
			return 0x07;
		}
	}
	
	// call this function to return the led indices
	public int convertToIndex(int stringValue, int fretValue) {
		
		// initialise to -1 (to catch cases of string = 7 etc)
		int index = -1;
		// Map the string/fret combo to the instashred led array
		// because of the way the FRET_ARRAY is defined need to reverse
		
		// Make sure fretValue and stringValue are bounded
		if ((fretValue <= NO_FRETS+1 && fretValue >= 0) && (stringValue <= NO_STRINGS+1 && stringValue >= 0)) {
			index = FRET_ARRAY[NO_FRETS-fretValue][NO_STRINGS-(stringValue-1)];
		}
		return index;
	}
	
	// Takes RGB in 0-255 and converts to the 2-byte array representation
	public byte[] convertToColour(int red, int green, int blue) {
		byte[] colourData = {0, 0};
		
		// Map to 0-7 (000 -> 111)
		byte redScaled = mapToByte(red);
		byte greenScaled = mapToByte(green);
		byte blueScaled = mapToByte(blue);
		
		// construct the two byte payload
		colourData[0] = (byte) (redScaled << 4 | greenScaled);
		colourData[1] = (byte) (blueScaled << 4);
		
		return colourData;
	}
	
	// set the string and open fret colours
	// Open fret = WHITE
	// E = Green, A = Red, D = Blue, G = Yellow, B = Pink, e = Teal?
	public byte[] stringAndFretColour(int string, int fret, boolean isDead) {
		
		// if fret = X, return dull red
		// else return white
		if (fret == 0) {		
			if (isDead) {
				return convertToColour(110, 0, 55);
			}
			else {
				return convertToColour(255, 255, 255);
			}
		}
		
		// now check each string value, 0-5
		switch (string) {
			// e 
			case 1:
				return convertToColour(0, 255, 255);
			// B pink? (looks red)
			case 2:
				return convertToColour(255, 53, 184);
			// G Yellow 
			case 3:
				return convertToColour(255, 255, 0);
			// D Blue
			case 4:
				return convertToColour(0, 0, 255);
			// A Red
			case 5:
				return convertToColour(255, 0, 0);
			// E Green
			case 6:
				return convertToColour(0, 255, 0);
			
			default:
				return convertToColour(255, 255, 255);
		}
		
	}
	
	// Constructor
	public InstaShredLeds() {
		System.out.println("InstaShredLeds.java: InstaShred init");
	}
}
