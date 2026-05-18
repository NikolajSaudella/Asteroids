import java.awt.*;
import java.util.List;
/**
 * Classe base per tutte le entità del gioco: nave, asteroidi e proiettili.
 *
 * Ogni entità ha:
 *   - una posizione (un Vector2D con coordinate x, y)
 *   - una velocità  (un Vector2D che indica lo spostamento per frame)
 *   - un raggio     (un numero che approssima le dimensioni dell'entità)
 *   - un flag alive (false = l'entità è stata distrutta e va rimossa)
 *
 * ─────────────────────────────────────────────────────────────────
 *  RENDERING — come funziona, cosa devono fare gli studenti
 * ─────────────────────────────────────────────────────────────────
 *
 * Il disegno è gestito interamente da questa classe tramite il metodo
 * final draw(Graphics g): voi non dovrete mai scrivere codice grafico.
 *
 * Per definire l'aspetto della vostra entità dovete solo sovrascrivere
 * due metodi semplici:
 *
 *   List<Vector2D>  getShape()  → restituisce i vertici del poligono che
 *                                 rappresenta l'entità, come array di Vector2D
 *                                 relativi al centro (0, 0). La classe Entity sa
 *                                 già come ruotarli e posizionarli nel punto giusto.
 *
 *   Color     getColor()  → restituisce il colore con cui disegnare
 *                           il poligono (es. Color.WHITE, Color.GRAY…).
 *
 * Esempi di forme già pronte che potete usare come riferimento:
 *
 *   Nave (triangolo che punta a destra):
 *     {(15,0),  (-10,-8),  (-10,8)};
 *
 *   Asteroide grande (ottagono):
 *     {(0,-40), (28,-28), (40,0), (28,28), (0,40), (-28,28), (-40,0), (-28,-28)};
 *
 *   Proiettile (quadratino):
 *     {(-2,-2), (2,-2), (2,2), (-2,2)};
 *
 * Questa classe è già completa: non è necessario modificarla.
 */
public abstract class Entity {

    private Vector2D position;
    private Vector2D velocity;
    private double   radius;
    private boolean  alive;

    /**
     * @param position posizione iniziale dell'entità
     * @param velocity velocità iniziale dell'entità
     * @param radius   raggio che approssima le dimensioni (usato per le collisioni)
     */
    public Entity(Vector2D position, Vector2D velocity, double radius) {
        this.position = position;
        this.velocity = velocity;
        this.radius   = radius;
        this.alive    = true;
    }

    // ═══════════════════════════════════════════════════════════════
    // Metodi astratti che le sottoclassi DEVONO implementare
    // ═══════════════════════════════════════════════════════════════

    /**
     * Aggiorna lo stato dell'entità per un frame.
     *
     * @param width  larghezza dell'area di gioco in pixel
     * @param height altezza dell'area di gioco in pixel
     */
    public abstract void update(int width, int height);

    /**
     * Restituisce i vertici del poligono che rappresenta questa entità.
     *
     * @return array di Vector2D con le coordinate dei vertici
     */
    public abstract List<Vector2D> getShape();

    /**
     * Restituisce il colore con cui disegnare questa entità.
     *
     * @return un oggetto Color
     */
    public abstract Color getColor();

    // ═══════════════════════════════════════════════════════════════
    // Rendering — già implementato, non modificare
    // ═══════════════════════════════════════════════════════════════

    /**
     * Disegna l'entità sullo schermo.
     * Prende la forma restituita da getShape(), la ruota dell'angolo
     * corrente e la disegna nella posizione corretta.
     *
     * Questo metodo è già completo: non è necessario sovrascriverlo.
     */
    public final void draw(Graphics g) {
        List<Vector2D> shape = getShape();
        int n = shape.size();
        int[] px = new int[n];
        int[] py = new int[n];

        double angle = getAngle();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        for (int i = 0; i < n; i++) {
            Vector2D v = shape.get(i);
            // Rotation matrix: x' = x*cos - y*sin, y' = x*sin + y*cos
            px[i] = (int) Math.round(position.x + v.x * cos - v.y * sin);
            py[i] = (int) Math.round(position.y + v.x * sin + v.y * cos);
        }

        g.setColor(getColor());
        g.drawPolygon(px, py, n);
    }

    /**
     * Angolo di rotazione dell'entità in radianti.
     * Le sottoclassi che ruotano (come la nave) devono sovrascrivere
     * questo metodo restituendo il loro angolo corrente.
     * Le entità che non ruotano (asteroidi, proiettili) non devono
     * fare nulla: il valore di default 0 va bene.
     *
     * @return angolo in radianti (default: 0)
     */
    public double getAngle() {
        return 0;
    }

    // ═══════════════════════════════════════════════════════════════
    // Metodi di utilità — già implementati
    // ═══════════════════════════════════════════════════════════════

    /**
     * Sposta l'entità di un frame aggiungendo la velocità alla posizione.
     * Chiamare questo metodo dentro update().
     */
    public void move() {
        position.add(velocity);
    }

    /**
     * Se l'entità è uscita da un bordo, la fa riapparire dal lato opposto.
     * Chiamare questo metodo dentro update() dopo move().
     *
     * @param width  larghezza dell'area di gioco
     * @param height altezza dell'area di gioco
     */
    protected void wrapAround(int width, int height) {
        if (position.x < 0)       position.x += width;
        if (position.x > width)   position.x -= width;
        if (position.y < 0)       position.y += height;
        if (position.y > height)  position.y -= height;
    }

    /**
     * Verifica se questa entità tocca un'altra entità.
     * Due entità collidono quando la distanza tra i loro centri
     * è minore della somma dei loro raggi.
     *
     * @param other l'altra entità con cui verificare la collisione
     * @return true se le due entità si sovrappongono
     */
    public boolean collidesWith(Entity other) {
        return Vector2D.distance(this.position, other.position)
               < (this.radius + other.radius);
    }

    /** Segna questa entità come distrutta. Verrà rimossa da GameArea. */
    public void destroy() { this.alive = false; }

    public boolean  isAlive()    { return alive; }
    public Vector2D getPosition(){ return position; }
    public Vector2D getVelocity(){ return velocity; }
    public double   getRadius()  { return radius; }
}
