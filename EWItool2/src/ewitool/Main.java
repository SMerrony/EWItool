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
	
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private static final double  VERSION = 0.1;
	private static final int     COPYRIGHT_YEAR = 2015;
	private static final String  RELEASE_STATUS = "Alpha";
	private static final int     SCENE_PREF_WIDTH = 1100;
	private static final int     SCENE_PREF_HEIGHT = 750;
	private static final String  WINDOW_TITLE = "EWItool - EWI4000s Patch Handling Tool";
	
	MenuBar mainMenuBar;
	Tab statusTab, scratchPadTab, patchSetsTab, epxTab, currentPatchSetTab, keyPatchesTab, patchEditorTab; 
	MidiHandler midiHandler;
	
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
			midiHandler = new MidiHandler();
			
			mainMenuBar = new MainMenuBar( mainStage, userPrefs );
			root.setTop( mainMenuBar );
			
			TabPane tabPane = new TabPane();
			
			statusTab = new StatusTab();
			tabPane.getTabs().add( statusTab );
			
			scratchPadTab = new ScratchPadTab( scratchPad );
			tabPane.getTabs().add( scratchPadTab );
			
			patchSetsTab = new PatchSetsTab( scratchPad );
			tabPane.getTabs().add( patchSetsTab );
			
			epxTab = new EPXTab();
			tabPane.getTabs().add( epxTab );
			//epxTab.setDisable( true );
			
			currentPatchSetTab = new CurrentPatchSetTab();
			tabPane.getTabs().add( currentPatchSetTab );
			
			keyPatchesTab = new KeyPatchesTab();
			tabPane.getTabs().add( keyPatchesTab );
			
			patchEditorTab = new PatchEditorTab();
			tabPane.getTabs().add( patchEditorTab );
			
			// MIDI port assignment change listener
			userPrefs.midiInPort.addListener( new ChangeListener<String>() {
			  @Override
			  public void changed( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
			    System.out.println( "Debug - Noticed that IN Port Changed to : " + newValue );
			    midiHandler.stop();
			    midiHandler = null;
			    midiHandler = new MidiHandler();
			  }
			});

			root.setCenter( tabPane );
			mainStage.setScene(scene);
			mainStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
