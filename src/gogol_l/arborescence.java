package gogol_l;

import java.util.ArrayList;

/**
 * @class arborescence
 * @brief représente une arborescence
 */
public class arborescence {

    private arborescence pere;
    private ArrayList<arborescence> fils;
    int sommet; // numéro du sommet du graphe

    public arborescence(int sommet) {
        this.sommet = sommet;
        this.pere = null;
        fils = new ArrayList<arborescence>();
    }

    /**
     * ajout d'un sommet fils à l'arborescence
     * @param fils le fils à ajouter à l'arborescence
     */
    public void ajouterFils(arborescence fils) {
        this.fils.add(fils);
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
     * @brief retourne l'information de l'arborescence sous forme d'une chaîne de caractère
     * @return la chaîne de caractère
     */
    public String toString() {

        String res = "";

        res += sommet + " : ";

        for(int i = 0; i < fils.size(); i++) {
            res += fils.get(i).sommet + " ";
        }
        res += "\n";

        for(int i = 0; i < fils.size(); i++) {
            res += fils.get(i);
        }

        return res;

    }

}
