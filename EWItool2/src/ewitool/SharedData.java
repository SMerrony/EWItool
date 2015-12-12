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

/**
 * @author steve
 *
 */
public class SharedData extends Observable {
  
  public final static int NONE = -1;
  public final static int PATCH_LOADED = 1;
  public final static int EDIT_PATCH   = 2;
  
  private volatile int lastPatchLoaded,
                       editingPatchNumber;
  EWI4000sPatch[] ewiPatches;
  
  SharedData() {
    lastPatchLoaded = NONE;
    editingPatchNumber = NONE;
    ewiPatches = new EWI4000sPatch[EWI4000sPatch.EWI_NUM_PATCHES];
  }
  
  public void clear() {
    ewiPatches = new EWI4000sPatch[EWI4000sPatch.EWI_NUM_PATCHES];
    setLastPatchLoaded( NONE );
    setEditingPatchNumber( NONE );
  }
  
  public int getLastPatchLoaded() {
    return lastPatchLoaded;
  }
  
  public void setLastPatchLoaded( int p ) {
    lastPatchLoaded = p;
    setChanged();
    notifyObservers( PATCH_LOADED );
  }

  public int getEditingPatchNumber() {
    return editingPatchNumber;
  }
  
  public void setEditingPatchNumber( int p ) {
    editingPatchNumber = p;
    setChanged();
    notifyObservers( EDIT_PATCH );
  }
}
