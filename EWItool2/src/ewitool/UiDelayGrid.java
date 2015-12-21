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
public class UiDelayGrid extends GridPane {
  
  Slider timeSlider, feedbackSlider, dampingSlider, drySlider, volSlider;
  
  UiDelayGrid(SharedData sharedData, MidiHandler midiHandler) {
    
    setId( "editor-grid" );

    RowConstraints fixedRC, vgrowRC;
    fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    getRowConstraints().addAll( fixedRC, vgrowRC, vgrowRC, vgrowRC, vgrowRC );
  
    Label mainLabel = new Label( "Delay" );
    mainLabel.setId( "editor-section-label" );
    add( mainLabel, 0, 0 );
    
    timeSlider = new Slider( 0.0, 127.0, 0.0 );
    timeSlider.setOrientation( Orientation.HORIZONTAL );
    timeSlider.setMajorTickUnit( 32.0 );
    timeSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      midiHandler.sendLiveControl( 0, 113, newVal.intValue() );
      sharedData.editPatch.delayTime = newVal.intValue();
    });
    add( timeSlider, 0, 2 );
    add( new BoundBelowControlLabel( "Time", HPos.CENTER, timeSlider ), 0, 1 );
    
    feedbackSlider = new Slider( 0.0, 127.0, 0.0 );
    feedbackSlider.setOrientation( Orientation.HORIZONTAL );
    feedbackSlider.setMajorTickUnit( 32.0 );
    feedbackSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      midiHandler.sendLiveControl( 1, 113, newVal.intValue() );
      sharedData.editPatch.delayFeedback = newVal.intValue();
    });
    add( feedbackSlider, 1, 2 );
    add( new BoundBelowControlLabel( "Feedback", HPos.CENTER, feedbackSlider ), 1, 1 );
    
    volSlider = new Slider( 0.0, 127.0, 0.0 );
    volSlider.setOrientation( Orientation.VERTICAL );
    volSlider.setMajorTickUnit( 32.0 );
    GridPane.setRowSpan( volSlider, 4 );
    volSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      midiHandler.sendLiveControl( 3, 113, newVal.intValue() );
      sharedData.editPatch.delayLevel = newVal.intValue();
    });
    add( volSlider, 2, 1 );
    add( new BoundBelowControlLabel( "Vol", HPos.CENTER, volSlider ), 2, 0 );
    
    dampingSlider = new Slider( 0.0, 127.0, 0.0 );
    dampingSlider.setOrientation( Orientation.HORIZONTAL );
    dampingSlider.setMajorTickUnit( 32.0 );
    dampingSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      midiHandler.sendLiveControl( 2, 113, newVal.intValue() );
      sharedData.editPatch.delayDamp = newVal.intValue();
    });
    add( dampingSlider, 0, 4 );
    add( new BoundBelowControlLabel( "Damping", HPos.CENTER, dampingSlider ), 0, 3 );
    
    drySlider = new Slider( 0.0, 127.0, 0.0 );
    drySlider.setOrientation( Orientation.HORIZONTAL );
    drySlider.setMajorTickUnit( 32.0 );
    drySlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      midiHandler.sendLiveControl( 4, 113, newVal.intValue() );
      sharedData.editPatch.delayDry = newVal.intValue();
    });
    add( drySlider, 1, 4 );
    add( new BoundBelowControlLabel( "Dry", HPos.CENTER, drySlider ), 1, 3 );
  }
  
  void setControls( SharedData sharedData ) {
    timeSlider.setValue( sharedData.editPatch.delayTime );
    feedbackSlider.setValue( sharedData.editPatch.delayFeedback );
    volSlider.setValue( sharedData.editPatch.delayLevel );
    dampingSlider.setValue( sharedData.editPatch.delayDamp );
    drySlider.setValue( sharedData.editPatch.delayDry );
  }
}
