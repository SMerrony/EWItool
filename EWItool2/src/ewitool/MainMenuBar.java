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

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class MainMenuBar extends MenuBar {

  Menu fileMenu, midiMenu, ewiMenu, patchMenu, helpMenu;
  MenuItem printItem, preferencesItem, quitItem,
           portsItem, panicItem, 
           fetchAllItem,
           helpItem, aboutItem;
  
  public MainMenuBar( Stage mainStage ) {
    
    fileMenu = new Menu( "File" );
    printItem = new MenuItem( "Print" );
    preferencesItem = new MenuItem( "Preferences" );
    quitItem = new MenuItem( "Quit" );
    fileMenu.getItems().addAll( printItem, preferencesItem, quitItem );
    
    midiMenu = new Menu( "MIDI" );
    portsItem = new MenuItem( "Ports" );
    portsItem.addEventHandler( ActionEvent.ANY, new MidiPortsEventHandler() );
 
    panicItem = new MenuItem( "Panic (All Notes Off)" );
 
    midiMenu.getItems().addAll( portsItem, panicItem );
    
    
    ewiMenu = new Menu( "EWI" );
    fetchAllItem = new MenuItem( "Fetch All Patches" );
    ewiMenu.getItems().addAll( fetchAllItem );
    
    patchMenu = new Menu( "Patch" );
    
    helpMenu = new Menu( "Help" );
    helpItem = new MenuItem( "Online Help" );
    aboutItem = new MenuItem( "About" );
    helpMenu.getItems().addAll( helpItem, aboutItem );
    
    getMenus().addAll( fileMenu, midiMenu, ewiMenu, patchMenu, helpMenu );
    
  }

}
