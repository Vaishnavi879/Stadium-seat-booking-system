import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BeginDashboard extends JFrame {

    private JPanel WelcomePage;
    private JButton LoginButton;
    private JButton RegButton;


    public BeginDashboard() {
        setTitle("Welcome Page");
        setContentPane(WelcomePage);
        setMinimumSize(new Dimension(500, 429));
        setSize( 500, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        RegButton.addActionListener(e -> {
            RegistrationForm registrationForm = new RegistrationForm(BeginDashboard.this);
            User user = registrationForm.user;

            if (user != null) {
                JOptionPane.showMessageDialog(BeginDashboard.this,
                        "New user: " + user.name,
                        "Successful Registration",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        LoginButton.addActionListener(e -> {
            LoginForm loginForm = new LoginForm(BeginDashboard.this);
            User user = loginForm.user;
            if (user != null) {
                DashboardForm dashboardForm = new DashboardForm(user);

            }
            else {
                System.out.println("Authentication canceled");
            }
        });
    }


    public static void main(String[] args) {
        BeginDashboard myForm = new BeginDashboard();
    }
}
