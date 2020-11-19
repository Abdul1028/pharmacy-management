package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.model.DataSource;
import sample.model.Medicines;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

public class FXMLDocumentControllerTransaction {
    @FXML
    private TextField searchMed;
    @FXML
    private Button searchButton;
    @FXML
    private ListView<BorderPane> MedicineList;
    @FXML
    private Label totalCost;
    @FXML
    private Button proceedButton;
    @FXML
    private Label emptyLabel;
    private double totalCostValue = 0;
    public void initialize(){
        try {
            MedicineList.getItems().clear();
            String search = searchMed.getText();
            DataSource dataSource = new DataSource();
            dataSource.connectionOpen();
            dataSource.createMedicineList();
            dataSource.addToHash();
            for (Medicines m : DataSource.medicinesArrayList) {
                BorderPane bp = new BorderPane();
                HBox hbx = new HBox();

                Button minusButton = new Button("-");
                minusButton.setOnAction(this::onClickMinusButton);
                minusButton.setId(m.getName() + "m");

                Button plusButton = new Button("+");
                plusButton.setOnAction(this::onClickPlusButton);
                plusButton.setId(m.getName() + "p");


                Label quant = new Label(String.format("%2d ",DataSource.medicineHashMap.get(m.getName())));
                quant.setId(m.getName() + "q");

                Label medName = new Label(m.getName());

                hbx.getChildren().add(minusButton);
                hbx.getChildren().add(quant);
                hbx.getChildren().add(plusButton);


                bp.setLeft(medName);
                bp.setRight(hbx);
                MedicineList.getItems().add(bp);
            }
            dataSource.connectionClose();
        } catch (Exception e) {
            System.out.println("Exception:(onClickSearchMedicine) " + e.getMessage());
        }
    }
    public void onClickSearchMedicine(ActionEvent actionEvent) {
        try {
            MedicineList.getItems().clear();
            String search = searchMed.getText();
            DataSource dataSource = new DataSource();
            for (Medicines m : DataSource.medicinesArrayList) {
                if(m.getName().contains(search)){
                    BorderPane bp = new BorderPane();
                    HBox hbx = new HBox();

                    Button minusButton = new Button("-");
                    minusButton.setOnAction(this::onClickMinusButton);
                    minusButton.setId(m.getName() + "m");

                    Button plusButton = new Button("+");
                    plusButton.setOnAction(this::onClickPlusButton);
                    plusButton.setId(m.getName() + "p");


                    Label quant = new Label(String.format("%2d ",DataSource.medicineHashMap.get(m.getName())));
                    quant.setId(m.getName() + "q");

                    Label medName = new Label(m.getName());

                    hbx.getChildren().add(minusButton);
                    hbx.getChildren().add(quant);
                    hbx.getChildren().add(plusButton);


                    bp.setLeft(medName);
                    bp.setRight(hbx);
                    MedicineList.getItems().add(bp);
                }
            }
            dataSource.connectionClose();
        } catch (Exception e) {
            System.out.println("Exception:(onClickSearchMedicine) " + e.getMessage());
        }
    }

    private void onClickPlusButton(ActionEvent actionEvent) {
        try{
            String idMedName = actionEvent.getSource().toString().split("=")[1].split(",")[0].split("p")[0];
            DataSource dataSource = new DataSource();
            dataSource.connectionOpen();
            if(DataSource.medicineHashMap.get(idMedName)>9) return;
            totalCostValue += dataSource.getPrice(idMedName);
            Currency indiaCurrency = Currency.getInstance(new Locale("en","IN"));
            totalCost.setText("Total Cost - " + indiaCurrency.getSymbol() + " " + totalCostValue);
//            dataSource.decrementQuant(idMedName);
//            list.add(dataSource.getMed(idMedName));
            if(DataSource.medicineHashMap.get(idMedName)>9) return;
            int count = DataSource.medicineHashMap.get(dataSource.getMed(idMedName).getName());
            DataSource.medicineHashMap.replace(dataSource.getMed(idMedName).getName(),count+1);
            for(int i = 0;i<MedicineList.getItems().size();i++){
                if(((HBox)(MedicineList.getItems().get(i).getRight())).getChildren().get(1).getId().split("q")[0].equals(idMedName)){
                    ((Label)((HBox)(MedicineList.getItems().get(i).getRight())).getChildren().get(1)).setText(String.format("%2d ",DataSource.medicineHashMap.get(idMedName)));
                }
            }
        }catch (Exception e){
            System.out.println("Exception:(onClickPlusButton) " + e.getMessage());
        }
    }
    private void onClickMinusButton(ActionEvent actionEvent) {
        try{
            String idMedName = actionEvent.getSource().toString().split("=")[1].split(",")[0].split("m")[0];
            DataSource dataSource = new DataSource();
            dataSource.connectionOpen();
            if(totalCostValue==0)return;
            if(DataSource.medicineHashMap.get(idMedName)<1) return;
            totalCostValue -= dataSource.getPrice(idMedName);
            Currency indiaCurrency = Currency.getInstance(new Locale("en","IN"));
            totalCost.setText("Total Cost - " + indiaCurrency.getSymbol() + " " + totalCostValue);
//            dataSource.decrementQuant(idMedName);
//            list.add(dataSource.getMed(idMedName));
            int count = DataSource.medicineHashMap.get(dataSource.getMed(idMedName).getName());
            DataSource.medicineHashMap.replace(dataSource.getMed(idMedName).getName(),count-1);
            for(int i = 0;i<MedicineList.getItems().size();i++){
                if(((HBox)(MedicineList.getItems().get(i).getRight())).getChildren().get(1).getId().split("q")[0].equals(idMedName)){
                    ((Label)((HBox)(MedicineList.getItems().get(i).getRight())).getChildren().get(1)).setText(String.format("%2d ",DataSource.medicineHashMap.get(idMedName)));
                }
            }
        }catch (Exception e){
            System.out.println("Exception:(onClickPlusButton) " + e.getMessage());
        }
    }
        @FXML
    private void onClickProceed(ActionEvent actionEvent) throws IOException {
//        DataSource.val= list;
//            for (Medicines m:
//                    DataSource.val) {
//                System.out.println(m.getName() + "\t" + m.getMed_id()+ "\t"  + m.getQuantity());
//            }
//        try{
            Stage primaryStage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
            Parent root = FXMLLoader.load(getClass().getResource("Add Patient.fxml"));
            primaryStage.setTitle("Hello ");
            primaryStage.setScene(new Scene(root, 750, 600));
            primaryStage.show();
//        }catch (IOException exception){
//            System.out.println("Exception: (onClickProceed)" + exception);
//        }

    }
}