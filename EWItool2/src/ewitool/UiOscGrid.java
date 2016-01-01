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

import ewitool.PatchEditorTab.Osc;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class UiOscGrid extends GridPane {
  
  Slider fineSlider, beatSlider,
  	 sawSlider, triSlider, sqrSlider,
  	 pwmWidthSlider, pwmFreqSlider, pwmDepthSlider,
         sweepTimeSlider, sweepDepthSlider,
         brAttainSlider, brThreshSlider, brDepthSlider, brCurveSlider,
         volSlider;
   ChoiceBox<Integer> octaveChoice, semitoneChoice;
   CheckBox crossFadeCheck; 
   
  UiOscGrid( EWI4000sPatch editPatch, MidiHandler midiHandler, Osc osc ) {
    
    setId( "editor-grid" );
    
    RowConstraints fixedRC, vgrowRC;
    
    fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    getRowConstraints().addAll( fixedRC, vgrowRC, vgrowRC, vgrowRC, vgrowRC );
    
    Label mainLabel = new Label( );
    switch( osc ) {
    case OSC1:
      mainLabel.setText( "Oscillator 1" );
      break;
    case OSC2:
      mainLabel.setText( "Oscillator 2" );
      break;
    }
    mainLabel.setId( "editor-section-label" );
    GridPane.setColumnSpan( mainLabel, 2 );
    add( mainLabel, 0, 0 );

    add( new GroupLabel( "PWM" ), 6, 1 );
    add( new GroupLabel( "Sweep" ), 7, 0 );
    add( new GroupLabel( "Breath" ), 9, 0 );
    
    add( new ControlLabel( "Octave", HPos.CENTER ), 0, 1 );
    octaveChoice = new ChoiceBox<Integer>();
    octaveChoice.getItems().addAll( 2, 1, 0, -1, -2 );
    octaveChoice.setOnAction( (event) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 0, 64, octaveChoice.getSelectionModel().getSelectedIndex() + 62);
	editPatch.osc1.octave = octaveChoice.getSelectionModel().getSelectedIndex() + 62; 
	break;
      case OSC2:
	midiHandler.sendLiveControl( 0, 65, octaveChoice.getSelectionModel().getSelectedIndex() + 62);
	editPatch.osc2.octave = octaveChoice.getSelectionModel().getSelectedIndex() + 62; 
	break;
      }
    });
    add( octaveChoice, 0, 2 );
    
    fineSlider = new Slider( 14.0, 114.0, 64.0 );  // Val is 14-114
    fineSlider.setOrientation( Orientation.HORIZONTAL );
    fineSlider.setMajorTickUnit( 32.0 );
    fineSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 2, 64, newVal.intValue() );
	editPatch.osc1.fine = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 2, 65, newVal.intValue() );
	editPatch.osc2.fine = newVal.intValue();
	break;
      }
    });
    add( fineSlider, 1, 2 );
    add( new BoundRightControlLabel( "Fine", HPos.CENTER, fineSlider ), 1, 1 );
  
    add( new ControlLabel( "Semitone", HPos.CENTER ), 0, 3 );
    semitoneChoice = new ChoiceBox<Integer>();
    for (int s = 12; s > -13; s--) semitoneChoice.getItems().add( s );
    semitoneChoice.setOnAction( (event) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 1, 64, octaveChoice.getSelectionModel().getSelectedIndex() + 52);
	editPatch.osc1.semitone = octaveChoice.getSelectionModel().getSelectedIndex() + 52; 
	break;
      case OSC2:
	midiHandler.sendLiveControl( 1, 65, octaveChoice.getSelectionModel().getSelectedIndex() + 52);
	editPatch.osc2.semitone = octaveChoice.getSelectionModel().getSelectedIndex() + 52; 
	break;
      }
    });
    add( semitoneChoice, 0, 4 );

    beatSlider = new Slider( 0.0, 127.0, 0.0 );
    beatSlider.setOrientation( Orientation.HORIZONTAL );
    beatSlider.setMajorTickUnit( 32.0 );
    beatSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 3, 64, newVal.intValue() );
	editPatch.osc1.beat = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 3, 65, newVal.intValue() );
	editPatch.osc2.beat = newVal.intValue();
	break;
      }
    });
    add( beatSlider, 1, 4 );
    add( new BoundRightControlLabel( "Beat", HPos.CENTER, beatSlider ), 1, 3 );
    
    sawSlider = new Slider( 0.0, 127.0, 0.0 );
    sawSlider.setOrientation( Orientation.VERTICAL );
    sawSlider.setMajorTickUnit( 32.0 );
    sawSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 5, 64, newVal.intValue() );
	editPatch.osc1.sawtooth = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 5, 65, newVal.intValue() );
	editPatch.osc2.sawtooth = newVal.intValue();
	break;
      }
    });
    GridPane.setRowSpan( sawSlider, 4 );
    add( sawSlider, 2, 1 );
    add( new BoundBelowControlLabel( "Saw", HPos.CENTER, sawSlider ), 2, 0 );
    
    triSlider = new Slider( 0.0, 127.0, 0.0 );
    triSlider.setOrientation( Orientation.VERTICAL );
    triSlider.setMajorTickUnit( 32.0 );
    GridPane.setRowSpan( triSlider, 4 );
    triSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 6, 64, newVal.intValue() );
	editPatch.osc1.triangle = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 6, 65, newVal.intValue() );
	editPatch.osc2.triangle = newVal.intValue();
	break;
      }
    });
    add( triSlider, 3, 1 );
    add( new BoundBelowControlLabel( "Tri", HPos.CENTER, triSlider ), 3, 0 );
    
    sqrSlider = new Slider( 0.0, 127.0, 0.0 );
    sqrSlider.setOrientation( Orientation.VERTICAL );
    sqrSlider.setMajorTickUnit( 32.0 );
    GridPane.setRowSpan( sqrSlider, 4 );
    sqrSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 7, 64, newVal.intValue() );
	editPatch.osc1.square = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 7, 65, newVal.intValue() );
	editPatch.osc2.square = newVal.intValue();
	break;
      }
    });
    add( sqrSlider, 4, 1 );
    add( new BoundBelowControlLabel( "Sqr", HPos.CENTER, sqrSlider ), 4, 0 );
    
    if (osc == Osc.OSC2) {
      crossFadeCheck = new CheckBox( "Cross-fade" );
      crossFadeCheck.setOnAction( (event) -> {
	if (crossFadeCheck.isSelected()) {
	  midiHandler.sendLiveControl( 6, 81, 1 );
	  editPatch.osc2Xfade = 1;
	} else {
	  midiHandler.sendLiveControl( 6, 81, 0 );
	  editPatch.osc2Xfade = 0;
	}
      });
      add( crossFadeCheck, 5, 0 );
      GridPane.setColumnSpan( crossFadeCheck, 2 );
    }
     
    pwmWidthSlider = new Slider( 0.0, 127.0, 0.0 );
    pwmWidthSlider.setOrientation( Orientation.HORIZONTAL );
    pwmWidthSlider.setMajorTickUnit( 32.0 );
    pwmWidthSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 8, 64, newVal.intValue() );
	editPatch.osc1.pulseWidth = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 8, 65, newVal.intValue() );
	editPatch.osc2.pulseWidth = newVal.intValue();
	break;
      }
    });
    add( pwmWidthSlider, 6, 2 );
    add( new BoundBelowControlLabel( "Width", HPos.RIGHT, pwmWidthSlider ), 5, 2 );
    
    pwmFreqSlider = new Slider( 0.0, 127.0, 0.0 );
    pwmFreqSlider.setOrientation( Orientation.HORIZONTAL );
    pwmFreqSlider.setMajorTickUnit( 32.0 );
    pwmFreqSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 9, 64, newVal.intValue() );
	editPatch.osc1.pwmFreq = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 9, 65, newVal.intValue() );
	editPatch.osc2.pwmFreq = newVal.intValue();
	break;
      }
    });
    add( pwmFreqSlider, 6, 3 );  
    add( new BoundBelowControlLabel( "Freq", HPos.CENTER , pwmFreqSlider ), 5, 3 );
    
    pwmDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    pwmDepthSlider.setOrientation( Orientation.HORIZONTAL );
    pwmDepthSlider.setMajorTickUnit( 32.0 );
    pwmDepthSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 10, 64, newVal.intValue() );
	editPatch.osc1.pwmDepth = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 10, 65, newVal.intValue() );
	editPatch.osc2.pwmDepth = newVal.intValue();
	break;
      }
    });
    add( pwmDepthSlider, 6, 4 );
    add( new BoundBelowControlLabel( "Depth", HPos.RIGHT, pwmDepthSlider ), 5, 4 );
   
    sweepTimeSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepTimeSlider.setOrientation( Orientation.HORIZONTAL );
    sweepTimeSlider.setMajorTickUnit( 32.0 );
    sweepTimeSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 12, 64, newVal.intValue() );
	editPatch.osc1.sweepTime = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 12, 65, newVal.intValue() );
	editPatch.osc2.sweepTime = newVal.intValue();
	break;
      }
    });
    add( sweepTimeSlider, 7, 2 );  
    add( new BoundRightControlLabel( "Time", HPos.CENTER, sweepTimeSlider ), 7,1 );
    
    sweepDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepDepthSlider.setOrientation( Orientation.HORIZONTAL );
    sweepDepthSlider.setMajorTickUnit( 32.0 );
    sweepDepthSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 11, 64, newVal.intValue() );
	editPatch.osc1.sweepDepth = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 11, 65, newVal.intValue() );
	editPatch.osc2.sweepDepth = newVal.intValue();
	break;
      }
    });
    add( sweepDepthSlider, 7, 4 );
    add( new BoundRightControlLabel( "Depth", HPos.CENTER, sweepDepthSlider ), 7, 3 );
    
    brAttainSlider = new Slider( 0.0, 127.0, 0.0 );
    brAttainSlider.setOrientation( Orientation.HORIZONTAL );
    brAttainSlider.setMajorTickUnit( 32.0 );
    brAttainSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 14, 64, newVal.intValue() );
	editPatch.osc1.breathAttain = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 14, 65, newVal.intValue() );
	editPatch.osc2.breathAttain = newVal.intValue();
	break;
      }
    });
    add( brAttainSlider, 9, 1 );
    add( new BoundBelowControlLabel( "Attain", HPos.CENTER, brAttainSlider ), 8, 1 );
       
    brThreshSlider = new Slider( 0.0, 127.0, 0.0 );
    brThreshSlider.setOrientation( Orientation.HORIZONTAL );
    brThreshSlider.setMajorTickUnit( 32.0 );
    brThreshSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 16, 64, newVal.intValue() );
	editPatch.osc1.breathThreshold = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 16, 65, newVal.intValue() );
	editPatch.osc2.breathThreshold = newVal.intValue();
	break;
      }
    });
    add( brThreshSlider, 9, 2 );
    add( new BoundBelowControlLabel( "Thresh", HPos.CENTER, brThreshSlider ), 8, 2 ); 
    
    brCurveSlider = new Slider( 0.0, 127.0, 0.0 );
    brCurveSlider.setOrientation( Orientation.HORIZONTAL );
    brCurveSlider.setMajorTickUnit( 32.0 );
    brCurveSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 15, 64, newVal.intValue() );
	editPatch.osc1.breathCurve = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 15, 65, newVal.intValue() );
	editPatch.osc2.breathCurve = newVal.intValue();
	break;
      }
    });
    add( brCurveSlider, 9, 3 );  
    add( new BoundBelowControlLabel( "Curve", HPos.CENTER, brCurveSlider ), 8, 3 );
        
    brDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    brDepthSlider.setOrientation( Orientation.HORIZONTAL );
    brDepthSlider.setMajorTickUnit( 32.0 );
    brDepthSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 13, 64, newVal.intValue() );
	editPatch.osc1.breathDepth = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 13, 65, newVal.intValue() );
	editPatch.osc2.breathDepth = newVal.intValue();
	break;
      }
    });
    add( brDepthSlider, 9, 4 );
    add( new BoundBelowControlLabel( "Depth", HPos.CENTER, brDepthSlider ), 8, 4 );
    
    volSlider = new Slider( 0.0, 127.0, 0.0 );
    volSlider.setOrientation( Orientation.VERTICAL );
    volSlider.setMajorTickUnit( 32.0 );
    volSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( osc ) {
      case OSC1:
	midiHandler.sendLiveControl( 17, 64, newVal.intValue() );
	editPatch.osc1.level = newVal.intValue();
	break;
      case OSC2:
	midiHandler.sendLiveControl( 17, 65, newVal.intValue() );
	editPatch.osc2.level = newVal.intValue();
	break;
      }
    });
    GridPane.setRowSpan( volSlider, 4 );
    add( volSlider, 10, 1 );
    add( new BoundBelowControlLabel( "Vol", HPos.CENTER, volSlider ), 10, 0 );
    
  }
  
  void setControls( EWI4000sPatch editPatch, Osc osc ) {
    switch( osc ) {
    case OSC1:
      octaveChoice.getSelectionModel().select( editPatch.osc1.octave - 62 );
      semitoneChoice.getSelectionModel().select( editPatch.osc1.semitone - 52);
      fineSlider.setValue( editPatch.osc1.fine );
      beatSlider.setValue( editPatch.osc1.beat );
      sawSlider.setValue( editPatch.osc1.sawtooth );
      triSlider.setValue( editPatch.osc1.triangle );
      sqrSlider.setValue( editPatch.osc1.square );
      pwmWidthSlider.setValue( editPatch.osc1.pulseWidth );
      pwmFreqSlider.setValue( editPatch.osc1.pwmFreq );
      pwmDepthSlider.setValue( editPatch.osc1.pwmDepth );
      sweepTimeSlider.setValue( editPatch.osc1.sweepTime );
      sweepDepthSlider.setValue( editPatch.osc1.sweepDepth );
      brAttainSlider.setValue( editPatch.osc1.breathAttain );
      brThreshSlider.setValue( editPatch.osc1.breathThreshold );
      brDepthSlider.setValue( editPatch.osc1.breathDepth );
      brCurveSlider.setValue( editPatch.osc1.breathCurve );
      volSlider.setValue( editPatch.osc1.level );
      break;
    case OSC2:
      octaveChoice.getSelectionModel().select( editPatch.osc2.octave - 62 );
      semitoneChoice.getSelectionModel().select( editPatch.osc2.semitone - 52 );
      fineSlider.setValue( editPatch.osc2.fine );
      beatSlider.setValue( editPatch.osc2.beat );
      sawSlider.setValue( editPatch.osc2.sawtooth );
      triSlider.setValue( editPatch.osc2.triangle );
      sqrSlider.setValue( editPatch.osc2.square );
      pwmWidthSlider.setValue( editPatch.osc2.pulseWidth );
      pwmFreqSlider.setValue( editPatch.osc2.pwmFreq );
      pwmDepthSlider.setValue( editPatch.osc2.pwmDepth );
      sweepTimeSlider.setValue( editPatch.osc2.sweepTime );
      sweepDepthSlider.setValue( editPatch.osc2.sweepDepth );
      brAttainSlider.setValue( editPatch.osc2.breathAttain );
      brThreshSlider.setValue( editPatch.osc2.breathThreshold );
      brDepthSlider.setValue( editPatch.osc2.breathDepth );
      brCurveSlider.setValue( editPatch.osc2.breathCurve );
      volSlider.setValue( editPatch.osc2.level );
      crossFadeCheck.setSelected( editPatch.osc2Xfade == 1 );
      break;

    }
  }
}