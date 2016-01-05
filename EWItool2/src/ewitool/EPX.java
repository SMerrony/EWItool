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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author steve
 *
 */
public class EPX {
  
  public static final String USER_AGENT = Main.APP_NAME + "/" + Main.VERSION;
  public static final String PROTOCOL = "http://";
  public static final String BASE_REQ = "/EPX/epx.php?action=";
  
  UserPrefs userPrefs;
  
  EPX( UserPrefs pUserPrefs ) {
    userPrefs = pUserPrefs;
  }

  public boolean testConnection() {
    
    try {
      URL url = new URL( PROTOCOL + userPrefs.getEpxHost() + BASE_REQ + "connectionTest" );
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty( "User-Agent", USER_AGENT );
      int respCode = con.getResponseCode();
      System.out.println( "DEBUG - EPX: Got response " + respCode + " for connection test" );
      if (respCode != 200) return false;
      BufferedReader br = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
      String line;
      StringBuffer reply = new StringBuffer();
      while ((line = br.readLine()) != null) reply.append( line );
      br.close();
      if (reply.toString().contains( "Connection: OK" )) return true;
    } catch( MalformedURLException e ) {
      e.printStackTrace();
    } catch( IOException e ) {
      e.printStackTrace();
    }
    return false;
  }
  
  public boolean testUser() {
    try {
      URL url = new URL( PROTOCOL + userPrefs.getEpxHost() + BASE_REQ + 
                         "validUser&userid=" + userPrefs.getEpxUserid() + 
                         "&passwd=" + userPrefs.getEpxPassword() );
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty( "User-Agent", USER_AGENT );
      int respCode = con.getResponseCode();
      System.out.println( "DEBUG - EPX: Got response " + respCode + " for valid user test" );
      if (respCode != 200) return false;
      BufferedReader br = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
      String line;
      StringBuffer reply = new StringBuffer();
      while ((line = br.readLine()) != null) reply.append( line );
      br.close();
      if (reply.toString().contains( "Login: OK" )) return true;
    } catch( MalformedURLException e ) {
      e.printStackTrace();
    } catch( IOException e ) {
      e.printStackTrace();
    }
    return false;
  }
  
}
