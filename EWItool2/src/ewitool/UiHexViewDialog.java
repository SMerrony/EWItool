package ewitool;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.stage.StageStyle;

public class UiHexViewDialog extends Dialog<ButtonType> {
  
  UiHexViewDialog( byte[] patchBlob ) {
    
    // setId( "hex-view-dialog" );
    
    setTitle( "EWItool - Patch in Hex" );
    initStyle(StageStyle.UTILITY);
    getDialogPane().getButtonTypes().add( ButtonType.OK );
    TextArea hexArea = new TextArea( EWI4000sPatch.toHex( patchBlob, true ) );
    hexArea.setPrefColumnCount( 62 );
    hexArea.setWrapText( true );
    getDialogPane().setContent( hexArea );
  }

}
