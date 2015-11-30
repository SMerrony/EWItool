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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class MidiPortsEventHandler implements EventHandler<ActionEvent>
{

  public void handle( ActionEvent arg0 ) {
    
    Dialog<ButtonType> dialog = new Dialog<ButtonType>();
    dialog.setTitle( "EWItool - Select MIDI Ports" );
    dialog.getDialogPane().getButtonTypes().addAll( ButtonType.CANCEL, ButtonType.OK );
    HBox hBox = new HBox();
    ListView<String> inView, outView;
    List<String> inPortList = new ArrayList<String>(),
                 outPortList = new ArrayList<String>();
    ObservableList<String> inPorts = FXCollections.observableArrayList( inPortList ), 
                           outPorts = FXCollections.observableArrayList( outPortList );
    inView = new ListView<String>( inPorts );
    outView = new ListView<String>( outPorts );
       
    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    for (int d = 0; d < infos.length; d++) {
      try {
        device = MidiSystem.getMidiDevice( infos[d] );
        if (!(device instanceof Sequencer) && !(device instanceof Synthesizer)) {
          if (device.getMaxReceivers() != 0) {
            outPorts.add( infos[d].getName() );
            System.out.println( "Debug - OUT Port: " + infos[d].getName());
          } else if (device.getMaxTransmitters() != 0) {
            inPorts.add( infos[d].getName() );
            System.out.println( "Debug - IN Port: " + infos[d].getName() );
          }
        }
        
      } catch (Exception e) {
        System.err.println( "ERROR - Fetching MIDI information" );
      }
    }
    hBox.getChildren().addAll( inView, outView );
    dialog.getDialogPane().setContent( hBox );
    
    Optional<ButtonType> rc = dialog.showAndWait();
    
  }


}
