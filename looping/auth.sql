CREATE TABLE Token(
   id_token SERIAL,
   token VARCHAR(100) ,
   pin INTEGER,
   expiration TIMESTAMP,
   active BOOLEAN,
   PRIMARY KEY(id_token)
);

CREATE TABLE Setting(
   id_setting SERIAL,
   session_duree NUMERIC(15,2)  ,
   tentative_max INTEGER NOT NULL,
   daty TIMESTAMP NOT NULL,
   PRIMARY KEY(id_setting)
);

CREATE TABLE Users(
   id_user SERIAL,
   nom VARCHAR(100) ,
   prenom VARCHAR(100) ,
   email VARCHAR(100)  NOT NULL,
   password VARCHAR(100)  NOT NULL,
   is_verified BOOLEAN,
   id_token INTEGER,
   PRIMARY KEY(id_user),
   UNIQUE(email),
   FOREIGN KEY(id_token) REFERENCES Token(id_token)
);

CREATE TABLE Tentative(
   id_tentative SERIAL,
   compteur INTEGER,
   daty TIMESTAMP,
   id_user INTEGER NOT NULL,
   PRIMARY KEY(id_tentative),
   FOREIGN KEY(id_user) REFERENCES Users(id_user)
);

CREATE TABLE Blocked(
   id_blocked SERIAL,
   expiration TIMESTAMP,
   daty TIMESTAMP,
   id_user INTEGER NOT NULL,
   PRIMARY KEY(id_blocked),
   FOREIGN KEY(id_user) REFERENCES Users(id_user)
);
