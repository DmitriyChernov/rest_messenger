package my.repository;

import my.model.User;
import my.model.UserConversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserConversationRepository extends JpaRepository<UserConversation, Long> {
    public Optional<List<UserConversation>> findByConversationId(Long id);
}
