import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Populate{
	public void deletedata() throws SQLException, IOException{
		Connection conn =null;
		Statement stmnt = null;
		try {
			 conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@dagobah.engr.scu.edu:1521:db11g", "gvemulap",
					"geetha24");
			 stmnt = conn.createStatement();
			 int delValue=stmnt.executeUpdate("DELETE FROM firebuilding");
			 System.out.println("delete value from firebuilding "+ delValue);
			 int delValue1=stmnt.executeUpdate("DELETE FROM firehydrant");
			 System.out.println("delete value from firehydrant "+ delValue1);
			 int delValue2=stmnt.executeUpdate("DELETE FROM building");
			 System.out.println("delete value from building"+ delValue2);
			 stmnt.close();
			 
				 
		} catch (SQLException e) {
			System.out.println(e);
 		}finally{
 			conn.close();
 		}
	}
	
	public void populateBuildingSCU() throws SQLException, IOException{
		Connection conn =null;
		Statement stmnt = null;
		try {
			 conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@dagobah.engr.scu.edu:1521:db11g", "gvemulap",
					"geetha24");
			 stmnt = conn.createStatement();
			// int delValue=stmnt.executeUpdate("DELETE FROM building");
			// System.out.println("delete value "+ delValue);
			 stmnt.close();
			 BufferedReader br=new BufferedReader(new FileReader("InputFiles/building.xy"));
			 String row;
			 
			 while((row = br.readLine())!=null){
				 stmnt = conn.createStatement();
				 String buildingId;
				 String buildingName;
				 String shape="";
				 String[] cellData;
				 cellData = row.split(",");
		         buildingId = cellData[0];
				 buildingName = cellData[1].trim();
				 for(int i = 3; i < cellData.length; i++){
					shape += cellData[i] + ",";
				 }
				 shape += cellData[3] + ",";
				 shape += cellData[4];
				 stmnt.executeUpdate("INSERT INTO building VALUES('"+ buildingId +"',+'"+buildingName+"',MDSYS.SDO_GEOMETRY(2003,NULL, NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),"
				 		+ "MDSYS.SDO_ORDINATE_ARRAY("+shape+")))");
				 stmnt.close();
			 }
			 System.out.println("Building data is popuated");
			 br.close();
		} catch (SQLException e) {
			System.out.println(e);
 		}finally{
 			conn.close();
 		}
	}
	
	public void populateFirehydrant() throws SQLException, IOException{
		Connection conn =null;
		Statement stmnt = null;
		try {
			 conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@dagobah.engr.scu.edu:1521:db11g", "gvemulap",
					"geetha24");
			 stmnt = conn.createStatement();
			// int delValue=stmnt.executeUpdate("DELETE FROM firehydrant");
			// System.out.println("delete value "+ delValue);
			 stmnt.close();
			 BufferedReader br=new BufferedReader(new FileReader("InputFiles/hydrant.xy"));
			 String row;
			 
			 while((row = br.readLine())!=null){
				 stmnt = conn.createStatement();
				 String firehydrantId;
				 String[] cellData;
				 cellData = row.split(",");
				 firehydrantId = cellData[0];
				 String shape = cellData[1] + "," + cellData[2];
				 stmnt.executeUpdate("INSERT INTO firehydrant VALUES('"+ firehydrantId +"',"
				 		+ "MDSYS.SDO_GEOMETRY(2001,NULL, MDSYS.SDO_POINT_TYPE(" +shape+", NULL), NULL,NULL))");
				 stmnt.close();
			 }
			 System.out.println("firehydrant data is popuated");
			 br.close();
		} catch (SQLException e) {
			System.out.println(e);
 		}finally{
 			conn.close();
 		}
	}
	
	public void populateFirebuilding() throws SQLException, IOException{
		Connection conn =null;
		Statement stmnt = null;
		try {
			 conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@dagobah.engr.scu.edu:1521:db11g", "gvemulap",
					"geetha24");
			 stmnt = conn.createStatement();
			// int delValue=stmnt.executeUpdate("DELETE FROM firebuilding");
			// System.out.println("delete value "+ delValue);
			 stmnt.close();
			 BufferedReader br=new BufferedReader(new FileReader("InputFiles/firebuilding.txt"));
			 String row;
			 
			 while((row = br.readLine())!=null){
				 stmnt = conn.createStatement();
				 String name;
				 
				 String[] cellData;
				
				 cellData = row.split(",");
				 name = cellData[0];
				
				
				 stmnt.executeUpdate("INSERT INTO firebuilding VALUES('"+ name +"')");

				 stmnt.close();
			 }
			 System.out.println("firebuilding data is popuated");
			 br.close();
		} catch (SQLException e) {
			System.out.println(e + " here");
 		}finally{
 			conn.close();
 		}
	}
	
	public static void main(String[] args) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your Oracle JDBC Driver?");
			return;
		}
		System.out.println("Oracle JDBC Driver Registered!");
		Populate p = new Populate();
		try {
			p.deletedata();
			p.populateBuildingSCU();
			p.populateFirehydrant();
			p.populateFirebuilding();
		} catch (SQLException e1) {
			System.out.println("e1 "+ e1);
		} catch (IOException e2) {
			System.out.println("e2 "+e2);
		}catch(Exception e){
			System.out.println(e + " e");
		}
		
	}
}
