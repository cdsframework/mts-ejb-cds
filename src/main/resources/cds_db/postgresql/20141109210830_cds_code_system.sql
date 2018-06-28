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

-- // cds_code_system
-- Migration SQL that makes the change goes here.

create table cds_code_system (
            code_system_id varchar(32) primary key,
            oid varchar(256) not null,
            name varchar(256),
            last_mod_datetime timestamp not null,
            last_mod_id varchar(32) not null,
            create_datetime timestamp not null,
            create_id varchar(32) not null);

alter table cds_code_system add constraint uncdscsn unique (name);

alter table cds_code_system add constraint un_cds_code_system_oid unique (oid);

INSERT INTO cds_code_system (code_system_id, oid, name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('2b4c99853b45d76f572ef1c6ac339901', '2.16.840.1.113883.5.1', 'GENDER', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code_system (code_system_id, oid, name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('2c9ee278e930742dd9753e81933bae79', '2.16.840.1.113883.6.100', 'ISO 639-2 Language (Three Alpha Character)', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code_system (code_system_id, oid, name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('3d922f54ce6043c26e2d94b97db12fc6', '2.16.840.1.113883.6.99', 'ISO 639-1 Language (Two Alpha Character)', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code_system (code_system_id, oid, name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('734d727366047ef1b6292d310d36b657', '2.16.840.1.113883.6.103', 'ICD9CM Diagnosis', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code_system (code_system_id, oid, name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('32494faa861cef4360359942ecf772da', '2.16.840.1.113883.6.1', 'LOINC', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code_system (code_system_id, oid, name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('65810edd45ecf7b39a43f62eb2715feb', '2.16.840.1.113883.6.96', 'SNOMED_CT', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code_system (code_system_id, oid, name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f47e38fdcc2340ae7282963bf26c26ca', '2.16.840.1.113883.6.60', 'MANUFACTURER', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code_system (code_system_id, oid, name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a4a8f1103051fffb35a8f65bd5ed3270', '2.16.840.1.113883.6.3', 'ICD 10', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code_system (code_system_id, oid, name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('d183b1b0c2add222afd18842ea35b1d8', '2.16.840.1.113883.6.90', 'ICD-10-CM Diagnosis', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

drop table cds_code_system;
