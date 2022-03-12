package jsonplaceholder;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UserTest {

    @Test
    public void createNewUser() {

        User user = new User();
        user.setName("Bartosz Testowy");
        user.setUsername("Bartek");
        user.setEmail("bartek@akademiaqa.pl");
        user.setPhone("123123123");
        user.setWebsite("www.akademiaQa.pl");

        Geo geo = new Geo();
        geo.setLat("-37.3159");
        geo.setLng("81.1496");

        Address address = new Address();
        address.setStreet("Sezamkowa");
        address.setSuite("Apt 1");
        address.setCity("New York");
        address.setZipcode("123123");
        address.setGeo(geo);

        user.setAddress(address);

        Company company = new Company();
        company.setName("Company name");
        company.setCatchPhrase("Multi-layered client-server neural-net");
        company.setBs("harness real-time e-markets");

        user.setCompany(company);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("https://jsonplaceholder.typicode.com/users")
                .then()
                .statusCode(201)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("name")).isEqualTo(user.getName());
    }

}