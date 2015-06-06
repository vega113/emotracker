# Users schema

# --- !Ups

CREATE TABLE Emotions(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    userId varchar(255) NOT NULL,
    emotion varchar(255) NOT NULL,
    reason varchar(255),
    target varchar(255),
    location varchar(255),
    link varchar(255),
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE Emotions;