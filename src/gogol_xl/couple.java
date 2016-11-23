package gogol_xl;
import java.lang.String;

public class couple {
	public int premier;
	public int deuxieme;

	public couple(int premier, int deuxieme) {
		this.premier = premier;
		this.deuxieme = deuxieme;
	}

	public String toString() {
		return "("+premier+" "+deuxieme+")";
	}
}
