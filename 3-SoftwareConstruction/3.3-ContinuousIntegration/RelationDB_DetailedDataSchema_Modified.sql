-- Create user table
CREATE TABLE "User" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(359) CHECK (name ~ '^[a-zA-Z0-9]+$'),
    surname VARCHAR(350) CHECK (surname ~ '^[a-zA-Z0-9]+$'),
    email VARCHAR(255) UNIQUE CHECK (
        email ~ '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
    ),
    password VARCHAR(255) CHECK (
        password ~ '^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\$!?\])$'
    )
);

-- Create microclimate table
CREATE TABLE microclimate (
    id SERIAL PRIMARY KEY,
    temperature VARCHAR(20),
    ventilation VARCHAR(150),
    lightlevel NUMERIC CHECK (lightlevel > 0),
    humidity_id INT
);

-- Create humidity table
CREATE TABLE humidity (
    id SERIAL PRIMARY KEY,
    relativehumidity NUMERIC CHECK (relativehumidity > 0),
    absolutehumidity NUMERIC CHECK (absolutehumidity > 0)
);

-- Create planparameters table
CREATE TABLE planparameters (
    id SERIAL PRIMARY KEY,
    temperaturesked VARCHAR(100),
    lightsofftime TIMESTAMP
);

-- Create planpattern table
CREATE TABLE planpattern (
    id SERIAL PRIMARY KEY,
    optimalmicroclimate_id INT,
    device VARCHAR(200),
    planparameters_id INT
);

-- Create theme table
CREATE TABLE theme (
    id INT PRIMARY KEY,
    title VARCHAR(255)
);

-- Create topicsinfo table
CREATE TABLE topicsinfo (
    id INT PRIMARY KEY,
    description TEXT,
    type VARCHAR(255),
    info BYTEA
);

-- Create junction table for many-to-many relationship
CREATE TABLE themetopicsinfo (
    theme_id INT,
    topics_info_id INT,
    PRIMARY KEY (theme_id, topics_info_id),
    CONSTRAINT fk_theme FOREIGN KEY (theme_id)
    REFERENCES theme (id),
    CONSTRAINT fk_topics_info FOREIGN KEY (topics_info_id)
    REFERENCES topicsinfo (id)
);

-- Create microclimateplan table
CREATE TABLE microclimateplan (
    id SERIAL PRIMARY KEY,
    planpattern_id INT,
    initiallymicroclimate_id INT,
    user_id INT,
    topic_id INT
);

-- Add foreign key constraints to microclimate
ALTER TABLE microclimate
ADD CONSTRAINT fk_humidity_id
FOREIGN KEY (humidity_id) REFERENCES humidity (id)
ON DELETE CASCADE ON UPDATE CASCADE;

-- Add foreign key constraints to planpattern
ALTER TABLE planpattern
ADD CONSTRAINT fk_optimalmicroclimate_id
FOREIGN KEY (optimalmicroclimate_id) REFERENCES microclimate (id)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE planpattern
ADD CONSTRAINT fk_planparameters_id
FOREIGN KEY (planparameters_id) REFERENCES planparameters (id)
ON DELETE CASCADE ON UPDATE CASCADE;

-- Add foreign key constraints to microclimateplan
ALTER TABLE microclimateplan
ADD CONSTRAINT fk_planpattern_id
FOREIGN KEY (planpattern_id) REFERENCES planpattern (id)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE microclimateplan
ADD CONSTRAINT fk_initiallymicroclimate_id
FOREIGN KEY (initiallymicroclimate_id) REFERENCES microclimate (id)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE microclimateplan
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id) REFERENCES "User" (id)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE microclimateplan
ADD CONSTRAINT fk_topic_id
FOREIGN KEY (topic_id) REFERENCES topicsinfo (id)
ON DELETE CASCADE ON UPDATE CASCADE;
