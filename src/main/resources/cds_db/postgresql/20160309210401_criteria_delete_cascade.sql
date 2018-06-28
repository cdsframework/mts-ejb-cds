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

-- // criteria delete cascade
-- Migration SQL that makes the change goes here.

alter table criteria_version_rel drop constraint fk_cvr_cid_2_c_cid;
alter table criteria_version_rel add constraint fk_cvr_cid_2_c_cid foreign key (criteria_id) references criteria (criteria_id) on delete cascade;
alter table criteria_template_rel drop constraint fk_ctr_cid_2_c_cid;
alter table criteria_template_rel add constraint fk_ctr_cid_2_c_cid foreign key (criteria_id) references criteria (criteria_id) on delete cascade;
alter table criteria_template_rel_node drop constraint fk_ctrn_prid_2_ctr_rid;
alter table criteria_template_rel_node add constraint fk_ctrn_prid_2_ctr_rid foreign key (parent_rel_id) references criteria_template_rel (rel_id) on delete cascade;
alter table criteria_predicate drop constraint fk_cp_cid_2_c_cid;
alter table criteria_predicate drop constraint fk_cp_ppid_2_cp_pid;
alter table criteria_predicate add constraint fk_cp_cid_2_c_cid foreign key (criteria_id) references criteria (criteria_id) on delete cascade;
alter table criteria_predicate add constraint fk_cp_ppid_2_cp_pid foreign key (parent_predicate_id) references criteria_predicate (predicate_id) on delete cascade;
alter table criteria_predicate_part drop constraint fk_cpp_pid_2_cp_pid;
alter table criteria_predicate_part add constraint fk_cpp_pid_2_cp_pid foreign key (predicate_id) references criteria_predicate (predicate_id) on delete cascade;
alter table criteria_predicate_part_rel drop constraint fk_cppr_pid_2_cpp_pid;
alter table criteria_predicate_part_rel add constraint fk_cppr_pid_2_cpp_pid foreign key (part_id) references criteria_predicate_part (part_id) on delete cascade;

-- //@UNDO
-- SQL to undo the change goes here.

alter table criteria_version_rel drop constraint fk_cvr_cid_2_c_cid;
alter table criteria_version_rel add constraint fk_cvr_cid_2_c_cid foreign key (criteria_id) references criteria (criteria_id);
alter table criteria_template_rel drop constraint fk_ctr_cid_2_c_cid;
alter table criteria_template_rel add constraint fk_ctr_cid_2_c_cid foreign key (criteria_id) references criteria (criteria_id);
alter table criteria_template_rel_node drop constraint fk_ctrn_prid_2_ctr_rid;
alter table criteria_template_rel_node add constraint fk_ctrn_prid_2_ctr_rid foreign key (parent_rel_id) references criteria_template_rel (rel_id);
alter table criteria_predicate drop constraint fk_cp_cid_2_c_cid;
alter table criteria_predicate drop constraint fk_cp_ppid_2_cp_pid;
alter table criteria_predicate add constraint fk_cp_cid_2_c_cid foreign key (criteria_id) references criteria (criteria_id);
alter table criteria_predicate add constraint fk_cp_ppid_2_cp_pid foreign key (parent_predicate_id) references criteria_predicate (predicate_id);
alter table criteria_predicate_part drop constraint fk_cpp_pid_2_cp_pid;
alter table criteria_predicate_part add constraint fk_cpp_pid_2_cp_pid foreign key (predicate_id) references criteria_predicate (predicate_id);
alter table criteria_predicate_part_rel drop constraint fk_cppr_pid_2_cpp_pid;
alter table criteria_predicate_part_rel add constraint fk_cppr_pid_2_cpp_pid foreign key (part_id) references criteria_predicate_part (part_id);
