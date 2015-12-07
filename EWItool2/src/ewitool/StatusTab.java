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

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class StatusTab extends Tab {
	
  StatusLabel ewiField, epxField, scratchpadField, libLocField;
	
	StatusTab() {
		this.setText( "Status" );
		this.setClosable( false );
		GridPane gp = new GridPane();
		gp.setId( "status-grid" );
				
		gp.add( new Label( "EWI Connection" ), 0, 0 );
		ewiField = new StatusLabel( "Not Connected" );
		gp.add( ewiField, 1, 0 );
		
		gp.add( new Label( "EWI Patch Exchange (EPX) Connection" ), 0, 1 );
		epxField = new StatusLabel( "Not Connected" );
		gp.add( epxField, 1, 1 );
		
    gp.add( new Label( "Library Location" ), 0, 2 );
    libLocField = new StatusLabel( Prefs.getLibraryLocation() );
    gp.add( libLocField, 1, 2 );
    
		gp.add( new Label( "Patches on Scratchpad" ), 0, 3 );
		scratchpadField = new StatusLabel( "0" );
		gp.add( scratchpadField, 1, 3 );
		
		AnchorPane ap = new AnchorPane();
		ap.getChildren().add( gp );
		this.setContent( ap );
		
	}

}

class StatusLabel extends Label {
  StatusLabel( String initVal ) {
    setText( initVal );
    setId( "status-label" );
  }
  
}