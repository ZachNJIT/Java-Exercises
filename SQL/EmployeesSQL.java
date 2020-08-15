// 23.4 EmployeesSQL.java
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class EmployeesSQL extends JFrame {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData rsMetaData;
    private Container container;
    private JTable table;
    private JTextField input;
    private JButton addSalariedEmployee, addCommissionEmployee,
            addBasePlusCommissionEmployee, addHourlyEmployee;

    public EmployeesSQL()
    {
        super( "Add Employees" );

// The URL specifying the books database to which this program
// connects to using JDBC
        String url = "jdbc:mysql://localhost/employees";

// Load the driver to allow connection to the database
        try {
            //Class.forName( "com.mysql.cj.jdbc.Driver" );

            connection = DriverManager.getConnection(url, "deitel", "deitel");
            //Connection connection = DriverManager.getConnection(url, "deitel", "deitel");
            statement = connection.createStatement();
        }
        /*catch ( ClassNotFoundException cnfex ) {
           System.err.println( "Failed to load JDBC driver." );
            cnfex.printStackTrace();
            System.exit( 1 ); // terminate program
        }*/
        catch ( SQLException sqlex ) {
            System.err.println( "Unable to connect" );
            sqlex.printStackTrace();
            System.exit( 1 ); // terminate program
        }

// if connected to database, set up GUI
        JPanel topPanel = new JPanel();
        topPanel.setLayout( new FlowLayout() );
        topPanel.add( new JLabel( "Enter query to insert employees:" ) );

        input = new JTextField( 50 );
        topPanel.add( input );
        input.addActionListener(

                new ActionListener() {

                    public void actionPerformed( ActionEvent e )
                    {
                        addEmployee( input.getText() );
                    }
                }
        );

// create four buttons that allow user to add specific employee
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout( new FlowLayout() );

        addSalariedEmployee = new JButton( "Add Salaried Employee" );
        addSalariedEmployee.addActionListener( new ButtonHandler() );

        addCommissionEmployee = new JButton( "Add Commission Employee" );
        addCommissionEmployee.addActionListener( new ButtonHandler() );

        addBasePlusCommissionEmployee =
                new JButton( "Add Base Plus Commission Employee" );
        addBasePlusCommissionEmployee.addActionListener(
                new ButtonHandler() );

        addHourlyEmployee = new JButton( "Add Hourly Employee" );
        addHourlyEmployee.addActionListener( new ButtonHandler() );

// add four buttons to centerPanel
        centerPanel.add( addSalariedEmployee );
        centerPanel.add( addCommissionEmployee );
        centerPanel.add( addBasePlusCommissionEmployee );
        centerPanel.add( addHourlyEmployee );

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout( new BorderLayout() );
        inputPanel.add( topPanel, BorderLayout.NORTH );
        inputPanel.add( centerPanel, BorderLayout.CENTER );

        table = new JTable( 4, 4 );

        container = getContentPane();
        container.setLayout( new BorderLayout() );
        container.add( inputPanel, BorderLayout.NORTH );
        container.add( table, BorderLayout.CENTER );

        getTable();

        setSize( 800, 300 );
        setVisible( true );

    } // end constructor AddEmployees

    private void getTable()
    {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery( "SELECT * FROM employees" );
            displayResultSet( resultSet );
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
        }
    }

    private void addEmployee( String query )
    {
        try {
            statement = connection.createStatement();
            statement.executeUpdate( query );
            getTable();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
        }
    }

    private void displayResultSet( ResultSet rs ) throws SQLException
    {
// position to first record
        boolean moreRecords = rs.next();

// if there are no records, display a message
        if ( !moreRecords ) {
            JOptionPane.showMessageDialog( this,
                    "ResultSet contained no records" );
            return;
        }

        Vector columnHeads = new Vector();
        Vector rows = new Vector();

        try {
// get column heads
            ResultSetMetaData rsmd = rs.getMetaData();

            for ( int i = 1; i <= rsmd.getColumnCount(); ++i )
                columnHeads.addElement( rsmd.getColumnName( i ) );

// get row data
            do {
                rows.addElement( getNextRow( rs, rsmd ) );
            } while ( rs.next() );

// display table with ResultSet contents
            table = new JTable( rows, columnHeads );
            JScrollPane scroller = new JScrollPane( table );
            container.remove( 1 );
            container.add( scroller, BorderLayout.CENTER );
            container.validate();

        } // end try

        catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
        }

    } // end method displayResultSet

    private Vector getNextRow( ResultSet rs,
                               ResultSetMetaData rsmd ) throws SQLException
    {
        Vector currentRow = new Vector();

        for ( int i = 1; i <= rsmd.getColumnCount(); ++i )
            switch( rsmd.getColumnType( i ) ) {
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    currentRow.addElement( rs.getString( i ) );
                    break;
                case Types.INTEGER:
                    currentRow.addElement( new Long( rs.getLong( i ) ) );
                    break;
                case Types.REAL:
                    currentRow.addElement( new Float( rs.getDouble( i ) ) );
                    break;
                case Types.DATE:
                    currentRow.addElement( rs.getDate( i ) );
                    break;
                default:
                    System.out.println( "Type was: " +
                            rsmd.getColumnTypeName( i ) );
            }

        return currentRow;

    } // end method getNextRow

    public void shutDown()
    {
        try {
            connection.close();
        }
        catch ( SQLException sqlex ) {
            System.err.println( "Unable to disconnect" );
            sqlex.printStackTrace();
        }
    }

    public static void main( String[] args )
    {
        final EmployeesSQL application = new EmployeesSQL();
        application.addWindowListener(

                new WindowAdapter() {

                    public void windowClosing( WindowEvent e )
                    {
                        application.shutDown();
                        System.exit( 0 );
                    }
                }
        );
    }

    // inner class ButtonHandler handles button event
    private class ButtonHandler implements ActionListener {

        public void actionPerformed( ActionEvent event )
        {
            String socialSecurityNumber = JOptionPane.showInputDialog(
                    "Employee Social Security Number" );
            String insertQuery = "", displayQuery = "";

// add salaried employee to table salariedEmployee
            if ( event.getSource() == addSalariedEmployee ) {
                double weeklySalary = Double.parseDouble(
                        JOptionPane.showInputDialog( "Weekly Salary:" ) );
                insertQuery = "INSERT INTO salariedEmployees VALUES ( '" +
                        socialSecurityNumber + "', '" + weeklySalary + "', '0' )";
                displayQuery = "SELECT employees.socialSecurityNumber, " +
                        "employees.firstName, employees.lastName, " +
                        "employees.employeeType, salariedEmployees.weeklySalary" +
                        " FROM employees, salariedEmployees WHERE " +
                        "employees.socialSecurityNumber = " +
                        "salariedEmployees.socialSecurityNumber";
            }

// add commission employee to table commissionEmployee
            else if ( event.getSource() == addCommissionEmployee ) {
                int grossSales = Integer.parseInt(
                        JOptionPane.showInputDialog( "Gross Sales:" ) );
                double commissionRate = Double.parseDouble(
                        JOptionPane.showInputDialog( "Commission Rate:" ) );
                insertQuery = "INSERT INTO commissionEmployees VALUES ( '" +
                        socialSecurityNumber + "', '" + grossSales + "', '" +
                        commissionRate + "', '0' )";
                displayQuery = "SELECT employees.socialSecurityNumber, " +
                        "employees.firstName, employees.lastName, " +
                        "employees.employeeType, commissionEmployees.grossSales," +
                        " commissionEmployees.commissionRate FROM employees, " +
                        "commissionEmployees WHERE employees.socialSecurityNumber="
                        + "commissionEmployees.socialSecurityNumber";
            }

// add base plus commission employee to table
// basePlusCommissionEmployee
            else if ( event.getSource() == addBasePlusCommissionEmployee ) {
                int grossSales = Integer.parseInt(
                        JOptionPane.showInputDialog( "Gross Sales:" ) );
                double commissionRate = Double.parseDouble(
                        JOptionPane.showInputDialog( "Commission Rate:" ) );
                double baseSalary = Double.parseDouble(
                        JOptionPane.showInputDialog( "Base Salary:" ) );
                insertQuery = "INSERT INTO basePlusCommissionEmployees " +
                        "VALUES ( '" + socialSecurityNumber + "', '" + grossSales +
                        "', '" + commissionRate + "', '" + baseSalary + "', '0' )";
                displayQuery = "SELECT employees.socialSecurityNumber, " +
                        "employees.firstName, employees.lastName, employees." +
                        "employeeType, basePlusCommissionEmployees.baseSalary, " +
                        "basePlusCommissionEmployees.grossSales, basePlus" +
                        "CommissionEmployees.commissionRate FROM employees, " +
                        "basePlusCommissionEmployees WHERE " +
                        "employees.socialSecurityNumber = " +
                        "basePlusCommissionEmployees.socialSecurityNumber";
            }

// add hourly employee to table hourlyEmployee
            else {
                int hours = Integer.parseInt(
                        JOptionPane.showInputDialog( "Hours:" ) );
                double wage = Double.parseDouble(
                        JOptionPane.showInputDialog( "Wage:" ) );
                insertQuery = "INSERT INTO hourlyEmployees VALUES ( '" +
                        socialSecurityNumber + "', '" + hours + "', '" + wage +
                        "', '0' )";
                displayQuery = "SELECT employees.socialSecurityNumber, " +
                        "employees.firstName, employees.lastName, " +
                        "employees.employeeType, hourlyEmployees.hours, " +
                        "hourlyEmployees.wage FROM employees, hourlyEmployees " +
                        "WHERE employees.socialSecurityNumber = " +
                        "hourlyEmployees.socialSecurityNumber";
            }

// execute insert query and display employee info
            try {
                statement = connection.createStatement();
                statement.executeUpdate( insertQuery );

// display the employee info
                statement = connection.createStatement();
                resultSet = statement.executeQuery( displayQuery );
                displayResultSet( resultSet );
            }
            catch ( SQLException exception ) {
                exception.printStackTrace();
            }

        } // end method actionPerformed

    } // end inner class ButtonHandler

} // end class AddEmployees