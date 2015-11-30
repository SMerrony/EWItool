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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class EPXTab extends Tab {

  ChoiceBox typeChoice, addedChoice, contribChoice, originChoice;
  TextField qTagsField;
  Button queryButton;
  ListView resultsView;
  TextField nameField, contribField, originField, typeField, privacyField,
            rTagsField, addedField;
  TextArea  descriptionArea;
  Button deleteButton, copyButton;
  
  EPXTab() {
    
    setText( "EWI Patch eXchange" );
    setClosable( false );
 
    // for vertically fixed rows in the GridPanes...
    RowConstraints fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    // for vertically growable rows in the GridPanes...
    RowConstraints vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    // left pane
    GridPane queryGrid = new GridPane();
    queryGrid.setVgap( 3.0 );
    
    queryGrid.add( new Label( "Query" ), 0, 0 );
    queryGrid.add( new Label( "Type" ), 0, 1 );
    typeChoice = new ChoiceBox();
    queryGrid.add( typeChoice, 1, 1 );
    queryGrid.add( new Label( "Added in the last..." ), 0, 2 );
    addedChoice = new ChoiceBox();
    queryGrid.add( addedChoice, 1, 2 );
    queryGrid.add( new Label( "Contributor" ), 0, 3 );
    contribChoice = new ChoiceBox();
    queryGrid.add( contribChoice, 1, 3 );
    queryGrid.add( new Label( "Origin" ), 0, 4 );
    originChoice = new ChoiceBox();
    queryGrid.add( originChoice, 1, 4 );
    queryGrid.add( new Label( "Tags" ), 0, 5 );
    qTagsField = new TextField();
    queryGrid.add( qTagsField, 1, 5 );
    
    queryButton = new Button( "Query" );
    queryGrid.add( queryButton, 1, 6 );
     
    // centre pane
    GridPane resultsGrid = new GridPane();
    
    resultsGrid.add( new Label( "Results" ), 0, 0 );
    resultsGrid.getRowConstraints().add( fixedRC );
    
    resultsView = new ListView();
    resultsGrid.add( resultsView, 0, 1 );
    resultsGrid.getRowConstraints().add( vgrowRC );
    
    // right pane
    GridPane detailGrid = new GridPane();

    detailGrid.add( new Label( "Details" ), 0, 0 );
    detailGrid.getRowConstraints().add( fixedRC );
    
    detailGrid.add( new Label( "Name" ), 0, 1 );
    nameField = new TextField();  
    detailGrid.add( nameField, 1, 1 );
    detailGrid.getRowConstraints().add( vgrowRC );
    
    detailGrid.add( new Label( "Contributor" ), 0, 2 );
    contribField = new TextField();
    detailGrid.add( contribField, 1, 2 );
    detailGrid.getRowConstraints().add( vgrowRC );
    
    detailGrid.add( new Label( "Originator" ), 0, 3 );
    originField = new TextField();
    detailGrid.add( originField, 1,3 );
    detailGrid.getRowConstraints().add( vgrowRC );
    
    detailGrid.add( new Label( "Type" ), 0, 4 );
    typeField = new TextField();
    detailGrid.add( typeField, 1, 4 );
    detailGrid.getRowConstraints().add( vgrowRC );
    
    detailGrid.add( new Label( "Privacy" ), 0, 5 );
    privacyField = new TextField();
    detailGrid.add( privacyField, 1, 5 );
    detailGrid.getRowConstraints().add( vgrowRC );
    
    detailGrid.add( new Label( "Description" ), 0, 6 );
    descriptionArea = new TextArea();
    detailGrid.add( descriptionArea, 0, 7 );
    GridPane.setColumnSpan( descriptionArea, 2 );
    detailGrid.getRowConstraints().add( vgrowRC );
    
    detailGrid.add( new Label( "Tags" ), 0, 8 );
    rTagsField = new TextField();
    detailGrid.add( rTagsField, 1, 8 );
    detailGrid.getRowConstraints().add( vgrowRC );
    
    detailGrid.add( new Label( "Added" ), 0, 9 );
    addedField = new TextField();
    detailGrid.add( addedField, 1, 9 );
    detailGrid.getRowConstraints().add( vgrowRC );
    
    deleteButton = new Button( "Delete" );
    detailGrid.add( deleteButton, 0, 10 );
    copyButton = new Button( "Copy to Scratchpad" );
    detailGrid.add( copyButton, 1, 10 );
    detailGrid.getRowConstraints().add( vgrowRC );
    
    HBox hBox = new HBox();
    hBox.setPadding( new Insets( 4.0 ) );
    hBox.setSpacing( 8.0 );
    hBox.getChildren().addAll( queryGrid, resultsGrid, detailGrid );
    
    setContent( hBox );

  }
}
