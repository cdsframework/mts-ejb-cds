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

-- // data_model_node
-- Migration SQL that makes the change goes here.

create table data_model_node (
            node_id varchar(32) primary key,
            model_id varchar(32),
            parent_node_id varchar(32),
            class_id varchar(32) not null,
            name varchar(512) not null,
            cardinality_type varchar(32) not null,
            mandatory boolean,
            conformance_type varchar(32),
            fixed_value boolean,
            ad_hoc boolean,
            class_constraints varchar(2048),
            comments varchar(2048),
            last_mod_datetime timestamp not null,
            last_mod_id varchar(32) not null,
            create_datetime timestamp not null,
            create_id varchar(32) not null);

alter table data_model_node add constraint un_dmn_name_mid_pnid unique (name, model_id, parent_node_id);

alter table data_model_node add constraint fk_dmn_mid_2_dm_mid foreign key (model_id) references data_model (model_id);

alter table data_model_node add constraint fk_dmn_pnid_2_dmn_nid foreign key (parent_node_id) references data_model_node (node_id);

alter table data_model_node add constraint fk_dmn_cid_2_dmc_cid foreign key (class_id) references data_model_class (class_id);

INSERT INTO data_model_node (node_id, model_id, class_id, name, cardinality_type, mandatory, conformance_type, fixed_value, ad_hoc, class_constraints, comments, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f7439ef5ee86afd6aecc42db2d618351', 'cdb46802ca1e8385a2175f566a47b20d', '786a59ede9d1336ebdcf5190cfc869cf', 'observationResult', 'ONE_TO_ONE', true, 'R', false, true, 'This is a constraint', 'This is a comment', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_node (node_id, parent_node_id, class_id, name, cardinality_type, mandatory, conformance_type, fixed_value, ad_hoc, class_constraints, comments, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('44d60c58a904b007c53cb6f0e0304a1d', 'f7439ef5ee86afd6aecc42db2d618351', '3d252497cbbb36fe9a818ed858b981af', 'observationValue', 'ZERO_TO_ONE', true, 'R', false, false, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_node (node_id, parent_node_id, class_id, name, cardinality_type, mandatory, conformance_type, fixed_value, ad_hoc, class_constraints, comments, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b0ad54fb9c778f36aefefb9a4eac88a2', 'f7439ef5ee86afd6aecc42db2d618351', '3d252497cbbb36fe9a818ed858b981af', 'observationFocus', 'ONE_TO_ONE', true, 'R', false, false, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_node (node_id, parent_node_id, class_id, name, cardinality_type, mandatory, conformance_type, fixed_value, ad_hoc, class_constraints, comments, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('fee847918df545f221feba4953c0d515', 'f7439ef5ee86afd6aecc42db2d618351', '14be041b6fd0231b73f599a5683058f2', 'relatedEntity', 'ZERO_TO_ONE', false, NULL, false, false, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_node (node_id, parent_node_id, class_id, name, cardinality_type, mandatory, conformance_type, fixed_value, ad_hoc, class_constraints, comments, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('7d4a97934fe16f514c206848bf639ab0', 'f7439ef5ee86afd6aecc42db2d618351', '3d252497cbbb36fe9a818ed858b981af', 'interpretation', 'ONE_TO_MANY', true, 'R', false, false, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_node (node_id, parent_node_id, class_id, name, cardinality_type, mandatory, conformance_type, fixed_value, ad_hoc, class_constraints, comments, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('85214533a3a85b12dfcea8f2c9869fa9', 'f7439ef5ee86afd6aecc42db2d618351', '3d252497cbbb36fe9a818ed858b981af', 'status', 'ZERO_TO_ONE', false, NULL, false, false, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO data_model_node (node_id, parent_node_id, class_id, name, cardinality_type, mandatory, conformance_type, fixed_value, ad_hoc, class_constraints, comments, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('67ea157029c39b4ce87a56abf87e40a0', 'f7439ef5ee86afd6aecc42db2d618351', 'f89c7a73aff266756922d437e0ad8044', 'observationEventTime', 'ONE_TO_ONE', true, 'R', false, false, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

drop table data_model_node;
