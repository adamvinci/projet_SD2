import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

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


        for (Troncon troncon: tronconStations.get("BOILEAU"))
            System.out.println(troncon.toString());

    }

    // BFS FILE+Ensemble sommet visite + Hashmap<Sommet,Troncon>
    public void calculerCheminMinimisantNombreTroncons(String stationDepart, String stationArrivee) {
    }

    //Dijkstra
    public void calculerCheminMinimisantTempsTransport(String stationDepart, String stationArrivee) {
    }
}
