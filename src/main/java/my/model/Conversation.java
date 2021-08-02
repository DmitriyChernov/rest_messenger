package my.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Conversation")
public class Conversation {

    //fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne(cascade={CascadeType.PERSIST}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @OneToMany(cascade={CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy="conversation")
    private Set<Message> messages;

    @OneToMany(cascade={CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy="conversation")
    private Set<UserConversation> userConversation;

    //Constructors
    public Conversation(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public Conversation(String name, Long ownerId) {
        this.name = name;
        this.owner = owner;
    }

    public Conversation() {

    }

    // Setters & getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    // Overrided funcs
    @Override
    public String toString() {
        return "Conversation [id=" + id + ", name=" + name + ", owner=" + owner.getUsername() +"]";
    }
}