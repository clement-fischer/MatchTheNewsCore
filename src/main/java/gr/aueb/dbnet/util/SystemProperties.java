package gr.aueb.dbnet.util;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class SystemProperties {

	public static String TOPICS_BY_LANGUAGE_FILEPATH;
	public static String TOPICS_DESCRIPTION_FILEPATH;
	public static String TOPICS_RELEVANCE_FILEPATH;
	public static String LINK_DETECTION_FILEPATH;
	public static String DOCUMENT_FOLDER_IMPORT_TKN;
	public static String DOCUMENT_FOLDER_IMPORT_MTTKN;
	public static Boolean ELIMINATE_STOPWORDS;
	public static Boolean USE_PORTER_STEMMER;
	public static Boolean READ_ONLY_ENGLISH_DOCUMENTS;
	public static String LINK_DETECTION_RESULT_FOLDER;
	public static String DOCUMENT_FOLDER_IMPORT_GOOGLE_NEWS;
	public static String DOCUMENT_FOLDER_IMPORT_GOOGLE_NEWS_TRAIN;
	public static String DOCUMENT_FOLDER_IMPORT_GOOGLE_NEWS_TEST;

	public void init() {
		Properties props = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream("properties.prop");
			props.load(fis);

			Field[] d = this.getClass().getFields();
			for (Field field : d) {
				if (field.getType().toString().contains("Integer"))
					field.set(this, Integer.valueOf(props.getProperty((field.getName()))));
				else if (field.getType().toString().contains("Boolean"))
					field.set(this, Boolean.valueOf(props.getProperty((field.getName()))));
				else if (field.getType().toString().contains("Long"))
					field.set(this, Long.valueOf(props.getProperty((field.getName()))));
				else if (field.getType().toString().contains("Double"))
					field.set(this, Double.valueOf(props.getProperty((field.getName()))));
				else if (field.getType().toString().contains("Float"))
					field.set(this, Double.valueOf(props.getProperty((field.getName()))));
				else
					field.set(this, props.getProperty((field.getName())));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
