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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
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
  public Button storeButton, revertButton, toScratchPadButton;
  private EWI4000sPatch editPatch, uneditedPatch;

  PatchEditorTab(SharedData sharedData, ScratchPad scratchPad, MidiHandler midiHandler) {
    setText( "Patch Editor" );
    setClosable( false );

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
    storeButton = new Button( "Store" );
    storeButton.setTooltip( new Tooltip( "Replace the patch in the EWI with the current version" ) );
    revertButton = new Button( "Revert" );
    revertButton.setTooltip( new Tooltip( "Revert any edits and reload the original patch" ) );
    revertButton.setOnAction( (event) -> {
      editPatch = uneditedPatch;
      Event.fireEvent( patchesCombo, new ActionEvent() );
    });
    toScratchPadButton = new Button( "Copy to Scratchpad" );
    toScratchPadButton.setTooltip( new Tooltip( "Copy current state of patch to the ScratchPad" ) );
    toScratchPadButton.setOnAction( (event) -> {
      scratchPad.addPatch( editPatch );
    });
    HBox headerBox = new HBox();
    headerBox.setId( "editor-header-box" );
    Region lSpaceRegion = new Region(), rSpaceRegion = new Region();
    HBox.setHgrow( lSpaceRegion, Priority.ALWAYS );
    HBox.setHgrow( rSpaceRegion, Priority.ALWAYS );
    headerBox.getChildren().addAll( lSpaceRegion, patchesCombo, storeButton, revertButton, toScratchPadButton, rSpaceRegion );

    UiOscGrid osc1Grid = new UiOscGrid( editPatch, midiHandler, Osc.OSC1 );
    UiOscGrid osc2Grid = new UiOscGrid( editPatch, midiHandler, Osc.OSC2 );

    UiFormantGrid formantGrid = new UiFormantGrid( editPatch, midiHandler );
    UiKeyTriggerGrid keyTriggerGrid = new UiKeyTriggerGrid( editPatch, midiHandler );
    VBox subVbox = new VBox();
    VBox.setVgrow( formantGrid, Priority.ALWAYS );
    VBox.setVgrow( keyTriggerGrid, Priority.ALWAYS );
    subVbox.getChildren().addAll( formantGrid, keyTriggerGrid );
    UiFilterGrid oscPriFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.OSC_PRI );
    UiFilterGrid oscSecFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.OSC_SEC );

    UiNoiseGrid noiseGrid = new UiNoiseGrid( editPatch, midiHandler );
    UiFilterGrid noisePriFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.NOISE_PRI );
    UiFilterGrid noiseSecFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.NOISE_SEC );

    UiChorusGrid chorusGrid = new UiChorusGrid( editPatch, midiHandler);
    UiDelayGrid delayGrid = new UiDelayGrid( editPatch, midiHandler );
    UiReverbGrid reverbGrid = new UiReverbGrid( editPatch, midiHandler );
    UiBiteGrid biteGrid = new UiBiteGrid( editPatch, midiHandler);
    UiPitchBendGrid pitchBendGrid = new UiPitchBendGrid( editPatch, midiHandler);
    UiAntiAliasGrid antiAliasGrid = new UiAntiAliasGrid( editPatch, midiHandler);
    UiLevelsGrid levelsGrid = new UiLevelsGrid( editPatch, midiHandler );

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
        editPatch = sharedData.ewiPatchList.get( patchesCombo.getSelectionModel().getSelectedIndex() );
        uneditedPatch = editPatch;
        Debugger.log( "DEBUG - Patch editor selection changed" );
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
        midiHandler.ignoreEvents = false;
        midiHandler.sendPatch( editPatch, EWI4000sPatch.EWI_EDIT ); 
      }
    });
  }

  public void populateCombo( SharedData sharedData ) {
    patchesCombo.getItems().clear();
    for (int p = 0; p < EWI4000sPatch.EWI_NUM_PATCHES; p++) {
      patchesCombo.getItems().add( ((p + 1) % 100) + " - " + sharedData.ewiPatchList.get( p ).getName() );
    }
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
