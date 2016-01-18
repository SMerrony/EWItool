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

import java.util.Optional;

import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;

public class ScratchPadTab extends Tab {

  Button clearButton, deleteButton, renameButton, 
  viewHexButton, exchangeButton, exportButton;
  ListView<EWI4000sPatch> patchList;
  ScratchPad scratchPad;

  ScratchPadTab( ScratchPad scratchPad, Tab epxTab ) {

    setText( "Scratchpad" );
    setClosable( false );

    if (!scratchPad.load()) {
      Alert warnAlert = new Alert( AlertType.WARNING );
      warnAlert.setTitle( "EWItool - Warning" );
      warnAlert.setContentText( "Cannot load Scratchpad.  It could be that the Library Path is not set or the directory is inaccessible.  " + 
          "Please check the location on the Patch Set Library tab before continuing." );
      warnAlert.showAndWait();
    }

    GridPane gp = new GridPane();
    gp.setMaxHeight( Double.MAX_VALUE );
    gp.setId( "scratchpad-grid" );

    ColumnConstraints col1 = new ColumnConstraints( 100.0, 200.0, Double.MAX_VALUE );
    col1.setHgrow( Priority.ALWAYS );
    gp.getColumnConstraints().addAll( col1 );
    RowConstraints row1 = new RowConstraints( 100.0, 200.0, Double.MAX_VALUE );
    row1.setVgrow( Priority.ALWAYS );
    gp.getRowConstraints().addAll( row1 );

    patchList = new ListView<EWI4000sPatch>( scratchPad.patchList );
    patchList.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
    patchList.setCellFactory( new Callback<ListView<EWI4000sPatch>, ListCell<EWI4000sPatch>>(){
      @Override 
      public ListCell<EWI4000sPatch> call( ListView<EWI4000sPatch> p ) {
        ListCell<EWI4000sPatch> cell = new ListCell<EWI4000sPatch>() {
          @Override
          protected void updateItem( EWI4000sPatch ep, boolean bln ) {
            super.updateItem( ep, bln );
            if (ep != null) {
              setText( ep.getName() );
            } else {
              setText( "" );
            }
          }
        };
        return cell;
      }
    });

    gp.add( patchList, 0, 0, 1, 2 );

    clearButton = new Button( "Clear" );
    clearButton.setOnAction( (ae) -> {
      Alert clrAl = new Alert( AlertType.CONFIRMATION );
      clrAl.setTitle( "EWItool - Clear Scratchpad" );
      clrAl.setContentText( "Do you really want to clear the scratchpad?" );
      clrAl.showAndWait()
        .filter( response -> response == ButtonType.OK )
        .ifPresent( response -> scratchPad.clearAll() );
    });
    gp.add( clearButton, 1, 0 );
    GridPane.setValignment( clearButton, VPos.CENTER );

    deleteButton = new Button( "Delete" );
    deleteButton.setOnAction( (ae) -> {
      if (patchList.getSelectionModel().getSelectedIndex() != -1) {
        scratchPad.removePatch( patchList.getSelectionModel().getSelectedIndex() );
      }
    } );
    gp.add( deleteButton, 2, 0 );

    renameButton = new Button( "Rename" );
    renameButton.setOnAction( (ae) -> {
      if (patchList.getSelectionModel().getSelectedIndex() != -1) {
        TextInputDialog tid = new TextInputDialog();
        tid.setTitle( "EWItool - Rename" );
        tid.setHeaderText( "Rename patch on Scratchpad" );
        tid.setContentText( "New name:" );
        Optional<String> result = tid.showAndWait();
        if (result.isPresent()) {
          scratchPad.renamePatch( patchList.getSelectionModel().getSelectedIndex(), result.get() );
          patchList.refresh();
        }
      }
    } );
    gp.add( renameButton, 3, 0 );	

    viewHexButton = new Button( "View in Hex" );
    viewHexButton.setOnAction( (event) -> {
      if (patchList.getSelectionModel().getSelectedIndex() != -1) {
        byte[] blob = scratchPad.patchList.get( patchList.getSelectionModel().getSelectedIndex() ).patchBlob;
        UiHexViewDialog hexDialog = new UiHexViewDialog( blob );
        hexDialog.showAndWait();
      }
    } ); 
    gp.add( viewHexButton, 1, 1 );

    exchangeButton = new Button( "Add to EWI Patch eXchange" );
    exchangeButton.setOnAction( (ae) -> {
      if (patchList.getSelectionModel().getSelectedIndex() != -1) {
        ((EPXTab) epxTab).contribute( patchList.getSelectionModel().getSelectedItem() );
      }
    });
    gp.add( exchangeButton, 2, 1 );

    exportButton = new Button( "Export" );
    gp.add( exportButton, 3, 1 );

    setContent( gp );

  }
}
