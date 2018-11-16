--
--    Copyright 2010-2016 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

-- // vw_data_template_node_rel
-- Migration SQL that makes the change goes here.

drop view vw_data_template_node_rel;

create or replace view vw_data_template_node_rel as
select dtnr.*, dmc.name as template_class_name, dmc2.name as attribute_class_name
from data_template_node_rel dtnr
    left outer join data_template dt on dtnr.template_id = dt.template_id
    left outer join data_model_class dmc on dt.class_id = dmc.class_id
    left outer join data_model_class_node dmcn on dtnr.source_node_id = dmcn.node_id
    left outer join data_model_class dmc2 on dmc2.class_id = dmcn.node_class_id;

-- //@UNDO
-- SQL to undo the change goes here.

drop view vw_data_template_node_rel;

create or replace view vw_data_template_node_rel as
select dtnr.*, dmc.name as template_class_name
from data_template_node_rel dtnr
    left outer join data_template dt on dtnr.template_id = dt.template_id
    left outer join data_model_class dmc on dt.class_id = dmc.class_id;
