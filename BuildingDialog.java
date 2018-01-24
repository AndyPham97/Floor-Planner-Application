import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Optional;

public class BuildingDialog extends Dialog{
    private DirectoryDialog directoryDialog;
    public BuildingDialog(String title, Building o, int num, int num2, int num3) {
        setTitle(title);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,10,10,10));

        Label numFloorsLabel = new Label("Num Floors:");
        TextField numFloorsField = new TextField();
        numFloorsField.setText("" + o.getFloorPlans().length);
        numFloorsField.setEditable(false);
        grid.add(numFloorsLabel, 0, 0,1,1);
        grid.add(numFloorsField, 1,0,1,1);

        Label numExitsLabel = new Label("Num Exits:");
        TextField numExitsField = new TextField();
        numExitsField.setText("" + num3);
        numExitsField.setEditable(false);
        grid.add(numExitsLabel, 0, 1,1,1);
        grid.add(numExitsField, 1, 1,1,1);

        Label numRoomsLabel = new Label("Num Rooms:");
        TextField numRoomsField = new TextField();
        numRoomsField.setText("" + num);
        numRoomsField.setEditable(false);
        grid.add(numRoomsLabel, 0, 2,1,1);
        grid.add(numRoomsField, 1, 2,1,1);

        Label totalSizeLabel = new Label("Floor Size:");
        TextField totalSizeField = new TextField();
        totalSizeField.setText(num2 + " Sq. Ft.");
        totalSizeField.setEditable(false);
        grid.add(totalSizeLabel, 0, 3,1,1);
        grid.add(totalSizeField, 1, 3, 1,1);

        Button directoryListing = new Button("Building Directory");
        grid.add(directoryListing, 0, 4, 2, 1);
        getDialogPane().setContent(grid);

        directoryListing.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                directoryDialog = new DirectoryDialog("Directory Listing", o, num);
                Optional<Room> result = directoryDialog.showAndWait();
            }
        });
    }
}
