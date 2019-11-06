package beans;

public class Recenzija {

	private int idRecenzije=0;
	private Oglas oglas;
	private String recezent;
	private String naslovRecenzije;
	private String sadrzajRecenzije;
	private String slika;
	private Boolean opisTacan;
	private String ispostovanDogovor;
	
	// Kontruktori
	public Recenzija(int idRecenzije, Oglas oglas, String recezent, String naslovRecenzije, String sadrzajRecenzije,
			String slika, Boolean opisTacan, String ispostovanDogovor) {
		super();
		this.idRecenzije = idRecenzije;
		this.oglas = oglas;
		this.recezent = recezent;
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

	public Oglas getOglas() {
		return oglas;
	}

	public void setOglas(Oglas oglas) {
		this.oglas = oglas;
	}

	public String getRecezent() {
		return recezent;
	}

	public void setRecezent(String recezent) {
		this.recezent = recezent;
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

	public Boolean getOpisTacan() {
		return opisTacan;
	}

	public void setOpisTacan(Boolean opisTacan) {
		this.opisTacan = opisTacan;
	}

	public String getIspostovanDogovor() {
		return ispostovanDogovor;
	}

	public void setIspostovanDogovor(String ispostovanDogovor) {
		this.ispostovanDogovor = ispostovanDogovor;
	}
	

	
	
}
