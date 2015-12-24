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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class PatchEditorTab extends Tab {
  
  enum Osc { OSC1, OSC2 }
  enum Filter { OSC_PRI, OSC_SEC, NOISE_PRI, NOISE_SEC }
    
  PatchEditorTab(SharedData sharedData, MidiHandler midiHandler) {
    setText( "Patch Editor" );
    setClosable( false );
      
    ListView<EWI4000sPatch> patchList; // TODO - this occurs elsewhere eg. ScratchPadTab - abstract it out?
	patchList = new ListView<EWI4000sPatch>( sharedData.ewiPatchList ); 

    patchList.setCellFactory( new Callback<ListView<EWI4000sPatch>, ListCell<EWI4000sPatch>>(){
      @Override 
      public ListCell<EWI4000sPatch> call( ListView<EWI4000sPatch> p ) {
        ListCell<EWI4000sPatch> cell = new ListCell<EWI4000sPatch>() {
          @Override
          protected void updateItem( EWI4000sPatch ep, boolean bln ) {
            super.updateItem( ep, bln );
            if (ep != null) {
              setText( ep.patchNum + ": " + ep.getName() );
            } else {
              setText( "" );
            }
          }
        };
        return cell;
      }
    });
    HBox headerBox = new HBox();
    Region lSpaceRegion = new Region(), rSpaceRegion = new Region();
    HBox.setHgrow( lSpaceRegion, Priority.ALWAYS );
    HBox.setHgrow( rSpaceRegion, Priority.ALWAYS );
    headerBox.getChildren().addAll( lSpaceRegion, patchList, rSpaceRegion );
    
    UiOscGrid osc1Grid = new UiOscGrid( sharedData, midiHandler, Osc.OSC1 );
    UiOscGrid osc2Grid = new UiOscGrid( sharedData, midiHandler, Osc.OSC2 );
    HBox oscBox = new HBox();
    oscBox.getChildren().addAll( osc1Grid, osc2Grid );
    
    UiFormantGrid formantGrid = new UiFormantGrid();
    UiKeyTriggerGrid keyTriggerGrid = new UiKeyTriggerGrid();
    VBox subVbox = new VBox();
    VBox.setVgrow( formantGrid, Priority.ALWAYS );
    VBox.setVgrow( keyTriggerGrid, Priority.ALWAYS );
    subVbox.getChildren().addAll( formantGrid, keyTriggerGrid );
    subVbox.setMinWidth( 80.0 );
    UiFilterGrid oscPriFilterGrid = new UiFilterGrid( sharedData, midiHandler, Filter.OSC_PRI );
    UiFilterGrid oscSecFilterGrid = new UiFilterGrid( sharedData, midiHandler, Filter.OSC_SEC );
    HBox filterBox = new HBox();    
    filterBox.getChildren().addAll( subVbox, oscPriFilterGrid, oscSecFilterGrid );

    UiNoiseGrid noiseGrid = new UiNoiseGrid();
    UiFilterGrid noisePriFilterGrid = new UiFilterGrid( sharedData, midiHandler, Filter.NOISE_PRI );
    UiFilterGrid noiseSecFilterGrid = new UiFilterGrid( sharedData, midiHandler, Filter.NOISE_SEC );
    HBox noiseBox = new HBox();
    noiseBox.getChildren().addAll( noiseGrid, noisePriFilterGrid, noiseSecFilterGrid );
    
    UiChorusGrid chorusGrid = new UiChorusGrid( sharedData, midiHandler);
    UiDelayGrid delayGrid = new UiDelayGrid( sharedData, midiHandler );
    UiReverbGrid reverbGrid = new UiReverbGrid( sharedData, midiHandler );
    UiBiteGrid biteGrid = new UiBiteGrid( sharedData, midiHandler);
    UiPitchBendGrid pitchBendGrid = new UiPitchBendGrid( sharedData, midiHandler);
    UiAntiAliasGrid antiAliasGrid = new UiAntiAliasGrid( sharedData, midiHandler);
    UiLevelsGrid levelsGrid = new UiLevelsGrid( sharedData, midiHandler );
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

    // what to do when the tab is selected...
    this.setOnSelectionChanged( new EventHandler<Event>() {
    	@Override
    	public void handle(Event arg0) {
    		System.out.println( "DEBUG - Patch editor activated" );
    		patchList.getSelectionModel().select( sharedData.getEditingPatchNumber() );
    		patchList.scrollTo( sharedData.getEditingPatchNumber() );
    		osc1Grid.setControls( sharedData, Osc.OSC1 );
    		osc2Grid.setControls( sharedData, Osc.OSC2 );
    		oscPriFilterGrid.setControls( sharedData, Filter.OSC_PRI );
    		oscSecFilterGrid.setControls( sharedData, Filter.OSC_SEC );
    		noisePriFilterGrid.setControls( sharedData, Filter.NOISE_PRI );
    		noiseSecFilterGrid.setControls( sharedData, Filter.NOISE_SEC );
    		chorusGrid.setControls( sharedData );
    		delayGrid.setControls( sharedData );
    		reverbGrid.setControls( sharedData );
    		biteGrid.setControls( sharedData );
    		pitchBendGrid.setControls( sharedData );
    		antiAliasGrid.setControls( sharedData );
    		levelsGrid.setControls( sharedData );
    		
    		midiHandler.sendPatch( sharedData.editPatch, EWI4000sPatch.EWI_EDIT );
    	}
    }
    );

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
