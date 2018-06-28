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

-- // vw_cds_list_item
-- Migration SQL that makes the change goes here.

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

drop view vw_cds_list_item;
