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
import org.apache.commons.lang3.StringUtils;
import org.cdsframework.base.BaseDAO;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.callback.QueryCallback;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.CdsListItemDTO;
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
public class CdsListItemDAO extends BaseDAO<CdsListItemDTO> {

    @Override
    protected void initialize() throws MtsException {

        this.registerDML(ByGeneralProperties.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String sql = getSelectDML();
                String listCode = (String) baseDTO.getQueryMap().get("listCode");
                logger.info("getQueryDML ByGeneralProperties listCode=", listCode);
                String versionId = (String) baseDTO.getQueryMap().get("versionId");
                logger.info("getQueryDML ByGeneralProperties versionId=", versionId);
                boolean dirty = false;
                if (!StringUtils.isEmpty(listCode)) {
                    setNonWildcardPredicateValue(baseDTO, " list_code = :list_code ", "listCode");
                    dirty = true;
                }
                if (!StringUtils.isEmpty(versionId)) {
                    setNonWildcardPredicateValue(baseDTO, " version_id = :version_id ", "versionId");
                    dirty = true;
                }
                if (dirty) {
                    sql += getAndClearPredicateMap(" where ", "", Operator.AND);
                }

                setWildcardPredicateValue(baseDTO, " lower(ad_hoc_id) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(ad_hoc_label) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(concept_code) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(concept_code_display_name) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(code) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(code_display_name) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(vset_code) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(vset_code_display_name) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(cbased_code) like :text ", "text");
                setWildcardPredicateValue(baseDTO, " lower(cbased_code_display_name) like :text ", "text");

                if (dirty) {
                    sql += getAndClearPredicateMap(" and ( ", " ) ", Operator.OR);
                } else {
                    sql += getAndClearPredicateMap(" where ", "", Operator.OR);
                }

                logger.info("getQueryDML ByGeneralProperties ", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
                    throws MtsException {
                setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
                setExactQueryMapValue(baseDTO, "list_code", "listCode", namedParameters);
                setExactQueryMapValue(baseDTO, "version_id", "versionId", namedParameters);
            }

        }, false);

        this.registerDML(CdsListItemDTO.ByValueSetCdsCodeRelId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list_item where value_set_cds_code_rel_id = :value_set_cds_code_rel_id";
            }
        }, false);

        this.registerDML(CdsListItemDTO.ByValueSetCdsCodeRelIdCdsListId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list_item where value_set_cds_code_rel_id = :value_set_cds_code_rel_id and list_id = :list_id";
            }
        }, false);

        this.registerDML(CdsListItemDTO.ByCdsCodeId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list_item where code_id = :code_id";
            }
        }, false);

        this.registerDML(CdsListItemDTO.ByOpenCdsCodeId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list_item where opencds_code_id = :opencds_code_id";
            }
        }, false);

        this.registerDML(CdsListItemDTO.ByOpenCdsConceptRelId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list_item where opencds_concept_rel_id = :opencds_concept_rel_id";
            }
        }, false);

        this.registerDML(CdsListItemDTO.ByOpenCdsCodeIdCdsListId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list_item where opencds_code_id = :opencds_code_id and list_id = :list_id";
            }
        }, false);

        this.registerDML(CdsListItemDTO.ByCdsCodeIdCdsListId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from cds_list_item where code_id = :code_id and list_id = :list_id";
            }
        }, false);

    }

}
