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
	
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	
  static final String  APP_NAME = "EWItool";
	static final double  VERSION = 0.1;
	static final int     COPYRIGHT_YEAR = 2015;
	static final String  RELEASE_STATUS = "Alpha";
	
	private static final String  ICON = "/resources/EWItoolLogo1.png";
	private static final int     SCENE_PREF_WIDTH = 1100;
	private static final int     SCENE_PREF_HEIGHT = 750;
	private static final String  WINDOW_TITLE = APP_NAME + " - EWI4000s Patch Handling Tool";
	
	MenuBar mainMenuBar;
	TabPane tabPane;
	Tab statusTab, scratchPadTab, patchSetsTab, epxTab, currentPatchSetTab, keyPatchesTab, patchEditorTab; 
	MidiHandler midiHandler;
	volatile SharedData sharedData;

	public static void main(String[] args) {
	    launch(args);
	  }
	 
	@Override
	public void start(Stage mainStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene( root, SCENE_PREF_WIDTH, SCENE_PREF_HEIGHT );
			scene.getStylesheets().add(getClass().getResource("ewitool.css").toExternalForm());
			mainStage.setTitle(WINDOW_TITLE);
			
			Prefs userPrefs = new Prefs();
			ScratchPad scratchPad = new ScratchPad();
			sharedData = new SharedData();

			midiHandler = new MidiHandler( sharedData, userPrefs ); 
			
			mainMenuBar = new MainMenuBar( mainStage, userPrefs, midiHandler );
			root.setTop( mainMenuBar );
			
			tabPane = new TabPane();
			
			statusTab = new StatusTab();
			tabPane.getTabs().add( statusTab );
			
			scratchPadTab = new ScratchPadTab( scratchPad );
			tabPane.getTabs().add( scratchPadTab );
			
			patchSetsTab = new PatchSetsTab( scratchPad );
			tabPane.getTabs().add( patchSetsTab );
			
			epxTab = new EPXTab();
			tabPane.getTabs().add( epxTab );
			//epxTab.setDisable( true );
			
			currentPatchSetTab = new CurrentPatchSetTab( sharedData, scratchPad );
			tabPane.getTabs().add( currentPatchSetTab );
			
			keyPatchesTab = new KeyPatchesTab();
			tabPane.getTabs().add( keyPatchesTab );
			
			patchEditorTab = new PatchEditorTab( sharedData, midiHandler );
			tabPane.getTabs().add( patchEditorTab );
			
			// MIDI port assignment change listeners
			userPrefs.midiInPort.addListener( new ChangeListener<String>() {
			  @Override
			  public void changed( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
			    System.out.println( "Debug - Noticed that IN Port Changed to : " + newValue );
			    midiHandler.restart();
			  }
			});
			userPrefs.midiOutPort.addListener( new ChangeListener<String>() {
			  @Override
			  public void changed( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
			    System.out.println( "Debug - Noticed that OUT Port Changed to : " + newValue );
			    midiHandler.restart();
			  }
			});
			
	    EditPatchWatcher editPatchWatcher = new EditPatchWatcher();
	    sharedData.addObserver( editPatchWatcher );			
			
      // customise icon
      mainStage.getIcons().add( new Image( this.getClass().getResourceAsStream( ICON )));
      
			mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			  @Override
			  public void handle( WindowEvent we ) {
			    System.out.println( "DEBUG - clean exit" );
	        midiHandler.close();
			    Platform.exit();
			    System.exit( 0 );           
			  }
			});


			root.setCenter( tabPane );
			mainStage.setScene(scene);
			mainStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
  class EditPatchWatcher implements Observer {
    @Override
    public void update( Observable o, Object arg ) {
      if ((int)arg == SharedData.EDIT_PATCH) {
        System.out.println( "DEBUG - Main: noticed shared data change" );
        tabPane.getSelectionModel().select( patchEditorTab );
      }
    }
  }
  
  class MainMenuBar extends MenuBar {

    Menu fileMenu, midiMenu, ewiMenu, patchMenu, helpMenu;
    MenuItem printItem, preferencesItem, quitItem,
             portsItem, panicItem, 
             fetchAllItem,
             helpItem, aboutItem;
    
    public MainMenuBar( Stage mainStage, Prefs userPrefs, MidiHandler midiHandler ) {
      
      fileMenu = new Menu( "File" );
      printItem = new MenuItem( "Print" );
      preferencesItem = new MenuItem( "Preferences" );
      quitItem = new MenuItem( "Quit" );
      quitItem.setOnAction( new EventHandler<ActionEvent>() {
        @Override
        public void handle( ActionEvent ae) {
          System.out.println( "DEBUG - clean exit" );
          midiHandler.close();
          Platform.exit();
          System.exit( 0 );           
        }
      });
      fileMenu.getItems().addAll( printItem, preferencesItem, quitItem );
      
      midiMenu = new Menu( "MIDI" );
      portsItem = new MenuItem( "Ports" );
      portsItem.addEventHandler( ActionEvent.ANY, new PortsItemEventHandler( userPrefs ) );
   
      panicItem = new MenuItem( "Panic (All Notes Off)" );
   
      midiMenu.getItems().addAll( portsItem, panicItem );
        
      ewiMenu = new Menu( "EWI" );
      fetchAllItem = new MenuItem( "Fetch All Patches" );
      fetchAllItem.setOnAction( new EventHandler<ActionEvent>() {
        @Override
        public void handle( ActionEvent ae) {
          System.out.println( "DEBUG - Fetch All..." );
          Alert busyAlert = new Alert( AlertType.INFORMATION, "Fetching Patches..." );
          busyAlert.setTitle( "EWItool" );
          busyAlert.setHeaderText( null );
          busyAlert.show();
          midiHandler.clearPatches();
          for (int p = 0; p < EWI4000sPatch.EWI_NUM_PATCHES; p++) {
            midiHandler.requestPatch( p );
          }
          busyAlert.close();
          tabPane.getSelectionModel().select( currentPatchSetTab );
        }
      });
      ewiMenu.getItems().addAll( fetchAllItem );

      patchMenu = new Menu( "Patch" );
      
      helpMenu = new Menu( "Help" );
      helpItem = new MenuItem( "Online Help" );
      aboutItem = new MenuItem( "About " + Main.APP_NAME );
      helpMenu.getItems().addAll( helpItem, aboutItem );
      
      getMenus().addAll( fileMenu, midiMenu, ewiMenu, patchMenu, helpMenu );
      
    }

  }
}
