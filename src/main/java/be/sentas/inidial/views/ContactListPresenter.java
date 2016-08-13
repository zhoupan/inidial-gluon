package be.sentas.inidial.views;

import be.sentas.inidial.Service;
import be.sentas.inidial.model.Contact;
import be.sentas.inidial.model.KeyboardConfig;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import be.sentas.inidial.InidialApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Arrays;

public class ContactListPresenter implements Keyboard.OnInteractionListener {

    @FXML
    private View mainView;

    @FXML
    private CharmListView<Contact, String> contactList;

    @FXML
    private Keyboard keyboard;

    @FXML
    private Label initials;

    @FXML
    private Label numberOfMatches;

    private SimpleStringProperty searchedInitials;

    public void initialize() {
        mainView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                initAppBar();
                initContacts();
                initKeyboard();
                initHeader();
            }
        });
    }

    private void initAppBar() {
        AppBar appBar = MobileApplication.getInstance().getAppBar();
        appBar.setTitleText("Inidial");
        appBar.getActionItems().add(MaterialDesignIcon.SETTINGS.button(e ->
                MobileApplication.getInstance().switchView(InidialApp.SETTINGS_VIEW)));
    }

    private void initHeader() {
        searchedInitials = new SimpleStringProperty("");
        searchedInitials.addListener((observable, oldValue, newValue) -> searchedInitialsChanged(oldValue, newValue));
        initials.setMaxWidth(Double.MAX_VALUE);
        numberOfMatches.setText("3 matches");
    }

    private void searchedInitialsChanged(String oldValue, String newValue) {
        AppBar appBar = MobileApplication.getInstance().getAppBar();
        if (!newValue.equals("")) {
            if (oldValue.equals("")) {
                appBar.getActionItems().add(0, MaterialDesignIcon.CLEAR.button(e -> clearSearch()));
            }
        } else {
            appBar.getActionItems().remove(0);
        }
        initials.setText(newValue);

    }

    private void initContacts() {
        ObservableList<Contact> contacts = FXCollections.observableList(Service.getService().getContacts());
        contactList.setCellFactory(param -> new ContactListCell());
        contactList.setItems(contacts);
    }

    private void initKeyboard() {
        keyboard.setMaxWidth(mainView.getScene().getWidth());
        keyboard.load(KeyboardConfig.getConfig(KeyboardConfig.Layout.QWERTY, Arrays.asList("A", "B", "C")));
        keyboard.setListener(this);
    }

    private void clearSearch() {
        searchedInitials.setValue("");
        keyboard.load(KeyboardConfig.getConfig(KeyboardConfig.Layout.QWERTY, Arrays.asList("A", "B", "C")));
    }

    @Override
    public void onKeyPressed(String symbol) {
        searchedInitials.set(searchedInitials.getValue().concat(symbol));
    }
    
    /*@FXML
    void buttonClick() {
        label.setText("Hello JavaFX Universe!");
    }*/
    
}
