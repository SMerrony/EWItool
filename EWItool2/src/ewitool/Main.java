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

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
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
			
			mainMenuBar = new MainMenuBar( mainStage );
			root.setTop( mainMenuBar );
			
			TabPane tabPane = new TabPane();
			
			statusTab = new StatusTab();
			tabPane.getTabs().add( statusTab );
			
			scratchPadTab = new ScratchPadTab();
			tabPane.getTabs().add( scratchPadTab );
			
			patchSetsTab = new PatchSetsTab();
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
			
			root.setCenter( tabPane );
			mainStage.setScene(scene);
			mainStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
