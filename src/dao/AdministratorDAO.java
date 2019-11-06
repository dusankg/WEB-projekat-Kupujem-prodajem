package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import beans.Administrator;

public class AdministratorDAO {

	private Map<String, Administrator> administratori = new HashMap<>();
	public static String path;
	
	public AdministratorDAO(String path){
		administratori = new HashMap<String,Administrator>();
		this.path = path;
		ucitajAdministratore(path);
		
	}
	
	private void ucitajAdministratore(String path) {
		 	String putanja = path + "\\podaci\\admini.json";
			FileWriter fileWriter = null;
			BufferedReader in = null;
			File file = null;
			try {
				file = new File(putanja);
				in = new BufferedReader(new FileReader(file));

				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.setVisibilityChecker(
						VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
				TypeFactory factory = TypeFactory.defaultInstance();
				MapType type = factory.constructMapType(HashMap.class, String.class, Administrator.class);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				administratori = (HashMap<String, Administrator>) objectMapper.readValue(file, type);
			} catch (FileNotFoundException fnfe) {
				try {
					file.createNewFile();
					fileWriter = new FileWriter(file);
					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
					objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
					String string = objectMapper.writeValueAsString(administratori);
					fileWriter.write(string);

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fileWriter != null) {
						try {
							fileWriter.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
	}
	public void sacuvajAdministratore(String path) {
		String putanja = path + "\\podaci\\admini.json";
		File f = new File(putanja);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String string = objectMapper.writeValueAsString(administratori);
			fileWriter.write(string);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Map<String, Administrator> getAdministratori() {
		return administratori;
	}

	public void setAdministratori(Map<String, Administrator> administratori) {
		this.administratori = administratori;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		AdministratorDAO.path = path;
	}
	
	
}
