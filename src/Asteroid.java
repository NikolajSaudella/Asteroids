import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ═══════════════════════════════════════════════════════════════════
 *  CLASSE DA IMPLEMENTARE — Asteroid (un asteroide)
 * ═══════════════════════════════════════════════════════════════════
 *
 * Asteroid è una sottoclasse di Entity che rappresenta un asteroide
 * in movimento nell'area di gioco.
 *
 * Gli asteroidi esistono in tre dimensioni:
 *   LARGE  (grande)  → si divide in 2 asteroidi MEDIUM quando colpito
 *   MEDIUM (medio)   → si divide in 2 asteroidi SMALL  quando colpito
 *   SMALL  (piccolo) → viene semplicemente distrutto quando colpito
 *
 * La dimensione è rappresentata da un enum già fornito qui sotto.
 *
 * STATO INTERNO (campi che dovete aggiungere):
 * ┌──────────────┬────────────────┬────────────────────────────────────┐
 * │ Nome         │ Tipo           │ Descrizione                        │
 * ├──────────────┼────────────────┼────────────────────────────────────┤
 * │ size         │ Size           │ La dimensione di questo asteroide  │
 * ├──────────────┼────────────────┼────────────────────────────────────┤
 * │ customShape  │ List<Vector2D> │ Lista di vertici del poligono      │
 * └──────────────┴────────────────┴────────────────────────────────────┘
 *
 * COSTANTI SUGGERITE:
 *   RADIUS_LARGE  = 40    → raggio asteroide grande  (pixel)
 *   RADIUS_MEDIUM = 22    → raggio asteroide medio
 *   RADIUS_SMALL  = 12    → raggio asteroide piccolo
 *   SPEED_LARGE   = 1.2   → velocità massima di riferimento (LARGE)
 *   SPEED_MEDIUM  = 2.0   → velocità massima di riferimento (MEDIUM)
 *   SPEED_SMALL   = 3.0   → velocità massima di riferimento (SMALL)
 *   SCORE_LARGE   = 20    → punti assegnati al giocatore se distrutto
 *   SCORE_MEDIUM  = 50
 *   SCORE_SMALL   = 100
 *
 * RENDERING — cosa fare:
 *   Sovrascrivere getShape() e getColor() (ereditati da Entity).
 *   Non serve scrivere altro: draw() è già gestito da Entity.
 *
 *   getShape(): restituire i vertici di un poligono, da adattare al raggio della dimensione corrente:
 *
 *   getColor(): restituire un colore
 *
 *   getAngle(): gli asteroidi non ruotano visivamente, quindi non è
 *     necessario sovrascrivere questo metodo (il default 0 va bene).
 */

public class Asteroid extends Entity {

    /** Le tre dimensioni possibili di un asteroide. */
    public enum Size { LARGE, MEDIUM, SMALL }

    // Generatore di numeri casuali condiviso da tutti gli asteroidi.
    private static final Random rng = new Random();

    private static final double RADIUS_LARGE = 40.0;
    private static final double RADIUS_MEDIUM = 22.0;
    private static final double RADIUS_SMALL = 12.0;

    private static final double SPEED_LARGE = 1.2;
    private static final double SPEED_MEDIUM = 2.0;
    private static final double SPEED_SMALL = 3.0;

    private static final int SCORE_LARGE = 20;
    private static final int SCORE_MEDIUM = 50;
    private static final int SCORE_SMALL = 100;

    private final Size size;
    private final List<Vector2D> customShape;

    /*
     * ── COSTRUTTORE ───────────────────────────────────────────────
     *
     * public Asteroid(Vector2D position, Vector2D velocity, Size size)
     *
     * Input:
     *   position → posizione iniziale
     *   velocity → velocità iniziale
     *   size     → dimensione (LARGE, MEDIUM o SMALL)
     *
     * Cosa fare:
     *   Chiamare super(...) passando position, velocity e il raggio
     *   corrispondente alla dimensione (usare le costanti RADIUS_*).
     *   Salvare size nel campo corrispondente.
     *   Assegnare a customShape i vertici del poligono che daranno la forma all'asteroide usando generateRandomShape()
     */

     public Asteroid(Vector2D position, Vector2D velocity, Size size) {
        super(position, velocity, radiusFor(size));
        this.size = size;
        this.customShape = generateRandomShape(getRadius());
    }

    private static double radiusFor(Size size) {
        switch (size) {
            case LARGE: return RADIUS_LARGE;
            case MEDIUM: return RADIUS_MEDIUM;
            case SMALL: return RADIUS_SMALL;
            default: return RADIUS_LARGE;
        }
    }

    private static double speedFor(Size size) {
        switch (size) {
            case LARGE: return SPEED_LARGE;
            case MEDIUM: return SPEED_MEDIUM;
            case SMALL: return SPEED_SMALL;
            default: return SPEED_LARGE;
        }
    }

    /*
     * ── private List<Vector2D> generateRandomShape(double radius) ────────────
     *
     * Metodo che ritorna la forma di un asteroide sotto forma di vertici di un poligono,
     * i vertici devono essere vicini a radius in modo che la hitbox durante il gioco sia realistica
     */

    private List<Vector2D> generateRandomShape(double radius) {
        List<Vector2D> shape = new ArrayList<>();
        int vertices = 8;

        for (int i = 0; i < vertices; i++) {
            double angle = i * 2 * Math.PI / vertices;
            double pointRadius = radius * (0.75 + rng.nextDouble() * 0.35);
            double x = Math.cos(angle) * pointRadius;
            double y = Math.sin(angle) * pointRadius;
            shape.add(new Vector2D(x, y));
        }

        return shape;
    }

    /*
     * ── spawnRandom(int screenWidth, int screenHeight) ────────────
     *
     * Metodo STATICO che crea un asteroide LARGE in una posizione
     * casuale sul bordo dello schermo con velocità casuale.
     * Viene chiamato da GameArea per creare gli asteroidi iniziali.
     *
     * Output: Asteroid  — un nuovo asteroide grande pronto all'uso
     *
     * Algoritmo suggerito:
     *   1. Scegliere casualmente se posizionare l'asteroide su un bordo
     *      orizzontale (sopra/sotto) o verticale (sinistra/destra).
     *   2. Generare una posizione casuale su quel bordo.
     *   3. Generare un angolo casuale: rng.nextDouble() * 2 * Math.PI
     *   4. Generare una velocità casuale intorno a SPEED_LARGE:
     *   5. Calcolare il vettore Vector2D velocità:
     *        vx = Math.cos(angle) * speed
     *        vy = Math.sin(angle) * speed
     *   6. Creare e restituire il nuovo Asteroid.
     */

    public static Asteroid spawnRandom(int screenWidth, int screenHeight) {
        Vector2D position;
        int side = rng.nextInt(4);

        switch (side) {
            case 0:
                position = new Vector2D(rng.nextDouble() * screenWidth, 0);
                break;
            case 1:
                position = new Vector2D(screenWidth, rng.nextDouble() * screenHeight);
                break;
            case 2:
                position = new Vector2D(rng.nextDouble() * screenWidth, screenHeight);
                break;
            default:
                position = new Vector2D(0, rng.nextDouble() * screenHeight);
                break;
        }

        double angle = rng.nextDouble() * 2 * Math.PI;
        double speed = SPEED_LARGE * (0.6 + rng.nextDouble() * 0.8);
        Vector2D velocity = new Vector2D(
            Math.cos(angle) * speed,
            Math.sin(angle) * speed
        );

        return new Asteroid(position, velocity, Size.LARGE);
    }

    /*
     * ── update(int width, int height) ────────────────────────────
     *
     * Gli asteroidi si muovono in linea retta a velocità costante
     * e fanno wrapping ai bordi dello schermo.
     *
     * Passi:
     *   1. Chiamare move()              → sposta l'asteroide
     *   2. Chiamare wrapAround(w, h)
     *
     * Nota: gli asteroidi non cambiano mai velocità da soli.
     */

    @Override
    public void update(int width, int height) {
        move();
        wrapAround(width, height);
    }

    /*
     * ── split() ──────────────────────────────────────────────────
     *
     * Output: List<Asteroid>  — lista dei frammenti generati
     *                           (lista vuota se l'asteroide è SMALL)
     *
     * Quando un proiettile colpisce questo asteroide, GameArea chiama
     * split() per ottenere i frammenti da aggiungere all'area di gioco.
     *
     * Regole:
     *   LARGE  → restituire 2 nuovi asteroidi MEDIUM
     *   MEDIUM → restituire 2 nuovi asteroidi SMALL
     *   SMALL  → restituire una lista vuota (new ArrayList<>())
     *
     * I frammenti partono dalla stessa posizione del padre (position.copy())
     * e hanno velocità casuali proporzionali alla loro dimensione:
     *
     * Non chiamare destroy() qui: ci pensa GameArea.
     */

    public List<Asteroid> split() {
        List<Asteroid> fragments = new ArrayList<>();

        if (size == Size.SMALL) {
            return fragments;
        }

        Size newSize = (size == Size.LARGE) ? Size.MEDIUM : Size.SMALL;

        for (int i = 0; i < 2; i++) {
            double angle = rng.nextDouble() * 2 * Math.PI;
            double speed = speedFor(newSize) * (0.75 + rng.nextDouble() * 0.5);
            Vector2D velocity = new Vector2D(
                Math.cos(angle) * speed,
                Math.sin(angle) * speed
            );
            fragments.add(new Asteroid(getPosition().copy(), velocity, newSize));
        }

        return fragments;
    }

    /*
     * ── getScore() ───────────────────────────────────────────────
     *
     * Output: int  — punteggio assegnato al giocatore quando questo
     *                asteroide viene distrutto
     *
     */

     public int getScore() {
        switch (size) {
            case LARGE: return SCORE_LARGE;
            case MEDIUM: return SCORE_MEDIUM;
            case SMALL: return SCORE_SMALL;
            default: return 0;
        }
    }

    /*
     * ── getShape() ───────────────────────────────────────────────
     *
     * Output: List<Vector2D>  — vertici del poligono (vedi spiegazione sopra)
     *
     * Restituire i vertici che danno fomra all' asteroide.
     * Il sistema di coordinate è relativo al centro (0,0).
     */

     @Override
    public List<Vector2D> getShape() {
        return customShape;
    }

    /*
     * ── getColor() ───────────────────────────────────────────────
     *
     * Output: Color
     *
     */

    @Override
    public Color getColor() {
        return Color.LIGHT_GRAY;
    }

}
