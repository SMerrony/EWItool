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

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.SysexMessage;

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
  public final static byte MIDI_SYSEX_ALLCHANNELS = 0x7f;

  public final static byte MIDI_CC_DATA_ENTRY    = 0x06;
  public final static byte MIDI_CC_NRPN_LSB      = 0x62;
  public final static byte MIDI_CC_NRPN_MSB      = 0x63;
  
  public final static int  MAX_SYSEX_LENGTH      = 262144;
  public final static int  EWI_SYSEX_PRESET_DUMP_LEN = EWI4000sPatch.EWI_PATCH_LENGTH;
  public final static int  EWI_SYSEX_QUICKPC_DUMP_LEN = 91;
  public final static int  EWI_SYSEX_ID_RESPONSE_LEN = 15;
  public final static int  MIDI_TIMEOUT_MS       = 3000;
  
  SharedData sharedData;
  UserPrefs userPrefs;
  MidiDevice inDev = null, outDev = null;
  Receiver midiIn, midiOut;
  
  MidiHandler( SharedData pSharedData, UserPrefs pUserPrefs ) {
    
    sharedData = pSharedData;
    userPrefs = pUserPrefs;
     
    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
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
		System.err.println( "Error - MidiHandler() could not obtain chosen MIDI OUT device info" );
	      }
	      try {
		outDev.open();
	      } catch( MidiUnavailableException e1 ) {
		e1.printStackTrace();
		System.err.println( "Error - MidiHandler() could not open chosen MIDI OUT device" );
	      }
	      try {
		midiOut = outDev.getReceiver();
	      } catch( MidiUnavailableException e ) {
		e.printStackTrace();
		System.err.println( "Error - MidiHandler() could not get chosen MIDI OUT receiver" );
	      }
	      Thread sendThread;
	      (sendThread = new Thread( new MidiSender( sharedData.sendQ, midiOut ) )).start();
	      sendThread.setName( "EWItool MIDI Sender" );
/*	      try {
		outDev.getTransmitter().setReceiver( midiOut );
	      } catch( MidiUnavailableException e ) {
		e.printStackTrace();
		System.err.println( "Error - MidiHandler() could not set chosen MIDI OUT receiver" );
	      }*/
	      System.out.println( "Debug - OUT Port: " + infos[d].getName());
	    }
	  } else if (device.getMaxTransmitters() != 0) {
	    if (infos[d].getName().equals( userPrefs.midiInPort.getValue() )) {
	      try {
	        inDev = MidiSystem.getMidiDevice( infos[d] );
	      } catch( MidiUnavailableException e ) {
		e.printStackTrace();
		System.err.println( "Error - MidiHandler() could not obtain chosen MIDI IN device info" );
	      }
	      try {
	        inDev.open(); 
	      } catch( MidiUnavailableException e1 ) {
		e1.printStackTrace();
		System.err.println( "Error - MidiHandler() could not open chosen MIDI IN device" );
	      }
	      midiIn = new MidiReceiver( sharedData );
	      inDev.getTransmitter().setReceiver( midiIn );
	      System.out.println( "Debug - IN Port: " + infos[d].getName() );
	    }
	  }
	}  
      } catch( MidiUnavailableException e ) {
	e.printStackTrace();
	System.err.println( "Error - MidiHandler() could not obtain MIDI devices info" );
      }
    }

  }

  public void close() {
    if (outDev != null && outDev.isOpen()) { midiOut.close(); outDev.close(); }
    if (inDev !=null && inDev.isOpen()) { midiIn.close(); inDev.close(); }
  }

  /**
   * 
   */
  public void restart() {

    close();
    
    // TODO Restart MIDI connections
    System.err.println( "Error - MidiHandler.restart() not yet implemented" );

  }
  
  void clearPatches() {
    sharedData.clear();
  }
  
  void requestPatch( final int p ) {
    
    if (p < 0 || p >= EWI4000sPatch.EWI_NUM_PATCHES) {
      System.err.println( "Error - Attempt to request out-of-range patch (" + p + ")" );
      System.exit( 1 );
    }
    
    byte[] reqMsg = new byte[6];
    reqMsg[0] = MIDI_SYSEX_AKAI_ID;
    reqMsg[1] = MIDI_SYSEX_AKAI_EWI4K;
    reqMsg[2] = MIDI_SYSEX_CHANNEL;
    reqMsg[3] = MIDI_PRESET_DUMP_REQ;
    reqMsg[4] = (byte) p;
    reqMsg[5] = MIDI_SYSEX_TRAILER;
    boolean gotIt = false;
    try {
      while (!gotIt) {
	while (!sharedData.patchQ.isEmpty()) {
	  System.out.println( "DEBUG - MidiHandler waiting for patchQ to empty before reqeusting (next) patch" ); 
	  Thread.sleep( 250 );
	}
	System.out.println( "DEBUG - MidiHandler Sending request for patch: " + p );
	if (!sendSysEx( reqMsg )) continue;

	// wait for a patch to be received, or timeout
	Integer pGot = sharedData.patchQ.poll( MIDI_TIMEOUT_MS, TimeUnit.MILLISECONDS );
	if (pGot == null)  {
	  System.out.println( "DEBUG - MidiHandler patch request timed out" );
	  sharedData.patchQ.clear();
	  sendSystemReset();
	} else 	if (pGot == p) {
	  gotIt = true;
	} else if (pGot != p) {
	  System.out.println( "DEBUG - MidiHandler Got out-of-sync patch: " + p );
	  sharedData.patchQ.clear();
	} 
      }
    } catch( InterruptedException e ) {
      e.printStackTrace();
    }
  }

  synchronized void requestQuickPCs() {
    
    byte[] reqMsg = new byte[6];
    reqMsg[0] = MIDI_SYSEX_AKAI_ID;
    reqMsg[1] = MIDI_SYSEX_AKAI_EWI4K;
    reqMsg[2] = MIDI_SYSEX_ALLCHANNELS;
    reqMsg[3] = MIDI_QUICKPC_DUMP_REQ;
    reqMsg[4] = 0x00;
    reqMsg[5] = MIDI_SYSEX_TRAILER;
    SysexMessage sysEx;
    try {
      sysEx = new SysexMessage( SysexMessage.SYSTEM_EXCLUSIVE, reqMsg, reqMsg.length );
      midiOut.send( sysEx, -1 );
    } catch( InvalidMidiDataException e ) {
      e.printStackTrace();
    }

  }
  
  synchronized boolean requestDeviceID() {
    byte[] reqMsg = new byte[5];
    reqMsg[0] = 0x7E;
    reqMsg[1] = MIDI_SYSEX_ALLCHANNELS;
    reqMsg[2] = MIDI_SYSEX_GEN_INFO;
    reqMsg[3] = MIDI_SYSEX_ID_REQ;
    reqMsg[4] = MIDI_SYSEX_TRAILER;
    System.out.println( "DEBUG - MidiHandler requesting Device ID" );
    if (!sendSysEx( reqMsg )) return false;
    try {
      SharedData.DeviceIdResponse dId = sharedData.deviceIdQ.poll( MIDI_TIMEOUT_MS, TimeUnit.MILLISECONDS );
      if (dId == SharedData.DeviceIdResponse.IS_EWI4000S) {
	System.out.println( "DEBUG - MidiHandler found EWI4000s" );
	sharedData.setEwiAttached( true );
	return true;
      } else if (dId == null) {
	System.out.println( "DEBUG - MidiHandler did not find EWI4000s and timed out" );
	sharedData.setEwiAttached( false );
      } else {
	System.out.println( "DEBUG - MidiHandler did not find EWI4000s and got unexpected response" );
	sharedData.setEwiAttached( false );
      }
    } catch( InterruptedException e ) {
      System.out.println( "DEBUG - MidiHandler did not EWI4000s and was interrupted" );
    }
    requestDeviceID();
    return false;
  }
  
  // all SysEx requests must go via this method - which is synchronised to avoid overlapping requests
 synchronized boolean sendSysEx( final byte[] sysexBytes ) {
   SendMsg msg = new SendMsg();
   msg.msgType = SendMsg.MsgType.SYSEX;
   msg.bytes = sysexBytes;
   sharedData.sendQ.add( msg );
//   SendMsg msg = new SendMsg();
//   
//    try {
//      SysexMessage sysEx = new SysexMessage( SysexMessage.SYSTEM_EXCLUSIVE, sysexBytes, sysexBytes.length );
//      if (!outDev.isOpen()) {
//	System.err.println( "Error - MidiHandler: Attempt to sendSysEx to closed MIDI device" );
//	return false;
//      }
//      midiOut.send( sysEx, -1 );
//    } catch( InvalidMidiDataException e ) {
//       e.printStackTrace();
//       return false;
//    }
    // FIXME ?The Qt version had a SLEEP(250) here?
//    try {
//      Thread.sleep( 250 );
//    } catch( InterruptedException e ) {
//      e.printStackTrace();
//    }
    return true;
  }

  synchronized boolean sendPatch( EWI4000sPatch patch, final byte mode ) {
    patch.setPatchMode( mode );
    if (!sendSysEx( patch.sendableBlob() )) return false;
    if (mode == EWI4000sPatch.EWI_EDIT) {
      // if we're going to edit we need to send it again as patch 0 with the 
      // edit flag still set...  
      patch.setPatchNum( (byte) 0x00 );
      if (!sendSysEx( patch.sendableBlob() )) {
	return false;
      }
    }
    return true;
  }
  
  synchronized boolean sendControlChange( int cc, int val, int ch ) {
    try {
      ShortMessage sm = new ShortMessage( ShortMessage.CONTROL_CHANGE, ch, cc, val );
      midiOut.send( sm, -1 );
    } catch( InvalidMidiDataException e ) {
      e.printStackTrace();
      return false;
    }    
    return true;
  }
  
  synchronized boolean sendLiveControl( int lsb, int msb, int cValue ) {
    sendControlChange( MIDI_CC_NRPN_LSB, lsb, 0 );
    sendControlChange( MIDI_CC_NRPN_MSB, msb, 0 );
    sendControlChange( MIDI_CC_DATA_ENTRY, cValue, 0 );
    sendControlChange( MIDI_CC_NRPN_LSB, 127, 0 );
    sendControlChange( MIDI_CC_NRPN_MSB, 127, 0 );
    return true;
  }
  
  synchronized void sendSystemReset() {
    try {
      System.out.println( "DEBUG - MidiHandler sending System Reset to EWI" );
      ShortMessage sm = new ShortMessage( ShortMessage.SYSTEM_RESET );
      midiOut.send(  sm,  -1 );
    } catch( InvalidMidiDataException e ) {
      e.printStackTrace();
    } 
    try {
      Thread.sleep( 3000 );
    } catch( InterruptedException e ) {
      e.printStackTrace();
    }
  }
  
}
