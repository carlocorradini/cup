/*-----------------------------------------------------------*/
/*-------------------- CREATING DATABASE --------------------*/
/*-----------------------------------------------------------*/

CREATE DATABASE cup
OWNER			= postgres
ENCODING		= 'UTF8'
LC_COLLATE		= 'Italian_Italy.1252'
LC_CTYPE		= 'Italian_Italy.1252'
TABLESPACE		= pg_default
CONNECTION LIMIT	= -1

CREATE TABLE region (
	id		SERIAL,
	name		varchar(100)    NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE province (
	id		SERIAL,
	name_long	varchar(100)	NOT NULL,
	name_short	varchar(100)	NOT NULL,
	region_id	INT		NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (region_id) REFERENCES region(id)

);

CREATE TABLE city (
	id		SERIAL,
	name		varchar(100)	NOT NULL,
	province_id	INT		NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (province_id) REFERENCES province(id)

);

CREATE TABLE report (
	id		SERIAL,
	report_date	timestamp(6)
			without time
			zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

CREATE TABLE exam (
	id		SERIAL,
	name		varchar(100)	NOT NULL,
	price		SMALLINT,
	PRIMARY KEY (id),
	CHECK (price > 0)
);

CREATE TABLE medicine (
	id		SERIAL,
	name		varchar(100)	NOT NULL,
	price		SMALLINT,
	PRIMARY KEY (id),
	CHECK (price > 0)
);

CREATE TABLE report_exam (
	report_id	INT		NOT NULL,
	exam_id		INT		NOT NULL,
	PRIMARY KEY (report_id, exam_id),
	FOREIGN KEY (report_id) REFERENCES report(id),
	FOREIGN KEY (exam_id)	REFERENCES exam(id)
);

CREATE TABLE report_medicine (
	report_id	INT		NOT NULL,
	medicine_id	INT		NOT NULL,
	PRIMARY KEY (report_id, medicine_id),
	FOREIGN KEY (report_id) REFERENCES report(id),
	FOREIGN KEY (medicine_id) REFERENCES medicine(id)
);

CREATE TABLE person_sex (
	sex		CHAR		NOT NULL,
	PRIMARY KEY (sex)
);

CREATE TABLE person (
	id		SERIAL,
	name		varchar(100)	NOT NULL,
	surname		varchar(100)	NOT NULL,
	email		varchar(100)	NOT NULL,
	password	varchar(100)	NOT NULL,
	sex		CHAR		NOT NULL,
	birth_date	DATE		NOT NULL,
	birth_city_id	INT		NOT NULL,
	fiscal_code	varchar(16)	NOT NULL,
	city_id		BIGINT		NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (sex) REFERENCES person_sex(sex),
	FOREIGN KEY (birth_city_id) REFERENCES city(id),
	FOREIGN KEY (city_id) REFERENCES city(id)
);

CREATE TABLE doctor (
	id		SERIAL,
	since		timestamp(6)
			without time
			zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

CREATE TABLE doctor_specialist (
	id		SERIAL,
	since		timestamp(6)
			without time
			zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

CREATE TABLE is_patient (
	person_id	INT		NOT NULL,
	doctor_id	INT		NOT NULL,
	since		DATE		timestamp(6)
					without time
					zone		DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (person_id, doctor_id),
	FOREIGN KEY (person_id) REFERENCES person(id),
	FOREIGN KEY (doctor_id) REFERENCES doctor(id),
	CHECK (person_id != doctor_id)
);
	

CREATE TABLE prescription_exam (
	id		SERIAL,
	person_id	INT		NOT NULL,
	doctor_id	INT		NOT NULL,
	exam_id		INT		NOT NULL,
	report_id	INT,
	doctor_specialist_id		BIGINT,
	paid 		BOOLEAN		NOT NULL	DEFAULT FALSE,
	prescription_date		timestamp(6)
					without time
					zone				DEFAULT CURRENT_TIMESTAMP,
	prescription_date_registration	timestamp(6)
					without time
					zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id),
	FOREIGN KEY (doctor_id, person_id) REFERENCES is_patient(doctor_id, person_id),
	FOREIGN KEY (exam_id) REFERENCES exam(id),
	FOREIGN KEY (report_id) REFERENCES report(id),
	FOREIGN KEY (doctor_specialist_id) REFERENCES doctor_specialist(id)
);

CREATE TABLE prescription_medicine (
	id		SERIAL,
	person_id	INT		NOT NULL,
	doctor_id	INT		NOT NULL,
	medicine_id	INT		NOT NULL,
	quantity	SMALLINT	NOT NULL,
	paid		BOOLEAN		NOT NULL	DEFAULT FALSE
	prescription_date		timestamp(6)
					without time
					zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id),
	FOREIGN KEY (person_id) REFERENCES person(id),
	FOREIGN KEY (doctor_id) REFERENCES doctor(id),
	FOREIGN KEY (medicine_id) REFERENCES medicine(id)
);

CREATE TABLE person_avatar (
	id		SERIAL,
	person_id	INT		NOT NULL,
	name		varchar(100)	NOT NULL,
	upload		timestamp(6)
			without time
			zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id),
	FOREIGN KEY (person_id) REFERENCES person(id)
);



/*-----------------------------------------------------------*/
/*------------------- POPULATING DATABASE -------------------*/
/*-----------------------------------------------------------*/

INSERT INTO exam(name, price) VALUES
	('estrazione di dente deciduo'	,			318),
	('tomoscintigrafia globale'	,			5015),
	('ablazione tartaro'		,			2318);

INSERT INTO medicine(name, price) VALUES
	('Abba'				,			258),
	('Abstral'			,			189),
	('Artrotec'			,			249),
	('Asamax'			,			170);

INSERT INTO person_sex(sex) VALUES
	('M'),
	('F');

INSERT INTO region(name) VALUES
	('Abruzzo'),
	('Basilicata'),
	('Calabria'),
	('Campania'),
	('Emilia-Romagna'),
	('Friuli-Venezia-Giulia'),
	('Lazio'),
	('Liguria'),
	('Lombardia'),
	('Marche'),
	('Molise'),
	('Piemonte'),
	('Puglia'),
	('Sardegna'),
	('Sicilia'),
	('Toscana'),
	('Trentino-Alto Adige'),
	('Umbria'),
	('Valle d''Aosta'),
	('Veneto');

INSERT INTO province(name_long, name_short, region_id) VALUES
	('Chieti'			,'CH'	, (SELECT id FROM region WHERE name='Abruzzo')),
	('L''Aquila'			,'AQ'	, (SELECT id FROM region WHERE name='Abruzzo')),
	('Pescara'			,'PE'	, (SELECT id FROM region WHERE name='Abruzzo')),
	('Teramo'			,'TE'	, (SELECT id FROM region WHERE name='Abruzzo')),
	('Matera'			,'MT'	, (SELECT id FROM region WHERE name='Basilicata')),
	('Potenza'			,'PZ'	, (SELECT id FROM region WHERE name='Basilicata')),
	('Catanzaro'			,'CZ'	, (SELECT id FROM region WHERE name='Calabria')),
	('Cosenza'			,'CS'	, (SELECT id FROM region WHERE name='Calabria')),
	('Crotone'			,'KR'	, (SELECT id FROM region WHERE name='Calabria')),
	('Reggio Calabria'		,'RC'	, (SELECT id FROM region WHERE name='Calabria')),
	('Vibo Valentia'		,'VV'	, (SELECT id FROM region WHERE name='Calabria')),
	('Avellino'			,'AV'	, (SELECT id FROM region WHERE name='Campania')),
	('Benevento'			,'BN'	, (SELECT id FROM region WHERE name='Campania')),
	('Caserta'			,'CE'	, (SELECT id FROM region WHERE name='Campania')),
	('Napoli'			,'NA'	, (SELECT id FROM region WHERE name='Campania')),
	('Salerno'			,'SA'	, (SELECT id FROM region WHERE name='Campania')),
	('Bologna'			,'BO'	, (SELECT id FROM region WHERE name='Emilia-Romagna')),
	('Ferrara'			,'FE'	, (SELECT id FROM region WHERE name='Emilia-Romagna')),
	('Forlì-Cesena'			,'FC'	, (SELECT id FROM region WHERE name='Emilia-Romagna')),
	('Modena'			,'MO'	, (SELECT id FROM region WHERE name='Emilia-Romagna')),
	('Parma'			,'PR'	, (SELECT id FROM region WHERE name='Emilia-Romagna')),
	('Piacenza'			,'PC'	, (SELECT id FROM region WHERE name='Emilia-Romagna')),
	('Ravenna'			,'RA'	, (SELECT id FROM region WHERE name='Emilia-Romagna')),
	('Reggio Emilia'		,'RE'	, (SELECT id FROM region WHERE name='Emilia-Romagna')),
	('Rimini'			,'RN'	, (SELECT id FROM region WHERE name='Emilia-Romagna')),
	('Gorizia'			,'GO'	, (SELECT id FROM region WHERE name='Friuli-Venezia-Giulia')),
	('Pordenone'			,'PN'	, (SELECT id FROM region WHERE name='Friuli-Venezia-Giulia')),
	('Trieste'			,'TS'	, (SELECT id FROM region WHERE name='Friuli-Venezia-Giulia')),
	('Udine'			,'UD'	, (SELECT id FROM region WHERE name='Friuli-Venezia-Giulia')),
	('Frosinone'			,'FR'	, (SELECT id FROM region WHERE name='Lazio')),
	('Latina'			,'LT'	, (SELECT id FROM region WHERE name='Lazio')),
	('Rieti'			,'RI'	, (SELECT id FROM region WHERE name='Lazio')),
	('Roma'				,'RM'	, (SELECT id FROM region WHERE name='Lazio')),
	('Viterbo'			,'VT'	, (SELECT id FROM region WHERE name='Lazio')),
	('Genova'			,'GE'	, (SELECT id FROM region WHERE name='Liguria')),
	('Imperia'			,'IM'	, (SELECT id FROM region WHERE name='Liguria')),
	('La Spezia'			,'SP'	, (SELECT id FROM region WHERE name='Liguria')),
	('Savona'			,'SV'	, (SELECT id FROM region WHERE name='Liguria')),
	('Bergamo'			,'BG'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Brescia'			,'BS'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Como'				,'CO'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Cremona'			,'CR'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Lecco'			,'LC'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Lodi'				,'LO'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Mantova'			,'MN'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Milano'			,'MI'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Monza e Brianza'		,'MB'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Pavia'			,'PV'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Sondrio'			,'SO'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Varese'			,'VA'	, (SELECT id FROM region WHERE name='Lombardia')),
	('Ancona'			,'AN'	, (SELECT id FROM region WHERE name='Marche')),
	('Ascoli Piceno'		,'AP'	, (SELECT id FROM region WHERE name='Marche')),
	('Fermo'			,'FM'	, (SELECT id FROM region WHERE name='Marche')),
	('Macerata'			,'MC'	, (SELECT id FROM region WHERE name='Marche')),
	('Pesaro e Urbino'		,'PU'	, (SELECT id FROM region WHERE name='Marche')),
	('Campobasso'			,'CB'	, (SELECT id FROM region WHERE name='Molise')),
	('Isernia'			,'IS'	, (SELECT id FROM region WHERE name='Molise')),
	('Alessandria'			,'AL'	, (SELECT id FROM region WHERE name='Piemonte')),
	('Asti'				,'AT'	, (SELECT id FROM region WHERE name='Piemonte')),
	('Biella'			,'BI'	, (SELECT id FROM region WHERE name='Piemonte')),
	('Cuneo'			,'CN'	, (SELECT id FROM region WHERE name='Piemonte')),
	('Novara'			,'NO'	, (SELECT id FROM region WHERE name='Piemonte')),
	('Torino'			,'TO'	, (SELECT id FROM region WHERE name='Piemonte')),
	('Verbano Cuio Ossola'		,'VB'	, (SELECT id FROM region WHERE name='Piemonte')),
	('Vercelli'			,'VC'	, (SELECT id FROM region WHERE name='Piemonte')),
	('Bari'				,'BA'	, (SELECT id FROM region WHERE name='Puglia')),
	('Barletta-Andria-Trani'	,'BT'	, (SELECT id FROM region WHERE name='Puglia')),
	('Brindisi'			,'BR'	, (SELECT id FROM region WHERE name='Puglia')),
	('Lecce'			,'LE'	, (SELECT id FROM region WHERE name='Puglia')),
	('Foggia'			,'FG'	, (SELECT id FROM region WHERE name='Puglia')),
	('Taranto'			,'TA'	, (SELECT id FROM region WHERE name='Puglia')),
	('Cagliari'			,'CA'	, (SELECT id FROM region WHERE name='Sardegna')),
	('Carbonia-Iglesias'		,'CI'	, (SELECT id FROM region WHERE name='Sardegna')),
	('Medio Campidano'		,'VS'	, (SELECT id FROM region WHERE name='Sardegna')),
	('Nuoro'			,'NU'	, (SELECT id FROM region WHERE name='Sardegna')),
	('Ogliastra'			,'OG'	, (SELECT id FROM region WHERE name='Sardegna')),
	('Olbia-Tempio'			,'OT'	, (SELECT id FROM region WHERE name='Sardegna')),
	('Oristano'			,'OR'	, (SELECT id FROM region WHERE name='Sardegna')),
	('Sassari'			,'SS'	, (SELECT id FROM region WHERE name='Sardegna')),
	('Agrigento'			,'AG'	, (SELECT id FROM region WHERE name='Sicilia')),
	('Caltanissetta'		,'CL'	, (SELECT id FROM region WHERE name='Sicilia')),
	('Catania'			,'CT'	, (SELECT id FROM region WHERE name='Sicilia')),
	('Enna'				,'EN'	, (SELECT id FROM region WHERE name='Sicilia')),
	('Messina'			,'ME'	, (SELECT id FROM region WHERE name='Sicilia')),
	('Palermo'			,'PA'	, (SELECT id FROM region WHERE name='Sicilia')),
	('Ragusa'			,'RG'	, (SELECT id FROM region WHERE name='Sicilia')),
	('Siracusa'			,'SR'	, (SELECT id FROM region WHERE name='Sicilia')),
	('Trapani'			,'TP'	, (SELECT id FROM region WHERE name='Sicilia')),
	('Arezzo'			,'AR'	, (SELECT id FROM region WHERE name='Toscana')),
	('Firenze'			,'FI'	, (SELECT id FROM region WHERE name='Toscana')),
	('Grosseto'			,'GR'	, (SELECT id FROM region WHERE name='Toscana')),
	('Livorno'			,'LI'	, (SELECT id FROM region WHERE name='Toscana')),
	('Lucca'			,'LU'	, (SELECT id FROM region WHERE name='Toscana')),
	('Massa e Carrara'		,'MS'	, (SELECT id FROM region WHERE name='Toscana')),
	('Pisa'				,'PI'	, (SELECT id FROM region WHERE name='Toscana')),
	('Pistoia'			,'PT'	, (SELECT id FROM region WHERE name='Toscana')),
	('Prato'			,'PO'	, (SELECT id FROM region WHERE name='Toscana')),
	('Siena'			,'SI'	, (SELECT id FROM region WHERE name='Toscana')),
	('Bolzano'			,'BZ'	, (SELECT id FROM region WHERE name='Trentino-Alto Adige')),
	('Trento'			,'TN'	, (SELECT id FROM region WHERE name='Trentino-Alto Adige')),
	('Perugia'			,'PG'	, (SELECT id FROM region WHERE name='Umbria')),
	('Terni'			,'TR'	, (SELECT id FROM region WHERE name='Umbria')),
	('Aosta'			,'AO'	, (SELECT id FROM region WHERE name='Valle d''Aosta')),
	('Belluno'			,'BL'	, (SELECT id FROM region WHERE name='Veneto')),
	('Padova'			,'PD'	, (SELECT id FROM region WHERE name='Veneto')),
	('Rovigo'			,'RO'	, (SELECT id FROM region WHERE name='Veneto')),
	('Treviso'			,'TV'	, (SELECT id FROM region WHERE name='Veneto')),
	('Venezia'			,'VE'	, (SELECT id FROM region WHERE name='Veneto')),
	('Verona'			,'VR'	, (SELECT id FROM region WHERE name='Veneto')),
	('Vicenza'			,'VI'	, (SELECT id FROM region WHERE name='Veneto'));