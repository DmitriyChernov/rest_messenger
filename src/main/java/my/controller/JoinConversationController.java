package my.controller;

import my.error.ResourceExists;
import my.error.ResourceNotFoundException;
import my.model.Conversation;
import my.model.User;
import my.model.UserConversation;
import my.repository.ConversationRepository;
import my.repository.UserConversationRepository;
import my.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class JoinConversationController {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConversationRepository userConversationRepository;

    @RequestMapping(value = "/join_conversation",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity<UserConversation>  joinConversation(@RequestParam(value="conversation_id") Long conversationId
            , @RequestParam(value="user_id") Long userId)
                    throws ResourceNotFoundException, ResourceExists {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("conversation not found for this id :: " + conversationId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found for this id :: " + conversationId));

        if (userConversationRepository.findByConversationId(conversationId).orElse(null).stream().anyMatch(uc -> uc.getUser().equals(user))) {
            throw new ResourceExists("user conversation for conversation id :: " + conversationId + "; user id :: " + userId + " already exists!");
        }

        final UserConversation savedUC = userConversationRepository.save(new UserConversation(user, conversation));

        return ResponseEntity.ok(savedUC);
    }
}