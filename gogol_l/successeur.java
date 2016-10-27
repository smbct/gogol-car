package gogol_l;

import java.lang.String;

public class successeur {

    int sommet; // numéro de la place
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
        this.visite = Boolean.FALSE;
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
     * @sommet le numéro de la place
     * @nomRue le nom de la rue
     */
    public void ajouter_rue(int sommet, String nomRue) {
        if(suivant == null) {
            successeur suc = new successeur(sommet, nomRue);
            suivant = suc;
        } else {
            suivant.ajouter_rue(sommet, nomRue);
        }
    }


}
