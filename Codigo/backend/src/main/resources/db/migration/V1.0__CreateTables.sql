CREATE SEQUENCE city_seq;
CREATE TABLE cities (
                        city_id INT DEFAULT NEXTVAL ('city_seq') PRIMARY KEY,
                        city VARCHAR(255) NOT NULL
);

CREATE SEQUENCE states_seq;
CREATE TABLE states (
                        state_id INT DEFAULT NEXTVAL ('states_seq') PRIMARY KEY,
                        state VARCHAR(255) NOT NULL,
                        uf VARCHAR(2) NOT NULL
);

CREATE SEQUENCE address_seq;
CREATE TABLE address(
                        address_id INT DEFAULT NEXTVAL ('address_seq') PRIMARY KEY,
                        street VARCHAR(255) NOT NULL,
                        number INT NOT NULL,
                        district VARCHAR(100) NOT NULL,
                        zip_code VARCHAR(100),
                        city_id INT NOT NULL,
                        state_id INT NOT NULL,

                        created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                        deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                        CONSTRAINT FK_city_id FOREIGN KEY (city_id) REFERENCES cities(city_id),
                        CONSTRAINT FK_state_id FOREIGN KEY (state_id) REFERENCES states(state_id)
);

CREATE SEQUENCE users_seq;
CREATE TABLE users (
                       user_id INT DEFAULT NEXTVAL ('users_seq') PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL unique,
                       password VARCHAR(255) NOT NULL,
                       sex VARCHAR(1) NOT NULL,
                       legal_document VARCHAR(11) NOT NULL,
                       address_id INT NOT NULL,
                       birth_date TIMESTAMP(0) NULL DEFAULT NULL,
                       phone1 VARCHAR(100) NOT NULL,
                       phone2 VARCHAR(100) NOT NULL,

                       created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                       CONSTRAINT FK_address_id FOREIGN KEY (address_id) REFERENCES address(address_id)

);

CREATE SEQUENCE country_code_seq;
CREATE TABLE country_code(
                             country_code_id INT DEFAULT NEXTVAL('country_code_seq') PRIMARY KEY,
                             country_code INT NOT NULL UNIQUE
);

CREATE SEQUENCE phones_seq;
CREATE TABLE phones(
                       phones_id INT DEFAULT NEXTVAL ('phones_seq') PRIMARY KEY,
                       phone VARCHAR(12) NOT NULL,
                       country_code_id INT NOT NULL,
                       user_id int not null,

                       created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                       CONSTRAINT FK_users_id FOREIGN KEY (user_id) REFERENCES users(user_id),
                       CONSTRAINT FK_country_code FOREIGN KEY (country_code_id) REFERENCES country_code(country_code_id)
);


CREATE SEQUENCE roles_seq;
CREATE TABLE roles (
                       roles_id INT DEFAULT NEXTVAL ('roles_seq') PRIMARY KEY,
                       name varchar(45) NOT NULL UNIQUE,

                       created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP(0) NULL DEFAULT NULL
);


CREATE SEQUENCE user_roles_seq;
CREATE TABLE user_roles (
                    user_roles_id INT DEFAULT NEXTVAL ('user_roles_seq') PRIMARY KEY,
                    user_id INT CHECK (user_id > 0) NOT NULL,
                    role_id INT CHECK (role_id > 0) NOT NULL,

                    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                    deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                    CONSTRAINT FK_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
                    CONSTRAINT FK_role_id FOREIGN KEY (role_id) REFERENCES roles(roles_id)
);
CREATE SEQUENCE risk_areas_seq;
CREATE TABLE risk_areas (

                            risk_areas_id INT DEFAULT NEXTVAL ('risk_areas_seq') PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            latitude VARCHAR(50) NOT NULL,
                            longitude VARCHAR(50) NOT NULL,
                            status VARCHAR(20) NOT NULL,
                            description VARCHAR(250) NOT NULL,
                            address_id INT CHECK (address_id > 0),
                            id_creator INT,
                            id_attendant INT,
                            risk_type INT,
                            idAnual VARCHAR(50),
                            riskOfOccurring VARCHAR(50),
                            observationsAfterAttendance VARCHAR(250) NOT NULL,
                            forwardedOfficialNumber VARCHAR(50),
                            destinationEntity VARCHAR(50),
                            interdiction VARCHAR(50),
                            monitoring VARCHAR(50),
                            localizationLink VARCHAR(250),
                            affectedPeopleNumber VARCHAR(250),

                            created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                            deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                            CONSTRAINT FK_address_id FOREIGN KEY (address_id) REFERENCES address(address_id),
                            CONSTRAINT FK_id_creator FOREIGN KEY (id_creator) REFERENCES users(user_id),
                            CONSTRAINT FK_id_attendant FOREIGN KEY (id_attendant) REFERENCES users(user_id)


);

CREATE SEQUENCE risk_areas_images_seq;
CREATE TABLE risk_areas_images (

                                risk_areas_images_id INT DEFAULT NEXTVAL ('risk_areas_images_seq') PRIMARY KEY,
                                risk_areas_id INT CHECK (risk_areas_id > 0) NOT NULL,
                                image VARCHAR(250) NOT NULL,
                                title VARCHAR(250),

                                created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                                deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                                CONSTRAINT FK_risk_areas_id FOREIGN KEY (risk_areas_id) REFERENCES risk_areas(risk_areas_id)


);

CREATE SEQUENCE reports_backups_seq;
CREATE TABLE reports_backups (

                                 reports_backups_id INT DEFAULT NEXTVAL ('reports_backups_seq') PRIMARY KEY,
                                 risk_areas_id INT CHECK (risk_areas_id > 0) NOT NULL,
                                 description VARCHAR(250) NOT NULL,
                                 title VARCHAR(250) NOT NULL,

                                 created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                                 deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                                 CONSTRAINT FK_risk_areas_id FOREIGN KEY (risk_areas_id) REFERENCES risk_areas(risk_areas_id)


);
CREATE SEQUENCE reports_images_seq;
CREATE TABLE reports_images (

                                 reports_images_id INT DEFAULT NEXTVAL ('reports_images_seq') PRIMARY KEY,
                                 reports_backups_id INT CHECK (reports_backups_id > 0) NOT NULL,
                                 image VARCHAR(250) NOT NULL,
                                 title VARCHAR(250),
                                 image_description VARCHAR(250),

                                 created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                                 deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                                 CONSTRAINT FK_reports_backups_id FOREIGN KEY (reports_backups_id) REFERENCES reports_backups(reports_backups_id)


);

CREATE SEQUENCE feedback_service_seq;
CREATE TABLE feedback_service (

                                  feedback_service_id INT DEFAULT NEXTVAL ('feedback_service_seq') PRIMARY KEY,
                                  risk_areas_id INT CHECK (risk_areas_id > 0) NOT NULL,
                                  stars INT,
                                  description VARCHAR(250) NOT NULL,

                                  created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                                  deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                                  CONSTRAINT FK_risk_areas_id FOREIGN KEY (risk_areas_id) REFERENCES risk_areas(risk_areas_id)


);



CREATE SEQUENCE family_registration_seq;
CREATE TABLE family_registration (
                                     family_registration_id INT DEFAULT NEXTVAL ('family_registration_seq') PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
                                     email VARCHAR(100) NOT NULL unique,
                                     password VARCHAR(255) NOT NULL,
                                     address_id INT NOT NULL,
                                     phone1 VARCHAR(100) NOT NULL,
                                     phone2 VARCHAR(100) NOT NULL,
                                     additionalDescriptions VARCHAR(255) NOT NULL,

                                     created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                                     deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                                     CONSTRAINT FK_address_id FOREIGN KEY (address_id) REFERENCES address(address_id)

);


CREATE SEQUENCE family_risks_seq;
CREATE TABLE family_risks(
                             family_risks_id INT DEFAULT NEXTVAL ('family_risks_seq') PRIMARY KEY,
                             family_registration_id INT CHECK (family_registration_id > 0) NOT NULL,
                             risk_areas_id INT CHECK (risk_areas_id > 0) NOT NULL,

                             created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                             deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                             CONSTRAINT FK_risk_areas_id FOREIGN KEY (risk_areas_id) REFERENCES risk_areas(risk_areas_id),
                             CONSTRAINT FK_family_registration_id FOREIGN KEY (family_registration_id) REFERENCES family_registration(family_registration_id)
);


CREATE SEQUENCE family_legal_document_seq;
CREATE TABLE family_legal_document(

                                      family_legal_document_id INT DEFAULT NEXTVAL ('family_legal_document_seq') PRIMARY KEY,
                                      legal_document VARCHAR(11) NOT NULL,
                                      full_name VARCHAR(50),
                                      birth_date VARCHAR(50),
                                      title VARCHAR(50),
                                      family_registration_id INT CHECK (family_registration_id > 0) NOT NULL,

                                      created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                                      deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                                      CONSTRAINT FK_family_registration_id FOREIGN KEY (family_registration_id) REFERENCES family_registration(family_registration_id)
);

CREATE SEQUENCE family_legal_document_images_seq;
CREATE TABLE family_legal_document_images (

                                family_legal_document_images_id INT DEFAULT NEXTVAL ('family_legal_document_images_seq') PRIMARY KEY,
                                family_legal_document_id INT CHECK (family_legal_document_id > 0) NOT NULL,
                                image VARCHAR(250) NOT NULL,
                                title VARCHAR(50),

                                created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                                deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                                CONSTRAINT FK_family_legal_document_id FOREIGN KEY (family_legal_document_id) REFERENCES family_legal_document(family_legal_document_id)


);

CREATE SEQUENCE disabilities_seq;
CREATE TABLE disabilities (
                       disabilities_id INT DEFAULT NEXTVAL ('disabilities_seq') PRIMARY KEY,
                       name varchar(45) NOT NULL UNIQUE,

                       created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP(0) NULL DEFAULT NULL
);

CREATE SEQUENCE family_disabilities_seq;
CREATE TABLE family_disabilities(
                             family_disabilities_id INT DEFAULT NEXTVAL ('family_disabilities_seq') PRIMARY KEY,
                             disabilities_id INT CHECK (disabilities_id > 0) NOT NULL,
                             family_legal_document_id INT CHECK (family_legal_document_id > 0),

                             created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                             deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

                             CONSTRAINT FK_disabilities_id FOREIGN KEY (disabilities_id) REFERENCES disabilities(disabilities_id),
                             CONSTRAINT FK_family_legal_document_id FOREIGN KEY (family_legal_document_id) REFERENCES family_legal_document(family_legal_document_id)
);

CREATE SEQUENCE password_reset_seq;
CREATE TABLE password_reset (

    password_reset_id INT DEFAULT NEXTVAL ('password_reset_seq') PRIMARY KEY,
    user_id INT CHECK (user_id > 0) NOT NULL,
    token VARCHAR(250) NOT NULL,
    expire_at TIMESTAMP(0) DEFAULT NULL,

    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP(0) NULL DEFAULT NULL,

    CONSTRAINT FK_users_id FOREIGN KEY (user_id) REFERENCES users(user_id)


);


CREATE SEQUENCE anual_counter_seq;
CREATE TABLE anual_counter (

                                anual_counter_id INT DEFAULT NEXTVAL ('anual_counter_seq') PRIMARY KEY,
                                actual_count_value INT,
                                actual_year varchar (50),

                                created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                                deleted_at TIMESTAMP(0) NULL DEFAULT NULL


);

