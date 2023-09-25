import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Seats extends JFrame{
    private JButton BACKButton;
    private JLabel Seating;
    private JLabel LayoutImg;
    private JPanel SeatLayout;
    private JButton seat1;
    private JButton seat2;
    private JButton seat3;
    private JButton seat4;

    public Seats(){
        setTitle("Seat Layout");
        setContentPane(SeatLayout);
        setMinimumSize(new Dimension(500, 429));
        setSize( 600, 650);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        seat1.addActionListener(e->updateDatabase(1));
        seat2.addActionListener(e->updateDatabase(2));
        seat3.addActionListener(e->updateDatabase(3));
        seat4.addActionListener(e->updateDatabase(4));
        BACKButton.addActionListener(e -> dispose());
    }

    public void updateDatabase(int num) {
        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost:3306/";
        final String DB_URL = "jdbc:mysql://localhost:3306/vaishnavidb";
        final String USERNAME = "root";
        final String PASSWORD = "3574";
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            PreparedStatement pst=null;
            ResultSet rs=null;
            String userClass;

            String query = "UPDATE seatstatus SET status = 1 WHERE number = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, num);

            pstmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
