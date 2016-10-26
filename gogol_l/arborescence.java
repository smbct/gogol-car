
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
        fils = new ArrayList<arborescence>();
    }

    /**
     * @brief ajout d'un sommet fils à l'arborescence
     * @param fils le fils à ajouter à l'arborescence
     */
    public void ajouterFils(arborescence fils) {
        this.fils.add(fils);
        fils.pere = this;
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
