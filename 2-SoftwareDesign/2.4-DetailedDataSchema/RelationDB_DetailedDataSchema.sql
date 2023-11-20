-- Створення таблиць

-- Таблиця Volohist
CREATE TABLE Volohist (
    ID serial PRIMARY KEY,
    Vidnosna_Volohist numeric CHECK (Vidnosna_Volohist > 0),
    Absolyutna_Volohist numeric CHECK (Absolyutna_Volohist > 0)
);

-- Таблиця Mikroklimat
CREATE TABLE Mikroklimat (
    ID serial PRIMARY KEY,
    Temperatura varchar(20) NOT NULL,
    Ventilyatsiya varchar(150) NULL,
    Rivny_Osvitlenosti numeric CHECK (Rivny_Osvitlenosti > 0) NOT NULL,
    Volohist_ID int
);

-- Таблиця Korysuvach
CREATE TABLE Korysuvach (
    ID serial PRIMARY KEY,
    Imya varchar(300) NOT NULL,
    Prizvyshe varchar(300) NOT NULL
);

-- Таблиця ShablonPlanuStvorennya
CREATE TABLE ShablonPlanuStvorennya (
    ID serial PRIMARY KEY,
    Potichne_Stanovyshche integer,
    Proekt_Optimalnogo_Mikroklimatu json,
    Prysroy varchar(255)
);

-- Таблиця ParametryPlanuStvorennya
CREATE TABLE ParametryPlanuStvorennya (
    ID serial PRIMARY KEY,
    Temperaturnyi_Rezhym varchar(100) CHECK (Temperaturnyi_Rezhym::integer BETWEEN -50 AND 50) NOT NULL,
    Veluchyna_Volohosti integer CHECK (Veluchyna_Volohosti BETWEEN 0 AND 100) NOT NULL,
    Chas_Vklyuchennya_Osvitlennya date NOT NULL
);

-- Таблиця PlanMikroklimatu
CREATE TABLE PlanMikroklimatu (
    ID serial PRIMARY KEY,
    ShablonPlanuStvorennya_ID int,
    Potichne_Stanovyshche integer,
    Spysok_Parametriv text NULL,
    Mikroklimat_ID int,
    ParametryPlanuStvorennya_ID int,
    Korysuvach_ID int
);

-- Створення зовнішніх ключів з on update cascade і on delete cascade

-- Зовнішній ключ для таблиці Mikroklimat
ALTER TABLE Mikroklimat
ADD FOREIGN KEY (Volohist_ID) REFERENCES Volohist(ID) ON UPDATE CASCADE ON DELETE CASCADE;

-- Зовнішні ключі для таблиці PlanMikroklimatu
ALTER TABLE PlanMikroklimatu
ADD FOREIGN KEY (ShablonPlanuStvorennya_ID) REFERENCES ShablonPlanuStvorennya(ID) ON UPDATE CASCADE ON DELETE CASCADE,
ADD FOREIGN KEY (Mikroklimat_ID) REFERENCES Mikroklimat(ID) ON UPDATE CASCADE ON DELETE CASCADE,
ADD FOREIGN KEY (ParametryPlanuStvorennya_ID) REFERENCES ParametryPlanuStvorennya(ID) ON UPDATE CASCADE ON DELETE CASCADE,
ADD FOREIGN KEY (Korysuvach_ID) REFERENCES Korysuvach(ID) ON UPDATE CASCADE ON DELETE CASCADE;
