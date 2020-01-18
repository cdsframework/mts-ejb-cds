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

-- // create_data_input_node_encounter_rcs_entries
-- Migration SQL that makes the change goes here.

INSERT INTO data_input_node (node_id, node_path, template_name, template_class_name, attribute_name, attribute_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
	VALUES ('e2d0a3bd5f09536293098e9dc8a364da', 'encounterType', 'EncounterEvent', 'org.openvmr.v1_0.schema.EncounterEvent', 'CD', 'org.openvmr.v1_0.schema.CD', '2020-01-10 12:09:36.849', 'admin', '2020-01-10 12:09:36.849', 'admin');
INSERT INTO data_input_node (node_id, node_path, template_name, template_class_name, attribute_name, attribute_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
	VALUES ('17b8172d305b70016c51427fc18d6e4d', 'relatedClinicalStatement/observationResult/interpretation', 'EncounterEvent', 'org.openvmr.v1_0.schema.EncounterEvent', 'CD', 'org.openvmr.v1_0.schema.CD', '2020-01-10 12:12:10.964', 'admin', '2020-01-10 12:12:10.964', 'admin');
INSERT INTO data_input_node (node_id, node_path, template_name, template_class_name, attribute_name, attribute_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
	VALUES ('b130d5117778130e7d705dbeb93c4cdc', 'relatedClinicalStatement/observationResult/observationFocus', 'EncounterEvent', 'org.openvmr.v1_0.schema.EncounterEvent', 'CD', 'org.openvmr.v1_0.schema.CD', '2020-01-10 12:10:12.305', 'admin', '2020-01-10 12:10:12.305', 'admin');
INSERT INTO data_input_node (node_id, node_path, template_name, template_class_name, attribute_name, attribute_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
	VALUES ('db53973d22f72e53b6bd4208e12938cc', 'relatedClinicalStatement/observationResult/observationValue/concept', 'EncounterEvent', 'org.openvmr.v1_0.schema.EncounterEvent', 'CD', 'org.openvmr.v1_0.schema.CD', '2020-01-10 12:11:06.979', 'admin', '2020-01-10 12:11:06.979', 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

delete from data_input_node where node_id in ('e2d0a3bd5f09536293098e9dc8a364da','17b8172d305b70016c51427fc18d6e4d','b130d5117778130e7d705dbeb93c4cdc','db53973d22f72e53b6bd4208e12938cc');