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

-- // cds_code
-- Migration SQL that makes the change goes here.

create table cds_code (
            code_id varchar(32) primary key,
            code_system_id varchar(32) not null,
            code varchar(256) not null,
            display_name varchar(256),
            last_mod_datetime timestamp not null,
            last_mod_id varchar(32) not null,
            create_datetime timestamp not null,
            create_id varchar(32) not null);

alter table cds_code add constraint cdsc_csid_2_cdscs_csid foreign key (code_system_id) references cds_code_system (code_system_id);

alter table cds_code add constraint un_cccsidc unique (code_system_id, code);

-- languages
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('77df64143758f8f3ef4eb224349f6c82', '2c9ee278e930742dd9753e81933bae79', 'eng', 'english', CURRENT_TIMESTAMP, '8d8a9330a4f6e9f5af131df5c5ec6c7d', CURRENT_TIMESTAMP, '8d8a9330a4f6e9f5af131df5c5ec6c7d');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f7ef5b2cb0163e7c2f49a830f2cb0f05', '3d922f54ce6043c26e2d94b97db12fc6', 'en ', 'English', CURRENT_TIMESTAMP, '8d8a9330a4f6e9f5af131df5c5ec6c7d', CURRENT_TIMESTAMP, 'admin');

-- genders
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('537030c4d4a618263258a7d819b71b16', '2b4c99853b45d76f572ef1c6ac339901', 'M', 'Male', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('161a3206589043b77094ef410e707121', '2b4c99853b45d76f572ef1c6ac339901', 'F', 'Female', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a3e55f26912c2759407e01d34373ff50', '2b4c99853b45d76f572ef1c6ac339901', 'UN', 'Undifferentiated', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- icd9 diagnosis codes
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('1373d79652d8ae8027785a4ce670c0a7', '734d727366047ef1b6292d310d36b657', '795.0', 'Human Papillomavirus', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('3062ef2e4282ab57d8a5b0113d0bf267', '734d727366047ef1b6292d310d36b657', '008.61', 'Rotavirus', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('56b47c5fc851f3a32ff594281990b5e6', '734d727366047ef1b6292d310d36b657', '487', 'Influenza', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('59b23f3bd35da1477e29b2464ed5c525', '734d727366047ef1b6292d310d36b657', '070.1', 'Hepatitis A', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('5d5271debc5f5605cc16adca2600bbaa', '734d727366047ef1b6292d310d36b657', '052.9', 'Varicella', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('78c90abce2b98b431963f8fd9d9df353', '734d727366047ef1b6292d310d36b657', '481', 'Pneumococcal Pneumonia', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('7c2c9ca01cbd1148dd8e381dc062cd21', '734d727366047ef1b6292d310d36b657', '070.30', 'Hepatitis B', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('812f35323238c662608f9f47904bcc56', '734d727366047ef1b6292d310d36b657', '488.1', 'H1N1 Influenza', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('9d92df621a474bfa8a2d1b312002dcc5', '734d727366047ef1b6292d310d36b657', '056.9', 'Rubella', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b7c0ee3e3f7b6f85799fbaf15493f8dd', '734d727366047ef1b6292d310d36b657', '482.2', 'Hib', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('bf56c457d473e84a6c38847c61e6a4fd', '734d727366047ef1b6292d310d36b657', '036.9', 'Meningococcal', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('c5afe2a5f824a9305ccb51c9231b036c', '734d727366047ef1b6292d310d36b657', '033.9', 'Pertussis', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('c97adc04eccb8a88c3e3aa29289ff35d', '734d727366047ef1b6292d310d36b657', '055.9', 'Measles', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('d82844df4fbf6cfa55dc91dbe47b3e9b', '734d727366047ef1b6292d310d36b657', '032.9', 'Diphtheria', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('db3fa0b93c44bdb68365c09e377770a3', '734d727366047ef1b6292d310d36b657', '037', 'Tetanus', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('db8028ae56f91975c57f026ed9a9c321', '734d727366047ef1b6292d310d36b657', '045.9', 'Acute Poliomyelitis', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f5373142f662ddc367bfee9244d279d2', '734d727366047ef1b6292d310d36b657', '072.9', 'Mumps', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- manufacturers
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('1863880b6e4d58172fa038d810f75201', 'f47e38fdcc2340ae7282963bf26c26ca', 'BPC', 'Berna Products Corporation', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('1b890d6818b9f23c9dbbf29c901b0f73', 'f47e38fdcc2340ae7282963bf26c26ca', 'MIP', 'Emergent BioDefense Operations Lansing', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('1d113dd3bd637c421d93bfd8f3bd0243', 'f47e38fdcc2340ae7282963bf26c26ca', 'ALP', 'Alpha Therapeutic Corporation', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('21e1fa8e8288d90ef82ba31ad0ec2533', 'f47e38fdcc2340ae7282963bf26c26ca', 'MSD', 'Merck & Co., Inc.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('25b59120536c4d04c871c1727db4c758', 'f47e38fdcc2340ae7282963bf26c26ca', 'WA', 'Wyeth-Ayerst', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('26452fcc28764cea33ce19e480816d2f', 'f47e38fdcc2340ae7282963bf26c26ca', 'MA', 'Massachusetts Public Health Biologic Laboratories', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('265cc59f91b05ffbe55a9c57271849d5', 'f47e38fdcc2340ae7282963bf26c26ca', 'AR', 'Armour', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('292a973b75c6220022ef4f06a0557ce0', 'f47e38fdcc2340ae7282963bf26c26ca', 'AB', 'Abbott Laboratories', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('2d2b0877eb6b0ab936c599ee4d0c4574', 'f47e38fdcc2340ae7282963bf26c26ca', 'CON', 'Connaught', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('2f90b2f66a0f3d29c0d09742f9adb601', 'f47e38fdcc2340ae7282963bf26c26ca', 'IM', 'Merieux', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('326e0520c7c1b8b2c450b5a533d825e3', 'f47e38fdcc2340ae7282963bf26c26ca', 'CMP', 'Celltech Medeva Pharmaceuticals', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('3dc8da0f5d053e86abf1c6a8a2044459', 'f47e38fdcc2340ae7282963bf26c26ca', 'NAV', 'North American Vaccine, Inc.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('4594c45798ffce2948fc6e1ed7b3325d', 'f47e38fdcc2340ae7282963bf26c26ca', 'BAY', 'Bayer Corporation', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('45d6166082d23a5f35db2927a136a892', 'f47e38fdcc2340ae7282963bf26c26ca', 'NOV', 'Novartis Pharmaceutical Corporation', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('4e3a992ce8d9fa1fd5fab146fa69deb5', 'f47e38fdcc2340ae7282963bf26c26ca', 'JPN', 'The Research Foundation for Microbial Diseases of Osaka University (BIKEN)', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('542e68d2591d45bf218391a6b078ae49', 'f47e38fdcc2340ae7282963bf26c26ca', 'SI', 'Swiss Serum and Vaccine Inst.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('554b5bc5a57660f597fe2c3c97777b57', 'f47e38fdcc2340ae7282963bf26c26ca', 'TAL', 'Talecris Biotherapeutics', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('559dd4757cf81d6ebedfa2a9e422f54a', 'f47e38fdcc2340ae7282963bf26c26ca', 'BRR', 'Barr Laboratories', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('55f10e16d432551f5821a3fce1840ce3', 'f47e38fdcc2340ae7282963bf26c26ca', 'CSL', 'CSL Behring, Inc', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('566ea92b696b41ef5758f6a57cea5b63', 'f47e38fdcc2340ae7282963bf26c26ca', 'MBL', 'Massachusetts Biologic Laboratories', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('57b78a337597c7a5dffdce9992a5b366', 'f47e38fdcc2340ae7282963bf26c26ca', 'KGC', 'Korea Green Cross Corporation', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('5817965582c6e366187b12e553c6d6f1', 'f47e38fdcc2340ae7282963bf26c26ca', 'USA', 'United States Army Medical Research and Material Command', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('616e896fe1abce494fe3d9b0f7cef3c9', 'f47e38fdcc2340ae7282963bf26c26ca', 'INT', 'Intercell Biomedical', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('622e7cfb8f4ef0db1d0642aeb4269141', 'f47e38fdcc2340ae7282963bf26c26ca', 'CEN', 'Centeon L.L.C.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6890bb034b190c88fa4a1d1daaa00c17', 'f47e38fdcc2340ae7282963bf26c26ca', 'DVC', 'DynPort Vaccine Company, LLC', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('6feb6a5a53fca35bd36ada75f4b9e033', 'f47e38fdcc2340ae7282963bf26c26ca', 'AVB', 'Aventis Behring L.L.C.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('750e6fd83c2c810aadf54a861014e76f', 'f47e38fdcc2340ae7282963bf26c26ca', 'ZLB', 'ZLB Behring', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('7d7d16fedcbb25ab3fb71e57a3f4f415', 'f47e38fdcc2340ae7282963bf26c26ca', 'CHI', 'Chiron Corporation', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('844afd15b6ef4324922cad6ade5539c2', 'f47e38fdcc2340ae7282963bf26c26ca', 'BP', 'Berna Products', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('84c63729d55bfe718beb7e0057a9abc8', 'f47e38fdcc2340ae7282963bf26c26ca', 'MED', 'MedImmune, Inc.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('863470d5d1438e785c0dc9169d0c50a7', 'f47e38fdcc2340ae7282963bf26c26ca', 'GRE', 'Greer Laboratories, Inc.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('934d0cfea0f1aaecfc223db69eab7269', 'f47e38fdcc2340ae7282963bf26c26ca', 'ORT', 'Ortho-clinical Diagnostics', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('95ca6cb8ef592c65629023761a27eee0', 'f47e38fdcc2340ae7282963bf26c26ca', 'SCL', 'Sclavo, Inc.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('97a1827fe6ac177cb2e5b70f424c805a', 'f47e38fdcc2340ae7282963bf26c26ca', 'BAH', 'Baxter Healthcare Corporation', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('984ed662151c4caecc8ec37b500d30c5', 'f47e38fdcc2340ae7282963bf26c26ca', 'CNJ', 'Cangene Corporation', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('9a9e927bac5275ae674f11dd32d09899', 'f47e38fdcc2340ae7282963bf26c26ca', 'BTP', 'Biotest Pharmaceuticals Corporation', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('9c1c156b68e760f3bec41040987a709d', 'f47e38fdcc2340ae7282963bf26c26ca', 'LED', 'Lederle', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a231424793fb7e0a93021fc7cf94b123', 'f47e38fdcc2340ae7282963bf26c26ca', 'GEO', 'GeoVax Labs, Inc.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('a3459b38bce442e97125ff443d56e2bf', 'f47e38fdcc2340ae7282963bf26c26ca', 'BA', 'Baxter Healthcare Corporation-inactive', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b338ec12576d06b8e7ac6fb87f2aa044', 'f47e38fdcc2340ae7282963bf26c26ca', 'AD', 'Adams Laboratories, Inc.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b7f0837c2bdb4e63a41b4c3adfc4f88f', 'f47e38fdcc2340ae7282963bf26c26ca', 'PRX', 'Praxis Biologics', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b8135ca5554fe22439d60b72e4d90cb9', 'f47e38fdcc2340ae7282963bf26c26ca', 'OTC', 'Organon Teknika Corporation', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b83685fbd105cf36f52cd5a6e0c92247', 'f47e38fdcc2340ae7282963bf26c26ca', 'VXG', 'VaxGen', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('b8eef8f1b7dea1a0a75ea2333bc5f34e', 'f47e38fdcc2340ae7282963bf26c26ca', 'PD', 'Parkedale Pharmaceuticals', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('bb22bebfd85a4a74f9568fa16dc86d0c', 'f47e38fdcc2340ae7282963bf26c26ca', 'SOL', 'Solvay Pharmaceuticals', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('bdfa6fde7c6515a22b5e8aef560b6c09', 'f47e38fdcc2340ae7282963bf26c26ca', 'SKB', 'GlaxoSmithKline', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('c405d476923a452aa22890344191f72a', 'f47e38fdcc2340ae7282963bf26c26ca', 'UNK', 'Unknown manufacturer', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('c475283ebe0f1529aecac0873818aa4d', 'f47e38fdcc2340ae7282963bf26c26ca', 'PWJ', 'PowderJect Pharmaceuticals', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('ca3784a1363da37ad6e8dc2ce5ce15ac', 'f47e38fdcc2340ae7282963bf26c26ca', 'IUS', 'Immuno-U.S., Inc.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('cb6e24f9eecd0d0dab17cda29367c8c4', 'f47e38fdcc2340ae7282963bf26c26ca', 'IAG', 'Immuno International AG', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('ccef73dd406263932fa0c2142a1acae0', 'f47e38fdcc2340ae7282963bf26c26ca', 'NAB', 'NABI', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('cfad7f2189f4a1ec8c63bc542496ed5c', 'f47e38fdcc2340ae7282963bf26c26ca', 'PFR', 'Pfizer, Inc', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('d57a0126c34b7eae93a98b9860f91efa', 'f47e38fdcc2340ae7282963bf26c26ca', 'WAL', 'Wyeth', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('d72bcf21e8db61fd22d13be1f021552f', 'f47e38fdcc2340ae7282963bf26c26ca', 'AVI', 'Aviron', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('d7d9df3f4f02dc3f42a5e881c380f5e7', 'f47e38fdcc2340ae7282963bf26c26ca', 'ACA', 'Acambis, Inc', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('ddd1db303a67ca1d75aba7778d952a4a', 'f47e38fdcc2340ae7282963bf26c26ca', 'NVX', 'Novavax, Inc.', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('e4cdc40cfb9c81a85e6b491d7e2eb248', 'f47e38fdcc2340ae7282963bf26c26ca', 'NYB', 'New York Blood Center', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('e5df3350dfbd4a7e4460e60991afecad', 'f47e38fdcc2340ae7282963bf26c26ca', 'MIL', 'Miles', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f03d3788311fccd25ab0ca66266f9cf2', 'f47e38fdcc2340ae7282963bf26c26ca', 'PMC', 'sanofi pasteur', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('f16d261a693fa3a5c238389cbd23e37c', 'f47e38fdcc2340ae7282963bf26c26ca', 'EVN', 'Evans Medical Limited', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('fbef283d3517c0fc5f0c0ff3c3610db5', 'f47e38fdcc2340ae7282963bf26c26ca', 'OTH', 'Other manufacturer', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO cds_code (code_id, code_system_id, code, display_name, last_mod_datetime, last_mod_id, create_datetime, create_id) 
    VALUES ('fc8a55ee5700bfbc8e1eee575178c6c4', 'f47e38fdcc2340ae7282963bf26c26ca', 'AKR', 'Akorn, Inc', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

drop table cds_code;
