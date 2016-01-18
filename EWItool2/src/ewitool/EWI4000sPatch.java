/**
 * EWI4000sPatch - This class represents a patch on the EWI4000s
 * 
 * The patch is represented in two ways, a blob and a structure.  The decodeBlob() and
 * encodeBlob() methods convert between the two.
 * 
 * Converted from ewi4000spatch.h in C++ version.
 * 
 * @author steve
 * 
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
 *
 */
package ewitool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import ewitool.Main.Status;

public class EWI4000sPatch { 
  
  public final static int EWI_NUM_PATCHES  = 100;  // 0..99
  public final static int EWI_PATCH_LENGTH = 206;  // bytes
  public final static int EWI_PATCHNAME_LENGTH  = 32;
  public final static byte EWI_EDIT = 0x20;
  public final static byte EWI_SAVE = 0x00;
  
  class Nrpn {
    byte lsb, msb, offset;
    
    void decode( byte[] raw ) {
      lsb = raw[0];
      msb = raw[1];
      offset = raw[2];
    }
    
    byte[] toBytes() {
      byte[] blob = new byte[3];
      blob[0] = lsb;
      blob[1] = msb;
      blob[2] = offset;
      return blob;
    }
  }
  
  class Osc {
    Nrpn nrpn;
    int octave;     // int 64 +/-2
    int semitone;   // int 64 +/-12
    int fine;  // int -50 - +50 cents
    int beat;  // int 0% - 100%
    byte filler1;    // this really is unused (for firmware 2.3 anyway)
    int sawtooth;   // %
    int triangle;   // %
    int square;     // %
    int pulseWidth; // %
    int pwmFreq;    // %
    int pwmDepth;   // %
    int sweepDepth; // %
    int sweepTime;  // %
    int breathDepth;     // %  ?-50/+50?
    int breathAttain;    // %
    int breathCurve;     // %  ? =50+50?
    int breathThreshold; // %
    int level;      // %
    
    Osc() {
      nrpn = new Nrpn();
    }
    
    void decode( byte[] raw ) {
      nrpn.decode( raw );
      octave    = raw[3];
      semitone  = raw[4];
      fine      = raw[5];
      beat      = raw[6];
      filler1   = raw[7];
      sawtooth  = raw[8];
      triangle  = raw[9];
      square    = raw[10];
      pulseWidth = raw[11];
      pwmFreq     = raw[12];
      pwmDepth    = raw[13];
      sweepDepth  = raw[14];
      sweepTime   = raw[15];
      breathDepth = raw[16];
      breathAttain = raw[17];
      breathCurve = raw[18];
      breathThreshold = raw[19];
      level       = raw[20];
    }
    
    byte[] toBytes() {
      byte[] blob = new byte[21];
      blob = Arrays.copyOf( nrpn.toBytes(), 21 );
      blob[3] = (byte) octave;
      blob[4] = (byte) semitone;
      blob[5] = (byte) fine;
      blob[6] = (byte) beat;
      blob[7] = filler1;
      blob[8] = (byte) sawtooth;
      blob[9] = (byte) triangle;
      blob[10] = (byte) square;
      blob[11] = (byte) pulseWidth;
      blob[12] = (byte) pwmFreq;
      blob[13] = (byte) pwmDepth;
      blob[14] = (byte) sweepDepth;
      blob[15] = (byte) sweepTime;
      blob[16] = (byte) breathDepth;
      blob[17] = (byte) breathAttain;
      blob[18] = (byte) breathCurve;
      blob[19] = (byte) breathThreshold;
      blob[20] = (byte) level;
      return blob;
    }
  }
  
  class Filter {
    Nrpn nrpn;
    int mode;
    int freq;
    int q;
    int keyFollow;
    int breathMod;
    int lfoFreq;
    int lfoDepth;
    int lfoBreath;
    int lfoThreshold;
    int sweepDepth;
    int sweepTime;
    int breathCurve;  
    
    Filter() {
      nrpn = new Nrpn();
    }
 
    void decode( byte[] raw ) {
      nrpn.decode( raw );
      mode    = raw[3];
      freq    = raw[4];
      q       = raw[5];
      keyFollow = raw[6];
      breathMod = raw[7];
      lfoFreq   = raw[8];
      lfoDepth  = raw[9];
      lfoBreath = raw[10];
      lfoThreshold  = raw[11];
      sweepDepth    = raw[12];
      sweepTime     = raw[13];
      breathCurve   = raw[14];
    }
    
    byte[] toBytes() {
      byte[] blob = new byte[15];
      blob = Arrays.copyOf( nrpn.toBytes(), 21 );
      blob[3] = (byte) mode;
      blob[4] = (byte) freq;
      blob[5] = (byte) q;
      blob[6] = (byte) keyFollow;
      blob[7] = (byte) breathMod;
      blob[8] = (byte) lfoFreq;
      blob[9] = (byte) lfoDepth;
      blob[10] = (byte) lfoBreath;
      blob[11] = (byte) lfoThreshold;
      blob[12] = (byte) sweepDepth;
      blob[13] = (byte) sweepTime;
      blob[14] = (byte) breathCurve;
      return blob;
    }

  }
  
  byte patchBlob[];
  
  byte header[];
  byte mode;     // 0x00 to store, 0x20 to edit
  int patchNum;
  byte filler2;
  byte filler3;
  byte filler4;
  byte filler5;
  byte filler6;
  byte filler7;
  volatile char name[];
  Osc  osc1;     // 64,18
  Osc  osc2;     // 65,18
  Filter oscFilter1;   // 72,12
  Filter oscFilter2;   // 73,12
  Filter noiseFilter1; // 74,12
  Filter noiseFilter2; // 75,12
  Nrpn antiAliasNRPN;  // 79,3
  int antiAliasSwitch;
  int antiAliasCutoff;
  int antiAliasKeyFollow;
  Nrpn noiseNRPN;    // 80,3
  int noiseTime;
  int noiseBreath;
  int noiseLevel;
  Nrpn miscNRPN;   // 81,10
  int bendRange;
  int bendStepMode;
  int biteVibrato;
  int oscFilterLink;
  int noiseFilterLink;
  int formantFilter;
  int osc2Xfade;
  int keyTrigger;
  int filler10;
  int chorusSwitch;
  Nrpn ampNRPN;    // 88,3
  int biteTremolo;
  int ampLevel;
  int octaveLevel;
  Nrpn chorusNRPN;   // 112,9
  int chorusDelay1;
  int chorusModLev1;
  int chorusWetLev1;
  int chorusDelay2;
  int chorusModLev2;
  int chorusWetLev2;
  int chorusFeedback;
  int chorusLFOfreq;
  int chorusDryLevel;
  Nrpn delayNRPN;    // 113,5
  int delayTime;
  int delayFeedback;
  int delayDamp;
  int delayLevel;
  int delayDry;   // ZJ
  Nrpn reverbNRPN;   // 114,5
  int reverbDry;    // ZJ
  int reverbLevel;
  int reverbDensity;
  int reverbTime;
  int reverbDamp;
  byte trailer_f7;   // 0xf7 !!!  
  
  private boolean empty;
  
  EWI4000sPatch() {
    patchBlob = new byte[EWI_PATCH_LENGTH];
    header = new byte[4];
    name = new char[EWI_PATCHNAME_LENGTH];
    osc1 = new Osc();
    osc2 = new Osc();
    oscFilter1 = new Filter();
    oscFilter2 = new Filter();
    noiseFilter1 = new Filter();
    noiseFilter2 = new Filter();
    antiAliasNRPN = new Nrpn();
    noiseNRPN = new Nrpn();
    miscNRPN = new Nrpn();
    ampNRPN = new Nrpn();
    chorusNRPN = new Nrpn();
    delayNRPN = new Nrpn();
    reverbNRPN = new Nrpn();
   
    setEmpty( true );
  }
  
  /*** Construct from byte[], i.e. blob
   *  
   * @param blob
   */
  EWI4000sPatch( byte[] blob ) {
    this();
    patchBlob = blob;
    decodeBlob();
    setEmpty( false );
  }
  
  /*** Construct from String of hex digits
   * 
   * @param hexString
   */
  EWI4000sPatch( String hexString ) {
    this();
    patchBlob = DatatypeConverter.parseHexBinary( hexString );
    decodeBlob();
    setEmpty( false );
  }
  
  public boolean isEmpty() { return empty; }

  public void setEmpty( boolean empty ) { this.empty = empty; }
  
  // the patch name is stored in the patch as a space-padded char[]
  // so it must be converted for use as a normal string
  public String getName() { return new String( name ).trim(); }
  
  // set the patch name (see above) from a normal string
  public boolean setName( String newName ) {
    if (newName.length() == 0 || newName.length() > EWI_PATCHNAME_LENGTH) return false;
    char[] cArr = new char[EWI_PATCHNAME_LENGTH]; 
    for (int i = 0; i < EWI_PATCHNAME_LENGTH; i++)
      if (i < newName.length()) {
        cArr[i] = newName.charAt( i );
      } else {
        cArr[i] = ' ';
      }
    name = cArr;
    nameToBlob();
    return true;
  }
  
  public void setPatchNum( byte numByte ) {
    patchNum = numByte;
    patchBlob[5] = numByte;
  }
  
  public void setPatchMode( byte newMode ) {
    if (newMode != EWI_SAVE && newMode != EWI_EDIT) {
      System.err.println( "Error - Internal error in EWI4000sPatch: Invalid Edit/Save mode requested" );
      System.exit( 1 );
    }
    mode = newMode;
    patchBlob[4] = mode;
  }

  // extract the individual properties from the blob
  void decodeBlob() {
     header = Arrays.copyOfRange( patchBlob, 0, 4 );
     mode = patchBlob[4];     // 0x00 to store, 0x20 to edit
     patchNum = patchBlob[5];
     filler2 = patchBlob[6];
     filler3 = patchBlob[7];
     filler4 = patchBlob[8];
     filler5 = patchBlob[9];
     filler6 = patchBlob[10];
     filler7 = patchBlob[11];
     for (int ix = 12; ix < (12+EWI_PATCHNAME_LENGTH); ix++)
       name[ix - 12] = (char) patchBlob[ix];
     osc1.decode( Arrays.copyOfRange( patchBlob, 44, 65) );     // 64,18
     osc2.decode( Arrays.copyOfRange( patchBlob, 65, 86 ) );     // 65,18
     oscFilter1.decode( Arrays.copyOfRange( patchBlob, 86, 101 ) );   // 72,12
     oscFilter2.decode( Arrays.copyOfRange( patchBlob, 101, 116 ) );   // 73,12
     noiseFilter1.decode( Arrays.copyOfRange( patchBlob, 116, 131 ) ); // 74,12
     noiseFilter2.decode( Arrays.copyOfRange( patchBlob, 131, 146 ) ); // 75,12
     antiAliasNRPN.decode( Arrays.copyOfRange( patchBlob, 146, 149 ) );  // 79,3
     antiAliasSwitch    = patchBlob[149];
     antiAliasCutoff    = patchBlob[150];
     antiAliasKeyFollow = patchBlob[151];
     noiseNRPN.decode( Arrays.copyOfRange( patchBlob, 152, 155 ) );    // 80,3
     noiseTime    = patchBlob[155];
     noiseBreath  = patchBlob[156];
     noiseLevel   = patchBlob[157];
     miscNRPN.decode( Arrays.copyOfRange( patchBlob, 158, 161 ) );   // 81,10
     bendRange    = patchBlob[161];
     bendStepMode = patchBlob[162];
     biteVibrato  = patchBlob[163];
     oscFilterLink = patchBlob[164];
     noiseFilterLink = patchBlob[165];
     formantFilter   = patchBlob[166];
     osc2Xfade       = patchBlob[167];
     keyTrigger      = patchBlob[168];
     filler10        = patchBlob[169];
     chorusSwitch    = patchBlob[170];
     ampNRPN.decode( Arrays.copyOfRange( patchBlob, 171, 174 ) );    // 88,3
     biteTremolo    = patchBlob[174];
     ampLevel	    = (int) patchBlob[175];
     octaveLevel    = (int) patchBlob[176];
     chorusNRPN.decode( Arrays.copyOfRange( patchBlob, 177, 180 ) );   // 112,9
     chorusDelay1   = patchBlob[180];
     chorusModLev1  = patchBlob[181];
     chorusWetLev1  = patchBlob[182];
     chorusDelay2   = patchBlob[183];
     chorusModLev2  = patchBlob[184];
     chorusWetLev2  = patchBlob[185];
     chorusFeedback = patchBlob[186];
     chorusLFOfreq  = patchBlob[187];
     chorusDryLevel = patchBlob[188];
     delayNRPN.decode( Arrays.copyOfRange( patchBlob, 189, 192 ) );    // 113,5
     delayTime      = patchBlob[192];
     delayFeedback  = patchBlob[193];
     delayDamp      = patchBlob[194];
     delayLevel     = patchBlob[195];
     delayDry       = patchBlob[196];   // ZJ
     reverbNRPN.decode( Arrays.copyOfRange( patchBlob, 197, 200 ) );   // 114,5
     reverbDry      = patchBlob[200];    // ZJ
     reverbLevel    = patchBlob[201];
     reverbDensity  = patchBlob[202];
     reverbTime     = patchBlob[203];
     reverbDamp     = patchBlob[204];
     trailer_f7     = patchBlob[205];   // 0xf7 !!!  
     
     setEmpty( false );
  }
  
  // populate the blob from each of the individual properties
  void encodeBlob() {
    int ix;
    byte[] tmpBytes;
    
    for (ix = 0; ix < 4; ix++) patchBlob[ix] = header[ix];
    patchBlob[4] = mode;     // 0x00 to store, 0x20 to edit
    patchBlob[5] = (byte) patchNum; 
    patchBlob[6] = filler2;
    patchBlob[7] = filler3;
    patchBlob[8] = filler4;
    patchBlob[9] = filler5;
    patchBlob[10] = filler6;
    patchBlob[11] = filler7;
    for (ix = 0; ix < EWI_PATCHNAME_LENGTH; ix++) patchBlob[12+ix] = (byte) name[ix];
    tmpBytes = osc1.toBytes(); for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[44+ix] = tmpBytes[ix];
    tmpBytes = osc2.toBytes(); for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[65+ix] = tmpBytes[ix];
    tmpBytes = oscFilter1.toBytes(); for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[86+ix] = tmpBytes[ix];
    tmpBytes = oscFilter2.toBytes(); for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[101+ix] = tmpBytes[ix];
    tmpBytes = noiseFilter1.toBytes(); for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[116+ix] = tmpBytes[ix];
    tmpBytes = noiseFilter2.toBytes(); for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[131+ix] = tmpBytes[ix];
    tmpBytes = antiAliasNRPN.toBytes();  for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[146+ix] = tmpBytes[ix]; 
    patchBlob[149] = (byte) antiAliasSwitch   ;
    patchBlob[150] = (byte) antiAliasCutoff   ;
    patchBlob[151] = (byte) antiAliasKeyFollow;
    tmpBytes = noiseNRPN.toBytes();  for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[152+ix] = tmpBytes[ix];
    patchBlob[155] = (byte) noiseTime   ;
    patchBlob[156] = (byte) noiseBreath ;
    patchBlob[157] = (byte) noiseLevel  ;
    tmpBytes = miscNRPN.toBytes();  for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[158+ix] = tmpBytes[ix];
    patchBlob[161] = (byte) bendRange   ;
    patchBlob[162] = (byte) bendStepMode;
    patchBlob[163] = (byte) biteVibrato ;
    patchBlob[164] = (byte) oscFilterLink;
    patchBlob[165] = (byte) noiseFilterLink;
    patchBlob[166] = (byte) formantFilter  ;
    patchBlob[167] = (byte) osc2Xfade      ;
    patchBlob[168] = (byte) keyTrigger     ;
    patchBlob[169] = (byte) filler10       ;
    patchBlob[170] = (byte) chorusSwitch   ;
    tmpBytes = ampNRPN.toBytes();  for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[171+ix] = tmpBytes[ix];
    patchBlob[174] = (byte) biteTremolo   ;
    patchBlob[175] = (byte) ampLevel    ;
    patchBlob[176] = (byte) octaveLevel   ;
    tmpBytes = chorusNRPN.toBytes();  for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[177+ix] = tmpBytes[ix];
    patchBlob[180] = (byte) chorusDelay1  ;
    patchBlob[181] = (byte) chorusModLev1 ;
    patchBlob[182] = (byte) chorusWetLev1 ;
    patchBlob[183] = (byte) chorusDelay2  ;
    patchBlob[184] = (byte) chorusModLev2 ;
    patchBlob[185] = (byte) chorusWetLev2 ;
    patchBlob[186] = (byte) chorusFeedback;
    patchBlob[187] = (byte) chorusLFOfreq ;
    patchBlob[188] = (byte) chorusDryLevel;
    tmpBytes = delayNRPN.toBytes();  for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[189+ix] = tmpBytes[ix];
    patchBlob[192] = (byte) delayTime     ;
    patchBlob[193] = (byte) delayFeedback ;
    patchBlob[194] = (byte) delayDamp     ;
    patchBlob[195] = (byte) delayLevel    ;
    patchBlob[196] = (byte) delayDry      ;
    tmpBytes = reverbNRPN.toBytes();  for (ix = 0; ix < tmpBytes.length; ix++) patchBlob[197+ix] = tmpBytes[ix];
    patchBlob[200] = (byte) reverbDry     ;
    patchBlob[201] = (byte) reverbLevel   ;
    patchBlob[202] = (byte) reverbDensity ;
    patchBlob[203] = (byte) reverbTime    ;
    patchBlob[204] = (byte) reverbDamp    ;
    patchBlob[205] = trailer_f7    ;
 }
  
  void nameToBlob() {
    for (int ix = 12; ix < (12+EWI_PATCHNAME_LENGTH); ix++)
      patchBlob[ix] = (byte) name[ix - 12];
  }
    
  public static String toHex( byte[] blob, boolean addSpaces ) {
    String fmt, hexStr = "";
    if (blob.length < EWI_PATCH_LENGTH) return "Too short";
    if (blob.length > EWI_PATCH_LENGTH) return "Too long";
    if (addSpaces)
      fmt = "%02x ";
    else
      fmt = "%02x";
    for (int b = 0; b < EWI_PATCH_LENGTH; b++) {
      hexStr = hexStr + String.format( fmt, blob[b] );
    }
    return hexStr;
  }
  
  public String toHex() {
    return toHex( patchBlob, false );
  }
  
  public Status save( String filename ) throws IOException {
    UserPrefs prefs = new UserPrefs();
    Path path = Paths.get( prefs.getExportLocation(), filename );
    if (Files.exists( path )) return Status.ALREADY_EXISTS;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    bos.write( patchBlob );
    byte[] bytes = bos.toByteArray();
    try {
      Files.write( path, bytes );
    } catch( FileAlreadyExistsException e) {
      return Status.ALREADY_EXISTS;
    } catch( IOException e ) {
      return Status.NO_PERMISSION;
    }
    return Status.OK;
  }

}

