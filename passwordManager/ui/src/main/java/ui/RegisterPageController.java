package ui;

import client.RestTalker;
import client.ServerResponseException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * FXML Controller class for the login page.
 */
public class RegisterPageController extends PasswordManagerController {

  @FXML
  private TextField usernameTextField;

  @FXML
  private TextField passwordTextField;

  @FXML
  private TextField repeatPasswordTextField;

  @FXML
  private Text visualFeedbackText;

  @FXML
  private PasswordField passwordPasswordField;

  @FXML
  private PasswordField repeatPasswordPasswordField;

  @FXML
  private ImageView eyeImageView1;

  @FXML
  private ImageView eyeImageView2;

  /**
   * Initializes the controller class.
   */
  @FXML
  public void initialize() {
    eyeImageView1.setImage(eyeOpenImage);
    eyeImageView2.setImage(eyeOpenImage);
    visualFeedbackText.setFill(Color.valueOf(lightRed));
  }

  RestTalker restTalker = new RestTalker();

  @FXML
  private void eyeImageViewClick1() {
    passwordEye(passwordTextField, passwordPasswordField, eyeImageView1);
  }

  @FXML
  private void eyeImageViewClick2() {
    passwordEye(repeatPasswordTextField, repeatPasswordPasswordField, eyeImageView2);
  }

  @FXML
  private void onRegisterBackButtonClick(ActionEvent event) throws IOException {
    switchScene(event, "login.fxml");
  }

  /**
   * onCreateUserButtonClick sends the fields in the register page form to the
   * resttalker for validation, if they are validated, a new user is stored and
   * the user is sent back to the loginpage.
   *
   * @param event ActionEvent object used in the switchScene method
   * @throws IOException if the fxml file cant be opened
   */
  @FXML
  private void onCreateUserButtonClick(ActionEvent event) throws IOException {
    String username = usernameTextField.getText();
    String password = passwordTextField.getText();
    if (!passwordTextField.isVisible()) {
      password = passwordPasswordField.getText();
    }
    String passwordRepeat = repeatPasswordTextField.getText();
    if (!repeatPasswordTextField.isVisible()) {
      passwordRepeat = repeatPasswordPasswordField.getText();
    }

    // if all fields are filled
    if (username != "" && password != "" && passwordRepeat != "") {

      String validationResult;
      try {
        validationResult = restTalker.userValidator(username, password, passwordRepeat);
      } catch (
      URISyntaxException 
        | InterruptedException 
        | ExecutionException 
        | ServerResponseException e
      ) {
        e.printStackTrace();
        validationResult = "Server error";
      }

      if (validationResult.equals("OK")) {
        switchScene(event, "login.fxml");
        try {
          restTalker.registerUser(username, password);
        } catch (
        URISyntaxException 
          | InterruptedException 
          | ExecutionException 
          | ServerResponseException e
        ) {
          e.printStackTrace();
        }
      } else {
        visualFeedbackText.setText(validationResult);
      }
    } else {
      visualFeedbackText.setText("Please fill in all fields");
    }

    visualFeedbackText.setVisible(true);
    setBorder(usernameTextField, lightRed);
    setBorder(passwordTextField, lightRed);
    setBorder(repeatPasswordTextField, lightRed);
    setBorder(passwordPasswordField, lightRed);
    setBorder(repeatPasswordPasswordField, lightRed);
    rotateNode(visualFeedbackText, false);
  }

}
