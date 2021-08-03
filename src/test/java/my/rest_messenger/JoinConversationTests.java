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
public class JoinConversationTests {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void assertThatJoinChannelIsWorking() throws JSONException {
        // create user & conversation
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


        // join conversation by user id and conversation id
        given()
                .basePath("/join_conversation").post("?conversation_id=" + conversationId + "&user_id=" + userId)
                .then()
                    .log().body()
                    .assertThat()
                    .statusCode(200)
                    .body("user.id", equalTo(userId))
                    .body("user.username", equalTo("sergey"))
                    .body("conversation.id", equalTo(conversationId))
                    .body("conversation.name", equalTo("test_dialog"))
                    .body("conversation.owner.id", equalTo(userId))
                    .body("conversation.owner.username", equalTo("sergey"));
    }
}