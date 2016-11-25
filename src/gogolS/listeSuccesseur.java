package gogolS;
import java.util.*;
import java.lang.*;

/**
* contien le graphe de la ville avec les rues comme sommet et les places comme arrete
*/
public class listeSuccesseur implements Comparable, Comparator{
	private String nom; //nom de cette rue
	private String deb; //nom de la place a une extremite
	private String fin; //nom de la palce a l'autre extremite
	private TreeSet<listeSuccesseur> succ; //ensemble des noeuds successeur

	private boolean visite; //pour le parcour : note si le noeud a deja ete visite

	/**
	* construit le sommet corespondant a une rue entre deux places
	* 
	* @param nom le nom de la rue
	* @param deb une extremite de la rue
	* @param fin l'autre extremite de la rue
	*/
	public listeSuccesseur(String nom, String deb, String fin) {
		this.nom = nom;
		this.deb = deb;
		this.fin = fin;
		succ = new TreeSet<listeSuccesseur>();
		this.visite = false;
	}

	/**
	* ajoute cette rue comme sucesseur de celle ci
	* <p>
	* deux rue sont considere comme adjacente si elles ont une extremite en commun
	*
	* @param nouvsucc la rue a ajouter dans l'ensemble des successeur de cette rue
	*/
	public void ajoutSucc(listeSuccesseur nouvsucc) {
		this.succ.add(nouvsucc);
	}

	/**
	* renvoi l'ensemble des successeur de cette rue
	* 
	* @return l'ensemble des rues adjacentes a celle ci
	*/
	public TreeSet<listeSuccesseur> getSucc() {return this.succ;}

	/**
	* renvoi la premiere extremite de la rue
	*
	* @return la string contenant le nom de la premiere place sur laquelle debouche cette rue
	*/
	public String getdeb() {return this.deb;}

	/**
	* renvoi la deuxieme extremite de la rue
	*
	* @return la string contenant le nom de la deuxieme place sur laquelle debouche cette rue
	*/
	public String getfin() {return this.fin;}

	@Override
	public int compareTo(Object lautre) {
		listeSuccesseur lautreCast = (listeSuccesseur)lautre;
		return this.nom.compareTo(lautreCast.nom);
	}

	@Override
	public int compare(Object o1, Object o2) {
		listeSuccesseur o1Cast = (listeSuccesseur)o1;
		listeSuccesseur o2Cast = (listeSuccesseur)o2;
		return compare(o1Cast.nom, o2Cast.nom);
	}

	@Override
	public boolean equals(Object lautre) {
		listeSuccesseur lautreCast = (listeSuccesseur)lautre;
		return this.nom.equals(lautreCast.nom);
	}

	@Override
	public String toString() {
		TreeSet<listeSuccesseur> dejaParcouru = new TreeSet<listeSuccesseur>();
		return this.toStringrec(dejaParcouru);
	}

	/**
	* parcour recursivement le graph pour l'afficher
	*
	* @param dejaParcouru l'ensemble des sommet que l'on a deja visite
	* @rtuen la string contenant toute les information de l'arbre
	*/
	private String toStringrec(TreeSet<listeSuccesseur> dejaParcouru){
		String res = this.nom + "("+this.deb+" , "+this.fin+")";
		dejaParcouru.add(this);
		for(listeSuccesseur parc : this.succ) {
			if (! dejaParcouru.contains(parc)) {
				res = res + " ; " + parc.toStringrec(dejaParcouru);
			}
		}
		return res;
	}

	/**
	* effectue un pours en profondeur de l'arbre pour determiner le chemin de la gogol car
	* 
	* @return la string decrivant le chemin a suivre
	*/
	public String parcours() {
		String res = this.nom;
		this.visite = true;
		for( listeSuccesseur parc : this.succ) {
			if(! parc.visite) {
				res = res + "\n" + parc.parcours();
			}
		}
		res = res + "\n" + this.nom;
		return res;
	}

    public static void main(String[] argc) {
        System.out.println("hello graphe");
    }

}
