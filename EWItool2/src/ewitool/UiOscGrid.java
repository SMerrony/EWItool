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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class UiOscGrid extends GridPane {
  
  Slider sawSlider, triSlider, sqrSlider,
         brAttainSlider, brThreshSlider, brDepthSlider, brCurveSlider,
         fineSlider, beatSlider,
         pwmWidthSlider, pwmFreqSlider, pwmDepthSlider,
         sweepTimeSlider, sweepDepthSlider,
         volSlider;
   ChoiceBox octaveChoice, semitoneChoice;
   CheckBox crossFadeCheck; 
   
  UiOscGrid( int ix ) {
    
    setId( "editor-grid" );
    
    RowConstraints fixedRC, vgrowRC;
    ColumnConstraints fixedCC, medFixedCC, hgrowCC;
    
    fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    getRowConstraints().addAll( fixedRC, vgrowRC, vgrowRC, vgrowRC, vgrowRC );
    
    Label mainLabel = new Label( "Oscillator " + (ix + 1) );
    mainLabel.setId( "editor-section-label" );
    GridPane.setColumnSpan( mainLabel, 2 );
    add( mainLabel, 0, 0 );

    add( new GroupLabel( "PWM" ), 6, 1 );
    add( new GroupLabel( "Sweep" ), 7, 0 );
    add( new GroupLabel( "Breath" ), 9, 0 );
    
    add( new ControlLabel( "Octave", HPos.CENTER ), 0, 1 );
    octaveChoice = new ChoiceBox();
    add( octaveChoice, 0, 2 );
    
    fineSlider = new Slider( 0.0, 127.0, 0.0 );
    fineSlider.setOrientation( Orientation.HORIZONTAL );
    fineSlider.setMajorTickUnit( 32.0 );
    add( fineSlider, 1, 2 );
    add( new BoundRightControlLabel( "Fine", HPos.CENTER, fineSlider ), 1, 1 );
  
    add( new ControlLabel( "Semitone", HPos.CENTER ), 0, 3 );
    semitoneChoice = new ChoiceBox();
    add( semitoneChoice, 0, 4 );

    beatSlider = new Slider( 0.0, 127.0, 0.0 );
    beatSlider.setOrientation( Orientation.HORIZONTAL );
    beatSlider.setMajorTickUnit( 32.0 );
    add( beatSlider, 1, 4 );
    add( new BoundRightControlLabel( "Beat", HPos.CENTER, beatSlider ), 1, 3 );
    
    sawSlider = new Slider( 0.0, 127.0, 0.0 );
    sawSlider.setOrientation( Orientation.VERTICAL );
    sawSlider.setMajorTickUnit( 32.0 );
    sawSlider.setTooltip( new Tooltip( "Test" ) );
    GridPane.setRowSpan( sawSlider, 4 );
    add( sawSlider, 2, 1 );
    add( new BoundBelowControlLabel( "Saw", HPos.CENTER, sawSlider ), 2, 0 );
    
    triSlider = new Slider( 0.0, 127.0, 0.0 );
    triSlider.setOrientation( Orientation.VERTICAL );
    triSlider.setMajorTickUnit( 32.0 );
    GridPane.setRowSpan( triSlider, 4 );
    add( triSlider, 3, 1 );
    add( new BoundBelowControlLabel( "Tri", HPos.CENTER, triSlider ), 3, 0 );
    
    sqrSlider = new Slider( 0.0, 127.0, 0.0 );
    sqrSlider.setOrientation( Orientation.VERTICAL );
    sqrSlider.setMajorTickUnit( 32.0 );
    GridPane.setRowSpan( sqrSlider, 4 );
    add( sqrSlider, 4, 1 );
    add( new BoundBelowControlLabel( "Sqr", HPos.CENTER, sqrSlider ), 4, 0 );
    
    if (ix == 1) {
      crossFadeCheck = new CheckBox( "Cross-fade" );
      add( crossFadeCheck, 5, 0 );
      GridPane.setColumnSpan( crossFadeCheck, 2 );
    }
     
    pwmWidthSlider = new Slider( 0.0, 127.0, 0.0 );
    pwmWidthSlider.setOrientation( Orientation.HORIZONTAL );
    pwmWidthSlider.setMajorTickUnit( 32.0 );
    add( pwmWidthSlider, 6, 2 );
    add( new BoundBelowControlLabel( "Width", HPos.RIGHT, pwmWidthSlider ), 5, 2 );
    
    pwmFreqSlider = new Slider( 0.0, 127.0, 0.0 );
    pwmFreqSlider.setOrientation( Orientation.HORIZONTAL );
    pwmFreqSlider.setMajorTickUnit( 32.0 );
    add( pwmFreqSlider, 6, 3 );  
    add( new BoundBelowControlLabel( "Freq", HPos.CENTER , pwmFreqSlider ), 5, 3 );
    
    pwmDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    pwmDepthSlider.setOrientation( Orientation.HORIZONTAL );
    pwmDepthSlider.setMajorTickUnit( 32.0 );
    add( pwmDepthSlider, 6, 4 );
    add( new BoundBelowControlLabel( "Depth", HPos.RIGHT, pwmDepthSlider ), 5, 4 );
   
    sweepTimeSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepTimeSlider.setOrientation( Orientation.HORIZONTAL );
    sweepTimeSlider.setMajorTickUnit( 32.0 );
    add( sweepTimeSlider, 7, 2 );  
    add( new BoundRightControlLabel( "Time", HPos.CENTER, sweepTimeSlider ), 7,1 );
    
    sweepDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepDepthSlider.setOrientation( Orientation.HORIZONTAL );
    sweepDepthSlider.setMajorTickUnit( 32.0 );
    add( sweepDepthSlider, 7, 4 );
    add( new BoundRightControlLabel( "Depth", HPos.CENTER, sweepDepthSlider ), 7, 3 );
    
    brAttainSlider = new Slider( 0.0, 127.0, 0.0 );
    brAttainSlider.setOrientation( Orientation.HORIZONTAL );
    brAttainSlider.setMajorTickUnit( 32.0 );
    add( brAttainSlider, 9, 1 );
    add( new BoundBelowControlLabel( "Attain", HPos.CENTER, brAttainSlider ), 8, 1 );
       
    brThreshSlider = new Slider( 0.0, 127.0, 0.0 );
    brThreshSlider.setOrientation( Orientation.HORIZONTAL );
    brThreshSlider.setMajorTickUnit( 32.0 );
    add( brThreshSlider, 9, 2 );
    add( new BoundBelowControlLabel( "Thresh", HPos.CENTER, brThreshSlider ), 8, 2 ); 
    
    brCurveSlider = new Slider( 0.0, 127.0, 0.0 );
    brCurveSlider.setOrientation( Orientation.HORIZONTAL );
    brCurveSlider.setMajorTickUnit( 32.0 );
    add( brCurveSlider, 9, 3 );  
    add( new BoundBelowControlLabel( "Curve", HPos.CENTER, brCurveSlider ), 8, 3 );
        
    brDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    brDepthSlider.setOrientation( Orientation.HORIZONTAL );
    brDepthSlider.setMajorTickUnit( 32.0 );
    add( brDepthSlider, 9, 4 );
    add( new BoundBelowControlLabel( "Depth", HPos.CENTER, brDepthSlider ), 8, 4 );
    
    volSlider = new Slider( 0.0, 127.0, 0.0 );
    volSlider.setOrientation( Orientation.VERTICAL );
    volSlider.setMajorTickUnit( 32.0 );
    GridPane.setRowSpan( volSlider, 4 );
    add( volSlider, 10, 1 );
    add( new BoundBelowControlLabel( "Vol", HPos.CENTER, volSlider ), 10, 0 );
    
  }
}