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
import javax.ejb.Stateless;
import org.cdsframework.base.BaseDAO;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.callback.QueryCallback;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.dto.ValueSetCdsCodeRelDTO;
import org.cdsframework.enumeration.Operator;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.group.ByGeneralProperties;
import org.cdsframework.util.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class ValueSetCdsCodeRelDAO extends BaseDAO<ValueSetCdsCodeRelDTO> {

    @Override
    protected void initialize() throws MtsException {

        this.registerDML(ValueSetCdsCodeRelDTO.ByCodeId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from value_set_cds_code_rel where code_id = :code_id";
            }
        }, false);

        registerDML(ValueSetCdsCodeRelDTO.ByValueSetOid.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                Map<String, Object> queryMap = baseDTO.getQueryMap();
                logger.info("queryMap=", queryMap);
                boolean whereIncluded = false;
                String sql = "select vsccr.* from value_set_cds_code_rel vsccr join value_set vs on vs.value_set_id = vsccr.value_set_id join cds_code cc on cc.code_id = vsccr.code_id";
                String text = (String) queryMap.get("text");
                logger.info("ByValueSetOid text=", text);
                if (!StringUtils.isEmpty(text)) {
                    setWildcardPredicateValue(baseDTO, " lower(cc.display_name) like :text ", "text");
                    setWildcardPredicateValue(baseDTO, " lower(cc.code) like :text ", "text");
                    sql += getAndClearPredicateMap(whereIncluded ? " and " : " where " + "( ", " ) ", Operator.OR);
                    whereIncluded = true;
                }
                String oid = (String) queryMap.get("oid");
                logger.info("ByValueSetOid oid=", oid);
                if (!StringUtils.isEmpty(oid)) {
                    setNonWildcardPredicateValue(baseDTO, " vs.oid = :oid ", "oid");
                    sql += getAndClearPredicateMap(whereIncluded ? " and " : " where ", "", Operator.AND);
                }
                logger.info("ByValueSetOid sql=", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                if (baseDTO != null) {
                    setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
                    setExactQueryMapValue(baseDTO, "oid", "oid", namedParameters);
                } else {
                    logger.error("getCallbackNamedParameters: baseDTO is null!");
                }
            }

        }, false);

        registerDML(ByGeneralProperties.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                Map<String, Object> queryMap = baseDTO.getQueryMap();
                logger.info("ByGeneralProperties queryMap=", queryMap);
                boolean whereIncluded = false;
                String sql = "select vsccr.* from value_set_cds_code_rel vsccr"
                        + " join cds_code cc on cc.code_id = vsccr.code_id"
                        + " join cds_code_system cs on cs.code_system_id = cc.code_system_id";
                String text = (String) queryMap.get("text");
                logger.info("ByGeneralProperties text=", text);
                if (!StringUtils.isEmpty(text)) {
                    setWildcardPredicateValue(baseDTO, " lower(cc.display_name) like :text ", "text");
                    setWildcardPredicateValue(baseDTO, " lower(cc.code) like :text ", "text");
                    setWildcardPredicateValue(baseDTO, " lower(cs.oid) like :text ", "text");
                    setWildcardPredicateValue(baseDTO, " lower(cs.name) like :text ", "text");
                    sql += getAndClearPredicateMap(whereIncluded ? " and " : " where " + "( ", " ) ", Operator.OR);
                    whereIncluded = true;
                }
                String valueSetId = (String) queryMap.get("valueSetId");
                logger.info("ByGeneralProperties valueSetId=", valueSetId);
                if (!StringUtils.isEmpty(valueSetId)) {
                    setNonWildcardPredicateValue(baseDTO, " vsccr.value_set_id = :value_set_id ", "valueSetId");
                    sql += getAndClearPredicateMap(whereIncluded ? " and " : " where ", "", Operator.AND);
                }
                logger.info("ByGeneralProperties sql=", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                if (baseDTO != null) {
                    setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
                    setExactQueryMapValue(baseDTO, "value_set_id", "valueSetId", namedParameters);
                } else {
                    logger.error("getCallbackNamedParameters: baseDTO is null!");
                }
            }

        }, false);

    }
}
