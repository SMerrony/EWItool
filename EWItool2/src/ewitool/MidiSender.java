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

public class MidiSender implements Runnable {

  BlockingQueue<SendMsg> msgQ;
  MidiDevice outDev;
  Receiver receiver;
  
  private final static long NOW = -1;
  
  MidiSender( BlockingQueue<SendMsg> pMsgQ, MidiDevice pOutDev ) {
    msgQ = pMsgQ;
    outDev = pOutDev;
    try {
      receiver = outDev.getReceiver();
    } catch( MidiUnavailableException e ) {
      e.printStackTrace();
      System.err.println( "Error - MidiSender() could not obtain chosen MIDI OUT receiver" );
      System.exit( 1 );
    }
  }

  @Override
  public void run() {

    SendMsg msg;

    while( true ) {
      try {
	msg = msgQ.take(); 
	if (!outDev.isOpen()) {
	  System.err.println( "Error - MidiSender: MIDI Out device is not open" );
	  System.exit( 1 );
	}
	switch( msg.msgType ) {
	case CC:
	    try {
	      ShortMessage sm = new ShortMessage( ShortMessage.CONTROL_CHANGE, msg.channel, msg.cc, msg.value );
	      receiver.send( sm, NOW );
	    } catch( InvalidMidiDataException e ) {
	      e.printStackTrace();
	    } 
	  break;
	case SYSEX:
	  try {
	    System.out.println( "DBEUG - MidiSender thread got SysEx to send.  Length: " + msg.bytes.length );
	    if (msg.bytes[0] != MidiHandler.MIDI_SYSEX_HEADER) {
	      System.err.println( "Error - MidiSender received invalid SysEx send request" );
	      System.exit( 1 );
	    }
	    SysexMessage sysEx = new SysexMessage( msg.bytes, msg.bytes.length );
	    receiver.send( sysEx, NOW );
	    // N.B. The final Qt version had a SLEEP(250) here
	    try {
	      Thread.sleep( MidiHandler.MIDI_MESSAGE_SPACER_MS );
	    } catch( InterruptedException e ) {
	      e.printStackTrace();
	    }
	  } catch( InvalidMidiDataException e ) {
	    e.printStackTrace();
	  }
	  break;
	case SYSTEM_RESET:
	    try {
	      System.out.println( "DEBUG - MidiSender sending System Reset to EWI" );
	      ShortMessage sm = new ShortMessage( ShortMessage.SYSTEM_RESET );
	      receiver.send(  sm,  NOW );
	    } catch( InvalidMidiDataException e ) {
	      e.printStackTrace();
	    } 
	    try {
	      Thread.sleep( MidiHandler.MIDI_MESSAGE_SPACER_MS );
	    } catch( InterruptedException e ) {
	      e.printStackTrace();
	    }
	  break;

	}
      } catch( InterruptedException e ) {
	System.out.println( "DEBUG - MidiSender closing" );
	receiver.close();
	outDev.close();
      }

    }

  }

}

