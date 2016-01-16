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

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class PatchEditorTab extends Tab {

  enum Osc { OSC1, OSC2 }
  enum Filter { OSC_PRI, OSC_SEC, NOISE_PRI, NOISE_SEC }
  public ComboBox<String> patchesCombo;
  
  UiOscGrid osc1Grid, osc2Grid;
  UiFormantGrid formantGrid;
  UiKeyTriggerGrid keyTriggerGrid;
  UiNoiseGrid noiseGrid;
  UiFilterGrid oscPriFilterGrid, oscSecFilterGrid, noisePriFilterGrid, noiseSecFilterGrid;
  UiChorusGrid chorusGrid;
  UiDelayGrid delayGrid;
  UiReverbGrid reverbGrid;
  UiBiteGrid biteGrid;
  UiPitchBendGrid pitchBendGrid;
  UiAntiAliasGrid antiAliasGrid;
  UiLevelsGrid levelsGrid;
  
  private volatile EWI4000sPatch editPatch, uneditedPatch;
  MidiHandler midiHandler;
  SharedData sharedData;
  ScratchPad scratchPad;
 
  PatchEditorTab(SharedData pSharedData, ScratchPad pScratchPad, MidiHandler pMidiHandler) {
    setText( "Patch Editor" );
    setClosable( false );

    sharedData = pSharedData;
    scratchPad = pScratchPad;
    midiHandler = pMidiHandler;
    
    editPatch = new EWI4000sPatch();

    GridPane peGrid = new GridPane();
    ColumnConstraints ccsGrowable = new ColumnConstraints( 40.0, 90.0, Double.MAX_VALUE );
    RowConstraints rcsFixed = new RowConstraints(), rcsGrowable = new RowConstraints();
    ccsGrowable.setHgrow( Priority.ALWAYS );
    rcsFixed.setVgrow( Priority.NEVER );
    rcsGrowable.setVgrow( Priority.ALWAYS );
    for (int col = 0; col < 12; col++) peGrid.getColumnConstraints().add( ccsGrowable );
    peGrid.getRowConstraints().addAll( rcsFixed, rcsGrowable, rcsGrowable, rcsGrowable, rcsGrowable );

    patchesCombo = new ComboBox<String>();

    HBox headerBox = new HBox();
    headerBox.setId( "editor-header-box" );
    Region lSpaceRegion = new Region(), rSpaceRegion = new Region();
    HBox.setHgrow( lSpaceRegion, Priority.ALWAYS );
    HBox.setHgrow( rSpaceRegion, Priority.ALWAYS );
    headerBox.getChildren().addAll( lSpaceRegion, patchesCombo, rSpaceRegion );

    osc1Grid = new UiOscGrid( editPatch, midiHandler, Osc.OSC1 );
    osc2Grid = new UiOscGrid( editPatch, midiHandler, Osc.OSC2 );

    formantGrid = new UiFormantGrid( editPatch, midiHandler );
    keyTriggerGrid = new UiKeyTriggerGrid( editPatch, midiHandler );
    VBox subVbox = new VBox();
    VBox.setVgrow( formantGrid, Priority.ALWAYS );
    VBox.setVgrow( keyTriggerGrid, Priority.ALWAYS );
    subVbox.getChildren().addAll( formantGrid, keyTriggerGrid );
    oscPriFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.OSC_PRI );
    oscSecFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.OSC_SEC );

    noiseGrid = new UiNoiseGrid( editPatch, midiHandler );
    noisePriFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.NOISE_PRI );
    noiseSecFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.NOISE_SEC );

    chorusGrid = new UiChorusGrid( editPatch, midiHandler);
    delayGrid  = new UiDelayGrid( editPatch, midiHandler );
    reverbGrid = new UiReverbGrid( editPatch, midiHandler );
    biteGrid = new UiBiteGrid( editPatch, midiHandler);
    pitchBendGrid = new UiPitchBendGrid( editPatch, midiHandler);
    antiAliasGrid = new UiAntiAliasGrid( editPatch, midiHandler);
    levelsGrid = new UiLevelsGrid( editPatch, midiHandler );

    GridPane.setColumnSpan( headerBox, 12 );
    peGrid.add( headerBox, 0, 0 );

    GridPane.setColumnSpan( osc1Grid, 6 );
    peGrid.add( osc1Grid, 0, 1 );
    GridPane.setColumnSpan( osc2Grid, 6 );
    peGrid.add( osc2Grid, 6, 1 );

    GridPane.setColumnSpan( subVbox, 2 );
    peGrid.add( subVbox, 0, 2 );
    GridPane.setColumnSpan( oscPriFilterGrid, 5 );
    peGrid.add( oscPriFilterGrid, 2, 2 );
    GridPane.setColumnSpan( oscSecFilterGrid, 5 );
    peGrid.add( oscSecFilterGrid, 7, 2 );

    GridPane.setColumnSpan( noiseGrid, 2 );
    peGrid.add( noiseGrid, 0, 3 );
    GridPane.setColumnSpan( noisePriFilterGrid, 5 );
    peGrid.add( noisePriFilterGrid, 2, 3 );
    GridPane.setColumnSpan( noiseSecFilterGrid, 5 );
    peGrid.add( noiseSecFilterGrid, 7, 3 );

    GridPane.setColumnSpan( chorusGrid, 4 );
    peGrid.add( chorusGrid, 0, 4 );
    GridPane.setColumnSpan( delayGrid, 2 );
    peGrid.add( delayGrid, 4, 4 );
    GridPane.setColumnSpan( reverbGrid, 2 );
    peGrid.add( reverbGrid, 6, 4 );
    peGrid.add( biteGrid, 8, 4 );
    peGrid.add( pitchBendGrid, 9, 4 );
    peGrid.add( antiAliasGrid, 10, 4 );
    peGrid.add( levelsGrid, 11, 4 );

    setContent( peGrid );

    // what to do when the combo is changed...
    patchesCombo.setOnAction( (ae) -> {
      if (!patchesCombo.getSelectionModel().isEmpty()) {
        midiHandler.ignoreEvents = true;
        editPatch.patchBlob = sharedData.ewiPatchList.get( patchesCombo.getSelectionModel().getSelectedIndex() ).patchBlob;
        editPatch.decodeBlob();
        uneditedPatch = editPatch;
        Debugger.log( "DEBUG - Patch editor selection changed" );
        setAllControls();
        midiHandler.ignoreEvents = false;
        midiHandler.sendPatch( editPatch, EWI4000sPatch.EWI_EDIT ); 
      }
    });
  }
  
  private void setAllControls() {
    osc1Grid.setControls( editPatch, Osc.OSC1 );
    osc2Grid.setControls( editPatch, Osc.OSC2 );
    formantGrid.setControls( editPatch );
    keyTriggerGrid.setControls( editPatch );
    oscPriFilterGrid.setControls( editPatch, Filter.OSC_PRI );
    oscSecFilterGrid.setControls( editPatch, Filter.OSC_SEC );
    noiseGrid.setControls( editPatch );
    noisePriFilterGrid.setControls( editPatch, Filter.NOISE_PRI );
    noiseSecFilterGrid.setControls( editPatch, Filter.NOISE_SEC );
    chorusGrid.setControls( editPatch );
    delayGrid.setControls( editPatch );
    reverbGrid.setControls( editPatch );
    biteGrid.setControls( editPatch );
    pitchBendGrid.setControls( editPatch );
    antiAliasGrid.setControls( editPatch );
    levelsGrid.setControls( editPatch );

  }

  public void populateCombo( SharedData sharedData ) {
    patchesCombo.getItems().clear();
    for (int p = 0; p < EWI4000sPatch.EWI_NUM_PATCHES; p++) {
      patchesCombo.getItems().add( ((p + 1) % 100) + " - " + sharedData.ewiPatchList.get( p ).getName() );
    }
  }
  
  // actions invoked by the main menu...
  
  public void store() {
    editPatch.encodeBlob();
    midiHandler.sendPatch( editPatch, EWI4000sPatch.EWI_SAVE );
    sharedData.ewiPatchList.set( editPatch.patchNum, editPatch );
    sharedData.setStatusMessage( "Patch #" + (editPatch.patchNum + 1 ) + " stored in EWI" );
  }
  
  public void revert() {
    editPatch = uneditedPatch;
    Event.fireEvent( patchesCombo, new ActionEvent() );
  }
  
  public void copyToScratchPad() {
    scratchPad.addPatch( editPatch );
    sharedData.setStatusMessage( editPatch.getName() + " added to Scratchpad" );
  }
  
  public void makeDry() {
    if (chorusGrid.enableCheck.isSelected()) chorusGrid.enableCheck.fire();
    chorusGrid.drySlider.setValueChanging( true );
    chorusGrid.drySlider.setValue( 127 );
    delayGrid.drySlider.setValueChanging( true );
    delayGrid.drySlider.setValue( 127 );
    delayGrid.volSlider.setValueChanging( true );
    delayGrid.volSlider.setValue( 0 );
    reverbGrid.drySlider.setValueChanging( true );
    reverbGrid.drySlider.setValue( 127 );
    reverbGrid.volSlider.setValueChanging( true );
    reverbGrid.volSlider.setValue( 0 );
  }
  
  public void makeMaxVol() {
    int orig_max = 0;
    double factor = 0.0;

    // find highest level
    orig_max = editPatch.osc1.level;
    if (editPatch.osc2.level > orig_max) orig_max = editPatch.osc2.level;
    if (editPatch.ampLevel > orig_max) orig_max = editPatch.ampLevel;
    if (editPatch.octaveLevel > orig_max) orig_max = editPatch.octaveLevel;

    factor = 127.0 / orig_max;
    
    osc1Grid.volSlider.setValueChanging( true );
    osc1Grid.volSlider.setValue( editPatch.osc1.level * factor );
    osc2Grid.volSlider.setValueChanging( true );
    osc2Grid.volSlider.setValue( editPatch.osc2.level * factor );
    levelsGrid.masterSlider.setValueChanging( true );
    levelsGrid.masterSlider.setValue( editPatch.ampLevel * factor );
    levelsGrid.octaveSlider.setValueChanging( true );
    levelsGrid.octaveSlider.setValue( editPatch.octaveLevel * factor );
  }
  
  public void makeNoNoise() {
    noiseGrid.timeSlider.setValueChanging( true );
    noiseGrid.timeSlider.setValue( 0 );
    noiseGrid.breathSlider.setValueChanging( true );
    noiseGrid.breathSlider.setValue( 0 );
    noiseGrid.volSlider.setValueChanging( true );
    noiseGrid.volSlider.setValue( 0 );
    noisePriFilterGrid.typeChoice.getSelectionModel().select( 4 );  // event does get fired
    noiseSecFilterGrid.typeChoice.getSelectionModel().select( 4 );
  } 
  
  public void randomiseBy10pct() {
    
    /* There is a risk of flooding the EWI if we change everything via CCs,
     * so we change the editPatch object and reset the UI.
     */
    midiHandler.ignoreEvents = true;
    uneditedPatch = editPatch;
    
    editPatch.osc1.fine = randNear( 0, 127, editPatch.osc1.fine );
    editPatch.osc1.beat = randNear( 0, 127, editPatch.osc1.beat );
    editPatch.osc1.sawtooth = randNear( 0, 127, editPatch.osc1.sawtooth );
    editPatch.osc1.triangle = randNear( 0, 127, editPatch.osc1.triangle );
    editPatch.osc1.square = randNear( 0, 127, editPatch.osc1.square );
    editPatch.osc1.pulseWidth = randNear( 0, 127, editPatch.osc1.pulseWidth );
    editPatch.osc1.pwmDepth = randNear( 0, 127, editPatch.osc1.pwmDepth );
    editPatch.osc1.pwmFreq = randNear( 0, 127, editPatch.osc1.pwmFreq );
    editPatch.osc1.sweepDepth = randNear( 0, 127, editPatch.osc1.sweepDepth );
    editPatch.osc1.sweepTime = randNear( 0, 127, editPatch.osc1.sweepTime );
    editPatch.osc1.breathAttain = randNear( 0, 127, editPatch.osc1.breathAttain );
    editPatch.osc1.breathDepth = randNear( 0, 127, editPatch.osc1.breathDepth );
    editPatch.osc1.breathCurve = randNear( 0, 127, editPatch.osc1.breathCurve );
    editPatch.osc1.breathThreshold = randNear( 0, 127, editPatch.osc1.breathThreshold );
    editPatch.osc1.level = randNear( 0, 127, editPatch.osc1.level );

    editPatch.osc2.fine = randNear( 0, 127, editPatch.osc2.fine );
    editPatch.osc2.beat = randNear( 0, 127, editPatch.osc2.beat );
    editPatch.osc2.sawtooth = randNear( 0, 127, editPatch.osc2.sawtooth );
    editPatch.osc2.triangle = randNear( 0, 127, editPatch.osc2.triangle );
    editPatch.osc2.square = randNear( 0, 127, editPatch.osc2.square );
    editPatch.osc2.pulseWidth = randNear( 0, 127, editPatch.osc2.pulseWidth );
    editPatch.osc2.pwmDepth = randNear( 0, 127, editPatch.osc2.pwmDepth );
    editPatch.osc2.pwmFreq = randNear( 0, 127, editPatch.osc2.pwmFreq );
    editPatch.osc2.sweepDepth = randNear( 0, 127, editPatch.osc2.sweepDepth );
    editPatch.osc2.sweepTime = randNear( 0, 127, editPatch.osc2.sweepTime );
    editPatch.osc2.breathAttain = randNear( 0, 127, editPatch.osc2.breathAttain );
    editPatch.osc2.breathDepth = randNear( 0, 127, editPatch.osc2.breathDepth );
    editPatch.osc2.breathCurve = randNear( 0, 127, editPatch.osc2.breathCurve );
    editPatch.osc2.breathThreshold = randNear( 0, 127, editPatch.osc2.breathThreshold );
    editPatch.osc2.level = randNear( 0, 127, editPatch.osc2.level );
    
    editPatch.oscFilter1.breathCurve = randNear( 0, 127, editPatch.oscFilter1.breathCurve );
    editPatch.oscFilter1.breathMod = randNear( 0, 127, editPatch.oscFilter1.breathMod );
    editPatch.oscFilter1.freq = randNear( 0, 127, editPatch.oscFilter1.freq );
    editPatch.oscFilter1.keyFollow = randNear( 0, 127, editPatch.oscFilter1.keyFollow );
    editPatch.oscFilter1.lfoBreath = randNear( 0, 127, editPatch.oscFilter1.lfoBreath );
    editPatch.oscFilter1.lfoDepth = randNear( 0, 127, editPatch.oscFilter1.lfoDepth );
    editPatch.oscFilter1.lfoFreq = randNear( 0, 127, editPatch.oscFilter1.lfoFreq );
    editPatch.oscFilter1.lfoThreshold = randNear( 0, 127, editPatch.oscFilter1.lfoThreshold );
    editPatch.oscFilter1.q = randNear( 5, 127, editPatch.oscFilter1.q );
    editPatch.oscFilter1.sweepDepth = randNear( 0, 127, editPatch.oscFilter1.sweepDepth );
    editPatch.oscFilter1.sweepTime = randNear( 0, 127, editPatch.oscFilter1.sweepTime );

    editPatch.oscFilter2.breathCurve = randNear( 0, 127, editPatch.oscFilter2.breathCurve );
    editPatch.oscFilter2.breathMod = randNear( 0, 127, editPatch.oscFilter2.breathMod );
    editPatch.oscFilter2.freq = randNear( 0, 127, editPatch.oscFilter2.freq );
    editPatch.oscFilter2.keyFollow = randNear( 0, 127, editPatch.oscFilter2.keyFollow );
    editPatch.oscFilter2.lfoBreath = randNear( 0, 127, editPatch.oscFilter2.lfoBreath );
    editPatch.oscFilter2.lfoDepth = randNear( 0, 127, editPatch.oscFilter2.lfoDepth );
    editPatch.oscFilter2.lfoFreq = randNear( 0, 127, editPatch.oscFilter2.lfoFreq );
    editPatch.oscFilter2.lfoThreshold = randNear( 0, 127, editPatch.oscFilter2.lfoThreshold );
    editPatch.oscFilter2.q = randNear( 5, 127, editPatch.oscFilter2.q );
    editPatch.oscFilter2.sweepDepth = randNear( 0, 127, editPatch.oscFilter2.sweepDepth );
    editPatch.oscFilter2.sweepTime = randNear( 0, 127, editPatch.oscFilter2.sweepTime );
    
    editPatch.chorusDelay1 = randNear( 0, 127, editPatch.chorusDelay1 );
    editPatch.chorusModLev1 = randNear( 0, 127, editPatch.chorusModLev1 );
    editPatch.chorusWetLev1 = randNear( 0, 127, editPatch.chorusWetLev1 );
    editPatch.chorusDelay2 = randNear( 0, 127, editPatch.chorusDelay2 );
    editPatch.chorusModLev2 = randNear( 0, 127, editPatch.chorusModLev2 );
    editPatch.chorusWetLev2 = randNear( 0, 127, editPatch.chorusWetLev2 );
    editPatch.chorusDryLevel = randNear( 0, 127, editPatch.chorusDryLevel );
    editPatch.chorusFeedback = randNear( 0, 127, editPatch.chorusFeedback );
    editPatch.chorusLFOfreq = randNear( 0, 127, editPatch.chorusLFOfreq );
    editPatch.delayTime = randNear( 0, 127, editPatch.delayTime );
    editPatch.delayDamp = randNear( 0, 127, editPatch.delayDamp );
    editPatch.delayFeedback = randNear( 0, 127, editPatch.delayFeedback );
    editPatch.delayDry = randNear( 0, 127, editPatch.delayDry );
    editPatch.delayLevel = randNear( 0, 127, editPatch.delayLevel );
    editPatch.reverbDamp = randNear( 0, 127, editPatch.reverbDamp );
    editPatch.reverbDensity = randNear( 0, 127, editPatch.reverbDensity );
    editPatch.reverbDry = randNear( 0, 127, editPatch.reverbDry );
    editPatch.reverbLevel = randNear( 0, 127, editPatch.reverbLevel );
    editPatch.reverbTime = randNear( 0, 127, editPatch.reverbTime );
    
    editPatch.encodeBlob();
    setAllControls();
    midiHandler.ignoreEvents = false;
    midiHandler.sendPatch( editPatch, EWI4000sPatch.EWI_EDIT );    
  }
  
  public void defaultPatch() {
    /* There is a risk of flooding the EWI if we change everything via CCs,
     * so we change the editPatch object and reset the UI.
     */
    midiHandler.ignoreEvents = true;
    uneditedPatch = editPatch;
    // just a plain triangle wave @ 75% volume
    editPatch.osc1.octave = 64;
    editPatch.osc1.semitone = 64;
    editPatch.osc1.fine = 64;
    editPatch.osc1.beat = 64;
    editPatch.osc1.sawtooth = 0;
    editPatch.osc1.triangle = 127;
    editPatch.osc1.square = 0;
    editPatch.osc1.pulseWidth = 64;
    editPatch.osc1.pwmDepth = 0;
    editPatch.osc1.pwmFreq = 0;
    editPatch.osc1.sweepDepth = 64;
    editPatch.osc1.sweepTime = 0;
    editPatch.osc1.breathAttain = 0;
    editPatch.osc1.breathDepth = 0;
    editPatch.osc1.breathCurve = 64;
    editPatch.osc1.breathThreshold = 0;
    editPatch.osc1.level = 96;

    // osc2 silent
    editPatch.osc2Xfade = 0;
    editPatch.osc2.octave = 64;
    editPatch.osc2.semitone = 64;
    editPatch.osc2.fine = 64;
    editPatch.osc2.beat = 64;
    editPatch.osc2.sawtooth = 0;
    editPatch.osc2.triangle = 0;
    editPatch.osc2.square = 0;
    editPatch.osc2.pulseWidth = 64;
    editPatch.osc2.pwmDepth = 0;
    editPatch.osc2.pwmFreq = 0;
    editPatch.osc2.sweepDepth = 64;
    editPatch.osc2.sweepTime = 0;
    editPatch.osc2.breathAttain = 0;
    editPatch.osc2.breathDepth = 0;
    editPatch.osc2.breathCurve = 64;
    editPatch.osc2.breathThreshold = 0;
    editPatch.osc2.level = 0;
    
    // no filtering
    editPatch.formantFilter = 0;
    editPatch.keyTrigger = 1;
    editPatch.oscFilterLink = 0; // ?
    
    editPatch.oscFilter1.mode = 4; // off
    editPatch.oscFilter1.breathCurve = 64;
    editPatch.oscFilter1.breathMod = 0;
    editPatch.oscFilter1.freq = 0;
    editPatch.oscFilter1.keyFollow = 64;
    editPatch.oscFilter1.lfoBreath = 64;
    editPatch.oscFilter1.lfoDepth = 0;
    editPatch.oscFilter1.lfoFreq = 0;
    editPatch.oscFilter1.lfoThreshold = 0;
    editPatch.oscFilter1.q = 5;
    editPatch.oscFilter1.sweepDepth = 64;
    editPatch.oscFilter1.sweepTime = 0;

    editPatch.oscFilter2.mode = 4; // off
    editPatch.oscFilter2.breathCurve = 64;
    editPatch.oscFilter2.breathMod = 0;
    editPatch.oscFilter2.freq = 0;
    editPatch.oscFilter2.keyFollow = 64;
    editPatch.oscFilter2.lfoBreath = 64;
    editPatch.oscFilter2.lfoDepth = 0;
    editPatch.oscFilter2.lfoFreq = 0;
    editPatch.oscFilter2.lfoThreshold = 0;
    editPatch.oscFilter2.q = 5;
    editPatch.oscFilter2.sweepDepth = 64;
    editPatch.oscFilter2.sweepTime = 0;
  
    editPatch.noiseBreath = 0;
    editPatch.noiseLevel = 0;
    editPatch.noiseTime = 0;
    
    editPatch.noiseFilter1.mode = 4; // off
    editPatch.noiseFilter1.breathCurve = 64;
    editPatch.noiseFilter1.breathMod = 0;
    editPatch.noiseFilter1.freq = 0;
    editPatch.noiseFilter1.keyFollow = 64;
    editPatch.noiseFilter1.lfoBreath = 64;
    editPatch.noiseFilter1.lfoDepth = 0;
    editPatch.noiseFilter1.lfoFreq = 0;
    editPatch.noiseFilter1.lfoThreshold = 0;
    editPatch.noiseFilter1.q = 5;
    editPatch.noiseFilter1.sweepDepth = 64;
    editPatch.noiseFilter1.sweepTime = 0;

    editPatch.noiseFilter2.mode = 4; // off
    editPatch.noiseFilter2.breathCurve = 64;
    editPatch.noiseFilter2.breathMod = 0;
    editPatch.noiseFilter2.freq = 0;
    editPatch.noiseFilter2.keyFollow = 64;
    editPatch.noiseFilter2.lfoBreath = 64;
    editPatch.noiseFilter2.lfoDepth = 0;
    editPatch.noiseFilter2.lfoFreq = 0;
    editPatch.noiseFilter1.lfoThreshold = 0;
    editPatch.noiseFilter2.q = 5;
    editPatch.noiseFilter2.sweepDepth = 64;
    editPatch.noiseFilter2.sweepTime = 0;
    
    editPatch.chorusSwitch = 0;
    editPatch.chorusDelay1 = 0;
    editPatch.chorusModLev1 = 0;
    editPatch.chorusWetLev1 = 0;
    editPatch.chorusDelay2 = 0;
    editPatch.chorusModLev2 = 0;
    editPatch.chorusWetLev2 = 0;
    editPatch.chorusDryLevel = 127;
    editPatch.chorusFeedback = 0;
    editPatch.chorusLFOfreq = 0;
    editPatch.delayTime = 0;
    editPatch.delayDamp = 0;
    editPatch.delayFeedback = 0;
    editPatch.delayDry = 127;
    editPatch.delayLevel = 0;
    editPatch.reverbDamp = 0;
    editPatch.reverbDensity = 0;
    editPatch.reverbDry = 127;
    editPatch.reverbLevel = 0;
    editPatch.reverbTime = 0;
    
    editPatch.biteTremolo = 0;
    editPatch.biteVibrato = 127;
    
    editPatch.ampLevel = 96; // 75%
    editPatch.octaveLevel = 36; // seems to be default
    
    editPatch.encodeBlob();
    setAllControls();
    midiHandler.ignoreEvents = false;
    midiHandler.sendPatch( editPatch, EWI4000sPatch.EWI_EDIT );    
  }
  
  public void randomPatch() {
    /* There is a risk of flooding the EWI if we change everything via CCs,
     * so we change the editPatch object and reset the UI.
     */
    midiHandler.ignoreEvents = true;
    uneditedPatch = editPatch;
    
    editPatch.osc1.octave = 64;
    editPatch.osc1.semitone = 64;

    editPatch.osc1.fine = randBetween( 0, 127 );
    editPatch.osc1.beat = randBetween( 0, 127 );
    editPatch.osc1.sawtooth = randBetween( 0, 127 );
    editPatch.osc1.triangle = randBetween( 0, 127 );
    editPatch.osc1.square = randBetween( 0, 127 );
    editPatch.osc1.pulseWidth = randBetween( 0, 127 );
    editPatch.osc1.pwmDepth = randBetween( 0, 127 );
    editPatch.osc1.pwmFreq = randBetween( 0, 127 );
    editPatch.osc1.sweepDepth = randBetween( 0, 127 );
    editPatch.osc1.sweepTime = randBetween( 0, 127 );
    editPatch.osc1.breathAttain = randBetween( 0, 127 );
    editPatch.osc1.breathDepth = randBetween( 0, 127 );
    editPatch.osc1.breathCurve = randBetween( 0, 127 );
    editPatch.osc1.breathThreshold = randBetween( 0, 127 );
    editPatch.osc1.level = randBetween( 48, 127 );

    editPatch.osc2Xfade = randBetween( 0, 1 );
    
    editPatch.osc1.octave = randBetween( 62, 66 );
    editPatch.osc1.semitone = 64;  // keep sane for now
    
    editPatch.osc2.fine = randBetween( 0, 127 );
    editPatch.osc2.beat = randBetween( 0, 127 );
    editPatch.osc2.sawtooth = randBetween( 0, 127 );
    editPatch.osc2.triangle = randBetween( 0, 127 );
    editPatch.osc2.square = randBetween( 0, 127 );
    editPatch.osc2.pulseWidth = randBetween( 0, 127 );
    editPatch.osc2.pwmDepth = randBetween( 0, 127 );
    editPatch.osc2.pwmFreq = randBetween( 0, 127 );
    editPatch.osc2.sweepDepth = randBetween( 0, 127 );
    editPatch.osc2.sweepTime = randBetween( 0, 127 );
    editPatch.osc2.breathAttain = randBetween( 0, 127 );
    editPatch.osc2.breathDepth = randBetween( 0, 127 );
    editPatch.osc2.breathCurve = randBetween( 0, 127 );
    editPatch.osc2.breathThreshold = randBetween( 0, 127 );
    editPatch.osc2.level = randBetween( 0, 127 );
    
    editPatch.formantFilter = randBetween( 0, 2 );
    editPatch.keyTrigger = randBetween( 0, 1 );
    editPatch.oscFilterLink = randBetween( 0, 2 );
    
    editPatch.oscFilter1.mode = randBetween( 0, 4 );
    editPatch.oscFilter1.breathCurve = randBetween( 0, 127 );
    editPatch.oscFilter1.breathMod = randBetween( 0, 127 );
    editPatch.oscFilter1.freq = randBetween( 0, 127 );
    editPatch.oscFilter1.keyFollow = randBetween( 0, 127 );
    editPatch.oscFilter1.lfoBreath = randBetween( 0, 127 );
    editPatch.oscFilter1.lfoDepth = randBetween( 0, 127 );
    editPatch.oscFilter1.lfoFreq = randBetween( 0, 127 );
    editPatch.oscFilter1.lfoThreshold = randBetween( 0, 127 );
    editPatch.oscFilter1.q = randBetween( 5, 127 );
    editPatch.oscFilter1.sweepDepth = randBetween( 0, 127 );
    editPatch.oscFilter1.sweepTime = randBetween( 0, 127 );

    editPatch.oscFilter2.mode = randBetween( 0, 4 );
    editPatch.oscFilter2.breathCurve = randBetween( 0, 127 );
    editPatch.oscFilter2.breathMod = randBetween( 0, 127 );
    editPatch.oscFilter2.freq = randBetween( 0, 127 );
    editPatch.oscFilter2.keyFollow = randBetween( 0, 127 );
    editPatch.oscFilter2.lfoBreath = randBetween( 0, 127 );
    editPatch.oscFilter2.lfoDepth = randBetween( 0, 127 );
    editPatch.oscFilter2.lfoFreq = randBetween( 0, 127 );
    editPatch.oscFilter2.lfoThreshold = randBetween( 0, 127 );
    editPatch.oscFilter2.q = randBetween( 5, 127 );
    editPatch.oscFilter2.sweepDepth = randBetween( 0, 127 );
    editPatch.oscFilter2.sweepTime = randBetween( 0, 127 );
    
    editPatch.noiseBreath = randBetween( 0, 127 );
    editPatch.noiseLevel = randBetween( 0, 127 );
    editPatch.noiseTime = randBetween( 0, 127 );
    
    // TODO add noise filters
    
    editPatch.chorusSwitch = randBetween( 0, 1 );
    editPatch.chorusDelay1 = randBetween( 0, 127 );
    editPatch.chorusModLev1 = randBetween( 0, 127 );
    editPatch.chorusWetLev1 = randBetween( 0, 127 );
    editPatch.chorusDelay2 = randBetween( 0, 127 );
    editPatch.chorusModLev2 = randBetween( 0, 127 );
    editPatch.chorusWetLev2 = randBetween( 0, 127 );
    editPatch.chorusDryLevel = randBetween( 0, 127 );
    editPatch.chorusFeedback = randBetween( 0, 127 );
    editPatch.chorusLFOfreq = randBetween( 0, 127 );
    editPatch.delayTime = randBetween( 0, 127 );
    editPatch.delayDamp = randBetween( 0, 127 );
    editPatch.delayFeedback = randBetween( 0, 127 );
    editPatch.delayDry = randBetween( 64, 127 );
    editPatch.delayLevel = randBetween( 0, 127 );
    editPatch.reverbDamp = randBetween( 0, 127 );
    editPatch.reverbDensity = randBetween( 0, 127 );
    editPatch.reverbDry = randBetween( 64, 127 );
    editPatch.reverbLevel = randBetween( 0, 12 );
    editPatch.reverbTime = randBetween( 0, 127 );
    
    editPatch.encodeBlob();
    setAllControls();
    midiHandler.ignoreEvents = false;
    midiHandler.sendPatch( editPatch, EWI4000sPatch.EWI_EDIT );     
  }
  
  /*
   * return a psuedorandom integer between min and max
   */
  private int randBetween( int min, int max ) {
    return min + (int)((max - min) * Math.random());
  }
  
  /*
   * return current value psuedorandomly modified by up to 10% within specified bounds
   */
  private int randNear( int min, int max, int currval ) {
    int newmin = currval - (max/10); 
    if (newmin < min) newmin = min;
    if (newmin == max) newmin -= max/10;  
    
    int newmax = currval + (max/10);
    if (newmax > max) newmax = max;
    if (newmax == min) newmax += max/10;
    
    return randBetween( newmin, newmax );
  }
}

// helper classes used for (dynamically) labelling the controls
class ControlLabel extends Label {
  ControlLabel( String lab, HPos pos ) {
    setText( lab );
    setId( "editor-control-label" );
    GridPane.setHalignment( this, pos );
  }
}
class BoundRightControlLabel extends Label {
  BoundRightControlLabel( String lab, HPos pos, Slider sl ) {
    setId( "editor-control-label" );
    setTextAlignment( TextAlignment.CENTER );
    GridPane.setHalignment( this, pos );
    textProperty().bind( Bindings.format( lab + " %.0f", sl.valueProperty()) );
  }
}
class BoundBelowControlLabel extends Label {
  BoundBelowControlLabel( String lab, HPos pos, Slider sl ) {
    setId( "editor-control-label" );
    setTextAlignment( TextAlignment.CENTER );
    GridPane.setHalignment( this, pos );
    textProperty().bind( Bindings.format( lab + "%n%.0f", sl.valueProperty()) );
  }
}
class GroupLabel extends Label {
  GroupLabel( String lab ) {
    setText( lab );
    setId( "editor-group-label" );
    GridPane.setHalignment( this, HPos.CENTER );
  }
}
