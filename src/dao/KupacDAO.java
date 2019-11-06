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
import beans.Kupac;

public class KupacDAO {

	private Map<String, Kupac> kupci = new HashMap<>();
	private static String path;
	
	public KupacDAO() {
		
	}
	public KupacDAO(String putanja) {
		kupci = new HashMap<>();
		this.path = putanja;
		ucitajKupce(path);
	}
	
	// Krajnje nisam morao ovako, ali nema veze sada ako bude radilo. Svakako imam samo jedan path zbog static
	// Funkcija za ucitavanje kupaca iz fajla
	public void ucitajKupce(String path) {
		String putanja = path + "\\podaci\\kupci.json";
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
			MapType type = factory.constructMapType(HashMap.class, String.class, Kupac.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			kupci = (HashMap<String, Kupac>) objectMapper.readValue(file, type);
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String string = objectMapper.writeValueAsString(kupci);
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
		
		return;
	}
	
	
	// Funkcija za cuvanje liste kupaca u memoriju
	public void sacuvajKupce(String path) {
		String putanja = path + "\\podaci\\kupci.json";
		File f = new File(putanja);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String string = objectMapper.writeValueAsString(kupci);
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
	
	// Nalazenje kupca iz mape
	public Kupac find(String korisnickoIme) {
		if(kupci.containsKey(korisnickoIme)) {
			Kupac nadjeni = kupci.get(korisnickoIme);
			return nadjeni;
		} else return null; 
	}
	//Provera da li postoji kupac sa unetim username-om
	public boolean postojiUMapi(String korisnickoIme) {
		if(kupci.containsKey(korisnickoIme)) {
			return true;
		} else return false;
	}
	// Get set
	public Map<String, Kupac> getKupci() {
		return kupci;
	}
	public void setKupci(Map<String, Kupac> kupci) {
		this.kupci = kupci;
	}
	public static String getPath() {
		return path;
	}
	public static void setPath(String path) {
		KupacDAO.path = path;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

