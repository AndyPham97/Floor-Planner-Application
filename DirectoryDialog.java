import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

import java.util.Optional;

public class DirectoryDialog extends Dialog {
    public DirectoryDialog(String title, Building o, int num) {
        setTitle(title);

        ListView<String> directoryList = new ListView<String>();
        directoryList.setPrefSize(500, 200);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Button searchButton = new Button("Search");
        searchButton.setPrefSize(500, 25);
        grid.add(searchButton, 0, 1, 1, 1);

        Room[] rooms = new Room[num];

        int counter = 0;
        for (int i = 0; i < 5; i++) {
            for (int color = 0; color < 12; color++) {
                if (o.getFloorPlan(i).roomWithColor(color) != null) {
                    Room occupied = o.getFloorPlan(i).roomWithColor(color);
                    rooms[counter++] = occupied;
                }
            }
        }

        String[] occupants = new String[rooms.length];
        String[] positions = new String[rooms.length];
        String[] roomNumbers = new String[rooms.length];
        String[] string = new String[rooms.length];
        for (int i = 0; i < rooms.length; i++) {
            occupants[i] = rooms[i].getOccupant();
            positions[i] = rooms[i].getPosition();
            roomNumbers[i] = rooms[i].getNumber();
            string[i] = roomNumbers[i] + " - " + occupants[i] + " (" + positions[i] + ")";
        }
        directoryList.setItems(FXCollections.observableArrayList(string));
        grid.add(directoryList, 0, 0, 1, 1);
        getDialogPane().setContent(grid);

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Input Required");
                dialog.setHeaderText(null);
                dialog.setContentText("Please enter the full name of the person that you are searching for:");
                Optional <String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    boolean search = false;
                    for(int i  =0; i < 5; i++) {
                        for(int color = 0; color < 12; color++) {
                            if (o.getFloorPlan(i).roomWithColor(color) != null) {
                                Room found = o.getFloorPlan(i).roomWithColor(color);
                                if (result.get().equals(found.getOccupant())) {
                                    search = true;
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Search Results");
                                    alert.setHeaderText(null);
                                    alert.setContentText(found.getOccupant() + " is our " + found.getPosition() + " and can be located on the " +
                                            o.getFloorPlan(i).getName() + " in room " + found.getNumber());
                                    alert.showAndWait();
                                    break;
                                }
                                else
                                    search = false;
                            }
                        }
                    }

                    if (!search) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Search Results");
                        alert.setHeaderText(null);
                        alert.setContentText("That name does not match anyone in our records, please try again.");
                        alert.showAndWait();
                    }


                }
            }
        });
    }
}
