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

import java.util.Observable;
import java.util.Observer;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * @author steve
 *
 * The status bar watches the shared data object for changes and displays
 * information at the bottom of the EWItool window.
 * 
 */
public class UiStatusBar extends HBox implements Observer {
  
  Label messageLabel, midiInLabel, midiOutLabel, ewiLabel, scratchPadLabel, epxLabel;
  SharedData sharedData;
  
  private static final long MESSAGE_TIMEOUT_MS = 10 * 1000;  // minimum 10s timeout for textual message
  
  UiStatusBar(SharedData pSharedData) {
    sharedData = pSharedData;
    setId( "status-bar" );

    midiInLabel = new Label();
    midiInLabel.setId( "status-value" );
    midiOutLabel = new Label();
    midiOutLabel.setId( "status-value" );
    ewiLabel = new Label();
    ewiLabel.setId( "status-value" );
    scratchPadLabel = new Label();
    scratchPadLabel.setId( "status-value" );
    epxLabel = new Label();
    epxLabel.setId( "status-value" );
    Region spacerRegion = new Region();
    HBox.setHgrow( spacerRegion, Priority.ALWAYS );
    messageLabel = new Label();
    messageLabel.setId( "status-message" );
    
    getChildren().addAll( new Label( "MIDI In:" ), midiInLabel,
                          new Label( "MIDI Out:" ), midiOutLabel,
                          new Label( "EWI:" ), ewiLabel,
                          new Label( "Scratchpad Items:" ), scratchPadLabel,
                          new Label( "EPX:" ), epxLabel,
                          spacerRegion,
                          messageLabel
                        );
  }

  /* This update method is invoked whenever any of the observable SharedData items change.
   * It might be necessary to refine this in the future, or replace with a timed scan of 
   * the items.
   * 
   * (non-Javadoc)
   * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
   */
  @Override
  public void update( Observable o, Object arg ) {
    midiInLabel.setText( sharedData.getMidiInDev() );
    midiOutLabel.setText( sharedData.getMidiOutDev() );
    if (sharedData.getEwiAttached()) {
      ewiLabel.setText( "EWI4000s" );
    } else {
      ewiLabel.setText( "Not detected" );
    }
    scratchPadLabel.setText( Integer.toString( sharedData.getScratchPadCount() ) );
    if (sharedData.getEpxAvailable()) {
      epxLabel.setText( "Available" );
    } else {
      epxLabel.setText( "Not connected" );
    }
    if (System.currentTimeMillis() - sharedData.getStatusMillis() > MESSAGE_TIMEOUT_MS ) {
      messageLabel.setText( "" );
    } else {
      messageLabel.setText( sharedData.getStatusMessage() );
    }
  }
}
