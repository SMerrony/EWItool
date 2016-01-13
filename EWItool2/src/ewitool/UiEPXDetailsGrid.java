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

import ewitool.EPX.DetailsResult;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * @author steve
 *
 */
public class UiEPXDetailsGrid extends GridPane {
  
  TextField nameField, contribField, originField, typeField, rTagsField, addedField;
  CheckBox privacyCheckBox;
  TextArea  descriptionArea;


  UiEPXDetailsGrid( ) {
    // for vertically fixed rows in the GridPanes...
    RowConstraints fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    // for vertically growable rows in the GridPanes...
    RowConstraints vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    Label detailsSectionLabel = new Label( "Details" );
    detailsSectionLabel.setId( "epx-section-label" );
    add( detailsSectionLabel, 0, 0 );
    getRowConstraints().add( fixedRC );
    
    add( new Label( "Name" ), 0, 1 );
    nameField = new TextField();  
    add( nameField, 1, 1 );
    getRowConstraints().add( vgrowRC );
    
    add( new Label( "Contributor" ), 0, 2 );
    contribField = new TextField();
    add( contribField, 1, 2 );
    getRowConstraints().add( vgrowRC );
    
    add( new Label( "Originator" ), 0, 3 );
    originField = new TextField();
    add( originField, 1,3 );
    getRowConstraints().add( vgrowRC );
    
    add( new Label( "Type" ), 0, 4 );
    typeField = new TextField();
    add( typeField, 1, 4 );
    getRowConstraints().add( vgrowRC );
    
    add( new Label( "Private" ), 0, 5 );
    privacyCheckBox = new CheckBox();
    add( privacyCheckBox, 1, 5 );
    getRowConstraints().add( vgrowRC );
    
    add( new Label( "Description" ), 0, 6 );
    descriptionArea = new TextArea();
    descriptionArea.setWrapText( true );
    add( descriptionArea, 0, 7 );
    GridPane.setColumnSpan( descriptionArea, 2 );
    getRowConstraints().add( vgrowRC );
    
    add( new Label( "Tags" ), 0, 8 );
    rTagsField = new TextField();
    add( rTagsField, 1, 8 );
    getRowConstraints().add( vgrowRC );
    
    add( new Label( "Added" ), 0, 9 );
    addedField = new TextField();
    add( addedField, 1, 9 );
    getRowConstraints().add( vgrowRC );
    

  }


  /**
   * @param dr
   */
  public void setFields( DetailsResult dr ) {
    nameField.setText( dr.name );
    contribField.setText( dr.contrib );
    originField.setText( dr.origin );
    typeField.setText( dr.type );
    privacyCheckBox.setSelected( dr.privateFlag );
    descriptionArea.setText( dr.desc );
    rTagsField.setText( dr.tags );
    addedField.setText( dr.added );
  }

  public void clearFields() {
    nameField.setText( "" );
    contribField.setText( "" );
    originField.setText( "" );
    typeField.setText( "" );
    privacyCheckBox.setSelected( false );
    descriptionArea.setText( "" );
    rTagsField.setText( "" );
    addedField.setText( "" );
  }
}
