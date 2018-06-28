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

-- // EntityNameUse
-- Migration SQL that makes the change goes here.

INSERT INTO cds_list (list_id, list_type, code, name, description, code_system_id, value_set_id, opencds_concept_type_id, enum_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', 'EntityNameUse', 'EntityNameUse', NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('fef2163d733d5cd2c28763f0c4319e27', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'ABC', 'Alphabetic', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a11dada4587a3dca1208ab37bc32d020', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'SYL', 'Syllabic', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f1812ab6b819111cc67352d6a62204b3', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'IDE', 'Ideographic', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('107cd5f56dc4ca88e3d9aa55ac2c2dfa', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'C', 'Customary', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('4b3a1dadb9c743fc54c05d48b1c08a5a', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'OR', 'Official Registry Name', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('589aa146406e885d8e1b60c1e287f6e7', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'T', 'Temporary', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('18bc1dda8c358686b27c4059d948407b', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'I', 'Indigenous/Tribal', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6d967090e645588183e33055672f1f05', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'P', 'Other/Pseudonym/Alias', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('650d4b53eae64feae83edc75eb5a4faa', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'ANON', 'Anonymous', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('fe1ad813bf5a55122fa7e88c18551301', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'A', 'Business Name', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f03faddbd1abab4c39addbb8f055e682', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'R', 'Religious', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('fff27ce85bd51dd36c8f73e1325aa8b1', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'OLD', 'No Longer in Use', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('edc45e7713cf586d6e4fef2c281515a4', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'DN', 'Do Not Use', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('e166a5e47ad2d9b038279b0a8f86e881', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'M', 'Maiden Name', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('1bc2a9a4213af0158ca0cae8e2b8ade0', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'SRCH', 'Search Type Uses', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('ea3956a6dc8717208e87ba2e1bb8d058', '463f870450d83b6cdea7a219a93cb51a', 'AD_HOC', NULL, NULL, NULL, NULL, 'PHON', 'Phonetic', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

delete from cds_list_item where list_id = '463f870450d83b6cdea7a219a93cb51a';
delete from cds_list where list_id = '463f870450d83b6cdea7a219a93cb51a';
