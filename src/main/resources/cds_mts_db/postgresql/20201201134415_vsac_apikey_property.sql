--
-- The RCKMS EJB cdsframework implementation.
--
-- Copyright 2016 HLN Consulting, LLC
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
-- For more information about the this software, see https://www.hln.com/services/open-source/ or send
-- correspondence to scm@cdsframework.org.
--

-- // set default cdm
-- Migration SQL that makes the change goes here.

INSERT INTO system_property (property_id, name, type, property_group, scope, value, obscure, mts_only, last_mod_datetime, last_mod_id, create_datetime, create_id)
    VALUES ((md5(random()::text || now()::text)::cstring), 'VSAC_APIKEY', 'java.lang.String', NULL, 'cds', '97019c57-46fe-4f21-ab7d-a9d57443aebc', false, false, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- //@UNDO
-- SQL to undo the change goes here.

delete from system_property where name = 'VSAC_APIKEY' and scope = 'cds';