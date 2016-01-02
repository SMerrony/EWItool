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
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class PatchEditorTab extends Tab {
  
  enum Osc { OSC1, OSC2 }
  enum Filter { OSC_PRI, OSC_SEC, NOISE_PRI, NOISE_SEC }
  public ComboBox<String> patchesCombo;
  private EWI4000sPatch editPatch;
    
  PatchEditorTab(SharedData sharedData, MidiHandler midiHandler) {
    setText( "Patch Editor" );
    setClosable( false );
    
    editPatch = new EWI4000sPatch();
      
    patchesCombo = new ComboBox<String>();
    HBox headerBox = new HBox();
    Region lSpaceRegion = new Region(), rSpaceRegion = new Region();
    HBox.setHgrow( lSpaceRegion, Priority.ALWAYS );
    HBox.setHgrow( rSpaceRegion, Priority.ALWAYS );
    headerBox.getChildren().addAll( lSpaceRegion, patchesCombo, rSpaceRegion );
    
    UiOscGrid osc1Grid = new UiOscGrid( editPatch, midiHandler, Osc.OSC1 );
    UiOscGrid osc2Grid = new UiOscGrid( editPatch, midiHandler, Osc.OSC2 );
    HBox oscBox = new HBox();
    oscBox.getChildren().addAll( osc1Grid, osc2Grid );
    
    UiFormantGrid formantGrid = new UiFormantGrid( editPatch, midiHandler );
    UiKeyTriggerGrid keyTriggerGrid = new UiKeyTriggerGrid( editPatch, midiHandler );
    VBox subVbox = new VBox();
    VBox.setVgrow( formantGrid, Priority.ALWAYS );
    VBox.setVgrow( keyTriggerGrid, Priority.ALWAYS );
    subVbox.getChildren().addAll( formantGrid, keyTriggerGrid );
    subVbox.setMinWidth( 80.0 );
    UiFilterGrid oscPriFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.OSC_PRI );
    UiFilterGrid oscSecFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.OSC_SEC );
    HBox filterBox = new HBox();    
    filterBox.getChildren().addAll( subVbox, oscPriFilterGrid, oscSecFilterGrid );

    UiNoiseGrid noiseGrid = new UiNoiseGrid( editPatch, midiHandler );
    UiFilterGrid noisePriFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.NOISE_PRI );
    UiFilterGrid noiseSecFilterGrid = new UiFilterGrid( editPatch, midiHandler, Filter.NOISE_SEC );
    HBox noiseBox = new HBox();
    noiseBox.getChildren().addAll( noiseGrid, noisePriFilterGrid, noiseSecFilterGrid );
    
    UiChorusGrid chorusGrid = new UiChorusGrid( editPatch, midiHandler);
    UiDelayGrid delayGrid = new UiDelayGrid( editPatch, midiHandler );
    UiReverbGrid reverbGrid = new UiReverbGrid( editPatch, midiHandler );
    UiBiteGrid biteGrid = new UiBiteGrid( editPatch, midiHandler);
    UiPitchBendGrid pitchBendGrid = new UiPitchBendGrid( editPatch, midiHandler);
    UiAntiAliasGrid antiAliasGrid = new UiAntiAliasGrid( editPatch, midiHandler);
    UiLevelsGrid levelsGrid = new UiLevelsGrid( editPatch, midiHandler );
    HBox multiBox = new HBox();
    multiBox.getChildren().addAll( chorusGrid, delayGrid, reverbGrid, biteGrid, 
                                   pitchBendGrid, antiAliasGrid, levelsGrid );

    // all-encompassing VBox
    VBox vBox = new VBox();
    VBox.setVgrow( headerBox, Priority.NEVER );
    VBox.setVgrow( oscBox, Priority.ALWAYS );
    VBox.setVgrow( filterBox, Priority.ALWAYS );
    VBox.setVgrow( noiseBox, Priority.ALWAYS );
    VBox.setVgrow( multiBox, Priority.ALWAYS );
    vBox.getChildren().addAll( headerBox, oscBox, filterBox, noiseBox, multiBox );
    
    setContent( vBox );

    // what to do when the combo is changed...
    patchesCombo.setOnAction( new EventHandler<ActionEvent>() {
      @Override
      public void handle( ActionEvent ae ) {
	if (!patchesCombo.getSelectionModel().isEmpty()) {
	  editPatch = sharedData.ewiPatchList.get( patchesCombo.getSelectionModel().getSelectedIndex() );
	  System.out.println( "DEBUG - Patch editor selection changed" );
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

	  midiHandler.sendPatch( editPatch, EWI4000sPatch.EWI_EDIT ); 

	}
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
