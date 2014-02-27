import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;


public class P2 
{
	public static void main (String [] args) throws FileNotFoundException 
	{
		// Section 1: Load the driver
		try 
		{
			// Load the driver (registers itself)
	        Class.forName ("com.mysql.jdbc.Driver");
	    } 
	      
		catch (Exception E) 
		{
			System.err.println ("Unable to load driver.");
	        E.printStackTrace ();
	    }
		
		try 
		{
			// Section 2. Connect to the databse
	        Connection conn1; // An object of type connection 
	        String dbUrl = "jdbc:mysql://csdb.cs.iastate.edu:3306/lambacamDB";
	        String user = "lambacam";
	        String password = "lambacam-23";
	        conn1 = DriverManager.getConnection (dbUrl, user, password);
	        System.out.println ("*** Connected to the database ***"); 

	        // Section 3A. Create stmt1 object conn1
	        Statement stmt1 = conn1.createStatement ();

	        PrintWriter file = new PrintWriter("P2Output.txt");
	        
	        // Execute a query, receive result in a result set
	        ResultSet rs1 = stmt1.executeQuery("select " +
	        		"p.Name, i.Salary from Person p, Instructor i" +
	        		" where p.ID = i.InstructorID");

		    // ****************************************
	        //A. Print Current Salaries of Instructors
		    // ****************************************

	        System.out.println ( );	
	        file.println();
	        System.out.println ("Name           Salary");		
	        System.out.println ("----           ------");		
	        file.println("Name           Salary");
	        file.println("----           ------");
	        //Print report:
	        int totalPayroll = 0; 

	        String jName; // To store value of Name attribute 
	        int jSalary; // To store value of Salary attribute
	        while(rs1.next()) 
	        {
	        	// Access and print contents of one tuple
	            jName = rs1.getString (1); // Value accessed by its position
	            jSalary = rs1.getInt ("Salary"); // Access by attribute Name
	            System.out.println (jName + "     " + jSalary);
	            file.println(jName + "     " + jSalary);
	            totalPayroll = (int) (totalPayroll + jSalary);
	        }			
 
	        System.out.println ( );		
	        System.out.println ("Total of all salaries: " + totalPayroll);
	        file.println();
	        file.println();
	        file.println("Total of all salaries: " + totalPayroll);
	        System.out.println ( );
	        stmt1.close (); 

	        // ****************************
	        // B. Compute the MeritList  
	        // ****************************
	        Statement stmt2 = conn1.createStatement();
	        
	       //stmt2.executeUpdate("drop table MeritList");

	        String sql = "create table MeritList " +
	                   "(StudentID char (9) not null, " +
	                   " Classification char (10), " + 
	                   " MentorID char (9), " + 
	                   " GPA double, " +
	                   " Primary Key (StudentID) " + 
	                   ")"; 

	        stmt2.executeUpdate(sql);
	        stmt2.executeUpdate("insert into MeritList" + " select s.StudentID, s.Classification, s.MentorID, s.GPA from Student s order by s.GPA DESC limit 20");
	        
	        ResultSet rs2 = stmt2.executeQuery("select * from MeritList m order by m.GPA DESC");
	        String jSID = "";
	        String jSClass = "";
	        String jMID = "";
	        double jSGPA = 0.0;
	        
	        System.out.println ( );	
	        file.println();
	        System.out.println ("StudentID           Classification           MentorID           GPA");		
	        System.out.println ("---------           --------------           --------           ---");		
	        file.println("StudentID           Classification           MentorID           GPA");
	        file.println("---------           --------------           --------           ---");
	        
	        // **********************************
	        // C. Print contents of the MeritList  
	        // **********************************
	        
	        while(rs2.next()) 
	        {
	            jSID = rs2.getString (1);
	            jSClass = rs2.getString (2);
	            jMID = rs2.getString (3);
	            jSGPA = rs2.getDouble("GPA");
	            System.out.println (jSID + "           " + jSClass + "                 " + jMID + "           " + jSGPA);
	            file.println(jSID + "           " + jSClass + "                 " + jMID + "           " + jSGPA);
	        }	
	        
	        System.out.println ( ); 
	        System.out.println ( );		
	        file.println();
	        file.println();
	        stmt2.close (); 
	        
	        // ******************************
	        // D. Update Instructor Salaries  
	        // ******************************
	        
	        Statement stmt3 = conn1.createStatement();
	        String jIName = "";
	        int jISalary = 0;
	        
	        stmt3.execute("update Instructor, MeritList set Instructor.Salary = (Instructor.Salary + (Instructor.Salary * 0.04)) " +
	            		" where MeritList.MentorID = Instructor.InstructorID and MeritList.Classification = 'Freshman'");
	        
	        stmt3.execute("update Instructor, MeritList set Instructor.Salary = (Instructor.Salary + (Instructor.Salary * 0.06)) " +
	            		"where MeritList.MentorID = Instructor.InstructorID and MeritList.Classification = 'Sophomore'");
	    
	        stmt3.execute("update Instructor, MeritList set Instructor.Salary = (Instructor.Salary + (Instructor.Salary * 0.08)) " +
	        		"where MeritList.MentorID = Instructor.InstructorID and MeritList.Classification = 'Junior'");
	        
	        stmt3.execute("update Instructor, MeritList set Instructor.Salary = (Instructor.Salary + (Instructor.Salary * 0.1)) " +
	        		"where MeritList.MentorID = Instructor.InstructorID and MeritList.Classification = 'Senior'");
	
	        
	        // *****************************************
	        // E. Print Revised Salaries of Instructors
		    // *****************************************

	        ResultSet rs4 = stmt3.executeQuery("select " +
	        		"p.Name, i.Salary, i.InstructorID" +
	        		" from Person p, Instructor i" +
	        		" where p.ID = i.InstructorID");
	        
	        System.out.println ( );	
	        file.println();
	        System.out.println ("Name           Salary");		
	        System.out.println ("----           ------");		
	        file.println("Name           Salary");
	        file.println("----           ------");

	        int newTotalPayroll = 0; 

	        while(rs4.next()) 
	        {
	            jIName = rs4.getString (1);
	            jISalary = rs4.getInt ("Salary");
	            System.out.println (jIName + "   " + jISalary);
	            file.println(jIName + "   " + jISalary);
	            newTotalPayroll = (int) (newTotalPayroll + jISalary);
	        }			

	        System.out.println ( ); 
	        System.out.println ( );		
	        System.out.println ("Total of all salaries: " + newTotalPayroll);
	        file.println();
	        file.println();
	        file.println("Total of all salaries: " + newTotalPayroll);
	        
	        stmt3.executeUpdate("drop table MeritList");
	        stmt3.close (); 
	        file.close();
	        conn1.close (); 
	        
	        

	     } // End of try

	     catch (SQLException E) {
	        System.out.println ("SQLException: " + E.getMessage());
	        System.out.println ("SQLState: " + E.getSQLState());
	        System.out.println ("VendorError: " + E.getErrorCode());
	         
	     } // End of catch
	}
}
