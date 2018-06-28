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

-- // increace display name
-- Migration SQL that makes the change goes here.
drop view cds_code_opencds_concept_rel;
drop view v_cds_code;
drop view v_cds_code_collection;
drop view v_cds_code_list_collection;
drop view v_opencds_concept_collection;
drop view v_opencds_concept_list_collection;
drop view if exists vw_rckms_condition;
drop view vw_cds_list_item;
drop view if exists vw_ice_test_proposal;
drop view if exists vw_ice_vaccine_series;
drop view if exists vw_ice_vaccine_group_test;
drop view if exists vw_ice_test_event;
drop view if exists vw_ice_test_event_component;
drop view if exists vw_ice_test_vac_vac_group;
drop view if exists vw_ice_test_vac_comp_vac_group;
drop view if exists vw_ice_series;
drop view if exists vw_ice_test_vaccine_group_vaccine;
drop view if exists vw_ice_test_disease_code;
drop view if exists vw_ice_vaccine_component_rel;
drop view if exists vw_ice_vaccine;
drop view if exists vw_ice_vaccine_group_vaccine_rel;
drop view if exists vw_ice_vaccine_group_disease_rel;
drop view if exists vw_ice_test_vaccine_group_rel;
drop view if exists vw_ice_vaccine_group;
drop view if exists vw_ice_vaccine_disease_rel;
drop view if exists vw_ice_vaccine_component;
drop view if exists vw_ice_disease;

alter table cds_code alter column display_name TYPE varchar(1024);

CREATE VIEW cds_code_opencds_concept_rel as select distinct ocr.concept_code_id, cc.*
    from OPENCDS_CONCEPT_REL ocr, cds_code cc where ocr.cds_code_id = cc.code_id;

create or replace view v_cds_code as
select cc.*, ccs.oid, ccs."name" 
from cds_code cc
join cds_code_system ccs on cc.code_system_id = ccs.code_system_id;

create or replace view v_cds_code_list_collection as
select distinct cc.*, ccs.oid, ccs.name, cpp.part_id
from 
cds_code_system ccs,
cds_code cc,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr,
cds_list cl
where 
ccs.code_system_id = cc.code_system_id and
cpp.part_id = cppr.part_id and
cl.list_type = 'CODE_SYSTEM' and
cl.code_system_id = ccs.code_system_id and
cpp.concept_selection_type = 'Code' and
cppr.constraint_type = 'List'
union
select distinct cc.*, ccs.oid, ccs.name, cpp.part_id
from 
cds_code_system ccs,
cds_code cc,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr,
value_set_cds_code_rel vsccr,
cds_list cl
where 
ccs.code_system_id = cc.code_system_id and
cc.code_id = vsccr.code_id and
cl.value_set_id = vsccr.value_set_id and
cl.list_id = cppr.list_id and
cpp.part_id = cppr.part_id and
cl.list_type = 'VALUE_SET' and
cpp.concept_selection_type = 'Code' and
cppr.constraint_type = 'List'
union
select distinct cc.*, ccs.oid, ccs.name, cpp.part_id
from 
cds_code_system ccs,
cds_code cc,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr,
cds_list_item cli, 
cds_list cl,
opencds_concept_rel ocr
where 
ccs.code_system_id = cc.code_system_id and
cc.code_id = ocr.cds_code_id and
cli.opencds_code_id = ocr.concept_code_id and
cl.list_id = cli.list_id and
cli.list_id = cppr.list_id and
cpp.part_id = cppr.part_id and
cl.list_type = 'AD_HOC_CONCEPT' and 
cpp.concept_selection_type = 'Code' and
cppr.constraint_type = 'List'
union
select distinct cc.*, ccs.oid, ccs.name, cpp.part_id
from 
cds_code_system ccs,
cds_code cc,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr,
cds_list cl,
opencds_concept_rel ocr
where 
ccs.code_system_id = cc.code_system_id and
cl.code_system_id = ccs.code_system_id and
cc.code_id = ocr.cds_code_id and
cppr.list_id = cl.list_id and
cpp.part_id = cppr.part_id and
cl.list_type = 'CONCEPT' and 
cpp.concept_selection_type = 'Code' and
cppr.constraint_type = 'List';

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

create or replace view v_opencds_concept_collection as
select oc.*, cpp.part_id
from 
opencds_concept oc,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr
where cppr.part_id = cpp.part_id and
cpp.concept_selection_type = 'Concept' and
cppr.constraint_type = 'Concept' and
oc.code_id = cppr.concept_id
union
select oc.*, cpp.part_id
from 
cds_code cc,
opencds_concept oc,
opencds_concept_rel ocr,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr
where cppr.part_id = cpp.part_id and
cpp.concept_selection_type = 'Concept' and
cppr.constraint_type  = 'CodeSystem' and
cc.code_system_id = cppr.code_system_id and
ocr.cds_code_id = cc.code_id and
ocr.concept_code_id = oc.code_id
union
select oc.*, cpp.part_id
from 
cds_code cc,
opencds_concept oc,
opencds_concept_rel ocr,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr
where cppr.part_id = cpp.part_id and
cpp.concept_selection_type = 'Concept' and
cppr.constraint_type  = 'Code' and
cc.code_id = cppr.code_id and
ocr.cds_code_id = cc.code_id and
ocr.concept_code_id = oc.code_id
union
select oc.*, cpp.part_id
from 
cds_code cc,
opencds_concept oc,
opencds_concept_rel ocr,
value_set_cds_code_rel vsccr,
criteria_predicate_part cpp, 
criteria_predicate_part_rel cppr
where cppr.part_id = cpp.part_id and
cpp.concept_selection_type = 'Concept' and
cppr.constraint_type  = 'ValueSet' and
cppr.value_set_id = vsccr.value_set_id and
cc.code_id = vsccr.code_id and
ocr.cds_code_id = cc.code_id and
ocr.concept_code_id = oc.code_id
union
select * from v_opencds_concept_list_collection
union
select oc.*, cpp.part_id
from 
opencds_concept oc
join opencds_concept_rel ocr on oc.code_id = ocr.concept_code_id and ocr.mapping_type = 'VALUE_SET'
join criteria_predicate_part_rel cppr on cppr.value_set_id = ocr.value_set_id and cppr.constraint_type  = 'ValueSet'
join criteria_predicate_part cpp on cpp.part_id = cppr.part_id and cpp.concept_selection_type = 'Concept';

create or replace view vw_cds_list_item as
select cli.*,
       cl.code as list_code,
       cl.name as list_name,
       clvr.version_id as version_id,
       cc.code as code,
       cc.display_name as code_display_name,
       oc.code as concept_code,
       oc.display_name as concept_code_display_name,
       cc2.code as vset_code,
       cc2.display_name as vset_code_display_name,
       cc3.code as cbased_code,
       cc3.display_name as cbased_code_display_name
from cds_list_item cli
    join cds_list cl on cl.list_id = cli.list_id
    left join cds_list_version_rel clvr on clvr.list_id = cl.list_id
    left join cds_version cv on cv.version_id = clvr.version_id
    left join cds_business_scope cbs on cbs.business_scope_id = cv.business_scope_id
    left join cds_code cc on cc.code_id = cli.code_id
    left join opencds_concept oc on oc.code_id = cli.opencds_code_id
    left join value_set_cds_code_rel vsccr on cli.value_set_cds_code_rel_id = vsccr.value_set_cds_code_rel_id
    left join cds_code cc2 on vsccr.code_id = cc2.code_id
    left join opencds_concept_rel ocr on ocr.relationship_id = cli.opencds_concept_rel_id
    left join cds_code cc3 on ocr.cds_code_id = cc3.code_id;

-- //@UNDO
-- SQL to undo the change goes here.
