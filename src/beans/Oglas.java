package beans;

import java.sql.Date;
import java.util.ArrayList;

public class Oglas {

	private int idOglasa;
	private String naziv;
	private double cena;
	private String opis;
	private int lajkovi;
	private int dislajkovi;
	private String slika;
	private Date datumPostavljanja;
	private Date datumIsticanja;
	private Boolean aktivan;
	private String grad;
	
	// Na pocetku uvek mora da bude prazna
	private ArrayList<Recenzija> listaRezencija = new ArrayList<>();
	
	
	// Konstruktori. Za sada samo jedan. Za novi oglas stavljati da je broj lajkova i dislajkova 0
	public Oglas(int id,String naziv, double cena, String opis, int lajkovi, int dislajkovi, String slika,
				Date datumPostavljanja, Date datumIsticanja, Boolean aktivan, String grad) {
			super();
			this.idOglasa = id; //Vidi da li ce trebati ili nece, ali nije lose da svaki oglas ima id
			this.naziv = naziv;
			this.cena = cena;
			this.opis = opis;
			this.lajkovi = lajkovi;
			this.dislajkovi = dislajkovi;
			this.slika = slika;
			this.datumPostavljanja = datumPostavljanja;
			this.datumIsticanja = datumIsticanja;
			this.aktivan = aktivan;
			this.grad = grad;
			listaRezencija = new ArrayList<>();
	}	
	
	public Oglas(String naziv, double cena, String opis, int lajkovi, int dislajkovi, String slika,
				Date datumPostavljanja, Date datumIsticanja, Boolean aktivan, String grad) {
			super();
			this.naziv = naziv;
			this.cena = cena;
			this.opis = opis;
			this.lajkovi = lajkovi;
			this.dislajkovi = dislajkovi;
			this.slika = slika;
			this.datumPostavljanja = datumPostavljanja;
			this.datumIsticanja = datumIsticanja;
			this.aktivan = aktivan;
			this.grad = grad;
			listaRezencija = new ArrayList<>();
	}

	// Get set
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public int getLajkovi() {
		return lajkovi;
	}
	public void setLajkovi(int lajkovi) {
		this.lajkovi = lajkovi;
	}
	public int getDislajkovi() {
		return dislajkovi;
	}
	public void setDislajkovi(int dislajkovi) {
		this.dislajkovi = dislajkovi;
	}
	public String getSlika() {
		return slika;
	}
	public void setSlika(String slika) {
		this.slika = slika;
	}
	public Date getDatumPostavljanja() {
		return datumPostavljanja;
	}
	public void setDatumPostavljanja(Date datumPostavljanja) {
		this.datumPostavljanja = datumPostavljanja;
	}
	public Date getDatumIsticanja() {
		return datumIsticanja;
	}
	public void setDatumIsticanja(Date datumIsticanja) {
		this.datumIsticanja = datumIsticanja;
	}
	public Boolean getAktivan() {
		return aktivan;
	}
	public void setAktivan(Boolean aktivan) {
		this.aktivan = aktivan;
	}
	public String getGrad() {
		return grad;
	}
	public void setGrad(String grad) {
		this.grad = grad;
	}

	public int getIdOglasa() {
		return idOglasa;
	}

	public void setIdOglasa(int idOglasa) {
		this.idOglasa = idOglasa;
	}

	public ArrayList<Recenzija> getListaRezencija() {
		return listaRezencija;
	}

	public void setListaRezencija(ArrayList<Recenzija> listaRezencija) {
		this.listaRezencija = listaRezencija;
	}	
	
	
}
