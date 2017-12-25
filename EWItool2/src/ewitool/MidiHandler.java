/**
 * MidiHandler - This class controls the MIDI connections and their
 * respective events.
 * 
 * This file is part of EWItool.

    EWItool is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    EWItool is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with EWItool.  If not, see <http://www.gnu.org/licenses/>.
 */

package ewitool;

import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.concurrent.Task;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MidiHandler {

  //SysEx commands from Akai
  public final static byte MIDI_PRESET_DUMP      = 0x00;
  public final static byte MIDI_PRESET_DUMP_REQ  = 0x40;
  public final static byte MIDI_QUICKPC_DUMP     = 0x01;
  public final static byte MIDI_QUICKPC_DUMP_REQ = 0x41;
  public final static byte MIDI_EDIT_LOAD        = 0x10;
  public final static byte MIDI_EDIT_STORE       = 0x11;
  public final static byte MIDI_EDIT_DUMP        = 0x20;
  public final static byte MIDI_EDIT_DUMP_REQ    = 0x60;

  public final static byte MIDI_SYSEX_HEADER     = (byte) 0b11110000; // 0xf0
  public final static byte MIDI_SYSEX_TRAILER    = (byte) 0b11110111; // 0xf7
  public final static byte MIDI_SYSEX_GEN_INFO   = 0x06;
  public final static byte MIDI_SYSEX_ID_REQ     = 0x01;
  public final static byte MIDI_SYSEX_ID         = 0x02;
  public final static byte MIDI_SYSEX_AKAI_ID    = 0x47; // 71.
  public final static byte MIDI_SYSEX_AKAI_EWI4K = 0x64; // 100.
  public final static byte MIDI_SYSEX_CHANNEL    = 0x00;
  public final static byte MIDI_SYSEX_NONREALTIME = 0x7e;
  public final static byte MIDI_SYSEX_ALLCHANNELS = 0x7f;

  public final static byte MIDI_CC_DATA_ENTRY    = 0x06;
  public final static byte MIDI_CC_NRPN_LSB      = 0x62;
  public final static byte MIDI_CC_NRPN_MSB      = 0x63;

  public final static int  MAX_SYSEX_LENGTH      = 262144;
  public final static int  EWI_SYSEX_PRESET_DUMP_LEN = EWI4000sPatch.EWI_PATCH_LENGTH;
  public final static int  EWI_SYSEX_QUICKPC_DUMP_LEN = 91;
  public final static int  EWI_SYSEX_ID_RESPONSE_LEN = 15;

  public final static int  EWI_NUM_QUICKPCS      = 84;
  
  /** N.B. MIDI_TIMEOUT_MS must be significantly longer than MIDI_MESSAGE_LONG_PAUSE_MS
  * otherwise send & receive can get out of sync
  */
  public final static int  MIDI_MESSAGE_SHORT_PAUSE_MS = 100;
  public final static int  MIDI_MESSAGE_LONG_PAUSE_MS  = 250;
  public final static int  MIDI_TIMEOUT_MS            = 3000;

  /** This is used to temporarily IGNORE (not block) CC events on the sendQ.  The
   * events are deliberately lost.  Using this prevents the EWI being flooded with 
   * messages when the UI is being set up or redrawn.
   */
  public volatile boolean  ignoreEvents;
  
  SharedData sharedData;
  UserPrefs userPrefs;
  Thread sendThread;
  MidiDevice inDev = null, outDev = null;
  Receiver midiIn;
  
  MidiDevice.Info[] infos;

  MidiHandler( SharedData pSharedData, UserPrefs pUserPrefs ) {

    sharedData = pSharedData;
    userPrefs = pUserPrefs;
    ignoreEvents = false;
    
    /** 
     * This task was introduced to work around a hang on OS X in
     * the CoreMidi4J v0.4 MidiSystem.getMidiDeviceInfo() call which 
     * seems to encounter a resource lock if run on the main thread.
     */
    Task<Void> mt = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        scanAndOpenMIDIPorts();
        return null;
      }
    };
    new Thread( mt ).start();

    
    // This was unsafe w.r.t. other MIDI messages that may be in-flight.
    // If it is to be reintroduced at any point then it should probably wait for 
    // MIDI idle and somehow LOCK other MIDI traffic before each query
//    Task<Void> checkerTask = new Task<Void>() {
//      @Override
//      protected Void call() throws Exception {
//        while (true) {
//          if (inDev != null && outDev != null) {
//            if (inDev.isOpen() && outDev.isOpen()) {
//              Platform.runLater( () -> requestDeviceID() );
//            }
//          }
//          Thread.sleep( 15000 ); // 15s
//        }
//      }
//    };
//    new Thread( checkerTask ).start();
  }
  
  /** Go through all MIDI devices looking for Ports.  If the port matches 
   * either the IN or OUT port set in UserPrefs then open it.
   */
  private void scanAndOpenMIDIPorts() {
    
    MidiDevice device;
    
    infos = MidiSystem.getMidiDeviceInfo();
    for (int d = 0; d < infos.length; d++) {
      try {
        device = MidiSystem.getMidiDevice( infos[d] );
        if (!(device instanceof Sequencer) && !(device instanceof Synthesizer)) {
          if (device.getMaxReceivers() != 0) {
            if (infos[d].getName().equals( userPrefs.midiOutPort.getValue() )) {
              try {
                outDev = MidiSystem.getMidiDevice( infos[d] );
              } catch( MidiUnavailableException e ) {
                e.printStackTrace();
                errAlert( "Error - MidiHandler() could not obtain chosen MIDI OUT device info" );
              }
              try {
                outDev.open();
              } catch( MidiUnavailableException e1 ) {
                e1.printStackTrace();
                errAlert( "Error - MidiHandler() could not open chosen MIDI OUT device" );
              }
              if (outDev.isOpen()) {
                (sendThread = new Thread( new MidiSender( sharedData, outDev ) )).start();
                sendThread.setName( "EWItool MIDI Sender" );
                Debugger.log( "Debug - OUT Port: " + infos[d].getName());
                final String outName = infos[d].getName();
                Platform.runLater( () -> sharedData.setMidiOutDev( outName ) );
              } else {
                Debugger.log( "Debug - Could not open OUT Port: " + infos[d].getName());
              }
            }
          } else if (device.getMaxTransmitters() != 0) {
            if (infos[d].getName().equals( userPrefs.midiInPort.getValue() )) {
              try {
                inDev = MidiSystem.getMidiDevice( infos[d] );
              } catch( MidiUnavailableException e ) {
                e.printStackTrace();
                errAlert( "Error - MidiHandler() could not obtain chosen MIDI IN device info" );
              }
              try {
                inDev.open(); 
              } catch( MidiUnavailableException e1 ) {
                e1.printStackTrace();
                errAlert( "Error - MidiHandler() could not open chosen MIDI IN device" );
              }
              midiIn = new MidiReceiver( sharedData );
              inDev.getTransmitter().setReceiver( midiIn );
              Debugger.log( "Debug - IN Port: " + infos[d].getName() );
              final String inName = infos[d].getName();
              Platform.runLater( () -> sharedData.setMidiInDev( inName ) );
            }
          }
        }  
      } catch( MidiUnavailableException e ) {
        e.printStackTrace();
        errAlert( "Error - MidiHandler() could not obtain MIDI devices info" );
      }
    }
  }
  
  private static void errAlert( String msg ) {
    System.err.println( msg );
    Alert al = new Alert( AlertType.ERROR );
    al.setTitle( "EWItool - MIDI" );
    al.setContentText( msg );
    al.showAndWait();
  }

  public void close() {
    if (sendThread != null && sendThread.isAlive()) {
      sendThread.interrupt();
      try {
        sendThread.join();
      } catch( InterruptedException e ) {
      }
    }
    if (inDev !=null && inDev.isOpen()) { midiIn.close(); inDev.close(); }
    sharedData.setEwiAttached( false );
  }

  public void restart() {
    close();
    scanAndOpenMIDIPorts();   
    /* this is just a nice-to-have... */
    if (inDev != null && outDev != null)
      if (inDev.isOpen() && outDev.isOpen()) {
        requestDeviceID();
    }
  }

  void clearPatches() {
    sharedData.clear();
  }

  /**
   * Fetch a patch from the EWI
   * 
   * EWItool uses the DISPLAYED patch number internally, so here we must convert that 
   * to the EWI-internal patch number...
   * 
   * @param p 
   */
  void requestPatch( final int p ) {
    if (p < 0 || p >= EWI4000sPatch.EWI_NUM_PATCHES) {
      System.err.println( "Error - Attempt to request out-of-range patch (" + p + ")" );
      System.exit( 1 );
    }
    byte[] reqMsg = new byte[7];
    reqMsg[0] = MIDI_SYSEX_HEADER;
    reqMsg[1] = MIDI_SYSEX_AKAI_ID;	// 0x47 71.
    reqMsg[2] = MIDI_SYSEX_AKAI_EWI4K;	// 0x64 100.
    reqMsg[3] = MIDI_SYSEX_CHANNEL;	// 0x00
    reqMsg[4] = MIDI_PRESET_DUMP_REQ;	// 0x40 64.
    reqMsg[5] = (byte) sharedData.ewiPatchNums[p]; 
    reqMsg[6] = MIDI_SYSEX_TRAILER;	// 0xf7 -9.
    boolean gotIt = false;
    try {
      while (!gotIt) {
        Debugger.log( "DEBUG - MidiHandler Sending request for patch: " + p + " (Internal patch #: " + sharedData.ewiPatchNums[p] +")" );
        sendSysEx( reqMsg.clone(), SendMsg.DelayType.SHORT );
        // wait for a patch to be received, or timeout
        Integer pGot = sharedData.patchQ.poll( MIDI_TIMEOUT_MS, TimeUnit.MILLISECONDS );
        if (pGot == null)  {
          Debugger.log( "DEBUG - MidiHandler patch request timed out" );
          sharedData.patchQ.clear();
          // sendSystemReset();
        } else 	if (pGot == p) {
          gotIt = true;
        } else if (pGot != p) {
          Debugger.log( "DEBUG - MidiHandler Got out-of-sync patch: " + p );
          sharedData.patchQ.clear();
        } 
      }
    } catch( InterruptedException e ) {
      e.printStackTrace();
    }
  }

  synchronized void requestQuickPCs() {
    byte[] reqMsg = new byte[7];
    reqMsg[0] = MIDI_SYSEX_HEADER;
    reqMsg[1] = MIDI_SYSEX_AKAI_ID;
    reqMsg[2] = MIDI_SYSEX_AKAI_EWI4K;
    reqMsg[3] = MIDI_SYSEX_ALLCHANNELS;
    reqMsg[4] = MIDI_QUICKPC_DUMP_REQ;
    reqMsg[5] = 0x00;
    reqMsg[6] = MIDI_SYSEX_TRAILER;
    Debugger.log( "DEBUG - MidiHandler Sending request for all Quick PCs" );
    sharedData.loadedQuickPCs = false;
    sendSysEx( reqMsg.clone(), SendMsg.DelayType.SHORT );
  }

  synchronized void sendQuickPCs( byte[] pcs ) {
    if (pcs.length != EWI_NUM_QUICKPCS) {
      System.err.println( "Error - sendQuickPCs got wrong number of PCs" );
      System.exit( 1 );
    }
    byte[] sndMsg = new byte[6 + pcs.length + 1];
    sndMsg[0] = MIDI_SYSEX_HEADER;
    sndMsg[1] = MIDI_SYSEX_AKAI_ID;
    sndMsg[2] = MIDI_SYSEX_AKAI_EWI4K;
    sndMsg[3] = MIDI_SYSEX_ALLCHANNELS;
    sndMsg[4] = MIDI_QUICKPC_DUMP;
    sndMsg[5] = 0x00;
    for (int b = 0; b < pcs.length; b++)
      sndMsg[6 + b] = (pcs[b] >= 0 && pcs[b] < 100) ? pcs[b] : 0;
    sndMsg[6 + pcs.length] = MIDI_SYSEX_TRAILER;
    Debugger.log( "DEBUG - MidiHandler Sending dump of all Quick PCs to EWI" );
    sendSysEx( sndMsg.clone(), SendMsg.DelayType.LONG );
  }
  
  synchronized final boolean requestDeviceID() {
    byte[] reqMsg = new byte[6];
    reqMsg[0] = MIDI_SYSEX_HEADER;
    reqMsg[1] = MIDI_SYSEX_NONREALTIME;
    reqMsg[2] = MIDI_SYSEX_ALLCHANNELS;
    reqMsg[3] = MIDI_SYSEX_GEN_INFO;
    reqMsg[4] = MIDI_SYSEX_ID_REQ;
    reqMsg[5] = MIDI_SYSEX_TRAILER;
    Debugger.log( "DEBUG - MidiHandler requesting Device ID" );
    sendSysEx( reqMsg.clone(), SendMsg.DelayType.NONE );
    try {
      SharedData.DeviceIdResponse dId = sharedData.deviceIdQ.poll( MIDI_TIMEOUT_MS, TimeUnit.MILLISECONDS );
      if (dId == SharedData.DeviceIdResponse.IS_EWI4000S) {
        Debugger.log( "DEBUG - MidiHandler found EWI4000s" );
        sharedData.setEwiAttached( true );
        return true;
      } else if (dId == null) {
        Debugger.log( "DEBUG - MidiHandler did not find EWI4000s and timed out" );
        sharedData.setEwiAttached( false );
      } else {
        Debugger.log( "DEBUG - MidiHandler did not find EWI4000s and got unexpected response" );
        sharedData.setEwiAttached( false );
      }
    } catch( InterruptedException e ) {
      Debugger.log( "DEBUG - MidiHandler did not EWI4000s and was interrupted" );
    }
    //requestDeviceID();
    return false;
  }

  // all SysEx requests must go via this method - which is synchronised to avoid overlapping requests
  synchronized void sendSysEx( final byte[] sysexBytes, SendMsg.DelayType pause ) {
    SendMsg msg = new SendMsg();
    msg.msgType = SendMsg.MidiMsgType.SYSEX;
    msg.bytes = sysexBytes;
    msg.delay = pause;
    sharedData.sendQ.add( msg );
  }

  synchronized boolean sendPatch( EWI4000sPatch patch, final byte mode ) {
    patch.setPatchMode( mode );
    sendSysEx( patch.patchBlob, SendMsg.DelayType.LONG );
    if (mode == EWI4000sPatch.EWI_EDIT) {
      // if we're going to edit we need to send it again as patch 0 with the 
      // edit flag still set...  
      int pNum = patch.internalPatchNum;
      patch.setInternalPatchNum( (byte) 0x00 );
      sendSysEx( patch.patchBlob, SendMsg.DelayType.LONG );
      patch.setInternalPatchNum( (byte) pNum ); 
    }
    return true;
  }

  synchronized void sendControlChange( int cc, int val ) {
    SendMsg msg = new SendMsg();
    msg.msgType = SendMsg.MidiMsgType.CC;
    msg.cc = cc;
    msg.value = val;
    sharedData.sendQ.add( msg );
  }

  synchronized boolean sendLiveControl( int lsb, int msb, int cValue ) {
    if (!ignoreEvents) {
      sendControlChange( MIDI_CC_NRPN_LSB, lsb );
      sendControlChange( MIDI_CC_NRPN_MSB, msb );
      sendControlChange( MIDI_CC_DATA_ENTRY, cValue );
      sendControlChange( MIDI_CC_NRPN_LSB, 127 );
      sendControlChange( MIDI_CC_NRPN_MSB, 127 );
    }
    return true;
  }

  synchronized void sendSystemReset() {
    SendMsg msg = new SendMsg();
    msg.msgType = SendMsg.MidiMsgType.SYSTEM_RESET;
    sharedData.sendQ.add( msg );
  }

}
