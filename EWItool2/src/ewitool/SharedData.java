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

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author steve
 *
 */
public class SharedData extends Observable {
  
  public final static int NONE = -1;
  public final static int PATCH_LOADED = 1;
  public final static int EDIT_PATCH   = 2;
  public final static int EWI_CONNECTION = 3;
  
  private volatile int lastPatchLoaded,
                       editingPatchNumber;
  private volatile boolean ewiAttached;
  
  enum DeviceIdResponse { WRONG_LENGTH, NOT_AKAI, NOT_EWI4000S, IS_EWI4000S }
  
  // We ASSUME that there are always NUM_EWI_PATCHES on this list once it is loaded...
  ArrayList<EWI4000sPatch> ewiPatchList;
  //ObservableList<EWI4000sPatch> ewiPatchList;
  
  public volatile EWI4000sPatch editPatch;  // This is the patch currently being edited
  
  // Queues to synchronise requesting/receiving MIDI info
  BlockingQueue<Integer> patchQ, keyPatchQ;
  BlockingQueue<DeviceIdResponse> deviceIdQ;
  
  SharedData() {
    lastPatchLoaded = NONE;
    editingPatchNumber = NONE;
    editPatch = new EWI4000sPatch();
    ewiPatchList = new ArrayList<EWI4000sPatch>(); //FXCollections.observableArrayList();
    patchQ = new LinkedBlockingQueue<Integer>();
    keyPatchQ = new LinkedBlockingQueue<Integer>();
    deviceIdQ = new LinkedBlockingQueue<DeviceIdResponse>();
  }
  
  public void clear() {
    ewiPatchList.clear();
    setLastPatchLoaded( NONE );
    setEditingPatchNumber( NONE );
    patchQ.clear();
  }
  
  public int getLastPatchLoaded() {
    return lastPatchLoaded;
  }
  
  public void setLastPatchLoaded( int p ) {
    lastPatchLoaded = p;
    // Notification seems dangerous as it causes GUI updates to occur on the (implicit) MIDI listener thread...
    //setChanged();
    //notifyObservers( PATCH_LOADED );
  }
  
  public boolean getEwiAttached() {
    return ewiAttached;
  }
  
  public void setEwiAttached( boolean isIt ) {
    if (isIt != ewiAttached) {
      ewiAttached = isIt;
      setChanged();
      notifyObservers( EWI_CONNECTION );
    }
  }

  public int getEditingPatchNumber() {
    return editingPatchNumber;
  }
  
  public void setEditingPatchNumber( int p ) {
    if (p != NONE) {
      editingPatchNumber = p;
      editPatch = ewiPatchList.get( editingPatchNumber );
      setChanged();
      notifyObservers( EDIT_PATCH );
    }
  }
}
