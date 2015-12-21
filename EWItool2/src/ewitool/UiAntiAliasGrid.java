/**
 * This file is part of EWItool.
 *
 *  EWItool is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EWItool is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EWItool.  If not, see <http://www.gnu.org/licenses/>.
 */

package ewitool;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * @author steve
 *
 */
public class UiAntiAliasGrid extends GridPane {
  
  CheckBox enableCheck;
  Slider cutoffSlider, keyFollowSlider;
  
  UiAntiAliasGrid(SharedData sharedData, MidiHandler midiHandler) {
    
    setId( "editor-grid" );
    
    RowConstraints fixedRC, vgrowRC;
    fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    getRowConstraints().addAll( fixedRC, vgrowRC, vgrowRC, vgrowRC, vgrowRC );

    Label mainLabel = new Label( "Anti-Alias" );
    mainLabel.setId( "editor-section-label" );
    add( mainLabel, 0, 0 );
    
    enableCheck = new CheckBox( "Enable" );
    enableCheck.setOnAction( (event) -> {
      if (enableCheck.isSelected()) {
	midiHandler.sendLiveControl( 0, 79, 1 );
	sharedData.editPatch.antiAliasSwitch = 1;
      } else {
	midiHandler.sendLiveControl( 0, 79, 0 );
	sharedData.editPatch.antiAliasSwitch = 0;
      }
    });

    add( enableCheck, 0,1 );
    
    cutoffSlider = new Slider( 0.0, 127.0, 0.0 );
    cutoffSlider.setOrientation( Orientation.HORIZONTAL );
    cutoffSlider.setMajorTickUnit( 32.0 );
    cutoffSlider.valueProperty().addListener( (observable, oldVal, newVal)-> {
      midiHandler.sendLiveControl( 1, 79, newVal.intValue() );
      sharedData.editPatch.antiAliasCutoff = newVal.intValue();
    });
    add( cutoffSlider, 0, 3 );
    add( new BoundBelowControlLabel( "Cutoff Freq", HPos.CENTER, cutoffSlider ), 0, 2 );

    
    keyFollowSlider = new Slider( 0.0, 127.0, 0.0 );
    keyFollowSlider.setOrientation( Orientation.HORIZONTAL );
    keyFollowSlider.setMajorTickUnit( 32.0 );
    keyFollowSlider.valueProperty().addListener( (observable, oldVal, newVal)-> {
      midiHandler.sendLiveControl( 2, 79, newVal.intValue() );
      sharedData.editPatch.antiAliasKeyFollow = newVal.intValue();
    });
    add( keyFollowSlider, 0, 5 );
    add( new BoundBelowControlLabel( "Key Follow", HPos.CENTER, keyFollowSlider ), 0, 4 );
    
  }
  
  void setControls( SharedData sharedData ) {
    enableCheck.setSelected( sharedData.editPatch.antiAliasSwitch == 1 );
    cutoffSlider.setValue( sharedData.editPatch.antiAliasCutoff );
    keyFollowSlider.setValue( sharedData.editPatch.antiAliasKeyFollow );
  }
}
