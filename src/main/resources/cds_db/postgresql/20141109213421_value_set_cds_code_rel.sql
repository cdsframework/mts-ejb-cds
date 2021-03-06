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

-- // value_set_cds_code_rel
-- Migration SQL that makes the change goes here.

create table value_set_cds_code_rel (
            value_set_cds_code_rel_id varchar(32) primary key,
            value_set_id varchar(32) not null,
            code_id varchar(32) not null,
            last_mod_datetime timestamp not null,
            last_mod_id varchar(32) not null,
            create_datetime timestamp not null,
            create_id varchar(32) not null);

alter table value_set_cds_code_rel add constraint vsccr_rid_vsid foreign key (value_set_id) references value_set (value_set_id);

alter table value_set_cds_code_rel add constraint vsccr_ccid_cc_ccid foreign key (code_id) references cds_code (code_id);

alter table value_set_cds_code_rel add constraint un_vsccr_vsidsvsid unique (value_set_id, code_id);

INSERT INTO value_set_cds_code_rel (value_set_cds_code_rel_id, value_set_id, code_id, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('89b274d37fb03d81a4dbf393e6f20ff6', 'a072819bdc62cd212247128c597682be', 'f7ef5b2cb0163e7c2f49a830f2cb0f05', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

drop table value_set_cds_code_rel;
