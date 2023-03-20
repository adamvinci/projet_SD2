public class Deplacement {

    private String ligne;
    private String depart;
    private String arrivee;
    private int attenteMoyenne;
    private int duree;
    private int nbTroncon;
    private String type;
    private String direction;

    public Deplacement(String ligne, String depart, String arrivee, int attenteMoyenne, int duree, int nbTroncon, String type, String direction) {
        this.ligne = ligne;
        this.depart = depart;
        this.arrivee = arrivee;
        this.attenteMoyenne = attenteMoyenne;
        this.duree = duree;
        this.nbTroncon = nbTroncon;
        this.type = type;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Deplacement [" +
                "ligne=" + ligne +
                ", depart='" + depart + '\'' +
                ", arrivee='" + arrivee + '\'' +
                ", attenteMoyenne=" + attenteMoyenne +
                ", duree=" + duree +
                ", nbTroncon=" + nbTroncon +
                ", type='" + type + '\'' +
                ", direction='" + direction + '\'' +
                ']';
    }
}
