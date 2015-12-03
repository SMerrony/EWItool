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

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class UiFilterGrid extends GridPane {

  ChoiceBox routingChoice, typeChoice;
  Slider qSlider, keyFollowSlider,
         cutoffFreqSlider,
         brModSlider, brCurveSlider,
         lfoFreqSlider, lfoBreathSlider, lfoDepthSlider, lfoThresholdSlider,
         sweepTimeSlider, sweepDepthSlider;
  
  UiFilterGrid( int ix, String title ) {
    
    setId( "editor-grid" );
    
    RowConstraints fixedRC, vgrowRC;
    ColumnConstraints fixedCC, medFixedCC, hgrowCC;
    
    fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    getRowConstraints().addAll( fixedRC, vgrowRC, vgrowRC, vgrowRC, vgrowRC );
    
    Label mainLabel = new Label( title );
    mainLabel.setId( "editor-section-label" );
    GridPane.setColumnSpan( mainLabel, 2 );
    add( mainLabel, 0, 0 );
    
    add( new GroupLabel( "Breath" ), 2, 0 );
    
    GroupLabel lfoLabel = new GroupLabel( "LFO" );
    add( lfoLabel, 3, 0 );
    GridPane.setColumnSpan( lfoLabel, 2 );
    
    add( new GroupLabel( "Sweep"), 5, 0 );
    
    if (ix == 0) {
      routingChoice = new ChoiceBox();
      add( routingChoice, 0, 1 );
    }
    
    add( new ControlLabel( "Q", HPos.CENTER ), 1, 1 );
    add( new ControlLabel( "Mod", HPos.CENTER ), 2, 1 );
    add( new ControlLabel( "Freq", HPos.CENTER ), 3, 1 );
    add( new ControlLabel( "Breath", HPos.CENTER ), 4, 1 );
    add( new ControlLabel( "Time", HPos.CENTER ), 5, 1 );
    
    typeChoice = new ChoiceBox();
    add( typeChoice, 0, 2 );
    
    qSlider = new Slider( 0.0, 127.0, 0.0 );
    qSlider.setOrientation( Orientation.HORIZONTAL );
    qSlider.setMajorTickUnit( 32.0 );
    add( qSlider, 1, 2 );
   
    brModSlider = new Slider( 0.0, 127.0, 0.0 );
    brModSlider.setOrientation( Orientation.HORIZONTAL );
    brModSlider.setMajorTickUnit( 32.0 );
    add( brModSlider, 2, 2 );

    lfoFreqSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoFreqSlider.setOrientation( Orientation.HORIZONTAL );
    lfoFreqSlider.setMajorTickUnit( 32.0 );
    add( lfoFreqSlider, 3, 2 );

    lfoBreathSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoBreathSlider.setOrientation( Orientation.HORIZONTAL );
    lfoBreathSlider.setMajorTickUnit( 32.0 );
    add( lfoBreathSlider, 4, 2 );

    sweepTimeSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepTimeSlider.setOrientation( Orientation.HORIZONTAL );
    sweepTimeSlider.setMajorTickUnit( 32.0 );
    add( sweepTimeSlider, 5, 2 );
    
    add( new ControlLabel( "Key Follow", HPos.CENTER ), 0, 3 );
    add( new ControlLabel( "Cutoff Freq", HPos.CENTER ), 1, 3 );
    add( new ControlLabel( "Curve", HPos.CENTER ), 2, 3 );
    add( new ControlLabel( "Depth", HPos.CENTER ), 3, 3 );
    add( new ControlLabel( "Threshold", HPos.CENTER ), 4, 3 );
    add( new ControlLabel( "Depth", HPos.CENTER ), 5, 3 );
    
    keyFollowSlider = new Slider( 0.0, 127.0, 0.0 );
    keyFollowSlider.setOrientation( Orientation.HORIZONTAL );
    keyFollowSlider.setMajorTickUnit( 32.0 );
    add( keyFollowSlider, 0, 4 );   
    
    cutoffFreqSlider = new Slider( 0.0, 127.0, 0.0 );
    cutoffFreqSlider.setOrientation( Orientation.HORIZONTAL );
    cutoffFreqSlider.setMajorTickUnit( 32.0 );
    add( cutoffFreqSlider, 1, 4 );
    
    brCurveSlider = new Slider( 0.0, 127.0, 0.0 );
    brCurveSlider.setOrientation( Orientation.HORIZONTAL );
    brCurveSlider.setMajorTickUnit( 32.0 );
    add( brCurveSlider, 2, 4 );
    
    lfoDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoDepthSlider.setOrientation( Orientation.HORIZONTAL );
    lfoDepthSlider.setMajorTickUnit( 32.0 );
    add( lfoDepthSlider, 3, 4 );
    
    lfoThresholdSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoThresholdSlider.setOrientation( Orientation.HORIZONTAL );
    lfoThresholdSlider.setMajorTickUnit( 32.0 );
    add( lfoThresholdSlider, 4, 4 );
    
    sweepDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepDepthSlider.setOrientation( Orientation.HORIZONTAL );
    sweepDepthSlider.setMajorTickUnit( 32.0 );
    add( sweepDepthSlider, 5, 4 );
    
  }
}
