package gogol_l;

import java.util.ArrayList;
import java.lang.String;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.*;

import java.util.Hashtable;

import java.io.PrintWriter;

/**
 * @class gogol_car_l
 */
public class gogol_car_l {

    private ArrayList<String> places; // liste des places de la ville
    private Hashtable<String, Integer> numeroPlaces; // liste associant les numéros dex places à leur noms
    private int nbPlace; // nombre de place de la ville
    private int nbRue; // nombre de rue de la ville

    private ArrayList<successeur> listeSuccesseurs; // liste des successeurs du graphe
    private ArrayList<Integer> degre; // degré sortant de chaque sommet

    /**
    * retourne le degre du sommet corespondant a la ligne i
    */
    public Integer getDegre(int i) { return this.degre.get(i);}

    /**
    * retourne le nombre de place de la ville
    */
    public int getNbPlace() { return this.nbPlace;}

    /**
    * donne le nom de la place i
    */
    public String getNomPlace(int i) {return places.get(i);}

    /**
     * chargement en mémoire de la ville
     * @param nom le nom du fichier contenant la ville
     * @return vrai ssi la lecture s'est bien déroulée
     */
    public boolean parser(String nom) {

        FileReader fr;
        try {
            // création des objtes permettant de lire le fichier contenant la ville
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

            // création de la liste des degrés sortant mémorisant le degré sortant de chaque sommet
            degre = new ArrayList<Integer>(nbPlace);
            for(int i = 0; i < nbPlace; i++) {
                // pour l'instant, les degrés sont initialisés à 0
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
                // si la liste des sucesseurs était vide, le premier élément est initialisé
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

                // mise à jour des degrés sortants
                degre.set(num1, degre.get(num1)+1);
                degre.set(num2, degre.get(num2)+1);
            }
            return true; //tout s'est bien passe
    	} catch(IOException ex) {
			System.out.println("Erreur à l'ouverture du ficher");
			fr = null;
            return false; //erreur
		}


    }

    /**
     * constructeur par défaut
     */
    public gogol_car_l() {

        // création de la liste associative entre les numéros des places et leur noms
        numeroPlaces = new Hashtable<String, Integer>();

        // création de la liste des places
        places = new ArrayList<String>();

    }

    /**
     * retourne l'information du graphe sous forme de chaîne de caractère
     * @return la chaîne de caractère
     */
    public String toString() {

        String str = "";

        // pour chaque place, affichage des rues et des places vers lesquelles elles conduisent
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
    * utilisé par gogol_xl, sert à doubler des aretes dans le graphe
    * @param deb la premiere extremite de l'arete
    * @param fin la seconde extremite de l'arete
    * @pre cette arete existe deja
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
        listeSuccesseurs.get(deb).ajouter_rue(new successeur(fin, places.get(fin), nomRue)); //dans la liste de successeur de deb
        listeSuccesseurs.get(fin).ajouter_rue(new successeur(deb, places.get(deb), nomRue)); //dans la liste de successeur de fin

        degre.set(deb, degre.get(deb)+1);
        degre.set(fin, degre.get(fin)+1);
    }

    /**
     * création de l'arborescence recouvrante de la ville
     * @pre le graphe est connexe
     */
    public arborescence creer_arborescence() {

        // création d'une liste indiquant quels sommets ont été visités durant le parcours
        ArrayList<Boolean> visite = new ArrayList<Boolean>(nbPlace);
        for(int numPlace = 1; numPlace <= nbPlace; numPlace ++) {
            visite.add(Boolean.FALSE);
        }

        // création de l'arborescence de manière récursive
        arborescence res = creer_arborescence_rec(0, visite);

        return res;
    }

    /**
     * création récursive d'une arborescence recouvrante du graphe par parcours en profondeur
     * @param sommet le sommet à visiter
     * @param visite la liste des sommets qui ont été visités
     * @return la racine de l'arborescence créee à partir du sommet donné en paramètre
     */
    private arborescence creer_arborescence_rec(int sommet, ArrayList<Boolean> visite) {

        // création d'un nouveau sommet qui sera ajouté à l'arborescence
        arborescence arbo = new arborescence(sommet);

        // marquer ce sommet comme étant visité
        visite.set(sommet, Boolean.TRUE);

        // parcours de tous les successeurs du sommet et ajout des successeurs non visités à l'arborescence
        successeur succ = listeSuccesseurs.get(sommet);
        while(succ != null) {

            // si le successeur n'a pas été visité, il est ajouté à l'arborescence
            if(!visite.get(succ.get_sommet())) {
                // le successeur est ajouté par appel récursif de la fonction
                arbo.ajouterFils(creer_arborescence_rec(succ.get_sommet(), visite));
            }
            // avancement vers le prochain successeur dans la liste des successeurs
            succ = succ.suivant();
        }

        return arbo;
    }

    /**
     * Numérotation des sommets du graphe à partir de l'anti-arborescence de manière récurisive
     * @param arbo l'aborescence utilisée pour numéroter
     */
    public void numeroter_rec(arborescence arbo) {

        // récupération des indices des sommets : le sommet courant et sont successeur dans l'anti-arborescence (c'est à dire son prédecesseur dans l'arborescence)
        int sommet = arbo.get_sommet();
        int sommetPere = -1;
        // le successeur correspond ainsi au père dans l'arborescence créée
        // si le sommet est la racine de l'arborescence, alors l'indice du père est laissé à -1
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
            // on s'assure à chaque fois de numéroter les arcs sortants correspondant aux arcs entrants de l'anti-racine de telle sorte qu'ils ne soient pas les plus petits

            if(arbo.est_fils(suc.get_sommet())) { // numérotation la plus grande possible
                suc.numeroter(numFils);
                numFils --;
            }
            else if(suc.get_sommet() == sommetPere || sommetPere == -1) { // numérotation avec le plus grand numéro
                suc.numeroter(degre.get(sommet));
                // s'il n'y a pas de sommet père, le premier sommet est le plus grand
                if(sommetPere == -1) {
                    sommetPere = suc.get_sommet(); // ce numéro ne sera pas réutilisé
                }
            } else { // numérotation quelconque
                suc.numeroter(numero);
                numero ++;
            }
            suc = suc.suivant(); // passage au successeur suivant
        }

        // numérotation récursive des autres sommets, correspondants aux fils dans l'arborescence
        for(arborescence fils : arbo.get_fils()) {
            numeroter_rec(fils);
        }
    }

    /**
     * Calcule le cycle que doit effectuer la voiture de gogol
     * @param racine la racine de l'arborescence
     */
    public void cycle_gogol(int racine) {

        boolean continuer = Boolean.TRUE;

        // création d'une liste qui contiendra le parcours à réaliser par le véhicule
        ArrayList<Integer> chemin = new ArrayList<Integer>();

        // le point de départ est ajouté à la liste
        chemin.add(racine);

        // sommet courant du graphe
        int sommetCourant = racine;

        while(continuer) {

            // recherche du prochain sommet à ajouter dans le chemin : celui qui a le plus petit numéro
            successeur succMin = null;
            successeur succ = listeSuccesseurs.get(sommetCourant);

            // parcours de tous les successeurs du sommet et détermination de celui qui a le plus petit numéro
            while(succ != null) {

                if(!succ.est_utilise() && (succMin == null || succ.get_numero() < succMin.get_numero())) {
                    succMin = succ;
                }
                // passage au successeur suivant dans le chaînage
                succ = succ.suivant();
            }

            // si un sommet a été trouvé, il est ajouté et le chemin continue à partir de celui-ci
            if(succMin != null) {

                // marquage des deux arcs
                succMin.marquer();
                // seule opération couteuse : en O du degré sortant du sommet (parcours de la liste d'adjacence)
                listeSuccesseurs.get(succMin.get_sommet()).marquer_rue(sommetCourant);

                // ajout du sommet au chemin
                chemin.add(succMin.get_sommet());

                // passage au sommet suivant
                sommetCourant = succMin.get_sommet();
            } else { // sinon, le cycle est terminé, l'algorithme s'arrête
                continuer = false;
            }

        }


        //ecriture des resultats
        try{
            PrintWriter writer = new PrintWriter("itineraire.txt", "UTF-8");
            writer.print("Itineraire à suivre : ");
            System.out.print("Itineraire à suivre : ");
            for(int sommet : chemin) {
                writer.print(places.get(sommet) + ", ");
                System.out.print(places.get(sommet) + ", ");
            }
            writer.println("");
            System.out.println("");
            writer.close();
        } catch (Exception e) {
            System.out.println("Erreur à l'ecriture du parcours dans un fichier");
            System.out.print("Itineraire à suivre : ");
            for(int sommet : chemin) {
                System.out.print(places.get(sommet) + ", ");
            }
            System.out.println("");
        }

    }

    /**
    * transforme la representation en liste d'adjacence en une matrice d'adjacence
    * @param matriceAdjacence la matrice qui contient les distances entre les sommets
    * @param succecesseurs indique les sommet à parcourir pour aller de sommet en sommet
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

        //ajout de arêtes
        successeur parcrec;
        for(int parc = 0; parc<nbPlace; ++parc) {
            parcrec = listeSuccesseurs.get(parc);
            while(parcrec != null) {
                matriceAdjacence[parc][parcrec.get_sommet()] = 1; //il y a une arete
                succecesseurs[parc][parcrec.get_sommet()] = parc;
                parcrec = parcrec.suivant();
            }
        }
    }

    /**
    * calcule si le graphe contient un cycle eulerien
    */
    public boolean estEuler() {
        boolean res = true;
        int parc = 0;
        while (res && (parc<this.getNbPlace())) {
            // le graphe est eulerien ssi le degré de tous les sommets est pair
            res = res && (this.getDegre(parc) % 2 == 0);
            parc++;
        }
        return res;
    }

    /**
     * fonction qui gere toute la gogol_l
     */
    public void calculItineraire(String fichier) {

        // si le fichier a pu être lu, le calcul peut commencer
        if (this.parser(fichier)) {

            System.out.println("Graphe de la ville :");
            System.out.println(this);

            // vérification que le graphe est eulerien ; si ce n'est pas le cas, l'itinéraire ne peut pas être calculé
            if (this.estEuler()) {

                // création de l'arborescence utilisée pour la numérotation
                arborescence arbo = this.creer_arborescence();

                // numérotation des arcs
                this.numeroter_rec(arbo);

                // calcul du cycle eulerien donnant le trajet du vehicule
                this.cycle_gogol(arbo.get_sommet());
            } else {
                System.out.println("Impossible d'utiliser gogol_l car le graphe ne contient pas de circuit eulerien");
            }
        }
    }

}
