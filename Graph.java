import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Graph {

  private HashSet<Troncon> troncons;
  private ArrayList<Ligne> lignes1;

  private HashMap<String, HashSet<Troncon>> tronconStations;

  public Graph(File lignes, File tronconFile) {
    troncons = new HashSet<>();
    lignes1 = new ArrayList<>();
    tronconStations = new HashMap<String, HashSet<Troncon>>();

    try (FileReader fileReader = new FileReader(lignes);

        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;

      while ((line = bufferedReader.readLine()) != null) {

        String[] data = line.split(",");

        int numeroLigne = Integer.parseInt(data[0]);
        String nom = data[1];
        String source = data[2];
        String destination = data[3];
        String type = data[4];
        int attenteMoyenne = Integer.parseInt(data[5]);

        lignes1.add(new Ligne(numeroLigne, nom, source, destination, type, attenteMoyenne));

      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    try (FileReader fileReader = new FileReader(tronconFile);

        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        String[] data = line.split(",");
        int numeroLigne = Integer.parseInt(data[0]);
        String departTroncon = data[1];
        String arriveeTroncon = data[2];
        int dureeTroncon = Integer.parseInt(data[3]);
        Troncon tr = new Troncon(departTroncon, arriveeTroncon, dureeTroncon,
            lignes1.get(numeroLigne - 1));
        troncons.add(tr);
        tronconStations.computeIfAbsent(departTroncon, k -> new HashSet<>()).add(tr);
      }


    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  // BFS FILE+Ensemble sommet visite + Hashmap<Sommet,Troncon>
  public void calculerCheminMinimisantNombreTroncons(String stationDepart, String stationArrivee) {
    // Sommet Courant deja parcouru HashSet pour eviter doublon + contains = o(1)
    Set<String> sommetVisite = new HashSet<>();
    // Sommet Courant LinkedHashSet pour preserver l'ordre
    ArrayDeque<String> file = new ArrayDeque<>();
    // Origine de chaque sommet de la file
    Map<String, Troncon> tronconMap = new HashMap<>();

    file.addLast(stationDepart);
    sommetVisite.add(stationDepart);

    String sommetCourant = null;

    while (!file.contains(stationArrivee)) {
      sommetCourant = file.removeFirst();
      for (Troncon troncon : tronconStations.get(sommetCourant)) {
        if (!sommetVisite.contains(troncon.getArrivee())) {
          file.addLast(troncon.getArrivee());
          tronconMap.putIfAbsent(troncon.getArrivee(), troncon);
          sommetVisite.add(troncon.getArrivee());
        }

      }
      if (file.isEmpty()) {
        System.out.println("Pas de route entre" + stationDepart + " et " + stationArrivee);
        System.exit(1);
      }
    }
    System.out.println(
        "Le chemin avec le moins de troncon pour aller de " + stationDepart + " a " + stationArrivee
            + " est le suivant");

    afficherChemin(tronconMap, sommetCourant, stationArrivee, stationDepart);


  }


  //Dijkstra Provisoire TreeMap(pour le minimum) hashmap pour definitif
  public void calculerCheminMinimisantTempsTransport(String stationDepart, String stationArrivee) {
    // HashMap<String,String> pour retracer le chemin qui peux changer a chaque parcours de minimum
    Map<String, Troncon> tronconMap = new HashMap<>();

    // Set parce que on a pas besoin de savoir quel duree min est associe a quel sommet
    Set<String> etiquetteDefinitif = new HashSet<>();

    HashMap<String, Integer> etiquettesProvisoires = new HashMap<>();
    // Comparator pour prendre le first qui est le minimum
    TreeSet<String> sortedMap = new TreeSet<>(new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        int value1 = etiquettesProvisoires.get(s1);
        int value2 = etiquettesProvisoires.get(s2);
        int compare = Integer.compare(value1, value2);
        if (compare == 0) {
          return s1.compareTo(s2);
        }
        return value1 - value2;
      }
    });

    etiquettesProvisoires.put(stationDepart, 0);
    sortedMap.add(stationDepart);
    while (!sortedMap.isEmpty()) {
      String minValue = sortedMap.pollFirst();
      etiquetteDefinitif.add(minValue);
      for (Troncon troncon : tronconStations.get(minValue)) {
        if (!etiquetteDefinitif.contains(troncon.getArrivee())){
          int duree = etiquettesProvisoires.get(minValue) + troncon.getDuree();
          if( etiquettesProvisoires.get(troncon.getArrivee()) == null || etiquettesProvisoires.get(troncon.getArrivee()) > duree) {

              try {
                sortedMap.remove(troncon.getArrivee());
              } catch (NullPointerException e){
                e.getMessage();
              }


            etiquettesProvisoires.put(troncon.getArrivee(), duree);
            sortedMap.add(troncon.getArrivee());
            tronconMap.put(troncon.getArrivee(), troncon);
          }
        }


        }
      etiquettesProvisoires.remove(minValue);
      }




    // tronconMap.get(stationArrivee).getDepart() et pas minValue parce que sinon il va remonter le chemin depuis la derniere station
    // qui etait dans sorted map vu que dans le bfs la derniere station de la file est celle qui relie directement au
    // chemin avec le moins de troncon et que afficherChemin a ete fait pour le BFS a la base
    System.out.println(
        "Le chemin avec le moins de temps passé dans les transports pour aller de " + stationDepart
            + " a " + stationArrivee
            + " est le suivant");
    afficherChemin(tronconMap, tronconMap.get(stationArrivee).getDepart(), stationArrivee,
        stationDepart);

  }

  void afficherChemin(Map<String, Troncon> tronconMap, String sommetCourant, String stationArrivee,
      String stationDepart) {

    List<Troncon> cheminTroncon = new LinkedList<>();
    cheminTroncon.add(tronconMap.get(stationArrivee));

    while (!sommetCourant.equals(stationDepart)) {
      for (Troncon troncon : tronconMap.values()) {
        if (troncon.getArrivee().equals(sommetCourant)) {
          sommetCourant = troncon.getDepart();
          cheminTroncon.add(troncon);
          break;
        }

      }
    }

    ListIterator<Troncon> iterator = cheminTroncon.listIterator(cheminTroncon.size());

    while (iterator.hasPrevious()) {
      System.out.println(iterator.previous());
    }
    System.out.println("nbTroncons = " + cheminTroncon.size());
    int dureeTransport = cheminTroncon.stream().map(Troncon::getDuree)
        .reduce(0, Integer::sum);
    System.out.println("dureeTransport = " + dureeTransport);

    int dureeTotale = cheminTroncon.get(0).getLigne().getAttenteMoyenne();
    ;
    int idLigne = cheminTroncon.get(0).getLigne().getId();
    for (Troncon troncon : cheminTroncon) {
      if (troncon.getLigne().getId() != idLigne) {
        idLigne = troncon.getLigne().getId();
        dureeTotale += troncon.getLigne().getAttenteMoyenne();
      }
    }

    dureeTotale += dureeTransport;
    System.out.println("durreeTotale = " + dureeTotale);
  }

}
