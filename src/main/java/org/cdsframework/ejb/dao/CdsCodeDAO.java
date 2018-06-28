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
import java.util.Map.Entry;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.cdsframework.base.BaseDAO;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.callback.QueryCallback;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.enumeration.CodedElementType;
import org.cdsframework.enumeration.Operator;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.group.ByGeneralProperties;
import org.cdsframework.group.FindAll;
import org.cdsframework.util.StringUtils;
import org.cdsframework.util.support.CoreConstants;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
@LocalBean
public class CdsCodeDAO extends BaseDAO<CdsCodeDTO> {

    @Override
    protected void initialize() throws MtsException {

        registerDML(FindAll.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String sql = getSelectDML();
                String partId = (String) baseDTO.getQueryMap().get("partId");
                if (!StringUtils.isEmpty(partId)) {
                    sql = "select * from v_cds_code_collection";
                    setWildcardPredicateValue(baseDTO, " lower(part_id) like :part_id ", "partId");
                    sql += getAndClearPredicateMap(" where ", "", Operator.AND);
                }
                logger.info("FindAll sql=", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                setLowerQueryMapValue(baseDTO, "part_id", "partId", namedParameters);
            }

        }, false);

        registerDML(CdsCodeDTO.ByCodeCollection.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String sql = "select * from v_cds_code_collection";
                Map<String, Object> queryMap = baseDTO.getQueryMap();

                boolean whereIncluded = false;
                String text = (String) queryMap.get("text");
                if (!StringUtils.isEmpty(text)) {
                    setWildcardPredicateValue(baseDTO, " lower(display_name) like :text ", "text");
                    setWildcardPredicateValue(baseDTO, " lower(code) like :text ", "text");
                    sql += getAndClearPredicateMap(whereIncluded ? " and " : " where " + "( ", " ) ", Operator.OR);
                    whereIncluded = true;
                }
                String partId = (String) queryMap.get("partId");
                if (!StringUtils.isEmpty(partId)) {
                    setWildcardPredicateValue(baseDTO, " lower(part_id) like :part_id ", "partId");
                    sql += getAndClearPredicateMap(whereIncluded ? " and " : " where ", "", Operator.AND);
                    whereIncluded = true;
                }
                logger.info("ByCodeCollection sql=", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
                setLowerQueryMapValue(baseDTO, "part_id", "partId", namedParameters);
            }

        }, false);

        registerDML(ByGeneralProperties.class, new QueryCallback(getDtoTableName()) {
            @Override
            public String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String sql = getSelectDML() + " left outer join value_set_cds_code_rel vsccr on " + getTableAlias() + "code_id = vsccr.code_id";
//                String sql = "select cc.*, ccs.oid, ccs.name "
//                        + " from cds_code cc left outer join value_set_cds_code_rel vsccr on cc.code_id = vsccr.code_id, "
//                        + " cds_code_system ccs "
//                        + " where cc.code_system_id = ccs.code_system_id";
                Map<String, Object> queryMap = baseDTO.getQueryMap();

                String text = (String) queryMap.get("text");
                logger.info("TEXT: ", text);

                boolean whereIncluded = false;
                if (text != null) {
                    setWildcardPredicateValue(baseDTO, " lower(display_name) like :text ", "text");
                    setWildcardPredicateValue(baseDTO, " lower(code) like :text ", "text");
                    sql += getAndClearPredicateMap(whereIncluded ? " and " : " where " + "( ", " ) ", Operator.OR);
                    whereIncluded = true;
                }
                String codeSystemId = (String) queryMap.get("codeSystemId");
                logger.info("CODE SYSTEM ID: ", codeSystemId);

                String codeSourceId = (String) queryMap.get("code_source_id");
                logger.info("CODE SOURCE ID: ", codeSourceId);
                if (codeSourceId == null && codeSystemId != null) {
                    codeSourceId = codeSystemId;
                    queryMap.put("code_source_id", queryMap.get("codeSystemId"));
                }

                if (codeSourceId != null) {
                    setNonWildcardPredicateValue(baseDTO, " code_system_id = :code_source_id", "code_source_id");
                    setNonWildcardPredicateValue(baseDTO, " vsccr.value_set_id = :code_source_id", "code_source_id");
                    sql += getAndClearPredicateMap((whereIncluded ? " and " : " where ") + "( ", " ) ", Operator.OR);
                    whereIncluded = true;
                }

                CodedElementType codedElementType = (CodedElementType) queryMap.get("source_type_filter");
                String[] sourceIdFilter = (String[]) queryMap.get("source_id_filter");
                logger.info("SOURCE TYPE FILTER: ", codedElementType);
                logger.info("SOURCE ID FILTER: ", sourceIdFilter != null ? StringUtils.getStringFromArray(", ", sourceIdFilter) : null);

                if (codeSourceId == null && sourceIdFilter != null && sourceIdFilter.length > 0) {
                    int c = 0;
                    for (String id : sourceIdFilter) {
                        String param = "source" + c;
                        queryMap.put(param, id);
                        setNonWildcardPredicateValue(baseDTO, " code_system_id = :" + param + " ", param);
                        setNonWildcardPredicateValue(baseDTO, " vsccr.value_set_id = :" + param + " ", param);
                        c++;
                    }

                    sql += getAndClearPredicateMap((whereIncluded ? " and " : " where ") + "( ", " ) ", Operator.OR);
                }
                logger.info("ByGeneralProperties sql: ", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
                setLowerQueryMapValue(baseDTO, "code_source_id", "code_source_id", namedParameters);
                String[] sourceIdFilter = (String[]) baseDTO.getQueryMap().get("source_id_filter");
                if (sourceIdFilter != null && sourceIdFilter.length > 0) {
                    int c = 0;
                    for (String id : sourceIdFilter) {
                        String param = "source" + c;
                        setExactQueryMapValue(baseDTO, param, param, namedParameters);
                        c++;
                    }
                }

                for (Entry<String, Object> entry : namedParameters.getValues().entrySet()) {
                    logger.info("ByGeneralProperties getCallbackNamedParameter: ", entry.getKey(), " - ", entry.getValue());
                }
            }

        }, false);

        registerDML(CdsCodeDTO.ByCodeSystemOid.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                Map<String, Object> queryMap = baseDTO.getQueryMap();
                logger.info("queryMap=", queryMap);
                boolean whereIncluded = false;
                String sql = getSelectDML();
                String text = (String) queryMap.get("text");
                logger.info("ByCodeSystemOid text=", text);
                if (!StringUtils.isEmpty(text)) {
                    setWildcardPredicateValue(baseDTO, " lower(display_name) like :text ", "text");
                    setWildcardPredicateValue(baseDTO, " lower(code) like :text ", "text");
                    sql += getAndClearPredicateMap(whereIncluded ? " and " : " where " + "( ", " ) ", Operator.OR);
                    whereIncluded = true;
                }
                String oid = (String) queryMap.get("oid");
                logger.info("ByCodeSystemOid oid=", oid);
                if (!StringUtils.isEmpty(oid)) {
                    setNonWildcardPredicateValue(baseDTO, " oid = :oid ", "oid");
                    sql += getAndClearPredicateMap(whereIncluded ? " and " : " where ", "", Operator.AND);
                }
                logger.info("ByCodeSystemOid sql=", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                if (baseDTO != null) {
                    setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
                    setExactQueryMapValue(baseDTO, "oid", "oid", namedParameters);
                } else {
                    logger.error("CdsCodeDTO.ByCodeSystemCode: baseDTO is null!");
                }
            }

        }, false);

        registerDML(CdsCodeDTO.ByCodeSystemCode.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String sql = getSelectDML() + " where lower(oid) = :oid and lower(code) = :code";
                logger.info("ByCodeSystemCode sql=", sql);
                return sql;
//                return "select cc.* from cds_code cc, cds_code_system ccs where cc.code_system_id = ccs.code_system_id and lower(ccs.oid) = :oid and lower(cc.code) = :code";
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                if (baseDTO != null) {
                    if (baseDTO instanceof CdsCodeDTO) {
                        CdsCodeDTO cdsCodeDTO = (CdsCodeDTO) baseDTO;
                        if (cdsCodeDTO.getCode() != null) {
                            if (cdsCodeDTO.getCodeSystem() != null) {
                                namedParameters.addValue("code", cdsCodeDTO.getCode().toLowerCase());
                                namedParameters.addValue("oid", cdsCodeDTO.getCodeSystem().toLowerCase());
                                logger.info("ByCodeSystemCode code=", cdsCodeDTO.getCode().toLowerCase());
                                logger.info("ByCodeSystemCode oid=", cdsCodeDTO.getCodeSystem().toLowerCase());
                            } else {
                                logger.error("CdsCodeDTO.ByCodeSystemCode: cdsCodeDTO.getCodeSystem() is null!");
                            }
                        } else {
                            logger.error("CdsCodeDTO.ByCodeSystemCode: cdsCodeDTO.getCode() is null!");
                        }
                    } else {
                        logger.error("CdsCodeDTO.ByCodeSystemCode: unexpected DTO type - " + baseDTO.getClass());
                    }
                } else {
                    logger.error("CdsCodeDTO.ByCodeSystemCode: baseDTO is null!");
                }
            }

        }, false);

        registerDML(CdsCodeDTO.ByTestIdCodeSystemOidDisease.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
//                String oid = (String) baseDTO.getQueryMap().get("oid");
//                String testId = (String) baseDTO.getQueryMap().get("test_id");
//                logger.info("ByTestIdCodeSystemOidDisease oid=", oid);
//                logger.info("ByTestIdCodeSystemOidDisease testId=", testId);
                String sql = " select * from vw_ice_test_disease_code where test_id = :test_id and oid != :oid ";

                String text = (String) baseDTO.getQueryMap().get("text");
                logger.info("ByTestIdCodeSystemOidDisease text=", text);
                if (!StringUtils.isEmpty(text)) {
                    setWildcardPredicateValue(baseDTO, " lower(display_name) like :text ", "text");
                    setWildcardPredicateValue(baseDTO, " lower(code) like :text ", "text");
                    setWildcardPredicateValue(baseDTO, " lower(oid) like :text ", "text");
                    setWildcardPredicateValue(baseDTO, " lower(name) like :text ", "text");
                    sql += getAndClearPredicateMap(" and ( ", " ) ", Operator.OR);
                }

                logger.info("ByTestIdCodeSystemOidDisease sql=", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
                    throws MtsException {
                setExactQueryMapValue(baseDTO, "test_id", "test_id", namedParameters);
                setExactQueryMapValue(baseDTO, "oid", "oid", namedParameters);
            }

        }, false);

        registerDML(CdsCodeDTO.ByCodeSystemId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String METHODNAME = "getQueryDML ";
                Map<String, Object> filterMap = (Map) baseDTO.getQueryMap().get(CoreConstants.FILTERS);
                String sql = getSelectDML() + " where code_system_id = :code_system_id";
//                String sql = "select * from cds_code where code_system_id = :code_system_id";
                if (filterMap != null) {
                    String code = (String) filterMap.get("code");
                    String displayName = (String) filterMap.get("displayName");
                    if (code != null && !StringUtils.isEmpty(code)) {
                        baseDTO.getQueryMap().put("code", code.trim());
                        setWildcardPredicateValue(baseDTO, " lower(code) like :code ", "code");
                    }
                    if (displayName != null && !StringUtils.isEmpty(displayName)) {
                        baseDTO.getQueryMap().put("displayName", displayName.trim());
                        setWildcardPredicateValue(baseDTO, " lower(display_name) like :display_name ", "displayName");
                    }
                }
                sql += getAndClearPredicateMap(" and ( ", " ) ", Operator.OR);
                logger.debug(METHODNAME, "filterMap=", filterMap);
                logger.debug(METHODNAME, "ByCodeSystemId sql=", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                if (baseDTO != null) {
                    Map<String, Object> filterMap = (Map) baseDTO.getQueryMap().get(CoreConstants.FILTERS);
                    String codeSystemId;
                    logger.debug("getCallbackNamedParameters filterMap: ", filterMap);

                    if (baseDTO instanceof CdsCodeDTO) {
                        CdsCodeDTO cdsCodeDTO = (CdsCodeDTO) baseDTO;
                        codeSystemId = cdsCodeDTO.getCodeSystemId();
                    } else if (baseDTO instanceof CdsCodeSystemDTO) {
                        CdsCodeSystemDTO cdsCodeSystemDTO = (CdsCodeSystemDTO) baseDTO;
                        codeSystemId = cdsCodeSystemDTO.getCodeSystemId();
                    } else {
                        throw new MtsException("Unexpected DTO type: " + baseDTO.getClass().getCanonicalName());
                    }
                    if (filterMap != null) {
                        setLowerQueryMapValue(baseDTO, "display_name", "displayName", namedParameters);
                        setLowerQueryMapValue(baseDTO, "code", "code", namedParameters);
                    }
                    namedParameters.addValue("code_system_id", codeSystemId);
                } else {
                    throw new MtsException("baseDTO was null!");
                }
            }

        }, false);

    }
}
