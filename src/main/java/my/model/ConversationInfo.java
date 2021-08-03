package my.model;

import javax.persistence.*;
import java.util.List;

public class ConversationInfo {

    //fields
    private Conversation conversation;
    private List<User> users;

    //Constructors
    public ConversationInfo(Conversation conversation, List<User> users) {
        this.conversation = conversation;
        this.users = users;
    }

    public ConversationInfo() {

    }

    // Setters & getters
    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    // Overrided funcs
    @Override
    public String toString() {
        return "ConversationInfo [conversation_id=" + conversation.getId() + ", conversationName=" + conversation.getName() + ", owner=" + conversation.getOwner().getUsername() + ", users count=" + users.size() +"]";
    }
}