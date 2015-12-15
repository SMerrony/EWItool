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
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class PatchEditorTab extends Tab {
    
  PatchEditorTab(SharedData sharedData) {
    setText( "Patch Editor" );
    setClosable( false );
      
    
    HBox headerBox = new HBox();
    
    UiOscGrid osc1Grid = new UiOscGrid( 0 );
    UiOscGrid osc2Grid = new UiOscGrid( 1 );
    HBox oscBox = new HBox();
    oscBox.getChildren().addAll( osc1Grid, osc2Grid );
    
    UiFormantGrid formantGrid = new UiFormantGrid();
    UiKeyTriggerGrid keyTriggerGrid = new UiKeyTriggerGrid();
    VBox subVbox = new VBox();
    VBox.setVgrow( formantGrid, Priority.ALWAYS );
    VBox.setVgrow( keyTriggerGrid, Priority.ALWAYS );
    subVbox.getChildren().addAll( formantGrid, keyTriggerGrid );
    subVbox.setMinWidth( 80.0 );
    UiFilterGrid oscPriFilterGrid = new UiFilterGrid( 0, "Osc Primary Filter" );
    UiFilterGrid oscSecFilterGrid = new UiFilterGrid( 1, "Osc Secondary Filter" );
    HBox filterBox = new HBox();    
    filterBox.getChildren().addAll( subVbox, oscPriFilterGrid, oscSecFilterGrid );

    UiNoiseGrid noiseGrid = new UiNoiseGrid();
    UiFilterGrid noisePriFilterGrid = new UiFilterGrid( 0, "Noise Primary Filter" );
    UiFilterGrid noiseSecFilterGrid = new UiFilterGrid( 1, "Noise Secondary Filter" );
    HBox noiseBox = new HBox();
    noiseBox.getChildren().addAll( noiseGrid, noisePriFilterGrid, noiseSecFilterGrid );
    
    UiChorusGrid chorusGrid = new UiChorusGrid();
    UiDelayGrid delayGrid = new UiDelayGrid();
    UiReverbGrid reverbGrid = new UiReverbGrid();
    UiBiteGrid biteGrid = new UiBiteGrid();
    UiPitchBendGrid pitchBendGrid = new UiPitchBendGrid();
    UiAntiAliasGrid antiAliasGrid = new UiAntiAliasGrid();
    UiLevelsGrid levelsGrid = new UiLevelsGrid();
    HBox multiBox = new HBox();
    multiBox.getChildren().addAll( chorusGrid, delayGrid, reverbGrid, biteGrid, 
                                   pitchBendGrid, antiAliasGrid, levelsGrid );

    // all-encompassing VBox
    VBox vBox = new VBox();
    VBox.setVgrow( oscBox, Priority.ALWAYS );
    VBox.setVgrow( filterBox, Priority.ALWAYS );
    VBox.setVgrow( noiseBox, Priority.ALWAYS );
    VBox.setVgrow( multiBox, Priority.ALWAYS );
    vBox.getChildren().addAll( headerBox, oscBox, filterBox, noiseBox, multiBox );
    
    setContent( vBox );
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
