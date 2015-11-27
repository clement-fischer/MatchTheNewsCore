package gr.aueb.dbnet.importers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import gr.aueb.dbnet.tdt.structures.Document;
import gr.aueb.dbnet.util.SystemProperties;

public class GoogleNewsImporter extends Importer {
	
	private ConcurrentHashMap<String,Document> documents_google;
	private ConcurrentHashMap<String, Document> trainDocuments;
	private ConcurrentHashMap<String, Document> testDocuments;
	
	@Override
	public void importData() throws IOException{
		
		documents_google = new ConcurrentHashMap<String, Document>();
		String data_path = System.getProperty("user.home")+SystemProperties.DOCUMENT_FOLDER_IMPORT_GOOGLE_NEWS;
		File folder = new File(data_path);
		
		for (File fileEntry : folder.listFiles()) {				        	
		    if(fileEntry.isDirectory())        
		    	continue;
		    System.out.println(fileEntry.getName());           
			try {
					    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileEntry));
					    HSSFWorkbook wb = new HSSFWorkbook(fs);
					    HSSFSheet sheet = wb.getSheetAt(0);
					    HSSFRow row;
					    HSSFCell cell;

					    int rows; // No of rows
					    rows = sheet.getPhysicalNumberOfRows();

					    int cols = 0; // No of columns
					    int tmp = 0;

					    // This trick ensures that we get the data properly even if it doesn't start from first few rows
					    for(int i = 0; i < 10 || i < rows; i++) {
					        row = sheet.getRow(i);
					        if(row != null) {
					            tmp = sheet.getRow(i).getPhysicalNumberOfCells();
					            if(tmp > cols) cols = tmp;
					        }
					    }

					    for(int r = 1; r < rows; r++) {
					        row = sheet.getRow(r);
					        if(row != null) {
					        	//TODO 
					        	cell= row.getCell(0);
					        	String id =cell.toString();
					        	cell=row.getCell(7);
					        	String text = cell.toString();
					        	//cell=row.getCell(6);
					        	//String cluster=cell.toString();
					        	String cluster=fileEntry.getName().split("\\.")[0].split("oct_")[1];
					        	documents_google.put(id, new Document(id, text, cluster));
					        }
					       
					    }
			} catch(Exception ioe) {
					   ioe.printStackTrace();
			}		           	           		       
		 }
	}
	
	
	@Override
	public Map<String, ? extends Document> getTrainDocuments() {
		return trainDocuments;
	}

	@Override
	public Map<String, ? extends Document> getTestDocuments() {
		return testDocuments;
	}


	@Override
	public Map<String, ? extends Document> getData() {
		// TODO Auto-generated method stub
		
		return documents_google;
	}
	
	
		
}
