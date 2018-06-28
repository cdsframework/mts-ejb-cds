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

-- // criteria_predicate_part_concept
-- Migration SQL that makes the change goes here.

CREATE TABLE criteria_predicate_part_concept (
            id varchar(32) primary key,
            part_id varchar(32) not null,
            code_id varchar(32),
            concept_id varchar(32),
            LAST_MOD_ID varchar(32) not null,
            LAST_MOD_DATETIME timestamp not null,
            CREATE_ID varchar(32) not null,
            CREATE_DATETIME timestamp not null);

alter table criteria_predicate_part_concept add constraint fk_cppc_pid_cpp_pid foreign key (part_id) references criteria_predicate_part (part_id);
alter table criteria_predicate_part_concept add constraint fk_cppc_codid_cc_cid foreign key (code_id) references cds_code (code_id);
alter table criteria_predicate_part_concept add constraint fk_cppc_concid_oc_cid foreign key (concept_id) references opencds_concept (code_id);

insert into criteria_predicate_part_concept 
    select md5(random()::text || now()::text), part_id, default_code_id, default_concept_id, last_mod_id, last_mod_datetime,create_id, create_datetime
    from criteria_predicate_part
    where default_code_id is not null
       or default_concept_id is not null;

alter table criteria_predicate_part drop default_code_id;
alter table criteria_predicate_part drop default_concept_id;

-- //@UNDO
-- SQL to undo the change goes here.

alter table criteria_predicate_part add default_code_id varchar(32);
alter table criteria_predicate_part add default_concept_id varchar(32);

alter table criteria_predicate_part add constraint fk_cpp_dcid_2_cc_cid foreign key (default_code_id) references cds_code (code_id);
alter table criteria_predicate_part add constraint fk_cpp_dcid_2_oc_cid foreign key (default_concept_id) references opencds_concept (code_id);

update criteria_predicate_part
set default_code_id = (select cppc.code_id
                       from criteria_predicate_part_concept cppc
                       where cppc.part_id = criteria_predicate_part.part_id
                       limit 1);

update criteria_predicate_part
set default_concept_id = (select cppc.concept_id
                          from criteria_predicate_part_concept cppc
                          where cppc.part_id = criteria_predicate_part.part_id
                          limit 1);

drop table criteria_predicate_part_concept;
