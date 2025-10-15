package fr.insalyon.waso.som.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.insalyon.waso.util.DBConnection;
import fr.insalyon.waso.util.JsonServletHelper;
import fr.insalyon.waso.util.exception.DBException;
import fr.insalyon.waso.util.exception.ServiceException;
import java.util.List;

/**
 *
 * @author WASO Team
 */
public class ServiceObjetMetier {

    protected DBConnection dBConnection;
    protected JsonObject container;

    public ServiceObjetMetier(DBConnection dBConnection, JsonObject container) {
        this.dBConnection = dBConnection;
        this.container = container;
    }

    public void release() {
        this.dBConnection.close();
    }

    public void getListeClient() throws ServiceException {
        try {
            JsonArray jsonListe = new JsonArray();

            List<Object[]> listeClients = this.dBConnection.launchQuery("SELECT ClientID, TypeClient, Denomination, Adresse, Ville FROM CLIENT ORDER BY ClientID");

            for (Object[] row : listeClients) {
                JsonObject jsonItem = new JsonObject();

                Integer clientId = (Integer) row[0];
                jsonItem.addProperty("id", clientId);
                jsonItem.addProperty("type", (String) row[1]);
                jsonItem.addProperty("denomination", (String) row[2]);
                jsonItem.addProperty("adresse", (String) row[3]);
                jsonItem.addProperty("ville", (String) row[4]);

                List<Object[]> listePersonnes = this.dBConnection.launchQuery("SELECT ClientID, PersonneID FROM COMPOSER WHERE ClientID = ? ORDER BY ClientID,PersonneID", clientId);
                JsonArray jsonSousListe = new JsonArray();
                for (Object[] innerRow : listePersonnes) {
                    jsonSousListe.add((Integer) innerRow[1]);
                }

                jsonItem.add("personnes-ID", jsonSousListe);

                jsonListe.add(jsonItem);
            }

            this.container.add("clients", jsonListe);

        } catch (DBException ex) {
            throw JsonServletHelper.ServiceObjectMetierExecutionException("Client","getListeClient", ex);
        }
    }

    public void rechercherClientParDenomination(String denomination, String ville) throws ServiceException {
        try {
            JsonArray jsonListe = new JsonArray();

            List<Object[]> listeClients = this.dBConnection.launchQuery(
                    "SELECT ClientID, TypeClient, Denomination, Adresse, Ville FROM CLIENT WHERE LOWER(Denomination) LIKE LOWER(?)  AND LOWER(Ville) LIKE LOWER(?) ORDER BY ClientID",
                    "%" + denomination + "%", "%" + ville + "%"
            );



            for (Object[] row : listeClients) {
                JsonObject jsonItem = new JsonObject();
                Integer clientId = (Integer) row[0];
                jsonItem.addProperty("id", clientId);
                jsonItem.addProperty("type", (String) row[1]);
                jsonItem.addProperty("denomination", (String) row[2]);
                jsonItem.addProperty("adresse", (String) row[3]);
                jsonItem.addProperty("ville", (String) row[4]);

                List<Object[]> listePersonnes = this.dBConnection.launchQuery("SELECT ClientID, PersonneID FROM COMPOSER WHERE ClientID = ? ORDER BY ClientID,PersonneID", clientId);
                JsonArray jsonSousListe = new JsonArray();
                for (Object[] innerRow : listePersonnes) {
                    jsonSousListe.add((Integer) innerRow[1]);
                }

                jsonItem.add("personnes-ID", jsonSousListe);

                jsonListe.add(jsonItem);
            }

            this.container.add("clients", jsonListe);

        } catch (DBException ex) {
            throw JsonServletHelper.ServiceObjectMetierExecutionException("Client","rechercherClientParDenomination", ex);
        }
    }

    public void rechercherClientParNumero(Integer numero) throws ServiceException {
        try {
            JsonArray jsonListe = new JsonArray();

            List<Object[]> listeClients = this.dBConnection.launchQuery(
                    "SELECT ClientID, TypeClient, Denomination, Adresse, Ville FROM CLIENT WHERE ClientID = ?",
                    numero
            );


            for (Object[] row : listeClients) {
                JsonObject jsonItem = new JsonObject();
                Integer clientId = (Integer) row[0];
                jsonItem.addProperty("id", clientId);
                jsonItem.addProperty("type", (String) row[1]);
                jsonItem.addProperty("denomination", (String) row[2]);
                jsonItem.addProperty("adresse", (String) row[3]);
                jsonItem.addProperty("ville", (String) row[4]);

                List<Object[]> listePersonnes = this.dBConnection.launchQuery("SELECT ClientID, PersonneID FROM COMPOSER WHERE ClientID = ? ORDER BY ClientID,PersonneID", clientId);
                JsonArray jsonSousListe = new JsonArray();
                for (Object[] innerRow : listePersonnes) {
                    jsonSousListe.add((Integer) innerRow[1]);
                }

                jsonItem.add("personnes-ID", jsonSousListe);

                jsonListe.add(jsonItem);
            }

            this.container.add("clients", jsonListe);

        } catch (DBException ex) {
            throw JsonServletHelper.ServiceObjectMetierExecutionException("Client", "rechercherClientParNumero", ex);
        }
    }

    public void rechercherClientParPersonne(List<Integer> numeros, String ville) throws ServiceException {
        try {
            JsonArray jsonListe = new JsonArray();

            if (numeros.isEmpty()) {
                this.container.add("clients", jsonListe);
                return;
            }

            // On itère à travers les numéros de personnes et on fait une requête pour chaque
            for (Integer numero : numeros) {

                List<Object[]> listeClients = this.dBConnection.launchQuery(
                        "SELECT C.ClientID, C.TypeClient, C.Denomination, C.Adresse, C.Ville FROM CLIENT C JOIN COMPOSER CO ON C.ClientID = CO.ClientID WHERE CO.PersonneID = ? AND LOWER(C.Ville) LIKE LOWER(?) ORDER BY C.ClientID",
                        numero, "%" + ville + "%"
                );
                for (Object[] row : listeClients) {
                    JsonObject jsonItem = new JsonObject();
                    Integer clientId = (Integer) row[0];
                    jsonItem.addProperty("id", clientId);
                    jsonItem.addProperty("type", (String) row[1]);
                    jsonItem.addProperty("denomination", (String) row[2]);
                    jsonItem.addProperty("adresse", (String) row[3]);
                    jsonItem.addProperty("ville", (String) row[4]);

                    List<Object[]> listePersonnes = this.dBConnection.launchQuery("SELECT ClientID, PersonneID FROM COMPOSER WHERE ClientID = ? ORDER BY ClientID,PersonneID", clientId);
                    JsonArray jsonSousListe = new JsonArray();
                    for (Object[] innerRow : listePersonnes) {
                        jsonSousListe.add((Integer) innerRow[1]);
                    }

                    jsonItem.add("personnes-ID", jsonSousListe);

                    jsonListe.add(jsonItem);
                }
            }
            this.container.add("clients", jsonListe);




        } catch (DBException ex) {
            throw JsonServletHelper.ServiceObjectMetierExecutionException("Client", "rechercherClientParNumero", ex);
        }
    }

}
