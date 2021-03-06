package studentempsignin;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainPage_Controller implements Initializable {
	@FXML
	Text clockInText;

	public MainPage_Controller() {

	}

	// this method will show the hoursinfo page and also setting the hours text
	// relative to its empID
	// this uses a controller to do so
	// also sets the empID hours for the method in signIncontroller to use
	public void estimateHours(String empID) throws IOException, SQLException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"HoursInfo.fxml"));
		Parent root = (Parent) loader.load();
		SignIn_Controller controller = (SignIn_Controller) loader
				.getController();
		Scene scene = new Scene(root);
		window.setScene(scene);

		Connection connect = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/AUStudentEmployees", "root",
				"password");

		Statement stmnt = connect.createStatement();
		ResultSet result = stmnt
				.executeQuery("select EstimatedHours from emp_info"
						+ " where EmployeeID = " + empID + "; ");

		while (result.next()) {
			controller.estimateHoursText.setText(result.getString(1));
			controller.empIDHours.setText(empID);
		}

		window.show();

		connect.close();

	}

	// this method fixed my problem of changing text before window shows up
	// essentially you are supposed to call the controller from where you loaded
	// the fxml
	// then you call the fields in that controller and then thats when you can
	// change it

	// also sets the empIDMain for the method in signInController to use
	public void signIn(String empID, Button btn) throws IOException,
			SQLException {

		Stage window;
		window = (Stage) btn.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"MainPage_Controller.fxml"));
		Parent root = (Parent) loader.load();
		SignIn_Controller controller = (SignIn_Controller) loader
				.getController();
		Scene scene = new Scene(root, 500, 250);
		window.setScene(scene);

		Connection connect = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/AUStudentEmployees", "root",
				"password");

		Statement stmnt = connect.createStatement();
		ResultSet result = stmnt
				.executeQuery("select FirstName, LastName from emp_info"
						+ " where EmployeeID = " + empID + "; ");

		while (result.next()) {
			controller.empName.setText(result.getString(1) + " "
					+ result.getString(2));
			controller.empIDMain.setText(empID);
		}
		//check if clocked in
		ResultSet statusResult = stmnt.executeQuery("select isClockedIn from emp_info"
				+ " where EmployeeID = " + empID + ";");
		
		//change status according to result when loading page
		while(statusResult.next()){
			if(statusResult.getString(1).equalsIgnoreCase("1")) {
				controller.changeStatusTxt(controller.statusTxt, true);
			}
			else{
				controller.changeStatusTxt(controller.statusTxt, false);
			}
		}

		// Label timeLabel = new Label();
		// DateFormat df = new SimpleDateFormat("HH:mm:ss");
		// Date dateobj = new Date();
		new DigitalClock(controller.timeText); // creates instance
		// controller.timeText.setText(dynamicTime.);
		

		window.show();

		connect.close();
	}

	public void signInAdmin(String ID, Button btn) throws IOException,
			SQLException {

		Stage window;
		window = (Stage) btn.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"adminMainPage.fxml"));
		Parent root = (Parent) loader.load();
		SignIn_Controller controller = (SignIn_Controller) loader
				.getController();
		Scene scene = new Scene(root, 270, 200);
		window.setScene(scene);

		Connection connect = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/AUStudentEmployees", "root",
				"password");

		Statement stmnt = connect.createStatement();
		ResultSet result = stmnt
				.executeQuery("select GroupName from admin_info"
						+ " where AdminID = " + ID + "; ");

		while (result.next()) {
			controller.adminGroupText.setText(result.getString("GroupName"));
			//controller.empIDMain.setText(empID);
		}

		// Label timeLabel = new Label();
		// DateFormat df = new SimpleDateFormat("HH:mm:ss");
		// Date dateobj = new Date();
		//new DigitalClock(controller.timeText); // creates instance
		// controller.timeText.setText(dynamicTime.);

		window.show();

		connect.close();
	}

	// clockingIn method will show the time clocked In
	public long clockingIn() throws Exception {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"ClockIn_Controller.fxml"));
		Parent root = (Parent) loader.load();
		SignIn_Controller controller = (SignIn_Controller) loader
				.getController();
		Scene scene = new Scene(root);
		window.setScene(scene);

		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date dateobj = new Date();

		controller.clockInText.setText(df.format(dateobj));

		window.show();
		return dateobj.getTime();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
