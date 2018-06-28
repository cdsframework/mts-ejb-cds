/**
 * The MTS cds EJB project is the base framework for the CDS Framework Middle Tier Service.
 *
 * Copyright (C) 2016 New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * Contributions by HLN Consulting, LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. You should have received a copy of the GNU Lesser
 * General Public License along with this program. If not, see <http://www.gnu.org/licenses/> for more
 * details.
 *
 * The above-named contributors (HLN Consulting, LLC) are also licensed by the New York City
 * Department of Health and Mental Hygiene, Bureau of Immunization to have (without restriction,
 * limitation, and warranty) complete irrevocable access and rights to this project.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; THE
 *
 * SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING,
 * BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES, OR OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information about this software, see https://www.hln.com/services/open-source/ or send
 * correspondence to ice@hln.com.
 */
package org.cdsframework.ejb.dao;

import javax.ejb.Stateless;
import org.cdsframework.base.BaseDAO;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.callback.QueryCallback;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.CdsListDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.enumeration.Operator;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.group.ByGeneralProperties;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class CdsListDAO extends BaseDAO<CdsListDTO> {

    @Override
    protected void initialize() throws MtsException {

        this.registerDML(ByGeneralProperties.class, new QueryCallback(getDtoTableName()) {
            @Override
            public String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                setWildcardPredicateValue(baseDTO, " lower(name) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(list_id) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(code) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(description) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(enum_class_name) like :text ", "text");
                String sql = getSelectDML() + getAndClearPredicateMap(" where ", "", Operator.OR);
                logger.debug("SQL: ", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
                setLowerQueryMapValue(baseDTO, "list_type", "list_type", namedParameters);
            }

        }, false);

        this.registerDML(CdsListDTO.ByLowerCode.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list where lower(code) = :code";
            }
        }, false);

        this.registerDML(CdsListDTO.ByCodeSystem.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list where code_system_id = :code_system_id";
            }
        }, false);

        this.registerDML(CdsListDTO.ByCodeSystemListType.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list where code_system_id = :code_system_id and list_type = :list_type";
            }
        }, false);

        this.registerDML(CdsListDTO.ByCdsListType.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list where list_type = :list_type";
            }
        }, false);

        this.registerDML(CdsListDTO.ByOpenCdsConcept.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select distinct cl.* from cds_list cl, cds_list_item cli where cl.list_id = cli.list_id and cli.opencds_code_id = :opencds_code_id";
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                logger.info("DAO CdsListDTO.ByOpenCdsConcept called!");
                CdsListDTO parameter = (CdsListDTO) baseDTO;
                OpenCdsConceptDTO openCdsConceptDTO = (OpenCdsConceptDTO) parameter.getQueryMap().get("openCdsConceptDTO");
                namedParameters.addValue("opencds_code_id", openCdsConceptDTO != null ? openCdsConceptDTO.getCodeId() : null);
            }

        }, false);

        this.registerDML(CdsListDTO.ByValueSet.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list where value_set_id = :value_set_id";
            }
        }, false);

        this.registerDML(CdsListDTO.ByCodeVersionId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list cl, cds_list_version_rel clvr where clvr.list_id = cl.list_id and cl.code = :code and clvr.version_id";
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                logger.info("DAO CdsListDTO.ByCodeVersionId called!");
                CdsListDTO parameter = (CdsListDTO) baseDTO;
                namedParameters.addValue("code", parameter.getCode());
                namedParameters.addValue("version_id", propertyBagDTO.get("version_id"));
                logger.info("DAO CdsListDTO.ByValueSet: ", namedParameters.getValue("code"), " - ", namedParameters.getValue("version_id"));
            }

        }, false);
    }
}
