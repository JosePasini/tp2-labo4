import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpResponse<String> response;
        JsonNode jsonNode;
        Document document = new Document();
        Document find;
        Document update = new Document();
        HttpRequest request;

        MongoClient mongoClient;
        MongoDatabase db;
        MongoCollection<Document> collection;
        ClientSession session = null;
        try {
            mongoClient = getConexion();
            session = mongoClient.startSession();
            db = mongoClient.getDatabase("paises");
            collection = db.getCollection("pais");
            session.startTransaction();
            for (int i = 1; i <= 300; i++ ){
                request = getRequest(i);
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200){
                    jsonNode = objectMapper.readTree(response.body());
                    for (int j = 0; j < jsonNode.size(); j++) {
                        document.clear();
                        document.append("codigoPais", jsonNode.get(j).get("numericCode").asInt() )
                                .append("nombrePais", jsonNode.get(j).get("name").textValue() )
                                .append("CapitalPais", jsonNode.get(j).get("capital") == null ? "No tiene" : jsonNode.get(j).get("capital").textValue() )
                                .append("region", jsonNode.get(j).get("region").textValue() )
                                .append("poblacion", jsonNode.get(j).get("population").asLong() )
                                .append("latitud", jsonNode.get(j).get("latlng") == null ? new BigDecimal(0) : jsonNode.get(j).get("latlng").get(0).decimalValue() )
                                .append("longitud", jsonNode.get(j).get("latlng") == null ? new BigDecimal(0) : jsonNode.get(j).get("latlng").get(1).decimalValue() );

                        find = collection.find(Filters.eq("codigoPais", document.get("codigoPais"))).first();
                        if (find == null) {
                            collection.insertOne(document);
                        } else {
                            update.clear();
                            update.append("$set", document);
                            collection.updateOne(find, update);
                            find.clear();
                        }
                    }
                }
            }
            session.commitTransaction();
        }catch (Exception e) {
            e.printStackTrace();
            session.abortTransaction();
        } finally {
            session.close();
        }
    }

    public static MongoClient getConexion() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        return mongoClient;
    }

    public static HttpRequest getRequest(int num){
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://restcountries.com/v2/callingcode/"+num))
                .build();
        return httpRequest;
    }
}
