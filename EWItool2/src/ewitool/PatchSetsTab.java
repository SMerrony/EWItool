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

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PatchSetsTab extends Tab {
  
  Button importButton, loadEwiButton, deleteButton, saveButton;
  ListView patchSetList;
  ListView patchesList;
  
  PatchSetsTab() {
    
    setText( "Patch Set Collection" );
    setClosable( false );
    
    VBox lhVbox = new VBox();

    Label setsLabel = new Label( "Patch Sets" );
    patchSetList = new ListView();
    
    HBox lhInnerHbox = new HBox();
    
    importButton = new Button( "Import" );
    loadEwiButton = new Button( "Load into EWI" );
    deleteButton = new Button( "Delete" );
    
    lhInnerHbox.getChildren().addAll( importButton, loadEwiButton, deleteButton );
    lhVbox.getChildren().addAll( setsLabel, patchSetList, lhInnerHbox );
    VBox.setVgrow( patchSetList, Priority.ALWAYS );
    
    VBox rhVbox = new VBox();
    Label patchesLabel = new Label( "Patches" );
    patchesList = new ListView();
    saveButton = new Button( "Copy to Scratchpad" );
    
    rhVbox.getChildren().addAll( patchesLabel, patchesList, saveButton );
    
    HBox hBox = new HBox();
    hBox.setPadding( new Insets( 4.0 ) );
    hBox.setSpacing( 8.0 );
    hBox.getChildren().addAll( lhVbox, rhVbox );
    VBox.setVgrow( patchesList, Priority.ALWAYS );
    HBox.setHgrow( lhVbox, Priority.ALWAYS );
    HBox.setHgrow( rhVbox, Priority.ALWAYS );
    
    setContent( hBox );
    
  }

}
