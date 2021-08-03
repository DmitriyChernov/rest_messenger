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
import static my.rest_messenger.ConversationTests.createConversationRest;
import static my.rest_messenger.UserTests.createUserRest;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageTests {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void assertThatGetMessageListIsWorking() {
        given()
                .basePath("/message").get("").
                then()
                .log()
                .body()
                .statusCode(200);
    }

    @Test
    public void assertThatCreateMessageIsWorking() throws JSONException {
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        int userId = createUserRest(newUser);

        JSONObject author = new JSONObject()
                .put("username","sergey")
                .put("id", userId);

        JSONObject newConversation = new JSONObject()
                .put("name","test_dialog")
                .put("owner", author);
        int conversationId = createConversationRest(newConversation);
        newConversation.put("id", conversationId);

        JSONObject newMessage = new JSONObject()
                .put("text","test_hi")
                .put("author", author)
                .put("conversation", newConversation);

        given()
                .basePath("/message")
                .contentType("application/json")  //another way to specify content type
                .body(newMessage.toString())   // use jsonObj toString method
                .when()
                    .post()
                .then()
                    .log().body()
                    .assertThat()
                    .statusCode(201)
                    .body("text", equalTo("test_hi"))
                    .body("author.username", equalTo("sergey"))
                    .body("author.id", equalTo(userId))
                    .body("conversation.id", equalTo(conversationId))
                    .body("conversation.name", equalTo("test_dialog"))
                    .body("conversation.owner.id", equalTo(userId))
                    .body("conversation.owner.username", equalTo("sergey"));
    }

    @Test
    public void assertThatGetMessageByIdIsWorking() throws JSONException {
        // create message
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        int userId = createUserRest(newUser);

        JSONObject author = new JSONObject()
                .put("username","sergey")
                .put("id", userId);

        JSONObject newConversation = new JSONObject()
                .put("name","test_dialog")
                .put("owner", author);
        int conversationId = createConversationRest(newConversation);
        newConversation.put("id", conversationId);

        JSONObject newMessage = new JSONObject()
                .put("text","test_hi")
                .put("author", author)
                .put("conversation", newConversation);

        int messageId = createMessageRest(newMessage);

        // get message by id
        given()
                .basePath("/message").get("/" + messageId)
                .then()
                    .log().body()
                    .statusCode(200);
    }

    @Test
    public void assertThatUpdateMessageIsWorking() throws JSONException {
        // create message
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        int userId = createUserRest(newUser);

        JSONObject author = new JSONObject()
                .put("username","sergey")
                .put("id", userId);

        JSONObject newConversation = new JSONObject()
                .put("name","test_dialog")
                .put("owner", author);
        int conversationId = createConversationRest(newConversation);
        newConversation.put("id", conversationId);

        JSONObject newMessage = new JSONObject()
                .put("text","test_hi")
                .put("author", author)
                .put("conversation", newConversation);

        int messageId = createMessageRest(newMessage);

        // create updated message
        JSONObject updatedMessage = new JSONObject()
                .put("text","test_hi_updated")
                .put("author", author)
                .put("conversation", newConversation);

        // update conversation
        given()
                .basePath("/message")
                .contentType("application/json")  //another way to specify content type
                .body(updatedMessage.toString())   // use jsonObj toString method
                .when()
                .put(String.valueOf(messageId))
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("text", equalTo("test_hi_updated"));
    }

    @Test
    public void assertThatDeleteConversationIsWorking() throws JSONException {
        // create message
        JSONObject newUser = new JSONObject()
                .put("username","sergey");
        int userId = createUserRest(newUser);

        JSONObject author = new JSONObject()
                .put("username","sergey")
                .put("id", userId);

        JSONObject newConversation = new JSONObject()
                .put("name","test_dialog")
                .put("owner", author);
        int conversationId = createConversationRest(newConversation);
        newConversation.put("id", conversationId);

        JSONObject newMessage = new JSONObject()
                .put("text","test_hi")
                .put("author", author)
                .put("conversation", newConversation);

        int messageId = createMessageRest(newMessage);

        // delete conversation
        given()
                .basePath("/message")
                .contentType("application/json")  //another way to specify content type
                .body(newMessage.toString())   // use jsonObj toString method
                .when()
                .delete(String.valueOf(messageId))
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("deleted", equalTo(true));
    }

    public static int createMessageRest(JSONObject newMessage) {
        int messageId = given()
                .basePath("/message")
                .contentType("application/json")
                .body(newMessage.toString())
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        return messageId;
    }
}
