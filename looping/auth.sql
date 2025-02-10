CREATE TABLE IF NOT EXISTS Token(
   id_token SERIAL,
   token VARCHAR(100) ,
   pin INTEGER,
   expiration TIMESTAMP,
   active BOOLEAN,
   PRIMARY KEY(id_token)
);

CREATE TABLE IF NOT EXISTS Setting(
   id_setting SERIAL,
   session_duree NUMERIC(15,2)  ,
   tentative_max INTEGER DEFAULT 3,
   daty TIMESTAMP NOT NULL,
   PRIMARY KEY(id_setting)
);

CREATE TABLE IF NOT EXISTS Users(
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
CREATE TABLE IF NOT EXISTS Tentative(
   id_tentative SERIAL,
   compteur INTEGER,
   daty TIMESTAMP,
   id_user INTEGER NOT NULL,
   PRIMARY KEY(id_tentative),
   FOREIGN KEY(id_user) REFERENCES Users(id_user)
);

CREATE TABLE IF NOT EXISTS Blocked(
   id_blocked SERIAL,
   expiration TIMESTAMP,
   daty TIMESTAMP,
   id_user INTEGER NOT NULL,
   PRIMARY KEY(id_blocked),
   FOREIGN KEY(id_user) REFERENCES Users(id_user)
);

CREATE TABLE IF NOT EXISTS devise (
    id SERIAL PRIMARY KEY,
    valeur NUMERIC(18,8) NOT NULL,
    nom CHARACTER VARYING(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS config_devise (
    id SERIAL PRIMARY KEY,
    devise_id INTEGER NOT NULL,
    devise_base_id INTEGER NOT NULL,
    valeur NUMERIC(18,8) NOT NULL,
    FOREIGN KEY (devise_id) REFERENCES devise(id),
    FOREIGN KEY (devise_base_id) REFERENCES devise(id)
);


CREATE TABLE IF NOT EXISTS crypto (
    id SERIAL PRIMARY KEY,
    nom CHARACTER VARYING(255) NOT NULL,
    symbol CHARACTER VARYING(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS crypto_cours (
   id SERIAL PRIMARY KEY,
   devise_id INTEGER NOT NULL,
   crypto_id INTEGER NOT NULL,
   cours NUMERIC(18,8) NOT NULL,
   datetime TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
   FOREIGN KEY (crypto_id) REFERENCES crypto(id),
   FOREIGN KEY (devise_id) REFERENCES devise(id)
);


CREATE TABLE IF NOT EXISTS crypto_transaction (
   id SERIAL PRIMARY KEY,
   crypto_id INTEGER NOT NULL,
   devise_id INTEGER NOT NULL,
   id_user INTEGER NOT NULL,
   entre NUMERIC(18,8) NOT NULL,
   sortie NUMERIC(18,8) NOT NULL,
   crypto_cours NUMERIC(18,8) NOT NULL,
   datetime TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
   FOREIGN KEY (crypto_id) REFERENCES crypto(id),
   FOREIGN KEY (devise_id) REFERENCES devise(id)
);



CREATE TABLE IF NOT EXISTS user_transaction (
   id SERIAL PRIMARY KEY,
   devise_id INTEGER NOT NULL,
   id_user INTEGER NOT NULL,
   entre NUMERIC(18,8) NOT NULL,
   sortie NUMERIC(18,8) NOT NULL,
   datetime TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
   etat NUMERIC(18,8) NOT NULL,
   FOREIGN KEY (devise_id) REFERENCES devise(id)
);

CREATE TABLE IF NOT EXISTS admin (
   id SERIAL PRIMARY KEY,
   user_name CHARACTER VARYING(255) NOT NULL,
   mot_depasse CHARACTER VARYING(255) NOT NULL
);




-- Data

INSERT INTO devise (id,nom, valeur) VALUES
(1,'EUR', 1),
(2,'USD', 1),   
(3,'MGA', 1);

-- Insertion des cryptomonnaies
INSERT INTO crypto (id,nom,symbol) VALUES
(1,'Bitcoin','BTC'),
(2,'Ethereum','ETH');
INSERT INTO crypto (id, nom, symbol) VALUES
(3, 'Binance Coin', 'BNB'),
(4, 'Solana', 'SOL'),
(5, 'XRP', 'XRP'),
(6, 'Cardano', 'ADA'),
(7, 'Dogecoin', 'DOGE'),
(8, 'Polkadot', 'DOT'),
(9, 'Avalanche', 'AVAX'),
(10, 'Polygon', 'MATIC'),
(11, 'Litecoin', 'LTC'),
(12, 'Chainlink', 'LINK');


-- Configuration des paires de devises
INSERT INTO config_devise (id,devise_id, devise_base_id, valeur) VALUES 
(1,1, 1, 1), -- 1 EUR = 4900 MGA
(2,2, 1, 1.10),    -- 1 EUR = 1.10 USD
(3,3, 1, 4500);


INSERT INTO ADMIN VALUES(default, 'admin','test');

CREATE OR REPLACE VIEW crypto_transaction_view AS
SELECT 
    id,
    crypto_id,
    devise_id,
    id_user,
    entre,
    sortie,
    crypto_cours,
    datetime,
    CASE 
        WHEN sortie = 0 THEN 'buy'
        WHEN entre = 0 THEN 'sell'
        ELSE 'unknown'
    END AS type
FROM 
    crypto_transaction;

