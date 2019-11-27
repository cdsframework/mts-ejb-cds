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

-- // create_data_input_node_alter_part
-- Migration SQL that makes the change goes here.

create table data_input_node as
select distinct dtnr.node_id,
    dtnr.node_path,
    dmc.name as template_name,
    dmc.class_name as template_class_name,
    dmc2.name as attribute_name,
    dmc2.class_name as attribute_class_name,
    dtnr.last_mod_datetime,
    dtnr.last_mod_id,
    dtnr.create_datetime,
    dtnr.create_id
from data_template_node_rel dtnr
    left outer join data_template dt on dtnr.template_id = dt.template_id
    left outer join data_model_class dmc on dt.class_id = dmc.class_id
    left outer join data_model_class_node dmcn on dtnr.source_node_id = dmcn.node_id
    left outer join data_model_class dmc2 on dmc2.class_id = dmcn.node_class_id
order by dmc.name, dtnr.node_path, dmc2.name;
alter table data_input_node add primary key (node_id);
alter table data_input_node alter column template_name set not null;
alter table data_input_node alter column template_class_name set not null;
alter table data_input_node alter column attribute_name set not null;
alter table data_input_node alter column attribute_class_name set not null;
alter table data_input_node alter column last_mod_datetime set not null;
alter table data_input_node alter column last_mod_id set not null;
alter table data_input_node alter column create_datetime set not null;
alter table data_input_node alter column create_id set not null;
delete FROM data_input_node where node_id not in (select ctrn.node_id from criteria_predicate_part cpp join criteria_template_rel_node ctrn on cpp.node_rel_id = ctrn.rel_id);
alter table criteria_predicate_part add column node_id varchar(32);
alter table criteria_predicate_part add constraint fk_cpp_nid_2_din_nid foreign key (node_id) references data_input_node (node_id);
update criteria_predicate_part cpp set node_id =
  (select ctrn.node_id from criteria_template_rel_node ctrn where cpp.node_rel_id = ctrn.rel_id);
alter table criteria_predicate_part add column node_label varchar(2048);
update criteria_predicate_part cpp set node_label = (select label from criteria_template_rel ctr join criteria_template_rel_node ctrn on ctr.rel_id = ctrn.parent_rel_id 
    and ctrn.rel_id = cpp.node_rel_id );

-- //@UNDO
-- SQL to undo the change goes here.

alter table criteria_predicate_part drop constraint fk_cpp_nid_2_din_nid;
alter table criteria_predicate_part drop column node_id;
alter table criteria_predicate_part drop column node_label;
drop table data_input_node;