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

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * @author steve
 *
 */
public class UiKeyTriggerGrid extends GridPane {
  
  ChoiceBox<String> keyTriggerChoice;
  
  UiKeyTriggerGrid( EWI4000sPatch editPatch, MidiHandler midiHandler ) {
    
    setId( "editor-grid" );
    
    Label mainLabel = new Label( "Key Trigger" );
    mainLabel.setId( "editor-section-label" );
    GridPane.setColumnSpan( mainLabel, 2 );
    add( mainLabel, 0, 0 );

    keyTriggerChoice = new ChoiceBox<String>();
    keyTriggerChoice.getItems().addAll( "Single", "Multi" );
    keyTriggerChoice.setOnAction( (event) -> {
      midiHandler.sendLiveControl( 7, 81, keyTriggerChoice.getSelectionModel().getSelectedIndex() );
      editPatch.formantFilter = keyTriggerChoice.getSelectionModel().getSelectedIndex(); 
    });
    add( keyTriggerChoice, 0, 1 );
    
  }
  void setControls( EWI4000sPatch editPatch ) {
    keyTriggerChoice.getSelectionModel().select( editPatch.keyTrigger );
  }
}
