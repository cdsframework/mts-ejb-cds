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

-- // TemplateStatusCode
-- Migration SQL that makes the change goes here.

INSERT INTO cds_code_system (code_system_id, oid, name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('916447d1fb626e7954574b7018cd6bae', '2.16.840.1.113883.3.1937.98.5.8', 'TemplateStatusCode', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f35cfe9d79b79835d882767052d26051', '916447d1fb626e7954574b7018cd6bae', 'terminated', 'Terminated', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('62ba36472b50f5479b893af9e7fdf41b', '916447d1fb626e7954574b7018cd6bae', 'retired', 'Retired', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('fd4c9745484c178f871eda9dafaf3b70', '916447d1fb626e7954574b7018cd6bae', 'rejected', 'Rejected', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('fd8f43bfacabba787ebb62bb2b1ac54e', '916447d1fb626e7954574b7018cd6bae', 'cancelled', 'Cancelled', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6082da52bfc4086bce0b76dc46e99fe9', '916447d1fb626e7954574b7018cd6bae', 'review', 'In Review', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('c4ef6110dc2143883fa7dd60ceeb9276', '916447d1fb626e7954574b7018cd6bae', 'active', 'Active', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f5ab5ccb7963c59324a6b259bcd2a047', '916447d1fb626e7954574b7018cd6bae', 'pending', 'Under pre-publication review', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('15a698931c78b2c725264b6dbbb29173', '916447d1fb626e7954574b7018cd6bae', 'draft', 'Draft', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

INSERT INTO value_set (value_set_id, code, name, oid, description, version, version_description, version_effective_date, version_expiration_date, version_status, value_set_type, source, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('65bcbe43e170e1a3c2a53aa77a313aa4', 'TemplateStatusCodeLifeCycle', 'Template Status Code Life Cycle', '2.16.840.1.113883.3.1937.98.11.8', 'Life cycle of the Status Code of a Template Design (Version)', '2013-12-05', NULL, NULL, NULL, 'Final', 'STATIC', 'http://art-decor.org/art-decor/decor-valuesets--tmpldstu-?valueSetRef=2.16.840.1.113883.3.1937.98.11.8', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

INSERT INTO value_set_cds_code_rel (value_set_cds_code_rel_id, value_set_id, code_id, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('e96e53d74cb056c8c3f5d8fa728c1710', '65bcbe43e170e1a3c2a53aa77a313aa4', '15a698931c78b2c725264b6dbbb29173', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO value_set_cds_code_rel (value_set_cds_code_rel_id, value_set_id, code_id, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b913168ac4f0206083ae45c64547f52b', '65bcbe43e170e1a3c2a53aa77a313aa4', '6082da52bfc4086bce0b76dc46e99fe9', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO value_set_cds_code_rel (value_set_cds_code_rel_id, value_set_id, code_id, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('cafe2303d1fcb619a055c014969bfce8', '65bcbe43e170e1a3c2a53aa77a313aa4', '62ba36472b50f5479b893af9e7fdf41b', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO value_set_cds_code_rel (value_set_cds_code_rel_id, value_set_id, code_id, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('3c59f3c95c4792304d38e789e7884d73', '65bcbe43e170e1a3c2a53aa77a313aa4', 'c4ef6110dc2143883fa7dd60ceeb9276', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO value_set_cds_code_rel (value_set_cds_code_rel_id, value_set_id, code_id, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6e94d0c7a07023c98238c24c1f309a17', '65bcbe43e170e1a3c2a53aa77a313aa4', 'f35cfe9d79b79835d882767052d26051', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO value_set_cds_code_rel (value_set_cds_code_rel_id, value_set_id, code_id, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('78e4c748916c568e31ee2ea9f44c7f35', '65bcbe43e170e1a3c2a53aa77a313aa4', 'f5ab5ccb7963c59324a6b259bcd2a047', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO value_set_cds_code_rel (value_set_cds_code_rel_id, value_set_id, code_id, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('64e9bf546c425128bcf6448dd94c838b', '65bcbe43e170e1a3c2a53aa77a313aa4', 'fd4c9745484c178f871eda9dafaf3b70', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO value_set_cds_code_rel (value_set_cds_code_rel_id, value_set_id, code_id, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('da5309fb5753af638fa283768a82c84a', '65bcbe43e170e1a3c2a53aa77a313aa4', 'fd8f43bfacabba787ebb62bb2b1ac54e', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

INSERT INTO cds_list (list_id, list_type, code, name, description, code_system_id, value_set_id, opencds_concept_type_id, enum_class_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a1ca302dcd10cc26f8a2b7bf357d3217', 'VALUE_SET', 'TemplateStatusCodeLifeCycle', 'Template Status Code Life Cycle List', NULL, NULL, '65bcbe43e170e1a3c2a53aa77a313aa4', NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('731567ebb0f1d15d29f90e12070d2068', 'a1ca302dcd10cc26f8a2b7bf357d3217', 'VALUE_SET', NULL, NULL, 'e96e53d74cb056c8c3f5d8fa728c1710', NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('bcb77d12629dc30298e345a292c593d0', 'a1ca302dcd10cc26f8a2b7bf357d3217', 'VALUE_SET', NULL, NULL, 'b913168ac4f0206083ae45c64547f52b', NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('450b54d2416f87facbe909854803d7a5', 'a1ca302dcd10cc26f8a2b7bf357d3217', 'VALUE_SET', NULL, NULL, 'cafe2303d1fcb619a055c014969bfce8', NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f3952d895f3829bda331f16070d3d51a', 'a1ca302dcd10cc26f8a2b7bf357d3217', 'VALUE_SET', NULL, NULL, '3c59f3c95c4792304d38e789e7884d73', NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a3d550afbce620cf20545764de959ca1', 'a1ca302dcd10cc26f8a2b7bf357d3217', 'VALUE_SET', NULL, NULL, '6e94d0c7a07023c98238c24c1f309a17', NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('34722007368c25f7f4915de211423ac2', 'a1ca302dcd10cc26f8a2b7bf357d3217', 'VALUE_SET', NULL, NULL, '78e4c748916c568e31ee2ea9f44c7f35', NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('5ec769dcb7cd2b32ed692735daee5c2e', 'a1ca302dcd10cc26f8a2b7bf357d3217', 'VALUE_SET', NULL, NULL, '64e9bf546c425128bcf6448dd94c838b', NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('4f603d8dfe5a6feac26027df35ca9f14', 'a1ca302dcd10cc26f8a2b7bf357d3217', 'VALUE_SET', NULL, NULL, 'da5309fb5753af638fa283768a82c84a', NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');



-- //@UNDO
-- SQL to undo the change goes here.

delete from cds_list_item where list_id = 'a1ca302dcd10cc26f8a2b7bf357d3217';
delete from cds_list where list_id = 'a1ca302dcd10cc26f8a2b7bf357d3217';
delete from value_set_cds_code_rel where value_set_id = '65bcbe43e170e1a3c2a53aa77a313aa4';
delete from value_set where value_set_id = '65bcbe43e170e1a3c2a53aa77a313aa4';
delete from cds_code where code_system_id = '916447d1fb626e7954574b7018cd6bae';
delete from cds_code_system where code_system_id = '916447d1fb626e7954574b7018cd6bae';
