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
  

  UiFilterGrid( SharedData sharedData, MidiHandler midiHandler, Filter filter ) {
    
    setId( "editor-grid" );
    
    RowConstraints fixedRC, vgrowRC;
    
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
      routingChoice.setOnAction( (event) -> {
	switch( filter ) {
	case NOISE_PRI:
	  midiHandler.sendLiveControl( 4, 81, routingChoice.getSelectionModel().getSelectedIndex());
	  sharedData.editPatch.noiseFilterLink = routingChoice.getSelectionModel().getSelectedIndex(); 
	  break;
	case OSC_PRI:
	  midiHandler.sendLiveControl( 3, 81, routingChoice.getSelectionModel().getSelectedIndex());
	  sharedData.editPatch.oscFilterLink = routingChoice.getSelectionModel().getSelectedIndex(); 
	  break;
	case NOISE_SEC:
	  break;
	case OSC_SEC:
	  break;
	}
      });
      add( routingChoice, 0, 1 );
    }
    
    typeChoice = new ChoiceBox<String>();
    typeChoice.getItems().addAll( "Low Pass", "High Pass", "Band Pass", "Notch", "Off" );
    typeChoice.setOnAction( (event) -> {
      switch( filter ) {
      case NOISE_PRI:
	  midiHandler.sendLiveControl( 0, 74, typeChoice.getSelectionModel().getSelectedIndex());
	  sharedData.editPatch.noiseFilter1.mode = typeChoice.getSelectionModel().getSelectedIndex(); 
	break;
      case NOISE_SEC:
	  midiHandler.sendLiveControl( 0, 75, typeChoice.getSelectionModel().getSelectedIndex());
	  sharedData.editPatch.noiseFilter2.mode = typeChoice.getSelectionModel().getSelectedIndex(); 
	break;
      case OSC_PRI:
	  midiHandler.sendLiveControl( 0, 72, typeChoice.getSelectionModel().getSelectedIndex());
	  sharedData.editPatch.oscFilter1.mode = typeChoice.getSelectionModel().getSelectedIndex(); 
	break;
      case OSC_SEC:
	  midiHandler.sendLiveControl( 0, 73, typeChoice.getSelectionModel().getSelectedIndex());
	  sharedData.editPatch.oscFilter2.mode = typeChoice.getSelectionModel().getSelectedIndex(); 
	break;
      }
    });
    add( typeChoice, 0, 2 );
    
    qSlider = new Slider( 5.0, 127.0, 0.0 );          // Val: 5-127
    qSlider.setOrientation( Orientation.HORIZONTAL );
    qSlider.setMajorTickUnit( 32.0 );
    qSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( filter ) {
      case NOISE_PRI:
	midiHandler.sendLiveControl( 2, 74, newVal.intValue() );
	sharedData.editPatch.noiseFilter1.q = newVal.intValue();
	break;
      case NOISE_SEC:
	midiHandler.sendLiveControl( 2, 75, newVal.intValue() );
	sharedData.editPatch.noiseFilter2.q = newVal.intValue();
	break;
      case OSC_PRI:
	midiHandler.sendLiveControl( 2, 72, newVal.intValue() );
	sharedData.editPatch.oscFilter1.q = newVal.intValue();
	break;
      case OSC_SEC:
	midiHandler.sendLiveControl( 2, 73, newVal.intValue() );
	sharedData.editPatch.oscFilter2.q = newVal.intValue();
	break;
      }
    });
    add( qSlider, 1, 2 );
    add( new BoundRightControlLabel( "Q", HPos.CENTER, qSlider ), 1, 1 );
   
    brModSlider = new Slider( 0.0, 127.0, 0.0 );
    brModSlider.setOrientation( Orientation.HORIZONTAL );
    brModSlider.setMajorTickUnit( 32.0 );
    brModSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( filter ) {
      case NOISE_PRI:
	midiHandler.sendLiveControl( 4, 74, newVal.intValue() );
	sharedData.editPatch.noiseFilter1.breathMod = newVal.intValue();
	break;
      case NOISE_SEC:
	midiHandler.sendLiveControl( 4, 75, newVal.intValue() );
	sharedData.editPatch.noiseFilter2.breathMod = newVal.intValue();
	break;
      case OSC_PRI:
	midiHandler.sendLiveControl( 4, 72, newVal.intValue() );
	sharedData.editPatch.oscFilter1.breathMod = newVal.intValue();
	break;
      case OSC_SEC:
	midiHandler.sendLiveControl( 4, 73, newVal.intValue() );
	sharedData.editPatch.oscFilter2.breathMod = newVal.intValue();
	break;
      }
    });
    add( brModSlider, 2, 2 );
    add( new BoundRightControlLabel( "Mod", HPos.CENTER, brModSlider ), 2, 1 );
    
    lfoFreqSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoFreqSlider.setOrientation( Orientation.HORIZONTAL );
    lfoFreqSlider.setMajorTickUnit( 32.0 );
    lfoFreqSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( filter ) {
      case NOISE_PRI:
	midiHandler.sendLiveControl( 5, 74, newVal.intValue() );
	sharedData.editPatch.noiseFilter1.lfoFreq = newVal.intValue();
	break;
      case NOISE_SEC:
	midiHandler.sendLiveControl( 5, 75, newVal.intValue() );
	sharedData.editPatch.noiseFilter2.lfoFreq = newVal.intValue();
	break;
      case OSC_PRI:
	midiHandler.sendLiveControl( 5, 72, newVal.intValue() );
	sharedData.editPatch.oscFilter1.lfoFreq = newVal.intValue();
	break;
      case OSC_SEC:
	midiHandler.sendLiveControl( 5, 73, newVal.intValue() );
	sharedData.editPatch.oscFilter2.lfoFreq = newVal.intValue();
	break;
      }
    });
    add( lfoFreqSlider, 3, 2 );
    add( new BoundRightControlLabel( "Freq", HPos.CENTER, lfoFreqSlider ), 3, 1 );

    lfoBreathSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoBreathSlider.setOrientation( Orientation.HORIZONTAL );
    lfoBreathSlider.setMajorTickUnit( 32.0 );
    lfoBreathSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( filter ) {
      case NOISE_PRI:
	midiHandler.sendLiveControl( 7, 74, newVal.intValue() );
	sharedData.editPatch.noiseFilter1.lfoBreath = newVal.intValue();
	break;
      case NOISE_SEC:
	midiHandler.sendLiveControl( 7, 75, newVal.intValue() );
	sharedData.editPatch.noiseFilter2.lfoBreath = newVal.intValue();
	break;
      case OSC_PRI:
	midiHandler.sendLiveControl( 7, 72, newVal.intValue() );
	sharedData.editPatch.oscFilter1.lfoBreath = newVal.intValue();
	break;
      case OSC_SEC:
	midiHandler.sendLiveControl( 7, 73, newVal.intValue() );
	sharedData.editPatch.oscFilter2.lfoBreath = newVal.intValue();
	break;
      }
    });
    add( lfoBreathSlider, 4, 2 );
    add( new BoundRightControlLabel( "Breath", HPos.CENTER, lfoBreathSlider ), 4, 1 );

    sweepTimeSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepTimeSlider.setOrientation( Orientation.HORIZONTAL );
    sweepTimeSlider.setMajorTickUnit( 32.0 );
    sweepTimeSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( filter ) {
      case NOISE_PRI:
	midiHandler.sendLiveControl( 10, 74, newVal.intValue() );
	sharedData.editPatch.noiseFilter1.sweepTime = newVal.intValue();
	break;
      case NOISE_SEC:
	midiHandler.sendLiveControl( 10, 75, newVal.intValue() );
	sharedData.editPatch.noiseFilter2.sweepTime = newVal.intValue();
	break;
      case OSC_PRI:
	midiHandler.sendLiveControl( 10, 72, newVal.intValue() );
	sharedData.editPatch.oscFilter1.sweepTime = newVal.intValue();
	break;
      case OSC_SEC:
	midiHandler.sendLiveControl( 10, 73, newVal.intValue() );
	sharedData.editPatch.oscFilter2.sweepTime = newVal.intValue();
	break;
      }
    });
    add( sweepTimeSlider, 5, 2 );
    add( new BoundRightControlLabel( "Time", HPos.CENTER, sweepTimeSlider ), 5, 1 );
    
    keyFollowSlider = new Slider( 52.0, 88.0, 0.0 );          // Val. 52-88
    keyFollowSlider.setOrientation( Orientation.HORIZONTAL );
    keyFollowSlider.setMajorTickUnit( 32.0 );
    keyFollowSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( filter ) {
      case NOISE_PRI:
	midiHandler.sendLiveControl( 3, 74, newVal.intValue() );
	sharedData.editPatch.noiseFilter1.keyFollow = newVal.intValue();
	break;
      case NOISE_SEC:
	midiHandler.sendLiveControl( 3, 75, newVal.intValue() );
	sharedData.editPatch.noiseFilter2.keyFollow = newVal.intValue();
	break;
      case OSC_PRI:
	midiHandler.sendLiveControl( 3, 72, newVal.intValue() );
	sharedData.editPatch.oscFilter1.keyFollow = newVal.intValue();
	break;
      case OSC_SEC:
	midiHandler.sendLiveControl( 3, 73, newVal.intValue() );
	sharedData.editPatch.oscFilter2.keyFollow = newVal.intValue();
	break;
      }
    });
    add( keyFollowSlider, 0, 4 );   
    add( new BoundRightControlLabel( "Key Follow", HPos.CENTER, keyFollowSlider ), 0, 3 );
    
    cutoffFreqSlider = new Slider( 0.0, 127.0, 0.0 );
    cutoffFreqSlider.setOrientation( Orientation.HORIZONTAL );
    cutoffFreqSlider.setMajorTickUnit( 32.0 );
    cutoffFreqSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( filter ) {
      case NOISE_PRI:
	midiHandler.sendLiveControl( 1, 74, newVal.intValue() );
	sharedData.editPatch.noiseFilter1.freq = newVal.intValue();
	break;
      case NOISE_SEC:
	midiHandler.sendLiveControl( 1, 75, newVal.intValue() );
	sharedData.editPatch.noiseFilter2.freq = newVal.intValue();
	break;
      case OSC_PRI:
	midiHandler.sendLiveControl( 1, 72, newVal.intValue() );
	sharedData.editPatch.oscFilter1.freq = newVal.intValue();
	break;
      case OSC_SEC:
	midiHandler.sendLiveControl( 1, 73, newVal.intValue() );
	sharedData.editPatch.oscFilter2.freq = newVal.intValue();
	break;
      }
    });
    add( cutoffFreqSlider, 1, 4 );
    add( new BoundRightControlLabel( "Cutoff Freq", HPos.CENTER, cutoffFreqSlider ), 1, 3 );
    
    brCurveSlider = new Slider( 0.0, 127.0, 0.0 );
    brCurveSlider.setOrientation( Orientation.HORIZONTAL );
    brCurveSlider.setMajorTickUnit( 32.0 );
    brCurveSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( filter ) {
      case NOISE_PRI:
	midiHandler.sendLiveControl( 11, 74, newVal.intValue() );
	sharedData.editPatch.noiseFilter1.breathCurve = newVal.intValue();
	break;
      case NOISE_SEC:
	midiHandler.sendLiveControl( 11, 75, newVal.intValue() );
	sharedData.editPatch.noiseFilter2.breathCurve = newVal.intValue();
	break;
      case OSC_PRI:
	midiHandler.sendLiveControl( 11, 72, newVal.intValue() );
	sharedData.editPatch.oscFilter1.breathCurve = newVal.intValue();
	break;
      case OSC_SEC:
	midiHandler.sendLiveControl( 11, 73, newVal.intValue() );
	sharedData.editPatch.oscFilter2.breathCurve = newVal.intValue();
	break;
      }
    });
    add( brCurveSlider, 2, 4 );
    add( new BoundRightControlLabel( "Curve", HPos.CENTER, brCurveSlider ), 2, 3 );
    
    lfoDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoDepthSlider.setOrientation( Orientation.HORIZONTAL );
    lfoDepthSlider.setMajorTickUnit( 32.0 );
    lfoDepthSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( filter ) {
      case NOISE_PRI:
	midiHandler.sendLiveControl( 6, 74, newVal.intValue() );
	sharedData.editPatch.noiseFilter1.lfoDepth = newVal.intValue();
	break;
      case NOISE_SEC:
	midiHandler.sendLiveControl( 6, 75, newVal.intValue() );
	sharedData.editPatch.noiseFilter2.lfoDepth = newVal.intValue();
	break;
      case OSC_PRI:
	midiHandler.sendLiveControl( 6, 72, newVal.intValue() );
	sharedData.editPatch.oscFilter1.lfoDepth = newVal.intValue();
	break;
      case OSC_SEC:
	midiHandler.sendLiveControl( 6, 73, newVal.intValue() );
	sharedData.editPatch.oscFilter2.lfoDepth = newVal.intValue();
	break;
      }
    });
    add( lfoDepthSlider, 3, 4 );
    add( new BoundRightControlLabel( "Depth", HPos.CENTER, lfoDepthSlider ), 3, 3 );
    
    lfoThresholdSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoThresholdSlider.setOrientation( Orientation.HORIZONTAL );
    lfoThresholdSlider.setMajorTickUnit( 32.0 );
    lfoThresholdSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( filter ) {
      case NOISE_PRI:
	midiHandler.sendLiveControl( 8, 74, newVal.intValue() );
	sharedData.editPatch.noiseFilter1.lfoThreshold = newVal.intValue();
	break;
      case NOISE_SEC:
	midiHandler.sendLiveControl( 8, 75, newVal.intValue() );
	sharedData.editPatch.noiseFilter2.lfoThreshold = newVal.intValue();
	break;
      case OSC_PRI:
	midiHandler.sendLiveControl( 8, 72, newVal.intValue() );
	sharedData.editPatch.oscFilter1.lfoThreshold = newVal.intValue();
	break;
      case OSC_SEC:
	midiHandler.sendLiveControl( 8, 73, newVal.intValue() );
	sharedData.editPatch.oscFilter2.lfoThreshold = newVal.intValue();
	break;
      }
    });
    add( lfoThresholdSlider, 4, 4 );
    add( new BoundRightControlLabel( "Threshold", HPos.CENTER, lfoThresholdSlider ), 4, 3 );
    
    sweepDepthSlider = new Slider( 0.0, 127.0, 0.0 );
    sweepDepthSlider.setOrientation( Orientation.HORIZONTAL );
    sweepDepthSlider.setMajorTickUnit( 32.0 );
    add( sweepDepthSlider, 5, 4 );
    sweepDepthSlider.valueProperty().addListener( (observable, oldVal, newVal) -> {
      switch( filter ) {
      case NOISE_PRI:
	midiHandler.sendLiveControl( 9, 74, newVal.intValue() );
	sharedData.editPatch.noiseFilter1.sweepDepth = newVal.intValue();
	break;
      case NOISE_SEC:
	midiHandler.sendLiveControl( 9, 75, newVal.intValue() );
	sharedData.editPatch.noiseFilter2.sweepDepth = newVal.intValue();
	break;
      case OSC_PRI:
	midiHandler.sendLiveControl( 9, 72, newVal.intValue() );
	sharedData.editPatch.oscFilter1.sweepDepth = newVal.intValue();
	break;
      case OSC_SEC:
	midiHandler.sendLiveControl( 9, 73, newVal.intValue() );
	sharedData.editPatch.oscFilter2.sweepDepth = newVal.intValue();
	break;
      }
    });
    add( new BoundRightControlLabel( "Depth", HPos.CENTER, sweepDepthSlider ), 5, 3 );
    
  }
  
  void setControls( SharedData sharedData, Filter filter ) {
    switch( filter ) {
    case NOISE_PRI:
      routingChoice.getSelectionModel().select( sharedData.editPatch.noiseFilterLink );
      typeChoice.getSelectionModel().select( sharedData.editPatch.noiseFilter1.mode );
      qSlider.setValue( sharedData.editPatch.noiseFilter1.q );
      brModSlider.setValue( sharedData.editPatch.noiseFilter1.breathMod );
      lfoFreqSlider.setValue( sharedData.editPatch.noiseFilter1.lfoFreq );
      lfoBreathSlider.setValue( sharedData.editPatch.noiseFilter1.lfoBreath );
      sweepTimeSlider.setValue( sharedData.editPatch.noiseFilter1.sweepTime );
      keyFollowSlider.setValue( sharedData.editPatch.noiseFilter1.keyFollow );
      cutoffFreqSlider.setValue( sharedData.editPatch.noiseFilter1.freq );
      brCurveSlider.setValue( sharedData.editPatch.noiseFilter1.breathCurve );
      lfoDepthSlider.setValue( sharedData.editPatch.noiseFilter1.lfoDepth );
      lfoThresholdSlider.setValue( sharedData.editPatch.noiseFilter1.lfoThreshold );
      sweepDepthSlider.setValue( sharedData.editPatch.noiseFilter1.sweepDepth );
      break;
    case NOISE_SEC:
      typeChoice.getSelectionModel().select( sharedData.editPatch.noiseFilter2.mode );
      qSlider.setValue( sharedData.editPatch.noiseFilter2.q );
      brModSlider.setValue( sharedData.editPatch.noiseFilter2.breathMod );
      lfoFreqSlider.setValue( sharedData.editPatch.noiseFilter2.lfoFreq );
      lfoBreathSlider.setValue( sharedData.editPatch.noiseFilter2.lfoBreath );
      sweepTimeSlider.setValue( sharedData.editPatch.noiseFilter2.sweepTime );
      keyFollowSlider.setValue( sharedData.editPatch.noiseFilter2.keyFollow );
      cutoffFreqSlider.setValue( sharedData.editPatch.noiseFilter2.freq );
      brCurveSlider.setValue( sharedData.editPatch.noiseFilter2.breathCurve );
      lfoDepthSlider.setValue( sharedData.editPatch.noiseFilter2.lfoDepth );
      lfoThresholdSlider.setValue( sharedData.editPatch.noiseFilter2.lfoThreshold );
      sweepDepthSlider.setValue( sharedData.editPatch.noiseFilter2.sweepDepth );
      break;
    case OSC_PRI:
      routingChoice.getSelectionModel().select( sharedData.editPatch.oscFilterLink );
      typeChoice.getSelectionModel().select( sharedData.editPatch.oscFilter1.mode );
      qSlider.setValue( sharedData.editPatch.oscFilter1.q );
      brModSlider.setValue( sharedData.editPatch.oscFilter1.breathMod );
      lfoFreqSlider.setValue( sharedData.editPatch.oscFilter1.lfoFreq );
      lfoBreathSlider.setValue( sharedData.editPatch.oscFilter1.lfoBreath );
      sweepTimeSlider.setValue( sharedData.editPatch.oscFilter1.sweepTime );
      keyFollowSlider.setValue( sharedData.editPatch.oscFilter1.keyFollow );
      cutoffFreqSlider.setValue( sharedData.editPatch.oscFilter1.freq );
      brCurveSlider.setValue( sharedData.editPatch.oscFilter1.breathCurve );
      lfoDepthSlider.setValue( sharedData.editPatch.oscFilter1.lfoDepth );
      lfoThresholdSlider.setValue( sharedData.editPatch.oscFilter1.lfoThreshold );
      sweepDepthSlider.setValue( sharedData.editPatch.oscFilter1.sweepDepth );
      break;
    case OSC_SEC:
      typeChoice.getSelectionModel().select( sharedData.editPatch.oscFilter2.mode );
      qSlider.setValue( sharedData.editPatch.oscFilter2.q );
      brModSlider.setValue( sharedData.editPatch.oscFilter2.breathMod );
      lfoFreqSlider.setValue( sharedData.editPatch.oscFilter2.lfoFreq );
      lfoBreathSlider.setValue( sharedData.editPatch.oscFilter2.lfoBreath );
      sweepTimeSlider.setValue( sharedData.editPatch.oscFilter2.sweepTime );
      keyFollowSlider.setValue( sharedData.editPatch.oscFilter2.keyFollow );
      cutoffFreqSlider.setValue( sharedData.editPatch.oscFilter2.freq );
      brCurveSlider.setValue( sharedData.editPatch.oscFilter2.breathCurve );
      lfoDepthSlider.setValue( sharedData.editPatch.oscFilter2.lfoDepth );
      lfoThresholdSlider.setValue( sharedData.editPatch.oscFilter2.lfoThreshold );
      sweepDepthSlider.setValue( sharedData.editPatch.oscFilter2.sweepDepth );
      break;
    }
    
  }
}
