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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.ejb.Stateless;
import org.cdsframework.base.BaseDAO;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.callback.QueryCallback;
import org.cdsframework.dto.CriteriaDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
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
public class CriteriaDAO extends BaseDAO<CriteriaDTO> {

    @Override
    protected void initialize() throws MtsException {

        // General search query
        this.registerDML(ByGeneralProperties.class, new QueryCallback(getDtoTableName()) {
            @Override
            public String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String sql = getSelectDML();
                String text = (String) baseDTO.getQueryMap().get("text");
                String criteriaTypeFilters = (String) baseDTO.getQueryMap().get("criteriaTypeFilters");
                logger.info("ByGeneralProperties text: ", text);
                logger.info("ByGeneralProperties criteriaTypeFilters: ", criteriaTypeFilters);

                if (!StringUtils.isEmpty(text)) {
                    setWildcardPredicateValue(baseDTO,
                            " ( lower(name) like :text OR lower(description) like :text OR lower(criteria_type) like :text OR lower(criteria_id) like :text ) ", "text");
                }

                if (!StringUtils.isEmpty(criteriaTypeFilters)) {
                    Map<String, Boolean> filterMap = getFilterMap(criteriaTypeFilters);
                    logger.info("ByGeneralProperties filterMap: ", filterMap);
                    int c = 0;
                    for (Entry<String, Boolean> filter : filterMap.entrySet()) {
                        String filterParam = "filter" + c;
                        baseDTO.getQueryMap().put(filterParam, filter.getKey());
                        if (filter.getValue()) {
                            setNonWildcardPredicateValue(baseDTO, " criteria_type != :" + filterParam, filterParam);
                        } else {
                            setNonWildcardPredicateValue(baseDTO, " criteria_type = :" + filterParam, filterParam);
                        }
                        c++;
                    }
                }

                sql += getAndClearPredicateMap(" WHERE ", "", Operator.AND);
                logger.info(sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
                Map<String, Boolean> filterMap = getFilterMap((String) baseDTO.getQueryMap().get("criteriaTypeFilters"));
                int c = 0;
                for (String filter : filterMap.keySet()) {
                    String filterParam = "filter" + c;
                    c++;
                    setExactQueryMapValue(baseDTO, filterParam, filterParam, namedParameters);
                }
            }

        }, false);
    }

    private static Map<String, Boolean> getFilterMap(String criteriaTypeFilters) {
        Map<String, Boolean> filterMap = new HashMap<String, Boolean>();
        if (criteriaTypeFilters != null) {
            String[] criteriaFilterArray = criteriaTypeFilters.split("\\|");
            for (String filter : criteriaFilterArray) {
                if (!StringUtils.isEmpty(filter)) {
                    filter = filter.trim();
                    if (filter.startsWith("!")) {
                        filterMap.put(filter.substring(1).trim(), Boolean.TRUE);
                    } else {
                        filterMap.put(filter, Boolean.FALSE);
                    }
                }
            }
        }
        return filterMap;
    }
}
