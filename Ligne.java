public class Ligne {
    private int id;
    private String nom;
    private String source;
    private String destination;
    private String type;
    private int attenteMoyenne;

    public Ligne(int id, String nom, String source, String destination, String type, int attenteMoyenne) {
        this.id = id;
        this.nom = nom;
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.attenteMoyenne = attenteMoyenne;
    }

    @Override
    public String toString() {
        return "Ligne[" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", type='" + type + '\'' +
                ", attenteMoyenne=" + attenteMoyenne +
                ']';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAttenteMoyenne() {
        return attenteMoyenne;
    }

    public void setAttenteMoyenne(int attenteMoyenne) {
        this.attenteMoyenne = attenteMoyenne;
    }
}
