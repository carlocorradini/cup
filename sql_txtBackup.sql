/*-----------------------------------------------------------*/
/*-------------------- CREATING DATABASE --------------------*/
/*-----------------------------------------------------------*/

CREATE TABLE region(
	ID		SERIAL,
	name		varchar(100)    NOT NULL,
	PRIMARY KEY (ID)
);

CREATE TABLE province (
	ID		SERIAL,
	name_long	varchar(100)	NOT NULL,
	name_short	varchar(100)	NOT NULL,
	region_id	INT		NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (region_id) REFERENCES region(ID)

);

CREATE TABLE city (
	ID		SERIAL,
	name		varchar(100)	NOT NULL,
	province_id	INT		NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (province_id) REFERENCES province(ID)

);

CREATE TABLE report (
	ID		SERIAL,
	report_date	timestamp(6)
			without time
			zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ID)
);

CREATE TABLE exam (
	ID		SERIAL,
	name		varchar(100)	NOT NULL,
	price		SMALLINT,
	PRIMARY KEY (ID),
	CHECK (price > 0)
);

CREATE TABLE medicine (
	ID		SERIAL,
	name		varchar(100)	NOT NULL,
	price		SMALLINT,
	PRIMARY KEY (ID),
	CHECK (price > 0)
);

CREATE TABLE report_exam (
	report_id	INT		NOT NULL,
	exam_id		INT		NOT NULL,
	PRIMARY KEY (report_id, exam_id),
	FOREIGN KEY (report_id) REFERENCES report(ID),
	FOREIGN KEY (exam_id)	REFERENCES exam(ID)
);

CREATE TABLE report_medicine (
	report_id	INT		NOT NULL,
	medicine_id	INT		NOT NULL,
	PRIMARY KEY (report_id, medicine_id),
	FOREIGN KEY (report_id) REFERENCES report(ID),
	FOREIGN KEY (medicine_id) REFERENCES medicine(ID)
);

CREATE TABLE person_sex (
	sex		CHAR		NOT NULL,
	PRIMARY KEY (sex)
);

CREATE TABLE person (
	ID		SERIAL,
	name		varchar(100)	NOT NULL,
	surname		varchar(100)	NOT NULL,
	email		varchar(100)	NOT NULL,
	password	varchar(100)	NOT NULL,
	sex		CHAR		NOT NULL,
	birth_date	DATE		NOT NULL,
	birth_city_id	INT		NOT NULL,
	fiscal_code	varchar(16)	NOT NULL,
	city_id		BIGINT		NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (sex) REFERENCES person_sex(sex),
	FOREIGN KEY (birth_city_id) REFERENCES city(ID),
	FOREIGN KEY (city_id) REFERENCES city(ID)
);

CREATE TABLE doctor (
	ID		SERIAL,
	since		timestamp(6)
			without time
			zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ID)
);

CREATE TABLE doctor_specialist (
	ID		SERIAL,
	since		timestamp(6)
			without time
			zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ID)
);

CREATE TABLE is_patient (
	person_id	INT		NOT NULL,
	doctor_id	INT		NOT NULL,
	since		DATE		NOT NULL	DEFAULT CURRENT_DATE,
	PRIMARY KEY (person_id, doctor_id),
	FOREIGN KEY (person_id) REFERENCES person(ID),
	FOREIGN KEY (doctor_id) REFERENCES doctor(ID),
	CHECK (person_id != doctor_id)
);
	

CREATE TABLE prescription_exam (
	ID		SERIAL,
	person_id	INT		NOT NULL,
	doctor_id	INT		NOT NULL,
	exam_id		INT		NOT NULL,
	report_id	INT,
	doctor_specialist_id		BIGINT,
	prescription_date		timestamp(6)
					without time
					zone				DEFAULT CURRENT_TIMESTAMP,
	prescription_date_registration	timestamp(6)
					without time
					zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ID),
	FOREIGN KEY (doctor_id, person_id) REFERENCES is_patient(doctor_id, person_id),
	FOREIGN KEY (exam_id) REFERENCES exam(ID),
	FOREIGN KEY (report_id) REFERENCES report(ID),
	FOREIGN KEY (doctor_specialist_id) REFERENCES doctor_specialist(ID)
);

CREATE TABLE prescription_medicine (
	ID		SERIAL,
	person_id	INT		NOT NULL,
	doctor_id	INT		NOT NULL,
	medicine_id	INT		NOT NULL,
	quantity	SMALLINT	NOT NULL,
	prescription_date		timestamp(6)
					without time
					zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ID),
	FOREIGN KEY (person_id) REFERENCES person(ID),
	FOREIGN KEY (doctor_id) REFERENCES doctor(ID),
	FOREIGN KEY (medicine_id) REFERENCES medicine(ID)
);

CREATE TABLE person_avatar (
	ID		SERIAL,
	person_id	INT		NOT NULL,
	name		varchar(100)	NOT NULL,
	upload		timestamp(6)
			without time
			zone		NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ID),
	FOREIGN KEY (person_id) REFERENCES person(ID)
);