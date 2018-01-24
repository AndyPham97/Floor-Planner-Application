import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;


public class RoomInfoDialog extends Dialog {
    public RoomInfoDialog(String title, Room r, String c, String n) {
        setTitle(title);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,10,10,10));

        Label occupantLabel = new Label("Occupant:");
        TextField occupantField = new TextField();
        occupantField.setPromptText("Person who occupies this room");
        grid.add(occupantLabel, 0, 0,1,1);
        grid.add(occupantField, 1,0,2,1);

        Label positionLabel = new Label("Position:");
        TextField positionField = new TextField();
        positionField.setPromptText("Job position/title of this person");
        grid.add(positionLabel, 0, 1,1,1);
        grid.add(positionField, 1, 1,2,1);

        Label numberLabel = new Label("Number:");
        TextField numberField = new TextField();
        numberField.setPromptText("The room number");
        Button roomColour = new Button();
        roomColour.setPrefWidth(160);
        roomColour.setStyle("-fx-base: " + c);
        roomColour.setFocusTraversable(false);
        grid.add(numberLabel, 0, 2,1,1);
        grid.add(numberField, 1, 2,1,1);
        grid.add(roomColour, 2,2 ,1,1);

        Label floorLabel = new Label("Floor:");
        TextField floorField = new TextField();
        floorField.setText(n);
        floorField.setEditable(false);
        grid.add(floorLabel, 0, 3,1,1);
        grid.add(floorField, 1, 3, 2,1);

        Label sizeLabel = new Label("Size");
        TextField sizeField = new TextField();
        sizeField.setText(r.getNumberOfTiles() + " Sq. Ft.");
        sizeField.setEditable(false);
        grid.add(sizeLabel, 0, 4,1,1);
        grid.add(sizeField, 1, 4,2,1);
        getDialogPane().setContent(grid);

        setResultConverter(new Callback<ButtonType, Room>() {
            public Room call(ButtonType b) {
                if (b == okButtonType) {
                    r.setOccupant(occupantField.getText());
                    r.setPosition(positionField.getText());
                    r.setNumber(numberField.getText());
                    return r;
                }
                return null;
            }
        });

    }

}
