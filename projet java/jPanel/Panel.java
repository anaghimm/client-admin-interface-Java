package jPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;





public class Panel {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShowInterface();
        });
    }


    private static void ShowInterface() {
        JFrame fen = new JFrame();
        fen.setSize(600, 600);
        fen.setLocationRelativeTo(null);
        JPanel pan = new JPanel();
        fen.setContentPane(pan);

        // Button client
        JButton buttonClient = new JButton("Client");
        buttonClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openClientInterface();
            }
        });
        fen.getContentPane().add(buttonClient);

        // Button admin
        JButton buttonAdmin = new JButton("Admin");
        buttonAdmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAdminInterface();
            }
        });
        fen.getContentPane().add(buttonAdmin);
        fen.setLayout(new GridBagLayout());
        fen.setVisible(true);
    }

    //interface opened when you click on client button

    private static void openClientInterface() {
        JFrame clientFrame = new JFrame("Client Interface");
        clientFrame.setSize(600, 550);
        clientFrame.setLocationRelativeTo(null);
        clientFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        clientFrame.setLayout(new GridLayout(8, 2));

        // specifying the interface:the textfields and the labels
            Label name = new Label("Name:");
            clientFrame.add(name);
            TextField nameTf= new TextField();
            clientFrame.add(nameTf);
            nameTf.setPreferredSize(new Dimension(150, 25));

            Label lastName = new Label("Last Name:");
            clientFrame.add(lastName);
            TextField lastnameTf= new TextField();
            lastnameTf.setPreferredSize(new Dimension(150, 25));
            clientFrame.add(lastnameTf);

            Label speciality = new Label("Speciality:");
            clientFrame.add(speciality);
            
            //for the textfield
            String[] specialitylist={"Big Data","Informatique Multimedia","Communication Multimedia","MIME","Audiovisuel et cinema"};
            JComboBox<String> specialityComboBox= new JComboBox<>(specialitylist);
            clientFrame.add(specialityComboBox);

            Label clubName = new Label("Club Name:");
            clientFrame.add(clubName);
            String[] clubslist={"Microsoft","Google Dev","Aisec","Orenda","Club Musique","Club Radio","J2i","CultuClub","Tunivisions ISAMM","Boubli","Engineers Spark"};
            JComboBox<String> clubComboBox=new JComboBox<>(clubslist);
            clientFrame.add(clubComboBox);

            JButton submitbutton = new JButton("OK");
            submitbutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String name = nameTf.getText();
                    String lastName = lastnameTf.getText();
                    String speciality = (String) specialityComboBox.getSelectedItem();;
                    String clubName = (String) clubComboBox.getSelectedItem();
                    String jdbcUrl = "jdbc:mysql://localhost:3306/clients";
                    String username = "naghma";
                    String password = "anaghimnaarf";
    
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
    
                        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                            // Insert data into the "clients" table
                            String sql = "INSERT INTO client (name, lastname, speciality, clubname) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                preparedStatement.setString(1, name);
                                preparedStatement.setString(2, lastName);
                                preparedStatement.setString(3, speciality);
                                preparedStatement.setString(4, clubName);
    
                                int rowsAffected = preparedStatement.executeUpdate();
    
                                if (rowsAffected > 0) {
                                    JOptionPane.showMessageDialog(clientFrame, "Client information submitted successfully!");
                                } else {
                                    JOptionPane.showMessageDialog(clientFrame, "Failed to submit client information.");
                                }
                            }
                        }
                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(clientFrame, "Error connecting to the database.");
                    }
                }
            });
            clientFrame.getContentPane().add(submitbutton);    
            JButton outbutton = new JButton("Annuler");
            outbutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clientFrame.dispose();
                }
            });
            clientFrame.getContentPane().add(outbutton);

            clientFrame.setVisible(true);


    };

    //interface opened when you click on admin button

    private static void openAdminInterface() {
        JFrame adminFrame = new JFrame("Admin Interface");
        adminFrame.setSize(600, 550);
        adminFrame.setLocationRelativeTo(null);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel choixLabel = new JLabel("Choix");
        adminFrame.add(choixLabel);
        String[] choiceslist = {"Microsoft","Google Dev","Aisec","Orenda","Club Musique","Club Radio","J2i","CultuClub","Tunivisions ISAMM","Boubli","Engineers Spark","Big Data","Informatique Multimedia","Communication Multimedia","MIME","Audiovisuel et cinema"};
        JComboBox choicesComboBox = new JComboBox<>(choiceslist);
        adminFrame.add(choicesComboBox);

        JButton actionButton = new JButton("Perform Action");
        actionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String adminChoice = (String) choicesComboBox.getSelectedItem();
                performAdminAction(adminChoice);
            }
        });
        adminFrame.add(actionButton);
        adminFrame.setVisible(true);
        adminFrame.setLayout(new FlowLayout());

        }


         private static void performAdminAction(String choice) {
            String[] specialitylist={"Big Data","Informatique Multimedia","Communication Multimedia","MIME","Audiovisuel et cinema"};
            String jdbcUrl = "jdbc:mysql://localhost:3306/clients";
            String username = "naghma";
            String password = "anaghimnaarf";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");


                try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                    String sql;
                    if (Arrays.asList(specialitylist).contains(choice)) {
                        sql = "SELECT * FROM client WHERE speciality = ?";
                    } else {
                        sql = "SELECT * FROM client WHERE clubname = ?";
                    }
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, choice);
    
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            JTable table = new JTable(buildTableModel(resultSet));
                            JOptionPane.showMessageDialog(null, new JScrollPane(table), "Query Results",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                }

            } 
            }catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error connecting to the database or executing the query.");
            }
            }

        //converting ResultSet to TableModel
        public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
            ResultSetMetaData metaData = rs.getMetaData();

            // Column names
            int columnCount = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            // Data of the table
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    row.add(rs.getObject(columnIndex));
                }
                data.add(row);
            }

            return new DefaultTableModel(data, columnNames);
            }
}

