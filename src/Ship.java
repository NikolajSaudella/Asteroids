import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════════
 *  CLASSE DA IMPLEMENTARE — Ship (la nave del giocatore)
 * ═══════════════════════════════════════════════════════════════════
 *
 * Ship è una sottoclasse di Entity che rappresenta la nave controllata
 * dal giocatore.
 *
 * STATO INTERNO (campi che dovete aggiungere):
 * ┌─────────────────┬────────────┬────────────────────────────────────────┐
 * │ Nome suggerito  │ Tipo       │ Descrizione                            │
 * ├─────────────────┼────────────┼────────────────────────────────────────┤
 * │ angle           │ double     │ Direzione in cui punta la nave,        │
 * │                 │            │ in radianti. 0 = destra, -PI/2 = su.  │
 * │ thrusting       │ boolean    │ true se il motore è acceso.            │
 * │ turningLeft     │ boolean    │ true se il tasto sinistra è premuto.   │
 * │ turningRight    │ boolean    │ true se il tasto destra è premuto.     │
 * │ shootTimer      │ int        │ Countdown al prossimo sparo permesso.  │
 * │                 │            │ La nave può sparare solo quando è 0.   │
 * └─────────────────┴────────────┴────────────────────────────────────────┘
 *
 * COSTANTI SUGGERITE (aggiungetele come campi static final):
 *   RAGGIO = 15
 *   ROTATION_SPEED = 0.07   → radianti aggiunti/sottratti per frame
 *   THRUST_POWER   = 0.25   → incremento di velocità per frame
 *   FRICTION       = 0.98   → fattore di attrito applicato ogni frame
 *   MAX_SPEED      = 6.0    → velocità massima (pixel/frame)
 *   BULLET_COOLDOWN = 15    → frame minimi tra uno sparo e l'altro
 *
 * RENDERING — cosa fare:
 *   Sovrascrivere getShape() e getColor() (ereditati da Entity).
 *   Il metodo draw() viene chiamato automaticamente e usa queste due
 *   informazioni per disegnare la nave: voi non scrivete mai codice grafico.
 *
 *   getShape(): restituire i vertici di un triangolo che punta a destra:
 *     { 15, 0,   -10, -8,   -10, 8 }
 *     Il sistema di coordinate è relativo al centro della nave (0,0).
 *     Entity ruoterà automaticamente il triangolo dell'angolo corrente.
 *
 *   getColor(): restituire Color.WHITE.
 *
 *   getAngle(): sovrascrivere questo metodo per restituire il campo angle,
 *     così Entity sa di quanto ruotare la forma.
 *
 */
public class Ship extends Entity {

    private static final double RAGGIO = 15.0;
    private static final double ROTATION_SPEED = 0.07;
    private static final double THRUST_POWER = 0.25;
    private static final double FRICTION = 0.98;
    private static final double MAX_SPEED = 6.0;
    private static final int BULLET_COOLDOWN = 15;

    private double angle;
    private boolean thrusting;
    private boolean turningLeft;
    private boolean turningRight;
    private int shootTimer;

    private boolean invulnerable;


    /*
     * ── COSTRUTTORE ───────────────────────────────────────────────
     *
     * public Ship(int screenWidth, int screenHeight)
     *
     * Input:
     *   screenWidth, screenHeight → dimensioni dell'area di gioco
     *
     * Cosa fare:
     *   Chiamare super(...) passando:
     *     - posizione iniziale: centro dello schermo
     *     - velocità iniziale: ferma
     *     - raggio:
     *   Inizializzare angle = -Math.PI / 2  (punta verso l'alto)
     *   Inizializzare shootTimer = 0
     */
    public Ship(int screenWidth, int screenHeight) {
        super(
            new Vector2D(screenWidth / 2.0, screenHeight / 2.0),
            new Vector2D(0, 0),
            RAGGIO
        );

        angle = -Math.PI / 2;
        shootTimer = 0;
    }


    /*
     * ── METODI DI INPUT - NON MODIFICARE ───────────────────────────
     *
     * Questi tre metodi vengono chiamati da GameArea ogni volta che
     * il giocatore preme o rilascia un tasto. Semplicemente assegnano
     * il valore al campo corrispondente.
     *
     * public void setTurningLeft(boolean v)   → turningLeft  = v
     * public void setTurningRight(boolean v)  → turningRight = v
     * public void setThrusting(boolean v)     → thrusting    = v
     */
    public void setTurningLeft(boolean v)  { turningLeft  = v; }
    public void setTurningRight(boolean v) { turningRight = v; }
    public void setThrusting(boolean v)    { thrusting    = v; }



    /*
     * ── update(int width, int height) ────────────────────────────
     *
     * Chiamato ~60 volte al secondo da GameArea. Aggiorna la nave.
     *
     * Passi da implementare nell'ordine:
     *
     *  1. ROTAZIONE
     *     if turningLeft  → angle -= ROTATION_SPEED
     *     if turningRight → angle += ROTATION_SPEED
     *
     *  2. PROPULSIONE
     *     if thrusting → aggiungere alla velocità un vettore nella
     *     direzione angle con modulo THRUST_POWER:
     *       velocity.x += Math.cos(angle) * THRUST_POWER
     *       velocity.y += Math.sin(angle) * THRUST_POWER
     *
     *  3. ATTRITO
     *     velocity.scale(FRICTION)
     *     Questo fa rallentare la nave da sola quando non si accelera.
     *
     *  4. LIMITE DI VELOCITÀ
     *     Se velocity.length() > MAX_SPEED, riscalare il vettore
     *     mantenendo la direzione ma portando la lunghezza a MAX_SPEED.
     *
     *  5. MOVIMENTO
     *
     *  6. WRAPPING
     *     Chiamare wrapAround(width, height)
     *     Se la nave esce da un bordo, riappare dal lato opposto.
     *
     *  7. COOLDOWN SPARO
     */
    @Override
    public void update(int width, int height) {
        if (turningLeft) {
            angle -= ROTATION_SPEED;
        }
        if (turningRight) {
            angle += ROTATION_SPEED;
        }

        Vector2D velocity = getVelocity();
        if (thrusting) {
            velocity.x += Math.cos(angle) * THRUST_POWER;
            velocity.y += Math.sin(angle) * THRUST_POWER;
        }

        velocity.scale(FRICTION);

        double speed = velocity.length();
        if (speed > MAX_SPEED) {
            velocity.scale(MAX_SPEED / speed);
        }

        move();
        wrapAround(width, height);

        if (shootTimer > 0) {
            shootTimer--;
        }
    }


    /*
     * ── shoot() ──────────────────────────────────────────────────
     *
     * Output: Bullet  (il nuovo proiettile) oppure null (se in cooldown)
     *
     * Comportamento:
     *   Se shootTimer > 0 → restituire null (non si può ancora sparare)
     *   Altrimenti:
     *     Calcolare la posizione iniziale del proiettile (la punta della nave):
     *       bx = position.x + Math.cos(angle) * radius
     *       by = position.y + Math.sin(angle) * radius
     *     Calcolare la velocità del proiettile (direzione angle + velocità nave):
     *       vx = Math.cos(angle) * Bullet.BULLET_SPEED + velocity.x
     *       vy = Math.sin(angle) * Bullet.BULLET_SPEED + velocity.y
     *     Resettare shootTimer = BULLET_COOLDOWN
     *     Restituire il proiettile appena creato.
     *
     * Questo metodo viene chiamato da GameArea quando il giocatore
     * preme la barra spaziatrice.
     */
    public Bullet shoot() {
        if (shootTimer > 0) {
            return null;
        }

        Vector2D position = getPosition();
        Vector2D velocity = getVelocity();

        double bx = position.x + Math.cos(angle) * getRadius();
        double by = position.y + Math.sin(angle) * getRadius();
        double vx = Math.cos(angle) * Bullet.BULLET_SPEED + velocity.x;
        double vy = Math.sin(angle) * Bullet.BULLET_SPEED + velocity.y;

        shootTimer = BULLET_COOLDOWN;
        return new Bullet(new Vector2D(bx, by), new Vector2D(vx, vy));
    }


    /*
     * ── getShape() ───────────────────────────────────────────────
     *
     * Output: List<Vector2D>  — vertici del triangolo della nave
     *
     * Sono tre vertici (coppie x,y) relativi al centro (0,0):
     *   (15, 0)    → punta della nave (destra)
     *   (-10, -8)  → angolo superiore sinistro
     *   (-10,  8)  → angolo inferiore sinistro
     * Entity ruoterà automaticamente questi punti di `angle` radianti.
     */

    @Override
    public List<Vector2D> getShape() {
        return Arrays.asList(
            new Vector2D(15, 0),
            new Vector2D(-10, -8),
            new Vector2D(-10, 8)
        );
    }

    /*
     * ── getColor() ───────────────────────────────────────────────
     *
     * Output: Color
     *
     * Restituire Color
     */

    @Override
    public Color getColor() {
        if (invulnerable) 
            return Color.YELLOW;
        
        return Color.WHITE;
    }

    /*
     * ── getAngle() ───────────────────────────────────────────────
     *
     * Output: double  — l'angolo corrente della nave in radianti
     *
     * Sovrascrivere il metodo ereditato da Entity e restituire
     * il campo angle. Questo permette a draw() di ruotare
     * correttamente la forma.
     */

    @Override
    public double getAngle() {
        return angle;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

}


