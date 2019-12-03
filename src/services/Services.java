package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import beans.Administrator;
import beans.Kategorija;
import beans.Kupac;
import beans.Oglas;
import beans.Poruka;
import beans.Prodavac;
import beans.Recenzija;
import dao.AdministratorDAO;
import dao.KategorijaDAO;
import dao.KupacDAO;
import dao.OglasDAO;
import dao.PorukaDAO;
import dao.ProdavacDAO;

@Path("")
public class Services {

	@Context
	ServletContext ctx;
	
	// Putanje do fajlova u kojima cuvam podatke, koje prosledjujem konstruktorima dao-va
	public String pathKupci;
	public String pathaAdministratori;
	public String pathProdavci;
	public String pathKategorije;
	public String pathPoruke;
	public String pathOglasi;
	
	/*
	 * promenljive za pracenje funkcionalnosti
	 * */
	public static String selektovan;
	public static int pZaIzm;
	
	public static String selektovaniOglas;
	public static String selektovaniOglasProdavac;
	public static ArrayList<Prodavac> markiraniProdavci = new ArrayList<>();
	public static ArrayList<Oglas>rac=new ArrayList<>();
	public static String oglasZaRecenziju;
	public static String selektovaniOglasZaDodavanjeRezencijeZaIzmenu;
	public static String selektovaniOglasZaDodavanjeRezencijeZaIzmenuOglas;
	public static int selektovanaPorukaProdavac;
	public static int naKojuPorukuSeOdgovara;
	public static int selektovanaPorukaKupac;
	public static String koJePoslaoPoruku;
	
	public void initAttributes() { //Zavrseno
		//Inicijalizujem sve atribute konteksta 
		if(ctx.getAttribute("kupacDAO") == null) {
			pathKupci = ctx.getRealPath(""); // Sta mu dodje ovo?
			ctx.setAttribute("kupacDAO", new KupacDAO(pathKupci));
		}
		if(ctx.getAttribute("administratorDAO") == null) {
			pathaAdministratori = ctx.getRealPath(""); // Sta mu dodje ovo?
			ctx.setAttribute("administratorDAO", new KupacDAO(pathaAdministratori));
		}
		if(ctx.getAttribute("prodavacDao") == null) {
			pathProdavci = ctx.getRealPath(""); // Sta mu dodje ovo?
			ctx.setAttribute("prodavacDao", new ProdavacDAO(pathProdavci));
		}
		if(ctx.getAttribute("kategorijaDAO") == null) {
			pathKategorije= ctx.getRealPath(""); // Sta mu dodje ovo?
			ctx.setAttribute("kategorijaDAO", new KategorijaDAO(pathKategorije));
		}
		if(ctx.getAttribute("porukaDAO") == null) {
			pathPoruke = ctx.getRealPath(""); // Sta mu dodje ovo?
			ctx.setAttribute("porukaDAO", new PorukaDAO(pathPoruke));
		}
		if(ctx.getAttribute("oglasDAO") == null) {
			pathOglasi = ctx.getRealPath(""); // Sta mu dodje ovo?
			ctx.setAttribute("oglasDAO", new OglasDAO(pathOglasi));
		}
		
	}
	
	
	//Registracija
	@POST
	@Path("/registracija")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registracija(Kupac kp, @Context HttpServletRequest request) {
		KupacDAO kupci = (KupacDAO)ctx.getAttribute("kupacDAO"); //Vraca mi listu svih kupaca koji 
		Kupac kupac = kupci.find(kp.getKorisnickoIme()); //Nalazi trazenog kupca unutar liste iz prethodne linije
		Response ret;
		if(kupac != null) {
			ret =  Response.status(400).build();
		}
		
		kupci.getKupci().put(kp.getKorisnickoIme(), kp);
		ctx.setAttribute("kupacDAO", kupci); //Postavljam novu listu kupaca
		kupci.sacuvajKupce(kupci.getPath()); //Iskreno se nadam da je ovako. PROVERITI
		ret = Response.status(200).build();
		return ret;
	}
	
	//Logovanje //Doradi malo logiku
	@POST
	@Path("/login/{korisnickoIme}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<String> login(@PathParam("korisnickoIme")String korisnickoIme,@Context HttpServletRequest request){

		ArrayList<String> ret = new ArrayList<>();
		AdministratorDAO administratori = (AdministratorDAO)ctx.getAttribute("administratorDAO");
		KupacDAO kupci= (KupacDAO)ctx.getAttribute("kupacDAO");
		ProdavacDAO prodavci= (ProdavacDAO)ctx.getAttribute("prodavacDAO");
		
		String[] parametri = korisnickoIme.split(",");
		String korIme = parametri[0];
		String lozinka = parametri[1];
		String uloga = parametri[2];
		
		if(uloga.equals("Kupac")){
			for(Kupac k:kupci.getKupci().values()){
				if(k.getKorisnickoIme().equals(korIme) && k.getLozinka().equals(lozinka)){
				ret.add("Kupac");
				ctx.setAttribute("kupacDAO",kupci );
				request.getSession().setAttribute("kupac", k);
				break;
				}
			}
		}else if(uloga.equals("Administrator")){
			for(Administrator k:administratori.getAdministratori().values()){
				if(k.getKorisnickoIme().equals(korIme) && k.getLozinka().equals(lozinka)){
				ret.add("Administrator");
				ctx.setAttribute("administratorDAO",administratori );
				request.getSession().setAttribute("administrator", k);
				break;
				}
			}
		} else{
			for(Prodavac k:prodavci.getProdavci().values()){
				if(k.getKorisnickoIme().equals(korIme) && k.getLozinka().equals(lozinka)){
				ret.add("Prodavac");
				ctx.setAttribute("prodavacDAO",prodavci );
				request.getSession().setAttribute("prodavac", k);
				break;
			}
		}
		}
		return ret;
	}
	
	
	// dodavanje recenzije 
	@POST
	@Path("/dodajRecenziju")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajRecenziju(Recenzija recenzija, @Context HttpServletRequest request){
		Kupac kupac = (Kupac)request.getSession().getAttribute("kupac");
		
		if(kupac == null){
			return Response.status(400).build();
		}
		
		kupac.getListaRecenzija().add(kupac.getListaRecenzija().size()+1,recenzija);
	return Response.status(200).build();

		
	}
	
	/*
	 * vraca listu svih recenzija kupca koji ima sesiju
	 * */
	@GET
	@Path("/recenzije")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Recenzija> getRecenzije (@Context HttpServletRequest request){
		Kupac k = (Kupac)request.getSession().getAttribute("kupac");
		ArrayList<Recenzija>rec = new ArrayList<>();
		try{
			for(Recenzija r : k.getListaRecenzija()){
				rec.add(r);
			}
		}catch(Exception e){
			
		}
		return rec;
		
	}
	
	@GET
	@Path("/kategorijeAdmin")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Kategorija> getKategorijeAdmin(@Context HttpServletRequest request){
		//Administrator a = (Administrator)request.getSession().getAttribute("administrator");
		
		ArrayList<Kategorija> ret = new ArrayList<>();
		KategorijaDAO kategorije = (KategorijaDAO) ctx.getAttribute("kategorijaDAO");
		System.out.println(kategorije.getKategorije().size());
		
		for(Kategorija k : kategorije.getKategorije().values()){
			System.out.println(k.getNaziv());
			ret.add(k);
			
		}
		return ret;
	}
	
	@POST
	@Path("/dodajKategorijuAdmin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajdodaj(Kategorija kategorija, @Context HttpServletRequest request){
		Administrator admin=(Administrator) request.getSession().getAttribute("administrator");
		KategorijaDAO kategorije=(KategorijaDAO) ctx.getAttribute("kategorijaDAO");
		if(admin==null)
		{
			return Response.status(400).build();
		}
		// provera da li postoji kategorija sa tim nazivom
		for(Kategorija k : kategorije.getKategorije().values()){
			if(k.getNaziv().equals(kategorija.getNaziv())){
				return Response.status(400).build();
			}
		}
		kategorija.setListaOglasa(new ArrayList<Oglas>());
		kategorije.getKategorije().put(kategorija.getNaziv(), kategorija);
		kategorije.sacuvajKategorije();
		return Response.status(200).build();
	}
	
	
	@DELETE		
	@Path("/obrisiKategoriju/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Kategorija>brisi(@PathParam("naziv") String naziv,@Context HttpServletRequest request){
		KategorijaDAO kategorije = (KategorijaDAO) ctx.getAttribute("kategorijaDAO");
		Administrator u = (Administrator)request.getSession().getAttribute("administrator");		
		ArrayList<Kategorija> rac=new ArrayList<>();
		try
		{
		for(Kategorija k : kategorije.getKategorije().values()) {
			if(k.getNaziv().equals(naziv))
			{	
				kategorije.getKategorije().remove(k.getNaziv());
			}
			rac.add(k);
		}
		}
		catch(Exception e)
		{			
		}
		kategorije.sacuvajKategorije();
		return rac;
		
	}
	
	@GET
	@Path("/selektovanaKategorija/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public void getKategorija(@PathParam("naziv") String naziv,@Context HttpServletRequest request){
		selektovan = naziv; 
	}
	
	@POST
	@Path("/promeniKategorijuAdmin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response nesto(Kategorija kategorija, @Context HttpServletRequest request){
		Administrator admin=(Administrator)request.getSession().getAttribute("administrator");		
		KategorijaDAO kategorije=(KategorijaDAO) ctx.getAttribute("kategorijaDAO");
		if(admin==null)
		{
			System.out.println("KOMSO NE SMARAJ");
		  return Response.status(400).build();
		}
		//Ako se pokusa izbrisati kategorija a da nije selektovana,kategorije imaju jedinstven nacin,al ako je doslo do narusenja (npr pri editovanjem fajla podataka)
		for(Kategorija k : kategorije.getKategorije().values()){
			if(k.getNaziv().equals(kategorija.getNaziv()) && (!(k.getNaziv().equals(selektovan)))){//
				return Response.status(400).build();
			}
		}
		//izmena kategorije
		for(Kategorija k : kategorije.getKategorije().values()){
			if(k.getNaziv().equals(selektovan)){
				k.setNaziv(kategorija.getNaziv());
				k.setOpis(kategorija.getOpis());
				break;
			}
		}
		kategorije.sacuvajKategorije();
		return Response.status(200).build();
	}
	
	
	@POST
	@Path("/dodajPorukuAdmin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajPorukuAdmin(Poruka poruka,@Context HttpServletRequest request){
		Administrator admin = (Administrator)request.getSession().getAttribute("administrator");
		AdministratorDAO admins = (AdministratorDAO)ctx.getAttribute("administratorDAO");
		ProdavacDAO prodavci = (ProdavacDAO)ctx.getAttribute("prodavacDAO");
		KupacDAO kupci = (KupacDAO)ctx.getAttribute("kupacDAO");
		if(admin == null){
			Response.status(400).build();
		}
		//promenljiva za odredjivanje kojoj ulozi je poslato
		int in = 0;
		if(poruka.getUlogiPoslato().equals("kupac")){
			for(Kupac c:kupci.getKupci().values()){
				if(c.getKorisnickoIme().equals(poruka.getPrimalac())){
					System.out.println("dal vidi da treba kupcu?");
					in=2;//kupac je uloga 2 nek bude
				}
			}
		}else if(poruka.getUlogiPoslato().equals("prodavac")){
			for(Prodavac p:prodavci.getProdavci().values()){
				if(p.getKorisnickoIme().equals(poruka.getPrimalac())){
					in=3;//prodavac 3
				}
			}
		}else if(poruka.getUlogiPoslato().equals("administrator")){
			for(Administrator c:admins.getAdministratori().values()){
				if(c.getKorisnickoIme().equals(poruka.getPrimalac())){
					in=1; 
				}
			}
		}else {
			
		}
		
		if(!(in==1 || in==2 || in==3)){
			return Response.status(400).build();
		}
		if(in==2){
			Random rand = new Random();
			int n = rand.nextInt(200000);
			Random rand1 = new Random();
			int n1 = rand1.nextInt(200000);
		
			poruka.setId(n);
			PorukaDAO.getCurrentTimeWithOffset();
			poruka.setDatum(PorukaDAO.trenutnoVreme);
			poruka.setUlogaPoslala("administrator1");
			poruka.setPosiljalac(admin.getKorisnickoIme());
			poruka.setUlogiPoslato("customer");
			admin.getPoruke().add(poruka);
		poruka.setId(n1);		
			for(Kupac s : kupci.getKupci().values()){
				if(s.getKorisnickoIme().equals(poruka.getPrimalac())){
					s.getPoruke().add(poruka);
				}
			}
		}else if(in==3){
			Random rand = new Random();
			int n = rand.nextInt(200000);
			Random rand1 = new Random();
			int n1 = rand1.nextInt(200000);
			poruka.setId(n);
			PorukaDAO.getCurrentTimeWithOffset();
			poruka.setDatum(PorukaDAO.trenutnoVreme);
			poruka.setUlogaPoslala("administrator1");
			poruka.setPosiljalac(admin.getKorisnickoIme());
			poruka.setUlogiPoslato("seller");
			admin.getPoruke().add(poruka);
			poruka.setId(n1);
			for(Prodavac s : prodavci.getProdavci().values()){
				if(s.getKorisnickoIme().equals(poruka.getPrimalac())){
					s.getListaPoruka().add(poruka);
				}
			}
		}else {
			Random rand = new Random();
			int n = rand.nextInt(200000);
			Random rand1 = new Random();
			int n1 = rand1.nextInt(200000);
			poruka.setId(n);
			PorukaDAO.getCurrentTimeWithOffset();
			poruka.setDatum(PorukaDAO.trenutnoVreme);
			poruka.setUlogaPoslala("administrator1");
			poruka.setPosiljalac(admin.getKorisnickoIme());
			poruka.setUlogiPoslato("administrator");
			admin.getPoruke().add(poruka);
			poruka.setId(n1);
			for(Administrator s : admins.getAdministratori().values()){
				if(s.getKorisnickoIme().equals(poruka.getPrimalac())){
					s.getPoruke().add(poruka);
				}
			}
		}
		admins.sacuvajAdministratore(admins.getPath());
		kupci.sacuvajKupce(kupci.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
		return Response.status(200).build();
	}
	
	// lista poruka od admina
	@GET
	@Path("/porukeAdmin")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Poruka> vratiPoruke(@Context HttpServletRequest request){
		ArrayList<Poruka> rac = new ArrayList<>();
		//uzimanje svih postojecih poruka
		Administrator admin=(Administrator) request.getSession().getAttribute("administrator");
		for(Poruka p : admin.getPoruke()){
			rac.add(p);
	}
		return rac;
	}
	
	@DELETE
	@Path("/izbrisiPorukuAdmin/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Poruka> deleteAdmin(@PathParam("id")int id, @Context HttpServletRequest request){
		ArrayList<Poruka> ret = new ArrayList<>();
		Administrator admin = (Administrator)request.getSession().getAttribute("administrator");
		AdministratorDAO admins = (AdministratorDAO)ctx.getAttribute("administratorDAO");
		//Poruka ima jedinstven ID pa je dovoljno da se nadje 1 id
		for(Poruka p : admin.getPoruke()){
			if(p.getId() == id){
				admin.getPoruke().remove(p);
				break;
			}
		}
		//uzimanje svih preostalih poruka 
		for(Poruka p:admin.getPoruke()){
			ret.add(p);
		}
		admins.sacuvajAdministratore(admins.getPath());
		return ret;
			
	}
	
	
	@GET
	@Path("/kojaPorukaZaIzmenu/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void stacestakojeeeL(@PathParam("id") int id,@Context HttpServletRequest request){
		pZaIzm = id;
	}
	
	@POST
	@Path("/izmeniPorukuAdmin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response promeniPA(Poruka poruka, @Context HttpServletRequest request){
		
		Administrator admin = (Administrator) request.getSession().getAttribute("administrator");
		AdministratorDAO admins = (AdministratorDAO) ctx.getAttribute("administratorDAO");
		if(admin == null){
			return Response.status(400).build();
		}
		//kad nadje ID koji je dobijen malo pre onda ga menja
		for(Poruka p : admin.getPoruke()){
			if(p.getId() == pZaIzm){
				p.setNazivPoruke(poruka.getNazivPoruke());
				p.setNaslovPoruke(poruka.getNaslovPoruke());
				p.setSadrzajPoruke(poruka.getSadrzajPoruke());
				break;
			}
		}	
		admins.sacuvajAdministratore(admins.getPath());
		return Response.status(200).build();
	}
	
	@GET
	@Path("/uzmiOglaseAdmin")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> uzmiAdmin(@Context HttpServletRequest request){
		ArrayList<Oglas> rac=new ArrayList<>();

		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO");
		for(Oglas r:oglasi.getOglasi().values()) {
			rac.add(r);
		}
		return rac;
	}
	
	
	@DELETE
	@Path("/izbrisiOglasAdmin/{naziv}")
//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> izbrisiOglasAdmin(@PathParam("naziv") String naziv, @Context HttpServletRequest request)
	{
		String[] niz = naziv.split(",");
		String naziv11 = niz[0];
		
		Administrator admin = (Administrator) request.getSession().getAttribute("administrator");
		AdministratorDAO administratori = (AdministratorDAO) ctx.getAttribute("administratorDAO");
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		KategorijaDAO kategorije = (KategorijaDAO) ctx.getAttribute("kategorijaDAO");	
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO");
		ArrayList<Oglas> rac=new ArrayList<>();
		for(Oglas r:oglasi.getOglasi().values()) {
			if(r.getNaziv().equals(naziv11))
			{	//ISA DUDEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
				if(r.getOpis().equals("Novo")){
					oglasi.getOglasi().remove(r.getNaziv());
						for(Prodavac p1 : prodavci.getProdavci().values()){
						if(p1.getKorisnickoIme().equals(r.getKoJeNapravio())){
							PorukaDAO.getCurrentTimeWithOffset();
							//Uzimanje random Broja za ID
							Random rand=new Random();
							Random rand1=new Random();
							int n=rand.nextInt(200000);
							int n1=rand1.nextInt(200000);
							Poruka porukaProdavcu=new Poruka(n,"Obrisan oglas : "+naziv11,"oglas","ss",PorukaDAO.trenutnoVreme,"administrator",r.getNaziv(),"prodavci",p1.getKorisnickoIme());//Brise administrator neki oglas od Prodavca i mora da ga obavesti
							p1.getListaPoruka().add(porukaProdavcu);
							Poruka porukaAdministratoru=new Poruka(n1,"Obrisan oglas : "+naziv11,"oglas","ss",PorukaDAO.trenutnoVreme,"administrator",r.getNaziv(),"prodavci",p1.getKorisnickoIme());//Administratoru isto ide jedna poruka
							admin.getPoruke().add(porukaAdministratoru);
							for(Kategorija kat : kategorije.getKategorije().values()){
								for(Oglas o : kat.getListaOglasa()){
									if(o.getNaziv().equals(naziv11)){
										//Brisanje oglasa iz kategorije koja je sadrzi
										kat.getListaOglasa().remove(o);
										break;
									}
								}
							}
							//Brisanje Oglasa iz Liste oglasa,da se ne bi pojavljivao nadalje
							for(Oglas oss:p1.getListaObjavljenihOglasa()){
								if(oss.getNaziv().equals(naziv11)){
									p1.getListaObjavljenihOglasa().remove(oss);
									break;
								}
							}
						}
					}	
					break;
				}	
			}
		}
	
		//svi preostali oglasi
		for(Oglas o:oglasi.getOglasi().values()) {
			rac.add(o);
		}
		kategorije.sacuvajKategorije();
		oglasi.sacuvajOglase(oglasi.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
		administratori.sacuvajAdministratore(administratori.getPath());
		return rac;
	}
	
	
	@GET
	@Path("/dobijanjeSelektovanogOglasa/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public void dajkjsss(@PathParam("naziv") String naziv,@Context HttpServletRequest request){
		selektovaniOglas=naziv;
	}
	
	
	
	@POST
	@Path("/izmenaOglasaAdmin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dajIzmeniOglas(Oglas oglas, @Context HttpServletRequest request){
		Administrator admin=(Administrator) request.getSession().getAttribute("administrator");
		if(admin==null)
		{
			return Response.status(400).build();
		}
		OglasDAO oglasi=(OglasDAO) ctx.getAttribute("oglasDAO");
		AdministratorDAO admins=(AdministratorDAO) ctx.getAttribute("administratorDAO");
		KupacDAO kupci=(KupacDAO) ctx.getAttribute("kupacDAO");
		ProdavacDAO prodavci=(ProdavacDAO) ctx.getAttribute("prodavacDAO");
		KategorijaDAO kategorije=(KategorijaDAO) ctx.getAttribute("kategorijaDAO");
	
		for(Oglas c : oglasi.getOglasi().values()){
			if(c.getNaziv().equals(oglas.getNaziv()) && !c.getNaziv().equals(selektovaniOglas)){
				return Response.status(400).build();
			}
		}
		//trazimo naziv oglasa sa sacuvanom vrednoscu
		for(Oglas o : oglasi.getOglasi().values()){
			if(o.getNaziv().equals(selektovaniOglas)){
				o.setNaziv(oglas.getNaziv());
				o.setAktivan(oglas.getAktivan());
				o.setGrad(oglas.getGrad());
				o.setDatumIsticanja(oglas.getDatumIsticanja());
				o.setDatumPostavljanja(oglas.getDatumPostavljanja());
				o.setSlika(oglas.getSlika());
				o.setCena(oglas.getCena());
				for(Prodavac pp : prodavci.getProdavci().values()){
					//trazi se prodavac koji je napravio
					if(pp.getKorisnickoIme().equals(o.getKoJeNapravio())){
						for(Oglas oo: pp.getListaObjavljenihOglasa()){
							if(oo.getNaziv().equals(o.getNaziv())){
								oo.setNaziv(oglas.getNaziv());
								oo.setAktivan(oglas.getAktivan());
								oo.setGrad(oglas.getGrad());
								oo.setDatumIsticanja(oglas.getDatumIsticanja());
								oo.setDatumPostavljanja(oglas.getDatumPostavljanja());
								oo.setSlika(oglas.getSlika());
								oo.setCena(oglas.getCena());
								PorukaDAO.getCurrentTimeWithOffset();
								//Daje se novi ID promenjenoj poruci
								Random rand=new Random();
								int n=rand.nextInt(200000);
								Poruka p1=new Poruka(n,"Promenjen","naslov1","sss",PorukaDAO.trenutnoVreme,"administrator",admin.getKorisnickoIme(),"seller",pp.getKorisnickoIme());
								Random rand1=new Random();
								int n1=rand1.nextInt(200000);
								Poruka p2=new Poruka(n1,"Promenjen","naslov1","sss",PorukaDAO.trenutnoVreme,"administrator",admin.getKorisnickoIme(),"seller",pp.getKorisnickoIme());
								pp.getListaPoruka().add(p1);
								admin.getPoruke().add(p2);
					}
	}
			}
	}
				//Menjanje oglasa kod kupaca koji su narucili
				for(Kupac cc:kupci.getKupci().values()){
					for(Oglas oo:cc.getListaPorucenihOglasa()){
						if(oo.getNaziv().equals(o.getNaziv())){
							oo.setNaziv(oglas.getNaziv());
							oo.setAktivan(oglas.getAktivan());
							oo.setGrad(oglas.getGrad());
							oo.setDatumIsticanja(oglas.getDatumIsticanja());
							oo.setDatumPostavljanja(oglas.getDatumPostavljanja());
							oo.setSlika(oglas.getSlika());
							oo.setCena(oglas.getCena());
							PorukaDAO.getCurrentTimeWithOffset();
							Random rand = new Random();
							int n = rand.nextInt(200000);
							Random rand1 = new Random();
							int n1 = rand1.nextInt(200000);
							Poruka p = new Poruka(n,"Promenjen","naslov1","sss",PorukaDAO.trenutnoVreme,"administrator",admin.getKorisnickoIme(),"customer",cc.getKorisnickoIme());
							Poruka p1 = new Poruka(n1,"Promenjen","naslov1","sss",PorukaDAO.trenutnoVreme,"administrator",admin.getKorisnickoIme(),"customer",cc.getKorisnickoIme());
							admin.getPoruke().add(p1);
							cc.getPoruke().add(p);
							
				}
				}
		}
			
		}
	}
				//menjanje kod kategorija 
		
		for(Kategorija kat : kategorije.getKategorije().values()){
			for(Oglas c : kat.getListaOglasa()){
				if(c.getNaziv().equals(selektovaniOglas)){
					c.setNaziv(oglas.getNaziv());
					c.setAktivan(oglas.getAktivan());
					c.setGrad(oglas.getGrad());
					c.setDatumIsticanja(oglas.getDatumIsticanja());
					c.setDatumPostavljanja(oglas.getDatumPostavljanja());
					c.setOpis(oglas.getOpis());
					c.setSlika(oglas.getSlika());
					c.setCena(oglas.getCena());
					break;
					
				}
			}
		}
		admins.sacuvajAdministratore(admins.getPath());
		kupci.sacuvajKupce(kupci.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
		kategorije.sacuvajKategorije();
		oglasi.sacuvajOglase(oglasi.getPath());
		return Response.status(200).build();
		
	}
	
	@GET
	@Path("/uzmiKupce")
	//@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Kupac> dajKupce(@Context HttpServletRequest request){
		ArrayList<Kupac> rac = new ArrayList<>();
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");
		for(Kupac k: kupci.getKupci().values()){
			rac.add(k);
		}
		
		return rac;
		
	}
	
	@GET
	@Path("/uzmiProdavce")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Prodavac> dajProdavce(@Context HttpServletRequest request){
		ArrayList<Prodavac> rac = new ArrayList<>();
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		for(Prodavac p: prodavci.getProdavci().values()){
			rac.add(p);
		}
		return rac;
		
	}
	
	
	@PUT
	@Path("/promeniKupcaUAdmina/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public void promeniKuA(@PathParam("username")String username,@Context HttpServletRequest request){
		AdministratorDAO admins = (AdministratorDAO) ctx.getAttribute("administratorDAO");
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");
		Kupac t = new Kupac();
		boolean usao = false;
		for(Kupac c : kupci.getKupci().values()){
			if(c.getKorisnickoIme().equals(username)){
				usao = true;
				t = c;
				kupci.getKupci().remove(username);
			}
		}
		
		if(usao == true){
			Administrator admin = new Administrator(t.getKorisnickoIme(), t.getLozinka(), t.getIme(), t.getPrezime(), "administrator", t.getKontaktTelefon(), t.getGrad(),t.getEmail(), t.getDatumRegistracije());
			admins.getAdministratori().put(t.getKorisnickoIme(), admin);
			ctx.setAttribute("administratorDAO",admins);
			request.getSession().setAttribute("administrator", admin);
		}
		
		admins.sacuvajAdministratore(admins.getPath());
		kupci.sacuvajKupce(kupci.getPath());
		
	}
	
	
	@PUT
	@Path("/promeniKupcaUProdavca/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public void promeniKuP(@PathParam("username")String username,@Context HttpServletRequest request){
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		Kupac temp = new Kupac();
		temp = kupci.getKupci().get(username);
		kupci.getKupci().remove(username);
		Prodavac p = new Prodavac(temp.getKorisnickoIme(),temp.getLozinka(),temp.getIme(),temp.getPrezime(),"prodavac",temp.getKontaktTelefon(),temp.getGrad(),temp.getEmail(),temp.getDatumRegistracije(),0,0,false);
		prodavci.getProdavci().put(p.getKorisnickoIme(), p);
		ctx.setAttribute("prodavacDAO",prodavci);
		request.getSession().setAttribute("prodavac",p);
		prodavci.sacuvajProdavce(prodavci.getPath());
		kupci.sacuvajKupce(kupci.getPath());
		
	}
	
	
	
	@PUT
	@Path("/promeniProdavcaUAdmina/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public void promjeniPuA(@PathParam("username")String username,@Context HttpServletRequest request){

		AdministratorDAO admins = (AdministratorDAO) ctx.getAttribute("administratorDAO");
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		Prodavac temp = new Prodavac();
		temp = prodavci.getProdavci().get(username);
		prodavci.getProdavci().remove(username);
			Administrator admin = new Administrator(temp.getKorisnickoIme(), temp.getLozinka(), temp.getIme(), temp.getPrezime(), "administrator", temp.getKontaktTelefon(), temp.getGrad(),temp.getEmail(), temp.getDatumRegistracije());
			admins.getAdministratori().put(temp.getKorisnickoIme(), admin);
			ctx.setAttribute("administratorDAO",admins);
			request.getSession().setAttribute("administrator", admin);

		admins.sacuvajAdministratore(admins.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
		
	}
	
	
	@PUT
	@Path("/promeniProdavcaUKupca/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public void promjeniProdavcaUKupca(@PathParam("username")String username,@Context HttpServletRequest request){
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		Prodavac tempP = new Prodavac();
		tempP = prodavci.getProdavci().get(username);
		prodavci.getProdavci().remove(username);
		Kupac kupac = new Kupac(tempP.getKorisnickoIme(), tempP.getLozinka(), tempP.getIme(), tempP.getPrezime(), "kupac", tempP.getKontaktTelefon(), tempP.getGrad(),tempP.getEmail(), tempP.getDatumRegistracije());
		kupci.getKupci().put(tempP.getKorisnickoIme(), kupac);
		ctx.setAttribute("kupacDAO",kupci);
		request.getSession().setAttribute("kupac", kupac);
		kupci.sacuvajKupce(kupci.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
	}
	
	@GET
	@Path("/uzmiOglaseProdavac")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> uzmiOglaseZP(@Context HttpServletRequest request){
		ArrayList<Oglas> rac = new ArrayList<>();
		Prodavac p=(Prodavac) request.getSession().getAttribute("prodavac");
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO");
		for(Oglas o:oglasi.getOglasi().values()) {
		rac.add(o);
		}
		return rac;
	}
	
	
	@GET
	@Path("/posaljiUpozorenje/{sve}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendWarning(@PathParam("sve")String sve,@Context HttpServletRequest request){
		String[] niz = sve.split(",");
		String nekoIme = niz[0];
		String ko = niz[1];
		Administrator admin=new Administrator();
		AdministratorDAO admins=(AdministratorDAO) ctx.getAttribute("administratorDAO");
		ProdavacDAO prodavci=(ProdavacDAO) ctx.getAttribute("prodavacDAO");
		for(Administrator a :admins.getAdministratori().values()){
			admin = a;
			break;
		}
		for(Prodavac s : prodavci.getProdavci().values()){
			if(s.getKorisnickoIme().equals(ko)){
				
				PorukaDAO.getCurrentTimeWithOffset();
				Random rand = new Random();
				int n = rand.nextInt(200000);
				Random rand1 = new Random();
				int n1 = rand1.nextInt(200000);
				Poruka p = new Poruka(n,"UPOZORENJE","naslov","sadrzaj",PorukaDAO.trenutnoVreme,"administrator",admin.getIme(),"prodavac",s.getKorisnickoIme());
				Poruka p1 = new Poruka(n1,"UPOZORENJE","naslov","sadrzaj",PorukaDAO.trenutnoVreme,"administrator",admin.getIme(),"prodavac",s.getKorisnickoIme());
				s.getListaPoruka().add(p);
				admin.getPoruke().add(p1);
				
			}
		}
		int i = 0;
		for(Prodavac s : prodavci.getProdavci().values()){
			if(s.getKorisnickoIme().equals(ko)){
				for(Poruka p : s.getListaPoruka()){
					if(p.getNazivPoruke().equals("UPOZORENJE")){
						i++;
					}
				}
				if(i >= 3){
					s.setMarkiran(true);
					markiraniProdavci.add(s);
				}
			}
		}
		prodavci.sacuvajProdavce(prodavci.getPath());
		admins.sacuvajAdministratore(admins.getPath());
		return Response.status(200).build();
	}
	
	@GET
	@Path("/poruciOglas/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> vrati(@PathParam("name")String name,@Context HttpServletRequest request){
		ArrayList<Oglas> rac=new ArrayList<>();
		Kupac kupac=(Kupac) request.getSession().getAttribute("kupac");
		ProdavacDAO prodavci=(ProdavacDAO) ctx.getAttribute("prodavacDAO");
		OglasDAO oglasi=(OglasDAO) ctx.getAttribute("oglasDAO");
		KupacDAO customers = (KupacDAO) ctx.getAttribute("kupacDAO");
		Oglas oglas = new Oglas();
		for(Oglas o : oglasi.getOglasi().values()){
			if(o.getNaziv().equals(name)){
				oglas = o;
				break;
			}
		}
		oglas.setOpis("Realizacija");
		kupac.getListaPorucenihOglasa().add(oglas);
		for(Oglas o:oglasi.getOglasi().values()){
			if(o.getNaziv().equals(name)){
				o.setOpis("Realizacija");
			}
		}
		for(Prodavac o : prodavci.getProdavci().values()){
			for(Oglas oglas1 : o.getListaObjavljenihOglasa()){
				if(oglas1.equals(name)){
					oglas1.setOpis("Realizacija");
				}
			}
		}
		for(Oglas o : oglasi.getOglasi().values()){
			rac.add(o);
		}
		prodavci.sacuvajProdavce(prodavci.getPath());
		oglasi.sacuvajOglase(oglasi.getPath());
		customers.sacuvajKupce(customers.getPath());
		return rac;
	}
	
	@PUT
	@Path("/dodajUFavorit/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajMedjuOmiljeneOglasee(@PathParam("name")String name,@Context HttpServletRequest request){
		
		Kupac kupac=(Kupac) request.getSession().getAttribute("kupac");
		KupacDAO kupci=(KupacDAO) ctx.getAttribute("kupacDAO");
		OglasDAO oglasi=(OglasDAO) ctx.getAttribute("oglasDAO");
		if(kupac.getListaOmiljenihOglasa().size() != 0){
			for(Oglas o : kupac.getListaOmiljenihOglasa()){
				if(o.getNaziv().equals(name)){
					return Response.status(400).build();
				}
			}
		}
		for(Oglas o : oglasi.getOglasi().values()){
			if(o.getNaziv().equals(name)){
				int counter = o.getLajkovi();
				counter++;
				o.setLajkovi(counter);
				kupac.getListaOmiljenihOglasa().add(o);
			}
		}
		
		kupci.sacuvajKupce(kupci.getPath());
		return Response.status(200).build();
	}
	
	
	
	@GET
	@Path("/uzmiPoruceneOglaseKupac")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> uzmiU(@Context HttpServletRequest request){
		ArrayList<Oglas> rac=new ArrayList<>();
		Kupac u=(Kupac) request.getSession().getAttribute("kupac");
		for(Oglas o:u.getListaPorucenihOglasa()) {
			rac.add(o);
		}
		return rac;
	}
	
	
	@PUT
	@Path("/lajkuj/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> lajkujAD(@PathParam("name")String name,@Context HttpServletRequest request){
		String[] niz = name.split(",");
		ArrayList<Oglas> rac = new ArrayList<>();
		String naziv = niz[0];
		String ko = niz[1];
		Kupac kupac = (Kupac) request.getSession().getAttribute("kupac");
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO"); 
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO"); 
		int broj=0;
		for(Prodavac s:prodavci.getProdavci().values()){
			if(s.getKorisnickoIme().equals(ko)){
				int brojLajkova = s.getBrojLajkova();
				brojLajkova++;
				s.setBrojLajkova(brojLajkova);
				for(Oglas o1:s.getListaObjavljenihOglasa()){
					if(o1.getNaziv().equals(naziv)){
						int brojLajkova1 = o1.getLajkovi();
						System.out.println(brojLajkova1);
						brojLajkova1++;
						broj=brojLajkova1;
						System.out.println(broj);
						o1.setLajkovi(brojLajkova1);
						
					}
				}
				for(Oglas o:s.getListaIsporucenihOglasa()){
					if(o.getNaziv().equals(naziv)){
						int brojLajkova0 = o.getLajkovi();
						System.out.println(brojLajkova0);
						brojLajkova0++;
						o.setLajkovi(brojLajkova0);
						
					}
				}
			}
		}
		for(Oglas o2:oglasi.getOglasi().values()){
			if(o2.getNaziv().equals(naziv)){
				int brojLajkova2 = o2.getLajkovi();
				System.out.println(brojLajkova2);
				brojLajkova2++;
				o2.setLajkovi(broj);
				break;
			}
		}
		for(Oglas o4 : kupac.getListaPorucenihOglasa()){
			if(o4.getNaziv().equals(naziv)){
				int brojLajkova3 = o4.getLajkovi();
				System.out.println(brojLajkova3);
				brojLajkova3++;
				o4.setLajkovi(broj);
			}
		}
		for(Oglas o5 : kupac.getListaOmiljenihOglasa()){
			if(o5.getNaziv().equals(naziv)){
				int brojLajkova4 = o5.getLajkovi();
				System.out.println(brojLajkova4);
				brojLajkova4++;
				o5.setLajkovi(broj);
			}
		}
		for(Oglas o6 : kupac.getListaDostavljenihOglasa()){
			if(o6.getNaziv().equals(naziv)){
				int brojLajkova5 = o6.getLajkovi();
				System.out.println(brojLajkova5);
				brojLajkova5++;
				o6.setLajkovi(broj);
			}
		}

		
		for(Oglas o7:oglasi.getOglasi().values()){
			//if(o7.getNaziv().equals(naziv)){
			
			rac.add(o7);//}
		}
		
		
		kupci.sacuvajKupce(kupci.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
		oglasi.sacuvajOglase(oglasi.getPath());
		return rac;
	}
	
	
	@PUT
	@Path("/promeniUDostavljen/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> promeniIUDostavljen(@PathParam("name")String name,@Context HttpServletRequest request){
		ArrayList<Oglas> rac1 = new ArrayList<>();
		Kupac u=(Kupac) request.getSession().getAttribute("kupac");
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO");
		String koJePoslao = "";
		Oglas o1 = new Oglas();
		Kupac kupac = (Kupac) request.getSession().getAttribute("kupac");
		for(Oglas o: oglasi.getOglasi().values()){
			if(o.getNaziv().equals(name)){
				o.setOpis("Dostavljen");
				koJePoslao = o.getKoJeNapravio();
				o1 = o;
			}
		}
		for(Oglas o: u.getListaPorucenihOglasa()){
			if(o.getNaziv().equals(name)){
				o.setOpis("Dostavljen");
				koJePoslao = o.getKoJeNapravio();
			}
		}
		for(Prodavac s : prodavci.getProdavci().values()){
			for(Oglas o: s.getListaObjavljenihOglasa()){
				if(o.getNaziv().equals(name)){
					o.setOpis("Dostavljen");
				}
			}
		}
		
		for(Oglas o: u.getListaPorucenihOglasa()){
			rac1.add(o);
		}
		
		for(Prodavac s : prodavci.getProdavci().values()){
			if(s.getKorisnickoIme().equals(koJePoslao)){
				PorukaDAO.getCurrentTimeWithOffset();
				Random rand = new Random();
				int n = rand.nextInt(200000);
				Random rand1 = new Random();
				int n1 = rand1.nextInt(200000);
				Poruka p = new Poruka(n,"Dostavljen","naslov","sadrzaj",PorukaDAO.trenutnoVreme,"kupac",u.getIme(),"prodavac",s.getKorisnickoIme());
				Poruka p1 = new Poruka(n1,"Dostavljen","naslov","sadrzaj",PorukaDAO.trenutnoVreme,"kupac",u.getIme(),"prodavac",s.getKorisnickoIme());
				s.getListaIsporucenihOglasa().add(o1);
				kupac.getPoruke().add(p1);
			}
		}
		for(Oglas o: u.getListaPorucenihOglasa()){
			if(o.getNaziv().equals(name)){
				u.getListaDostavljenihOglasa().add(o);
			}
		}
		oglasi.sacuvajOglase(oglasi.getPath());
		kupci.sacuvajKupce(kupci.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
		return rac1;
	}
	
	
	
	@GET
	@Path("/dobijanjeOglasaZaRecenziju/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public void dajRezencijijsuzs(@PathParam("naziv") String naziv,@Context HttpServletRequest request){
		oglasZaRecenziju=naziv;
	}
	
	@PUT
	@Path("/dislajkuj/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> dislajkujAD(@PathParam("name")String name,@Context HttpServletRequest request){
		String[] niz = name.split(",");
		ArrayList<Oglas> rac = new ArrayList<>();
		String naziv = niz[0];
		String ko = niz[1];
		Kupac customer = (Kupac) request.getSession().getAttribute("kupac");
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO"); 
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO"); 
		int broj=0;
		for(Prodavac s:prodavci.getProdavci().values()){
			if(s.getKorisnickoIme().equals(ko)){
				int brojLajkova = s.getBrojDislajkova();
				brojLajkova++;
				for(Oglas o:s.getListaObjavljenihOglasa()){
					if(o.getNaziv().equals(naziv)){
						int brojLajkova2 = o.getDislajkovi();
						brojLajkova2++;
						broj=brojLajkova2;
						o.setDislajkovi(broj);
						
					}
				}
				s.setBrojDislajkova(brojLajkova);
				for(Oglas o:s.getListaIsporucenihOglasa()){
					if(o.getNaziv().equals(naziv)){
						int brojLajkova1 = o.getDislajkovi();
						brojLajkova1++;
						o.setDislajkovi(brojLajkova1);
						
					}
				}
			}
		}
		for(Oglas s:oglasi.getOglasi().values()){
			if(s.getNaziv().equals(naziv)){
				int brojLajkova3 = s.getDislajkovi();
				brojLajkova3++;
				s.setDislajkovi(broj);
				break;
			}
		}
		for(Oglas s : customer.getListaPorucenihOglasa()){
			if(s.getNaziv().equals(naziv)){
				int brojLajkova = s.getDislajkovi();
				brojLajkova++;
				s.setDislajkovi(broj);
			}
		}
		for(Oglas s : customer.getListaOmiljenihOglasa()){
			if(s.getNaziv().equals(naziv)){
				int brojLajkova4 = s.getDislajkovi();
				brojLajkova4++;
				s.setDislajkovi(broj);
			}
		}
		for(Oglas s : customer.getListaDostavljenihOglasa()){
			if(s.getNaziv().equals(naziv)){
				int brojLajkova5 = s.getDislajkovi();
				brojLajkova5++;
				s.setDislajkovi(broj);
			}
		}

		
		for(Oglas s:oglasi.getOglasi().values()){
			//if(s.getNaziv().equals(naziv)){
			rac.add(s);//}
		}
		
		
		kupci.sacuvajKupce(kupci.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
		oglasi.sacuvajOglase(oglasi.getPath());
		return rac;
	}
	
	@GET
	@Path("/uzmiRecenzijeZaPrikaz")
	//@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Recenzija> uzmi(@Context HttpServletRequest request){
		ArrayList<Recenzija> rac = new ArrayList<>();		
		Kupac kupac = (Kupac) request.getSession().getAttribute("kupac");

		if(kupac == null){
			return null;
		}
		
		for(Recenzija r:kupac.getListaRecenzija()) {
			rac.add(r);
		}
		
		return rac;
	}
	
	@DELETE
	@Path("/obrisiRecenziju/{naziv}")
//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Recenzija> obrR(@PathParam("naziv") String naziv, @Context HttpServletRequest request)
	{
		String[] niz = naziv.split(",");
		String naslov = niz[0];
		String oglas = niz[1];
		
		Kupac kupac = (Kupac) request.getSession().getAttribute("kupac");
		AdministratorDAO administrators = (AdministratorDAO) ctx.getAttribute("administratorDAO");
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO");
		Prodavac p = (Prodavac) request.getSession().getAttribute("prodavac");
		
		ArrayList<Recenzija> rac = new ArrayList<>();
		
		for(Oglas oo:oglasi.getOglasi().values()){
			if(oo.getNaziv().equals(oglas)){
				for(int i = 0; i < oo.getListaRecenzija().size(); i++){
					if(oo.getListaRecenzija().get(i).getNaslovRecenzije().equals(naslov)){
						oo.getListaRecenzija().remove(i);
					}
				}
			}
		}
		for (int i = 0; i < kupac.getListaRecenzija().size(); i++) {
			if(kupac.getListaRecenzija().get(i).getNaslovRecenzije().equals(naslov)){
				kupac.getListaRecenzija().remove(i);
			}
		}
		
		
		for (int i = 0; i < kupac.getListaRecenzija().size(); i++) {
			rac.add(kupac.getListaRecenzija().get(i));
		}
		
		kupci.sacuvajKupce(kupci.getPath());
		oglasi.sacuvajOglase(oglasi.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
		administrators.sacuvajAdministratore(administrators.getPath());
		return rac;
	}
	
	
	
	@GET
	@Path("/dobijanjeSelektovaneRecenzije/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public void dajjjjjjj(@PathParam("naziv") String naziv,@Context HttpServletRequest request){
		String[] niz = naziv.split(",");
		String naziv1 = niz[0];
		String oglas1 = niz[1];
		selektovaniOglasZaDodavanjeRezencijeZaIzmenu = naziv1;
		selektovaniOglasZaDodavanjeRezencijeZaIzmenuOglas = oglas1;

		
	}
	
	
	
	@POST
	@Path("/dodajRecenzijuKupac")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dddd(Recenzija rec, @Context HttpServletRequest request){

		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO");

		Kupac kupac = (Kupac) request.getSession().getAttribute("kupac");
		if(kupac==null)
		{
			return Response.status(400).build();
		}
		
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		

		rec.setOglas(oglasZaRecenziju);
		rec.setRecenzent(kupac.getKorisnickoIme());

			kupac.getListaRecenzija().add(rec);
		
		
		for(Oglas oo:oglasi.getOglasi().values()){
			if(oo.getNaziv().equals(oglasZaRecenziju)){
				oo.getListaRecenzija().add(rec);
			}
		}
		for(Prodavac p : prodavci.getProdavci().values()){
			for(Oglas o : p.getListaObjavljenihOglasa()){
				if(o.getNaziv().equals(oglasZaRecenziju)){
					o.getListaRecenzija().add(rec);
				}
			}
		}
		for(Prodavac p : prodavci.getProdavci().values()){
			for(Oglas o : p.getListaIsporucenihOglasa()){
				if(o.getNaziv().equals(oglasZaRecenziju)){
					o.getListaRecenzija().add(rec);
				}
			}
		}
		
		prodavci.sacuvajProdavce(prodavci.getPath());
		kupci.sacuvajKupce(kupci.getPath());
		oglasi.sacuvajOglase(oglasi.getPath());
		return Response.status(200).build();
	}
	
	
	@POST
	@Path("/promeniRecenzijuKupac")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pRK(Recenzija oglas, @Context HttpServletRequest request){
		
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO");
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");

		Kupac kupac = (Kupac) request.getSession().getAttribute("kupac");
		
		for(Recenzija r: kupac.getListaRecenzija()){
			if(r.getNaslovRecenzije().equals(selektovaniOglasZaDodavanjeRezencijeZaIzmenu)){
				r.setSlika(oglas.getSlika());
				r.setIspostovanDogovor(oglas.isIspostovanDogovor());
				r.setNaslovRecenzije(oglas.getNaslovRecenzije());
				r.setOpisTacan(oglas.isOpisTacan());
				r.setSadrzajRecenzije(oglas.getSadrzajRecenzije());
			}
		}
		
		for(Oglas oo:oglasi.getOglasi().values()){
			if(oo.getNaziv().equals(selektovaniOglasZaDodavanjeRezencijeZaIzmenuOglas)){
				for(Recenzija r : oo.getListaRecenzija()){
					if(r.getNaslovRecenzije().equals(selektovaniOglasZaDodavanjeRezencijeZaIzmenu)){
						r.setSlika(oglas.getSlika());
						r.setIspostovanDogovor(oglas.isIspostovanDogovor());
						r.setNaslovRecenzije(oglas.getNaslovRecenzije());
						r.setOpisTacan(oglas.isOpisTacan());
						r.setSadrzajRecenzije(oglas.getSadrzajRecenzije());
					}
				}
			}
		}
		
		
		oglasi.sacuvajOglase(oglasi.getPath());
		kupci.sacuvajKupce(kupci.getPath());
		

		return Response.status(200).build();
	}
	
	
	@POST
	@Path("/kreirajPorukuKupac")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajPorukicucc(Poruka poruka,@Context HttpServletRequest request){
	
		boolean nasao = false;
		Kupac kupac = (Kupac) request.getSession().getAttribute("kupac");
		if(kupac == null){
			Response.status(400).build();
		}
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");
		//DA LI JE UNETO NESTO
		if(poruka.getPosiljalac().equals("") || poruka.getPrimalac().equals(null)){
			Response.status(400).build();
		}
		//DA LI JE NASAO PRODAVCA SA TIM KORISNICKIM IMENOM
		for(Prodavac p : prodavci.getProdavci().values()){
			if(p.getKorisnickoIme().equals(poruka.getPrimalac())){
				nasao = true;
			}
		}
		if(!nasao){
			return Response.status(400).build();
		}
		
		
		Random rand = new Random();
		int n = rand.nextInt(200000);
		poruka.setId(n);
		PorukaDAO.getCurrentTimeWithOffset();
		poruka.setDatum(PorukaDAO.trenutnoVreme);
		poruka.setUlogaPoslala("kupac");
		poruka.setPosiljalac(kupac.getKorisnickoIme());
		poruka.setUlogiPoslato("prodavac");
		
		kupac.getPoruke().add(poruka);
		
		
		
		for(Prodavac pppp : prodavci.getProdavci().values()){
			if(pppp.getKorisnickoIme().equals(poruka.getPrimalac())){
				pppp.getListaPoruka().add(poruka);
			}
		}
		
		
		
		
		kupci.sacuvajKupce(kupci.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
		
		return Response.status(200).build();
	}
	
	
	@GET
	@Path("/selektovanaPorukaKupac/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void getSelektovanaPorukaKupac(@PathParam("id") int id,@Context HttpServletRequest request){
		
		selektovanaPorukaKupac = id;
		
	}
	
	
	@POST
	@Path("/izmeniPorukuKupac")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dajPoruku(Poruka poruka, @Context HttpServletRequest request){
		
		Kupac kupac = (Kupac) request.getSession().getAttribute("kupac");
		
		if(kupac == null){
			return Response.status(400).build();
		}
		
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");
		for(Poruka p : kupac.getPoruke()){
			if(p.getId() == selektovanaPorukaKupac){
				p.setNazivPoruke(poruka.getNazivPoruke());
				p.setNaslovPoruke(poruka.getNaslovPoruke());
				p.setSadrzajPoruke(poruka.getSadrzajPoruke());
				break;
			}
		}
		
		kupci.sacuvajKupce(kupci.getPath());
		return Response.status(200).build();
	}
	
	@GET
	@Path("/uzmiPorukeKupac")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Poruka>vratiPorukeeee(@Context HttpServletRequest request){
		ArrayList<Poruka>ret=new ArrayList<>();
		Kupac kupac=(Kupac)request.getSession().getAttribute("kupac");
		for(Poruka p:kupac.getPoruke()){
			ret.add(p);
		}
		return ret;
		
	}
	
	
	@DELETE
	@Path("/izbrisiPorukuKupac/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Poruka> izbrisiSAD(@PathParam("id")int id,@Context HttpServletRequest request){
		ArrayList<Poruka> rac= new ArrayList<>();
		
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");
		Kupac kupac = (Kupac) request.getSession().getAttribute("kupac");
		
		for(Poruka p :kupac.getPoruke()){
			if(p.getId() == id){
				kupac.getPoruke().remove(p);
				break;
			}
		}
		for(Poruka p :kupac.getPoruke()){
			rac.add(p);
		}
		
		kupci.sacuvajKupce(kupci.getPath());
		return rac;
		
		
	}
	
	
	
	@GET
	@Path("/uzmiKategorije")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Kategorija> uzmiK(@Context HttpServletRequest request){
		ArrayList<Kategorija> rac = new ArrayList<>();
		
		Administrator a=(Administrator) request.getSession().getAttribute("administrator");
		
		KategorijaDAO kategorije = (KategorijaDAO) ctx.getAttribute("kategorijaDAO");
		

		
		for(Kategorija r:kategorije.getKategorije().values()) {
			rac.add(r);
		}
		
		return rac;
	}
	
	
	@GET
	@Path("/uzmiOglaseKategorije/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> dajOglaseIzKategorije(@PathParam("naziv") String naziv,@Context HttpServletRequest request){
		ArrayList<Oglas> rac= new ArrayList<>();
		KategorijaDAO kategorije = (KategorijaDAO) ctx.getAttribute("kategorijaDAO");
		
		for(Kategorija k : kategorije.getKategorije().values()){
			if(k.getNaziv().equals(naziv)){
				for(Oglas o : k.getListaOglasa()){
					rac.add(o);
				}
			}
		}
		

		return rac;
	}
	
	
	
	@POST
	@Path("/dodajOglasProdavac")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajdodajOg(Oglas oglas, @Context HttpServletRequest request){
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		Prodavac prodavac= (Prodavac) request.getSession().getAttribute("prodavac");
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO");
		System.out.println("dal si ovde");
		if(prodavac==null)
		{
			return Response.status(400).build();
		}
		//da li postoji oglas sa tim nazivom
		for(Oglas c : oglasi.getOglasi().values()){
			if(c.getNaziv().equals(oglas.getNaziv())){
				return Response.status(400).build();
			}
		}
		//ne sme biti markiran
		if(prodavac.isMarkiran() == false){
			System.out.println("Ulazis ovde????");
			oglas.setKoJeNapravio(prodavac.getKorisnickoIme());
			oglas.setLajkovi(0);
			oglas.setDislajkovi(0);
			oglas.setuKolikoOmiljenih(0);
			oglas.setListaRecenzija(new ArrayList<Recenzija>());
			System.out.println(oglas);
			prodavac.getListaObjavljenihOglasa().add(oglas);
			oglasi.getOglasi().put(oglas.getNaziv(), oglas);
		
			oglasi.sacuvajOglase(oglasi.getPath());
			prodavci.sacuvajProdavce(prodavci.getPath());
			
		}else {
			return Response.status(400).build();
		}
		return Response.status(200).build();
	}
	
	
	@PUT
	@Path("/dodajOglasUKategoriju/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ajdeDOdajreakow(@PathParam("naziv") String naziv, @Context HttpServletRequest request)
	{
		String[] niz = naziv.split(",");
		String oglas = niz[0];
		String kategorija = niz[1];
		Prodavac prodavac = (Prodavac) request.getSession().getAttribute("prodavac");
		KategorijaDAO kategorije = (KategorijaDAO) ctx.getAttribute("kategorijaDAO");
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO");
		
		Oglas o = new Oglas();
		
		for(Oglas oo : oglasi.getOglasi().values()){
			if(oo.getNaziv().equals(oglas)){
				o = oo;
			}
		}
		
		//brisemo iz prethodne
		for(Kategorija k : kategorije.getKategorije().values()){
			for(Oglas ogl : k.getListaOglasa()){
				if(ogl.getNaziv().equals(oglas)){
					k.getListaOglasa().remove(ogl);
					break;
				}
			}
		}
		for(Kategorija k2 : kategorije.getKategorije().values()){
			if(k2.getNaziv().equals(kategorija)){
				k2.getListaOglasa().add(o);
			}
		}
		
		kategorije.sacuvajKategorije();
		return Response.status(200).build();
	}
	
	@GET
	@Path("/uzmiKategorijeProdavac")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Kategorija> uuuuuu(@Context HttpServletRequest request){
		ArrayList<Kategorija> rac = new ArrayList<>();
		
		Prodavac p=(Prodavac) request.getSession().getAttribute("prodavac");
		
		KategorijaDAO categories = (KategorijaDAO) ctx.getAttribute("kategorijaDAO");
		

		
		for(Kategorija k:categories.getKategorije().values()) {
			rac.add(k);
		}
		
		return rac;
	}
	
	@DELETE
	@Path("/obrisiOglasProdavac/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> obrProda(@PathParam("naziv") String naziv, @Context HttpServletRequest request)
	{
		String[] niz = naziv.split(",");
		String naziv1 = niz[0];
		String opis = niz[1];
		
		Prodavac p = (Prodavac) request.getSession().getAttribute("prodavac");
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		KategorijaDAO kategorije = (KategorijaDAO) ctx.getAttribute("kategorijaDAO");
		PorukaDAO poruke = (PorukaDAO) ctx.getAttribute("porukaDAO");
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO");
		AdministratorDAO administrators = (AdministratorDAO) ctx.getAttribute("administratorDAO");
			
		
		
		ArrayList<Oglas> rac=new ArrayList<>();
		
		for(Oglas o1:oglasi.getOglasi().values()) {
			
			if(o1.getNaziv().equals(naziv1))
			{	if(opis.equals("Novo")){
					oglasi.getOglasi().remove(o1.getNaziv());
					PorukaDAO.getCurrentTimeWithOffset();
					Random ra = new Random();
					Random ra1 = new Random();
					int n = ra.nextInt(200000);
					int n1 = ra1.nextInt(200000);
					for(Administrator a : administrators.getAdministratori().values()){
						Poruka porukaAdminu = new Poruka(n,"Obrisan :"+naziv1,"oglas"," ",PorukaDAO.trenutnoVreme,"seller",p.getKorisnickoIme(),"administrator",a.getKorisnickoIme());
						Poruka porukaSelleru = new Poruka(n1,"Obrisan  "+naziv1,"oglas"," ",PorukaDAO.trenutnoVreme,"seller",p.getKorisnickoIme(),"administrator",a.getKorisnickoIme());
						a.getPoruke().add(porukaAdminu);
						p.getListaPoruka().add(porukaSelleru);
					}
					break;
					
				}
			}
		}
		for(Oglas oo2:p.getListaObjavljenihOglasa()) {
			
			if(oo2.getNaziv().equals(naziv1))
			{	if(opis.equals("Novo")){
					p.getListaObjavljenihOglasa().remove(oo2);
					break;
				}
			}
		}
		
		for(Kategorija kkk : kategorije.getKategorije().values()){
			for(Oglas o : kkk.getListaOglasa()){
				if(o.getNaziv().equals(naziv1)){
					
					kkk.getListaOglasa().remove(o);
					break;
				}
			}
		}
		
		

		for(Oglas r:p.getListaObjavljenihOglasa()) {
			rac.add(r);
		}
		
		prodavci.sacuvajProdavce(prodavci.getPath());
		kategorije.sacuvajKategorije();
		oglasi.sacuvajOglase(oglasi.getPath());
		poruke.sacuvajPoruke(poruke.getPath());
		administrators.sacuvajAdministratore(administrators.getPath());
		return rac;
	}
	
	
	@GET
	@Path("/selektovaniOglasProdavac/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public void dajSel(@PathParam("naziv") String naziv,@Context HttpServletRequest request){
		
		selektovaniOglasProdavac = naziv;
		
	}
	
	
	
	@POST
	@Path("/izmeniOglasProdavac")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cads(Oglas oglas, @Context HttpServletRequest request){
		System.out.println(selektovaniOglasProdavac);
		Prodavac prodavac = (Prodavac) request.getSession().getAttribute("prodavac");
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		KategorijaDAO kategorije = (KategorijaDAO) ctx.getAttribute("kategorijaDAO");
		OglasDAO oglasi = (OglasDAO) ctx.getAttribute("oglasDAO");
		
		if(prodavac==null)
		{
			return Response.status(400).build();
		}
		for(Oglas c : oglasi.getOglasi().values()){
			if(c.getNaziv().equals(oglas.getNaziv()) && !c.getNaziv().equals(selektovaniOglas)){
				return Response.status(400).build();
			}
		}
		System.out.println("Sto ne menjas");
		for(Oglas c : oglasi.getOglasi().values()){
			if(c.getNaziv().equals(selektovaniOglasProdavac)){
				System.out.println("Ulazis oglasi??");
				c.setNaziv(oglas.getNaziv());
				c.setCena(oglas.getCena());
				c.setOpis(oglas.getOpis());
				c.setSlika(oglas.getSlika());
				c.setAktivan(oglas.getAktivan());
				c.setGrad(oglas.getGrad());
				c.setDatumIsticanja(oglas.getDatumIsticanja());
				c.setDatumPostavljanja(oglas.getDatumPostavljanja());
				break;
			}
		}
		
		for(Oglas oo : prodavac.getListaObjavljenihOglasa()){
			if(oo.getNaziv().equals(selektovaniOglasProdavac)){
				System.out.println("Ulazis [prodavac??");
				oo.setNaziv(oglas.getNaziv());
				oo.setCena(oglas.getCena());
				oo.setOpis(oglas.getOpis());
				oo.setSlika(oglas.getSlika());
				oo.setAktivan(oglas.getAktivan());
				oo.setGrad(oglas.getGrad());
				oo.setDatumIsticanja(oglas.getDatumIsticanja());
				oo.setDatumPostavljanja(oglas.getDatumPostavljanja());
				break;
			}
		}
		
		for(Kategorija kkk : kategorije.getKategorije().values()){
			for(Oglas c : kkk.getListaOglasa()){
				if(c.getNaziv().equals(selektovaniOglasProdavac)){
					System.out.println("Ulazis kategorija??");
					c.setNaziv(oglas.getNaziv());
					c.setCena(oglas.getCena());
					c.setOpis(oglas.getOpis());
					c.setSlika(oglas.getSlika());
					c.setAktivan(oglas.getAktivan());
					c.setGrad(oglas.getGrad());
					c.setDatumIsticanja(oglas.getDatumIsticanja());
					c.setDatumPostavljanja(oglas.getDatumPostavljanja());
					break;
					
				}
			}
		}
		
		
		prodavci.sacuvajProdavce(prodavci.getPath());
		oglasi.sacuvajOglase(oglasi.getPath());
		kategorije.sacuvajKategorije();
		return Response.status(200).build();
	}
	
	@GET
	@Path("/uzmiPorukeProdavac")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Poruka> vratimistacuti(@Context HttpServletRequest request){
		ArrayList<Poruka> ret = new ArrayList<>();
		
		Prodavac prodavac = (Prodavac) request.getSession().getAttribute("prodavac");
		
		for(Poruka p : prodavac.getListaPoruka()){
			ret.add(p);
		}
		
		return ret;
	}
	
	
	@DELETE
	@Path("/izbrisiPorukuProdavac/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Poruka> brisiiiiiii (@PathParam("id")int id,@Context HttpServletRequest request){
		ArrayList<Poruka> rac= new ArrayList<>();
		
		Prodavac prodavac = (Prodavac) request.getSession().getAttribute("prodavac");
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		
		for(Poruka p :prodavac.getListaPoruka()){
			if(p.getId() == id){
				prodavac.getListaPoruka().remove(p);
				break;
			}
		}
		for(Poruka p :prodavac.getListaPoruka()){
			rac.add(p);
		}
		
		prodavci.sacuvajProdavce(prodavci.getPath());
		return rac;
		
		
	}
	
	@GET
	@Path("/selektovanaPorukaProdavac/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void ajDAJ(@PathParam("id") int id,@Context HttpServletRequest request){
		
		selektovanaPorukaProdavac = id;
		
	}
	
	@GET
	@Path("/naKojuSeOdgovara/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void trtrtr1111(@PathParam("id") String id,@Context HttpServletRequest request){
		
		String[] niz = id.split(",");
		int id1 = Integer.parseInt(niz[0]);
		
		naKojuPorukuSeOdgovara = id1;
		
		
	}
	
	
	@POST
	@Path("/kreirajPorukuProdavac")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajPAREW(Poruka poruka,@Context HttpServletRequest request){
	
		Prodavac prodavac = (Prodavac) request.getSession().getAttribute("prodavac");
		boolean usao = false;
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		AdministratorDAO admins = (AdministratorDAO) ctx.getAttribute("administratorDAO");
		
		if(prodavac == null){
			return Response.status(400).build();
		}
		if(poruka.getPrimalac().equals("") || poruka.getPrimalac().equals(null)){
			return Response.status(400).build();
		}
		for(Administrator ss : admins.getAdministratori().values()){
			if(ss.getKorisnickoIme().equals(poruka.getPrimalac())){
				usao = true;
			}
		}
		if(usao == false){
			return Response.status(400).build();
		}
		
		Random rand = new Random();
		int n = rand.nextInt(200000);
		Random rand1 = new Random();
		int n1 = rand1.nextInt(200000);
		
		poruka.setId(n);
		PorukaDAO.getCurrentTimeWithOffset();
		poruka.setDatum(PorukaDAO.trenutnoVreme);
		poruka.setUlogaPoslala("prodavac");
		poruka.setPosiljalac(prodavac.getKorisnickoIme());
		poruka.setUlogiPoslato("administrator");
		
		prodavac.getListaPoruka().add(poruka);
		
		poruka.setId(n1);
		
		for(Administrator s : admins.getAdministratori().values()){
			if(s.getKorisnickoIme().equals(poruka.getPrimalac())){
				s.getPoruke().add(poruka);
			}
		}
		
		
		
		
		admins.sacuvajAdministratore(admins.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
		
		return Response.status(200).build();
	}
	
	
	@POST
	@Path("/izmeniPorukuProdavac")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response promjeniGaIstaCessP(Poruka poruka, @Context HttpServletRequest request){
		
		Prodavac prodavac = (Prodavac) request.getSession().getAttribute("prodavac");
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		
		if(prodavac == null){
			return Response.status(400).build();
		}
		
		for(Poruka p : prodavac.getListaPoruka()){
			if(p.getId() == selektovanaPorukaProdavac){
				p.setNazivPoruke(poruka.getNazivPoruke());
				p.setNaslovPoruke(poruka.getNaslovPoruke());
				p.setSadrzajPoruke(poruka.getSadrzajPoruke());
				break;
			}
		}
		
		prodavci.sacuvajProdavce(prodavci.getPath());
		return Response.status(200).build();
	}
	
	
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public void logout(@Context HttpServletRequest request, @Context HttpServletResponse response){
		request.getSession().invalidate();
	}
	
	@GET
	@Path("/naKojuSeOdgovara2/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void comeoooeoeoeoeon(@PathParam("id") String id, @Context HttpServletRequest request){
		
		String[] niz = id.split(",");
		int id1 = Integer.parseInt(niz[0]);
		koJePoslaoPoruku = niz[1];
	}
	
	
	@POST
	@Path("/porukaProdavacZaKupca")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response odg(Poruka poruka,@Context HttpServletRequest request){
	
		Prodavac prodavac = (Prodavac) request.getSession().getAttribute("prodavac");
		ProdavacDAO prodavci = (ProdavacDAO) ctx.getAttribute("prodavacDAO");
		KupacDAO kupci = (KupacDAO) ctx.getAttribute("kupacDAO");
		
		if(prodavac == null){
			Response.status(400).build();
		}

		
		
		
		Random rand = new Random();
		Random rand1 = new Random();
		int n = rand.nextInt(200000);
		int n1 = rand1.nextInt(200000);
		
		poruka.setId(n);
		poruka.setPrimalac(koJePoslaoPoruku);
		PorukaDAO.getCurrentTimeWithOffset();
		poruka.setDatum(PorukaDAO.trenutnoVreme);
		poruka.setUlogaPoslala("seller");
		poruka.setPosiljalac(prodavac.getKorisnickoIme());
		poruka.setUlogiPoslato("kupac");
		
		prodavac.getListaPoruka().add(poruka);
		
		poruka.setId(n1);
		
		for(Kupac k : kupci.getKupci().values()){
			if(k.getKorisnickoIme().equals(poruka.getPrimalac())){
				k.getPoruke().add(poruka);
			}
		}
		
		
		
		
		kupci.sacuvajKupce(kupci.getPath());
		prodavci.sacuvajProdavce(prodavci.getPath());
		
		return Response.status(200).build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
