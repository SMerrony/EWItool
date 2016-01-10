package ewitool;

public class SendMsg {
  
  public static enum MidiMsgType { SYSEX, CC, SYSTEM_RESET };
  public static enum DelayType { NONE, SHORT, LONG };
  
  public MidiMsgType msgType;
  public DelayType delay;
  
  // sysex body...
  public byte[]  bytes;

  // control change properties...
  public int channel;
  public int cc;
  public int value;
  
  SendMsg() {
    delay = DelayType.NONE;
  }
}
