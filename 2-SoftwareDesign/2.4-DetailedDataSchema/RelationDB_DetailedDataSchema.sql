-- Create User table
CREATE TABLE "User" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(359),
    surname VARCHAR(350),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);

-- Create Microclimate table
CREATE TABLE Microclimate (
    id SERIAL PRIMARY KEY,
    temperature VARCHAR(20),
    ventilation VARCHAR(150),
    lightLevel NUMERIC CHECK (lightLevel > 0),
    humidity_id INT
);

-- Create Humidity table
CREATE TABLE Humidity (
    id SERIAL PRIMARY KEY,
    relativeHumidity NUMERIC CHECK (relativeHumidity > 0),
    absoluteHumidity NUMERIC CHECK (absoluteHumidity > 0)
);

-- Create PlanParameters table
CREATE TABLE PlanParameters (
    id SERIAL PRIMARY KEY,
    temperatureSked VARCHAR(100),
    lightsOffTime TIMESTAMP
);

-- Create PlanPattern table
CREATE TABLE PlanPattern (
    id SERIAL PRIMARY KEY,
    optimalMicroclimate_id INT,
    device VARCHAR(200),
    planParameters_id INT
);

-- Create MicroclimatePlan table
CREATE TABLE MicroclimatePlan (
    id SERIAL PRIMARY KEY,
    planPattern_id INT,
    initiallyMicroclimate_id INT,
    user_id INT
);

-- Add foreign key constraints
ALTER TABLE Microclimate
ADD CONSTRAINT fk_humidity_id
FOREIGN KEY (humidity_id) REFERENCES Humidity(id);

ALTER TABLE PlanPattern
ADD CONSTRAINT fk_optimalMicroclimate_id
FOREIGN KEY (optimalMicroclimate_id) REFERENCES Microclimate(id);

ALTER TABLE PlanPattern
ADD CONSTRAINT fk_planParameters_id
FOREIGN KEY (planParameters_id) REFERENCES PlanParameters(id);

ALTER TABLE MicroclimatePlan
ADD CONSTRAINT fk_planPattern_id
FOREIGN KEY (planPattern_id) REFERENCES PlanPattern(id);

ALTER TABLE MicroclimatePlan
ADD CONSTRAINT fk_initiallyMicroclimate_id
FOREIGN KEY (initiallyMicroclimate_id) REFERENCES Microclimate(id);

ALTER TABLE MicroclimatePlan
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id) REFERENCES "User"(id);
