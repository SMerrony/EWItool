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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import ewitool.Main.Status;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class PatchSet {
   
  /** these legacy constants were determined purely by observation
   *  more refinement might be possible.
   */
  public final static int BNK_MAX_HEADER_LENGTH = 0x450;
  public final static byte[] BNK_BODY_START = { 'B', 'O', 'D', 'Y' };
  public final static int SQS_MAX_HEADER_LENGTH = 0x1000;
  //below is the sequence which appears to start the real body of SQS files, there
  //seem to be various false BODYs before the main one we care about
  public final static byte[] SQS_BODY_START = { 'B', 'O', 'D', 'Y', 0x00, 0x00, 0x50, 0x78, (byte) 0xf0, 0x47, 0x64, 0x7f, 0x00, 0x00 };
  
  /**
   * save the patchList into a file
   * 
   * @param patchList
   * @param filename
   * @return
   * @throws IOException
   */
  public static Status save( EWI4000sPatch[] patchList, String filename ) throws IOException {
    
    UserPrefs prefs = new UserPrefs();
    Path path = Paths.get( prefs.getLibraryLocation(), filename );
    if (Files.exists( path )) return Status.ALREADY_EXISTS;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    for ( EWI4000sPatch p : patchList) {
      bos.write( p.patchBlob );
    }
    byte[] bytes = bos.toByteArray();
    try {
      Files.write( path, bytes );
    } catch( FileAlreadyExistsException e) {
      return Status.ALREADY_EXISTS;
    } catch( IOException e ) {
      return Status.NO_PERMISSION;
    }
    
    return Status.OK;
  }

  /**
   * try to convert (import) a legacy soundbank file into our native sysex (.syx) format
   * 
   * @param legacyFile
   * @return patchesConverted
   */
  public static int convertLegacy( File legacyFile ) {

    int p = 0;
    
    // construct new filename for imported file
    String syxname = legacyFile.getPath();
    String legacyType = syxname.substring(  syxname.lastIndexOf( '.' ) + 1 ).toLowerCase();  
    syxname = syxname.substring( 0, syxname.lastIndexOf( '.' ) ) + ".syx";

    try {
      byte[] allLegacyBytes = Files.readAllBytes( legacyFile.toPath() );
      Debugger.log( "DEBUG - PatchSet read " + allLegacyBytes.length + " bytes from legacy soundbank" );

      int bodyStartIx = 0;
      switch( legacyType ) {
      case "bnk":
        bodyStartIx = indexOf( allLegacyBytes, BNK_BODY_START );
        break;
      case "sqs":
        bodyStartIx = indexOf( allLegacyBytes, SQS_BODY_START );
        break;
      default:
        System.err.println( "Error - Invalid legacy file name extension (should not occur!)" );
        System.exit( 1 );
        break;
      }
      
      if (bodyStartIx == -1) {
        Alert al = new Alert( AlertType.ERROR );
        al.setHeaderText( "Import Error" );
        al.setContentText( "Could not find BODY in soundbank file" );
        al.showAndWait();
        return 0;
      }
      
      bodyStartIx += 8; // skip BODY and next 4 bytes
      
      Path syxPath = Paths.get( syxname );
      Files.createFile( syxPath );
      
      // read up to 100 patches into list...
      for (p = 0; p < EWI4000sPatch.EWI_NUM_PATCHES; p++) {
        EWI4000sPatch tmpPatch = new EWI4000sPatch();
        tmpPatch.patchBlob = Arrays.copyOfRange( allLegacyBytes, bodyStartIx + (p * EWI4000sPatch.EWI_PATCH_LENGTH), 
                                                                 bodyStartIx + ((p + 1) * EWI4000sPatch.EWI_PATCH_LENGTH) );
        // trivial check that we got a patch...
        if (tmpPatch.patchBlob[EWI4000sPatch.EWI_PATCH_LENGTH - 1] != MidiHandler.MIDI_SYSEX_TRAILER) {
          if (p == 0) {
            // give up if cannot load 1st patch
            Alert al = new Alert( AlertType.ERROR );
            al.setHeaderText( "Import Error" );
            al.setContentText( "Could not find first patch in soundbank file" );
            al.showAndWait();
            Files.delete( syxPath );
            return 0;        
          }
        } else {
          Files.write( syxPath, tmpPatch.patchBlob,StandardOpenOption.APPEND );
        }
      }

    } catch( IOException e ) {
      e.printStackTrace();
    }
    return p;
  }
  
  private static int indexOf(byte[] outerArray, byte[] smallerArray) {
    for(int i = 0; i < outerArray.length - smallerArray.length+1; ++i) {
        boolean found = true;
        for(int j = 0; j < smallerArray.length; ++j) {
           if (outerArray[i+j] != smallerArray[j]) {
               found = false;
               break;
           }
        }
        if (found) return i;
     }
   return -1;  
}  
  
}
