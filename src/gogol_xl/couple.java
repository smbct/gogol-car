package gogol_xl;
import java.lang.String;

/**
* petite structure pour stocker deux indice ensemble
* <p>
* cette structure permet de gerer les ensemble de deux elements du couplage
*/
public class couple {
	public int premier; //premier element
	public int deuxieme; //deuxieme element

	/**
	* construit le couple contenant les deux elements corespondants
	*
	* @param premier le premier indice du couple
	* @param deuxieme le deuxieme indice du couple
	*/
	public couple(int premier, int deuxieme) {
		this.premier = premier;
		this.deuxieme = deuxieme;
	}

	public String toString() {
		return "("+premier+" "+deuxieme+")";
	}
}
