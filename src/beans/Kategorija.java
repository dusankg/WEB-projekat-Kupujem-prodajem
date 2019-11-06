package beans;

import java.util.ArrayList;

public class Kategorija {

	private int idKategorija;
	private String naziv;
	private String opis;
	private ArrayList<Oglas> listaOglasa = new ArrayList<>();
	
	//Kontruktori
	
	public Kategorija(int idKategorija, String naziv, String opis, ArrayList<Oglas> listaOglasa) {
		super();
		this.idKategorija = idKategorija;
		this.naziv = naziv;
		this.opis = opis;
		this.listaOglasa = listaOglasa;
	}
	public Kategorija(String naziv, String opis, ArrayList<Oglas> listaOglasa) {
		super();
		this.naziv = naziv;
		this.opis = opis;
		this.listaOglasa = listaOglasa;
		this.idKategorija = -1; //Ako nije zadato stavicu -1, da znam da nije zadato
	}
	public Kategorija(String naziv, String opis) {
		super();
		this.naziv = naziv;
		this.opis = opis;
	}
	public Kategorija(String naziv) {
		super();
		this.naziv = naziv;
	}	
		
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public ArrayList<Oglas> getListaOglasa() {
		return listaOglasa;
	}
	public void setListaOglasa(ArrayList<Oglas> listaOglasa) {
		this.listaOglasa = listaOglasa;
	}
	public int getIdKategorija() {
		return idKategorija;
	}
	public void setIdKategorija(int idKategorija) {
		this.idKategorija = idKategorija;
	}
	
	
	
}
