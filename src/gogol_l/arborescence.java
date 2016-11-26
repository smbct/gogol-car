package gogol_l;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @class arborescence
 * représente une arborescence du graphe de la ville
 */
public class arborescence {

    private arborescence pere; // le sommet père de l'arborescence (correspondant au sommet fils de l'anti-arborescence)
    private ArrayList<arborescence> fils; // les sommets fils de l'arborescence
    int sommet; // numéro du sommet du graphe

    /**
     * constructeur, création d'une arborescence contenant un unique sommet à partir d'un sommet racine
     * @param sommet l'indice du sommet racine
     */
    public arborescence(int sommet) {
        this.sommet = sommet;
        this.pere = null;
        fils = new ArrayList<arborescence>();
    }

    /**
     * ajout de la racine d'une arborescence en fils de cette arborescence
     * @param fils la racine à ajouter comme fils
     */
    public void ajouterFils(arborescence fils) {
        this.fils.add(fils);
        // il faut mettre à jour le père de l'arborescence ajoutée
        fils.pere = this;
    }

    /**
     * getter du pere du noeud
     * @return le pere, peut être null
     */
    public arborescence get_pere() {
        return pere;
    }

    /**
     * retourne la liste des fils
     * @return la liste des fils
     */
    public ArrayList<arborescence> get_fils() {
        return fils;
    }

    /**
     * retourne le numéro du sommet
     * @return le numéro du sommet
     */
    public int get_sommet() {
        return sommet;
    }

    /**
     * retourne vrai ssi sommet est un fils de l'arborescence
     * @param sommet le numéro du sommet à tester
     * @return vrai ssi sommet est un fils de l'arborescence
     */
    public boolean est_fils(int sommet) {
        boolean res = Boolean.FALSE;

        Iterator<arborescence> it = fils.iterator();

        // parcours de tous les fils de l'arborescence
        arborescence suiv;
        while(!res && it.hasNext()) {
            suiv = it.next();
            // si le sommet est trouvé parmis les fils, la fonction renvoie vrai
            if(suiv.get_sommet() == sommet) {
                res = Boolean.TRUE;
            }
        }

        return res;
    }

    /**
     * retourne le nombre de fils
     * @return le nombre de fils
     */
    public int nb_fils() {
        return fils.size();
    }

    /**
     * retourne l'information de l'arborescence sous forme d'une chaîne de caractère
     * @return la chaîne de caractère
     */
    public String toString() {

        String res = "";

        // ajout du numéro du sommet
        res += sommet + " : ";

        // ajout des numéros des fils
        for(int i = 0; i < fils.size(); i++) {
            res += fils.get(i).sommet + " ";
        }
        res += "\n";

        // ajout récursivement des chaînes de caractère des fils
        for(int i = 0; i < fils.size(); i++) {
            res += fils.get(i);
        }

        return res;

    }

}
