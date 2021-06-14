DROP TABLE IF EXISTS parent;
DROP TABLE IF EXISTS child;

CREATE TABLE parent
(
    id   int SERIAL PRIMARY KEY NOT NULL,
    name varchar(255) NOT NULL
);

CREATE TABLE child
(
    id        int SERIAL PRIMARY KEY NOT NULL,
    name      varchar(255) NOT NULL,
    parent_id int,
    CONSTRAINT child_parent_fk FOREIGN KEY (parent_id) REFERENCES parent
);
