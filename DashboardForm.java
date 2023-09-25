import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DashboardForm extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton btnBook;
    private JTextField tfNumber;
    private JLabel availSeats;
    private JLabel bookedSeats;
    private JButton viewSeatingLayoutButton;
    private JButton backButton;
    private JLabel seatNumbers;
    String userName;
    int MAX_NUMBER_OF_SEATS=50;
    User user;


//    display sets left, book seat button

    public DashboardForm(User user) {
        this.user=user;
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(850, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if (user != null) {
            lbAdmin.setText("User: " + user.name);
            setLocationRelativeTo(null);
            List<Integer> list=new ArrayList<>();
            list=sumOfSeats();
            int sumSeats=list.get(0);
            int currSeat=list.get(1);
            availSeats.setText(Integer.toString(MAX_NUMBER_OF_SEATS-sumSeats));
            bookedSeats.setText(Integer.toString(currSeat));
            setVisible(true);
            userName = user.name;
        }
        else {
            dispose();
        }

        btnBook.addActionListener(e -> bookseats());

        viewSeatingLayoutButton.addActionListener(e -> {
            Seats seat = new Seats();
        });

        backButton.addActionListener(e -> dispose());
    }

    List<Integer> sumOfSeats() {
        List<Integer> list=new ArrayList<>();
        final String DB_URL = "jdbc:mysql://localhost:3306/vaishnavidb";
        final String USERNAME = "root";
        final String PASSWORD = "3574";
        int sum = 0;
        int currentSeat=0;
        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement pst = conn.createStatement();
            ResultSet rs = pst.executeQuery("select seats from users");
            while (rs.next()) {
                int seat = rs.getInt("seats");
//                System.out.println(seat);
                sum =sum + seat;
            }
            list.add(sum);
            String query = ("SELECT * FROM users WHERE name = ?");
            PreparedStatement st = conn.prepareStatement(query);
//            System.out.println(user.name);
            st.setString(1, user.name);
            ResultSet rs1 = st.executeQuery();
            if (rs1.next()) {
                currentSeat = rs1.getInt("seats");
            }
//            System.out.println(currentSeat);
            list.add(currentSeat);


            ResultSet res = pst.executeQuery("select * from seatstatus");
            String query1;
            query1 = ("SELECT * FROM seatstatus WHERE status = ?");
            PreparedStatement st1 = conn.prepareStatement(query1);
            st1.setString(1,"0");
            ResultSet rs2 = st1.executeQuery();
            String availseat=new String();
            while(rs2.next()) {
//                System.out.println(rs2.getInt("number"));
                int x=rs2.getInt("number");
                availseat+=Integer.toString(x)+", ";
            }

            seatNumbers.setText(availseat);

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public void bookseats() {
        String str=tfNumber.getText();
        int numberOfSeats=0;
        if(!str.equals("")) {
            numberOfSeats = Integer.parseInt(str);
        }
        addSeatToDatabase(userName,numberOfSeats);
    }


    public void addSeatToDatabase(String name,int seats) {
        final String DB_URL = "jdbc:mysql://localhost:3306/vaishnavidb";
        final String USERNAME = "root";
        final String PASSWORD = "3574";

//        System.out.println(name+seats);

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            PreparedStatement pst=null;
            ResultSet rs=null;
            String userClass;

            String query = "UPDATE users SET seats = seats + ? WHERE name = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, seats);
            pstmt.setString(2,name);

            pstmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean connectToDatabase() {
        boolean hasRegistredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost:3306/";
        final String DB_URL = "jdbc:mysql://localhost:3306/vaishnavidb";
        final String USERNAME = "root";
        final String PASSWORD = "3574";

        try{
            //First, connect to MYSQL server and create the database if not created
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS MyStore");
            statement.close();
            conn.close();

            //Second, connect to the database and create the table "users" if cot created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(200) NOT NULL,"
                    + "email VARCHAR(200) NOT NULL UNIQUE,"
                    + "phone VARCHAR(200),"
                    + "address VARCHAR(200),"
                    + "password VARCHAR(200) NOT NULL,"
                    + "seats INT NOT NULL"
                    + ")";
            statement.executeUpdate(sql);

            //check if we have users in the table users
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");

            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegistredUsers = true;
                }
            }

            statement.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return hasRegistredUsers;
    }

    public static void main(String[] args) {
        DashboardForm myForm = new DashboardForm(new User());
    }
}

