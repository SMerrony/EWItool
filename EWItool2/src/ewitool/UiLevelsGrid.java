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
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * @author steve
 *
 */
public class UiLevelsGrid extends GridPane {
  
  Slider masterSlider, octaveSlider;
  
  UiLevelsGrid(EWI4000sPatch editPatch, MidiHandler midiHandler) {
    
    setId( "editor-grid" );
    
    RowConstraints fixedRC, vgrowRC;
    fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    getRowConstraints().addAll( fixedRC, vgrowRC, vgrowRC, vgrowRC, vgrowRC );
    Label mainLabel = new Label( "Levels" );
    mainLabel.setId( "editor-section-label" );
    add( mainLabel, 0, 0 );
   
    masterSlider = new Slider( 0.0, 127.0, 0.0 );
    masterSlider.setOrientation( Orientation.VERTICAL );
    masterSlider.setMajorTickUnit( 32.0 );
    GridPane.setRowSpan( masterSlider, 3 );
    masterSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      midiHandler.sendLiveControl( 1, 88, newVal.intValue() );
      editPatch.ampLevel = newVal.intValue();
    });
    add( masterSlider, 0, 2 );
    add( new BoundBelowControlLabel( "Master", HPos.CENTER, masterSlider ), 0, 1 );
    
    octaveSlider = new Slider( 0.0, 127.0, 0.0 );
    octaveSlider.setOrientation( Orientation.VERTICAL );
    octaveSlider.setMajorTickUnit( 32.0 );
    octaveSlider.valueProperty().addListener( (observable, oldVal, newVal)-> {
      midiHandler.sendLiveControl( 2, 88, newVal.intValue() );
      editPatch.octaveLevel = newVal.intValue();
    });
    GridPane.setRowSpan( octaveSlider, 3 );
    add( octaveSlider, 1, 2 );
    add( new BoundBelowControlLabel( "Octave", HPos.CENTER, octaveSlider ), 1, 1 );
  }
  
  void setControls( EWI4000sPatch editPatch ) {
    masterSlider.setValue( editPatch.ampLevel );
    octaveSlider.setValue( editPatch.octaveLevel );
  }
}
