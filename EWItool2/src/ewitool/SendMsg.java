package ewitool;

public class SendMsg {
  
  public static enum MsgType { SYSEX, CC, SYSTEM_RESET };
  
  public MsgType msgType;
  
  // sysex body...
  public byte[]  bytes;

  // control change properties...
  int channel;
  int cc;
  int value;
}
