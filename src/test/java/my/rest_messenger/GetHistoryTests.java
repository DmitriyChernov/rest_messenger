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
import static my.rest_messenger.MessageTests.createMessageRest;
import static my.rest_messenger.UserTests.createUserRest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetHistoryTests {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void assertThatGetHistoryIsWorking() throws JSONException {
        // create conversation & message
        JSONObject newUser = new JSONObject()
                .put("username", "sergey");
        int userId = createUserRest(newUser);

        JSONObject author = new JSONObject()
                .put("username", "sergey")
                .put("id", userId);

        JSONObject newConversation = new JSONObject()
                .put("name", "test_dialog")
                .put("owner", author);
        int conversationId = createConversationRest(newConversation);
        newConversation.put("id", conversationId);

        JSONObject newMessage1 = new JSONObject()
                .put("text", "test_hi")
                .put("author", author)
                .put("conversation", newConversation);

        int messageId1 = createMessageRest(newMessage1);

        JSONObject newMessage2 = new JSONObject()
                .put("text", "test_how_are_you")
                .put("author", author)
                .put("conversation", newConversation);
        int messageId2 = createMessageRest(newMessage2);

        // get conversation by id
        given()
                .basePath("/get_history").get("/conversation_id=" + conversationId)
                .then()
                    .log().body()
                    .assertThat()
                    .statusCode(200)
                    .body("id", hasItems(messageId1, messageId2))
                    .body("text", hasItems("test_hi", "test_how_are_you"))
                    .body("author.username", hasItems("sergey"))
                    .body("author.id", hasItems(userId))
                    .body("conversation.id", hasItems(conversationId))
                    .body("conversation.name", hasItems("test_dialog"))
                    .body("conversation.owner.id", hasItems(userId))
                    .body("conversation.owner.username", hasItems("sergey"));
    }
}