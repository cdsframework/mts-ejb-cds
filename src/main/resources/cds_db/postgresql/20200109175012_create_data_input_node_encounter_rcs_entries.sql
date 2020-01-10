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
	VALUES ('e76e84a443d4efc71cf6d06605c6928f', 'relatedClinicalStatement/observationResult/observationFocus', 'EncounterEvent', 'org.openvmr.v1_0.schema.EncounterEvent', 'CD', 'org.openvmr.v1_0.schema.CD', '2020-01-09 09:21:38.975', 'admin', '2020-01-09 09:21:38.975', 'admin');
INSERT INTO data_input_node (node_id, node_path, template_name, template_class_name, attribute_name, attribute_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
	VALUES ('7e9e14ea2bfdbf24bffb10d6cff54a95', 'relatedClinicalStatement/observationResult/observationValue/concept', 'EncounterEvent', 'org.openvmr.v1_0.schema.EncounterEvent', 'CD', 'org.openvmr.v1_0.schema.CD', '2020-01-09 09:21:38.975', 'admin', '2020-01-09 09:21:38.975', 'admin');
INSERT INTO data_input_node (node_id, node_path, template_name, template_class_name, attribute_name, attribute_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
	VALUES ('b39e15ea27f22f24bffb10d6cff54a95', 'encounterType', 'EncounterEvent', 'org.openvmr.v1_0.schema.EncounterEvent', 'CD', 'org.openvmr.v1_0.schema.CD', '2020-01-09 09:21:38.975', 'admin', '2020-01-09 09:21:38.975', 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

delete from data_input_node where node_id in ('e76e84a443d4efc71cf6d06605c6928f','7e9e14ea2bfdbf24bffb10d6cff54a95','b39e15ea27f22f24bffb10d6cff54a95');