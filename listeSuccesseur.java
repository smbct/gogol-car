import java.util.ArrayList;
import java.lang.String;

public class listeSuccesseur {
	private String nom; //nom de ce sommet
	private ArrayList<listeSuccesseur> succ; //liste des noeud successeur
	private ArrayList<String> nomrue; //nom de l'arc qui le relie au successeur au meme indice

	private boolean visite; //pour le parcour : note si le noeud a deja ete visite

	public listeSuccesseur(String nom) {
		this.nom = nom;
		this.visite = false;
	}

	public void ajoutSucc(listeSuccesseur nouvsucc, String nouvrue) {
		this.succ.add(nouvsucc);
		this.nomrue.add(nouvrue);
	}

	public String getNom() { return this.nom;}

	public listeSuccesseur getSucc(int i) {return succ.get(i);}

	public String getRue(int i) {return nomrue.get(i);}

	public void marque() {this.visite = true;}

	public boolean dejavisite() {return this.visite;}

    public static void main(String[] argc) {
        System.out.println("hello graphe");
    }

}
