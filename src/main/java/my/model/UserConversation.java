package my.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USER_CONVERSATION")
public class UserConversation {

    //fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade={CascadeType.PERSIST}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade={CascadeType.PERSIST}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    //Constructors
    public UserConversation(User user, Conversation conversation) {
        this.user = user;
        this.conversation = conversation;
    }

    public UserConversation() {

    }

    // Setters & getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    // Overrided funcs
    @Override
    public String toString() {
        return "UserConversation [id=" + id + ", user=" + user.getUsername() + ", conversation=" + conversation.getName() + "]";
    }
}