package gogol_s;
import java.util.Hashtable;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.Integer;
import java.util.LinkedList;
import java.io.IOException;
import java.io.PrintWriter;

/**
* classe qui implement l'algorithme gogolS
*/
public class gogol_car_s {

	private listeSuccesseur racine; //noeud qui nous sert de point d'entre dans le graphe

    /**
    * constructeur par défaut
    */
	public gogol_car_s() {

    }

	/**
	 * lecture et chargement en mémoire du graphe à traiter
	 * @param fichier le nom du fichier
	 * @return 1 ssi la lecture s'est bien déroulée
	 */
	private boolean parser(String fichier) {

		boolean res = true;

		FileReader fr;
        try {
            //objets de lecture
            fr = new FileReader(fichier);

    		BufferedReader reader = new BufferedReader(fr);
    		String tmp, tmp1, tmp2, tmp3;
            //pour les arretes : pour trouver l'ensemble des arettes ayant comme extremité chaque place
    		Hashtable<String, LinkedList<listeSuccesseur> > noeuds = new Hashtable<String, LinkedList<listeSuccesseur>> (0); //nomplaces => rue adjacentes

    		tmp = reader.readLine(); //on lit le nombre de place
            int nbplace = Integer.parseInt(tmp.substring(0,tmp.lastIndexOf(".")));
            tmp = reader.readLine(); //on lit le noombre de rue
            int nbrue = Integer.parseInt(tmp.substring(0,tmp.lastIndexOf(".")));

            //lecture du nom des places
            for(int i = 0; i<nbplace; ++i) {
            	tmp = reader.readLine();
                //chaque place est une case de la table de hashage, pour l'instant aucune rue n'y est associe
            	noeuds.put(tmp.substring(0,tmp.lastIndexOf(".")), new LinkedList<listeSuccesseur>());
            }

            //lecture des rues
            listeSuccesseur parc = null;
            for(int i = 0; i<nbrue; ++i) {
            	tmp = reader.readLine();
            	tmp1 = tmp.substring(0, tmp.indexOf(";")-1); //nom rue
            	tmp2 = tmp.substring(tmp.indexOf(";")+1,tmp.lastIndexOf(";")-1 ); //nom extremite 1
            	tmp3 = tmp.substring(tmp.lastIndexOf(";")+1, tmp.lastIndexOf(".")); //nom extremite 2
                parc = new listeSuccesseur(tmp1 , tmp2 , tmp3);
                //on ajoute comme successeur toutes les aretres qui ont une place en commun avec celle ci
                for(listeSuccesseur j : noeuds.get(tmp2)) {
                    parc.ajoutSucc(j);
                    j.ajoutSucc(parc);
                }
                for(listeSuccesseur j : noeuds.get(tmp3)) {
                    parc.ajoutSucc(j);
                    j.ajoutSucc(parc);
                }
                //on ajoute cette rue au esemble de rue ayant les meme extremites
                noeuds.get(tmp2).addFirst(parc);
                noeuds.get(tmp3).addFirst(parc);
            }
            //on prend arbitrairement comme racine le dernier noeud ajoute
            this.racine = parc;
        } catch(IOException ex) {
            System.out.println("erreur a l'ouverture du ficher");
			res = false;
        }

		return res; //vrai si tout c'est bien passe, faux sinon
	}

    @Override
    /**
    * ecrit tout le graphe de la ville
    */
    public String toString() {
        return racine.toString();
    }

    /**
    * effectue un parcours recursif en profondeur du graphe pour obtenir le parcours de la gogol car
    *
    * @return la chaine de caractère dans laquelle est ecrit le chemin a suivre
    */
    public String parcours() {
        //ondetermine la place de depart (choix entre les deux etremites de la rue racine), puis on affiche le parcours
        return "depart de " + encommun(racine, racine.getSucc().getFirst()) + " : \n" + racine.parcours();
    }

    /**
    * determine la place de laquelle on part quand on parcours ces deux rues dans l'ordre
    *
    * @param depart la premiere rue que l'on parcours
    * @param arrive la deuxieme rue quel'on parcours
    * @return le nom de la place de laquelle on part pour parcourir depart puis arrive
    */
    private String encommun(listeSuccesseur depart, listeSuccesseur arrive) {
        if ((depart.getdeb().equals(arrive.getdeb())) || (depart.getdeb().equals(arrive.getfin()))) {
            return depart.getfin();
        } else {
            return depart.getdeb();
        }
    }

    /**
     * fonction de calcul de l'itinéraire pour gogol_s
     *
	 * @param fichier le nom du fichier contenant la ville
     */
    public void calculItineraire(String fichier) {

		if(parser(fichier)) {

			System.out.println("Graphe de la ville : ");
			System.out.println(this);

			System.out.print("\nItineraire à suivre :\n");
			String res = this.parcours();
			//output des resultats
            System.out.println(res);
			try{
				PrintWriter writer = new PrintWriter("itineraire.txt", "UTF-8");
				writer.println(res);
				writer.close();
			} catch (Exception e) {
				System.out.println("Erreur pendant l'ecriture de l'itineraire dans un fichier");
			}
		}


    }
}
