package trello;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class OrganizationTest {

    private static final String KEY = "YOUR_KEY_HERE!!!";
    private static final String TOKEN = "YOUR_TOKEN_HERE!!!";

    private static Stream<Arguments> createOrganizationData() {
        return Stream.of(
                Arguments.of("This is display name", "Akademia QA is awesome!", "akademia qa", "https://akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "akademia qa", "http://akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "aqa", "http://akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "akademia_qa", "http://akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "akademiaqa123", "http://akademiaqa.pl"));
    }

    @DisplayName("Create organization with valida data")
    @ParameterizedTest(name = "Display name: {0}, desc: {1}, name: {2}, website: {3}")
    @MethodSource("createOrganizationData")
    public void createOrganization(String displayName, String desc, String name, String website) {

        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post("https://api.trello.com/1/organizations")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(displayName);

        final String organizationId = json.getString("id");

        given()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .when()
                .delete("https://api.trello.com/1/organizations" + "/" + organizationId)
                .then()
                .statusCode(200);
    }
}