import java.lang.Thread.State;
import java.sql.*;
import java.util.Scanner;

public class Hospital {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/Hospital?allowPublicKeyRetrieval=true&useSSL=false";
    static final String USER = "root";// add your user 
    static final String PASS = "admin";// add password

    public static void main(String[] args) {
        Connection conn = null;
        Statement stat = null;
        
        // STEP 2. Connecting to the Database
        
        try{
            //STEP 2a: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //STEP 2b: Open a connection
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            //STEP 2c: Execute a query
            stat = conn.createStatement();
            Scanner scan = new Scanner(System.in);

            main_menu(stat, scan);

            //STEP 5: Clean-up environment
            scan.close();
            stat.close();
            conn.close();
        }catch(SQLException se){    	 //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){        	//Handle errors for Class.forName
            e.printStackTrace();
        }finally{				//finally block used to close resources
            try{
                if(stat!=null)
                    stat.close();
            }catch(SQLException se2){

            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }					//end finally try
        }					//end try
    }		

    static void main_menu(Statement stat, Scanner scan){
        System.out.println("..................... Welcome to Hospital Managment System .....................");

        System.out.println("Login the Hospital as a:");
        System.out.println("1. Patient/Customer");
        System.out.println("2. Hospital Staff");
        System.out.println("3. Hospital Admin");
        System.out.println("0. Exit\n");

        System.out.print("Enter your choice : ");
        int option = Integer.parseInt(scan.nextLine());

        switch(option){
            case 1:
                patient_options(stat, scan);
                break;

            case 2:
                check_staff(stat, scan);
                break;

            case 3:
                check_hospital_admin(stat, scan);
                break;

            case 0:
                System.exit(0);

            default:
                System.out.println("Enter a valid choice\n");
                break;
        }
        main_menu(stat, scan);
    }

    static void patient_options(Statement stat, Scanner scan){
        System.out.println("\nSelect an appropiate option:");
        System.out.println("1. List of medicines available");
        System.out.println("0. Exit\n");

        System.out.print("Enter you option: ");
        int option = Integer.parseInt(scan.nextLine());
        switch(option){
            case 1:
                available_medicines(stat, scan, true);
                break;
            
            case 0:
                return;

            default:
                System.out.println("Enter a valid choice\n");
                break;
        }
        patient_options(stat, scan);
    }
    

    static void check_staff(Statement stat, Scanner scan){
        boolean isAuthenticated = false;

        System.out.print("Enter id: ");
        String id = scan.nextLine();
        System.out.print("Enter password: ");
        String password = scan.nextLine();

        
        String sql = "select * from hospital_staff";
        ResultSet rs = executeSqlStat(stat, sql);

        try{
            while(rs.next()){
                String id1 = rs.getString("hospital_staff_id");
                String password1 = rs.getString("hospital_staff_password");

                if(id.equals(id1) && password.equals(password1)){
                    isAuthenticated = true;
                    break;
                }
            }
        }catch(SQLException se){
            //se.printStackTrace();
        }
        
        if(isAuthenticated == true){
            hospital_staff_menu(stat, scan);
        }
    
        else{
            System.out.println("Details entered were incorrect.Do you wanna try again(Y/N)");
            String output = scan.nextLine();
            if(output.equals("Y") || output.equals("y")){
                check_staff(stat, scan);
            }
            else{
                return;
            }
        }
    }
    
    static void check_hospital_admin(Statement stat,Scanner scan){
        boolean isAuthenticated = false;

        System.out.print("Enter id: ");
        String id = scan.nextLine();
        System.out.print("Enter password: ");
        String password = scan.nextLine();

        
        String sql = "select * from super_admin";
        ResultSet rs = executeSqlStat(stat, sql);

        try{
            while(rs.next()){
                String id1 = rs.getString("super_admin_id");
                String password1 = rs.getString("super_admin_password");

                if(id.equals(id1) && password.equals(password1)){
                    isAuthenticated = true;
                    break;
                }
            }
        }catch(SQLException se){
            //se.printStackTrace();
        }
        
        if(isAuthenticated == true){
            super_admin_menu(stat, scan);
        }
        
        else{
            System.out.print("Details entered were incorrect.Do you wanna try again(Y/N)");
            String output = scan.nextLine();
            if(output.equals("Y") || output.equals("y")){
                check_hospital_admin(stat, scan);
            }
            else{
                return;
            }
        }
    }

    static void hospital_staff_menu(Statement stat, Scanner scan){
        System.out.println("Select an appropiate option:");
        System.out.println("1. List of all medicines");
        System.out.println("2. List of available medicines");
        System.out.println("3. Add a medicine");
        System.out.println("4. Delete a medicine");
        System.out.println("5. Take a medicine ");
        System.out.println("6. Return a medicine ");
        System.out.println("0. Exit\n");

        System.out.print("Enter your option: ");
        int option = Integer.parseInt(scan.nextLine());
        switch (option) {
            case 1:
                available_medicines(stat, scan, false);
                break;
            
            case 2:
                available_medicines(stat, scan, true);
                break;
            
            case 3:
                add_medicine(stat, scan);
                break;

            case 4:
                del_medicine(stat, scan);
                break;
            
            case 5:
                issue_medicine(stat, scan);
                break;
            
            case 6:
                return_medicine(stat, scan);
                break;

            case 0:
                return;
            
            default:
                System.out.println("Enter a valid choice\n");
                break;
        }
        hospital_staff_menu(stat, scan);
    }

    static void super_admin_menu(Statement stat, Scanner scan){
        System.out.println("Select an appropiate option:");
        System.out.println("1. List of all patients");
        System.out.println("2. List of all hospital staffs");
        System.out.println("3. Add ");
        System.out.println("4. Delete ");
        System.out.println("0. Exit\n");

        System.out.print("Enter your option: ");
        int option = Integer.parseInt(scan.nextLine());
        switch (option) {
            case 1:
                list_of_patients(stat, scan);
                break;

            case 2:
                list_of_hospital_staff(stat, scan);
                break;
                
            case 3:
                add(stat, scan);
                break;
                
            case 4:
                del(stat, scan);
                break;
            
            case 0:
                return;

            default:
                System.out.println("Enter a valid choice\n");
                break;
        }
        super_admin_menu(stat, scan);
    }

    static boolean available_medicines(Statement stat, Scanner scan, boolean available){
        String sql = "select * from medicine";
        ResultSet rs = executeSqlStat(stat, sql);
        boolean available_meds = true;

        try{
            System.out.println("List of medicines: ");
            while(rs.next()){
                String id = rs.getString("med_id");
                String name = rs.getString("med_name");
                String disease = rs.getString("which_disease");
                Integer year = rs.getInt("med_year");
                String buyer = rs.getString("buyer_of_med");

                if(available){
                    if(buyer == null){
                        //System.out.println("List of available medicines:\n");

                        System.out.println("Medicine_ID: "+ id);
                        System.out.println("Medicine_name: "+ name);
                        System.out.println("Disease of the patient: "+ disease);
                        System.out.println("Year of the medicine: "+ year);
                        available_meds = false;
                    }
                }

                else{
                    //System.out.println("list of all medicines:\n");

                    System.out.println("Medicine_ID: "+ id);
                    System.out.println("Medicine_name: "+ name);
                    System.out.println("Disease of the patient: "+ disease);
                    System.out.println("Year of the medicine: "+ year);
                    System.out.println("buyer of meds: "+ buyer);
                    available_meds = false;
                }
            }

            if(available_meds){
                System.out.println("No medicines are available\n");
            }
            rs.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return available_meds;
    }
    
    static void issue_medicine(Statement stat, Scanner scan){
        try{
            boolean issue = available_medicines(stat, scan, true);
            if(!issue){
                System.out.print("\nEnter medicine id: ");
                String id1 = scan.nextLine();
                System.out.print("\nEnter patient id: ");
                String id2 = scan.nextLine();

                String sql = String.format("UPDATE medicine SET buyer_of_med = '%s' WHERE med_id = '%s'", id2, id1);
                String sql1 = String.format("UPDATE patient SET med_id = '%s' WHERE patient_id = '%s'", id1, id2);
                int result = updateSqlStat(stat, sql);
                int result1 = updateSqlStat(stat, sql1);
                if(result != 0){
                    System.out.println("Buyer updated successfully\n");
                }
                else{
                    System.out.println("Buyer not updated successfully\n");
                }
            }
        }catch(Exception e){
            //e.printStackTrace();
        }
    }
    
    static void return_medicine(Statement stat, Scanner scan){
        try{
            System.out.print("\nEnter medicine id: ");
            String id = scan.nextLine();
            System.out.print("\nEnter patient id: ");
            String id2 = scan.nextLine();

            String sql = String.format("UPDATE medicine SET buyer_of_med = NULL WHERE med_id = '%s'", id);
            String sql2 = String.format("UPDATE patient SET med_id = NULL WHERE patient_id = %s", id2);
            int result = updateSqlStat(stat, sql);
            int result2 = updateSqlStat(stat, sql2);
            if(result != 0){
                System.out.println("Medicine returned successfully\n");
            }
            else{
                System.out.println("Medicine not returned successfully\n");
            }
        }catch(Exception e){

            //e.printStackTrace();
        }
    }

    static void add_medicine(Statement stat, Scanner scan){
        try{
            System.out.print("\nEnter medicine id: ");
            String id = scan.nextLine();
            System.out.print("\nEnter medicine name: ");
            String name = scan.nextLine();
            System.out.print("\nEnter the disease name: ");
            String disease = scan.nextLine();
            System.out.print("\nEnter the expiry year of medicine: ");
            Integer year = Integer.parseInt(scan.nextLine()); 

            String sql = String.format("insert into medicine values ('%s', '%s', '%s', '%d', NULL);", id, name, disease, year);
            int result = updateSqlStat(stat, sql);

            if(result == 0){
                System.out.println("Medicine not added successfully\n");
            }
            else{
                System.out.println("Medicine added successfully");
            }
        }catch(Exception e){
            //e.printStackTrace();
        }
    }

    static void del_medicine(Statement stat, Scanner scan){
        try{
            System.out.print("\nEnter medicine id: ");
            String id = scan.nextLine();

            String sql = String.format("delete from medicine WHERE med_id = '%s'",id);
            int result = updateSqlStat(stat, sql);

            if(result == 0){
                System.out.println("Medicine not deleted successfully\n");
            }
            else{
                System.out.println("Medicine deleted successfully");
            }
        }catch(Exception e){
            //e.printStackTrace();
        }
    }

    static void list_of_patients(Statement stat, Scanner scan){
        String sql = "select * from patient";
        ResultSet rs = executeSqlStat(stat, sql);

        try{
            System.out.println("List of patients:\n");
            while(rs.next()){
                String id = rs.getString("patient_id");
                String name = rs.getString("patient_name");

                System.out.println("Patient id: "+ id);
                System.out.println("Patient name: "+ name);
                System.out.println("\n");
            }
            rs.close();
        }catch(SQLException se){
            //se.printStackTrace();
        }
    }

    static void list_of_hospital_staff(Statement stat, Scanner scan){
        String sql = "select * from hospital_staff";
        ResultSet rs = executeSqlStat(stat, sql);

        try{
            System.out.println("List of hospital staff:\n");
            while(rs.next()){
                String id = rs.getString("hospital_staff_id");
                String name = rs.getString("hospital_staff_name");

                System.out.println("Staff id: "+ id);
                System.out.println("Staff name: "+ name);
                System.out.println("\n");
            }
            rs.close();
        }catch(SQLException se){
            //se.printStackTrace();
        }
    }

    static void add(Statement stat, Scanner scan){
        System.out.println("1. Add a patient");
        System.out.println("2. Add a hospital staff");
        System.out.println("0. Exit\n");

        System.out.print("Enter you option: ");
        int option = Integer.parseInt(scan.nextLine());

        switch(option){
            case 0:
                return;

            case 1:
                try{
                    System.out.print("Enter patient id: ");
                    String id = scan.nextLine();
                    System.out.print("Enter patient name: ");
                    String name = scan.nextLine();
    
                    String sql = String.format("insert into patient values ('%s', '%s', NULL);", id, name);
                    int result = updateSqlStat(stat, sql);
                    if(result == 0){
                        System.out.println("Patient not added successfully\n");
                    }
                    else{
                        System.out.println("Patient added successfully\n");
                    }
                }catch(Exception e){
                    //e.printStackTrace();
                }
                break;

            case 2:
                try{
                    System.out.print("Enter hospital staff id: ");
                    String id = scan.nextLine();
                    System.out.print("Enter hospital staff name: ");
                    String name = scan.nextLine();
                    System.out.print("Enter hospital staff password: ");
                    String password = scan.nextLine();
    
                    String sql = String.format("insert into hospital_staff values ('%s', '%s', '%s')", id, name, password);
                    int result = updateSqlStat(stat, sql);
                    if(result == 0){
                        System.out.println("Staff not added successfully\n");
                    }
                    else{
                        System.out.println("Staff added successfully\n");
                    }
                }catch(Exception e){
                    //e.printStackTrace();
                }
                break;
            
            default:
                System.out.println("Enter a valid option: ");
                break;
        }
    }

    static void del(Statement stat, Scanner scan){
        System.out.println("1. Delete a patient");
        System.out.println("2. Delete a hospital staff");
        System.out.println("0. Exit\n");

        System.out.print("Enter you option: ");
        int option = Integer.parseInt(scan.nextLine());
        switch(option){
            case 0:
                return;

            case 1:
                try{
                    System.out.print("Enter patient id: ");
                    String id = scan.nextLine();
    
                    String sql = String.format("delete from patient where patient_id = '%s'", id);
                    int result = updateSqlStat(stat, sql);
                    if(result == 0){
                        System.out.println("Patient not deleted successfully\n");
                    }
                    else{
                        System.out.println("Patient deleted successfully\n");
                    }
                }catch(Exception e){
                    //e.printStackTrace();
                }
                break;

            case 2:
                try{
                    System.out.print("Enter staff id: ");
                    String id = scan.nextLine();
    
                    String sql = String.format("delete from hospital_staff where hospital_staff_id = '%s'", id);
                    int result = updateSqlStat(stat, sql);
                    if(result == 0){
                        System.out.println("Staff not deleted successfully\n");
                    }
                    else{
                        System.out.println("Staff deleted successfully\n");
                    }
                }catch(Exception e){
                    //e.printStackTrace();
                }
                break;

            default:
                System.out.println("Enter a valid option: ");
                break;
        }

    }

    static ResultSet executeSqlStat(Statement stat, String sql){
        try{
            ResultSet rs = stat.executeQuery(sql);
            return rs;
        }catch(SQLException se){
            //se.printStackTrace();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return null;
    }

    static int updateSqlStat(Statement stat, String sql){
        try{
            int rs = stat.executeUpdate(sql);
            return rs;
        }catch(SQLException se){
            //se.printStackTrace();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return 0;
    }
}    