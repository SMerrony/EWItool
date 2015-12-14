/**
 * MidiHandler - This class controls the MIDI connections and their
 * respective tasks.
 * 
 * Pseudocode:
 * 

 *   start subtasks if MIDI ports set
 *   else
 *     wait for signal
 *     switch( signal-type ) 
 *       case midi-in-set: (re)start MidiReceiver task
 *       case midi-out-set: (re)start MidiSender task
 *       case stop: stop Midi tasks
 *     end-switch
 * end-loop
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
  public final static byte MIDI_SYSEX_AKAI_ID    = 0x47; 
  public final static byte MIDI_SYSEX_AKAI_EWI4K = 0x64;
  public final static byte MIDI_SYSEX_CHANNEL    = 0x00;
  public final static byte MIDI_SYSEX_ALLCHANNELS = 0x7f;

  public final static byte MIDI_CC_DATA_ENTRY    = 0x06;
  public final static byte MIDI_CC_NRPN_LSB      = 0x62;
  public final static byte MIDI_CC_NRPN_MSB      = 0x63;
  
  public final static int  MAX_SYSEX_LENGTH      = 262144;
  public final static int  EWI_SYSEX_PRESET_DUMP_LEN = EWI4000sPatch.EWI_PATCH_LENGTH;
  public final static int  EWI_SYSEX_QUICKPC_DUMP_LEN = 91;
  public final static int  MIDI_TIMEOUT_MS       = 1000;
  
  SharedData sharedData;
  Prefs userPrefs;
  MidiDevice inDev = null, outDev = null; 
  
  MidiHandler( SharedData pSharedData, Prefs pUserPrefs ) {
    
    sharedData = pSharedData;
    userPrefs = pUserPrefs;
     
    MidiDevice device;
    Receiver midiIn;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    for (int d = 0; d < infos.length; d++) {
      try {
        device = MidiSystem.getMidiDevice( infos[d] );
        if (!(device instanceof Sequencer) && !(device instanceof Synthesizer)) {
          if (device.getMaxReceivers() != 0) {
            if (infos[d].getName().equals( userPrefs.midiOutPort.getValue() )) {
              outDev = MidiSystem.getMidiDevice( infos[d] );
              outDev.open();
              //midiOut = new MidiSender();
              System.out.println( "Debug - OUT Port: " + infos[d].getName());
            }
          } else if (device.getMaxTransmitters() != 0) {
            if (infos[d].getName().equals( userPrefs.midiInPort.getValue() )) {
              inDev = MidiSystem.getMidiDevice( infos[d] );
              inDev.open(); 
              midiIn = new MidiReceiver( sharedData );
              inDev.getTransmitter().setReceiver( midiIn );
              System.out.println( "Debug - IN Port: " + infos[d].getName() );
            }
          }
        }  
      } catch (Exception e) {
        System.err.println( "ERROR - Fetching MIDI information" );
      }
    }

  }
  
  public void close() {
    outDev.close();
    inDev.close();
  }

  /**
   * 
   */
  public void restart() {

    close();
    
    MidiDevice device;
    Receiver midiIn;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    for (int d = 0; d < infos.length; d++) {
      try {
        device = MidiSystem.getMidiDevice( infos[d] );
        if (!(device instanceof Sequencer) && !(device instanceof Synthesizer)) {
          if (device.getMaxReceivers() != 0) {
            if (infos[d].getName().equals( userPrefs.midiOutPort.getValue() )) {
              outDev = MidiSystem.getMidiDevice( infos[d] );
              outDev.open();
              //midiOut = new MidiSender();
              System.out.println( "Debug - OUT Port: " + infos[d].getName());
            }
          } else if (device.getMaxTransmitters() != 0) {
            if (infos[d].getName().equals( userPrefs.midiInPort.getValue() )) {
              inDev = MidiSystem.getMidiDevice( infos[d] );
              inDev.open();
              midiIn = new MidiReceiver( sharedData );
              inDev.getTransmitter().setReceiver( midiIn );
              System.out.println( "Debug - IN Port: " + infos[d].getName() );
            }
          }
        }  
      } catch (Exception e) {
        System.err.println( "ERROR - Fetching MIDI information" );
      }
    }

  }
  
  void clearPatches() {
    sharedData.clear();
  }
  
  void requestPatch( int p ) {
    
    if (p < 0 || p > EWI4000sPatch.EWI_NUM_PATCHES) {
      System.err.println( "Error - Attempt to request out-of-range patch (" + p + ")" );
      System.exit( 1 );
    }
    
    sharedData.ewiPatches[p] = new EWI4000sPatch();
    
    byte[] reqMsg = new byte[6];
    //reqMsg[0] = MIDI_SYSEX_HEADER;
    reqMsg[0] = MIDI_SYSEX_AKAI_ID;
    reqMsg[1] = MIDI_SYSEX_AKAI_EWI4K;
    reqMsg[2] = MIDI_SYSEX_CHANNEL;
    reqMsg[3] = MIDI_PRESET_DUMP_REQ;
    reqMsg[4] = (byte) p;
    reqMsg[5] = MIDI_SYSEX_TRAILER;
    SysexMessage sysEx;
    try {
      sysEx = new SysexMessage( SysexMessage.SYSTEM_EXCLUSIVE, reqMsg, reqMsg.length );
      outDev.getReceiver().send( sysEx, -1 );
      System.out.println( "DEBUG - Requested patch: " + p );
      while (true) {
        // wait for a patch to be received, or timeout
        Integer pGot = sharedData.patchQ.poll( MIDI_TIMEOUT_MS, TimeUnit.MILLISECONDS );
        if (pGot == null || pGot != p) {
          // either timed-out or wrong (out-of-sync) patch, request again
          outDev.getReceiver().send( sysEx, -1 );
        } else 
          // OK - carry on
          break;
      } 
    } catch( InvalidMidiDataException e ) {
      e.printStackTrace();
    } catch( MidiUnavailableException e ) {
      e.printStackTrace();
    } catch( InterruptedException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
  
  void requestQuickPCs() {
    
    byte[] reqMsg = new byte[6];
    // reqMsg[0] = MIDI_SYSEX_HEADER;
    reqMsg[0] = MIDI_SYSEX_AKAI_ID;
    reqMsg[1] = MIDI_SYSEX_AKAI_EWI4K;
    reqMsg[2] = MIDI_SYSEX_ALLCHANNELS;
    reqMsg[3] = MIDI_QUICKPC_DUMP_REQ;
    reqMsg[4] = 0x00;
    reqMsg[5] = MIDI_SYSEX_TRAILER;
    SysexMessage sysEx;
    try {
      sysEx = new SysexMessage( SysexMessage.SYSTEM_EXCLUSIVE, reqMsg, reqMsg.length );
      outDev.getReceiver().send( sysEx, -1 );
    } catch( InvalidMidiDataException e ) {
      e.printStackTrace();
    } catch( MidiUnavailableException e ) {
      e.printStackTrace();
    }

  }

  
}
