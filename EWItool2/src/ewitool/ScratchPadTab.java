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

import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class ScratchPadTab extends Tab {
	
	Button clearButton, deleteButton, renameButton, 
		     viewHexButton, exchangeButton, exportButton;
	ListView patchList;

	ScratchPadTab() {

		setText( "Scratchpad" );
		setClosable( false );
		
		GridPane gp = new GridPane();
		gp.setMaxHeight( Double.MAX_VALUE );

		ColumnConstraints col1 = new ColumnConstraints( 100.0, 200.0, Double.MAX_VALUE );
		col1.setHgrow( Priority.ALWAYS );
		gp.getColumnConstraints().addAll( col1 );
		RowConstraints row1 = new RowConstraints( 100.0, 200.0, Double.MAX_VALUE );
		row1.setVgrow( Priority.ALWAYS );
		gp.getRowConstraints().addAll( row1 );
		
		patchList = new ListView();
    patchList.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
		gp.add( patchList, 0, 0, 1, 2 );
		
		clearButton = new Button( "Clear" );
		clearButton.setMinWidth( 150.0 );
		gp.add( clearButton, 1, 0 );
		GridPane.setValignment( clearButton, VPos.CENTER );
		
		deleteButton = new Button( "Delete" );
		deleteButton.setMinWidth( 150.0 );
		gp.add( deleteButton, 2, 0 );
		
		renameButton = new Button( "Rename" );
		renameButton.setMinWidth( 150.0 );
	  gp.add( renameButton, 3, 0 );	
	  
	  viewHexButton = new Button( "View in Hex" );
	  viewHexButton.setMinWidth( 150.0 );
	  gp.add( viewHexButton, 1, 1 );
	  
	  exchangeButton = new Button( "Prepare to Exchange" );
	  exchangeButton.setMinWidth( 150.0 );
	  gp.add( exchangeButton, 2, 1 );
	  
	  exportButton = new Button( "Export" );
	  exportButton.setMinWidth( 150.0 );
	  gp.add( exportButton, 3, 1 );
		
		AnchorPane ap = new AnchorPane();
		ap.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
		ap.getChildren().add( gp );
		AnchorPane.setLeftAnchor( gp, 30.0 );
		AnchorPane.setRightAnchor( gp, 30.0 );
		AnchorPane.setTopAnchor( gp, 30.0 );
		AnchorPane.setBottomAnchor( gp, 30.0 );
		setContent( ap );

	}
}
