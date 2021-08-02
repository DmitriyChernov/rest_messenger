package my.model;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "USER")
public class User {

    //fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String username;

    @OneToMany(cascade={CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy="owner")
    private Set<Conversation> conversations;

    @OneToMany(cascade={CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy="user")
    private Set<UserConversation> userConversation;

    @OneToMany(cascade={CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy="author")
    private Set<Message> messages;

    //Constructors
    public User(String name) {
        this.username = name;
    }

    public User() {

    }

    // Setters & getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Overrided funcs
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + username + "]";
    }
}