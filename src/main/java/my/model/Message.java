package my.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "MESSAGE")
public class Message {

    //fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEXT")
    private String text;

    @ManyToOne(cascade={CascadeType.PERSIST}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne(cascade={CascadeType.PERSIST}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    //Constructors
    public Message(String text, User author, Conversation conversation) {
        this.text = text;
        this.author = author;
        this.conversation = conversation;
    }

    public Message() {

    }

    // Setters & getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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
        return "Message [id=" + id + ", text=" + text + ", user=" + getAuthor().getUsername() + ", conversation=" + conversation.getName() + "]";
    }
}