-- Créer un utilisateur
CREATE USER auth WITH PASSWORD 'auth';

-- Créer une base de données pour cet utilisateur
CREATE DATABASE auths OWNER auth;

-- Accorder toutes les permissions sur la base
GRANT ALL PRIVILEGES ON DATABASE auths TO auth;

-- Vérifier
\l
