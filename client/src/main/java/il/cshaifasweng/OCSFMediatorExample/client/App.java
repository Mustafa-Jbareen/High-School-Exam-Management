package il.cshaifasweng.OCSFMediatorExample.client;

import aidClasses.Color;
import aidClasses.GlobalDataSaved;
import aidClasses.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private SimpleClient client;

    @Override
    public void start(Stage stage) throws IOException {
    	EventBus.getDefault().register(this);
    	client = SimpleClient.getClient();
    	client.openConnection();
        scene = new Scene(loadFXML("login"), 778, 576);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    public static void setContentTeacher(String pageName) throws IOException {
        scene.setRoot(loadFXML(pageName));
        scene.getWindow().setWidth(870);scene.getWindow().setHeight(670);
    }
    public static void backLogin(String pageName) throws IOException {
        scene.getWindow().setWidth(800);scene.getWindow().setHeight(600);
        scene.setRoot(loadFXML(pageName));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
	public void stop() throws Exception {
        EventBus.getDefault().unregister(this);
        try {
            Message ms1 = new Message("#logout",GlobalDataSaved.connectedUser.getId()); // creating a msg to the server demanding the students
            SimpleClient.getClient().sendToServer(ms1); // sending the msg to the server
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
		super.stop();
        Platform.exit();
        System.exit(0);
	}

    @Subscribe
    public void onWarningEvent(WarningEvent event) {
    	Platform.runLater(() -> {
    		Alert alert = new Alert(AlertType.WARNING,
        			String.format("Message: %s\nTimestamp: %s\n",
        					event.getWarning().getMessage(),
        					event.getWarning().getTime().toString())
        	);
        	alert.show();
    	});

    }
    @Subscribe
    public  void onMessageEvent(MessageEvent event)
    {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION,
                    String.format("%s\n%s\n",
                            event.getMessage().getMsg(),event.getMessage().getTime().toString()));
            alert.show();
        });
    }
    @FXML
    public void exitApplication(ActionEvent event) {
        System.out.println("bye");
        Platform.exit();
    }

	public static void main(String[] args) {
        launch();
    }

}