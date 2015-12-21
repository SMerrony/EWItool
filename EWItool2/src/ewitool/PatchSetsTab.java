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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

public class PatchSetsTab extends Tab {
  
  Button importButton, loadEwiButton, deleteButton, copyButton;
  ListView<String> patchSetList;
  
  ListView<EWI4000sPatch> patchListView;
  ObservableList<EWI4000sPatch> patchesInSetOL;

  PatchSetsTab( ScratchPad scratchPad ) {
    
    setText( "Patch Set Library" );
    setClosable( false );
    
    GridPane gp = new GridPane();
    gp.setMaxHeight( Double.MAX_VALUE );
    gp.setId( "scratchpad-grid" );

    Label llLabel = new Label( "Library location: " );
    gp.add( llLabel, 0, 0 );
    Label libLocLabel = new Label( Prefs.getLibraryLocation() );
    gp.add( libLocLabel, 1, 0 );
    Button libLocButton = new Button( "Change" );
    gp.add( libLocButton, 2, 0 );
    libLocButton.setOnAction( new EventHandler<ActionEvent>() {
      @Override public void handle( ActionEvent ae ) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle( "Choose EWI Patch Set Library location" );
        if (!Prefs.getLibraryLocation().equals( "<Not Chosen>" ))
          dc.setInitialDirectory( new File( Prefs.getLibraryLocation() ) );
        File chosenLLdirFile = dc.showDialog( null );
        if (chosenLLdirFile != null) {
          Prefs.setLibraryLocation( chosenLLdirFile.getAbsolutePath() );
          libLocLabel.setText( chosenLLdirFile.getAbsolutePath() );
        }
      }
    });

    Label setsLabel = new Label( "Patch Sets" );
    gp.add( setsLabel, 0, 1 );
    
    patchSetList = new ListView<String>();
    gp.add( patchSetList, 0, 2 );
    GridPane.setColumnSpan( patchSetList, 3 );
    if (!Prefs.getLibraryLocation().equals( "<Not Chosen>" )){
      File llFile = new File( Prefs.getLibraryLocation() );
      patchSetList.getItems().addAll( llFile.list( new FilenameFilter() {
        public boolean accept( File llFile, String name ) {
          return name.toLowerCase().endsWith( ".syx" );
        }
      }) );
    } 
    
    // Handle changes to the set list selection
    patchSetList.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<String>() {
      @Override
      public void changed( ObservableValue< ? extends String > observable, String oldValue, String newValue ) {
        System.out.println( "DEBUG - changed - " + newValue + " chosen" );
        patchesInSetOL.clear();
        Path path = Paths.get( Prefs.getLibraryLocation(), newValue );
        try {
          byte[] allBytes = Files.readAllBytes( path );
          if ((allBytes != null) && allBytes.length > 200 ) {
            System.out.println( "DEBUG - bytes read: " + allBytes.length );
            for (int byteOffset = 0; byteOffset < allBytes.length; byteOffset += EWI4000sPatch.EWI_PATCH_LENGTH ) {
              EWI4000sPatch ep = new EWI4000sPatch();
              ep.patchBlob = Arrays.copyOfRange( allBytes, byteOffset, byteOffset + EWI4000sPatch.EWI_PATCH_LENGTH  );
              ep.decodeBlob();
              patchesInSetOL.add( ep );
              //System.out.println( "DEBUG - patch loaded" );
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

      }   
    }); 
  
    importButton = new Button( "Import" );
    gp.add( importButton, 0, 3 );
    loadEwiButton = new Button( "Load into EWI" );
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
              // System.out.println( "DEBUG - patch loaded - " + patchName );
              setText( patchName );
            }
          }
        };
        return cell;
      }
    });
    
    // Handle changes to the patch list selection
    patchListView.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<EWI4000sPatch>() {
      @Override
      public void changed( ObservableValue< ? extends EWI4000sPatch > observable, EWI4000sPatch oldValue,
          EWI4000sPatch newValue ) {
        if (newValue != null) {
          System.out.println( "DEBUG - Patch selected: " + new String( newValue.name ) );
          copyButton.setDisable( false );
        }
      }   
    }); 

    copyButton = new Button( "Copy to Scratchpad" );
    copyButton.setDisable( true );
    copyButton.setOnAction( new EventHandler<ActionEvent>() {
      @Override public void handle( ActionEvent ae ) {
        if (patchListView.getSelectionModel().getSelectedIndex() != -1) {
          scratchPad.addPatch( patchListView.getSelectionModel().getSelectedItem() );
        }
      }
    });
    gp.add( copyButton, 3, 3 );
        
    setContent( gp );
    
  }

}
