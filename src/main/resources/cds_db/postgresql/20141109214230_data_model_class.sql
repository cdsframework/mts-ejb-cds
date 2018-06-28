--
-- The MTS cds EJB project is the base framework for the CDS Framework Middle Tier Service.
--
-- Copyright (C) 2016 New York City Department of Health and Mental Hygiene, Bureau of Immunization
-- Contributions by HLN Consulting, LLC
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU
-- Lesser General Public License as published by the Free Software Foundation, either version 3 of the
-- License, or (at your option) any later version. You should have received a copy of the GNU Lesser
-- General Public License along with this program. If not, see <http://www.gnu.org/licenses/> for more
-- details.
--
-- The above-named contributors (HLN Consulting, LLC) are also licensed by the New York City
-- Department of Health and Mental Hygiene, Bureau of Immunization to have (without restriction,
-- limitation, and warranty) complete irrevocable access and rights to this project.
--
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; THE
--
-- SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING,
-- BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
-- NONINFRINGEMENT. IN NO EVENT SHALL THE COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE FOR
-- ANY CLAIM, DAMAGES, OR OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN CONNECTION WITH
-- THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
--
-- For more information about this software, see https://www.hln.com/services/open-source/ or send
-- correspondence to ice@hln.com.
--

-- // data_model_element
-- Migration SQL that makes the change goes here.

create table data_model_class (
            class_id varchar(32) primary key,
            model_id varchar(32) not null,
            super_class_id varchar(32),
            list_id varchar(32),
            name varchar(512) not null,
            description varchar(2048),
            class_name varchar(1024) not null,
            class_type varchar(32) not null,
            is_abstract_class boolean,
            last_mod_datetime timestamp not null,
            last_mod_id varchar(32) not null,
            create_datetime timestamp not null,
            create_id varchar(32) not null);

INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('3d252497cbbb36fe9a818ed858b981af', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'CD', 'A CD is a reference to a concept defined in an external code system, terminology, or ontology.', 'org.openvmr.v1_0.schema.CD', 'Concept', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('8c6c775cd67a9f1b78bba47e9ed51156', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'BL', 'BL stands for the values of two-valued logic. A BL value can be either true or false, or may have a nullFlavor.', 'org.openvmr.v1_0.schema.BL', 'Boolean', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a27731f7cec0289f1dd9cd2a842010b5', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'II', 'An identifier that uniquely identifies a thing or object.', 'org.openvmr.v1_0.schema.II', 'Identifier', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('4dd45db4e2a35f6c3a3ecb0617970ce7', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'ST', 'The character string datatype stands for text data, primarily intended for machine processing (e.g., sorting, querying, indexing, etc.) or direct display. Used for names, symbols, presentation and formal expressions.', 'org.openvmr.v1_0.schema.ST', 'String', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('14be041b6fd0231b73f599a5683058f2', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'RelatedEntity', 'The relationship between one Entity and another Entity.', 'org.openvmr.v1_0.schema.RelatedEntity', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('e0ceffb67d245184752da695887f1157', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'Demographics', NULL, 'org.openvmr.v1_0.schema.EvaluatedPerson$Demographics', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('65ec3ccf32c66ca9cf9931d6557df80e', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'EN', 'A word or a combination of words by which a person is known.', 'org.openvmr.v1_0.schema.EN', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('9cfcf6519ec03b629c9f7d5cffbd7d5a', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'TEL', 'A locatable resource of a person that is identified by a URI, such as a web page, a telephone number (voice, fax or some other resource mediated by telecommunication equipment), an e-mail address, or any other locatable resource that can be specified by a URL.', 'org.openvmr.v1_0.schema.TEL', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('7f32be1fe4112fbf6e95452d21a6fdcf', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'Choice', 'Allows only one of the elements to be present within the containing element.', 'null', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('89fe6a9542e3f116b87b52fcdd93d5ea', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'Facility', NULL, 'org.openvmr.v1_0.schema.Facility', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('adfa3ce3cfc2b319c601c867b66df50c', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'AdministrableSubstance', NULL, 'org.openvmr.v1_0.schema.AdministrableSubstance', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('877476db6ee31230b47fd28e5c016164', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'Person', NULL, 'org.openvmr.v1_0.schema.Person', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('928d52a8a3f716b42b2288ea1d60749b', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'Organization', NULL, 'org.openvmr.v1_0.schema.Organization', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('d82d9f378a98dee4c69f5d35568c70a8', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'Sequence', NULL, 'java.util.List', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('1f947078dc926d53867e2fccdf4a35dc', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'BodySite', NULL, 'BodySite', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6aa74fca55b8c1a43004efc2333c882e', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'RelatedClinicalStatementToEntityInRole', NULL, 'RelatedClinicalStatementToEntityInRole', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('848aa36efb3662575185617becebb8c1', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'RelatedClinicalStatement', NULL, 'RelatedClinicalStatement', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('1a4002d2a0efa513af6fca4b946ea6b4', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'XP', 'A part of a name or address. Each part is a character string.', 'org.openvmr.v1_0.schema.XP', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('7a0d5a5756426bd61c68964dac481bf7', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'string', NULL, 'java.lang.String', 'String', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('3f1d39a2d13baf6259546ce642aa78a5', 'cdb46802ca1e8385a2175f566a47b20d', '1a4002d2a0efa513af6fca4b946ea6b4', NULL, 'ADXP', 'A part with a type-tag signifying its role in the address. Typical parts that exist in about every address are street, house number, or post box, postal code, city, country but other roles may be defined regionally, nationally, or on an enterprise level (e.g. in military addresses).', 'org.openvmr.v1_0.schema.ADXP', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('1f5b71df66969966ce0b33c9463a7786', 'cdb46802ca1e8385a2175f566a47b20d', '605ffbaeef31784360d4d0815536f9d3', NULL, 'ObservationBase', 'The abstract base class for an observation, which is the act of recognizing and noting a fact.', 'org.openvmr.v1_0.schema.ObservationBase', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('786a59ede9d1336ebdcf5190cfc869cf', 'cdb46802ca1e8385a2175f566a47b20d', '1f5b71df66969966ce0b33c9463a7786', NULL, 'ObservationResult', 'The findings from an observation.', 'org.openvmr.v1_0.schema.ObservationResult', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('ee18c7734785431ca52a5c7f332404e0', 'cdb46802ca1e8385a2175f566a47b20d', NULL, '7627cc879a5f7da28dd7c13fd20955e3', 'PostalAddressUse', NULL, 'org.openvmr.v1_0.schema.PostalAddressUse', 'Enumeration', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('d2b3f31bf3ce61c270a0da445e9f5352', 'cdb46802ca1e8385a2175f566a47b20d', '80336f5b9928c46e50d04c87dd7c6e44', NULL, 'Entity', 'A physical thing, group of physical things or a collection of physical things.', 'org.openvmr.v1_0.schema.Entity', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('52a382e1c132b173b54f6378c960c0c2', 'cdb46802ca1e8385a2175f566a47b20d', '80336f5b9928c46e50d04c87dd7c6e44', NULL, 'EvaluatedPerson', 'A person who is the subject of evaluation by a CDS system.', 'org.openvmr.v1_0.schema.EvaluatedPerson', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('be14e417df9f0e6e5e4238c0165b44e7', 'cdb46802ca1e8385a2175f566a47b20d', 'b9be3a45f615b4c9de28bc3474d83890', NULL, 'PQ', 'A dimensioned quantity expressing the result of measuring.', 'org.openvmr.v1_0.schema.PQ', 'Quantity', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('c8d6996d50b63f3aff7203388dd1bc76', 'cdb46802ca1e8385a2175f566a47b20d', NULL, 'b6aef07265544ee94a82d2fdb428a167', 'AddressPartType', 'Whether an address part names the street, city, country, postal code, post box, address line 1, etc.
The value of this attribute SHALL be taken from the HL7 AddressPartType code system.', 'org.openvmr.v1_0.schema.AddressPartType', 'Enumeration', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f89c7a73aff266756922d437e0ad8044', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'IVL_TS', 'A set of consecutive values of an ordered base datatype.', 'org.openvmr.v1_0.schema.IVLTS', 'DateRange', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a018d19046886f65187b439cdc519476', 'cdb46802ca1e8385a2175f566a47b20d', '86ff3d2226a4e46a800e795bd46b4799', NULL, 'Specimen', 'A sample of tissue, blood, urine, water, air, etc., taken for the purposes of diagnostic examination or evaluation.', 'org.openvmr.v1_0.schema.Specimen', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('8f7bce3f91c21ae11128d4a454309968', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'AD', 'Mailing and home or office addresses. 

AD is primarily used to communicate data that will allow printing mail labels, or that will allow a person to physically visit that address. The postal address datatype is not supposed to be a container for additional information that might be useful for finding geographic locations (e.g., GPS coordinates) or for performing epidemiological studies. Such additional information should be captured by other, more appropriate data structures.

Addresses are essentially sequences of address parts, but add a "use" code and a valid time range for information about if and when the address can be used for a given purpose.', 'org.openvmr.v1_0.schema.AD', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b8249b6d3f91a1eeada094c3c4ff2f9f', 'cdb46802ca1e8385a2175f566a47b20d', NULL, 'ab8dd755757972e06a668581c4605d87', 'boolean', NULL, 'java.lang.Boolean', 'Boolean', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b9be3a45f615b4c9de28bc3474d83890', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'QTY', 'The quantity datatype is an abstract generalization for all datatypes whose domain values has an order relation (less-or-equal) and where difference is defined in all of the datatype''s totally ordered value subsets.', 'org.openvmr.v1_0.schema.QTY', 'Quantity', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f567f7cbc8d7d5bf3b8b7f04ed9c3528', 'cdb46802ca1e8385a2175f566a47b20d', 'b9be3a45f615b4c9de28bc3474d83890', NULL, 'TS', NULL, 'org.openvmr.v1_0.schema.TS', 'Date', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('8c2d5e726193e26cc7ff685be73c5d6c', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'double', NULL, 'java.lang.Double', 'Numeric', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('faf96176f8986a06aa2cb3b0ba4ab686', 'cdb46802ca1e8385a2175f566a47b20d', '1a4002d2a0efa513af6fca4b946ea6b4', NULL, 'ENXP', 'A part with a type code signifying the role of the part in the whole entity name, and qualifier codes for more detail about the name part type.', 'org.openvmr.v1_0.schema.ENXP', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('c5a494058b3e302d7050b4c199981ef9', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'ObservationValue', 'Actual observed results.  E.g., 6.5 mg/dL, 5.7%.', 'org.openvmr.v1_0.schema.ObservationValue', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('d76e5c8e46692b8813a2f96a9070f628', 'cdb46802ca1e8385a2175f566a47b20d', NULL, '463f870450d83b6cdea7a219a93cb51a', 'EntityNameUse', NULL, 'org.openvmr.v1_0.schema.EntityNameUse', 'Enumeration', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('ec68c69fc006149b734bacbd382a9951', 'cdb46802ca1e8385a2175f566a47b20d', NULL, '1e3dc71e4000e28f8c760cadcd97807b', 'EntityNamePartQualifier', NULL, 'org.openvmr.v1_0.schema.EntityNamePartQualifier', 'Enumeration', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('8fa908eab3dd122a4fd9eb239829b8cc', 'cdb46802ca1e8385a2175f566a47b20d', NULL, 'dc5de08e97d5cd92c0885e72659bfac6', 'EntityNamePartType', NULL, 'org.openvmr.v1_0.schema.EntityNamePartType', 'Enumeration', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('569607c1d5f567415ac0ea566a5e9bcb', 'cdb46802ca1e8385a2175f566a47b20d', NULL, 'f512d66a606eec404452b253acf38b4e', 'TelecommunicationCapability', NULL, 'org.openvmr.v1_0.schema.TelecommunicationCapability', 'Enumeration', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('8d6e7696cf18130d10165a4dd71fc0cd', 'cdb46802ca1e8385a2175f566a47b20d', NULL, 'd813a93e675a74ddd09496237fb53486', 'TelecommunicationAddressUse', 'One or more codes advising system or user which telecommunication address in a set of like addresses to select for a given telecommunication need.', 'org.openvmr.v1_0.schema.TelecommunicationAddressUse', 'Enumeration', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('9d73b57dd4af1af9b8dab7d78ca672bc', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'ClinicalStatements', 'All ClinicalStatements for one EvaluatedPerson are placed in this element.', 'org.openvmr.v1_0.schema.ClinicalStatements', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('4c874d90ca32414669f3c6b413af6aef', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'Problems', NULL, 'Problems', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('acc1b077ca0442303cf65acf46ab89d2', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'ObservationResults', NULL, 'org.openvmr.v1_0.schema.ObservationResults', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('605ffbaeef31784360d4d0815536f9d3', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'ClinicalStatement', 'A record of something of clinical relevance that is being done, has been done, can be done, or is intended or requested to be done.', 'org.openvmr.v1_0.schema.ClinicalStatement', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('5866739f81146e48185212484298fc88', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'OtherEvaluatedPersons', 'All EvaluatedPersons should be represented at this level rather than being nested inside other evaluated persons.', 'org.openvmr.v1_0.schema.OtherEvaluatedPersons', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('3a624bf5e2516a93a4a3d4584f8c3924', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'EvaluatedPersonRelationships', 'Relationships among evaluated persons.', 'org.openvmr.v1_0.schema.EvaluatedPersonRelationships', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6d0ae0175512695e377445676e01a5f3', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'EntityRelationship', NULL, 'org.openvmr.v1_0.schema.EntityRelationship', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('9847ed1273ec1e62b74998aa57c24d06', 'cdb46802ca1e8385a2175f566a47b20d', 'bb129f3586ae5397f3fc5e586c8bf0a0', NULL, 'Problem', 'An assertion regarding a clinical condition of the subject that needs to be treated or managed.', 'org.openvmr.v1_0.schema.Problem', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('bb129f3586ae5397f3fc5e586c8bf0a0', 'cdb46802ca1e8385a2175f566a47b20d', '605ffbaeef31784360d4d0815536f9d3', NULL, 'ProblemBase', NULL, 'org.openvmr.v1_0.schema.ProblemBase', 'Complex', true, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('80336f5b9928c46e50d04c87dd7c6e44', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'EntityBase', 'Abstract base class for a physical thing, group of physical things, an organization, etc. ', 'org.openvmr.v1_0.schema.EntityBase', 'Complex', true, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('86ff3d2226a4e46a800e795bd46b4799', 'cdb46802ca1e8385a2175f566a47b20d', '80336f5b9928c46e50d04c87dd7c6e44', NULL, 'SpecimenSimple', 'A sample of tissue, blood, urine, water, air, etc., taken for the purposes of diagnostic examination or evaluation.', 'org.openvmr.v1_0.schema.SpecimenSimple', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('bd73194686dec4e7605e93cafd09201f', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'VMR', 'A virtual medical record (vMR) contains data about a patient relevant for CDS, either with regard to the data used for generating inferences (input) or the conclusions reached as a result of analyzing the data (output).  A vMR may contain, for example, problems and medications or CDS-generated assessments and recommended actions.  Note that CDS-generated assessments and recommended actions would typically be considered a CDS output but could also be used as a CDS input as well (e.g., prior CDS system recommendations could influence current CDS system recommendations).

This model does allow for the presence of data belonging to related persons (such as in the case of family history, or public health infectious disease cases) for a single patient.  These related persons are modeled as EvaluatedPersons who have associated ClinicalStatements.  Note that this model is not designed to be a data model for providing CDS for a large population.

Note that enumerations and value domains are anticipated to be specified in profiles in additional ballots.', 'org.openvmr.v1_0.schema.VMR', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6799cb0c3e20d0539bb5c0182f0734bf', 'cdb46802ca1e8385a2175f566a47b20d', '605ffbaeef31784360d4d0815536f9d3', NULL, 'SubstanceAdministrationBase', NULL, 'org.openvmr.v1_0.schema.SubstanceAdministrationBase', 'Complex', true, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('99bebd421bd7800e407a9a7d956ff3f6', 'cdb46802ca1e8385a2175f566a47b20d', NULL, NULL, 'ObservationOrders', 'Observation Orders', 'org.openvmr.v1_0.schema.ObservationOrders', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, super_class_id, list_id, name, description, class_name, class_type, is_abstract_class, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('14815f7fcbd253b970214bdd4f048c9d', 'cdb46802ca1e8385a2175f566a47b20d', '1f5b71df66969966ce0b33c9463a7786', NULL, 'ObservationOrder', 'Observation Order', 'org.openvmr.v1_0.schema.ObservationOrder', 'Complex', false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- run these last since this table is self referencing

alter table data_model_class add constraint un_dmc_name_model_id unique (name, model_id);

alter table data_model_class add constraint fk_dmc_mid_2_dm_mid foreign key (model_id) references data_model (model_id);

alter table data_model_class add constraint fk_dmc_pcid_2_dmc_cid foreign key (super_class_id) references data_model_class (class_id);

alter table data_model_class add constraint fk_dmc_lid_2_cl_lid foreign key (list_id) references cds_list (list_id);

-- //@UNDO
-- SQL to undo the change goes here.

drop table data_model_class;
