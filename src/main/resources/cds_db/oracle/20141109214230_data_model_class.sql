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
            name varchar(512) not null,
            description varchar(2048),
            class_name varchar(1024) not null,
            class_type varchar(32) not null,
            last_mod_datetime timestamp not null,
            last_mod_id varchar(32) not null,
            create_datetime timestamp not null,
            create_id varchar(32) not null);

alter table data_model_class add constraint un_dmc_name_model_id unique (name, model_id);

alter table data_model_class add constraint fk_dmc_mid_2_dm_mid foreign key (model_id) references data_model (model_id);

INSERT INTO data_model_class (class_id, model_id, name, description, class_name, class_type, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f89c7a73aff266756922d437e0ad8044', 'cdb46802ca1e8385a2175f566a47b20d', 'IVL_TS', 'A set of consecutive values of an ordered base datatype.', 'org.openvmr.v1_0.schema.IVLTS', 'Range', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, name, description, class_name, class_type, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('3d252497cbbb36fe9a818ed858b981af', 'cdb46802ca1e8385a2175f566a47b20d', 'CD', 'A CD is a reference to a concept defined in an external code system, terminology, or ontology.', 'org.openvmr.v1_0.schema.CD', 'Concept', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, name, description, class_name, class_type, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('be14e417df9f0e6e5e4238c0165b44e7', 'cdb46802ca1e8385a2175f566a47b20d', 'PQ', 'A dimensioned quantity expressing the result of measuring.', 'org.openvmr.v1_0.schema.PQ', 'Quantity', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, name, description, class_name, class_type, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('8c6c775cd67a9f1b78bba47e9ed51156', 'cdb46802ca1e8385a2175f566a47b20d', 'BL', 'BL stands for the values of two-valued logic. A BL value can be either true or false, or may have a nullFlavor.', 'org.openvmr.v1_0.schema.BL', 'Boolean', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, name, description, class_name, class_type, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a27731f7cec0289f1dd9cd2a842010b5', 'cdb46802ca1e8385a2175f566a47b20d', 'II', 'An identifier that uniquely identifies a thing or object.', 'org.openvmr.v1_0.schema.II', 'Identifier', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, name, description, class_name, class_type, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('4dd45db4e2a35f6c3a3ecb0617970ce7', 'cdb46802ca1e8385a2175f566a47b20d', 'ST', 'The character string datatype stands for text data, primarily intended for machine processing (e.g., sorting, querying, indexing, etc.) or direct display. Used for names, symbols, presentation and formal expressions.', 'org.openvmr.v1_0.schema.ST', 'String', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, name, description, class_name, class_type, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a018d19046886f65187b439cdc519476', 'cdb46802ca1e8385a2175f566a47b20d', 'Specimen', 'A sample of tissue, blood, urine, water, air, etc., taken for the purposes of diagnostic examination or evaluation.', 'org.openvmr.v1_0.schema.Specimen', 'Complex', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, name, description, class_name, class_type, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('d2b3f31bf3ce61c270a0da445e9f5352', 'cdb46802ca1e8385a2175f566a47b20d', 'Entity', 'A physical thing, group of physical things or a collection of physical things.', 'org.openvmr.v1_0.schema.Entity', 'Complex', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, name, description, class_name, class_type, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('14be041b6fd0231b73f599a5683058f2', 'cdb46802ca1e8385a2175f566a47b20d', 'RelatedEntity', 'The relationship between one Entity and another Entity.', 'org.openvmr.v1_0.schema.RelatedEntity', 'Complex', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, name, description, class_name, class_type, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('786a59ede9d1336ebdcf5190cfc869cf', 'cdb46802ca1e8385a2175f566a47b20d', 'ObservationResult', 'The findings from an observation.', 'org.openvmr.v1_0.schema.ObservationResult', 'Complex', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_class (class_id, model_id, name, description, class_name, class_type, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('9847ed1273ec1e62b74998aa57c24d06', 'cdb46802ca1e8385a2175f566a47b20d', 'Problem', 'An assertion regarding a clinical condition of the subject that needs to be treated or managed.', 'org.openvmr.v1_0.schema.Problem', 'Complex', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

drop table data_model_class;
