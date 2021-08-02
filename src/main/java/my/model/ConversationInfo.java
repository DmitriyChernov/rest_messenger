package my.model;

import javax.persistence.*;
import java.util.List;

public class ConversationInfo {

    //fields
    private Long conversationId;
    private String conversationName;
    private List<User> users;
    private User owner;

    //Constructors
    public ConversationInfo(Long conversationId, String conversationName, List<User> users, User owner) {
        this.conversationId = conversationId;
        this.conversationName = conversationName;
        this.users = users;
        this.owner = owner;
    }

    public ConversationInfo() {

    }

    // Setters & getters
    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User setOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    // Overrided funcs
    @Override
    public String toString() {
        return "ConversationInfo [conversation_id=" + conversationId + ", conversationName=" + conversationName + ", owner=" + owner.getUsername() + ", users count=" + users.size() +"]";
    }
}