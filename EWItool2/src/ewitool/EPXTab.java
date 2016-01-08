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

import java.util.LinkedList;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class EPXTab extends Tab {

  ChoiceBox<String> typeChoice, addedChoice, contribChoice, originChoice;
  TextField qTagsField;
  Button queryButton;
  TextField uidField, serverField;
  PasswordField passwdField;
  ListView<String> resultsView;
  TextField nameField, contribField, originField, typeField,
            rTagsField, addedField;
  CheckBox privacyCheckBox;
  TextArea  descriptionArea;
  Button deleteButton, copyButton;
  String hexPatch;
  
  EPX epx;
  
  EPXTab( SharedData sharedData, ScratchPad scratchPad, UserPrefs userPrefs ) {
    
    setText( "EWI Patch eXchange" );
    setClosable( false );
    
    epx = new EPX( sharedData, userPrefs );
 
    // for vertically fixed rows in the GridPanes...
    RowConstraints fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    // for vertically growable rows in the GridPanes...
    RowConstraints vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    // left top pane
    GridPane queryGrid = new GridPane();
    queryGrid.setId( "epx-query-grid" );
    
    Label querySectionLabel = new Label( "Query" );
    querySectionLabel.setId( "epx-section-label" );
    queryGrid.add( querySectionLabel, 0, 0 );
    queryGrid.add( new Label( "Type" ), 0, 1 );
    typeChoice = new ChoiceBox<String>();
    typeChoice.getItems().addAll( "All" );
    typeChoice.getSelectionModel().select( 0 );
    queryGrid.add( typeChoice, 1, 1 );
    queryGrid.add( new Label( "Added in the last..." ), 0, 2 );
    addedChoice = new ChoiceBox<String>();
    addedChoice.getItems().addAll( "All", "1 day", "7 days", "1 month", "3 months", "1 year" );
    addedChoice.getSelectionModel().select( 0 );
    queryGrid.add( addedChoice, 1, 2 );
    queryGrid.add( new Label( "Contributor" ), 0, 3 );
    contribChoice = new ChoiceBox<String>();
    contribChoice.getItems().addAll( "All" ); // more added programatically
    contribChoice.getSelectionModel().select( 0 );
    queryGrid.add( contribChoice, 1, 3 );
    queryGrid.add( new Label( "Origin" ), 0, 4 );
    originChoice = new ChoiceBox<String>();
    originChoice.getItems().addAll( "All" ); // more added programatically
    originChoice.getSelectionModel().select( 0 );
    queryGrid.add( originChoice, 1, 4 );
    queryGrid.add( new Label( "Tags" ), 0, 5 );
    qTagsField = new TextField();
    queryGrid.add( qTagsField, 1, 5 );
    
    queryButton = new Button( "Query" );
    queryButton.setOnAction( (ae) -> {
      LinkedList<EPX.QueryResult> lqr = epx.query( typeChoice.getSelectionModel().getSelectedItem(),
                                                   addedChoice.getSelectionModel().getSelectedItem(),
                                                   contribChoice.getSelectionModel().getSelectedItem(),
                                                   originChoice.getSelectionModel().getSelectedItem(),
                                                   qTagsField.getText()
                                                 );
      resultsView.getItems().clear();
      for (EPX.QueryResult qr : lqr) {
        resultsView.getItems().add( qr.name_user + " #" + qr.epx_id );
      }
    });
    queryGrid.add( queryButton, 1, 6 );
    queryGrid.setDisable( true );  // disabled until we know we can connect
    
    // left bottom pane
    GridPane settingsGrid = new UiEPXSettingsGrid( userPrefs, epx, queryGrid );
    settingsGrid.setId( "epx-settings-grid" );
     
    // centre pane
    GridPane resultsGrid = new GridPane();
    
    Label resultsSectionLabel = new Label( "Results" );
    resultsSectionLabel.setId( "epx-section-label" );
    resultsGrid.add( resultsSectionLabel, 0, 0 );
    resultsGrid.getRowConstraints().add( fixedRC );
    
    resultsView = new ListView<String>();
    resultsView.getSelectionModel().selectedItemProperty().addListener( (item)-> {
      if (resultsView.getSelectionModel().getSelectedIndex() != -1){
        String selected = resultsView.getSelectionModel().getSelectedItem();
        int epxId = Integer.parseInt( selected.substring( selected.lastIndexOf( '#' ) + 1 ) );
        EPX.DetailsResult dr = epx.getDetails( epxId );
        if (dr != null) {
          nameField.setText( dr.name );
          contribField.setText( dr.contrib );
          originField.setText( dr.origin );
          typeField.setText( dr.type );
          privacyCheckBox.setSelected( dr.privateFlag );
          descriptionArea.setText( dr.desc );
          rTagsField.setText( dr.tags );
          addedField.setText( dr.added );
          if (dr.contrib.contentEquals( userPrefs.getEpxUserid() ))
            deleteButton.setDisable( false );
          else
            deleteButton.setDisable( true );
          copyButton.setDisable( false );
          hexPatch = dr.hex;
        }
      }
    });
    resultsGrid.add( resultsView, 0, 1 );
    resultsGrid.getRowConstraints().add( vgrowRC );
    
    // right pane
    GridPane detailGrid = new GridPane();
    detailGrid.setId( "epx-details-grid" );

    Label detailsSectionLabel = new Label( "Details" );
    detailsSectionLabel.setId( "epx-section-label" );
    detailGrid.add( detailsSectionLabel, 0, 0 );
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
    
    detailGrid.add( new Label( "Private" ), 0, 5 );
    privacyCheckBox = new CheckBox();
    detailGrid.add( privacyCheckBox, 1, 5 );
    detailGrid.getRowConstraints().add( vgrowRC );
    
    detailGrid.add( new Label( "Description" ), 0, 6 );
    descriptionArea = new TextArea();
    descriptionArea.setWrapText( true );
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
    deleteButton.setDisable( true );
    deleteButton.setOnAction( (ae) -> {
      // TODO 
    });
    detailGrid.add( deleteButton, 0, 10 );
    copyButton = new Button( "Copy to Scratchpad" );
    copyButton.setDisable( true );
    copyButton.setOnAction( (ae) -> {
      EWI4000sPatch tmpPatch = new EWI4000sPatch( hexPatch );
      scratchPad.addPatch( tmpPatch );
    });
    detailGrid.add( copyButton, 1, 10 );
    detailGrid.getRowConstraints().add( vgrowRC );
        
    GridPane gp = new GridPane();
    gp.setId( "epx-grid" );
    
    ColumnConstraints ccsGrowable = new ColumnConstraints( 40.0, 90.0, Double.MAX_VALUE );
    RowConstraints rcsGrowable = new RowConstraints();
    ccsGrowable.setHgrow( Priority.ALWAYS );
    rcsGrowable.setVgrow( Priority.ALWAYS );
    gp.getColumnConstraints().addAll( ccsGrowable, ccsGrowable, ccsGrowable );
    gp.getRowConstraints().addAll( rcsGrowable, rcsGrowable );
    
    gp.add( queryGrid, 0, 0 );
    GridPane.setRowSpan( resultsGrid, 2 );
    GridPane.setRowSpan( detailGrid, 2 );
    gp.add( resultsGrid, 1, 0 );
    gp.add( detailGrid, 2, 0 );
    gp.add( settingsGrid, 0, 1 );
    
    setContent( gp );
    
    // enable the query grid if we can connect with user's credentials to EPX
    if (epx.testConnection() && epx.testUser()) {
      queryGrid.setDisable( false );
      String[] dropdownData = epx.getDropdowns();
      typeChoice.getItems().addAll( dropdownData[0].split( "," ) );
      contribChoice.getItems().addAll( dropdownData[1].split( "," ) );
      originChoice.getItems().addAll( dropdownData[2].split( "," ) );
    }

  }
}
