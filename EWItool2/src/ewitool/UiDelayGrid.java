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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;

/**
 * @author steve
 *
 */
public class UiDelayGrid extends GridPane {
  
  Slider timeSlider, feedbackSlider, dampingSlider, drySlider, volSlider;
  
  UiDelayGrid() {
    
    setId( "editor-grid" );
    
    Label mainLabel = new Label( "Delay" );
    mainLabel.setId( "editor-section-label" );
    add( mainLabel, 0, 0 );
    
    add( new ControlLabel( "Vol", HPos.CENTER ), 2, 0 );
    add( new ControlLabel( "Time", HPos.CENTER ), 0, 1 );
    add( new ControlLabel( "Feedback", HPos.CENTER ), 1, 1 );
    add( new ControlLabel( "Damping", HPos.CENTER ), 0, 3 );
    add( new ControlLabel( "Dry", HPos.CENTER ), 1, 3 );
    
    timeSlider = new Slider( 0.0, 127.0, 0.0 );
    timeSlider.setOrientation( Orientation.HORIZONTAL );
    timeSlider.setMajorTickUnit( 32.0 );
    add( timeSlider, 0, 2 );
    
    feedbackSlider = new Slider( 0.0, 127.0, 0.0 );
    feedbackSlider.setOrientation( Orientation.HORIZONTAL );
    feedbackSlider.setMajorTickUnit( 32.0 );
    add( feedbackSlider, 1, 2 );
    
    volSlider = new Slider( 0.0, 127.0, 0.0 );
    volSlider.setOrientation( Orientation.VERTICAL );
    volSlider.setMajorTickUnit( 32.0 );
    GridPane.setRowSpan( volSlider, 4 );
    add( volSlider, 2, 1 );
    
    dampingSlider = new Slider( 0.0, 127.0, 0.0 );
    dampingSlider.setOrientation( Orientation.HORIZONTAL );
    dampingSlider.setMajorTickUnit( 32.0 );
    add( dampingSlider, 0, 4 );
    
    drySlider = new Slider( 0.0, 127.0, 0.0 );
    drySlider.setOrientation( Orientation.HORIZONTAL );
    drySlider.setMajorTickUnit( 32.0 );
    add( drySlider, 1, 4 );
    
  }
}
