package gogol_l;

import java.util.ArrayList;
import java.lang.String;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.*;

import java.util.Hashtable;

/**
 * @class gogol_car_l
 */
public class gogol_car_l {

    private ArrayList<String> places;
    // dictionnaire association chaque ville à son numéro de sommet dans le graphe
    private Hashtable<String, Integer> numeroPlaces;
    private int nbPlace;
    private int nbRue;

    private ArrayList<successeur> listeSuccesseurs;
    private ArrayList<Integer> degre; // degré sortant de chaque sommet

    /**
    * retourne le degre du sommet corespondant a la ligne i
    */
    public Integer getDegre(int i) { return this.degre.get(i);}

    public int getNbPlace() { return this.nbPlace;}

    public void parser(String nom) {

        FileReader fr;
        try {
        	fr = new FileReader(nom);
            BufferedReader reader = new BufferedReader(fr);

            String ligne;

            // lecture du nombre de place
            ligne = reader.readLine();
            nbPlace = Integer.parseInt(ligne.substring(0, ligne.length()-1));

            // création de la liste des successeurs
            listeSuccesseurs = new ArrayList<successeur>(nbPlace);
            for(int i = 0; i < nbPlace; i ++) {
                listeSuccesseurs.add(null);
            }
            // création de la liste des degrés sortant
            degre = new ArrayList<Integer>(nbPlace);
            for(int i = 0; i < nbPlace; i++) {
                degre.add(0);
            }

            // lecture du nombre de rue
            ligne = reader.readLine();
            nbRue = Integer.parseInt(ligne.substring(0, ligne.length()-1));

            // lecture du nom des places
            for(int indPlace = 1; indPlace <= nbPlace; indPlace ++) {
                ligne = reader.readLine();
                places.add(ligne.substring(0, ligne.length()-1));
                numeroPlaces.put(places.get(places.size()-1), new Integer(places.size()-1));
            }


            // lecture du nom des rues et des places adjacentes
            for(int indRue = 1; indRue <= nbRue; indRue ++) {
                ligne = reader.readLine();
                String[] noms = ligne.split(";");

                int num1 = numeroPlaces.get(noms[1].substring(0, noms[1].length()-1));
                int num2 = numeroPlaces.get(noms[2].substring(0, noms[2].length()-1));

                String nomRue = noms[0].substring(0, noms[0].length()-1);

                // création de la liste d'adjacence
                successeur suc = new successeur(num2, places.get(num2), nomRue);
                if(listeSuccesseurs.get(num1) == null) {
                    listeSuccesseurs.set(num1, suc);
                } else {
                    listeSuccesseurs.get(num1).ajouter_rue(suc);
                }
                suc = new successeur(num1, places.get(num1), nomRue);
                if(listeSuccesseurs.get(num2) == null) {
                    listeSuccesseurs.set(num2, suc);
                } else {
                    listeSuccesseurs.get(num2).ajouter_rue(suc);
                }

                // mise à jour des degrés sortant
                degre.set(num1, degre.get(num1)+1);
                degre.set(num2, degre.get(num2)+1);
            }

    	} catch(IOException ex) {
			System.out.println("Erreur à l'ouverture du ficher");
			fr = null;
		}


    }

    /**
     * constructeur par défaut
     */
    public gogol_car_l() {

        numeroPlaces = new Hashtable<String, Integer>();

        // création de la liste des places
        places = new ArrayList<String>();

    }

    public String toString() {

        String str = "";

        for(int i = 0; i < nbPlace; i++) {
            str += places.get(i) + "  : ";
            if(listeSuccesseurs.get(i) != null) {
                str += listeSuccesseurs.get(i);
            }
            str += "\n";
        }

        return str;
    }

    /**
    * pour gogol_xl, sert à doubler des arretes
    * @param deb la premiere extremite de l'arrete
    * @param fin la seconde extremite de l'arrete
    * @pre cette arrete existe deja
    */
    public void ajout_arrete (int deb, int fin) {
        successeur succ = listeSuccesseurs.get(deb);
        String nomRue = new String();

        //recherche du nom de la rue
        while(succ != null) {
            if (succ.get_sommet() == fin) {
                nomRue = succ.get_nomRue();
                succ = null;
            } else {
                succ = succ.suivant();
            }
        }

        //ajout dans les deux sens
        System.out.println("deb et fin ("+deb+" ; "+fin+") : "+places.get(deb)+" "+places.get(fin));
        listeSuccesseurs.get(deb).ajouter_rue(new successeur(fin, places.get(fin), nomRue)); //dans la liste de successeur de deb
        listeSuccesseurs.get(fin).ajouter_rue(new successeur(deb, places.get(deb), nomRue)); //dans la liste de successeur de fin

        degre.set(deb, degre.get(deb)+1);
        degre.set(fin, degre.get(fin)+1);
    }

    /**
     * création de l'arborescence de la ville
     * @pre le graphe est connexe
     */
    public arborescence creer_arborescence() {

        ArrayList<Boolean> visite = new ArrayList<Boolean>(nbPlace);
        for(int numPlace = 1; numPlace <= nbPlace; numPlace ++) {
            visite.add(Boolean.FALSE);
        }

        arborescence res = creer_arborescence_rec(0, visite);

        return res;
    }

    /**
     * création récursivement d'un arbre recouvrant du graphe par parcours en profondeur
     * @param sommet le sommet à visiter
     * @param visite la liste des sommets qui ont été visités
     */
    private arborescence creer_arborescence_rec(int sommet, ArrayList<Boolean> visite) {

        arborescence arbo = new arborescence(sommet);

        // marquer ce sommet comme étant visité
        visite.set(sommet, Boolean.TRUE);

        // parcours de tous les successeurs et ajout à l'arborescence des non visités
        successeur succ = listeSuccesseurs.get(sommet);
        while(succ != null) {

            // si le successeur n'a pas été visité, il est ajouté à l'arborescence
            if(!visite.get(succ.get_sommet())) {
                arbo.ajouterFils(creer_arborescence_rec(succ.get_sommet(), visite));
            }
            succ = succ.suivant();
        }

        return arbo;
    }

    /**
     * Numérotation des sommets du graphe à partir de l'anti-arborescence de manière récurisive
     * @param arbo l'aborescence utilisée pour numéroter
     */
    public void numeroter_rec(arborescence arbo) {

        // récupération des indices des sommets : le sommet courant et sont successeur dans l'anti-arborescence
        int sommet = arbo.get_sommet();
        int sommetPere = -1;
        // le successeur correspond au père dans l'arborescence créée
        if(arbo.get_pere() != null) {
            sommetPere = arbo.get_pere().get_sommet();
        }

        // numérotation des arcs sortants
        successeur suc = listeSuccesseurs.get(sommet);
        int numero = 1;
        int numFils = degre.get(sommet)-1;
        while(suc != null) {

            // si l'arc qui est dans l'anti-arborescence est trouvée, il prend le plus grand numéro
            // si c'est l'anti-racine, le numéro le plus grand est attribué de manière quelconque
            // on s'assure à chaque fois de numéroter les arcs sortants correspondant aux arcs entrant de l'anti-racine de telle sorte qu'ils ne soient pas les plus petits

            if(arbo.est_fils(suc.get_sommet())) { // numérotation la plus grande possible
                suc.numeroter(numFils);
                numFils --;
            }
            else if(suc.get_sommet() == sommetPere || sommetPere == -1) {
                suc.numeroter(degre.get(sommet));
                // s'il n'y a pas de sommet père, le premier sommet est le plus grand
                if(sommetPere == -1) {
                    sommetPere = suc.get_sommet(); // ce numéro ne sera pas réutilisé
                }
            } else { // numérotation quelconque
                suc.numeroter(numero);
                numero ++;
            }
            suc = suc.suivant();
        }

        // numérotation récursive des autres sommets
        for(arborescence fils : arbo.get_fils()) {
            numeroter_rec(fils);
        }
    }

    /**
     * Calcule le cycle que doit effectuer la voiture de gogol
     * @param numeros la numérotation du graphe
     * @param racine la racine de l'arborescence
     */
    public void cycle_gogol(int racine) {

        boolean continuer = Boolean.TRUE;

        ArrayList<Integer> chemin = new ArrayList<Integer>();

        chemin.add(racine);

        // sommet courant du graphe
        int sommetCourant = racine;

        while(continuer) {

            // recherche du prochain sommet
            successeur succMin = null;
            successeur succ = listeSuccesseurs.get(sommetCourant);
            while(succ != null) {

                if(!succ.est_utilise() && (succMin == null || succ.get_numero() < succMin.get_numero())) {
                    succMin = succ;
                }
                succ = succ.suivant();
            }

            // si un sommet a été trouvé, la recherche continue
            if(succMin != null) {

                // marquage des deux arcs
                succMin.marquer();
                // seule opération couteuse : en O du degré sortant du sommet (parcours de la liste d'adjacence)
                listeSuccesseurs.get(succMin.get_sommet()).marquer_rue(sommetCourant);

                chemin.add(succMin.get_sommet());
                sommetCourant = succMin.get_sommet();
            } else {
                continuer = false;
            }

        }

        for(int sommet : chemin) {
            System.out.print(places.get(sommet) + ", ");
        }
        System.out.println("");

    }

    /**
    * transforme la representation en liste d'adjacence en une matrice d'adjacence
    * @param matriceAdjacence la matrice qui contien les distances entre les sommets
    * @param succecesseurs indique les sommet a parcourir pour aller de sommet en sommet
    **/
    public void transformeMatrice(int matriceAdjacence [][], int succecesseurs[][]) {
        //initialisation
        for(int i = 0; i<nbPlace; i++) {
            for(int j = 0; j<nbPlace; j++) {
                matriceAdjacence[i][j] = Integer.MAX_VALUE; //par defaut aucune arrete
                succecesseurs[i][j] = -1;
            }
            matriceAdjacence[i][i] = 0; //un sommet n'est pas distant de lui meme
        }

        //ajout de arretes
        successeur parcrec;
        for(int parc = 0; parc<nbPlace; ++parc) {
            parcrec = listeSuccesseurs.get(parc);
            while(parcrec != null) {
                matriceAdjacence[parc][parcrec.get_sommet()] = 1; //il y a une arrete
                succecesseurs[parc][parcrec.get_sommet()] = parc;
                parcrec = parcrec.suivant();
            }
        }
    }

    /**
    * calcule si le graphe contien un cycle eulerien
    */
    public boolean estEuler() {
        boolean res = true;
        int parc = 0
        while (res && (parc<this.getNbPlace())) {
            res = res && (this.getDegre(parc) % 2 != 0)
            parc++;
        }
        return res;
    }

    /**
     * fonction de test de la classe
     */
    public static void main(String[] args) {

        gogol_car_l car = new gogol_car_l();
        car.parser(/*"../instances/euler_city.txt"*/"../instances/test.txt");

        System.out.println(car);

        arborescence arbo = car.creer_arborescence();

        car.numeroter_rec(arbo);

        car.cycle_gogol(arbo.get_sommet());

    }

}
