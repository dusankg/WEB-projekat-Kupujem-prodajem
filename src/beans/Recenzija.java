package beans;

public class Recenzija {

	private int idRecenzije=0;
	private String oglas;
	private String recenzent;
	private String naslovRecenzije;
	private String sadrzajRecenzije;
	private String slika;
	private Boolean opisTacan;
	private Boolean ispostovanDogovor;
	
	// Kontruktori
	public Recenzija(int idRecenzije, String oglas, String recenzent, String naslovRecenzije, String sadrzajRecenzije,
			String slika, Boolean opisTacan, Boolean ispostovanDogovor) {
		super();
		this.idRecenzije = idRecenzije;
		this.oglas = oglas;
		this.recenzent = recenzent;
		this.naslovRecenzije = naslovRecenzije;
		this.sadrzajRecenzije = sadrzajRecenzije;
		this.slika = slika;
		this.opisTacan = opisTacan;
		this.ispostovanDogovor = ispostovanDogovor;
	}
	// Get set
	public int getIdRecenzije() {
		return idRecenzije;
	}

	public void setIdRecenzije(int idRecenzije) {
		this.idRecenzije = idRecenzije;
	}

	public String getOglas() {
		return oglas;
	}

	public void setOglas(String oglas) {
		this.oglas = oglas;
	}

	public String getRecenzent() {
		return recenzent;
	}

	public void setRecenzent(String recenzent) {
		this.recenzent = recenzent;
	}

	public String getNaslovRecenzije() {
		return naslovRecenzije;
	}

	public void setNaslovRecenzije(String naslovRecenzije) {
		this.naslovRecenzije = naslovRecenzije;
	}

	public String getSadrzajRecenzije() {
		return sadrzajRecenzije;
	}

	public void setSadrzajRecenzije(String sadrzajRecenzije) {
		this.sadrzajRecenzije = sadrzajRecenzije;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public Boolean isOpisTacan() {
		return opisTacan;
	}

	public void setOpisTacan(Boolean opisTacan) {
		this.opisTacan = opisTacan;
	}
	public Boolean isIspostovanDogovor() {
		return ispostovanDogovor;
	}
	public void setIspostovanDogovor(Boolean ispostovanDogovor) {
		this.ispostovanDogovor = ispostovanDogovor;
	}


	

	
	
}
