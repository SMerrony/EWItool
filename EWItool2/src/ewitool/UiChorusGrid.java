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
import javafx.geometry.VPos;
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
public class UiChorusGrid extends GridPane {
  
  CheckBox enableCheck;
  Slider delay1Slider, delay2Slider, vib1Slider, vib2Slider,
         wet1Slider, wet2Slider,
         drySlider,
         feedbackSlider, lfoFreqSlider;
  
  UiChorusGrid() {
    
    setId( "editor-grid" );
    
    RowConstraints fixedRC, vgrowRC;
    fixedRC = new RowConstraints();
    fixedRC.setVgrow( Priority.NEVER );
    vgrowRC = new RowConstraints();
    vgrowRC.setVgrow( Priority.ALWAYS );
    
    getRowConstraints().addAll( fixedRC, vgrowRC, vgrowRC, vgrowRC, vgrowRC );
    
    Label mainLabel = new Label( "Chorus" );
    mainLabel.setId( "editor-section-label" );
    add( mainLabel, 0, 0 );
    
    enableCheck = new CheckBox( "Enable" );
    add( enableCheck, 1,0 );
    
    delay1Slider = new Slider( 0.0, 127.0, 0.0 );
    delay1Slider.setOrientation( Orientation.HORIZONTAL );
    delay1Slider.setMajorTickUnit( 32.0 );
    add( delay1Slider, 0, 2 );
    add( new BoundBelowControlLabel( "Delay 1", HPos.CENTER, delay1Slider ), 0, 1 );
    
    delay2Slider = new Slider( 0.0, 127.0, 0.0 );
    delay2Slider.setOrientation( Orientation.HORIZONTAL );
    delay2Slider.setMajorTickUnit( 32.0 );
    add( delay2Slider, 0, 4 );
    add( new BoundBelowControlLabel( "Delay 2", HPos.CENTER, delay2Slider ), 0, 3 );
    
    vib1Slider = new Slider( 0.0, 127.0, 0.0 );
    vib1Slider.setOrientation( Orientation.HORIZONTAL );
    vib1Slider.setMajorTickUnit( 32.0 );
    add( vib1Slider, 1, 2 );
    add( new BoundBelowControlLabel( "Vib 1", HPos.CENTER, vib1Slider ), 1, 1 );
    
    vib2Slider = new Slider( 0.0, 127.0, 0.0 );
    vib2Slider.setOrientation( Orientation.HORIZONTAL );
    vib2Slider.setMajorTickUnit( 32.0 );
    add( vib2Slider, 1, 4 );
    add( new BoundBelowControlLabel( "Vib 2", HPos.CENTER, vib2Slider ), 1, 3 );
    
    wet1Slider = new Slider( 0.0, 127.0, 0.0 );
    wet1Slider.setOrientation( Orientation.HORIZONTAL );
    wet1Slider.setMajorTickUnit( 32.0 );
    add( wet1Slider, 2, 2 );
    add( new BoundBelowControlLabel( "Wet 1", HPos.CENTER, wet1Slider ), 2, 1 );
    
    wet2Slider = new Slider( 0.0, 127.0, 0.0 );
    wet2Slider.setOrientation( Orientation.HORIZONTAL );
    wet2Slider.setMajorTickUnit( 32.0 );
    add( wet2Slider, 2, 4 );
    add( new BoundBelowControlLabel( "Wet 2", HPos.CENTER, wet2Slider ), 2, 3 );
    
    drySlider = new Slider( 0.0, 127.0, 0.0 );
    drySlider.setOrientation( Orientation.VERTICAL );
    drySlider.setMajorTickUnit( 32.0 );
    GridPane.setRowSpan( drySlider, 4 );
    add( drySlider, 3, 1 );
    add( new BoundBelowControlLabel( "Dry", HPos.CENTER, drySlider ), 3, 0 );
    
    feedbackSlider = new Slider( 0.0, 127.0, 0.0 );
    feedbackSlider.setOrientation( Orientation.HORIZONTAL );
    feedbackSlider.setMajorTickUnit( 32.0 );
    add( feedbackSlider, 4, 2 );
    add( new BoundBelowControlLabel( "Feedback", HPos.CENTER.CENTER, feedbackSlider ), 4, 1 );
    
    lfoFreqSlider = new Slider( 0.0, 127.0, 0.0 );
    lfoFreqSlider.setOrientation( Orientation.HORIZONTAL );
    lfoFreqSlider.setMajorTickUnit( 32.0 );
    add( lfoFreqSlider, 4, 4 );
    add( new BoundBelowControlLabel( "LFO Freq", HPos.CENTER, lfoFreqSlider ), 4, 3 );
    
  }

}
