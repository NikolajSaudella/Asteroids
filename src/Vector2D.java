/**
 * Rappresenta un vettore bidimensionale.
 * Viene usato sia per le posizioni (dove si trova un'entità)
 * sia per le velocità (di quanto si sposta ogni frame).
 *
 * Questa classe è già completa: non è necessario modificarla.
 */
public class Vector2D {

    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /** Restituisce una copia indipendente di questo vettore. */
    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    /** Somma il vettore other a questo (modifica questo vettore). */
    public void add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    /** Moltiplica entrambe le componenti per factor (scala il vettore). */
    public void scale(double factor) {
        this.x *= factor;
        this.y *= factor;
    }

    /** Restituisce la lunghezza (modulo) del vettore. */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Calcola la distanza tra due posizioni.
     * Utile per la collision detection.
     */
    public static double distance(Vector2D a, Vector2D b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}
