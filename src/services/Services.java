package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import beans.Administrator;
import beans.Kupac;
import beans.Prodavac;
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
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
