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

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

/**
 * @author steve
 *
 */
public class UiEPXSubmitGrid extends GridPane {
  
  TextField nameField, originField, tagsField;
  CheckBox  privacyCheckBox;
  ChoiceBox<String> typeChoice;
  TextArea  descriptionArea;
  Button okButton, cancelButton;
  String hexPatch; // not displayed

  UiEPXSubmitGrid( String name, String pHexPatch ) {
    
    setId( "epx-submit-grid" );
    
    hexPatch = pHexPatch;
    
    Label hdrLabel = new Label( "Please complete the fields below so your patch can be catalogued correctly..." );
    hdrLabel.setWrapText( true );
    GridPane.setColumnSpan( hdrLabel, 3 );
    add( hdrLabel, 0, 0);
    
    add( new Label( "Name" ), 0, 1 );
    nameField = new TextField( name );
    nameField.setDisable( true );
    GridPane.setColumnSpan( nameField, 2 );
    add( nameField, 1, 1 );
    
    add( new Label( "Origin" ), 0, 2 );
    originField = new TextField();
    GridPane.setColumnSpan( originField, 2 );
    add( originField, 1, 2 ); 
    
    add( new Label( "Type" ), 0, 3 );
    typeChoice = new ChoiceBox<String>();
    typeChoice.getItems().addAll( "[None]" );
    typeChoice.getSelectionModel().select( 0 );
    add( typeChoice, 1, 3 );
    
    privacyCheckBox = new CheckBox( "Private" );
    add( privacyCheckBox, 2, 3 );
    
    add( new Label( "Description" ), 0, 4 );
    descriptionArea = new TextArea();
    descriptionArea.setWrapText( true );
    descriptionArea.setTooltip( new Tooltip( "(Optional - max. 255 characters)" ) );
    GridPane.setColumnSpan( descriptionArea, 2 );
    add( descriptionArea, 1, 4 );

    add( new Label( "Tags" ), 0, 5 );
    tagsField = new TextField();
    GridPane.setColumnSpan( tagsField, 2 );
    add( tagsField, 1, 5 ); 
    
    okButton = new Button( "OK" );
    add( okButton, 1, 6 );
    cancelButton = new Button( "Cancel" );
    add( cancelButton, 2, 6 );
     
  }
}
