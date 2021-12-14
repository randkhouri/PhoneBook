import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;

public class Phonebook {
	
	private static Scanner inScanner = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {

		//start of menu
		System.out.println("What would you like to do?");
		System.out.println("1. Create new contact ");
		System.out.println("2. Search for contact");
		System.out.println("3. Delete contact");
		
		//looping through switch statements
		loop : while(inScanner.hasNextInt()) {
		int choice = inScanner.nextInt();
		inScanner.nextLine();
		
				switch (choice) {
				case 0:
					break loop;
				//for creating new contact
				case 1:
					System.out.println("Please add new contact name: ");
					String name = inScanner.nextLine();
					System.out.println("Please add new number: (use format xxx-xxx-xxxx)");
					String phone = inScanner.nextLine();
					addNewContact(name, phone);
					System.out.println("Added new contact: " +name +": "+phone);
					break;
				

				//for creating searching through contacts, by name or number
				case 2:
					System.out.println("Would you like to search by name (n) or by phone number (p)?");
					//determining which letter the user has picked
					char option = inScanner.nextLine().charAt(0);
					ArrayList<String[]> results;
					if (option == 'n') {
						System.out.println("Choose a name: ");
						results = searchContactName(inScanner.nextLine());
						for (String[] contact: results) {
							System.out.println(contact[0] + ": " + contact[1]);
						}
					}  else if(option == 'p') {
						System.out.println("Choose a number: ");
						results = searchContactPhone(inScanner.nextLine());
						for (String[] contact: results) {
							System.out.println(contact[0] + ": " + contact[1]);
						}
					} else  {
						System.out.println("Please enter a valid letter");
					}
					break;
					
				//for deleting a contact
				case 3:
					System.out.println("Who would you like to delete?");
					String deleteContact = inScanner.nextLine();
					break;
					
				default:
					System.out.println("Invalid number, choose between 1 - 3");
				}

				System.out.println("\nWould you like to do something? Pick a number again");
	} }
	
	
	public static void addNewContact(String name, String phone){
		
		//regex special function for xxx-xxx-xxxx format
		//matcher is to compare the format of the input to the right expression
		Pattern pattern = Pattern.compile("^(\\d{3}[- .]?){2}\\d{4}$");
	    Matcher matcher = pattern.matcher(phone);

		File originalFile = new File("phonebook.txt");
		
		//if number is valid format
	    if (matcher.matches()) {
	    	
		//we write a name and a phone number into the txt file
		try (PrintWriter pw = new PrintWriter(new FileWriter(originalFile, true))){
			pw.println(name + ": " +phone);
		} catch(IOException e) { System.out.println(e.getMessage());} 
		} else { System.out.println("Invalid number format!"); }
	}
		
	public static ArrayList<String[]> searchContactName(String name){
		File originalFile = new File("phonebook.txt");
		try(BufferedReader phoneScanner = new BufferedReader(new FileReader(originalFile))){
			String contactNames[];
			ArrayList<String[]> results = new ArrayList<>();
			
			//I converted the contact's names all to lowercase to avoid case-sensitivity
			//and I split the line with the colons according to the formatting of the text file
			//whatever similarities we find between 'name' and contactNames[0] will get added to a results Array 
			//which will be stored and displayed to the user
			while(phoneScanner.ready()) {
				contactNames = phoneScanner.readLine().split(": ");
				if(contactNames[0].toLowerCase().contains(name.toLowerCase())) {
					results.add(contactNames);
				} }
			//if there are possible results that exist, we return them. otherwise we print nothing.
			if (results.size() > 0) { return results; }
			return null;
			} catch(IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	//searchContactPhone follows the same logic as searchContactName with a few minor changes
	public static ArrayList<String[]> searchContactPhone(String phone) throws IOException{
		File originalFile = new File("phonebook.txt");
		try(BufferedReader phoneScanner = new BufferedReader(new FileReader(originalFile))){
			String contactNames[];
			ArrayList<String[]> results = new ArrayList<>();
			
			while(phoneScanner.ready()) {
				contactNames = phoneScanner.readLine().split(": ");
				//minor change here
				if(contactNames[1].equals(phone)) {
					results.add(contactNames);
					} }
				if (results.size() > 0) { return results; }
				return null;
			}  catch(IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	public static void deleteContact(String name, String phone) throws IOException{

		//I created a temporary text file to store our new file with the deleted contacts
		//the temporary file will then be renamed to the original file's name and be
		//replaced as the new original file, taking the place of the older file which is no longer in use
		
		File originalFile = new File("phonebook.txt");
		BufferedReader br = new BufferedReader(new FileReader(originalFile));

		File tempFile = new File("tempphonebook.txt");
		PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		
		String line = null;
		
		//we check while the file contains both matching parameters (name and number)
		//we replace the line with an empty string, thus removing it from the file
		while ((line = br.readLine()) != null) {
			if((line.contains(name)) && (line.contains(phone))) {
				String contactNames = line.substring(line.lastIndexOf(" "), line.length());
				System.out.println("Deleted contact: " + name +", "+ phone);
			}
		}
		
		
		pw.println(line);
		pw.flush();
		
		pw.close(); 
		br.close();
		
		if (!originalFile.delete()) {
			System.out.println("Could not delete file");
            return;
		}
		
		if (!tempFile.renameTo(originalFile)) {
			System.out.println("Could not rename file");
		}
		}
		
	}
	
