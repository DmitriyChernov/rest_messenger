package my.controller;

import my.error.ResourceNotFoundException;
import my.model.*;
import my.repository.ConversationRepository;
import my.repository.MessageRepository;
import my.repository.UserConversationRepository;
import my.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GetConversationInfoController {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserConversationRepository userConversationRepository;

    @RequestMapping(value = "/get_conversation_info/conversation_id={id}",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ConversationInfo getConversationInfo(@PathVariable(value = "id") Long conversationId) throws ResourceNotFoundException {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("conversation not found for this id :: " + conversationId));

        List<UserConversation> userConversations = userConversationRepository.findByConversationId(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("users not found for this id :: " + conversationId));

        List<User> users = userConversations.stream().map(UserConversation::getUser).collect(Collectors.toList());

        return new ConversationInfo(conversation, users);
    }
}
