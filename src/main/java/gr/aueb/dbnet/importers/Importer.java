package gr.aueb.dbnet.importers;

import gr.aueb.dbnet.tdt.structures.Document;

import java.io.IOException;
import java.util.Map;

public abstract class Importer {
	
	public void importData() throws IOException {
	}
	
	public abstract Map<String, ? extends Document> getTrainDocuments() ;
	
	public abstract Map<String, ? extends Document> getTestDocuments() ;
	public abstract Map<String, ? extends Document> getData() ;


}
