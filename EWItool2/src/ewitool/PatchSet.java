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
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import ewitool.Main.Status;

public final class PatchSet {
   
  public static Status save( ArrayList<EWI4000sPatch> patchList, String filename ) throws IOException {
    
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

}
