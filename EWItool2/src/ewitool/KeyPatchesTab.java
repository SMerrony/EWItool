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

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class KeyPatchesTab extends Tab {
  
  ChoiceBox[] keyChoices;
  Button fetchButton, storeButton;
  
  KeyPatchesTab() {
    
    setText( "Key Patches" );
    setClosable( false );
    
    GridPane gp = new GridPane();
    gp.setPadding( new Insets( 10.0 ) );
    gp.setVgap( 3.0 ); // i.e. minimal vertical gap between buttons
    gp.setMaxWidth( Double.MAX_VALUE );

    ColumnConstraints ccs;
    // cols can grow indefinitely
    ccs = new ColumnConstraints( 40.0, 90.0, Double.MAX_VALUE );
    ccs.setHalignment( HPos.CENTER );
    gp.getColumnConstraints().add( ccs );
    gp.getColumnConstraints().add( ccs );

    RowConstraints fixedRC, vgrowRC;
    fixedRC = new RowConstraints();
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );

    
    gp.add( new Label( "Key" ), 0, 0 );
    gp.add( new Label( "Patch" ), 1, 0 );
    gp.getRowConstraints().add( fixedRC );
    
    keyChoices = new ChoiceBox[12];
    
    gp.add( new Label( "B" ), 0, 1 );
    keyChoices[0] = new ChoiceBox();
    gp.add( keyChoices[0], 1, 1 );
    gp.getRowConstraints().add( vgrowRC );
    
    gp.add( new Label( "Bb/A#" ), 0, 2 );
    keyChoices[1] = new ChoiceBox();
    gp.add( keyChoices[1], 1, 2 );
    gp.getRowConstraints().add( vgrowRC );
 
    gp.add( new Label( "A" ), 0, 3 );
    keyChoices[2] = new ChoiceBox();
    gp.add( keyChoices[2], 1, 3 );
    gp.getRowConstraints().add( vgrowRC );
 
    gp.add( new Label( "Ab/G#" ), 0, 4 );
    keyChoices[3] = new ChoiceBox();
    gp.add( keyChoices[3], 1, 4 );
    gp.getRowConstraints().add( vgrowRC );
 
    gp.add( new Label( "G" ), 0, 5 );
    keyChoices[4] = new ChoiceBox();
    gp.add( keyChoices[4], 1, 5 );
    gp.getRowConstraints().add( vgrowRC );
 
    gp.add( new Label( "Gb/F#" ), 0, 6 );
    keyChoices[5] = new ChoiceBox();
    gp.add( keyChoices[5], 1, 6 );
    gp.getRowConstraints().add( vgrowRC );
 
    gp.add( new Label( "F" ), 0, 7 );
    keyChoices[6] = new ChoiceBox();
    gp.add( keyChoices[6], 1, 7 );
    gp.getRowConstraints().add( vgrowRC );
 
    gp.add( new Label( "E" ), 0, 8 );
    keyChoices[7] = new ChoiceBox();
    gp.add( keyChoices[7], 1, 8 );
    gp.getRowConstraints().add( vgrowRC );
 
    gp.add( new Label( "Eb/D#" ), 0, 9 );
    keyChoices[8] = new ChoiceBox();
    gp.add( keyChoices[8], 1, 9 );
    gp.getRowConstraints().add( vgrowRC );
 
    gp.add( new Label( "D" ), 0, 10 );
    keyChoices[9] = new ChoiceBox();
    gp.add( keyChoices[9], 1, 10 );
    gp.getRowConstraints().add( vgrowRC );
 
    gp.add( new Label( "Db/C#" ), 0, 11 );
    keyChoices[10] = new ChoiceBox();
    gp.add( keyChoices[10], 1, 11 );
    gp.getRowConstraints().add( vgrowRC );
 
    gp.add( new Label( "C" ), 0, 12 );
    keyChoices[11] = new ChoiceBox();
    gp.add( keyChoices[11], 1, 12 );
    gp.getRowConstraints().add( vgrowRC );
 
    VBox rhVbox = new VBox();
    rhVbox.setAlignment( Pos.CENTER );
    rhVbox.setPadding( new Insets( 10.0 ) );
    rhVbox.setSpacing( 3.0 );
    
    Label eek = new Label( "Here be Dragons!" );
    Label blurb = new Label( "Only use this tab if your EWI4000s firmware is version 2.3 or later.  "
        + "For firmware version 2.3 you must have the 'dP' setting at 'OC' (octave) - which is the default.  "
        + "See page 12 of the EWI4000s Manual Addendum v2.3 for further details.  "
        + "For firmware version 2.4 (and later) you should go to the 'PC' menu and have 'dP' turned on "
        + "and 'AL' turned off - these are the default settings." );
    blurb.setWrapText( true ); 
    blurb.setMaxHeight( Double.MAX_VALUE );
    fetchButton = new Button( "Fetch Current Key Patches" );
    storeButton = new Button( "Store on EWI" );
    
    rhVbox.getChildren().addAll( eek, blurb, fetchButton, storeButton );
    VBox.setVgrow( blurb, Priority.ALWAYS );
    
    HBox hBox = new HBox();
    hBox.setSpacing( 8.0 );
    hBox.getChildren().addAll( gp, rhVbox );
    HBox.setHgrow( gp, Priority.ALWAYS );
    HBox.setHgrow( rhVbox, Priority.ALWAYS ); 
    
    setContent( hBox );
    
  }

}
