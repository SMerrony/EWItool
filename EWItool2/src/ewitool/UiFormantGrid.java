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

import javafx.geometry.VPos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * @author steve
 *
 */
public class UiFormantGrid extends GridPane {
  
  ChoiceBox<String> formantChoice;
  
  UiFormantGrid() {
    
    setId( "editor-grid" );
    
    Label mainLabel = new Label( "Formant Filter" );
    mainLabel.setId( "editor-section-label" );
    GridPane.setValignment( mainLabel, VPos.TOP );
    add( mainLabel, 0, 0 );

    formantChoice = new ChoiceBox<String>();
    formantChoice.getItems().addAll( "Off", "Woodwind", "Strings" );
    add( formantChoice, 0, 1 );
    
  }

}
