/**
 * The MTS cds EJB project is the base framework for the CDS Framework Middle Tier Service.
 *
 * Copyright (C) 2016 New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * Contributions by HLN Consulting, LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. You should have received a copy of the GNU Lesser
 * General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/> for more details.
 *
 * The above-named contributors (HLN Consulting, LLC) are also licensed by the
 * New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * to have (without restriction, limitation, and warranty) complete irrevocable
 * access and rights to this project.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; THE
 *
 * SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING, BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR
 * OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information about this software, see
 * https://www.hln.com/services/open-source/ or send correspondence to
 * ice@hln.com.
 */
package org.cdsframework.ejb.dao;

import javax.ejb.Stateless;
import org.cdsframework.base.BaseDAO;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.callback.QueryCallback;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.dto.ValueSetDTO;
import org.cdsframework.enumeration.Operator;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.group.ByGeneralProperties;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class ValueSetDAO extends BaseDAO<ValueSetDTO> {

    @Override
    protected void initialize() throws MtsException {

        this.registerDML(ByGeneralProperties.class, new QueryCallback(getDtoTableName()) {
            @Override
            public String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                setWildcardPredicateValue(baseDTO, " lower(name) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(description) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(code) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(oid) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(version_description) like :text ", "text");
                String sql = getSelectDML() + getAndClearPredicateMap(" where ", "", Operator.OR);
                logger.info("SQL: ", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
                    throws MtsException {
                setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
            }

        }, false);

        this.registerDML(ValueSetDTO.ByOid.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from value_set where oid = :oid";
            }
        }, false);

        this.registerDML(ValueSetDTO.ByOidVersion.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from value_set where oid = :oid and version = :version";
            }
        }, false);

        this.registerDML(ValueSetDTO.ByCodeOidVersion.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from value_set where oid = :oid and version = :version and code = :code";
            }
        }, false);

        this.registerDML(ValueSetDTO.ByOidVersionStatus.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                if (baseDTO instanceof ValueSetDTO) {
                    ValueSetDTO valueSetDTO = (ValueSetDTO) baseDTO;
                    logger.info("ByOidVersionStatus valueSetDTO.getOid()=", valueSetDTO.getOid());
                    logger.info("ByOidVersionStatus valueSetDTO.getCode()=", valueSetDTO.getCode());
                    logger.info("ByOidVersionStatus valueSetDTO.getVersion()=", valueSetDTO.getVersion());
                    logger.info("ByOidVersionStatus valueSetDTO.getVersionStatus()=", valueSetDTO.getVersionStatus());
                }
                return "select * from value_set where oid = :oid and version_status = :version_status";
            }
        }, false);

        this.registerDML(ValueSetDTO.ByCodeOidVersionVersionStatus.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                if (baseDTO instanceof ValueSetDTO) {
                    ValueSetDTO valueSetDTO = (ValueSetDTO) baseDTO;
                    logger.info("ByOidVersionStatus valueSetDTO.getOid()=", valueSetDTO.getOid());
                    logger.info("ByOidVersionStatus valueSetDTO.getCode()=", valueSetDTO.getCode());
                    logger.info("ByOidVersionStatus valueSetDTO.getVersion()=", valueSetDTO.getVersion());
                    logger.info("ByOidVersionStatus valueSetDTO.getVersionStatus()=", valueSetDTO.getVersionStatus());
                }
                return "select * from value_set where oid = :oid and version = :version and code = :code and version_status = :version_status";
            }
        }, false);
    }
}
