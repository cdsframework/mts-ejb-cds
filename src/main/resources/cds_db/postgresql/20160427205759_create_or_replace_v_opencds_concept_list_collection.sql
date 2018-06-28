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

-- // create_or_replace_v_opencds_concept_list_collection
-- Migration SQL that makes the change goes here.

create or replace view v_opencds_concept_list_collection as
select oc.*, cppr.part_id
from 
opencds_concept oc,
cds_list cl, 
cds_list_item cli, 
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr
where 
oc.code_id = cli.opencds_code_id and
cl.list_id = cli.list_id and
cpp.part_id = cppr.part_id and
cli.list_id = cppr.list_id and
cl.list_type = 'AD_HOC_CONCEPT' and
cpp.concept_selection_type = 'Concept' and
cppr.constraint_type = 'List'
union
select distinct oc.*, cpp.part_id
from 
opencds_concept oc,
opencds_concept_rel ocr,
cds_list cl, 
cds_list_item cli, 
criteria_predicate_part cpp,
criteria_predicate_part_rel cppr
where 
oc.code_id = ocr.concept_code_id and
cli.list_id = cli.list_id and
cpp.part_id = cppr.part_id and
cppr.list_id = cli.list_id and
cl.list_type = 'CODE_SYSTEM' and
cpp.concept_selection_type = 'Concept' and
cppr.constraint_type = 'List' and
ocr.code_system_id = cppr.code_system_id 
union
select oc.*, cppr.part_id
from 
opencds_concept oc,
cds_list cl,
cds_code_system ccs,
cds_code cc,
criteria_predicate_part cpp,
criteria_predicate_part_rel cppr,
opencds_concept_rel ocr
where 
ccs.code_system_id = cl.code_system_id and
ccs.code_system_id = cc.code_system_id and
ocr.cds_code_id = cc.code_id and
ocr.concept_code_id = oc.code_id and
cpp.part_id = cppr.part_id and
cl.list_id = cppr.list_id and
cl.list_type = 'CONCEPT' and
cpp.concept_selection_type = 'Concept' and
cppr.constraint_type = 'List'
union
select oc.*, cpp.part_id
from 
opencds_concept_rel ocr,
cds_list cl,
cds_code cc,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr,
opencds_concept oc,
value_set_cds_code_rel vsccr
where cppr.part_id = cpp.part_id and
cpp.concept_selection_type = 'Concept' and
cppr.constraint_type  = 'ValueSet' and
cppr.value_set_id = vsccr.value_set_id and
cppr.constraint_type = 'List' and
cl.list_id = cppr.list_id and
cc.code_id = vsccr.code_id and
ocr.cds_code_id = cc.code_id and
ocr.concept_code_id = oc.code_id;

-- //@UNDO
-- SQL to undo the change goes here.
drop view v_opencds_concept_list_collection;

