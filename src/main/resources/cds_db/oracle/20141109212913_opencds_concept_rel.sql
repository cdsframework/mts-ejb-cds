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

-- // opencds_concept_rel
-- Migration SQL that makes the change goes here.

create table opencds_concept_rel (
            relationship_id varchar(32) primary key,
            concept_code_id varchar(32) not null,
            cds_code_id varchar(32) not null,
            determination_method varchar(32) not null,
            specification_notes varchar(512),
            last_mod_datetime timestamp not null,
            last_mod_id varchar(32) not null,
            create_datetime timestamp not null,
            create_id varchar(32) not null);

alter table opencds_concept_rel add constraint cdsocdsc_2_detmethid foreign key (determination_method) references concept_determination_method (code_id);

alter table opencds_concept_rel add constraint fk_cdspccr2cdsca foreign key (concept_code_id) references opencds_concept (code_id);

alter table opencds_concept_rel add constraint fk_cdspccr2cdscb foreign key (cds_code_id) references cds_code (code_id);

alter table opencds_concept_rel add constraint un_ocdscrccidccidcdm unique (concept_code_id, cds_code_id, determination_method);

INSERT INTO opencds_concept_rel (relationship_id, concept_code_id, cds_code_id, determination_method, specification_notes, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('42e7cd289fb01a5328db778f6d6c225f', 'a295ba83953c06b459042aff294d2f73', '537030c4d4a618263258a7d819b71b16', 'cdb46802ca1e8385a2175f566a47b20d', NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO opencds_concept_rel (relationship_id, concept_code_id, cds_code_id, determination_method, specification_notes, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('d4a0c56acbc5f820bccc737bf6a85f79', 'fa72f597d1d47f25e6cb27d463535b90', '161a3206589043b77094ef410e707121', 'cdb46802ca1e8385a2175f566a47b20d', NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO opencds_concept_rel (relationship_id, concept_code_id, cds_code_id, determination_method, specification_notes, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('9ab9ad6cebf8ebc83809e5a69ec57b01', 'd2f0f724cb69f5d7fc47430f67ccf9af', 'a3e55f26912c2759407e01d34373ff50', 'cdb46802ca1e8385a2175f566a47b20d', NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

drop table opencds_concept_rel;
