/**
 * This file is part of EWItool.
 *
 *  EWItool is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EWItool is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EWItool.  If not, see <http://www.gnu.org/licenses/>.
 */

package ewitool;

import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author steve
 *
 */
public class SharedData extends Observable {
  
  public final static int NONE = -1;
  public final static int PATCH_LOADED = 1;
  public final static int EDIT_PATCH   = 2;
  public final static int EWI_CONNECTION = 3;
  
  private volatile int lastPatchLoaded;
  public  volatile boolean loadedQuickPCs;
  private volatile boolean ewiAttached, epxAvailable;
  private volatile int scratchPadCount;
  private volatile String statusMessage;
  private volatile long statusMillis;
  private volatile boolean midiMonitoring;
  
  enum DeviceIdResponse { WRONG_LENGTH, NOT_AKAI, NOT_EWI4000S, IS_EWI4000S }
  
  // We ASSUME that there are always NUM_EWI_PATCHES on this list once it is loaded...
  //public ArrayList<EWI4000sPatch> ewiPatchList;
  EWI4000sPatch[] ewiPatchList;
  
  public byte[] quickPCs;
  
  // Queues to synchronise requesting/receiving MIDI info
  BlockingQueue<Integer> patchQ; //, keyPatchQ;
  BlockingQueue<DeviceIdResponse> deviceIdQ;
  BlockingQueue<SendMsg> sendQ;
  BlockingQueue<MidiMonitorMessage> monitorQ;
  
  private String midiInDev, midiOutDev;
  
  SharedData() {
    lastPatchLoaded = NONE;
    ewiPatchList = new EWI4000sPatch[EWI4000sPatch.EWI_NUM_PATCHES];
    loadedQuickPCs = false;
    quickPCs = new byte[MidiHandler.EWI_NUM_QUICKPCS];
    patchQ = new LinkedBlockingQueue<>();
//    keyPatchQ = new LinkedBlockingQueue<>();
    deviceIdQ = new LinkedBlockingQueue<>();
    sendQ = new LinkedBlockingQueue<>();
    monitorQ = new LinkedBlockingQueue<>();
    ewiAttached = false;
    epxAvailable = false;
    midiInDev = "[Not set]";
    midiOutDev = "[Not set]";
    statusMessage = "";
    statusMillis = 0L;
    scratchPadCount = 0;
    midiMonitoring = false;
  }
  
  public void clear() {
    for ( EWI4000sPatch p : ewiPatchList ) p = null;
    setLastPatchLoaded( NONE );
  }
  
  public int getLastPatchLoaded() { return lastPatchLoaded; }
  public void setLastPatchLoaded( int p ) { lastPatchLoaded = p; }
  
  public boolean getEwiAttached() { return ewiAttached; }
  public void setEwiAttached( boolean isIt ) {
    if (isIt != ewiAttached) {
      ewiAttached = isIt; setChanged(); notifyObservers();
    }
  }
  public boolean getEpxAvailable() { return epxAvailable; }
  public void setEpxAvailable( boolean isIt ) {
    if (isIt != epxAvailable) {
      epxAvailable = isIt; setChanged(); notifyObservers();
    }
  }
  
  public String getMidiInDev() { return midiInDev; }
  public void setMidiInDev( String dev ) { midiInDev = dev; setChanged(); notifyObservers(); }
  public String getMidiOutDev() { return midiOutDev; }
  public void setMidiOutDev( String dev ) { midiOutDev = dev; setChanged(); notifyObservers(); }
  
  public int getScratchPadCount() { return scratchPadCount; }
  public void setScratchPadCount( int count ) { scratchPadCount = count; setChanged(); notifyObservers(); }
  
  public boolean getMidiMonitoring() { return midiMonitoring; }
  public void setMidiMonitoring( boolean areWe ) { midiMonitoring = areWe; setChanged(); notifyObservers(); }
  
  public String getStatusMessage() { return statusMessage; }
  public void setStatusMessage( String msg ) { statusMessage = msg; statusMillis = System.currentTimeMillis(); setChanged(); notifyObservers(); }
  public long getStatusMillis() { return statusMillis; }
  
}
