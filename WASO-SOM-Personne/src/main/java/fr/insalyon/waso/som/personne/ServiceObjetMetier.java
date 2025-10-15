package fr.insalyon.waso.som.personne;

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

    public void getListePersonne() throws ServiceException {
        try {
            List<Object[]> listePersonne = this.dBConnection.launchQuery("SELECT PersonneID, Nom, Prenom, Mail FROM PERSONNE ORDER BY PersonneID");

            JsonArray jsonListe = new JsonArray();

            for (Object[] row : listePersonne) {
                JsonObject jsonItem = new JsonObject();

                jsonItem.addProperty("id", (Integer) row[0]);
                jsonItem.addProperty("nom", (String) row[1]);
                jsonItem.addProperty("prenom", (String) row[2]);
                jsonItem.addProperty("mail", (String) row[3]);

                jsonListe.add(jsonItem);
            }

            this.container.add("personnes", jsonListe);

        } catch (DBException ex) {
            throw JsonServletHelper.ServiceObjectMetierExecutionException("Personne", "getListePersonne", ex);
        }
    }

    public void getPersonneById(int PersonneID) throws ServiceException {
        try {
            List<Object[]> result = this.dBConnection.launchQuery(
                    "SELECT PersonneID, Nom, Prenom, Mail FROM PERSONNE WHERE PersonneID = ?", PersonneID
            );

            if (!result.isEmpty()) {
                Object[] personne = result.get(0);
                JsonObject jsonItem = new JsonObject();
                jsonItem.addProperty("id", (Integer) personne[0]);
                jsonItem.addProperty("nom", (String) personne[1]);
                jsonItem.addProperty("prenom", (String) personne[2]);
                jsonItem.addProperty("mail", (String) personne[3]);
                this.container.add("personne", jsonItem);
            }


        } catch (DBException ex) {
            throw JsonServletHelper.ServiceObjectMetierExecutionException("Personne", "getPersonneById", ex);
        }

    }
    public void rechercherPersonneParNom(String Nom) throws ServiceException {
        try {
            List<Object[]> listePersonne = this.dBConnection.launchQuery(
                    "SELECT PersonneID, Nom, Prenom, Mail FROM PERSONNE WHERE LOWER(Nom) LIKE LOWER(?)",
                    "%" + Nom + "%"

            );

            JsonArray jsonListe = new JsonArray();

            for (Object[] row : listePersonne) {
                JsonObject jsonItem = new JsonObject();

                jsonItem.addProperty("id", (Integer) row[0]);
                jsonItem.addProperty("nom", (String) row[1]);
                jsonItem.addProperty("prenom", (String) row[2]);
                jsonItem.addProperty("mail", (String) row[3]);

                jsonListe.add(jsonItem);
            }

            this.container.add("personnes", jsonListe);



        } catch (DBException ex) {
            throw JsonServletHelper.ServiceObjectMetierExecutionException("Personne", "rechercherPersonneParNom", ex);
        }

    }



}
