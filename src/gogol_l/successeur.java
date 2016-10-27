package gogol_l;

import java.lang.String;

public class successeur {

    int sommet; // numéro de la place
    int numero; // numéro associé à l'arête
    successeur suivant; // rue suivante
    Boolean visite; // indique si la rue menant à ce sommet a été parcourue ou non
    String nomRue; // nom de la rue

    /**
     * constructeur, initialise l'objet avec le numéro du sommet et le nom de la rue
     * @param sommet le numéro du sommet
     * @param nomRue le nom de la rue menant à ce sommet
     */
    public successeur(int sommet, String nomRue) {
        suivant = null;
        this.sommet = sommet;
        this.nomRue = nomRue;
        this.numero = -1;
        this.visite = Boolean.FALSE;
    }

    /**
     * accesseur du numéro du sommet de ce successeur
     * @return le numéro du sommet
     */
    public int get_sommet() {
        return sommet;
    }

    /**
     * retourne le numéro attribué à l'arc
     * @return le numéro
     */
    public int get_numero() {
        return numero;
    }

    /**
     * remet à faux tous les booléens de visite du graphe
     */
    public void raz_visite() {
        visite = Boolean.FALSE;
        if(suivant != null) {
            suivant.raz_visite();
        }
    }

    /**
     * ajout d'une rue à la liste des rues connectés à un sommet
     * @param rue le nouveau successeur
     */
    public void ajouter_rue(successeur rue) {
        if(suivant == null) {
            suivant = rue;
        } else {
            suivant.ajouter_rue(rue);
        }
    }

    /**
     * retourne le successeur suivant dans la liste des successeurs
     * @return le successeur, peut être null
     */
    public successeur suivant() {
        return suivant;
    }

    /**
     * indique si cet arc a déjà été utilisé
     * @return vrai ssi l'arc a été utilisé
     */
    public Boolean est_utilise() {
        return visite;
    }

    /**
     * marque une rue comme étant visitée
     * @param sommet le numéro du sommet au bout de la rue
     * @pre la rue existe
     */
    void marquer_rue(int sommet) {
        if(this.sommet == sommet) {
            visite = Boolean.TRUE;
        } else {
            suivant.marquer_rue(sommet);
        }
    }

    /**
     * marque cette rue comme étant visitée
     */
    void marquer() {
        visite = Boolean.TRUE;
    }

    /**
     * numérotation de l'arête (entre le sommet prédécesseur et celui-ci)
     * @param numero le numero de l'arête
     */
    void numeroter(int numero) {
        this.numero = numero;
    }

    /**
     * affichage formaté de la liste des rues
     * @return la chaîne de caractère formatée
     */
    public String toString() {
        String res = nomRue + " -> " + sommet;
        if(suivant != null) {
            res += " ; " + suivant.toString();
        }
        return res;
    }


}
