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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

public class PatchSetsTab extends Tab {
  
  Button importButton, loadEwiButton, deleteButton, copyButton;
  ListView<String> patchSetList;
  
  ListView<EWI4000sPatch> patchListView;
  ObservableList<EWI4000sPatch> patchesInSetOL;

  // TODO: reload functionality
  PatchSetsTab( ScratchPad scratchPad, UserPrefs userPrefs, MidiHandler midiHandler ) {
    
    setText( "Patch Set Library" );
    setClosable( false );
    
    GridPane gp = new GridPane();
    gp.setMaxHeight( Double.MAX_VALUE );
    gp.setId( "scratchpad-grid" );

    Label llLabel = new Label( "Library location: " );
    gp.add( llLabel, 0, 0 );
    Label libLocLabel = new Label( userPrefs.getLibraryLocation() );
    gp.add( libLocLabel, 1, 0 );
    Button libLocButton = new Button( "Change" );
    gp.add( libLocButton, 2, 0 );
    libLocButton.setOnAction( (ae) -> {
      DirectoryChooser dc = new DirectoryChooser();
      dc.setTitle( "Choose EWI Patch Set Library location" );
      if (!userPrefs.getLibraryLocation().equals( "<Not Chosen>" ))
        dc.setInitialDirectory( new File( userPrefs.getLibraryLocation() ) );
      File chosenLLdirFile = dc.showDialog( null );
      if (chosenLLdirFile != null) {
        userPrefs.setLibraryLocation( chosenLLdirFile.getAbsolutePath() );
        libLocLabel.setText( chosenLLdirFile.getAbsolutePath() );
      }
    });

    Label setsLabel = new Label( "Patch Sets" );
    gp.add( setsLabel, 0, 1 );
    
    patchSetList = new ListView<String>();
    gp.add( patchSetList, 0, 2 );
    GridPane.setColumnSpan( patchSetList, 3 );
    if (!userPrefs.getLibraryLocation().equals( "<Not Chosen>" )){
      File llFile = new File( userPrefs.getLibraryLocation() );
      patchSetList.getItems().addAll( llFile.list( new FilenameFilter() {
        public boolean accept( File llFile, String name ) {
          return name.toLowerCase().endsWith( ".syx" );
        }
      }) );
    } 
    
    // Handle changes to the set list selection
    patchSetList.getSelectionModel().selectedItemProperty().addListener( 
        (ObservableValue< ? extends String > observable, String oldValue, String newValue ) -> {
          Debugger.log( "DEBUG - changed - " + newValue + " chosen" );
          patchesInSetOL.clear();
          Path path = Paths.get( userPrefs.getLibraryLocation(), newValue );
          try {
            byte[] allBytes = Files.readAllBytes( path );
            if ((allBytes != null) && allBytes.length > 200 ) {
              Debugger.log( "DEBUG - bytes read: " + allBytes.length );
              for (int byteOffset = 0; byteOffset < allBytes.length; byteOffset += EWI4000sPatch.EWI_PATCH_LENGTH ) {
                EWI4000sPatch ep = new EWI4000sPatch();
                ep.patchBlob = Arrays.copyOfRange( allBytes, byteOffset, byteOffset + EWI4000sPatch.EWI_PATCH_LENGTH  );
                ep.decodeBlob();
                patchesInSetOL.add( ep );
                //Debugger.log( "DEBUG - patch loaded" );
              }
              patchListView.setItems( null );
              patchListView.setItems( patchesInSetOL );
              loadEwiButton.setDisable( false );
              deleteButton.setDisable( false );
              copyButton.setDisable( true );
            }
          } catch( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } 
        }); 
  
    importButton = new Button( "Import" );
    gp.add( importButton, 0, 3 );
    loadEwiButton = new Button( "Load into EWI" );
    loadEwiButton.setOnAction( (ae) -> {
      if (patchSetList.getSelectionModel().getSelectedIndex() != -1) {
        Alert al = new Alert( AlertType.CONFIRMATION );
        al.setTitle( "EWItool - Confirmation" );
        al.setHeaderText( "Confirm Overwriting EWI4000s Contents" );
        al.setContentText( "This will overwrite all the patches in the EWI with the chosen Patch Set.  "
            + "Do you really want to do this?" );
        Optional<ButtonType> response = al.showAndWait();
        if(response.isPresent() && response.get() == ButtonType.OK) {  
          Alert sbusyAlert = new Alert( AlertType.INFORMATION, "Sending all patches.  Please wait..." );
          sbusyAlert.setTitle( "EWItool" );
          sbusyAlert.setHeaderText( null );
          sbusyAlert.show();
          for ( int p = 0; p < 100; p++ ) {
            midiHandler.sendPatch( patchesInSetOL.get( p ), EWI4000sPatch.EWI_SAVE );
            sbusyAlert.setTitle( (p+1) + " of 100" );
            // wait for sendQ to empty (otherwise the requests all queue up in a flash
            // and this method finishes...
            while( midiHandler.sharedData.sendQ.size() > 0) {
              try {
                Thread.sleep( 10 );
              } catch( Exception e ) {
                e.printStackTrace();
              }
            }
          } 
          sbusyAlert.close();
        }
      }
    });
    gp.add( loadEwiButton, 1, 3 );
    loadEwiButton.setDisable( true );
    deleteButton = new Button( "Delete" );
    gp.add( deleteButton, 2, 3 );
    deleteButton.setDisable( true );
    
    Label patchesLabel = new Label( "Patches" );
    gp.add( patchesLabel, 3, 0 );
    
    patchesInSetOL = FXCollections.observableArrayList( );
    
    patchListView = new ListView<EWI4000sPatch>( patchesInSetOL );
    gp.add( patchListView, 3, 1 );
    GridPane.setRowSpan( patchListView, 2 );
    patchListView.setCellFactory( new Callback<ListView<EWI4000sPatch>, ListCell<EWI4000sPatch>>(){
      @Override 
      public ListCell<EWI4000sPatch> call( ListView<EWI4000sPatch> p ) {
        ListCell<EWI4000sPatch> cell = new ListCell<EWI4000sPatch>() {
          @Override
          protected void updateItem( EWI4000sPatch ep, boolean bln ) {
            super.updateItem( ep, bln );
            if (ep != null) {
              String patchName = new String( ep.name );
              // Debugger.log( "DEBUG - patch loaded - " + patchName );
              setText( patchName );
            }
          }
        };
        return cell;
      }
    });
    
    // Handle changes to the patch list selection
    patchListView.getSelectionModel().selectedItemProperty().addListener( 
        (ObservableValue< ? extends EWI4000sPatch > observable, EWI4000sPatch oldValue, EWI4000sPatch newValue ) -> {
          if (newValue != null) {
            Debugger.log( "DEBUG - Patch selected: " + new String( newValue.name ) );
            copyButton.setDisable( false );
          }
        }); 

    copyButton = new Button( "Copy to Scratchpad" );
    copyButton.setDisable( true );
    copyButton.setOnAction( (ae) -> {
      if (patchListView.getSelectionModel().getSelectedIndex() != -1) {
        scratchPad.addPatch( patchListView.getSelectionModel().getSelectedItem() );
      }
    });
    gp.add( copyButton, 3, 3 );
        
    setContent( gp );
    
  }

}
