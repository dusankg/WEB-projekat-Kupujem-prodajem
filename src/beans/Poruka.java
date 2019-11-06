package beans;

public class Poruka {

	private String nazivOglasa;
	private String posiljalac;
	private String naslovPoruke;
	private String sadrzajPoruke;
	private String datum;
	private String vreme;
	public int idOglasa=-1; // Jer ko zna, mozda zatreba
	
	//Dodatni parametri
	private int idPoruke; 
	private String primalac;
	
	// Konstruktori
	public Poruka(String nazivOglasa, String posiljalac, String naslovPoruke, String sadrzajPoruke, String datum,
			String vreme, int idPoruke, String primalac) {
		super();
		this.nazivOglasa = nazivOglasa;
		this.posiljalac = posiljalac;
		this.naslovPoruke = naslovPoruke;
		this.sadrzajPoruke = sadrzajPoruke;
		this.datum = datum;
		this.vreme = vreme;
		this.idPoruke = idPoruke;
		this.primalac = primalac;
	}

	// Get set
	public String getNazivOglasa() {
		return nazivOglasa;
	}

	public void setNazivOglasa(String nazivOglasa) {
		this.nazivOglasa = nazivOglasa;
	}

	public String getPosiljalac() {
		return posiljalac;
	}

	public void setPosiljalac(String posiljalac) {
		this.posiljalac = posiljalac;
	}

	public String getNaslovPoruke() {
		return naslovPoruke;
	}

	public void setNaslovPoruke(String naslovPoruke) {
		this.naslovPoruke = naslovPoruke;
	}

	public String getSadrzajPoruke() {
		return sadrzajPoruke;
	}

	public void setSadrzajPoruke(String sadrzajPoruke) {
		this.sadrzajPoruke = sadrzajPoruke;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getVreme() {
		return vreme;
	}

	public void setVreme(String vreme) {
		this.vreme = vreme;
	}

	public int getIdPoruke() {
		return idPoruke;
	}

	public void setIdPoruke(int idPoruke) {
		this.idPoruke = idPoruke;
	}

	public String getPrimalac() {
		return primalac;
	}

	public void setPrimalac(String primalac) {
		this.primalac = primalac;
	}
	
	
	
	
	
}
