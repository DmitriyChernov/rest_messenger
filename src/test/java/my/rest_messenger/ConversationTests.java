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
import static my.rest_messenger.UserTests.createUserRest;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConversationTests {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void assertThatGetConversationListIsWorking() {
        given()
                .basePath("/conversation").get("").
                then()
                .log()
                .body()
                .statusCode(200);
    }

    @Test
    public void assertThatCreateConversationIsWorking() throws JSONException {
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        int userId = createUserRest(newUser);

        JSONObject owner = new JSONObject()
                .put("username","sergey")
                .put("id", userId);

        JSONObject newConversation = new JSONObject()
                .put("name","test_dialog")
                .put("owner", owner);

        given()
                .basePath("/conversation")
                .contentType("application/json")  //another way to specify content type
                .body(newConversation.toString())   // use jsonObj toString method
                .when()
                    .post()
                .then()
                    .log().body()
                    .assertThat()
                    .statusCode(201)
                    .body("name", equalTo("test_dialog"))
                    .body("owner.username", equalTo("sergey"))
                    .body("owner.id", equalTo(userId));
    }

    @Test
    public void assertThatGetConversationByIdIsWorking() throws JSONException {
        // create conversation
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        int userId = createUserRest(newUser);

        JSONObject owner = new JSONObject()
                .put("username","sergey")
                .put("id", userId);

        JSONObject newConversation = new JSONObject()
                .put("name","test_dialog")
                .put("owner", owner);

        int conversationId = createConversationRest(newConversation);

        // get conversation by id
        given()
                .basePath("/conversation").get("/" + conversationId)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    public void assertThatUpdateConversationIsWorking() throws JSONException {
        // create conversation
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        int userId = createUserRest(newUser);

        JSONObject owner = new JSONObject()
                .put("username","sergey")
                .put("id", userId);

        JSONObject newConversation = new JSONObject()
                .put("name","test_dialog")
                .put("owner", owner);

        int conversationId = createConversationRest(newConversation);

        // create updated conversation
        JSONObject updatedConversation = new JSONObject()
                .put("name","test_dialog_updated")
                .put("owner", owner);

        // update conversation
        given()
                .basePath("/conversation")
                .contentType("application/json")  //another way to specify content type
                .body(updatedConversation.toString())   // use jsonObj toString method
                .when()
                    .put(String.valueOf(conversationId))
                .then()
                    .log().body()
                    .assertThat()
                    .statusCode(200)
                    .body("name", equalTo("test_dialog_updated"));
    }

    @Test
    public void assertThatDeleteConversationIsWorking() throws JSONException {
        // create conversation
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        int userId = createUserRest(newUser);

        JSONObject owner = new JSONObject()
                .put("username","sergey")
                .put("id", userId);

        JSONObject newConversation = new JSONObject()
                .put("name","test_dialog")
                .put("owner", owner);

        int conversationId = createConversationRest(newConversation);

        // delete conversation
        given()
                .basePath("/conversation")
                .contentType("application/json")  //another way to specify content type
                .body(newUser.toString())   // use jsonObj toString method
                .when()
                    .delete(String.valueOf(conversationId))
                .then()
                    .log().body()
                    .assertThat()
                    .statusCode(200)
                    .body("deleted", equalTo(true));
    }

    public static int createConversationRest(JSONObject newConversation) {
        int conversationId = given()
                .basePath("/conversation")
                .contentType("application/json")
                .body(newConversation.toString())
                .when()
                    .post()
                .then()
                    .assertThat()
                    .statusCode(201)
                    .extract()
                    .path("id");

        return conversationId;
    }
}
