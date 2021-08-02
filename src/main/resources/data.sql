CREATE TABLE user (
                      id INT AUTO_INCREMENT  PRIMARY KEY,
                      name VARCHAR(250) NOT NULL,
                      PRIMARY KEY (`id`)
);

CREATE TABLE conversation (
                              id INT AUTO_INCREMENT  PRIMARY KEY,
                              name VARCHAR(250) NOT NULL,
                              user_id INT NOT NULL,
                              PRIMARY KEY (`id`),
                              CONSTRAINT FK_CONVERSATION_OWNER FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE message (
                              id INT AUTO_INCREMENT  PRIMARY KEY,
                              text VARCHAR(250) NOT NULL,
                              conversation_id INT NOT NULL,
                              user_id INT NOT NULL,
                              PRIMARY KEY (`id`),
                              CONSTRAINT FK_MESSAGE_AUTHOR FOREIGN KEY (user_id) REFERENCES user(id),
                              CONSTRAINT FK_MESSAGE_CONVERSATION FOREIGN KEY (conversation_id) REFERENCES conversation(id)
);

CREATE TABLE user_conversation (
                         id INT AUTO_INCREMENT  PRIMARY KEY,
                         conversation_id INT NOT NULL,
                         user_id INT NOT NULL,
                         PRIMARY KEY (`id`),
                         CONSTRAINT FK_USERCONVERSATION_USER FOREIGN KEY (user_id) REFERENCES user(id),
                         CONSTRAINT FK_USERCONVERSATION_CONVERSATION FOREIGN KEY (conversation_id) REFERENCES conversation(id)
);


INSERT INTO user (name) VALUES
('Vasya'),
('Petya'),
('Adolf');

INSERT INTO conversation (name, user_id) VALUES
('dialog1', 1),
('dialog2', 2),
('dialog3', 3);

INSERT INTO user_conversation (conversation_id, user_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 2);

--INSERT INTO message (text, conversation_id, user_id) VALUES
--('privet', 1, 1),
--('kak dela', 2, 3),
--('bratka', 2, 2) ;
