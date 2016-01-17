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

import java.util.concurrent.BlockingQueue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import ewitool.SendMsg.MidiMsgType;

public class MidiSender implements Runnable {

  SharedData sharedData;
  BlockingQueue<SendMsg> msgQ;
  MidiDevice outDev;
  Receiver receiver;
  
  MidiMonitorMessage mmsg;

  private final static long NOW = -1;

  MidiSender( SharedData pSharedData, MidiDevice pOutDev ) {
    sharedData = pSharedData;
    msgQ = pSharedData.sendQ;
    outDev = pOutDev;
    if (!outDev.isOpen()) {
      System.err.println( "Error - MidiSender() called with non-open MIDI Device" );
      System.exit( 1 );   
    }
    try {
      receiver = outDev.getReceiver();
    } catch( MidiUnavailableException e ) {
      e.printStackTrace();
      System.err.println( "Error - MidiSender() could not obtain chosen MIDI OUT receiver" );
      System.exit( 1 );
    }
    mmsg = new MidiMonitorMessage();
    mmsg.direction = MidiMonitorMessage.MidiDirection.SENT;
  }

  @Override
  public void run() {

    SendMsg msg;
    try {   
      while( true ) {
        msg = msgQ.take(); 
        if (!outDev.isOpen()) {
          System.err.println( "Error - MidiSender: MIDI Out device is not open" );
          System.exit( 1 );
        }
        switch( msg.msgType ) {
        case CC:
          try {
            ShortMessage sm = new ShortMessage( ShortMessage.CONTROL_CHANGE, msg.cc, msg.value );
            receiver.send( sm, NOW );
            if (sharedData.getMidiMonitoring()) {
              mmsg.type = MidiMsgType.CC;
              mmsg.bytes = sm.getMessage();
              sharedData.monitorQ.add( mmsg );
            }
          } catch( InvalidMidiDataException e ) {
            e.printStackTrace();
          } 
          break;
        case SYSEX:
          try {
            Debugger.log( "DBEUG - MidiSender thread got SysEx to send.  Length: " + msg.bytes.length );
            if (msg.bytes[0] != MidiHandler.MIDI_SYSEX_HEADER) {
              System.err.println( "Error - MidiSender received invalid SysEx send request" );
              System.exit( 1 );
            }
            SysexMessage sysEx = new SysexMessage( msg.bytes, msg.bytes.length );
            receiver.send( sysEx, NOW );
            if (sharedData.getMidiMonitoring()) {
              mmsg.type = MidiMsgType.SYSEX;
              mmsg.bytes = sysEx.getMessage();
              sharedData.monitorQ.add( mmsg );
            }
            // N.B. The final Qt version had a SLEEP(250) here
            try {
              switch( msg.delay) {
              case LONG:
                Debugger.log( "DEBUG - MidiSender: Long pause after SysEx" );
                Thread.sleep( MidiHandler.MIDI_MESSAGE_LONG_PAUSE_MS );
                break;
              case NONE:
                Debugger.log( "DEBUG - MidiSender: No pause after SysEx" );
                break;
              case SHORT:
                Debugger.log( "DEBUG - MidiSender: Short pause after SysEx" );
                Thread.sleep( MidiHandler.MIDI_MESSAGE_SHORT_PAUSE_MS );
                break;
              default:
                System.err.println( "ERROR - MidiSender: INVALID pause after SysEx" );
                break;
              }
            } catch( InterruptedException e ) {
              e.printStackTrace();
            }
          } catch( InvalidMidiDataException e ) {
            e.printStackTrace();
          }
          break;
        case SYSTEM_RESET:
          try {
            Debugger.log( "DEBUG - MidiSender sending System Reset to EWI" );
            ShortMessage sm = new ShortMessage( ShortMessage.SYSTEM_RESET );
            receiver.send(  sm,  NOW );
          } catch( InvalidMidiDataException e ) {
            e.printStackTrace();
          } 
          try {
            Thread.sleep( MidiHandler.MIDI_MESSAGE_LONG_PAUSE_MS );
          } catch( InterruptedException e ) {
            e.printStackTrace();
          }
          break;

        }
      } 
    }
    catch( InterruptedException e ) {
      Debugger.log( "DEBUG - MidiSender closing" );
      receiver.close();
      outDev.close();
    }
  }
}

