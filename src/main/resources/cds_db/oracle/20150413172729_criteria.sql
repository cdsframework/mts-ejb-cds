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

-- // criteria
-- Migration SQL that makes the change goes here.

CREATE TABLE criteria (
        criteria_id VARCHAR(32) primary key,
        code VARCHAR(32) NOT NULL,
        description VARCHAR(512) NOT NULL,
        criteria_type VARCHAR(32) NOT NULL,
        method VARCHAR(32) NOT NULL,
        LAST_MOD_ID VARCHAR(32) NOT NULL,
        LAST_MOD_DATETIME timestamp NOT NULL,
        CREATE_ID VARCHAR(32) NOT NULL,
        CREATE_DATETIME timestamp NOT NULL);

alter table criteria add constraint un_rckms_criteria_desc unique (description);
alter table criteria add constraint un_rckms_criteria_code unique (code);

INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('9fce4c325c18d7b6679b1daf26d5559f', 'APNEA', 'Apnea', 'CLINICAL', 'SYMPTOM', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('2ee51c0825997fb7c7c35aba25cafeb7', 'COUGH_ANY_DUR', 'Cough (any duration)', 'CLINICAL', 'SYMPTOM', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('b53fc7e0e3650a5170f694a7329badaf', 'COUGH_2W_DUR', 'Cough > 2 weeks duration', 'CLINICAL', 'SYMPTOM', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('85f191d4de65707278cb7161b59a3dd3', 'DIAG_PERTUSSIS', 'Healthcare record contains a diagnosis of pertussis', 'CLINICAL', 'DIAGNOSIS', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('e8daa9baa9037917716f0ffe82867821', 'INSPIRA_WHOOP', 'Inspiratory whoop', 'CLINICAL', 'SYMPTOM', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('b9c1a24fe494d65fab3b1823db37b23a', 'PAROX_COUGH', 'Paroxismal cough', 'CLINICAL', 'SYMPTOM', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('365885822c5ce808f7fa43ff1c9d2738', 'POST_TUSS_VOMIT', 'Post-tussive vomiting', 'CLINICAL', 'SYMPTOM', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('598fd25ecb5dc2e150deac005e96515c', 'PERTUSSIS_ON_DR', 'Pertussis Documented on Death Record', 'DEATH_RECORD', 'CONTRIBUTING_TO_DEATH', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('276c34db9ad097245e90420738864bb8', 'GT_ET_2Y', '< = 2 years of age', 'DEMOGRAPHIC', 'SYMPTOM', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('d1b025d98183db234ed765d1230aa6be', 'CONTACT_PERTUSSIS', 'Contact with a laboratory-confirmed pertussis case', 'EPIDEMIOLOGIC', 'CONTACT', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('dad1b6241915e247627b03f52db145bb', 'MEMBER_RISK_GROUP', 'Member of a defined risk group during an outbreak', 'EPIDEMIOLOGIC', 'RISK_GROUP', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria (criteria_id, code, description, criteria_type, method, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('a5cf57f4d67d50a3f18db87853cbbfc1', 'ANY_LAB_TEST_POS', 'Any lab test ''positive'' for B. pertussis infection', 'LABORATORY', 'TEST', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

-- //@UNDO
-- SQL to undo the change goes here.

drop table criteria;
