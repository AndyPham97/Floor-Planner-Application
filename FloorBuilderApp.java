import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.util.Optional;

public class FloorBuilderApp extends Application  {
    private FloorBuildingView   view;
    private Building           model;
    private Dialog roomDetails;
    private Dialog buildingOverview;
    private int                currentFloor;    // Index of the floor being displayed
    private int                currentColor;    // Index of the current room color

    public void start(Stage primaryStage) {
        model = Building.example();
        currentFloor = 0;
        currentColor = 0;

        VBox aPane = new VBox();
        view = new FloorBuildingView(model);
        view.setPrefWidth(Integer.MAX_VALUE);
        view.setPrefHeight(Integer.MAX_VALUE);

        aPane.getChildren().add(view);
        primaryStage.setTitle("Floor Plan Builder");
        primaryStage.setScene(new Scene(aPane, 370,340));
        primaryStage.show();

        // Plug in the floor panel event handlers:
        for (int r=0; r<model.getFloorPlan(0).size(); r++) {
            for (int c=0; c<model.getFloorPlan(0).size(); c++) {
                view.getFloorTileButton(r, c).setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        handleTileSelection(event);
                    }});
            }
        }

        // Plug in the radioButton event handlers
        view.getEditWallsButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                view.update(currentFloor, currentColor);
            }});
        view.getSelectExitsButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                view.update(currentFloor, currentColor);
            }});
        view.getEditRoomsButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                view.update(currentFloor, currentColor);
            }});
        view.getDefineRoomsButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                view.update(currentFloor, currentColor);
            }});
        view.getBuildingOverviewButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                handleBuildingDirectory(event);
                view.update(currentFloor, currentColor);
            }
        });

        // Plug in the office color button
        view.getRoomColorButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentColor = (currentColor + 1)%view.ROOM_COLORS.length;
                view.update(currentFloor, currentColor);
            }});

        //Choose what floor to see
        view.getBasementItem().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloor = 4;
                view.update(currentFloor, currentColor);
            }
        });

        view.getMainFloorItem().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloor = 0;
                view.update(currentFloor, currentColor);
            }
        });

        view.getSecondFloorItem().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloor = 1;
                view.update(currentFloor, currentColor);
            }
        });

        view.getThirdFloorItem().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloor = 2;
                view.update(currentFloor, currentColor);
            }
        });

        view.getFourthFloorItem().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloor = 3;
                view.update(currentFloor, currentColor);
            }
        });



        view.update(currentFloor, currentColor);
    }



    // Handle a Floor Tile Selection
    private void handleTileSelection(ActionEvent e) {
        // Determine which row and column was selected
        int r=0, c=0;
        OUTER:
        for (r=0; r<model.getFloorPlan(0).size(); r++) {
            for (c=0; c<model.getFloorPlan(0).size(); c++) {
                if (e.getSource() == view.getFloorTileButton(r, c))
                    break OUTER;
            }
        }

        // Check if we are in edit wall mode, then toggle the wall
        if (view.getEditWallsButton().isSelected()) {
            model.getFloorPlan(currentFloor).setWallAt(r, c, !model.getFloorPlan(currentFloor).wallAt(r, c));
            // Remove this tile from the room if it is on one, because it is now a wall
            Room room = model.getFloorPlan(currentFloor).roomAt(r, c);
            if (room != null)
                room.removeTile(r, c);
        }

        // Otherwise check if we are in edit exits mode
        else if (view.getSelectExitsButton().isSelected()) {
            if (model.exitAt(r, c) != null)
                model.removeExit(r, c);
            else {
                model.addExit(r, c);
                // Remove this tile from the room if it is on one, because it is now an exit
                Room off = model.getFloorPlan(currentFloor).roomAt(r, c);
                if (off != null)
                    off.removeTile(r, c);
            }
        }

        // Otherwise check if we are selecting a room tile
        else if (view.getEditRoomsButton().isSelected()) {
            if (!model.getFloorPlan(currentFloor).wallAt(r, c) && !model.hasExitAt(r, c)) {
                Room room = model.getFloorPlan(currentFloor).roomAt(r, c);
                if (room != null) {
                    room.removeTile(r, c);
                    if (room.getNumberOfTiles() == 0)
                        model.getFloorPlan(currentFloor).removeRoom(room);
                }
                else {
                    room = model.getFloorPlan(currentFloor).roomWithColor(currentColor);
                    if (room == null) {
                        room = model.getFloorPlan(currentFloor).addRoomAt(r, c);
                        room.setColorIndex(currentColor);
                    }
                    else {
                        room.addTile(r, c);
                    }
                }
            }
        }

        else if (view.getDefineRoomsButton().isSelected()) {
            //Invalid Selection dialog
            if (model.getFloorPlan(currentFloor).roomAt(r,c) == null) {
                Alert invalidSelection = new Alert(Alert.AlertType.ERROR);
                invalidSelection.setTitle("Invalid Selection");
                invalidSelection.setHeaderText(null);
                invalidSelection.setContentText("You must select a tile that belongs to a room.");
                invalidSelection.showAndWait();
            }
            else {
                Room occupied = model.getFloorPlan(currentFloor).roomAt(r,c);
                String roomColour = view.ROOM_COLORS[occupied.getColorIndex()];
                String floorName = model.getFloorPlan(currentFloor).getName();
                if (occupied.getOccupant() == null || occupied.getPosition() == null || occupied.getNumber() == null) {
                    roomDetails = new RoomInfoDialog("Room Details", occupied, roomColour, floorName);
                    Optional<Room> result = roomDetails.showAndWait();
                    if (result.isPresent())
                        view.update(currentFloor, currentColor);
                } else {
                    Optional<Room> result = roomDetails.showAndWait();
                    if (result.isPresent())
                        view.update(currentFloor, currentColor);
                }
            }
        }

        // Otherwise do nothing
        else {
        }

        view.update(currentFloor, currentColor);
    }

    public void handleBuildingDirectory(ActionEvent e) {
        int numRooms = 0, numTiles = 0, numExits = 0;
        for(int r = 0; r < model.getFloorPlan(0).size(); r++) {
            for(int c = 0; c < model.getFloorPlan(0).size(); c++) {
                for(int i = 0; i < 5; i++) {
                    if (!model.getFloorPlan(i).wallAt(r,c)) {
                        numTiles++;
                    }
                    if (model.hasExitAt(r,c) && i == 0)
                        numExits++;
                }
            }
        }

        for(int i = 0; i < 5; i++) {
            numRooms += model.getFloorPlan(i).getNumberOfRooms();
        }

        buildingOverview = new BuildingDialog("Building Overview", model, numRooms, numTiles, numExits);
        Optional<Building> result = buildingOverview.showAndWait();
        if (result.isPresent())
            view.update(currentFloor, currentColor);


    }


    public static void main(String[] args) {
        launch(args);
    }
}