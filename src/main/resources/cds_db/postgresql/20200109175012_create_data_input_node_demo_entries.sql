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

-- // create_data_input_node_demo_entries
-- Migration SQL that makes the change goes here.

INSERT INTO data_input_node (node_id, node_path, template_name, template_class_name, attribute_name, attribute_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
	VALUES ('587af9d695b4b1f47719a744fd253e55', 'demographics/birthTime', 'EvaluatedPerson', 'org.openvmr.v1_0.schema.EvaluatedPerson', 'TS', 'org.openvmr.v1_0.schema.TS', '2020-01-09 09:21:24.162', 'admin', '2020-01-09 09:21:24.162', 'admin');
INSERT INTO data_input_node (node_id, node_path, template_name, template_class_name, attribute_name, attribute_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
	VALUES ('3b9514da2bfdbf54bffb10d6cff54a95', 'demographics/gender', 'EvaluatedPerson', 'org.openvmr.v1_0.schema.EvaluatedPerson', 'CD', 'org.openvmr.v1_0.schema.CD', '2020-01-09 09:21:32.078', 'admin', '2020-01-09 09:21:32.078', 'admin');
INSERT INTO data_input_node (node_id, node_path, template_name, template_class_name, attribute_name, attribute_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
	VALUES ('46124f29305d308a6c482e948bd647dd', 'demographics/isDeceased', 'EvaluatedPerson', 'org.openvmr.v1_0.schema.EvaluatedPerson', 'BL', 'org.openvmr.v1_0.schema.BL', '2020-01-09 09:21:38.975', 'admin', '2020-01-09 09:21:38.975', 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

delete from data_input_node where node_id in ('587af9d695b4b1f47719a744fd253e55','3b9514da2bfdbf54bffb10d6cff54a95','46124f29305d308a6c482e948bd647dd');