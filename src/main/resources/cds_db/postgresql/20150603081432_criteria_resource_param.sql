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

-- // criteria_resource_param
-- Migration SQL that makes the change goes here.

CREATE TABLE criteria_resource_param (
            param_id varchar(32) primary key,
            resource_id varchar(32) not null,
            class_type varchar(32) not null,
            name varchar(512) not null,
            description varchar(2048),
            LAST_MOD_ID varchar(32) not null,
            LAST_MOD_DATETIME timestamp not null,
            CREATE_ID varchar(32) not null,
            CREATE_DATETIME timestamp not null);

alter table criteria_resource_param add constraint fk_crp_rid_2_cr_rid foreign key (resource_id) references criteria_resource (resource_id);

INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('334e0dc2384404c34d356c5c12235da0', '8bb2cadd277c0a9746331b3d5c13e950', 'String', 'intervalValue', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('39baed9db851c51d17e4172f9b815d3a', '8bb2cadd277c0a9746331b3d5c13e950', 'DateRange', 'interval2', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('ed66c2b9ce2ac9211f48ad1ff584bb66', '8bb2cadd277c0a9746331b3d5c13e950', 'DateRange', 'interval1', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('4912dea33c17ac5361b9abbe3bfa6471', '4fa5be4baa7f93d1fed07cdb76094dcb', 'String', '==', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('347fe26056549ece4ea10ec95bb18f1f', '4fa5be4baa7f93d1fed07cdb76094dcb', 'String', '!=', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('c3a4e9ad91b96a208e357fc6eed5a100', '4fa5be4baa7f93d1fed07cdb76094dcb', 'String', '<=', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('8ce38586fb5ff2fa504d45a327328fe7', '4fa5be4baa7f93d1fed07cdb76094dcb', 'String', '<', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('e95b54f7c0b9938d3e5616cccf7265bd', '4fa5be4baa7f93d1fed07cdb76094dcb', 'String', '>=', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('ecda5d788aab18ce1a7fd56358dd788c', '4fa5be4baa7f93d1fed07cdb76094dcb', 'String', '>', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('eaad04a5bb249290274bfc373e436f4b', '645f4e0bb12838981419230eb66f06ad', 'String', 'contains', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('6885bdd3cbda70609f4a45e0fdcf170f', '645f4e0bb12838981419230eb66f06ad', 'String', 'does not contain', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO criteria_resource_param (param_id, resource_id, class_type, name, description, last_mod_id, last_mod_datetime, create_id, create_datetime) 
    VALUES ('4640e2d52a50acc0654b14394cbfafef', '645f4e0bb12838981419230eb66f06ad', 'String', 'has', NULL, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

-- //@UNDO
-- SQL to undo the change goes here.

drop table criteria_resource_param;
