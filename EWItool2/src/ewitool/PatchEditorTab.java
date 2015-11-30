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

import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PatchEditorTab extends Tab {


    
  PatchEditorTab() {
    setText( "Patch Editor" );
    setClosable( false );
    

    
    VBox vBox = new VBox();
    vBox.setMaxWidth( Double.MAX_VALUE );
    
    HBox headerBox = new HBox();
    
    HBox oscBox = new HBox();
    OscGrid osc1Grid = new OscGrid( 0 );
    OscGrid osc2Grid = new OscGrid( 1 );
    oscBox.getChildren().addAll( osc1Grid, osc2Grid );

    HBox filterBox = new HBox();
    FilterGrid oscPriFilterGrid = new FilterGrid( 0, "Osc Primary Filter" );
    FilterGrid oscSecFilterGrid = new FilterGrid( 1, "Osc Secondary Filter" );
    
    filterBox.getChildren().addAll( oscPriFilterGrid, oscSecFilterGrid );

    HBox noiseBox = new HBox();
    FilterGrid noisePriFilterGrid = new FilterGrid( 0, "Noise Primary Filter" );
    FilterGrid noiseSecFilterGrid = new FilterGrid( 1, "Noise Secondary Filter" );
    
    noiseBox.getChildren().addAll( noisePriFilterGrid, noiseSecFilterGrid );
    
    vBox.getChildren().addAll( headerBox, oscBox, filterBox, noiseBox );
    
    setContent( vBox );
  }
}
