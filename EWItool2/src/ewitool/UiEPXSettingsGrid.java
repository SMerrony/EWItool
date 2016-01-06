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

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

/**
 * @author steve
 *
 */
public class UiEPXSettingsGrid extends GridPane {

  UiEPXSettingsGrid( UserPrefs userPrefs, EPX epx, GridPane queryGrid ) {
    Label settingsSectionLabel = new Label( "EPX Settings" );
    settingsSectionLabel.setId( "epx-section-label" );
    add( settingsSectionLabel, 0, 0 );
    add( new Label( "User ID" ), 0, 1 );
    //uidField, passwdField, serverField;
    TextField uidField = new TextField();
    String uid = userPrefs.getEpxUserid();
    if (uid == "<Not Set>") {
      uidField.setPromptText( uid );
    } else {
      uidField.setText( uid );
    }
    add( uidField, 1, 1 );

    add( new Label( "Password" ), 0, 2 );
    PasswordField passwdField = new PasswordField();
    String pwd = userPrefs.getEpxPassword();
    if (pwd == "<Not Set>") {
      passwdField.setPromptText( pwd );
    } else {
      passwdField.setText( pwd );
    }
    add( passwdField, 1, 2 );

    add( new Label( "Server" ), 0, 3 );
    TextField serverField = new TextField();
    String serv = userPrefs.getEpxHost();
    if (serv == "<Not Set>") {
      serverField.setPromptText( serv );
    } else {
      serverField.setText( serv );
    }
    add( serverField, 1, 3 );

    Button epxTestButton = new Button( "Test" );
    GridPane.setColumnSpan( epxTestButton, 2 );
    epxTestButton.setOnAction( (ae) -> {
      if (uidField.getText().isEmpty() ||
          passwdField.getText().isEmpty() ||
          serverField.getText().isEmpty()) {
        Alert warnAlert = new Alert( AlertType.WARNING );
        warnAlert.setTitle( "EWItool - Warning" );
        warnAlert.setContentText( "Please enter your EPX credentials in the Settings fields." );
        warnAlert.showAndWait();
      } else {
        userPrefs.setEpxUserid( uidField.getText() );
        userPrefs.setEpxPassword( passwdField.getText() );
        userPrefs.setEpxHost( serverField.getText() );
        if (!epx.testConnection()) {
          Alert errAlert = new Alert( AlertType.ERROR );
          errAlert.setTitle( "EWItool - Error" );
          errAlert.setContentText( "Could not connect to EPX server - please check your server setting" );
          errAlert.showAndWait();
        } else {
          if (!epx.testUser()) {
            Alert errAlert = new Alert( AlertType.ERROR );
            errAlert.setTitle( "EWItool - Error" );
            errAlert.setContentText( "Invalid EPX user ID or password - please check your settings" );
            errAlert.showAndWait();
          } else {
            Alert okAlert = new Alert( AlertType.INFORMATION );
            okAlert.setTitle( "EWItool - EPX OK" );
            okAlert.setContentText( "Connection, user ID and password all OK - EPX credentials stored" );
            okAlert.showAndWait();
            queryGrid.setDisable( false );
          }
        }
      }   
    });
    add( epxTestButton, 0, 4 );
  }
}
