package my.rest_messenger;

import io.restassured.RestAssured;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTests {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void assertThatGetUserListIsWorking() {
        given()
                .basePath("/user").get("").
                then()
                .log()
                .body()
                .statusCode(200);
    }

    @Test
    public void assertThatCreateUserIsWorking() throws JSONException {
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        given()
                .basePath("/user")
                .contentType("application/json")  //another way to specify content type
                .body(newUser.toString())   // use jsonObj toString method
                .when()
                .post()
                .then()
                .log().body()
                .assertThat()
                .statusCode(201)
                .body("username", equalTo("sergey"));
    }

    @Test
    public void assertThatGetUserByIdIsWorking() throws JSONException {
        // create user
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        int userId = createUserRest(newUser);

        // get user by id
        given()
                .basePath("/user").get("/" + userId)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    public void assertThatUpdateUserIsWorking() throws JSONException {
        // create user
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        int userId = createUserRest(newUser);

        // update user
        JSONObject updatedUser = new JSONObject()
                .put("username","sergey_updated");

        // update user
        given()
                .basePath("/user")
                .contentType("application/json")  //another way to specify content type
                .body(updatedUser.toString())   // use jsonObj toString method
                .when()
                .put(String.valueOf(userId))
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("username", equalTo("sergey_updated"));
    }

    @Test
    public void assertThatDeleteUserIsWorking() throws JSONException {
        // create user
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        int userId = createUserRest(newUser);

        // delete user
        given()
                .basePath("/user")
                .contentType("application/json")  //another way to specify content type
                .body(newUser.toString())   // use jsonObj toString method
                .when()
                    .delete(String.valueOf(userId))
                .then()
                    .log().body()
                    .assertThat()
                    .statusCode(200)
                    .body("deleted", equalTo(true));
    }

    public static int createUserRest(JSONObject newUser) {
        int userId = given()
                .basePath("/user")
                .contentType("application/json")
                .body(newUser.toString())
                .when()
                    .post()
                .then()
                    .assertThat()
                    .statusCode(201)
                    .extract()
                    .path("id");

        return userId;
    }
}
