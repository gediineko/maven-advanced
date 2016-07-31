package com.exist.advanced.util;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.lang.Exception;
import java.util.*;
import java.lang.StringBuffer;
public class FileUtil{
	private static final String FILE_NAME = "table";
	public static final String ENTRY_DELIMITER = " \u037E "; //greek question mark
	public static final String KEY_VALUE_DELIMITER = " \u2261 "; //triple bar
	private static final String CONFIG = "config.properties" ;

	public static List<Map<String,String>> readFile(){
		List<Map<String,String>> table = new LinkedList<Map<String,String>>();
		Path file = FileSystems.getDefault().getPath(getFilePath(), FILE_NAME);
		try (InputStream in = Files.newInputStream(file); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
			String line = null;
			while((line = reader.readLine()) != null){
				Map<String,String> row = new LinkedHashMap<String,String>();
				for (String entry : line.split(ENTRY_DELIMITER)){
					String[] keyValue = entry.split(KEY_VALUE_DELIMITER);
					row.put(keyValue[0],keyValue[1]);
				}
				table.add(row);
			}
		} catch (Exception ex) {
			System.out.println("[File does not exist]");
			return null;
		}
		return table;
	}
	public static void writeFile(List<Map<String,String>> table){
		try {
			File file = new File(getFilePath()+File.separator+FILE_NAME);
			if (file.exists()){
				file.delete();
			}
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);
			for(Map<String, String> row : table){
				StringBuffer newRow = new StringBuffer();
				for(Map.Entry<String, String> entry : row.entrySet()){
					newRow.append(entry.getKey() + KEY_VALUE_DELIMITER + entry.getValue() + ENTRY_DELIMITER);
				}
				bw.write(newRow.toString());
				bw.newLine();
			}
			bw.close();
		} catch (Exception ex) {
			System.out.println("Error writing file!");
		}
	}
	private static String getFilePath(){
		Properties prop = new Properties();
		try (InputStream in = ClassLoader.getSystemResourceAsStream(CONFIG)){
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(prop.getProperty("file.path"));
		return prop.getProperty("file.path");
	}

}