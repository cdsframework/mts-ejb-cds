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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.ValueSetCdsCodeRelDTO;
import org.cdsframework.dto.ValueSetDTO;

/**
 *
 * @author sdn
 */
public class PhinVadsUtils {

    private static final LogUtils logger = LogUtils.getLogger(PhinVadsUtils.class);
    private static final String PHIN_VADS_HEADER_FILENAME = "DownloadHeader.txt";
    private static final String PHIN_VADS_FILENAME_FRAGMENT = "PHVS";
    private static final String PHIN_VADS_FILE_START = "Value Set Name";
    private static final String PHIN_VADS_RECORD_START = "Concept Code";

    public static boolean isFileMapPhinVads(Map<String, String> fileMap) {
        boolean result = false;
        if (fileMap.containsKey(PHIN_VADS_HEADER_FILENAME)) {
            result = true;
            fileMap.remove(PHIN_VADS_HEADER_FILENAME);
        }
        return result;
    }

    public static boolean isFilePhinVads(String fileName, String fileData) {
        boolean result = false;
        if (fileName.contains(PHIN_VADS_FILENAME_FRAGMENT) || fileData.startsWith(PHIN_VADS_FILE_START)) {
            result = true;
        }
        return result;
    }

    public static ValueSetDTO getValueSetDTOFromPhinVadsExport(String fileData) {
        final String METHODNAME = "getValueSetDTOFromPhinVadsExport ";
        ValueSetDTO valueSetDTO = null;
        if (fileData != null) {
            InputStream inputStream = new ByteArrayInputStream(fileData.getBytes());
            BufferedReader bufferedReader = null;
            String lineBuffer;
            try {

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                // first line inputStream header info - skip
                lineBuffer = bufferedReader.readLine();
                logger.debug(METHODNAME, "skipping first line: ", lineBuffer);

                // value set meta data
                lineBuffer = bufferedReader.readLine();

                String[] valueSetInfoArray = lineBuffer.split("\t");
                valueSetDTO = new ValueSetDTO();
                if (valueSetInfoArray.length >= 1) {
                    valueSetDTO.setName(valueSetInfoArray[0]);
                }
                if (valueSetInfoArray.length >= 2) {
                    valueSetDTO.setCode(valueSetInfoArray[1]);
                }
                if (valueSetInfoArray.length >= 3) {
                    valueSetDTO.setOid(valueSetInfoArray[2]);
                }
                if (valueSetInfoArray.length >= 4) {
                    valueSetDTO.setVersion(valueSetInfoArray[3]);
                }
                if (valueSetInfoArray.length >= 5) {
                    valueSetDTO.setDescription(valueSetInfoArray[4]);
                }
                if (valueSetInfoArray.length >= 6) {
                    valueSetDTO.setVersionStatus(valueSetInfoArray[5]);
                }
                if (valueSetInfoArray.length >= 8) {
                    valueSetDTO.setVersionDescription(valueSetInfoArray[7]);
                }
                if (logger.isDebugEnabled()) {
                    logger.info(METHODNAME, "PHIN VADS Value Set: ", valueSetDTO.getName());
                    logger.info(METHODNAME, "    Code: ", valueSetDTO.getCode());
                    logger.info(METHODNAME, "    OID: ", valueSetDTO.getOid());
                    logger.info(METHODNAME, "    Version: ", valueSetDTO.getVersion());
                    logger.info(METHODNAME, "    Description: ", valueSetDTO.getDescription());
                    logger.info(METHODNAME, "    Version Status: ", valueSetDTO.getVersionStatus());
                }

                if (valueSetDTO.getName() != null
                        && valueSetDTO.getCode() != null
                        && valueSetDTO.getOid() != null) {

                    while ((lineBuffer = bufferedReader.readLine()) != null) {
                        String[] recordArray = lineBuffer.split("\t");
                        if (recordArray.length == 9) {

                            if (!recordArray[0].startsWith(PHIN_VADS_RECORD_START)) {
                                ValueSetCdsCodeRelDTO valueSetCdsCodeRelDTO = new ValueSetCdsCodeRelDTO();
                                CdsCodeDTO cdsCodeDTO = new CdsCodeDTO();
                                valueSetCdsCodeRelDTO.setCdsCodeDTO(cdsCodeDTO);
                                valueSetDTO.addOrUpdateChildDTO(valueSetCdsCodeRelDTO);
                                cdsCodeDTO.setCode(recordArray[0]);
                                cdsCodeDTO.setDisplayName(recordArray[1]);
                                cdsCodeDTO.setCodeSystem(recordArray[4]);
                                cdsCodeDTO.setCodeSystemName(recordArray[5]);

                                if (logger.isDebugEnabled()) {
                                    logger.info(METHODNAME, "Record Array: ", StringUtils.getStringFromArray(", ", recordArray));
                                }
                            }
                        }
                    }
                } else {
                    valueSetDTO = null;
                }
            } catch (IOException e) {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException o) {
                        // no hacer nada
                    }
                }
            }
        } else {
            logger.error(METHODNAME, "fileData is null!");
        }
        return valueSetDTO;
    }
}
