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
package org.cdsframework.util;

import org.cdsframework.dto.CdsVersionDTO;
import org.cdsframework.exceptions.MtsException;

/**
 *
 * @author HLN Consulting, LLC
 */
public class VersionUtils {

    final static private LogUtils logger = LogUtils.getLogger(VersionUtils.class);

    /**
     * Get the next string version after the supplied version.
     *
     * @param lastCdsVersionDTO
     * @return
     * @throws MtsException
     */
    public static String getNextVersionNumber(CdsVersionDTO lastCdsVersionDTO) throws MtsException {
        final String METHODNAME = "getNextVersionNumber ";
        String result = null;
        if (lastCdsVersionDTO != null) {
            String version = lastCdsVersionDTO.getVersion();
            if (version != null) {
                logger.info(METHODNAME, "got version: ", version);
                String[] split = version.split("\\.");
                if (split.length > 1) {
                    split[split.length - 1] = "" + (Integer.valueOf(split[split.length - 1]) + 1);
                    result = "";
                    for (String item : split) {
                        result += item + ".";
                    }
                    result = result.substring(0, result.length() - 1);
                    logger.info(METHODNAME, "next version: ", result);
                } else {
                    throw new MtsException("Couldn't get next version number for: " + version);
                }
            }
        }
        return result;
    }

//
//    public CdsVersionDTO getNextCdsVersionDTO(String businessId, String scopingEntityId) throws MtsException {
//        final String METHODNAME = "getNextCdsVersionDTO ";
//        CdsVersionDTO result = null;
//        if (businessId != null && scopingEntityId != null) {
//            for (CdsBusinessScopeDTO item : getAll()) {
//                if (businessId.equals(item.getBusinessId()) && scopingEntityId.equals(item.getScopingEntityId())) {
//                    result = getNextVersion(item);
//                    break;
//                }
//            }
//        } else {
//            logger.error(METHODNAME, "businessId or scopingEntityId or version was null!");
//        }
//        if (result == null) {
//            logger.error(METHODNAME, "result was null for: ", businessId, " - ", scopingEntityId);
//        }
//        return result;
//    }
//
//    private CdsVersionDTO getNextVersion(CdsBusinessScopeDTO cdsBusinessScopeDTO) throws MtsException {
//        final String METHODNAME = "getNextVersion ";
//        CdsVersionDTO result = new CdsVersionDTO();
//        if (cdsBusinessScopeDTO.getCdsVersionDTOs().isEmpty()) {
//            throw new MtsException("cdsBusinessScopeDTO didn't have any versions: " + cdsBusinessScopeDTO);
//        }
//
//        List<CdsVersionDTO> cdsVersionDTOs = cdsBusinessScopeDTO.getCdsVersionDTOs();
//        Collections.sort(cdsVersionDTOs, new CdsVersionComparator());
//        CdsVersionDTO lastCdsVersionDTO = cdsVersionDTOs.get(cdsVersionDTOs.size() - 1);
//        String nextVersion = getNextVersionNumber(lastCdsVersionDTO);
//        logger.info(METHODNAME, "nextVersion: ", nextVersion);
//
//        result.setCdsBusinessScopeDTO(cdsBusinessScopeDTO);
//        result.setVersion(nextVersion);
//        result.setStatus(Status.ACTIVE);
//        result.setCdsBusinessScopeDTO(cdsBusinessScopeDTO);
//        result.setName(lastCdsVersionDTO.getName());
//        result.setDescription(lastCdsVersionDTO.getDescription());
//
//        return result;
//    }
}
