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

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * @author steve
 *
 */
public class UiKeyPatchesGrid extends GridPane {
  
  public ChoiceBox<String>[] keyChoices;
  public Button fetchButton, storeButton;

  @SuppressWarnings( "unchecked" )
  UiKeyPatchesGrid(SharedData sharedData) {
    
    setId( "key-patches-grid" );
    
    ColumnConstraints ccs;
    // cols can grow indefinitely
    ccs = new ColumnConstraints(); // 40.0, 90.0, Double.MAX_VALUE );
    ccs.setHalignment( HPos.CENTER );
    ccs.setHgrow( Priority.ALWAYS );
    getColumnConstraints().addAll( ccs, ccs, ccs );

    RowConstraints fixedRC, vgrowRC;
    fixedRC = new RowConstraints();
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    Label keyLabel = new Label( "Key" );
    keyLabel.setId( "key-patches-header" );
    add( keyLabel, 0, 0 );
    Label patchLabel = new Label( "Patch" );
    patchLabel.setId( "key-patches-header" );
    add( patchLabel, 1, 0 );
    Label warnLabel = new Label( "Here Be Dragons!" );
    warnLabel.setId( "key-patches-header" );
    add( warnLabel, 2, 0 );
    getRowConstraints().add( fixedRC ); 
    
    keyChoices = new ChoiceBox[12];
    
    add( new Label( "B" ), 0, 1 );
    keyChoices[0] = new ChoiceBox<String>();
    keyChoices[0].setOnAction( (ae) -> sharedData.quickPCs[0] = (byte) keyChoices[0].getSelectionModel().getSelectedIndex() );
    add( keyChoices[0], 1, 1 );
    getRowConstraints().add( vgrowRC );
        
    add( new Label( "Bb/A#" ), 0, 2 );
    keyChoices[1] = new ChoiceBox<String>();
    keyChoices[1].setOnAction( (ae) -> sharedData.quickPCs[1] = (byte) keyChoices[1].getSelectionModel().getSelectedIndex() );
    add( keyChoices[1], 1, 2 );
    getRowConstraints().add( vgrowRC );
 
    add( new Label( "A" ), 0, 3 );
    keyChoices[2] = new ChoiceBox<String>();
    keyChoices[2].setOnAction( (ae) -> sharedData.quickPCs[2] = (byte) keyChoices[2].getSelectionModel().getSelectedIndex() );
    add( keyChoices[2], 1, 3 );
    getRowConstraints().add( vgrowRC );
 
    add( new Label( "Ab/G#" ), 0, 4 );
    keyChoices[3] = new ChoiceBox<String>();
    keyChoices[3].setOnAction( (ae) -> sharedData.quickPCs[3] = (byte) keyChoices[3].getSelectionModel().getSelectedIndex() );
    add( keyChoices[3], 1, 4 );
    getRowConstraints().add( vgrowRC );
 
    add( new Label( "G" ), 0, 5 );
    keyChoices[4] = new ChoiceBox<String>();
    keyChoices[4].setOnAction( (ae) -> sharedData.quickPCs[4] = (byte) keyChoices[4].getSelectionModel().getSelectedIndex() );
    add( keyChoices[4], 1, 5 );
    getRowConstraints().add( vgrowRC );
 
    add( new Label( "Gb/F#" ), 0, 6 );
    keyChoices[5] = new ChoiceBox<String>();
    keyChoices[5].setOnAction( (ae) -> sharedData.quickPCs[5] = (byte) keyChoices[5].getSelectionModel().getSelectedIndex() );
    add( keyChoices[5], 1, 6 );
    getRowConstraints().add( vgrowRC );
 
    add( new Label( "F" ), 0, 7 );
    keyChoices[6] = new ChoiceBox<String>();
    keyChoices[6].setOnAction( (ae) -> sharedData.quickPCs[6] = (byte) keyChoices[6].getSelectionModel().getSelectedIndex() );
    add( keyChoices[6], 1, 7 );
    getRowConstraints().add( vgrowRC );
 
    add( new Label( "E" ), 0, 8 );
    keyChoices[7] = new ChoiceBox<String>();
    keyChoices[7].setOnAction( (ae) -> sharedData.quickPCs[7] = (byte) keyChoices[7].getSelectionModel().getSelectedIndex() );
    add( keyChoices[7], 1, 8 );
    getRowConstraints().add( vgrowRC );
 
    add( new Label( "Eb/D#" ), 0, 9 );
    keyChoices[8] = new ChoiceBox<String>();
    keyChoices[8].setOnAction( (ae) -> sharedData.quickPCs[8] = (byte) keyChoices[0].getSelectionModel().getSelectedIndex() );
    add( keyChoices[8], 1, 9 );
    getRowConstraints().add( vgrowRC );
 
    add( new Label( "D" ), 0, 10 );
    keyChoices[9] = new ChoiceBox<String>();
    keyChoices[9].setOnAction( (ae) -> sharedData.quickPCs[9] = (byte) keyChoices[0].getSelectionModel().getSelectedIndex() );
    add( keyChoices[9], 1, 10 );
    getRowConstraints().add( vgrowRC );
 
    add( new Label( "Db/C#" ), 0, 11 );
    keyChoices[10] = new ChoiceBox<String>();
    keyChoices[10].setOnAction( (ae) -> sharedData.quickPCs[10] = (byte) keyChoices[10].getSelectionModel().getSelectedIndex() );
    add( keyChoices[10], 1, 11 );
    getRowConstraints().add( vgrowRC );
 
    add( new Label( "C" ), 0, 12 );
    keyChoices[11] = new ChoiceBox<String>();
    keyChoices[11].setOnAction( (ae) -> sharedData.quickPCs[11] = (byte) keyChoices[11].getSelectionModel().getSelectedIndex() );
    add( keyChoices[11], 1, 12 );
    getRowConstraints().add( vgrowRC );

    Label blurb = new Label( "Only use this tab if your EWI4000s firmware is version 2.3 or later.\n\n"
        + "For firmware version 2.3 you must have the 'dP' setting at 'OC' (octave) - which is the default.  "
        + "See page 12 of the EWI4000s Manual Addendum v2.3 for further details.\n\n"
        + "For firmware version 2.4 (and later) you should go to the 'PC' menu and have 'dP' turned on "
        + "and 'AL' turned off - these are the default settings." );
    blurb.setWrapText( true ); 
    GridPane.setRowSpan( blurb, 10 );
    add( blurb, 2, 1 );
    
    fetchButton = new Button( "Fetch Current Key Patches" );
    add( fetchButton, 2, 11 );
    storeButton = new Button( "Store on EWI" );
    add( storeButton, 2, 12 );

  }

}
