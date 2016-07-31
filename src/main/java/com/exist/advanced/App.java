package com.exist.advanced;

import com.exist.advanced.util.FileUtil;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

public class App {
	private Scanner scanner;
	private List<Map<String,String>> table;
	//Set the max size of each table content, -1 means off
	private static final int MAX_LEN = -1; 
	public App () {
		table = FileUtil.readFile();
		scanner = new Scanner(System.in);
	}
	public static void main (String[] args){
		new App().run();
	}
	public void run (){
		//Check if file is empty or existing
		if (table == null){
			initTable();
			FileUtil.writeFile(table);
		}
		printTable();
		menu();
	}
	public void menu(){
		int opt;
		boolean cont;
		do {
			opt = 0;
			cont = true;
			try {
			  System.out.println("Menu: [1 Search] [2 Edit] [3 Print] [4 Add Row] [5 Sort] [6 Reset] [7 Exit]");
			  opt = scanner.nextInt();
			} catch (InputMismatchException ex){
				cont = true;
			}
			scanner.nextLine();
			switch(opt){
				case 1: 
					searchTable();
					break;
				case 2:
				    editTable();
					break;
				case 3:
					printTable();
					break;
				case 4: 
					addRow();
					break;
				case 5: 
					sort();
					break;
				case 6:
					initTable();
					break;
				case 7: 
					cont = false;
					break;
				default:
					System.out.println("[Invalid option]");
					break;
			}
		} while (cont);
		System.out.println("Exiting...");
	}
	public void printTable(){
		for(Map<String, String> row : table){
			for(Map.Entry<String, String> entry : row.entrySet()){
				System.out.print("[" + entry.getKey() + ":" + entry.getValue() + "]\t");
			}
			System.out.println("");
		}
	}
	public void initTable(){
		int fDim = 0;
		int sDim = 0;
		boolean isValid;
		System.out.println("[Set new table dimensions]");
		do {
			isValid = true;
			try {
				System.out.print("Row: ");
				fDim = scanner.nextInt();
				if (fDim < 1){
					isValid = false;
					System.out.println("[Row count should be atleast 1]");
				}
			} catch (InputMismatchException ex){
				isValid = false;
				System.out.println("[Invalid row value]");
				scanner.nextLine();
			}
		} while(!isValid);
		do {
			isValid = true;
			try {
				System.out.print("Column: ");
				sDim = scanner.nextInt();
				if (sDim < 1){
					isValid = false;
					System.out.println("[Column count should be atleast 1]");
				}
			} catch (InputMismatchException ex){
				isValid = false;
				System.out.println("[Invalid column value]");
				scanner.nextLine();
			}
		} while (!isValid);
		//Fill the list of map with random characters
		table = new LinkedList<Map<String,String>>();
		for (int x = 0; x < fDim; x++){
			Map<String,String> row = new LinkedHashMap<String,String>();
			for (int y = 0; y < sDim; y++){
				row.put(RandomStringUtils.randomAscii(MAX_LEN != -1 ? MAX_LEN : 3)
					, RandomStringUtils.randomAscii(MAX_LEN != -1 ? MAX_LEN : 3));
			}
			table.add(row);
		}
		FileUtil.writeFile(table);
	}
	public void searchTable(){
		System.out.print("Search for: ");
		String character = scanner.nextLine();
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		int x = 0;
		int y = 0;
		//Check if key or value contains the char searched
		for(Map<String, String> row : table){
			y = 0;
			for(Map.Entry<String, String> entry : row.entrySet()){
				if (entry.getKey().contains(character)) {
					Map<String,String> keyFound = new HashMap<String,String>();
					keyFound.put("x", (x+1) + "");
					keyFound.put("y", (y+1) + "");
					keyFound.put("entryType", "KEY");
					result.add(keyFound);
				}
				 if (entry.getValue().contains(character)) {
				 	Map<String,String> valueFound = new HashMap<String,String>();
					valueFound.put("x", (x+1) + "");
					valueFound.put("y", (y+1) + "");
					valueFound.put("entryType", "VALUE");
					result.add(valueFound);
				}
				y++;
			}
			x++;
		}
		//Check if char is found on list of map
		if(result.size() > 0){
			int keyCount = 0;
			int valueCount = 0;
			System.out.print("Result(s): ");
			for (Map<String,String> entry : result){
				System.out.print("\n"  + "[" + entry.get("x") + "," + entry.get("y") + "] : " + entry.get("entryType"));
				if (entry.get("entryType").equals("KEY")){
					keyCount++;
				} else {
					valueCount++;
				}
			}
			System.out.println("\nOccurences as KEY: " + keyCount);
			System.out.println("Occurences as VALUE: " + valueCount);
			System.out.println("Total occurences: " + (keyCount+valueCount));
		} else {
			System.out.println("[No data found]");
		}
	}
	public void editTable(){
		int fInd = 0;
		int sInd = 0;
		boolean isValid = false;
		System.out.println("[Enter index of set to edit]");
		do {
			try {
				isValid = true;
				System.out.print("Row: ");
				fInd = scanner.nextInt();
				if (fInd > table.size()){
					isValid = false;
					System.out.println("[Invalid row value]");
				}
			} catch (InputMismatchException ex) {
				System.out.println("[Invalid row value]");
				scanner.nextLine();
				isValid = false;
			}
		} while (!isValid);
		do {
			try {
				isValid = true;
				System.out.print("Column: ");
				sInd = scanner.nextInt();
				if (sInd > table.get(0).size()){
					isValid = false;
					System.out.println("[Invalid column value]");
				}
			} catch (InputMismatchException ex){
				System.out.println("[Invalid column value]");
				scanner.nextLine();
				isValid = false;
			}
		} while (!isValid);
		Map<String,String> editedMap = new LinkedHashMap<String,String>();
		boolean repeat;
		String identifier = "";
		do {
			repeat = true;
			try {	
				System.out.println("Which would you like to edit? [1 Key] [2 Value]");
				int option = scanner.nextInt();
				scanner.nextLine();
				switch (option) {
					case 1: 
						identifier = "KEY";
						repeat = false;
						break;
					case 2:
						identifier	= "VALUE";
						repeat = false;
						break;
					default: 
						System.out.println("[Invalid option]");
						break;
				} 
			} catch (InputMismatchException ex){
				System.out.println("[Invalid option]");
				scanner.nextLine();
				repeat = true;
			}
		} while (repeat);
		//Get new value for k or v
		boolean valid;
		String newValue;
		do {
			valid = true;
			System.out.println("Enter new info: ");
			newValue = scanner.nextLine();
			if (newValue.length() != MAX_LEN && MAX_LEN != -1){
				System.out.println("[New value should be " + MAX_LEN + " characters]");
				valid = false;
			}
			if (newValue.equals(FileUtil.ENTRY_DELIMITER) || newValue.equals(FileUtil.KEY_VALUE_DELIMITER)){
				System.out.println("Input is used as delimiter on file. Try a different one.");
				valid = false;
			}
		} while (!valid);
		int x = 0;
		for (Map.Entry<String,String> entry : table.get(fInd-1).entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			if (x == sInd-1){
				if(identifier.equals("KEY")){
					key = newValue;
				} else {
					value = newValue;
				}
			}
			editedMap.put(key, value);
			x++;
		}
		table.set(fInd-1, editedMap);
		FileUtil.writeFile(table);
	}
	public void addRow(){
		Map<String,String> newRow = new LinkedHashMap<String,String>();
		System.out.println("Adding new row...\nInput " + table.get(0).size() 
			+ " key and value pairs separated by commas.\nex: key,value");
		//Get KV pairs for new row
		for (int y = 0; y < table.get(0).size(); y++){
			boolean isValid;
			do {
				isValid = true;
				try {
					System.out.print("[Pair #" + (y+1) + "] ");
					String entry = scanner.nextLine();
					String[] keyValue = entry.split(",");
					if (newRow.containsKey(keyValue[0])){
						System.out.println("[Keys per row should be unique]");
						isValid = false;
					} else if ((keyValue[0].length() != MAX_LEN && MAX_LEN != -1) 
						|| (keyValue[1].length() != MAX_LEN && MAX_LEN != -1)){ 
						System.out.println("[Keys and Values should be " + MAX_LEN + " characters only]");
						isValid = false;
					} else if (keyValue[0].equals(FileUtil.ENTRY_DELIMITER) || keyValue[0].equals(FileUtil.KEY_VALUE_DELIMITER)
						|| keyValue[1].equals(FileUtil.ENTRY_DELIMITER) || keyValue[1].equals(FileUtil.KEY_VALUE_DELIMITER)){
						System.out.println("One or both of the data input is used as a delimiter on file.\nTry a different one.");
						isValid = false;
					} else {
						newRow.put(keyValue[0],keyValue[1]);
					}
				} catch (ArrayIndexOutOfBoundsException ex){
					isValid = false;
					System.out.println("[Invalid pair input]");
				}		
			} while (!isValid);
		
		}
		table.add(newRow);
		FileUtil.writeFile(table);
	}
	public void sort(){
		//Sort per row
		List<Map<String,String>> sortedTable = new LinkedList<Map<String,String>>();
		for (Map<String,String> row : table){
			List<Map.Entry<String,String>> list = new LinkedList<Map.Entry<String,String>>(row.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String,String>>(){ // Anonymous class
				public int compare(Map.Entry<String,String> ent1, Map.Entry<String,String> ent2){
					return (ent1.getKey() + ent1.getValue()).compareTo(ent2.getKey() + ent2.getValue());
				}
			}); 
			Map<String,String> sortedMap = new LinkedHashMap<>();
			for (Map.Entry<String,String> entry : list){
				sortedMap.put(entry.getKey(),entry.getValue());
			}
			sortedTable.add(sortedMap);
		}
		//Sort per column
		// Collections.sort(sortedTable, new Comparator<Map<String,String>>(){
		// 	public int compare(Map<String,String> map1, Map<String,String> map2){
		// 			String o1 = "";
		// 			String o2 = "";
		// 			for (Map.Entry<String,String> entry : map1.entrySet()){
		// 				o1 += entry.getKey() + entry.getValue();
		// 			}
		// 			for (Map.Entry<String,String> entry : map2.entrySet()){
		// 				o2 += entry.getKey() + entry.getValue();
		// 			}
		// 			System.out.println(o1 + " " + o2);
		// 			return o1.compareTo(o2);
		// 	}
		// });
		table = sortedTable;
		FileUtil.writeFile(table);
		System.out.println("Table sorted!");
	}
}

















