package com.example.atm;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;


public class HelloApplication extends Application {

    private TextField selectedTextField;
    private boolean in_user = false;
    private boolean in_money = false;
    Pane screenPane = new Pane();
    GridPane gridPane = new GridPane();
    TilePane tilePane = new TilePane(screenPane, gridPane); // the whole tilepane that contains all elements
    TextField text = new TextField();
    TextField text1 = new TextField();
    TextField text2 = new TextField();
    TextField text3 = new TextField();
    TextField text4 = new TextField();
    TextField enter_money = new TextField();
    Label success = new Label("Money has been successfully transferred!");

    public void start(Stage stage) throws IOException {
        startScreen();
        int count = 1; // count used for the numpad
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                Button button = new Button();
                button.setStyle("-fx-background-radius: 25px; -fx-focus-color: transparent;");
                // last four buttons on the numpad
                // and the ok, cancel, and clear buttons
                if(i == 3 && j != 3){
                    if(j == 0){
                        button.setText(".");
                    }else if(j == 1){
                        button.setText("0");
                    }else if (j == 2){
                        button.setText("00");
                    }
                }else if(j == 3){
                    if(i == 0){
                        button.setText("Enter");
                        button.setStyle("-fx-background-radius: 25px; -fx-background-color: #00FF00; -fx-focus-color: transparent;");
                    }else if(i == 1){
                        button.setText("Cancel");
                        button.setStyle("-fx-background-radius: 25px; -fx-background-color: #FF0000; -fx-focus-color: transparent;");
                    }else if (i == 2){
                        button.setText("Clear");
                        button.setStyle("-fx-background-radius: 25px; -fx-background-color: #FFFF00; -fx-focus-color: transparent;");
                    }else if(i == 3){
                        button.setText("Back");
                    }
                }else{
                    button.setText(Integer.toString(count));
                    count++;
                }
                // the ok, cancel, clear and ok buttons
                // styling the buttons and adding actions
                // changing the corner radius and removing the focus colour

                button.setFont(new Font(32.5)); // making the text larger
                button.setPrefSize(150, 100);
                if(button.getText() == "Clear"){
                    button.setOnAction(event -> {
                        if (selectedTextField != null) {
                            selectedTextField.setText(" ");
                        }
                    });
                }else if(button.getText() == "Cancel"){
                    button.setOnAction(event -> {
                        if (selectedTextField != null) {
                            StringBuilder existingText = new StringBuilder(selectedTextField.getText());
                            existingText.deleteCharAt(existingText.length() - 1);
                            selectedTextField.setText(String.valueOf(existingText));
                        }
                    });
                }else if(button.getText() == "Enter"){
                    button.setOnAction(event -> {
                        if (selectedTextField != null) {
                            String existingText = selectedTextField.getText();
                            if(selectedTextField.equals(text)){
                                    selectedTextField = text1;
                                    text1.requestFocus();
                            }else if(selectedTextField.equals(text1)){
                                    selectedTextField = text2;
                                    text2.requestFocus();
                            }else if(selectedTextField.equals(text2)){
                                    selectedTextField = text3;
                                    text3.requestFocus();
                            }else if(selectedTextField.equals(text3)){
                                existingText.replaceAll("\\s", "");
                                if(check_number(text.getText()) && check_date(text1.getText()) && check_cvv(text2.getText()) && existingText != ""){
                                    text.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                                    text1.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                                    text2.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                                    text3.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                                    if(in_money)enter_money.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                                    if(in_user){
                                        insert_user(text.getText(), text1.getText(), text2.getText(), existingText);
                                    }else if(in_money){
                                        selectedTextField = enter_money;
                                        enter_money.requestFocus();
                                    }
                                }else{
                                    Node a = tilePane.getChildren().get(0);
                                    try{
                                        if(in_user) ((Pane) a).getChildren().remove(14);
                                        else if(in_money) ((Pane) a).getChildren().remove(16);
                                    }catch(IndexOutOfBoundsException ddd){
                                        System.out.println("not inside");
                                    }
                                    text.setStyle("-fx-background-color: mistyrose; -fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                                    text1.setStyle("-fx-background-color: mistyrose; -fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                                    text2.setStyle("-fx-background-color: mistyrose; -fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                                    text3.setStyle("-fx-background-color: mistyrose; -fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                                    if(in_money) enter_money.setStyle("-fx-background-color: mistyrose; -fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                                }
                            }else if(selectedTextField.equals(enter_money)){
                                if(check_money(enter_money)){
                                    double money = Double.parseDouble(enter_money.getText());
                                    insert_money(text.getText(), text3.getText(), text1.getText(), text2.getText(), money);
                                }
                            }

                        }
                    });
                }else if(button.getText() == "Back"){
                    button.setOnAction(event -> {
                            System.out.println("hi");
                            startScreen();
                    });
                }else{
                    button.setOnAction(event -> {
                        if (selectedTextField != null) {
                            String existingText = selectedTextField.getText();
                            existingText = existingText + button.getText();
                            selectedTextField.setText(existingText);
                        }
                    });
                }
                gridPane.add(button, j, i);//adding the button to the gridpane
            }
        }

        // finishers
        gridPane.setHgap(10); // set horizontal gap to 10 pixels
        gridPane.setVgap(20);
        Scene scene = new Scene(tilePane, 650.0, 960.0);
        stage.setTitle("Luka's Ultimate ATM");
        stage.setScene(scene);
        stage.show();
        System.out.println(tilePane.getHeight());
        System.out.println(tilePane.getWidth());
    }
    private boolean check_money(TextField sup){

        String a = sup.getText();
        if(a.lastIndexOf(".") != a.indexOf(".")) return false;
        if(a.lastIndexOf(".") == 0) return false;
        for(int v = 0; v < a.length(); v++){
            if(!Character.isDigit(a.charAt(v)) && a.charAt(v) != '.') return false;
        }
        return true;
    }
    private boolean check_number(String existingText){
        existingText = existingText.replaceAll("\\s", "");
        if(existingText.length() != 16) return false;
        for(int k= 0; k < 16; k++){
            if(!Character.isDigit(existingText.charAt(k)) && k != 2) return false;
        }
        return true;
    }
    private boolean check_date(String existingText){
        existingText = existingText.replaceAll("\\s", "");
        if(existingText.length() == 5 && existingText.charAt(2) == '/'){
            for(int k= 0; k < 4; k++){
                if(!Character.isDigit(existingText.charAt(k)) && k != 2) return false;
            }
            return true;
        }else{
            return false;
        }
    }
    private boolean check_cvv(String existingText){
        existingText = existingText.replaceAll("\\s", "");
        if(existingText.length() == 3){
            for(int k= 0; k < 3; k++){
                if(!Character.isDigit(existingText.charAt(k))) return false;
            }
            return true;
        }else{
            return false;
        }
    }

    private void startScreen(){
        tilePane.getChildren().set(0, screenPane);
        in_money = false;
        in_user = false;
        Rectangle rec = new Rectangle(630, 100);
        rec.setFill(Color.WHITE);
        rec.setLayoutY(10);
        rec.setLayoutX(10);
        Label number = new Label("+995 599 552 945");
        Label number1 = new Label("+995 579 092 128");
        number.setFont(new Font(20));
        number1.setFont(new Font(20));
        Image image = new Image("C:\\Users\\Administrator\\IdeaProjects\\ATM\\src\\main\\resources\\4590204.png");
        Image image1 = new Image("C:\\Users\\Administrator\\IdeaProjects\\ATM\\src\\main\\resources\\306232.png");
        Image image2 = new Image("C:\\Users\\Administrator\\IdeaProjects\\ATM\\src\\main\\resources\\2560px-TBC_Bank_logo.svg.png");
        Image image3 = new Image("C:\\Users\\Administrator\\IdeaProjects\\ATM\\src\\main\\resources\\photo.png");
        Image image4 = new Image("C:\\Users\\Administrator\\IdeaProjects\\ATM\\src\\main\\resources\\phone.jpg");
        ImageView imageView = new ImageView(image);
        ImageView imageView1 = new ImageView(image1);
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView3 = new ImageView(image3);
        ImageView imageView4 = new ImageView(image4);
        number.setLayoutX(375);
        number.setLayoutY(31);
        number1.setLayoutX(375);
        number1.setLayoutY(56.5);
        imageView4.setFitHeight(100);
        imageView4.setFitWidth(200);
        imageView4.setLayoutY(100);
        imageView4.setLayoutX(420);
        imageView3.setFitWidth(60);
        imageView3.setFitHeight(60);
        imageView2.setLayoutX(40);
        imageView2.setLayoutY(25);
        imageView3.setLayoutX(550);
        imageView3.setLayoutY(28);
        imageView2.setFitHeight(58);
        imageView2.setFitWidth(214);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView1.setFitWidth(200);
        imageView1.setFitHeight(200);
        Button transfer = new Button();
        Button addUser = new Button();
        Button credits = new Button();
        transfer.setGraphic(imageView);
        addUser.setGraphic(imageView1);
        transfer.setPrefSize(280, 210);
        addUser.setPrefSize(280, 210);
        transfer.setLayoutX(40);
        transfer.setLayoutY(170);
        addUser.setLayoutX(330);
        addUser.setLayoutY(170);
        transfer.setStyle("-fx-background-color: lightgray; -fx-border-color: #333333; -fx-border-width: 5px; -fx-background-radius: 25px; -fx-border-radius: 15; -fx-background-insets: 0");
        addUser.setStyle("-fx-background-color: lightgray; -fx-border-color: #333333; -fx-border-width: 5px; -fx-background-radius: 25px; -fx-border-radius: 15; -fx-background-insets: 0");
        transfer.setOnMousePressed(event -> {
            transfer.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20; -fx-border-radius: 20; -fx-background-insets: 2; -fx-border-color: #333333; -fx-border-width: 1px;");
        });
        transfer.setOnMouseReleased(event -> {
            transfer.setStyle("-fx-background-color: lightgray; -fx-border-color: #333333; -fx-border-width: 5px; -fx-background-radius: 25px; -fx-border-radius: 15; -fx-background-insets: 0");
            transfer_money();
        });
        addUser.setOnMousePressed(event -> {
            addUser.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20; -fx-border-radius: 20; -fx-background-insets: 2; -fx-border-color: #333333; -fx-border-width: 1px;");
        });
        addUser.setOnMouseReleased(event -> {
            addUser.setStyle("-fx-background-color: lightgray; -fx-border-color: #333333; -fx-border-width: 5px; -fx-background-radius: 25px; -fx-border-radius: 15; -fx-background-insets: 0");
            add_user();
        });
        screenPane.getChildren().add(rec);
        screenPane.getChildren().add(transfer);
        screenPane.getChildren().add(addUser);
        screenPane.getChildren().add(imageView);
        screenPane.getChildren().add(imageView1);
        screenPane.getChildren().add(imageView2);
        screenPane.getChildren().add(imageView3);
        screenPane.getChildren().add(imageView4);
        screenPane.getChildren().add(number);
        screenPane.getChildren().add(number1);
        screenPane.setStyle("-fx-background-color: #336699; -fx-padding: 10px; -fx-border-color: #333333; -fx-border-width: 10px");
        gridPane.setStyle("-fx-border-width: 10px; -fx-border-color: #333333; -fx-background-color: #333333;" );
        tilePane.setPrefColumns(1);// setting rows and cols for the tilapane
        tilePane.setPrefRows(2);
    }
    private void add_user() {
        in_money = false;
        in_user = true;
        Pane temp = new Pane();
        tilePane.getChildren().set(0, temp);
        System.out.println("hello");
        Rectangle rec = new Rectangle(630, 100);
        rec.setFill(Color.WHITE);
        rec.setLayoutY(10);
        rec.setLayoutX(10);
        Label number = new Label("+995 599 552 945");
        Label number1 = new Label("+995 579 092 128");
        number.setFont(new Font(20));
        number1.setFont(new Font(20));
        Image image2 = new Image("C:\\Users\\Administrator\\IdeaProjects\\ATM\\src\\main\\resources\\2560px-TBC_Bank_logo.svg.png");
        Image image3 = new Image("C:\\Users\\Administrator\\IdeaProjects\\ATM\\src\\main\\resources\\photo.png");
        Image image4 = new Image("C:\\Users\\Administrator\\IdeaProjects\\ATM\\src\\main\\resources\\phone.jpg");
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView3 = new ImageView(image3);
        ImageView imageView4 = new ImageView(image4);
        number.setLayoutX(375);
        number.setLayoutY(31);
        number1.setLayoutX(375);
        number1.setLayoutY(56.5);
        imageView4.setFitHeight(100);
        imageView4.setFitWidth(200);
        imageView4.setLayoutY(100);
        imageView4.setLayoutX(420);
        imageView3.setFitWidth(60);
        imageView3.setFitHeight(60);
        imageView2.setLayoutX(40);
        imageView2.setLayoutY(25);
        imageView3.setLayoutX(550);
        imageView3.setLayoutY(28);
        imageView2.setFitHeight(58);
        imageView2.setFitWidth(214);
        temp.getChildren().add(rec);
        temp.getChildren().add(imageView2);
        temp.getChildren().add(imageView3);
        temp.getChildren().add(imageView4);
        temp.getChildren().add(number);
        temp.getChildren().add(number1);
        text = new TextField();
        text.setPromptText("0000 0000 0000 0000");
        text1 = new TextField();
        text1.setPromptText("MM/YY");
        text2 = new TextField();
        text2.setPromptText(". . .");
        text3 = new TextField();
        text3.setPromptText("Enter cardholder's full name");
        text.setOnMouseClicked(event -> selectedTextField = text);
        text1.setOnMouseClicked(event -> selectedTextField = text1);
        text2.setOnMouseClicked(event -> selectedTextField = text2);
        text3.setOnMouseClicked(event -> selectedTextField = text3);
        Label card_number = new Label("Card number");
        Label card_date = new Label("Card date");
        Label card_cvv = new Label("Card security code");
        Label card_name = new Label("Cardholder name");
        card_number.setFont(new Font(14));
        card_number.setLayoutX(180);
        card_number.setLayoutY(178);
        card_date.setFont(new Font(14));
        card_date.setLayoutX(180);
        card_date.setLayoutY(243);
        card_cvv.setFont(new Font(14));
        card_cvv.setLayoutX(340);
        card_cvv.setLayoutY(243);
        card_name.setFont(new Font(14));
        card_name.setLayoutX(180);
        card_name.setLayoutY(308);
        text.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
        text1.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
        text2.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
        text3.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
        text.setLayoutX(175);
        text.setLayoutY(200);
        text.setPrefSize(300, 35);
        text1.setLayoutX(175);
        text1.setLayoutY(265);
        text1.setPrefSize(140, 35);
        text2.setLayoutX(335);
        text2.setLayoutY(265);
        text2.setPrefSize(140, 35);
        text3.setLayoutX(175);
        text3.setLayoutY(330);
        text3.setPrefSize(300, 35);
        temp.getChildren().add(text);
        temp.getChildren().add(text1);
        temp.getChildren().add(text2);
        temp.getChildren().add(text3);
        temp.getChildren().add(card_number);
        temp.getChildren().add(card_cvv);
        temp.getChildren().add(card_name);
        temp.getChildren().add(card_date);
        temp.setStyle("-fx-background-color: #336699; -fx-padding: 10px; -fx-border-color: #333333; -fx-border-width: 10px");
        gridPane.setStyle("-fx-border-width: 10px; -fx-border-color: #333333; -fx-background-color: #333333;");
        tilePane.setPrefColumns(1);// setting rows and cols for the tilapane
        tilePane.setPrefRows(2);
    }
    private void transfer_money() {
        in_money = true;
        in_user = false;
        Pane temp = new Pane();
        tilePane.getChildren().set(0, temp);
        System.out.println("hello");
        Rectangle rec = new Rectangle(630, 100);
        rec.setFill(Color.WHITE);
        rec.setLayoutY(10);
        rec.setLayoutX(10);
        Label number = new Label("+995 599 552 945");
        Label number1 = new Label("+995 579 092 128");
        number.setFont(new Font(20));
        number1.setFont(new Font(20));
        Image image2 = new Image("C:\\Users\\Administrator\\IdeaProjects\\ATM\\src\\main\\resources\\2560px-TBC_Bank_logo.svg.png");
        Image image3 = new Image("C:\\Users\\Administrator\\IdeaProjects\\ATM\\src\\main\\resources\\photo.png");
        Image image4 = new Image("C:\\Users\\Administrator\\IdeaProjects\\ATM\\src\\main\\resources\\phone.jpg");
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView3 = new ImageView(image3);
        ImageView imageView4 = new ImageView(image4);
        number.setLayoutX(375);
        number.setLayoutY(31);
        number1.setLayoutX(375);
        number1.setLayoutY(56.5);
        imageView4.setFitHeight(100);
        imageView4.setFitWidth(200);
        imageView4.setLayoutY(100);
        imageView4.setLayoutX(420);
        imageView3.setFitWidth(60);
        imageView3.setFitHeight(60);
        imageView2.setLayoutX(40);
        imageView2.setLayoutY(25);
        imageView3.setLayoutX(550);
        imageView3.setLayoutY(28);
        imageView2.setFitHeight(58);
        imageView2.setFitWidth(214);
        temp.getChildren().add(rec);
        temp.getChildren().add(imageView2);
        temp.getChildren().add(imageView3);
        temp.getChildren().add(imageView4);
        temp.getChildren().add(number);
        temp.getChildren().add(number1);
        text = new TextField();
        text.setPromptText("0000 0000 0000 0000");
        text1 = new TextField();
        text1.setPromptText("MM/YY");
        text2 = new TextField();
        text2.setPromptText(". . .");
        text3 = new TextField();
        text3.setPromptText("Enter cardholder's full name");
        enter_money = new TextField();
        enter_money.setPromptText("Enter transfer amount");
        text.setOnMouseClicked(event -> selectedTextField = text);
        text1.setOnMouseClicked(event -> selectedTextField = text1);
        text2.setOnMouseClicked(event -> selectedTextField = text2);
        text3.setOnMouseClicked(event -> selectedTextField = text3);
        enter_money.setOnMouseClicked(event -> selectedTextField = enter_money);
        Label card_number = new Label("Card number");
        Label card_date = new Label("Card date");
        Label card_cvv = new Label("Card security code");
        Label card_name = new Label("Cardholder name");
        Label card_balance = new Label("Cardholder balance");
        card_number.setFont(new Font(14));
        card_number.setLayoutX(180);
        card_number.setLayoutY(148);
        card_date.setFont(new Font(14));
        card_date.setLayoutX(180);
        card_date.setLayoutY(213);
        card_cvv.setFont(new Font(14));
        card_cvv.setLayoutX(340);
        card_cvv.setLayoutY(213);
        card_name.setFont(new Font(14));
        card_name.setLayoutX(180);
        card_name.setLayoutY(278);
        card_balance.setFont(new Font(14));
        card_balance.setLayoutX(180);
        card_balance.setLayoutY(343);
        text.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
        text1.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
        text2.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
        text3.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
        enter_money.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
        text.setLayoutX(175);
        text.setLayoutY(170);
        text.setPrefSize(300, 35);
        text1.setLayoutX(175);
        text1.setLayoutY(235);
        text1.setPrefSize(140, 35);
        text2.setLayoutX(335);
        text2.setLayoutY(235);
        text2.setPrefSize(140, 35);
        text3.setLayoutX(175);
        text3.setLayoutY(300);
        text3.setPrefSize(300, 35);
        enter_money.setLayoutX(175);
        enter_money.setLayoutY(365);
        enter_money.setPrefSize(300, 35);
        temp.getChildren().add(text);
        temp.getChildren().add(text1);
        temp.getChildren().add(text2);
        temp.getChildren().add(text3);
        temp.getChildren().add(enter_money);
        temp.getChildren().add(card_number);
        temp.getChildren().add(card_cvv);
        temp.getChildren().add(card_name);
        temp.getChildren().add(card_date);
        temp.getChildren().add(card_balance);
        temp.setStyle("-fx-background-color: #336699; -fx-padding: 10px; -fx-border-color: #333333; -fx-border-width: 10px");
        gridPane.setStyle("-fx-border-width: 10px; -fx-border-color: #333333; -fx-background-color: #333333;");
        tilePane.setPrefColumns(1);// setting rows and cols for the tilapane
        tilePane.setPrefRows(2);
    }
    private void insert_money(String card_num, String card_name, String card_date, String card_cvv, double money){
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "Programmer098")) {
            double previous_balance = 0.0;
            String command = "SELECT * FROM cards WHERE card_num = ? AND card_cvv = ? AND card_name = ? AND card_date = ?";
            PreparedStatement check = connection.prepareStatement(command);
            check.setString(1, card_num);
            check.setString(2, card_cvv);
            check.setString(3, card_name);
            check.setString(4, card_date);
            ResultSet resultSet = check.executeQuery();
            if (resultSet.next()) {
                System.out.println("Element exists in the table.");
                previous_balance = resultSet.getDouble("card_balance");
            } else {
                System.out.println("doesnot exist");
            }
            String updateQuery = "UPDATE cards SET card_balance = ? WHERE card_num = ? AND card_cvv = ? AND card_date = ? AND card_name = ?";
            double new_balance = previous_balance + money;
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setDouble(1, new_balance);
            updateStatement.setString(2, card_num);
            updateStatement.setString(3, card_cvv);
            updateStatement.setString(4, card_date);
            updateStatement.setString(5, card_name);
            int rowsUpdated = updateStatement.executeUpdate();
            if (rowsUpdated > 0) {
                text.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                text1.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                text2.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                text3.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                if(in_money)enter_money.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: #333333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                System.out.println("Element found and balance value has been updated successfully.");
                System.out.println("Previous balance: " + previous_balance);
                System.out.println("Additional balance: " + money);
                System.out.println("New balance: " + new_balance);
                success.setLayoutX(180);
                success.setLayoutY(402);
                Pane a = (Pane) tilePane.getChildren().get(0);
                a.getChildren().add(success);
            } else {
                text.setStyle("-fx-background-color: mistyrose; -fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                text1.setStyle("-fx-background-color: mistyrose; -fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                text2.setStyle("-fx-background-color: mistyrose; -fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                text3.setStyle("-fx-background-color: mistyrose; -fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
                enter_money.setStyle("-fx-background-color: mistyrose; -fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 15px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-background-insets: 2");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void insert_user(String number, String date, String cvv, String name){
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "Programmer098")) {
            String temp = "SELECT COUNT(*) FROM cards WHERE card_num = ?";
            PreparedStatement check = connection.prepareStatement(temp);
            check.setString(1, number);
            ResultSet resultSet = check.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if(count == 0){
                String sql = "INSERT INTO cards (card_num, card_cvv, card_name, card_date, card_balance) VALUES (?, ?, ?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, number);
                statement.setString(2, cvv);
                statement.setString(3, name);
                statement.setString(4, date);
                statement.setDouble(5, 0);
                // Execute the SQL statement
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    Node a = tilePane.getChildren().get(0);
                    System.out.println(((Pane) a).getChildren().size());
                    if(((Pane) a).getChildren().size() > 14){
                        ((Pane) a).getChildren().remove(14);
                    }
                    System.out.println("User registered successfully!");
                    Label error = new Label("User registered successfully!");
                    error.setLayoutX(175);
                    error.setLayoutY(367);
                    ((Pane) a).getChildren().add(error);
                }
            }else{
                Node a = tilePane.getChildren().get(0);
                try{
                    ((Pane) a).getChildren().remove(14);
                }catch(IndexOutOfBoundsException ddd){
                    System.out.println("not inside");
                }
                System.out.println("User registered successfully!");
                Label error = new Label("User registered successfully!");
                error.setLayoutX(175);
                error.setLayoutY(367);
                ((Pane) a).getChildren().add(error);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}