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

-- // data_template
-- Migration SQL that makes the change goes here.

create table data_template (
            template_id varchar(32) primary key,
            oid varchar(128) not null,
            code varchar(128) not null,
            name varchar(512) not null,
            description varchar(2048),
            model_id varchar(32) not null,
            class_id varchar(32) not null,
            effective_date timestamp,
            expiration_date timestamp,
            status varchar(32) not null,
            restriction_allowed boolean,
            data_expected varchar(2048),
            back_restriction boolean,
            forward_restriction boolean,
            number_restriction boolean,
            max_rate_restriction boolean,
            last_mod_datetime timestamp not null,
            last_mod_id varchar(32) not null,
            create_datetime timestamp not null,
            create_id varchar(32) not null);

alter table data_template add constraint un_rckms_dt_name unique (name);

alter table data_template add constraint fk_dt_mid_2_dm_mid foreign key (model_id) references data_model (model_id);

alter table data_template add constraint fk_dt_cid_2_dmc_cid foreign key (class_id) references data_model_class (class_id);

alter table data_template add constraint fk_dt_cid_2_cc_cid foreign key (status) references cds_code (code_id);

-- //@UNDO
-- SQL to undo the change goes here.

drop table data_template;
