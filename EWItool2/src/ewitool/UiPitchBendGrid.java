/**
 * This file is part of EWItool.
 *
 *  EWItool is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EWItool is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EWItool.  If not, see <http://www.gnu.org/licenses/>.
 */

package ewitool;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * @author steve
 *
 */
public class UiPitchBendGrid extends GridPane {
  
  CheckBox enableCheck;
  ChoiceBox rangeChoice;
  
  UiPitchBendGrid() {
    
    setId( "editor-grid" );
    
    RowConstraints fixedRC, vgrowRC;
    fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    getRowConstraints().addAll( fixedRC, vgrowRC, vgrowRC, vgrowRC );
    
    Label mainLabel = new Label( "Pitch Bend" ); 
    mainLabel.setId( "editor-section-label" );
    GridPane.setValignment( mainLabel, VPos.TOP );
    add( mainLabel, 0, 0 );

    enableCheck = new CheckBox( "Enable" );
    add( enableCheck, 0,1 );
    
    add( new ControlLabel( "Range", HPos.CENTER ), 0, 2 );
    
    rangeChoice = new ChoiceBox();
    add( rangeChoice, 0, 3 );   
  }
}
