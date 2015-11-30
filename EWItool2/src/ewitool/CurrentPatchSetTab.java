package ewitool;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class CurrentPatchSetTab extends Tab {
  
  Button[] patchButtons;
  
  CurrentPatchSetTab() {
    
    setText( "Current Patch Set" );
    setClosable( false );
    
    GridPane gp = new GridPane();
    gp.setPadding( new Insets( 10.0 ) );
    gp.setVgap( 3.0 ); // i.e. minimal vertical gap between buttons
    
    ColumnConstraints[] ccs;
    ccs = new ColumnConstraints[ 2 ];
    
    // Label cols are constrained to <=30 pixels wide
    ccs[0] = new ColumnConstraints( 10.0, 30.0, 30.0 );
    ccs[0].setHalignment( HPos.CENTER );
    // Button cols can grow indefinitely
    ccs[1] = new ColumnConstraints( 40.0, 90.0, Double.MAX_VALUE );
    ccs[1].setHgrow( Priority.ALWAYS );
    gp.getColumnConstraints().addAll( ccs[0], ccs[1] );
    gp.getColumnConstraints().addAll( ccs[0], ccs[1] );
    gp.getColumnConstraints().addAll( ccs[0], ccs[1] );
    gp.getColumnConstraints().addAll( ccs[0], ccs[1] );
    gp.getColumnConstraints().addAll( ccs[0], ccs[1] );
    
    RowConstraints rc;
    rc = new RowConstraints();
    rc.setVgrow( Priority.ALWAYS );
    
    patchButtons = new Button[100];
    
    for (int r = 0; r < 20; r++) {
      gp.getRowConstraints().add( rc );
      for (int c = 0; c < 5; c++) {
        int ix = (c * 20 ) + r;
        gp.add( new Label( Integer.toString( ix ) ), c * 2, r );
        patchButtons[ix] = new Button( "<Empty>" );  // TODO Create method to do this
        patchButtons[ix].setMinWidth( 40.0 );
        patchButtons[ix].setPrefWidth( 90.0 );
        patchButtons[ix].setMaxWidth( Double.MAX_VALUE );
        gp.add( patchButtons[ix], (c * 2 ) + 1, r );
      }
    }
    
    setContent( gp );   
  }

}
