import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Graph {
    private HashSet<Troncon> troncons;
    private ArrayList<Ligne> lignes1;

    private HashMap<String,HashSet<Troncon>> tronconStations;
    public Graph(File lignes, File tronconFile) {
        troncons = new HashSet<>();
        lignes1 = new ArrayList<>();
        tronconStations = new HashMap<String,HashSet<Troncon>>();


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
                Troncon tr = new Troncon(departTroncon, arriveeTroncon, dureeTroncon, lignes1.get(numeroLigne - 1));
                troncons.add(tr);
                tronconStations.computeIfAbsent(departTroncon,k->new HashSet<>()).add(tr);
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
        Set<String> file = new LinkedHashSet<>();
        // Origine de chaque sommet de la file
        Map<String, Troncon> tronconMap = new HashMap<>();


        file.add(stationDepart);

        String sommetCourant = null;

        while (!file.contains(stationArrivee)) {
            sommetCourant = file.iterator().next();
            for (Troncon troncon : tronconStations.get(sommetCourant)) {
                if (!sommetVisite.contains(troncon.getArrivee())) {
                    file.add(troncon.getArrivee());
                    tronconMap.putIfAbsent(troncon.getArrivee(), troncon);
                }

            }
            file.remove(sommetCourant);
            sommetVisite.add(sommetCourant);
            if(file.isEmpty()){
                System.out.println("Pas de route entre"+stationDepart+" et "+stationArrivee);
                System.exit(1);
            }
        }

        System.out.println(
            "Le chemin le plus court pour aller de " + stationDepart + " a " + stationArrivee
                + " est le suivant");

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
        System.out.println("nbTroncons = "+cheminTroncon.size());
        System.out.println("dureeTransport = "+ cheminTroncon.stream().map(Troncon::getDuree)
            .reduce(0, Integer::sum));
    }

    //Dijkstra
    public void calculerCheminMinimisantTempsTransport(String stationDepart, String stationArrivee) {
    }
}
