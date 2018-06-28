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

-- // criteria_predicate_part
-- Migration SQL that makes the change goes here.

CREATE TABLE criteria_predicate_part (
            part_id varchar(32) primary key,
            predicate_id varchar(32) not null,
            part_type varchar(32) not null,
            node_rel_id varchar(32),
            resource_id varchar(32),
            default_code_id varchar(32),
            default_concept_id varchar(32),
            selected_param_id varchar(32),
            concept_selection_type varchar(32),
            default_id_root varchar(256),
            default_id_ext varchar(256),
            text varchar(512),
            part_alias varchar(512),
            overridable boolean,
            predicate_part_order numeric not null,
            resource_type varchar(32),
            data_input_class_type varchar(32),
            data_input_boolean boolean,
            data_input_date1 date,
            data_input_date2 date,
            data_input_numeric numeric,
            LAST_MOD_ID varchar(32) not null,
            LAST_MOD_DATETIME timestamp not null,
            CREATE_ID varchar(32) not null,
            CREATE_DATETIME timestamp not null);

alter table criteria_predicate_part add constraint fk_cpp_pid_2_cp_pid foreign key (predicate_id) references criteria_predicate (predicate_id);
alter table criteria_predicate_part add constraint fk_cpp_nrid_2_ctrn_rid foreign key (node_rel_id) references criteria_template_rel_node (rel_id);
alter table criteria_predicate_part add constraint fk_cpp_rid_2_cr_rid foreign key (resource_id) references criteria_resource (resource_id);
alter table criteria_predicate_part add constraint fk_cpp_spid_2_cr_rid foreign key (selected_param_id) references criteria_resource_param (param_id);
alter table criteria_predicate_part add constraint fk_cpp_dcid_2_cc_cid foreign key (default_code_id) references cds_code (code_id);
alter table criteria_predicate_part add constraint fk_cpp_dcid_2_oc_cid foreign key (default_concept_id) references opencds_concept (code_id);

-- //@UNDO
-- SQL to undo the change goes here.

drop table criteria_predicate_part;
