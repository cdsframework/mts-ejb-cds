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
import org.cdsframework.dto.CriteriaPredicateDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.enumeration.DatabaseType;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.group.Add;
import org.cdsframework.group.Update;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class CriteriaPredicateDAO extends BaseDAO<CriteriaPredicateDTO> {

    @Override
    protected void initialize() throws MtsException {

        this.registerDML(CriteriaPredicateDTO.MaxOrderByCriteriaId.class, new QueryCallback<CriteriaPredicateDTO>(getDtoTableName()) {
            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select max(predicate_order) + 1 from criteria_predicate where criteria_id = :criteria_id group by predicate_order";
            }
        }, false);

        this.registerDML(CriteriaPredicateDTO.MaxOrderByPredicateId.class, new QueryCallback<CriteriaPredicateDTO>(getDtoTableName()) {
            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select max(predicate_order) + 1 from criteria_predicate where parent_predicate_id = :parent_predicate_id group by predicate_order";
            }
        }, false);

        this.registerDML(CriteriaPredicateDTO.ByCriteriaId.class, new QueryCallback<CriteriaPredicateDTO>(getDtoTableName()) {
            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from criteria_predicate where criteria_id = :criteria_id and parent_predicate_id is null";
            }
        }, false);
//
//        this.registerDML(CriteriaPredicateDTO.ByParentPredicateId.class, new QueryCallback<CriteriaPredicateDTO>(getDtoTableName()) {
//            @Override
//            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
//                return "select * from criteria_predicate where parent_predicate_id = :parent_predicate_id";
//            }
//
//            @Override
//            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
//                if (baseDTO != null && namedParameters != null) {
//                    if (baseDTO instanceof CriteriaPredicateDTO) {
//                        logger.debug("CriteriaPredicateDTO.ByParentPredicateId getCallbackNamedParameters called!");
//                        CriteriaPredicateDTO criteriaPredicateDTO = (CriteriaPredicateDTO) baseDTO;
//                        namedParameters.addValue("parent_predicate_id", criteriaPredicateDTO.getPredicateId());
//                        logger.debug("SET PARENT PREDICATE ID: ", namedParameters.getValues().get("parent_predicate_id"));
//                    }
//                }
//            }
//
//        }, false);

    }

    @Override
    protected void postProcessNamedParameters(DatabaseType databaseType, MapSqlParameterSource namedParameters, BaseDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
        // have to do this because of the circular reference in the dto - the named parameters don't get set right.
        if (baseDTO != null && namedParameters != null) {
            if (baseDTO instanceof CriteriaPredicateDTO) {
                logger.debug("postProcessNamedParameters called! ", baseDTO);
                CriteriaPredicateDTO criteriaPredicateDTO = (CriteriaPredicateDTO) baseDTO;
                if (queryClass == CriteriaPredicateDTO.ByParentPredicateId.class) {
                    namedParameters.addValue("parent_predicate_id", criteriaPredicateDTO.getPredicateId());
                } else if (queryClass == Add.class || queryClass == Update.class) {
                    namedParameters.addValue("parent_predicate_id", criteriaPredicateDTO.getParentPredicateId());
                }
            }
        }
    }
}
