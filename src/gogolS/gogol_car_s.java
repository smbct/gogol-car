package gogolS;
import java.util.Hashtable;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.Integer;
import java.util.TreeSet;
import java.io.IOException;

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
	private int parser(String fichier) {

		int res = 1;

		FileReader fr;
        try {
            fr = new FileReader(fichier);

    		BufferedReader reader = new BufferedReader(fr);
    		String tmp, tmp1, tmp2, tmp3;
    		Hashtable<String, TreeSet<listeSuccesseur> > noeuds = new Hashtable<String, TreeSet<listeSuccesseur>> (0); //nomplaces => rue adjacentes

    		tmp = reader.readLine();
            int nbplace = Integer.parseInt(tmp.substring(0,tmp.lastIndexOf(".")));
            tmp = reader.readLine();
            int nbrue = Integer.parseInt(tmp.substring(0,tmp.lastIndexOf(".")));

            //lecteure nom des places
            for(int i = 0; i<nbplace; ++i) {
            	tmp = reader.readLine();
            	noeuds.put(tmp.substring(0,tmp.lastIndexOf(".")), new TreeSet<listeSuccesseur>());
            }

            //lecture des rues
            listeSuccesseur parc = null;
            for(int i = 0; i<nbrue; ++i) {
            	tmp = reader.readLine();
            	tmp1 = tmp.substring(0, tmp.indexOf(";")-1); //nom rue
            	tmp2 = tmp.substring(tmp.indexOf(";")+1,tmp.lastIndexOf(";")-1 ); //nom extremite 1
            	tmp3 = tmp.substring(tmp.lastIndexOf(";")+1, tmp.lastIndexOf(".")); //nom extremite 2
                parc = new listeSuccesseur(tmp1 , tmp2 , tmp3);
                for(listeSuccesseur j : noeuds.get(tmp2)) {
                    parc.ajoutSucc(j);
                }
                for(listeSuccesseur j : noeuds.get(tmp3)) {
                    parc.ajoutSucc(j);
                }
                noeuds.get(tmp2).add(parc);
                noeuds.get(tmp3).add(parc);
            }
            this.racine = parc;
        } catch(IOException ex) {
            System.out.println("erreur a l'ouverture du ficher");
			res = 0;
        }

		return res;
	}

    @Override
    /**
    * ecrit tout le graphe de la ville
    */
    public String toString() {
        return racine.toString();
    }

    /**
    * effectue un parcours en profondeur du graphe pour obtenir le parcour de la gogol car
    *
    * @return la chaine de caractère dans laquelle est ecrit le chemin a suivre
    */
    public String parcours() {
        return "depart de " + encommun(racine, racine.getSucc().first()) + " : \n" + racine.parcours();
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
	 * @param fichier le nom du fichier contenant la ville
     */
    public void calculItineraire(String fichier) {
<<<<<<< HEAD

		int resLecture = parser(fichier);
		if(resLecture == 1) {
			System.out.println("Graphe de la ville : ");
			System.out.println(this);

			System.out.print("\nItineraire à suivre :\n");
			System.out.println(this.parcours());
		}

=======
        System.out.println("Graphe de la ville : ");
        System.out.println(this);

        System.out.print("\nItineraire à suivre :\n");
        String res = this.parcours();
        System.out.println(res);
        try{
            PrintWriter writer = new PrintWriter("itineraire.txt", "UTF-8");
            writer.println(res);
            writer.close();
        } catch (Exception e) {
            System.out.println("Erreur pendant l'ecriture de l'itineraire dans un fichier");
        }
>>>>>>> 343b122c671159097a117f3b6d7658927686987e
    }
}
