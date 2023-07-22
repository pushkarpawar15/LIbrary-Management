import java.sql.*;
import java.util.Scanner;

public class libsys {
	//Set JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
//static final String DB_URL = "jdbc:mysql://localhost/companydb";
   static final String DB_URL = "jdbc:mysql://localhost/libsys?useSSL=false";
//  Database credentials
   static final String USER = "root";// add your user 
   static final String PASS = "mysql";// add password
   

   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   Scanner in = new Scanner(System.in);
   

// STEP 2. Connecting to the Database
   try{
      //STEP 2a: Register JDBC driver
      Class.forName(JDBC_DRIVER);
      //STEP 2b: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      //STEP 2c: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();

      menu(stmt, in);

      stmt.close();
      conn.close();
	}catch(SQLException se){    	 //Handle errors for JDBC
      	se.printStackTrace();
   	}catch(Exception e){        	//Handle errors for Class.forName
      e.printStackTrace();
   }finally{				//finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }					//end finally try
   }					//end try
   System.out.println("End of Code");
}

static void menu(Statement stmt,Scanner in)
{
   System.out.print("Login as\n (1)Student \n (2)Librarian\n (3)Admin\n (0)Exit\n");
   int choice = in.nextInt();
   switch(choice)
   {
      case 1:
         student_menu(stmt,in);
         break;
      case 2:
         if(auth_librarian(stmt, in))librarian_menu(stmt,in);
         else
         {
            System.out.println("Wrong id or password");
            menu(stmt,in);
         }
         break;
      case 3:
         admin_menu(stmt, in);
         break;
      case 0:
         System.out.print("Exiting...");
         System.exit(0);
         break;
      default:
         System.out.println("Invalid choice");
         menu(stmt,in);
   }
}

static void admin_menu(Statement stmt,Scanner in)
{
   System.out.println("Enter Admin Password");
   String pass = in.next();
   if(!pass.equals("admin"))
   {
      System.out.println("Invalid Password");
      menu(stmt,in);
   }
   System.out.println("Adimin login Successful");
   System.out.print("Admin Menu:\n(1) Add librarian\n(0) Exit\n");
   int choice = in.nextInt();
   switch(choice)
   {
      case 1:
         add_librarian(stmt, in);
         admin_menu(stmt, in);
         break;
      case 0:
         menu(stmt,in);
         break;
      default:
         System.out.println("Invalid choice");
         admin_menu(stmt,in);
   }
}

static boolean auth_librarian(Statement stmt,Scanner in)
{
   System.out.println("Enter librarian id:");
   String id = in.next();
   System.out.println("Enter password:");
   String password = in.next();
   String sql = "SELECT * FROM librarian WHERE id = " + id + " AND password = " + password;
   try{
      ResultSet rs = stmt.executeQuery(sql);
      if(rs.next())
      {
         System.out.println("Librarian login Successful");
         return true;
      }
      else
      {
         return false;
      }
   }catch(SQLException se){
      se.printStackTrace();
      return false;
   }
}

static void add_librarian(Statement stmt,Scanner in)
{
   System.out.println("Enter librarian id: ");
   String id = in.next();
   System.out.print("Enter librarian name: ");
   String name = in.next();
   System.out.print("Enter librarian password: ");
   String password = in.next();
   String sql = "INSERT INTO librarian (Name,Password,id) VALUES ('"+name+"','"+password+"','"+id+"')";
   try{
      stmt.executeUpdate(sql);
      System.out.println("Librarian added successfully");
      admin_menu(stmt,in);
   }catch(SQLException se){
      System.out.println("Error in adding librarian");
      admin_menu(stmt,in);
   }
}

static void librarian_menu(Statement stmt,Scanner in)
{
   System.out.println("Librarian Menu:\n(1)Add Student \n(2)Update Student Name \n(3)Remove Student \n(4)Add Book \n(5)Remove Book \n(6)Issue Book \n(7)Return Book\n(0)Exit");
   int choice = in.nextInt();
   switch(choice)
   {
      case 1:
         add_student(stmt,in);
         break;
      case 2:
         update_student_name(stmt,in);
         break;
      case 3:
         remove_student(stmt,in);
         break;
      case 4:
         add_book(stmt,in);
         break;
      case 5:
         remove_book(stmt,in);
         break;
      case 6:
         issue_book(stmt,in);
         break;
      case 7:
         return_book(stmt,in);
         break;
      case 0:
         menu(stmt,in);
         break;
      default:
         System.out.println("Invalid choice");
         librarian_menu(stmt,in);
   }
   librarian_menu(stmt,in);
}

static void update_student_name(Statement stmt,Scanner in)
{
   System.out.print("Enter Student ID: ");
   int id = in.nextInt();
   System.out.print("Enter New Name: ");
   String name = in.next();
   String sql = "UPDATE student SET Name = '"+name+"' WHERE id = "+id;
   try{
      stmt.executeUpdate(sql);
      System.out.println("Name Updated");
   }catch(SQLException se){
      System.out.println("Error updating name");
   }
}

static void remove_student(Statement stmt,Scanner in)
{
   System.out.print("Enter student id: ");
   int id = in.nextInt();
   String sql = "DELETE FROM student WHERE id = " + id;
   try{
      stmt.executeUpdate(sql);
      System.out.println("Student removed");
   }catch(SQLException se){
      System.out.println("Error: " + se.getMessage());
   }
}

static void remove_book(Statement stmt,Scanner in)
{
   System.out.print("Enter book id: ");
   int id = in.nextInt();
   String sql = "DELETE FROM book WHERE id = " + id;
   try{
      stmt.executeUpdate(sql);
      System.out.println("Book removed");
   }catch(SQLException se){
      System.out.println("Error: " + se.getMessage());
   }
}

static void issue_book(Statement stmt,Scanner in)
{
   System.out.println("Enter Student Id:");
   int id = in.nextInt();
   System.out.println("Enter Book Id:");
   int bid = in.nextInt();

   String sql = "SELECT * FROM book WHERE id = " + id + " AND student_id is not null";

   try{
      ResultSet rs = stmt.executeQuery(sql);
      if(rs.next())
      {
         System.out.println("Book is already issued");
         librarian_menu(stmt,in);
      }
   }
   catch(SQLException se){
      System.out.println("Error: " + se.getMessage());
   }

   sql = "UPDATE book SET student_id = " + id + " WHERE id = " + bid + " AND student_id is null";
   try{
      stmt.executeUpdate(sql);
      System.out.println("Book issued");
   }catch(SQLException se){
      System.out.println("Error: " + se.getMessage());
   }

}

static void return_book(Statement stmt,Scanner in)
{
   System.out.println("Enter Book Id:");
   int bid = in.nextInt();
   System.out.println("Enter Student Id:");
   int sid = in.nextInt();
   String sql = "UPDATE book SET student_id = null WHERE id = " + bid + " AND student_id = " + sid;
   try{
      stmt.executeUpdate(sql);
      System.out.println("Book returned");
   }catch(SQLException se){
      System.out.println("Error: " + se.getMessage());
   }
}

static void add_book(Statement stmt,Scanner in)
{
   System.out.print("Enter Book ID: ");
   String book_id = in.next();
   System.out.print("Enter Book Name: ");
   String book_name = in.next();
   String sql = "INSERT INTO book(id,title) VALUES ('"+book_id+"','"+book_name+"')";
   try{
      stmt.executeUpdate(sql);
      System.out.println("Book added successfully");
   }catch(SQLException se){
      System.out.println("Error in adding book");
      se.printStackTrace();
   }
}

static void add_student(Statement stmt,Scanner in)
{
   System.out.print("Enter Student Name: ");
   String name = in.next();
   System.out.print("Enter Student Roll Number: ");
   String roll_number = in.next();
   String sql = "INSERT INTO student (id,name) VALUES ('"+roll_number+"','"+name+"')";
   try{
      stmt.executeUpdate(sql);
      System.out.println("Student added successfully");
   }catch(SQLException se){
      System.out.println("Error in adding student");
   }
}



static void student_menu(Statement stmt,Scanner in)
{
   System.out.print("Select from options below:\n(1) Available books\n(0)Exit\n");
   int choice = in.nextInt();
   switch(choice)
   {
      case 1:
         available_books(stmt,in);
         break;
      case 0:
         menu(stmt,in);
         break;
      default:
         System.out.println("Invalid choice");
         student_menu(stmt,in);
   }
}

static void available_books(Statement stmt,Scanner in)
{
   String sql;
   sql = "SELECT id,title,student_id from book where student_id is null";
   try
   {
      ResultSet rs = stmt.executeQuery(sql);
      while(rs.next()){
      //Retrieve by column name
      String id  = rs.getString("id");
      String title = rs.getString("title");
      String student_id = rs.getString("student_id");

      //Display values
      System.out.println("id: " + id);
      System.out.println("Title: " + title);
   }
   rs.close();
   }
   catch(SQLException e){
      e.printStackTrace();
   }

   student_menu(stmt,in);
}


}
