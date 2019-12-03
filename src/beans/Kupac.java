package beans;

import java.util.ArrayList;

public class Kupac {

	private String korisnickoIme; //Ima ulogu id-a
	private String lozinka;
	private String ime;
	private String prezime;
	private String uloga;
	private String kontaktTelefon;
	private String grad;
	private String email;
	private String datumRegistracije;
	
	// Dodatni parametri za kupce
	private ArrayList<Oglas> listaPorucenihOglasa = new ArrayList<Oglas>();
	private ArrayList<Oglas> listaDostavljenihOglasa = new ArrayList<Oglas>();
	private ArrayList<Oglas> listaOmiljenihOglasa = new ArrayList<Oglas>();
	private ArrayList<Recenzija>listaRecenzija=new ArrayList<>();
	private ArrayList<Poruka> poruke = new ArrayList<>();

	// Dodatni parametri za prodavce
	
	//Kontruktori. Za sada je samo jedan, sa svim parametrima
	public Kupac(String korisnickoIme, String lozinka, String ime, String prezime, String uloga,
			String kontaktTelefon, String grad, String email, String datumRegistracije) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.uloga = uloga;
		this.kontaktTelefon = kontaktTelefon;
		this.grad = grad;
		this.email = email;
		this.datumRegistracije = datumRegistracije;
	}	
	public Kupac() {}
	
	// Get set
	public String getKorisnickoIme() {
		return korisnickoIme;
	}
	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}
	public String getLozinka() {
		return lozinka;
	}
	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getUloga() {
		return uloga;
	}
	public void setUloga(String uloga) {
		this.uloga = uloga;
	}
	public String getKontaktTelefon() {
		return kontaktTelefon;
	}
	public void setKontaktTelefon(String kontaktTelefon) {
		this.kontaktTelefon = kontaktTelefon;
	}
	public String getGrad() {
		return grad;
	}
	public void setGrad(String grad) {
		this.grad = grad;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDatumRegistracije() {
		return datumRegistracije;
	}
	public void setDatumRegistracije(String datumRegistracije) {
		this.datumRegistracije = datumRegistracije;
	}
	public ArrayList<Poruka> getPoruke() {
		return poruke;
	}
	
	public void setPoruke(ArrayList<Poruka> poruke) {
		this.poruke = poruke;
	}

	public ArrayList<Oglas> getListaPorucenihOglasa() {
		return listaPorucenihOglasa;
	}

	public void setListaPorucenihOglasa(ArrayList<Oglas> listaPorucenihOglasa) {
		this.listaPorucenihOglasa = listaPorucenihOglasa;
	}

	public ArrayList<Oglas> getListaDostavljenihOglasa() {
		return listaDostavljenihOglasa;
	}

	public void setListaDostavljenihOglasa(ArrayList<Oglas> listaDostavljenihOglasa) {
		this.listaDostavljenihOglasa = listaDostavljenihOglasa;
	}

	public ArrayList<Oglas> getListaOmiljenihOglasa() {
		return listaOmiljenihOglasa;
	}

	public void setListaOmiljenihOglasa(ArrayList<Oglas> listaOmiljenihOglasa) {
		this.listaOmiljenihOglasa = listaOmiljenihOglasa;
	}

	public ArrayList<Recenzija> getListaRecenzija() {
		return listaRecenzija;
	}

	public void setListaRecenzija(ArrayList<Recenzija> listaRecenzija) {
		this.listaRecenzija = listaRecenzija;
	}
	
	
}
