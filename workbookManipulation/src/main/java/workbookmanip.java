import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class workbookmanip {
	/*
	 * Data structure storing data read from workbook files.
	 */
	static List<Integer> numberOnearr = new ArrayList<Integer>();
	static List<Integer> numberTwoarr = new ArrayList<Integer>();
	static List<String> word = new ArrayList<String>();
	/*
	 * Data structure storing manipulated(calculated) data.
	 */
	static int[] numSetOne = new int[4];
	static int[] numSetTwo = new int[4];
	static String[] wordSet = new String[4];
	/*
	 * New JSON request.
	 */
	static JSONObject reqobj;
	
	public static void main(String[] args) throws Exception {
		String filename1 = "src/main/resources/Data1.xlsx";
		String filename2 = "src/main/resources/Data2.xlsx";
		
		readFile(filename1);
		readFile(filename2);
		
		dataManipulation();
		JSONObj();
		JSONRequest();

	}
	
	/*
	 * This function reads and parses excel files and stores data to appropriate ArrayList.
	 */
	public static void readFile(String filename) throws EncryptedDocumentException, InvalidFormatException, IOException {
		Workbook databook = WorkbookFactory.create(new File(filename));
		Sheet sheet = databook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();
				
		for (Row row: sheet) {
            for(Cell cell: row) {
                String celldata = dataFormatter.formatCellValue(cell);
                if(!celldata.equals("numberSetOne") && !celldata.equals("numberSetTwo") && !celldata.equals("wordSetOne") && !celldata.isEmpty()){
                	if(cell.getColumnIndex() == 0) {
                		numberOnearr.add(Integer.parseInt(celldata));
                	}
                	
                	if(cell.getColumnIndex() == 1) {
                		numberTwoarr.add(Integer.parseInt(celldata));
                	}
                	
                	if(cell.getColumnIndex() == 2) {
                		word.add(celldata);
                	}
                }
            }
            
        }
		
	}
	
	/*
	 * This function calculates(Multiplication, division, concatenation) data based on instruction.
	 */
	public static void dataManipulation() throws Exception {
		//Since the size of every ArrayList is same, simplified the condition of for-loop.
		//Same class with detailed for-loop conditions are commented below.
		for(int i=0; i < numberOnearr.size()-4; i++) {
			int newnum1 = numberOnearr.get(i)*numberOnearr.get(i+4);
			
			//numberSetOne
			if(i < numSetOne.length) {
				numSetOne[i] = newnum1;
			}
			//numberSetTwo
			int newnum2 = numberTwoarr.get(i)/numberTwoarr.get(i+4);
			if(i < numSetTwo.length) {
				numSetTwo[i] = newnum2;
			}
			//wordSetOne
			String newword = word.get(i).concat(" ").concat(word.get(i+4));
			if(i < wordSet.length) {
				wordSet[i] = newword;
			}
		}
	}

	/*
	 * This function creates JSON object for JSON request.
	 */
	public static void JSONObj() throws Exception{
		reqobj = new JSONObject();
		reqobj.put("id", "njw392@gmail.com");
		JSONArray numberSetOne = new JSONArray();
		for(int i =0; i < numSetOne.length; i++) {
			numberSetOne.put(numSetOne[i]);
		}
		JSONArray numberSetTwo = new JSONArray();
		for(int i =0; i < numSetTwo.length; i++) {
			numberSetTwo.put(numSetTwo[i]);
		}
		JSONArray wordSetOne = new JSONArray();
		for(int i =0; i < wordSet.length; i++) {
			wordSetOne.put(wordSet[i]);
		}
		
		reqobj.put("numberSetOne", numberSetOne);
		reqobj.put("numberSetTwo", numberSetTwo);
		reqobj.put("wordSetOne", wordSetOne);
	}
	
	/*
	 * This function executes sending POST request to the server with the path, /challenge.
	 */
	public static void JSONRequest() throws Exception{
		HttpClient http = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://34.239.125.159:5000/challenge");
		
        StringEntity entity = new StringEntity(reqobj.toString(), ContentType.APPLICATION_JSON);
		request.setEntity(entity);
		
		HttpResponse response = http.execute(request);
		if(response.getStatusLine().getStatusCode() == 200) {
			System.out.println("POST request to the server has been successfully processed.");
		}
	}

	
	/*
	 * The function manipulates data and contains for-loop condition for every ArrayList.
	 */
	/*
	public static void dataManipulation() {
		//numberSetOne
		for(int i = 0; i < numberOnearr.size() - 4; i++) {
			int newnum1 = numberOnearr.get(i)*numberOnearr.get(i+4);
			if(i < numSetOne.length) {
				numSetOne[i] = newnum1;
			}
		}
		for(int i = 0; i < numSetOne.length; i++) {
			System.out.println(numSetOne[i]);
		}
		
		//numberSetTwo
		for(int i = 0; i < numberTwoarr.size() - 4; i++) {
			int newnum1 = numberTwoarr.get(i)/numberTwoarr.get(i+4);
			if(i < numSetTwo.length) {
				numSetTwo[i] = newnum1;
			}
		}
		for(int i = 0; i < numSetTwo.length; i++) {
			System.out.println(numSetTwo[i]);
		}
		
		//wordSet
		for(int i = 0; i < word.size() - 4; i++) {
			String newword = word.get(i).concat(" ").concat(word.get(i+4));
			if(i < wordSet.length) {
				wordSet[i] = newword;
			}
		}
		
		for(int i = 0; i < wordSet.length; i++) {
			System.out.println(wordSet[i]);
		}
	}*/
}
