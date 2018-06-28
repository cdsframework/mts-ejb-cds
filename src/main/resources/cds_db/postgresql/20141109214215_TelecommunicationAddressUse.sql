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

-- // TelecommunicationAddressUse
-- Migration SQL that makes the change goes here.

INSERT INTO cds_list (list_id, list_type, code, name, description, code_system_id, value_set_id, opencds_concept_type_id, enum_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('d813a93e675a74ddd09496237fb53486', 'AD_HOC', 'TelecommunicationAddressUse', 'TelecommunicationAddressUse', NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('9ea99eabfa74afd6f77a0692f92dc156', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'H', 'Home address', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6e4e769c7f63b3d894ddf5c5288a2b4e', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'HP', 'Primary Home', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('7ae0f14a34806e6f37ec1b1622c624e3', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'HV', 'Vacation Home', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('90079b2a39f94826dcafe585737253b7', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'WP', 'Work Place', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('89e619c93f94a21e9b1382ae4c0dae69', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'DIR', 'Direct', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('7c1cfed5676eb0efbcc780238a01de7c', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'PUB', 'Public', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('abc62fe4cc65003e282c5cc4676d2688', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'BAD', 'Bad address', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('9010001b8ef25b9c9d632a1df1a43123', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'TMP', 'Temporary address', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('2ab790f4a0ea19fae1c6e4c7c5768b76', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'AS', 'Answering Service', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('fc1fb40b1d82c5b57f86f0ed6a7b50e8', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'EC', 'Emergency Contact', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b9c6e3cbdadd11d4e048d8eff86c640f', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'MC', 'Mobile Contact', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6c6a28fdd89a7b441cc83efa9eadd921', 'd813a93e675a74ddd09496237fb53486', 'AD_HOC', NULL, NULL, NULL, NULL, 'PG', 'Pager', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');


-- //@UNDO
-- SQL to undo the change goes here.

delete from cds_list_item where list_id = 'd813a93e675a74ddd09496237fb53486';
delete from cds_list where list_id = 'd813a93e675a74ddd09496237fb53486';
