# TuxGuitar-InstaShred-Windows
A TuxGuitar plugin to enable the use of this software with InstaShred guitars

Made with modifications to the [javelin](https://github.com/hdonk/javelin) github project from hdonk and the [TuxGuitar Redraw Listener Plugin](https://sourceforge.net/p/tuxguitar/support-requests/37/) provided by Julian Casadesus.

## Description
TuxGuitar is a free and open-source multitrack guitar tablature editor and player written in Java. It can open/edit GuitarPro, PowerTab and TablEdit files. More information on TuxGuitar can be found at its [wiki page](https://en.wikipedia.org/wiki/TuxGuitar). This TuxGuitar-InstaShred-Windows plugin allows for the notes and chords of songs opened in TuxGuitar to be displayed on an InstaShred LED-enabled guitar in [realtime!](https://www.youtube.com/watch?v=LtRkjv9bZKI) 

More information on InstaShred guitars can be found at our [official website](https://www.instashred.com.au/). Happy shredding! 🎸🎼🎵

## Requirements
TuxGuitar-InstaShred-Windows requires your computer to have a BLE/Bluetooth module, TuxGuitar installed (Java 9 minimum requirement) and an InstaShred guitar.  

## Installation Instructions
Download and install the appropriate Windows release from the [Source Forge project site](https://sourceforge.net/projects/tuxguitar/files/TuxGuitar/).

Navigate to the [releases page](https://github.com/InstaShred/TuxGuitar-InstaShred-Windows/releases) and download both the javelin_x32.dll and tuxguitar-instashred--SNAPSHOT.jar files. Save both of these files in the /share/plugins directory of your TuxGuitar installation. If you are having trouble finding this directory, right click your TuxGuitar shortcut and select "Open file location".

Run TuxGuitar.exe, and the Bluetooth search window (Discovered Devices) should open automatically. If not go to "Tools > Plugins" and make sure the "InstaShred" plugin is enabled. Once the Bluetooth search window appears, press "Scan for Devices" and then select your InstaShred guitar before pressing "Connect". After a successful connection, the connected lightshow will display on your guitar and the Bluetooth search window will close. 

You are now ready to use TuxGuitar with your InstaShred guitar!

## Troubleshooting
