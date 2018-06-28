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

-- // criteria_predicate_part_rel
-- Migration SQL that makes the change goes here.

CREATE TABLE criteria_predicate_part_rel (
            rel_id varchar(32) primary key,
            part_id varchar(32) not null,
            constraint_type varchar(32) not null,
            list_id varchar(32),
            code_system_id varchar(32),
            value_set_id varchar(32),
            LAST_MOD_ID varchar(32) not null,
            LAST_MOD_DATETIME timestamp not null,
            CREATE_ID varchar(32) not null,
            CREATE_DATETIME timestamp not null);

alter table criteria_predicate_part_rel add constraint fk_cppr_pid_2_cpp_pid foreign key (part_id) references criteria_predicate_part (part_id);
alter table criteria_predicate_part_rel add constraint fk_cppr_lid_2_cl_lid foreign key (list_id) references cds_list (list_id);
alter table criteria_predicate_part_rel add constraint fk_cppr_csid_2_ccs_csid foreign key (code_system_id) references cds_code_system (code_system_id);
alter table criteria_predicate_part_rel add constraint fk_cppr_vsid_2_vs_vsid foreign key (value_set_id) references value_set (value_set_id);

-- //@UNDO
-- SQL to undo the change goes here.

drop table criteria_predicate_part_rel;

