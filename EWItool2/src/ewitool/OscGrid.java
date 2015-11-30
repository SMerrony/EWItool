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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class OscGrid extends GridPane {
  
  Slider sawSlider, triSlider, sqrSlider,
         brAttainSlider, brThreshSlider, brDepthSlider, brCurveSlider,
         fineSlider, beatSlider,
         pwmWidthSlider, pwmFreqSlider, pwmDepthSlider,
         sweepTimeSlider, sweepDepthSlider,
         volSlider;
   ChoiceBox octaveChoice, semitoneChoice;
   CheckBox crossFadeCheck;
   
  OscGrid( int ix ) {
    
    setId( "editor-grid" );
    
    RowConstraints fixedRC, vgrowRC;
    ColumnConstraints fixedCC, medFixedCC, hgrowCC;
    
    fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    fixedCC = new ColumnConstraints();
    fixedCC.setMinWidth( USE_COMPUTED_SIZE );
    fixedCC.setHgrow( Priority.NEVER );
    fixedCC.setPrefWidth( 25.0 );
    medFixedCC = new ColumnConstraints();
    medFixedCC.setHgrow( Priority.NEVER );
    medFixedCC.setPrefWidth( 40.0 );
    hgrowCC = new ColumnConstraints();
    hgrowCC.setPrefWidth( 80.0 );
    //hgrowCC.setHgrow( Priority.ALWAYS );
    
    getRowConstraints().addAll( fixedRC, vgrowRC, vgrowRC, vgrowRC, vgrowRC );
    
    Label mainLabel = new Label( "Oscillator " + (ix + 1) );
    mainLabel.setId( "editor-section-label" );
    GridPane.setColumnSpan( mainLabel, 2 );
    GridPane.setValignment( mainLabel, VPos.TOP );
    add( mainLabel, 0, 0 );
    getColumnConstraints().add( medFixedCC );  // #0
    getColumnConstraints().add( hgrowCC );  // #1
    
    add( new Label( "Saw" ), 2, 0 );
    getColumnConstraints().add( fixedCC );  // #2
    add( new Label( "Tri" ), 3, 0 );
    getColumnConstraints().add( fixedCC );  // #3
    add( new Label( "Sqr" ), 4, 0 );
    getColumnConstraints().add( fixedCC );  // #4
    getColumnConstraints().add( medFixedCC );  // #5
    add( new Label( "PWM" ), 6, 1 );
    getColumnConstraints().add( hgrowCC );  // #6
    Label sweepLabel = new Label( "Sweep" );
    sweepLabel.setId( "editor-group-label" );
    GridPane.setHalignment( sweepLabel, HPos.CENTER );
    add( sweepLabel, 7, 0 );
    getColumnConstraints().add( hgrowCC );  // #7
    getColumnConstraints().add( medFixedCC );  // #8
    add( new Label( "Breath" ), 9, 0 );
    getColumnConstraints().add( hgrowCC );  // #9
    add( new Label( "Vol" ), 10, 0 );
    getColumnConstraints().add( fixedCC );  // #10
    
    add( new Label( "Octave" ), 0, 1 );
    octaveChoice = new ChoiceBox();
    add( octaveChoice, 0, 2 );
    
    add( new Label( "Fine" ), 1, 1 );
    fineSlider = new Slider( 0.0, 127.0, 0.0 );
    fineSlider.setOrientation( Orientation.HORIZONTAL );
    fineSlider.setMajorTickUnit( 32.0 );
    fineSlider.setShowTickMarks( true );
    add( fineSlider, 1, 2 );
  
    add( new Label( "Semitone" ), 0, 3 );
    semitoneChoice = new ChoiceBox();
    add( semitoneChoice, 0, 4 );
    
    add( new Label( "Beat" ), 1, 3 );
    beatSlider = new Slider( 0.0, 127.0, 0.0 );
    beatSlider.setOrientation( Orientation.HORIZONTAL );
    beatSlider.setMajorTickUnit( 32.0 );
    beatSlider.setShowTickMarks( true );
    add( beatSlider, 1, 4 );
    
    sawSlider = new Slider( 0.0, 127.0, 0.0 );
    sawSlider.setOrientation( Orientation.VERTICAL );
    sawSlider.setMajorTickUnit( 32.0 );
    sawSlider.setShowTickMarks( true );
    GridPane.setRowSpan( sawSlider, 4 );
    add( sawSlider, 2, 1 );
    
    triSlider = new Slider( 0.0, 127.0, 0.0 );
    triSlider.setOrientation( Orientation.VERTICAL );
    triSlider.setMajorTickUnit( 32.0 );
    triSlider.setShowTickMarks( true );
    GridPane.setRowSpan( triSlider, 4 );
    add( triSlider, 3, 1 );
    
    sqrSlider = new Slider( 0.0, 127.0, 0.0 );
    sqrSlider.setOrientation( Orientation.VERTICAL );
    sqrSlider.setMajorTickUnit( 32.0 );
    sqrSlider.setShowTickMarks( true );
    GridPane.setRowSpan( sqrSlider, 4 );
    add( sqrSlider, 4, 1 );
    
    if (ix == 1) {
      crossFadeCheck = new CheckBox( "Cross-fade" );
      add( crossFadeCheck, 6, 0 );
    }
   
    add( new Label( "Width" ), 5, 2 );
    pwmWidthSlider = new Slider( 0.0, 127.0, 0.0 );
    pwmWidthSlider.setOrientation( Orientation.HORIZONTAL );
    pwmWidthSlider.setMajorTickUnit( 32.0 );
    pwmWidthSlider.setShowTickMarks( true );
    add( pwmWidthSlider, 6, 2 );
    
    add( new Label( "Freq" ), 5, 3 );
    pwmFreqSlider = new Slider( 0.0, 127.0, 0.0 );
    pwmFreqSlider.setOrientation( Orientation.HORIZONTAL );
    pwmFreqSlider.setMajorTickUnit( 32.0 );
    pwmFreqSlider.setShowTickMarks( true );
    add( pwmFreqSlider, 6, 3 );  
    
    add( new Label( "Depth" ), 5, 4 );
    pwmDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    pwmDepthSlider.setOrientation( Orientation.HORIZONTAL );
    pwmDepthSlider.setMajorTickUnit( 32.0 );
    pwmDepthSlider.setShowTickMarks( true );
    add( pwmDepthSlider, 6, 4 );

    
    add( new Label( "Time" ), 7,1 );
    sweepTimeSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepTimeSlider.setOrientation( Orientation.HORIZONTAL );
    sweepTimeSlider.setMajorTickUnit( 32.0 );
    sweepTimeSlider.setShowTickMarks( true );
    add( sweepTimeSlider, 7, 2 );  
    
    add( new Label( "Depth" ), 7, 3 );
    sweepDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepDepthSlider.setOrientation( Orientation.HORIZONTAL );
    sweepDepthSlider.setMajorTickUnit( 32.0 );
    sweepDepthSlider.setShowTickMarks( true );
    add( sweepDepthSlider, 7, 4 );
    
    add( new Label( "Attain" ), 8, 1 );
    brAttainSlider = new Slider( 0.0, 127.0, 0.0 );
    brAttainSlider.setOrientation( Orientation.HORIZONTAL );
    brAttainSlider.setMajorTickUnit( 32.0 );
    brAttainSlider.setShowTickMarks( true );
    add( brAttainSlider, 9, 1 );
    
    add( new Label( "Thresh" ), 8, 2 );
    brThreshSlider = new Slider( 0.0, 127.0, 0.0 );
    brThreshSlider.setOrientation( Orientation.HORIZONTAL );
    brThreshSlider.setMajorTickUnit( 32.0 );
    brThreshSlider.setShowTickMarks( true );
    add( brThreshSlider, 9, 2 );
    
    add( new Label( "Curve" ), 8, 3 );
    brCurveSlider = new Slider( 0.0, 127.0, 0.0 );
    brCurveSlider.setOrientation( Orientation.HORIZONTAL );
    brCurveSlider.setMajorTickUnit( 32.0 );
    brCurveSlider.setShowTickMarks( true );
    add( brCurveSlider, 9, 3 );  
    
    add( new Label( "Depth" ), 8, 4 );
    brDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    brDepthSlider.setOrientation( Orientation.HORIZONTAL );
    brDepthSlider.setMajorTickUnit( 32.0 );
    brDepthSlider.setShowTickMarks( true );
    add( brDepthSlider, 9, 4 );
    
    volSlider = new Slider( 0.0, 127.0, 0.0 );
    volSlider.setOrientation( Orientation.VERTICAL );
    volSlider.setMajorTickUnit( 32.0 );
    volSlider.setShowTickMarks( true );
    GridPane.setRowSpan( volSlider, 4 );
    add( volSlider, 10, 1 );
    
  }
  
}