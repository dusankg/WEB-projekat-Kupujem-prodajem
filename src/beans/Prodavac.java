package beans;

import java.util.ArrayList;

public class Prodavac {

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
	private ArrayList<Oglas> listaObjavljenihOglasa = new ArrayList<Oglas>();
	private ArrayList<Oglas> listaIsporucenihOglasa = new ArrayList<Oglas>();

	private ArrayList<Poruka> poruke = new ArrayList<Poruka>();

	private int brojLajkova;
	private int brojDislajkova;
	
	private boolean markiran;

	public Prodavac(String korisnickoIme, String lozinka, String ime, String prezime, String uloga,
			String kontaktTelefon, String grad, String email, String datumRegistracije,
			ArrayList<Oglas> listaObjavljenihOglasa, ArrayList<Oglas> listaIsporucenihOglasa,
			ArrayList<Poruka> listaPoruka, int brojLajkova, int brojDislajkova) {
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
		this.listaObjavljenihOglasa = listaObjavljenihOglasa;
		this.listaIsporucenihOglasa = listaIsporucenihOglasa;
		poruke = listaPoruka;
		this.brojLajkova = brojLajkova;
		this.brojDislajkova = brojDislajkova;
		
		//Markiran ce uvek u startu biti netacno
		this.markiran = false;
	}
	
	public Prodavac() {
		
	}

	

	public Prodavac(String korisnickoIme, String lozinka, String ime, String prezime, String uloga,
			String kontaktTelefon, String grad, String email, String datumRegistracije, int brojLajkova,
			int brojDislajkova, boolean markiran) {
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
		this.brojLajkova = brojLajkova;
		this.brojDislajkova = brojDislajkova;
		this.markiran = markiran;
	}





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

	public ArrayList<Oglas> getListaObjavljenihOglasa() {
		return listaObjavljenihOglasa;
	}

	public void setListaObjavljenihOglasa(ArrayList<Oglas> listaObjavljenihOglasa) {
		this.listaObjavljenihOglasa = listaObjavljenihOglasa;
	}

	public ArrayList<Oglas> getListaIsporucenihOglasa() {
		return listaIsporucenihOglasa;
	}

	public void setListaIsporucenihOglasa(ArrayList<Oglas> listaIsporucenihOglasa) {
		this.listaIsporucenihOglasa = listaIsporucenihOglasa;
	}

	public ArrayList<Poruka> getListaPoruka() {
		return poruke;
	}

	public void setListaPoruka(ArrayList<Poruka> listaPoruka) {
		poruke = listaPoruka;
	}

	public int getBrojLajkova() {
		return brojLajkova;
	}

	public void setBrojLajkova(int brojLajkova) {
		this.brojLajkova = brojLajkova;
	}

	public int getBrojDislajkova() {
		return brojDislajkova;
	}

	public void setBrojDislajkova(int brojDislajkova) {
		this.brojDislajkova = brojDislajkova;
	}

	public boolean isMarkiran() {
		return markiran;
	}

	public void setMarkiran(boolean markiran) {
		this.markiran = markiran;
	}
	
	
	
	
	
	
}
