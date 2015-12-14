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

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

public class MidiReceiver implements Receiver {
 
  SharedData sharedData;

  /**
   * @param ewiPatches
   */
  public MidiReceiver( SharedData pSharedData ) {
    sharedData = pSharedData;
  }

  @Override
  public void send( MidiMessage message, long timeStamp ) {

    // we are currently only interested in SysEx messages from the EWI
    if (message instanceof SysexMessage) {
      byte[] messageBytes = ((SysexMessage) message).getData();
      switch( messageBytes[3] ) {
      case MidiHandler.MIDI_PRESET_DUMP:
        if (messageBytes.length != (MidiHandler.EWI_SYSEX_PRESET_DUMP_LEN - 1)) {
          System.err.println( "Error - Invalid preset dump SysEx received from EWI");
          return;
        }
        EWI4000sPatch thisPatch = new EWI4000sPatch();
        for (int b = 0; b < (MidiHandler.EWI_SYSEX_PRESET_DUMP_LEN - 1); b++)
          thisPatch.patch_blob[b+1] = messageBytes[b];
        thisPatch.decodeBlob();
        if (thisPatch.header[3] == MidiHandler.MIDI_SYSEX_ALLCHANNELS) {
          int thisPatchNum = (int) thisPatch.patch_num; // FIXME ++?
          if (thisPatchNum < 0 || thisPatchNum >= EWI4000sPatch.EWI_NUM_PATCHES) {
            System.err.println( "Error - Invalid patch number (" + thisPatchNum + ") received from EWI");
          } else {
            sharedData.ewiPatches[thisPatchNum] = thisPatch;
            sharedData.ewiPatches[thisPatchNum].decodeBlob();
            sharedData.patchQ.add( thisPatchNum );
            System.out.println( "DEBUG - MidiReceiver: Patch number: " + thisPatchNum + " received" );
          }
        }
        break;
      case MidiHandler.MIDI_QUICKPC_DUMP:
        break;
      default:
        System.err.println( "Warning - Unrecognised SysEx type (" + messageBytes[4] + ") received from EWI");
        break;
      }
    }

  }

  @Override
  public void close() {
    
  }


 
}
