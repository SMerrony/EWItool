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

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Prefs {
  
  public static final String PREFS_NODE = "EWItool";
  public static final String MIDI_IN_PORT = "MIDI_IN_PORT";
  public static final String MIDI_OUT_PORT = "MIDI_OUT_PORT";
  public static final String LIBRARY_LOCATION = "LIBRARY_LOCATION";
  public static final String EPX_HOST = "EPX_HOST";
  public static final String EPX_USERID = "EPX_USERID";
  public static final String EPX_PASSWORD = "EPX_PASSWORD";
  
  public static String getMidiInPort() {
    Preferences p = Preferences.systemRoot().node( PREFS_NODE );
    return p.get( MIDI_IN_PORT, "" );
  }
  public static String getMidiOutPort() {
    Preferences p = Preferences.systemRoot().node( PREFS_NODE );
    return p.get( MIDI_OUT_PORT, "" );
  }
  public static String getLibraryLocation() {
    Preferences p = Preferences.systemRoot().node( PREFS_NODE );
    return p.get( LIBRARY_LOCATION, "" );
  }
  public static String getEpxHost() {
    Preferences p = Preferences.systemRoot().node( PREFS_NODE );
    return p.get( EPX_HOST, "" );
  }
  public static String getEpxUserid() {
    Preferences p = Preferences.systemRoot().node( PREFS_NODE );
    return p.get( EPX_USERID, "" );
  }
  public static String getEpxPassword() {
    Preferences p = Preferences.systemRoot().node( PREFS_NODE );
    return p.get( EPX_PASSWORD, "" );
  }
  
  public boolean clearPrefs() {
    Preferences p = Preferences.systemRoot().node( PREFS_NODE );
    try {
      p.clear();
    } catch( BackingStoreException e ) {
      return false;
    }
    return true;
  }
  
  

}
