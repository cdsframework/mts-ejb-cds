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

-- // EntityNamePartQualifier
-- Migration SQL that makes the change goes here.

INSERT INTO cds_list (list_id, list_type, code, name, description, code_system_id, value_set_id, opencds_concept_type_id, enum_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', 'EntityNamePartQualifier', 'EntityNamePartQualifier', NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('daae896ac8680ad7eb091b104f153aad', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'LS', 'Legal Status', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('e8b721128e9fb339fdeafb81380ca8b4', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'AC', 'Academic', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('2bf8db32400854b063b77dae6cd8edca', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'NB', 'Nobility', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('21e44708515bcdba5ab21c176b98243e', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'PR', 'Professional', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('2c2c700ff4d88736baef6895c32ac4f6', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'HON', 'Honorific', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f95c1b73fc69b20df586cefdbbe910a7', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'BR', 'Birth', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('db511397c95af7187ac962e05a51bdfb', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'AD', 'Acquired', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('571fd9a9f362ecea5009d698c763e1a0', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'SP', 'Spouse', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('5b2c9929b3fd92ffcbca7f34a7f1b6fd', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'MID', 'Middle Name', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b2544f50ddebdcc9c64e8d63b3d51bc1', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'CL', 'Callme', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('8504aaa7d5d00d969fded4e167af80e1', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'IN', 'Initial', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('16cb5f06d55896a698ee871e2864d9f9', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'PFX', 'Prefix', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6c7a46ebaf398bb7cdcb586457d0dcda', '1e3dc71e4000e28f8c760cadcd97807b', 'AD_HOC', NULL, NULL, NULL, NULL, 'SFX', 'Suffix', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');


-- //@UNDO
-- SQL to undo the change goes here.

delete from cds_list_item where list_id = '1e3dc71e4000e28f8c760cadcd97807b';
delete from cds_list where list_id = '1e3dc71e4000e28f8c760cadcd97807b';
