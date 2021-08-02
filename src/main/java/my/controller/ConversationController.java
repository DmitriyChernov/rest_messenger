package my.controller;

import my.error.ResourceNotFoundException;
import my.model.Conversation;
import my.model.User;
import my.repository.ConversationRepository;
import my.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ConversationController {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/conversation",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public List<Conversation> getAllConversations() {
        List<Conversation> conversations =  conversationRepository.findAll();
        for (Conversation conv : conversations) {
            System.out.println(conv);
        }
        return conversations;
    }

    @RequestMapping(value = "/conversation/{id}",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity<Conversation> getConversationById(@PathVariable(value = "id") Long conversationId)
            throws ResourceNotFoundException {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("conversation not found for this id :: " + conversationId));
        return ResponseEntity.ok().body(conversation);
    }

    @RequestMapping(value = "/conversation",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity<Conversation> createConversation(@RequestBody Conversation conversation) {
        System.out.println(conversation);
        Optional<User> optionalUser = userRepository.findById(conversation.getOwner().getId());
        if (!optionalUser.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        conversation.setOwner(optionalUser.get());

        Conversation savedConversation = conversationRepository.save(conversation);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedConversation.getId()).toUri();

        return ResponseEntity.created(location).body(savedConversation);
    }

    @RequestMapping(value = "/conversation/{id}",
            method = RequestMethod.PUT,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity <Conversation> updateConversation(@PathVariable(value = "id") Long conversationId,
                                            @RequestBody Conversation conversationDetails) throws ResourceNotFoundException {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("conversation not found for this id :: " + conversationId));

        conversation.setName(conversationDetails.getName());
        final Conversation updatedConversation = conversationRepository.save(conversation);
        return ResponseEntity.ok(updatedConversation);
    }

    @RequestMapping(value = "/conversation/{id}",
            method = RequestMethod.DELETE,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public Map<String, Boolean> deleteConversation(@PathVariable(value = "id") Long conversationId)
            throws ResourceNotFoundException {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("conversation not found for this id :: " + conversationId));

        conversationRepository.delete(conversation);
        Map <String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}