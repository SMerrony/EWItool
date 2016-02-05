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

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EPXTab extends Tab {

  ChoiceBox<String> typeChoice, addedChoice, contribChoice, originChoice;
  TextField qTagsField;
  Button queryButton;
  ListView<String> resultsView;
  String hexPatch;
  GridPane detailGrid;
  Button deleteButton, copyButton;
  
  EPX epx;
  
  private int currentEpxPatchID;
  
  EPXTab( SharedData sharedData, ScratchPad scratchPad, UserPrefs userPrefs ) {
    
    setText( "EWI Patch eXchange" );
    setClosable( false );
    
    epx = new EPX( sharedData, userPrefs );
    currentEpxPatchID = -1;
 
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
    typeChoice = new ChoiceBox<>();
    typeChoice.getItems().addAll( "All" );
    typeChoice.getSelectionModel().select( 0 );
    queryGrid.add( typeChoice, 1, 1 );
    queryGrid.add( new Label( "Added in the last..." ), 0, 2 );
    addedChoice = new ChoiceBox<>();
    addedChoice.getItems().addAll( "All", "1 day", "7 days", "1 month", "3 months", "1 year" );
    addedChoice.getSelectionModel().select( 0 );
    queryGrid.add( addedChoice, 1, 2 );
    queryGrid.add( new Label( "Contributor" ), 0, 3 );
    contribChoice = new ChoiceBox<>();
    contribChoice.getItems().addAll( "All" ); // more added programatically
    contribChoice.getSelectionModel().select( 0 );
    queryGrid.add( contribChoice, 1, 3 );
    queryGrid.add( new Label( "Origin" ), 0, 4 );
    originChoice = new ChoiceBox<>();
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
      lqr.stream().forEach( (qr) ->  resultsView.getItems().add( qr.name_user + " #" + qr.epx_id )  );
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
    
    resultsView = new ListView<>();
    resultsView.getSelectionModel().selectedItemProperty().addListener( (item)-> {
      if (resultsView.getSelectionModel().getSelectedIndex() != -1){
        String selected = resultsView.getSelectionModel().getSelectedItem();
        currentEpxPatchID = Integer.parseInt( selected.substring( selected.lastIndexOf( '#' ) + 1 ) );
        EPX.DetailsResult dr = epx.getDetails( currentEpxPatchID );
        if (dr != null) {
          ((UiEPXDetailsGrid) detailGrid).setFields( dr );
          if (dr.contrib != null && dr.contrib.contentEquals( userPrefs.getEpxUserid() ))
            deleteButton.setDisable( false );
          else
            deleteButton.setDisable( true );
          copyButton.setDisable( false );
          hexPatch = dr.hex;
        }
      } else {
        currentEpxPatchID = -1;
      }
    });
    resultsGrid.add( resultsView, 0, 1 );
    resultsGrid.getRowConstraints().add( vgrowRC );
    
    // right pane
    detailGrid = new UiEPXDetailsGrid();
    detailGrid.setId( "epx-details-grid" );

    deleteButton = new Button( "Delete" );
    deleteButton.setDisable( true );
    deleteButton.setOnAction( (ae) -> {
      if (currentEpxPatchID != -1) {
        epx.deletePatch( currentEpxPatchID );
        ((UiEPXDetailsGrid) detailGrid).clearFields();
        queryButton.fire();
      }
    });

    copyButton = new Button( "Copy to Scratchpad" );
    copyButton.setDisable( true );
    copyButton.setOnAction( (ae) -> {
      EWI4000sPatch tmpPatch = new EWI4000sPatch( hexPatch );
      scratchPad.addPatch( tmpPatch );
      sharedData.setStatusMessage( ((UiEPXDetailsGrid) detailGrid).getName() + " copied to Scratchpad" );
    });
         
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
    gp.add( deleteButton, 1, 2 );
    gp.add( copyButton, 2, 2 );
    
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
  
  public void contribute( EWI4000sPatch ewi4000sPatch ) {
    if (epx.testConnection() && epx.testUser()) {
      final Stage contDialog = new Stage();
      contDialog.initModality( Modality.APPLICATION_MODAL );
      UiEPXSubmitGrid esGrid = new UiEPXSubmitGrid( ewi4000sPatch.getName(), ewi4000sPatch.toHex() );
      Scene contScene = new Scene( esGrid, 450, 400 );
      contScene.getStylesheets().add(getClass().getResource("ewitool.css").toExternalForm());
      contDialog.setScene( contScene );
      contDialog.setTitle( "EWItool - EPX Contribution" );
      String[] dropdownData = epx.getDropdowns();
      esGrid.typeChoice.getItems().addAll( dropdownData[0].split( "," ) );
      esGrid.cancelButton.setOnAction( (ae) -> contDialog.close() );
      esGrid.okButton.setOnAction( (ae) -> {
        if ( esGrid.originField.getText().isEmpty() ||
             esGrid.typeChoice.getSelectionModel().getSelectedIndex() < 1 ||
             esGrid.descriptionArea.getText().length() > 255 ) {
          Alert al = new Alert( AlertType.WARNING );
          al.setTitle( "EWItool - Data Entry Error" );
          al.setContentText( "Please fill in all the required fields" );
          al.showAndWait();
        } else {
          epx.insertPatch( esGrid.nameField.getText(), 
                           esGrid.originField.getText(), 
                           esGrid.typeChoice.getSelectionModel().getSelectedItem(), 
                           esGrid.descriptionArea.getText(), 
                           esGrid.privacyCheckBox.isSelected(), 
                           esGrid.tagsField.getText(), 
                           esGrid.hexPatch );
        }
      });
      contDialog.showAndWait();

    } else {
      Alert al = new Alert( AlertType.WARNING );
      al.setTitle( "EWItool Warning" );
      al.setContentText( "You are not connected/logged-in to the EWI Patch eXchange." );
      al.showAndWait();
    }
  }
}
