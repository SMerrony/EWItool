package ewitool;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class CurrentPatchSetTab extends Tab {

  Button[] patchButtons;
  Button saveButton;
  SharedData sharedData;
  ScratchPad scratchPad;

  CurrentPatchSetTab( SharedData pSharedData, ScratchPad pScratchPad ) {

    setText( "Current Patch Set" );
    setClosable( false );
    
    sharedData = pSharedData;
    scratchPad = pScratchPad;

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
        patchButtons[ix].setUserData( ix );  // store the patch number 
        patchButtons[ix].setMinWidth( 40.0 );
        patchButtons[ix].setPrefWidth( 90.0 );
        patchButtons[ix].setMaxWidth( Double.MAX_VALUE );
        patchButtons[ix].setOnAction( new PatchButtonEventHandler() );
        gp.add( patchButtons[ix], (c * 2 ) + 1, r );
      }
    }
    
    saveButton = new Button( "Save to Library" );
    gp.add( saveButton, 5, 20 );

    setContent( gp );   
  }

  public void updateLabels() { 
    for (int p = 0; p <EWI4000sPatch.EWI_NUM_PATCHES; p++ ) {
      patchButtons[p].setText( sharedData.ewiPatchList.get( p ).getName() );
    }
  }

  // The Patch Actions dialog that appears when a button is pressed
  class PatchButtonEventHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle( ActionEvent ae ) {

      Dialog<ButtonType> dialog = new Dialog<ButtonType>();
      dialog.setTitle( "EWItool - Patch Actions" );
      String buttonText = ((Button)ae.getSource()).getText();
      ButtonType editType = new ButtonType( "Edit" );
      dialog.getDialogPane().getButtonTypes().add( editType );
      ButtonType copyType = new ButtonType( "Copy to Scratchpad" );
      if (buttonText != "<Empty>") dialog.getDialogPane().getButtonTypes().add( copyType );
      ButtonType replaceType = new ButtonType( "Replace from Scratchpad" );
      dialog.getDialogPane().getButtonTypes().add( replaceType );
      ButtonType renameType = new ButtonType( "Rename" );
      if (buttonText != "<Empty>") dialog.getDialogPane().getButtonTypes().add( renameType );
      dialog.getDialogPane().getButtonTypes().add( ButtonType.CANCEL );
      GridPane gp = new GridPane();
      gp.add( new Label( "Patch:" ), 0, 0 );
      TextField nameField = new TextField( ((Button)ae.getSource()).getText() );
      gp.add( nameField, 1, 0 );
      gp.add( new Label( "Scratchpad:" ), 2, 0 );
      ChoiceBox< String > spChoice = new ChoiceBox< String >();
      for (int p = 0; p < scratchPad.patchList.size(); p++) {
        spChoice.getItems().add( scratchPad.patchList.get( p ).getName() );
      }
      gp.add( spChoice, 3, 0 );
           
      dialog.getDialogPane().setContent( gp );
      
      Optional<ButtonType> rc = dialog.showAndWait();
      
      if (rc.get() == editType) {
        sharedData.setEditingPatchNumber( (int) ((Button)ae.getSource()).getUserData() );
        // editor tab should detect the change and self-activate
      } else if ( rc.get() == copyType) {
        
      } else if ( rc.get() == replaceType) {
        if (spChoice.getSelectionModel().getSelectedIndex() >= 0) {
          int pNum = (int) ((Button)ae.getSource()).getUserData();
          sharedData.ewiPatchList.set( pNum, scratchPad.patchList.get( spChoice.getSelectionModel().getSelectedIndex() ) );
          sharedData.setLastPatchLoaded( pNum );
        } else {
          Alert alert = new Alert( AlertType.WARNING, "No Patch selected from Scratchpad list" );
          alert.showAndWait();
        }
        
      } else if ( rc.get() == renameType) {
        int pNum = (int) ((Button)ae.getSource()).getUserData();
        if (sharedData.ewiPatchList.get( pNum ).setName( nameField.getText() )) {
          patchButtons[pNum].setText( nameField.getText() );
          Alert alert = new Alert( AlertType.INFORMATION, "Patch renamed" );
          alert.setTitle( "EWItool - Rename Patch" );
          alert.setHeaderText( "" );
          alert.showAndWait();
        } else {
          Alert alert = new Alert( AlertType.WARNING, "Could not rename to '" + nameField.getText() + "'" );
          alert.setTitle( "EWItool - Rename Patch" );
          alert.setHeaderText( "" );
          alert.showAndWait();
        }       
      }
    }
  }
  
}
