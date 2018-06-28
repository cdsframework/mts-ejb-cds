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

-- // create_or_replace_v_cds_code_collection
-- Migration SQL that makes the change goes here.

create or replace view v_cds_code_collection as
select distinct cc.*, ccs.oid, ccs.name, cpp.part_id
from 
cds_code_system ccs,
cds_code cc,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr 
where ccs.code_system_id = cc.code_system_id and
cc.code_system_id = cppr.code_system_id and
cpp.part_id = cppr.part_id and
cpp.concept_selection_type = 'Code' and
cppr.constraint_type = 'CodeSystem'
union
select distinct cc.*, ccs.oid, ccs.name, cpp.part_id 
from 
cds_code_system ccs,
cds_code cc,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr 
where ccs.code_system_id = cc.code_system_id and
cc.code_id = cppr.code_id and
cpp.part_id = cppr.part_id and
cpp.concept_selection_type = 'Code' and
cppr.constraint_type = 'Code'
union
select distinct cc.*, ccs.oid, ccs.name, cpp.part_id 
from 
cds_code_system ccs,
cds_code cc, 
value_set_cds_code_rel vsccr,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr 
where ccs.code_system_id = cc.code_system_id and
cc.code_id = vsccr.code_id and
vsccr.value_set_id = cppr.value_set_id and
cpp.part_id = cppr.part_id and
cpp.concept_selection_type = 'Code' and
cppr.constraint_type = 'ValueSet'
union 
select distinct cc.*, ccs.oid, ccs.name, cpp.part_id
from
cds_code_system ccs,
cds_code cc, 
opencds_concept_rel ocr,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr 
where ccs.code_system_id = cc.code_system_id and
cc.code_id = ocr.cds_code_id and
ocr.concept_code_id = cppr.concept_id and
cpp.part_id = cppr.part_id and
cpp.concept_selection_type = 'Code' and
cppr.constraint_type = 'Concept' 
union
select * from v_cds_code_list_collection;

-- //@UNDO
-- SQL to undo the change goes here.

drop view v_cds_code_collection;

