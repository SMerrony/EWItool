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
public class UiBiteGrid extends GridPane {
  
  Slider vibratoSlider, tremoloSlider;
  
  UiBiteGrid(SharedData sharedData, MidiHandler midiHandler) {
    
    setId( "editor-grid" );

    RowConstraints fixedRC, vgrowRC;
    fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    getRowConstraints().addAll( fixedRC, vgrowRC, vgrowRC, vgrowRC, vgrowRC );

    Label mainLabel = new Label( "Bite" );
    mainLabel.setId( "editor-section-label" );
    add( mainLabel, 0, 0 );
      
    vibratoSlider = new Slider( 0.0, 127.0, 0.0 );
    vibratoSlider.setOrientation( Orientation.HORIZONTAL );
    vibratoSlider.setMajorTickUnit( 32.0 );
    vibratoSlider.valueProperty().addListener( (observable, oldVal, newVal)-> {
      midiHandler.sendLiveControl( 2, 81, newVal.intValue() );
      sharedData.editPatch.biteVibrato = newVal.intValue();
    });
    add( vibratoSlider, 0, 2 );
    add( new BoundBelowControlLabel( "Vibrato", HPos.CENTER, vibratoSlider ), 0, 1 );

    
    tremoloSlider = new Slider( 0.0, 127.0, 0.0 );
    tremoloSlider.setOrientation( Orientation.HORIZONTAL );
    tremoloSlider.setMajorTickUnit( 32.0 );
    tremoloSlider.valueProperty().addListener( (observable, oldVal, newVal)-> {
      midiHandler.sendLiveControl( 0, 88, newVal.intValue() );
      sharedData.editPatch.biteTremolo = newVal.intValue();
    });
    add( tremoloSlider, 0, 4 );
    add( new BoundBelowControlLabel( "Tremolo", HPos.CENTER, tremoloSlider ), 0, 3 );
    
  }
  
  void setControls( SharedData sharedData ) {
    vibratoSlider.setValue( sharedData.editPatch.biteVibrato );
    tremoloSlider.setValue(  sharedData.editPatch.biteTremolo );
  }
}
