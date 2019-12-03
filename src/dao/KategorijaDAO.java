package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import beans.Kategorija;

public class KategorijaDAO { //Samo mapa kategorija i rukovanje njima

	private Map<String, Kategorija> kategorije = new HashMap<>(); //Naziv ce biti kljuc, mora biti jedinstven
	public static String path;
	
	
	//Konstruktori
	public KategorijaDAO() {
		super();
		kategorije = new HashMap<>();
	}
	public KategorijaDAO(String path) {
		super();
		this.path = path; // Ne znam da li ce moci ovako, proveriti kasnije
		kategorije = new HashMap<>();
		//Ucitavanje prethodno sacuvanih kategorija
		ucitajKategorije(path);
	}
	
	// Metoda za sacuvavanje liste kategorija u fajl
	public boolean sacuvajKategorije() {
		boolean uspesno = true;
		String lokacija = path + "\\categories.json"; 
		
		//Jednostavno cu da kopiram sa neta, pa kud puklo da puklo
		
		File f = new File(lokacija);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String string = objectMapper.writeValueAsString(kategorije);
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
			} else uspesno = false;
		}
		
		return uspesno; //Promeni da bude "uspesno" kasnije, prvo vidi da li ce ovako da radi
	}
	
	// Metoda za ucitavanje kategorija
	
	public void ucitajKategorije(String path) {
		String putanja = path + "\\podaci\\categories.json";
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
			MapType type = factory.constructMapType(HashMap.class, String.class, Kategorija.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			kategorije = (HashMap<String, Kategorija>) objectMapper.readValue(file, type);
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String string = objectMapper.writeValueAsString(kategorije);
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
	
	//Nalazenje kategorija sa unetim nazivom
	public Kategorija find(String nazivKategorije) {
		if(kategorije.containsKey(nazivKategorije)){
			Kategorija nadjena = kategorije.get(nazivKategorije);
			return nadjena;
		} else return null;
	}
	// Isto kao gore, samo da bi mi odmah vracala true ili false
	public boolean postojiUMapi(String nazivKategorije) {
		if(kategorije.containsKey(nazivKategorije)){
			return true;
		} else return false;
	}
	public Map<String, Kategorija> getKategorije() {
		return kategorije;
	}
	public void setKategorije(Map<String, Kategorija> kategorije) {
		this.kategorije = kategorije;
	}
	public static String getPath() {
		return path;
	}
	public static void setPath(String path) {
		KategorijaDAO.path = path;
	}
	
	
	
}
