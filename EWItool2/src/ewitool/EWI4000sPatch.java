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

import java.util.Arrays;
import java.util.Observable;

import javafx.beans.property.SimpleIntegerProperty;

public class EWI4000sPatch extends Observable {
  
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
  }
  
  class Osc {
    Nrpn nrpn;
    byte octave;     // int 64 +/-2
    byte semitone;   // int 64 +/-12
    byte fine;  // int -50 - +50 cents
    byte beat;  // int 0% - 100%
    byte filler1;    // this really is unused (for firmware 2.3 anyway)
    byte sawtooth;   // %
    byte triangle;   // %
    byte square;     // %
    byte pulseWidth; // %
    byte pwmFreq;    // %
    byte pwmDepth;   // %
    byte sweepDepth; // %
    byte sweepTime;  // %
    byte breathDepth;     // %  ?-50/+50?
    byte breathAttain;    // %
    byte breathCurve;     // %  ? =50+50?
    byte breathThreshold; // %
    byte level;      // %
    
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
  }
  
  class Filter {
    Nrpn nrpn;
    byte mode;
    byte freq;
    byte q;
    byte keyFollow;
    byte breathMod;
    byte lfoFreq;
    byte lfoDepth;
    byte lfoBreath;
    byte lfoThreshold;
    byte sweepDepth;
    byte sweepTime;
    byte breathCurve;  
    
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
  }
  
  byte patchBlob[];
  
  byte header[];
  byte mode;     // 0x00 to store, 0x20 to edit
  byte patchNum;
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
  byte antiAliasSwitch;
  byte antiAliasCutoff;
  byte antiAliasKeyFollow;
  Nrpn noiseNRPN;    // 80,3
  byte noiseTime;
  byte noiseBreath;
  byte noiseLevel;
  Nrpn miscNRPN;   // 81,10
  byte bendRange;
  byte bendStepMode;
  byte biteVibrato;
  byte oscFilterLink;
  byte noiseFilterLink;
  byte formantFilter;
  byte osc2Xfade;
  byte keyTrigger;
  byte filler10;
  byte chorusSwitch;
  Nrpn ampNRPN;    // 88,3
  byte biteTremolo;
  SimpleIntegerProperty ampLevel;
  SimpleIntegerProperty octaveLevel;
  Nrpn chorusNRPN;   // 112,9
  byte chorusDelay1;
  byte chorusModLev1;
  byte chorusWetLev1;
  byte chorusDelay2;
  byte chorusModLev2;
  byte chorusWetLev2;
  byte chorusFeedback;
  byte chorusLFOfreq;
  byte chorusDryLevel;
  Nrpn delayNRPN;    // 113,5
  byte delayTime;
  byte delayFeedback;
  byte delayDamp;
  byte delayLevel;
  byte delayMix;   // ZJ
  Nrpn reverbNRPN;   // 114,5
  byte reverbMix;    // ZJ
  byte reverbLevel;
  byte reverbDensity;
  byte reverbTime;
  byte reverbDamp;
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
    ampLevel = new SimpleIntegerProperty();
    octaveLevel = new SimpleIntegerProperty();
    setEmpty( true );
  }
  
  EWI4000sPatch( byte[] blob ) {
    this();
    patchBlob = blob;
    setEmpty( false );
    decodeBlob();
  }
  
  public boolean isEmpty() {
    return empty;
  }

  public void setEmpty( boolean empty ) {
    this.empty = empty;
    setChanged();
    notifyObservers();
  }
  
  // the patch name is stored in the patch as a space-padded char[]
  // so it must be converted for use as a normal string
  public String getName() {
    return new String( name ).trim();
  }
  
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
     ampLevel.set( (int) patchBlob[175] );
     octaveLevel.setValue( (int) patchBlob[176] );
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
     delayMix       = patchBlob[196];   // ZJ
     reverbNRPN.decode( Arrays.copyOfRange( patchBlob, 197, 200 ) );   // 114,5
     reverbMix      = patchBlob[200];    // ZJ
     reverbLevel    = patchBlob[201];
     reverbDensity  = patchBlob[202];
     reverbTime     = patchBlob[203];
     reverbDamp     = patchBlob[204];
     trailer_f7     = patchBlob[205];   // 0xf7 !!!  
     
     setEmpty( false );

  }
  
  void nameToBlob() {
    for (int ix = 12; ix < (12+EWI_PATCHNAME_LENGTH); ix++)
      patchBlob[ix] = (byte) name[ix - 12];
  }
  
  void encodeBlob() {
    
  }
}
