package seedu.finclient.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.finclient.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on FinClient level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label order;
    @FXML
    private Label remark;
    @FXML
    private FlowPane tags;
    @FXML
    private Label company;
    @FXML
    private Label job;
    @FXML
    private Label stockPlatform;
    @FXML
    private Label networth;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);

        phone.setText("Phone: " + person.getPhoneList().toString());
        address.setText("Address: " + person.getAddress().value);
        email.setText("Email: " + person.getEmail().value);
        order.setText("Order: " + person.getOrder().toString());

        System.out.println("Debug: UI PersonCard -> Name: "
                + person.getName().fullName + ", Remark: " + person.getRemark());

        // Optionals
        remark.setText("Remark: " + person.getRemark().value);
        if (person.getRemark().value == "") {
            remark.setVisible(false);
            remark.setManaged(false);
        }
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        if (person.getCompany() == null || person.getCompany().value == "") {
            company.setVisible(false);
            company.setManaged(false);
        } else {
            company.setText("Company: " + person.getCompany().value);
        }

        if (person.getJob() == null || person.getJob().value == "") {
            job.setVisible(false);
            job.setManaged(false);
        } else {
            job.setText("Job: " + person.getJob().value);
        }

        if (person.getStockPlatform() == null || person.getStockPlatform().value == "") {
            stockPlatform.setVisible(false);
            stockPlatform.setManaged(false);
        } else {
            stockPlatform.setText("Stock Platform: " + person.getStockPlatform().value);
        }

        if (person.getNetworth() == null || person.getNetworth().value == "") {
            networth.setVisible(false);
            networth.setManaged(false);
        } else {
            networth.setText("Networth: " + person.getNetworth().value);
        }
    }
}
