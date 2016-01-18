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
import java.net.URLEncoder;
import java.util.LinkedList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author steve
 *
 */
public class EPX {
  
  public static final String USER_AGENT = Main.APP_NAME + "/" + Main.APP_VERSION + 
                                          " (" + System.getProperty( "os.name" ) + ")";
  public static final String PROTOCOL = "http://";
  public static final String BASE_REQ = "/EPX/epx.php?action=";
  public static final String URL_ENCODING = "UTF-8";
  
  SharedData sharedData;
  UserPrefs userPrefs;
  
  class QueryResult {
    String name_user;
    int    epx_id;
  }
  
  class DetailsResult {
    String name;
    String contrib;
    String origin;
    String hex;
    String type;
    String desc;
    String added;
    boolean privateFlag;
    String tags;
  }
  
  EPX( SharedData pSharedData, UserPrefs pUserPrefs ) {
    sharedData = pSharedData;
    userPrefs = pUserPrefs;
  }

  public boolean testConnection() {
    
    try {
      URL url = new URL( PROTOCOL + userPrefs.getEpxHost() + BASE_REQ + "connectionTest" );
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty( "User-Agent", USER_AGENT );
      int respCode = con.getResponseCode();
      Debugger.log( "DEBUG - EPX: Got response " + respCode + " for connection test" );
      if (respCode == 200) {
        BufferedReader br = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
        String line;
        StringBuffer reply = new StringBuffer();
        while ((line = br.readLine()) != null) reply.append( line );
        br.close();
        if (reply.toString().contains( "Connection: OK" )) {
          sharedData.setEpxAvailable( true );
          return true;
        }
      }
    } catch( MalformedURLException e ) {
      e.printStackTrace();
    } catch( IOException e ) {
      e.printStackTrace();
    }
    sharedData.setEpxAvailable( false );
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
      Debugger.log( "DEBUG - EPX: Got response " + respCode + " for valid user test" );
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
  
  public String[] getDropdowns() {
    String[] dds = new String[3];
    try {
      URL url = new URL( PROTOCOL + userPrefs.getEpxHost() + BASE_REQ + "dropdownData" );
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty( "User-Agent", USER_AGENT );
      int respCode = con.getResponseCode();
      Debugger.log( "DEBUG - EPX: Got response " + respCode + " for dropdownData request" );
      if (respCode != 200) return null;
      BufferedReader br = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
      String line = br.readLine();
      while (!line.contains( "<body>" )) {
        line = br.readLine();
      }
      dds[0] = br.readLine();
      dds[1] = br.readLine();
      dds[2] = br.readLine();
      br.close();
      return dds;
    } catch( MalformedURLException e ) {
      e.printStackTrace();
    } catch( IOException e ) {
      e.printStackTrace();
    }
    return null;
  }
  
  public LinkedList<QueryResult> query( String type, String since, String contrib, String origin, String tags ) {
    try {
      URL url = new URL( PROTOCOL + userPrefs.getEpxHost() + BASE_REQ + "query" +
                         "&userid=" + userPrefs.getEpxUserid() + 
                         "&passwd=" + userPrefs.getEpxPassword() +
                         "&type=" + type +
                         "&since=" + URLEncoder.encode( since, URL_ENCODING ) +
                         "&contrib=" + URLEncoder.encode( contrib, URL_ENCODING ) +
                         "&origin=" + URLEncoder.encode( origin, URL_ENCODING ) +
                         "&tags=" + URLEncoder.encode( tags, URL_ENCODING )
                        );
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty( "User-Agent", USER_AGENT );
      int respCode = con.getResponseCode();
      Debugger.log( "DEBUG - EPX: Got response " + respCode + " for query request" );
      if (respCode != 200) return null;
      BufferedReader br = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
      String line = br.readLine();
      while (!line.contains( "<body>" )) {
        line = br.readLine();
      }
      line = br.readLine();
      LinkedList<QueryResult> lqr = new LinkedList<QueryResult>();
      while (line != null && line.length() > 3 && !line.contains( "</body>" )) {
        if (line.startsWith( "Error:" )) {
          System.err.println( "ERROR - EPX: " + line );
          break;
        }
        QueryResult qr = new QueryResult();
        qr.name_user = line.substring( 0, line.lastIndexOf( ',' ) );
        qr.epx_id = Integer.parseInt( line.substring( line.lastIndexOf( ',' ) + 1 ) );
        lqr.add( qr );
        line = br.readLine();
      }
      br.close();
      Debugger.log( "DEBUG - EPX: Query returning " + lqr.size() + " rows" );
      return lqr;
    } catch( MalformedURLException e ) {
      e.printStackTrace();
    } catch( IOException e ) {
      e.printStackTrace();
    }
    return null;
  }

  public DetailsResult getDetails( int id ) {
    DetailsResult dr = new DetailsResult();
    try {
      URL url = new URL( PROTOCOL + userPrefs.getEpxHost() + BASE_REQ + "fetchPatch" +
                         "&userid=" + userPrefs.getEpxUserid() + 
                         "&passwd=" + userPrefs.getEpxPassword() +
                         "&id=" + id );
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty( "User-Agent", USER_AGENT );
      int respCode = con.getResponseCode();
      Debugger.log( "DEBUG - EPX: Got response " + respCode + " for fetchPatch request" );
      if (respCode != 200) return null;
      BufferedReader br = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
      String line = br.readLine();
      while (!line.contains( "<body>" )) {
        line = br.readLine();
      }
      String[] dets = br.readLine().split( "," );
      br.close();
      dr.name = dets[0];
      dr.contrib = dets[1];
      dr.origin = dets[2];
      dr.hex = dets[3];
      dr.type = dets[4];
      dr.desc = dets[5];
      dr.added = dets[6];
      if (dets[7].contentEquals( "0" )) 
        dr.privateFlag = false;
      else
        dr.privateFlag = true;
      if (dets.length == 9) dr.tags = dets[8];
    } catch( MalformedURLException e ) {
      e.printStackTrace();
    } catch( IOException e ) {
      e.printStackTrace();
    }
    return dr;
  }
  
  public void insertPatch( String name, String origin, String type,
                           String desc, boolean isPrivate, String tags,
                           String hexPatch ) {
    URL url;
    String priv = isPrivate ? "true" : "false";
    try {
      url = new URL( PROTOCOL + userPrefs.getEpxHost() + BASE_REQ + "insertPatch" +
          "&userid=" + userPrefs.getEpxUserid() + 
          "&passwd=" + userPrefs.getEpxPassword() +
          "&name=" + URLEncoder.encode( name, URL_ENCODING ) +
          "&origin=" + URLEncoder.encode( origin, URL_ENCODING ) +
          "&type=" + URLEncoder.encode( type, URL_ENCODING ) +
          "&desc=" + URLEncoder.encode( desc, URL_ENCODING ) +
          "&private=" + priv +
          "&tags=" + URLEncoder.encode( tags, URL_ENCODING ) +
          "&hexpatch=" + hexPatch
          );
      
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty( "User-Agent", USER_AGENT );
      int respCode = con.getResponseCode();
      Debugger.log( "DEBUG - EPX: Got response " + respCode + " for insertPatch request" );
      BufferedReader br = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
      String line = br.readLine();
      while (!line.contains( "<body>" )) {
        line = br.readLine();
      }
      String resp = br.readLine();
      if (resp.contains( "Resource id #" )) {
        Alert okAl = new Alert( AlertType.INFORMATION );
        okAl.setTitle( "EWItool - Patch Exchange Submission" );
        okAl.setContentText( "Patch Succesfully sent to EWI Patch Exchange - Thank You" );
        okAl.showAndWait();
      } else if (resp.contains( "duplicate key" )) {
        Alert w1Al = new Alert( AlertType.ERROR );
        w1Al.setTitle( "EWItool - Patch Exchange Submission" );
        w1Al.setContentText( "Export error - that patch is already in the exchange" );
        w1Al.showAndWait(); 
      } else {
        Alert w2Al = new Alert( AlertType.ERROR);
        w2Al.setTitle( "EWItool - Patch Exchange Submission" );
        w2Al.setContentText( "Export Error" );
        Debugger.log( "DEBUG - EPX: Got error " + resp + " for insertPatch request" );
        w2Al.showAndWait();
      }
    } catch( MalformedURLException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch( IOException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
 
  }
  
  public void deletePatch( int id ) {
    URL url;
    try {
      url = new URL( PROTOCOL + userPrefs.getEpxHost() + BASE_REQ + "deletePatch" +
          "&userid=" + userPrefs.getEpxUserid() + 
          "&passwd=" + userPrefs.getEpxPassword() +
          "&id=" + id );
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty( "User-Agent", USER_AGENT );
      int respCode = con.getResponseCode();
      Debugger.log( "DEBUG - EPX: Got response " + respCode + " for deletePatch request" );

    } catch( MalformedURLException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch( IOException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
