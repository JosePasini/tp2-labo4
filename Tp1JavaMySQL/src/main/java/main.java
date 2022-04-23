import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import model.Pais;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class main {
    public static void main(String[] args) {
        Transaction txt = null;

        HttpClient httpClient = HttpClient.newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpResponse<String> response;
        JsonNode jsonNode;
        Pais pais;
        HttpRequest request;
        Session session;

        try (ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
             SessionFactory sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();) {
            session = sessionFactory.openSession();
            txt = session.beginTransaction();
            for (int i = 1; i <= 300; i++ ){
                request = getRequest(i);
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200){
                    jsonNode = objectMapper.readTree(response.body());
                    for (int j = 0; j < jsonNode.size(); j++) {
                        pais = new Pais().builder()
                                .codigo( jsonNode.get(j).get("numericCode").asInt() )
                                .nombre( jsonNode.get(j).get("name").textValue() )
                                .region( jsonNode.get(j).get("region").textValue() )
                                .capital( jsonNode.get(j).get("capital") == null ? "No tiene" : jsonNode.get(j).get("capital").textValue() )
                                .poblacion( jsonNode.get(j).get("population").asLong() )
                                .latitud( jsonNode.get(j).get("latlng") == null ? new BigDecimal(0) : jsonNode.get(j).get("latlng").get(0).decimalValue() )
                                .longitud( jsonNode.get(j).get("latlng") == null ? new BigDecimal(0) : jsonNode.get(j).get("latlng").get(1).decimalValue() )
                                .build();


                        if (rowExist(pais.getCodigoPais(), session)) {
                            session.merge(pais);
                        } else {
                            session.persist(pais);
                        }
                        session.flush();
                    }
                }
            }
            txt.commit();
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            txt.rollback();
        }
    }


    public static HttpRequest getRequest(int num){
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://restcountries.com/v2/callingcode/"+num))
                .build();
        return httpRequest;
    }

    public static boolean rowExist(int id, Session session) {
        Query query = session.createQuery("select 1 from Pais where codigoPais = " + id);
        Object resp = query.uniqueResult();
        return (resp == null) ? false : true;
    }
}
