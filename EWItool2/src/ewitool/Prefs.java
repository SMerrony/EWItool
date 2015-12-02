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
  
  public static final String PREFS_NODE = "ewitool";
  public static final String MIDI_IN_PORT = "MIDI_IN_PORT";
  public static final String MIDI_OUT_PORT = "MIDI_OUT_PORT";
  public static final String LIBRARY_LOCATION = "LIBRARY_LOCATION";
  public static final String EPX_HOST = "EPX_HOST";
  public static final String EPX_USERID = "EPX_USERID";
  public static final String EPX_PASSWORD = "EPX_PASSWORD";
  
  public static String getMidiInPort() {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    return p.get( MIDI_IN_PORT, "" );
  }
  public static void setMidiInPort( String ip ) {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    p.put( MIDI_IN_PORT, ip );
  }
  public static String getMidiOutPort() {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    return p.get( MIDI_OUT_PORT, "" );
  }
  public static void setMidiOutPort( String op ) {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    p.put( MIDI_OUT_PORT, op );
  }
  public static String getLibraryLocation() {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    return p.get( LIBRARY_LOCATION, "" );
  }
  public static void setLibraryLocation( String ll ) {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    p.put( LIBRARY_LOCATION, ll );
  }
  public static String getEpxHost() {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    return p.get( EPX_HOST, "" );
  }
  public static void setEpxHost( String host ) {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    p.put( EPX_HOST, host );
  }
  public static String getEpxUserid() {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    return p.get( EPX_USERID, "" );
  }
  public static void setEpxUserid( String user ) {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    p.put( EPX_USERID, user );
  }
  public static String getEpxPassword() {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    return p.get( EPX_PASSWORD, "" );
  }
  public static void setEpxPassword( String pwd ) {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    p.put( EPX_PASSWORD, pwd );
  }
  
  public boolean clearPrefs() {
    Preferences p = Preferences.userRoot().node( PREFS_NODE );
    try {
      p.clear();
    } catch( BackingStoreException e ) {
      return false;
    }
    return true;
  }
  
  

}
