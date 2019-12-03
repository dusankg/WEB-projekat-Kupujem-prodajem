package beans;

public class Poruka {

	public int id;
	private String nazivPoruke;
	private String naslovPoruke;
	private String sadrzajPoruke;
	private String datum;
	private String ulogaPoslala;
	private String posiljalac;
	private String ulogiPoslato;
	private String primalac;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//Dodatni parametri
	

	public Poruka(int id, String nazivPoruke, String naslovPoruke, String sadrzajPoruke, String datum,
			String ulogaPoslala, String posiljalac, String ulogiPoslato, String primalac) {
		super();
		this.id = id;
		this.nazivPoruke = nazivPoruke;
		this.naslovPoruke = naslovPoruke;
		this.sadrzajPoruke = sadrzajPoruke;
		this.datum = datum;
		this.ulogaPoslala = ulogaPoslala;
		this.posiljalac = posiljalac;
		this.ulogiPoslato = ulogiPoslato;
		this.primalac = primalac;
	}

	
	
	

	// Get set
	public String getNazivPoruke() {
		return nazivPoruke;
	}

	

	public void setNazivPoruke(String nazivPoruke) {
		this.nazivPoruke = nazivPoruke;
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


	public String getPrimalac() {
		return primalac;
	}

	public void setPrimalac(String primalac) {
		this.primalac = primalac;
	}

	public String getUlogiPoslato() {
		return ulogiPoslato;
	}

	public void setUlogiPoslato(String ulogiPoslato) {
		this.ulogiPoslato = ulogiPoslato;
	}

	public String getUlogaPoslala() {
		return ulogaPoslala;
	}

	public void setUlogaPoslala(String ulogaPoslala) {
		this.ulogaPoslala = ulogaPoslala;
	}
	
	
	
	
}
