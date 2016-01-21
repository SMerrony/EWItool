/**
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

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;

public class KeyPatchesTab extends Tab {
  
  UiKeyPatchesGrid keyPatchesGrid;
  SharedData sharedData;
  MidiHandler midiHandler;
  
  KeyPatchesTab( SharedData pSharedData, MidiHandler pMidiHandler ) {
    
    sharedData = pSharedData;
    midiHandler = pMidiHandler;
    
    setText( "Key Patches" );
    setClosable( false );
    
    keyPatchesGrid = new UiKeyPatchesGrid( sharedData );
    // N.B. Cannot populate choices on startup as they are not yet loaded from the EWI
    keyPatchesGrid.fetchButton.setOnAction( (ae) -> {
      // all this palaver just to set a busy cursor...
      keyPatchesGrid.getScene().setCursor( Cursor.WAIT );
      Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          midiHandler.requestQuickPCs();    
          while (!sharedData.loadedQuickPCs)
            try {
              Thread.sleep( 10 );
            } catch( Exception e ) {
              e.printStackTrace();
            }
          Platform.runLater( () -> { populateChoices(); setChoices(); } ); // GUI update stuff...
          return null;
        }
      };
      task.setOnSucceeded( new EventHandler<WorkerStateEvent>() {
        @Override
        public void handle(WorkerStateEvent event) {
          keyPatchesGrid.getScene().setCursor(Cursor.DEFAULT);
        }
      });
      new Thread( task ).start();
    });

    /**
     * saving key patches (QuickPCs) is currently DISABLED.
     * The 2.4 firmware misbehaves if they are sent.
     */
    keyPatchesGrid.storeButton.setDisable( true );
    keyPatchesGrid.storeButton.setTooltip( new Tooltip( "Disabled due to bug in EWI4000s version 2.4 firmware" ) );
//    keyPatchesGrid.storeButton.setOnAction( (ae) -> {
//      midiHandler.sendQuickPCs( sharedData.quickPCs );
//    });
    
    setContent( keyPatchesGrid );
   
  }
  
  void populateChoices() {
    String[] pNames = new String[sharedData.ewiPatchList.size()];
    for (int p = 0; p < sharedData.ewiPatchList.size(); p++) {
      pNames[p] = sharedData.ewiPatchList.get( p ).getName();
    }
    for (int cb = 0; cb < keyPatchesGrid.keyChoices.length; cb++) {
     keyPatchesGrid.keyChoices[cb].getItems().clear();
     keyPatchesGrid.keyChoices[cb].getItems().addAll( pNames );
    }
  }
  
  /**
   * set the selected choices from the data obtained from the EWI
   */
  void setChoices() {
    for (int cb = 0; cb < keyPatchesGrid.keyChoices.length; cb++) {
      keyPatchesGrid.keyChoices[cb].getSelectionModel().select( sharedData.quickPCs[cb] );
    }
  }

}
