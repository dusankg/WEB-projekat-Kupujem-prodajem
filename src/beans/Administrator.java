package beans;
/*
 * Vidi da li uopste moras da pravis Administratora kao posebnu klasu ili da ubacim boolean polje u korisnika
 * koje ce da oznacava da li je taj korisnik administrator
 * Onda bi korisnik mogao da bude u isto vreme i administrator
 * */

import java.util.ArrayList;

public class Administrator {

	private String korisnickoIme;
	private String lozinka;
	private String ime;
	private String prezime;
	private String uloga;
	private String kontaktTelefon;
	private String grad;
	private String email;
	private String datumRegistracije;
	
	// Dodaj listu poruka i ako bude trebalo jos nesto
	private ArrayList<Poruka> poruke = new ArrayList<>();
 
	public Administrator(String korisnickoIme, String lozinka, String ime, String prezime, String uloga,
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
	
}
