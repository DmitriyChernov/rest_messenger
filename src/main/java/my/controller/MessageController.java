package my.controller;

import my.error.ResourceNotFoundException;
import my.model.Conversation;
import my.model.Message;
import my.model.User;
import my.repository.ConversationRepository;
import my.repository.MessageRepository;
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
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @RequestMapping(value = "/message",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public List<Message> getAllMessages() {
        List<Message> messages =  messageRepository.findAll();
        return messages;
    }


    @RequestMapping(value = "/message/{id}",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity<Message> getMessageById(@PathVariable(value = "id") Long messageId)
            throws ResourceNotFoundException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("message not found for this id :: " + messageId));
        return ResponseEntity.ok().body(message);
    }

    @RequestMapping(value = "/message",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Optional<User> optionalUser = userRepository.findById(message.getAuthor().getId());
        if (!optionalUser.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        message.setAuthor(optionalUser.get());

        Optional<Conversation> optionalConversation = conversationRepository.findById(message.getConversation().getId());
        if (!optionalConversation.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        message.setConversation(optionalConversation.get());

        Message savedMessage = messageRepository.save(message);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedMessage.getId()).toUri();

        return ResponseEntity.created(location).body(savedMessage);
    }


    @RequestMapping(value = "/message/{id}",
            method = RequestMethod.PUT,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity <Message> updateMessage(@PathVariable(value = "id") Long messageId,
                                                            @RequestBody Message messageDetails) throws ResourceNotFoundException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("message not found for this id :: " + messageId));

        message.setText(messageDetails.getText());
        final Message updatedMessage = messageRepository.save(message);
        return ResponseEntity.ok(updatedMessage);
    }

    @RequestMapping(value = "/message/{id}",
            method = RequestMethod.DELETE,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public Map<String, Boolean> deleteMessage(@PathVariable(value = "id") Long messageId)
            throws ResourceNotFoundException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("conversation not found for this id :: " + messageId));

        messageRepository.delete(message);
        Map <String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
