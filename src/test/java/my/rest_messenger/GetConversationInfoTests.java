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
public class GetConversationInfoTests {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void assertThatGetConversationInfoIsWorking() throws JSONException {
        // create conversation & users
        JSONObject newUser1 = new JSONObject()
                .put("username", "sergey");
        int userId1 = createUserRest(newUser1);

        JSONObject newUser2 = new JSONObject()
                .put("username", "mary");
        int userId2 = createUserRest(newUser2);

        JSONObject author = new JSONObject()
                .put("username", "sergey")
                .put("id", userId1);

        JSONObject newConversation = new JSONObject()
                .put("name", "test_dialog")
                .put("owner", author);
        int conversationId = createConversationRest(newConversation);
        newConversation.put("id", conversationId);


        // join conversation first user
        joinConversation(conversationId, userId1, "sergey", userId1);
        // join conversation second user
        joinConversation(conversationId, userId2, "mary", userId1);

        // get conversation info
        given()
                .basePath("/get_conversation_info").get("/conversation_id=" + conversationId)
                .then()
                    .log().body()
                    .assertThat()
                    .statusCode(200)
                    .body("conversation.id", equalTo(conversationId))
                    .body("conversation.name", equalTo("test_dialog"))
                    .body("conversation.owner.username", equalTo("sergey"))
                    .body("conversation.owner.id", equalTo(userId1))
                    .body("users.id", hasItems(userId1, userId2))
                    .body("users.username", hasItems("sergey", "mary"));

    }

    private void joinConversation(int conversationId, int userId, String username, int ownerId) {
        given()
                .basePath("/join_conversation").post("?conversation_id=" + conversationId + "&user_id=" + userId)
                .then()
                    .log().body()
                    .assertThat()
                    .statusCode(200)
                    .body("user.id", equalTo(userId))
                    .body("user.username", equalTo(username))
                    .body("conversation.id", equalTo(conversationId))
                    .body("conversation.name", equalTo("test_dialog"))
                    .body("conversation.owner.id", equalTo(ownerId))
                    .body("conversation.owner.username", equalTo("sergey"));
    }
}