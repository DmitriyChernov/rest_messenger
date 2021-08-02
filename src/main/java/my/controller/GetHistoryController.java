package my.controller;

import my.error.ResourceNotFoundException;
import my.model.Conversation;
import my.model.Message;
import my.repository.ConversationRepository;
import my.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GetHistoryController {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(value = "/get_history/conversation_id={id}",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public List<Message> getHistory(@PathVariable(value = "id") Long conversationId) throws ResourceNotFoundException {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("conversation not found for this id :: " + conversationId));

        List<Message> history = messageRepository.findByConversationId(conversationId);

        return history;
    }
}