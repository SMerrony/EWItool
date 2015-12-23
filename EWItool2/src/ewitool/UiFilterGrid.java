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

import ewitool.PatchEditorTab.Filter;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
//import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class UiFilterGrid extends GridPane {

  ChoiceBox<String> routingChoice, typeChoice;
  Slider qSlider, keyFollowSlider,
         cutoffFreqSlider,
         brModSlider, brCurveSlider,
         lfoFreqSlider, lfoBreathSlider, lfoDepthSlider, lfoThresholdSlider,
         sweepTimeSlider, sweepDepthSlider;
  
  UiFilterGrid( Filter filter ) {
    
    setId( "editor-grid" );
    
    RowConstraints fixedRC, vgrowRC;
    //ColumnConstraints fixedCC, medFixedCC, hgrowCC;
    
    fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    getRowConstraints().addAll( fixedRC, vgrowRC, vgrowRC, vgrowRC, vgrowRC );
    
    Label mainLabel = new Label();
    switch( filter ) {
    case NOISE_PRI:
      mainLabel.setText( "Primary Noise Filter" );
      break;
    case NOISE_SEC:
      mainLabel.setText( "Secondary Noise Filter" );
      break;
    case OSC_PRI:
      mainLabel.setText( "Primary Osc Filter" );
      break;
    case OSC_SEC:
      mainLabel.setText( "Secondary Osc Filter" );
      break;  
    }
    mainLabel.setId( "editor-section-label" );
    GridPane.setColumnSpan( mainLabel, 2 );
    add( mainLabel, 0, 0 );
    
    add( new GroupLabel( "Breath" ), 2, 0 );
    
    GroupLabel lfoLabel = new GroupLabel( "LFO" );
    add( lfoLabel, 3, 0 );
    GridPane.setColumnSpan( lfoLabel, 2 );
    
    add( new GroupLabel( "Sweep"), 5, 0 );
    
    if (filter == Filter.NOISE_PRI || filter == Filter.OSC_PRI) {
      routingChoice = new ChoiceBox<String>();
      routingChoice.getItems().addAll( "1-only", "Linked", "Both" );
      add( routingChoice, 0, 1 );
    }
    
    typeChoice = new ChoiceBox<String>();
    typeChoice.getItems().addAll( "Low Pass", "High Pass", "Band Pass", "Notch", "Off" );
    add( typeChoice, 0, 2 );
    
    qSlider = new Slider( 5.0, 127.0, 0.0 );          // Val: 5-127
    qSlider.setOrientation( Orientation.HORIZONTAL );
    qSlider.setMajorTickUnit( 32.0 );
    add( qSlider, 1, 2 );
    add( new BoundRightControlLabel( "Q", HPos.CENTER, qSlider ), 1, 1 );
   
    brModSlider = new Slider( 0.0, 127.0, 0.0 );
    brModSlider.setOrientation( Orientation.HORIZONTAL );
    brModSlider.setMajorTickUnit( 32.0 );
    add( brModSlider, 2, 2 );
    add( new BoundRightControlLabel( "Mod", HPos.CENTER, brModSlider ), 2, 1 );
    
    lfoFreqSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoFreqSlider.setOrientation( Orientation.HORIZONTAL );
    lfoFreqSlider.setMajorTickUnit( 32.0 );
    add( lfoFreqSlider, 3, 2 );
    add( new BoundRightControlLabel( "Freq", HPos.CENTER, lfoFreqSlider ), 3, 1 );

    lfoBreathSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoBreathSlider.setOrientation( Orientation.HORIZONTAL );
    lfoBreathSlider.setMajorTickUnit( 32.0 );
    add( lfoBreathSlider, 4, 2 );
    add( new BoundRightControlLabel( "Breath", HPos.CENTER, lfoBreathSlider ), 4, 1 );

    sweepTimeSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepTimeSlider.setOrientation( Orientation.HORIZONTAL );
    sweepTimeSlider.setMajorTickUnit( 32.0 );
    add( sweepTimeSlider, 5, 2 );
    add( new BoundRightControlLabel( "Time", HPos.CENTER, sweepTimeSlider ), 5, 1 );
    
    keyFollowSlider = new Slider( 52.0, 88.0, 0.0 );          // Val. 52-88
    keyFollowSlider.setOrientation( Orientation.HORIZONTAL );
    keyFollowSlider.setMajorTickUnit( 32.0 );
    add( keyFollowSlider, 0, 4 );   
    add( new BoundRightControlLabel( "Key Follow", HPos.CENTER, keyFollowSlider ), 0, 3 );
    
    cutoffFreqSlider = new Slider( 0.0, 127.0, 0.0 );
    cutoffFreqSlider.setOrientation( Orientation.HORIZONTAL );
    cutoffFreqSlider.setMajorTickUnit( 32.0 );
    add( cutoffFreqSlider, 1, 4 );
    add( new BoundRightControlLabel( "Cutoff Freq", HPos.CENTER, cutoffFreqSlider ), 1, 3 );
    
    brCurveSlider = new Slider( 0.0, 127.0, 0.0 );
    brCurveSlider.setOrientation( Orientation.HORIZONTAL );
    brCurveSlider.setMajorTickUnit( 32.0 );
    add( brCurveSlider, 2, 4 );
    add( new BoundRightControlLabel( "Curve", HPos.CENTER, brCurveSlider ), 2, 3 );
    
    lfoDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoDepthSlider.setOrientation( Orientation.HORIZONTAL );
    lfoDepthSlider.setMajorTickUnit( 32.0 );
    add( lfoDepthSlider, 3, 4 );
    add( new BoundRightControlLabel( "Depth", HPos.CENTER, lfoDepthSlider ), 3, 3 );
    
    lfoThresholdSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoThresholdSlider.setOrientation( Orientation.HORIZONTAL );
    lfoThresholdSlider.setMajorTickUnit( 32.0 );
    add( lfoThresholdSlider, 4, 4 );
    add( new BoundRightControlLabel( "Threshold", HPos.CENTER, lfoThresholdSlider ), 4, 3 );
    
    sweepDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepDepthSlider.setOrientation( Orientation.HORIZONTAL );
    sweepDepthSlider.setMajorTickUnit( 32.0 );
    add( sweepDepthSlider, 5, 4 );
    add( new BoundRightControlLabel( "Depth", HPos.CENTER, sweepDepthSlider ), 5, 3 );
    
  }
}
