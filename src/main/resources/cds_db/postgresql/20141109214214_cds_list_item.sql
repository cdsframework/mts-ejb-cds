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

-- // cds_list_item
-- Migration SQL that makes the change goes here.

create table cds_list_item (
            item_id varchar(32) primary key,
            list_id varchar(32) not null,
            item_type varchar(32) not null,
            code_id varchar(32),
            opencds_code_id varchar(32),
            value_set_cds_code_rel_id varchar(32),
            opencds_concept_rel_id varchar(32),
            ad_hoc_id varchar(2000),
            ad_hoc_label varchar(2000),
            last_mod_datetime timestamp not null,
            last_mod_id varchar(32) not null,
            create_datetime timestamp not null,
            create_id varchar(32) not null);


alter table cds_list_item add constraint fk_clioccrid2occrrid foreign key (opencds_concept_rel_id) references opencds_concept_rel (relationship_id);

alter table cds_list_item add constraint fk_cds_list_lid foreign key (list_id) references cds_list (list_id);

alter table cds_list_item add constraint fk_clicid2cccid foreign key (code_id) references cds_code (code_id);

alter table cds_list_item add constraint fk_cliocid2occid foreign key (opencds_code_id) references opencds_concept (code_id);

alter table cds_list_item add constraint fk_clivsccrid2vsccrid foreign key (value_set_cds_code_rel_id) references value_set_cds_code_rel (value_set_cds_code_rel_id);

INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('94fafac49c5315ae817a6849dc7ceab7', '29c22c5c11ad74b72980a93a805fd951', 'AD_HOC_CONCEPT', NULL, 'a295ba83953c06b459042aff294d2f73', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('28c722f3655578ea9a08b9f43157e204', '29c22c5c11ad74b72980a93a805fd951', 'AD_HOC_CONCEPT', NULL, 'fa72f597d1d47f25e6cb27d463535b90', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_list_item (item_id, list_id, item_type, code_id, opencds_code_id, value_set_cds_code_rel_id, opencds_concept_rel_id, ad_hoc_id, ad_hoc_label, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f47d4fadd322238c9ad2ac9d57d3a3de', '29c22c5c11ad74b72980a93a805fd951', 'AD_HOC_CONCEPT', NULL, 'd2f0f724cb69f5d7fc47430f67ccf9af', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

drop table cds_list_item;
