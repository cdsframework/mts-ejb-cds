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

import java.util.Map;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.cdsframework.base.BaseDAO;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.callback.QueryCallback;
import org.cdsframework.dto.CdsBusinessScopeDTO;
import org.cdsframework.dto.CdsVersionDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.enumeration.Operator;
import org.cdsframework.enumeration.Status;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.group.ByGeneralProperties;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
@LocalBean
public class CdsVersionDAO extends BaseDAO<CdsVersionDTO> {

    @Override
    protected void initialize() throws MtsException {

        this.registerDML(ByGeneralProperties.class, new QueryCallback(getDtoTableName()) {
            @Override
            public String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                setWildcardPredicateValue(baseDTO, " lower(name) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(description) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(version) like :text ", "text");
                String result = getSelectDML() + getAndClearPredicateMap(" where ", "", Operator.OR);
                logger.warn(result);
                return result;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
            }

        }, false);

        this.registerDML(CdsVersionDTO.ByBusinessScopeIdVersion.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from vw_cds_version where business_scope_id = :business_scope_id and version = :version";
            }
        }, false);

        this.registerDML(CdsVersionDTO.ByBusinessIdScopeEntityIdVersion.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from vw_cds_version where business_id = :business_id and scoping_entity_id = :scoping_entity_id and version = :version";
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
                    throws MtsException {
                setExactQueryMapValue(baseDTO, "business_id", "business_id", namedParameters);
                setExactQueryMapValue(baseDTO, "scoping_entity_id", "scoping_entity_id", namedParameters);
                setExactQueryMapValue(baseDTO, "version", "version", namedParameters);

            }

        }, false);

        this.registerDML(CdsVersionDTO.ByBusinessScopeId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String sql = "select * from vw_cds_version where business_scope_id = :business_scope_id";
                logger.info("getQueryDML ByBusinessScopeId sql = ", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
                    throws MtsException {
                String businessScopeId = null;
                if (baseDTO instanceof CdsVersionDTO) {
                    CdsVersionDTO cdsVersionDTO = (CdsVersionDTO) baseDTO;
                    logger.info("ByBusinessScopeId getCallbackNamedParameters - cdsVersionDTO.getBusinessScopeId() - ", cdsVersionDTO.getBusinessScopeId());
                    logger.info("ByBusinessScopeId getCallbackNamedParameters - cdsVersionDTO.getQueryMap() - ", cdsVersionDTO.getQueryMap());
                    if (cdsVersionDTO.getBusinessScopeId() != null) {
                        businessScopeId = cdsVersionDTO.getBusinessScopeId();
                    } else if (cdsVersionDTO.getQueryMap().get("businessScopeId") != null) {
                        businessScopeId = (String) cdsVersionDTO.getQueryMap().get("businessScopeId");
                    } else {
                        logger.warn("getCallbackNamedParameters ByBusinessScopeId businessScopeId was not found.");
                    }
                } else if (baseDTO instanceof CdsBusinessScopeDTO) {
                    CdsBusinessScopeDTO cdsBusinessScopeDTO = (CdsBusinessScopeDTO) baseDTO;
                    logger.info("ByBusinessScopeId getCallbackNamedParameters - cdsBusinessScopeDTO.getBusinessScopeId() - ", cdsBusinessScopeDTO.getBusinessScopeId());
                    if (cdsBusinessScopeDTO.getBusinessScopeId() != null) {
                        businessScopeId = cdsBusinessScopeDTO.getBusinessScopeId();
                    }
                } else {
                    logger.warn("getCallbackNamedParameters ByBusinessScopeId businessScopeId was not found.");
                }
                logger.info("getCallbackNamedParameters ByBusinessScopeId businessScopeId = ", businessScopeId);
                namedParameters.addValue("business_scope_id", businessScopeId);
            }

        }, false);

        this.registerDML(CdsVersionDTO.ByBusinessScopeIdStatus.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from vw_cds_version where business_scope_id = :business_scope_id and status = :status";
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
                    throws MtsException {
                final String METHODNAME = "ByBusinessScopeIdStatus getCallbackNamedParameters ";
                if (baseDTO instanceof CdsVersionDTO) {
                    CdsVersionDTO cdsVersionDTO = (CdsVersionDTO) baseDTO;
                    Map<String, Object> queryMap = cdsVersionDTO.getQueryMap();
                    String status = null;
                    Status statusEnum = cdsVersionDTO.getStatus();
                    if (statusEnum != null) {
                        status = statusEnum.toString();
                    } else {
                        Object queryMapStatus = queryMap.get("status");
                        if (queryMapStatus != null) {
                            status = (String) queryMapStatus;
                        }
                        if (status == null) {
                            Object propertyBagStatus = propertyBagDTO.get("status");
                            if (propertyBagStatus != null) {
                                status = (String) propertyBagStatus;
                            }
                        }
                    }
                    logger.info(METHODNAME, "status=", status);
                    namedParameters.addValue("status", status);
                    String businessScopeId = cdsVersionDTO.getBusinessScopeId();
                    if (businessScopeId == null) {
                        Object queryMapBusinessScopeId = queryMap.get("businessScopeId");
                        if (queryMapBusinessScopeId != null) {
                            businessScopeId = (String) queryMapBusinessScopeId;
                        }
                        if (businessScopeId == null) {
                            Object propertyBagBusinessScopeId = propertyBagDTO.get("businessScopeId");
                            if (propertyBagBusinessScopeId != null) {
                                businessScopeId = (String) propertyBagBusinessScopeId;
                            }
                        }
                    }
                    logger.info(METHODNAME, "businessScopeId=", businessScopeId);
                    namedParameters.addValue("business_scope_id", businessScopeId);
                }
            }

        }, false);
    }
}
