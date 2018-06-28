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

-- // AddressPartType
-- Migration SQL that makes the change goes here.

INSERT INTO cds_list (list_id, list_type, code, name, description, code_system_id, value_set_id, opencds_concept_type_id, enum_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', 'AddressPartType', 'AddressPartType', NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('db52079ea4192aef18824e60165e2773', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'AL', 'Address Line', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('e16d8be9e723800f6ee2f714446f5046', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'ADL', 'Additional Locator', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('69085b8768f82e02f0dbdb65ce6dda36', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'UNID', 'Unit Identifier', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('bd7d993ea9102908f1c4239baaec390f', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'UNIT', 'Unit Designator', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('ac23e08aa1bad8e8100340af6cb99bb2', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'DAL', 'Delivery Address Line', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('752a78eb630b612e20ceca7d14da0144', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'DINST', 'Delivery Installation Type', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('fa79562c1da9b2efa18a7b80464096fa', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'DINSTA', 'Delivery Installation Area', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6641b593beb5c2f748b6f495191c7726', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'DINSTQ', 'Delivery Installation Qualifier', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('4edbfc34c592381a4fadb8017c55c479', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'DMOD', 'Delivery Mode', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('fef87ee3623e38333b15a7f88b0146c9', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'DMODID', 'Delivery Mode Identifier', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6c26329bfa599a67996a759820c22131', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'SAL', 'Street Address Line', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('3670c022c2ace5e0320dab3e6085f940', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'BNR', 'Building Number', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('2defe202e0a01ef4217ba5e9f3036d31', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'BNN', 'Building Number Numeric', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('164b49c014c5fb4db9daf4fb8cf9214f', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'BNS', 'Building Number Suffix', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('38a90a75a9903aedb56fd8e814206a95', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'STR', 'Street Name', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('ab7f989e63020d72df3c792ab0cd7f94', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'STB', 'Street Name Base', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('89e7732fa0cd079b6b9a0578cee75399', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'STTYP', 'Street Type', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('4935c9edf0096d0a8ea636368edf800e', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'DIR', 'Direction', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6e6ab4fde582d24b1c23e02edef4e020', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'INT', 'Intersection', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('da92d1b7c19e5b148b248f80dc91c2a2', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'CAR', 'Care of', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('96e558bf137018ce32bc5dd7cf40d630', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'CEN', 'Census Tract', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a465015615e94d53b6077dd8c2339798', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'CNT', 'Country', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a06195f975a405f0da7c2a74546addfb', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'CPA', 'County or Parish', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('475942cf320e6c152619b41a976f810f', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'CTY', 'Municipality', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('43735e26c201152c7fe2b0b9792e4728', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'DEL', 'Delimiter', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('378a285336523d65737852bbaf3b3a44', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'POB', 'Post Box', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('53a90bb210a56ff2f1625c7122aded36', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'PRE', 'Precinct', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('35e34cec57e1abfed38013ee5d882bbc', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'STA', 'State or Province', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6baefb7d91b064dd7a3a1133794f17d0', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'ZIP', 'Postal Code', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('7adfe670a9efd184e2804a8cdf88483e', 'b6aef07265544ee94a82d2fdb428a167', 'AD_HOC', NULL, NULL, NULL, NULL, 'DPID', 'Delivery Point Identifier', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

delete from cds_list where list_id = 'b6aef07265544ee94a82d2fdb428a167';
delete from cds_list_item where list_id = 'b6aef07265544ee94a82d2fdb428a167';