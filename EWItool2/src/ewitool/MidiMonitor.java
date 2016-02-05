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

import javafx.concurrent.Task;

/**
 * This class (task) currently logs MIDI I/O via the Debug object.
 * In the future it could log to a user display...
 * 
 * @author steve
 *
 */
public class MidiMonitor {

  private SharedData sharedData;
  private String dispMsg;

  MidiMonitor( SharedData pSharedData ) {
    sharedData = pSharedData;

    if (Debugger.VERBOSE) {
      Task<Object> task = new Task<Object>() {
        @Override
        protected Object call() throws Exception {
          MidiMonitorMessage msg;
          final String FMT = "%02x ";

          while( true ) {
            try {
              msg = sharedData.monitorQ.take();
              switch( msg.direction){
              case RECEIVED:
                dispMsg = "In:  ";
                switch( msg.type ) {
                case CC: dispMsg += "CC "; break;
                case SYSEX: dispMsg += "SysEx "; break;
                case SYSTEM_RESET: break;
                }
                break;
              case SENT:
                dispMsg = "Out: ";
                switch( msg.type ) {
                case CC: dispMsg += "CC ";  break;
                case SYSEX: dispMsg += "SysEx "; break;
                case SYSTEM_RESET: break;
                }
                break;
              default:
                System.err.println( "ERROR - Invalid message (unknown direction) on MIDI Monitor queue" );
                System.exit( 1 );
                break;     
              }
              for (int b = 0; b < msg.bytes.length; b++) 
                dispMsg += String.format( FMT, msg.bytes[b] );
              Debugger.log( dispMsg );
            } catch( InterruptedException e ) {
              e.printStackTrace();
              System.err.println( "ERROR - Exception reading MIDI Monitor queue" );
              System.exit( 1 );
            }
          }
        }
      };


      Thread th = new Thread( task );
      th.setDaemon( true );
      sharedData.setMidiMonitoring( true );
      th.start();
    }
  }
}
