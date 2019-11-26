package eg.edu.alexu.csd.oop.cs71.db;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Gui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private TableView<Object> table = new TableView();

    static String success="";
    @Override
    public void start(Stage stage) {
        Main.startUp();
        table.setEditable(true);
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(300);
        stage.setHeight(500);
        TextField textField =new TextField();
        Button button =new Button("Run");
        Facade facade =new Facade();
        Label currentDb=new Label();
        Label rowNum=new Label();
        button.setOnAction(e->{
            String query=textField.getText();
            //query = query.replaceAll("( )+", " ");
            query=query.replaceAll(";","");
            if(facade.validateQuery(query))
            {
                Object object;
                object=facade.parse(query);
                currentDb.setText("Database: "+facade.engine.currentDatabase);
                query=query.toLowerCase();
                if(query.contains("select")) {
                    rowNum.setText("");
                    table.getColumns().clear();
                    Object[][] x = (Object[][]) object;
                    final ObservableList<Object> data = FXCollections.observableArrayList();
                    for(int i  =1 ; i < x.length;i++){
                        for(int j=0;j<x[i].length;j++)
                        {

                        }
                        data.add(x[i]);
                    }
                    for (int i = 0; i < x[0].length; i++)
                    {
                        TableColumn Col = new TableColumn();
                        Col.setText((String)x[0][i]);
                        Col.setCellValueFactory(new PropertyValueFactory<Object, Object>(x[0][i].toString()));
                        table.getColumns().add(Col);
                    }
                    table.setItems(data);
                }else if(!(query.contains("create")||query.contains("drop")||query.contains("use")))
                {
                    if((int)object!=-1)
                    {
                        rowNum.setText("Rows Number: "+object.toString());
                    }
                }
                else rowNum.setText("");

                if(!(success.equals(""))|| object == null)
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(success);
                    alert.setContentText("Please try again!");
                    success="";
                    alert.showAndWait();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid query");
                alert.setContentText("Please try again!");
                alert.showAndWait();
            }
        });



        //-LOOP ON TABLE COLUMNS-
        //https://docs.oracle.com/javafx/2/ui_controls/table-view.htm



        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        HBox hBox=new HBox();
        hBox.setSpacing(100);
        hBox.getChildren().addAll(currentDb,rowNum);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(hBox,textField,button, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
        scene.setOnKeyPressed(ke -> {
            KeyCode kc = ke.getCode();
            if(kc.equals(KeyCode.ENTER))
            {
                button.fire();
            }
        });
    }
}
