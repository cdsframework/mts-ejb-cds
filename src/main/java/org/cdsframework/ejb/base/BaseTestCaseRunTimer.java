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
package org.cdsframework.ejb.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import org.cdsframework.base.BaseTestCaseDTO;
import org.cdsframework.base.BaseTestCaseResultDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.ejb.interfaces.BaseTestCaseRunTimerInterface;
import org.cdsframework.ejb.local.PropertyMGRLocal;
import org.cdsframework.util.AuthenticationUtils;
import org.cdsframework.util.ClassUtils;
import org.cdsframework.util.EJBUtils;
import org.cdsframework.util.LogUtils;

/**
 *
 * @author sdn
 * @param <T>
 * @param <Y>
 */
public abstract class BaseTestCaseRunTimer<T extends BaseTestCaseDTO, Y extends BaseTestCaseResultDTO> implements BaseTestCaseRunTimerInterface {

    protected LogUtils logger = LogUtils.getLogger(BaseTestCaseRunTimer.class);
    private final Map<UUID, Y> queuedMap = Collections.synchronizedMap(new LinkedHashMap<UUID, Y>());
    private final Map<UUID, Future<Y>> processingMap = Collections.synchronizedMap(new LinkedHashMap<UUID, Future<Y>>());
    private final Map<UUID, Y> completedMap = Collections.synchronizedMap(new LinkedHashMap<UUID, Y>());
    private static final int MAX_TEST_RESULT_AGE = 180000;
    private Class<? extends BaseTestCaseDTO> testClass;
    private Class<? extends BaseTestCaseResultDTO> resultClass;
    private Integer maxQueueSize;
    @Resource
    private SessionContext sessionCtx;
    private TimerService timerService = null;

    @PostConstruct
    public void initializeMain() {
        try {
            List<Class> typeArguments = ClassUtils.getTypeArguments(BaseTestCaseRunTimer.class, getClass());
            testClass = typeArguments.get(0);
            resultClass = typeArguments.get(1);
            PropertyMGRLocal propertyMGRLocal = EJBUtils.getPropertyMGRLocal();
            maxQueueSize = propertyMGRLocal.get("TEST_MGR_TEST_SUBMISSION_Q_SIZE", Integer.class);
            logger.debug("Got maxQueueSize: ", maxQueueSize);
            timerService = sessionCtx.getTimerService();
            initialize();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public Class<? extends BaseTestCaseDTO> getTestClass() {
        return testClass;
    }

    public Class<? extends BaseTestCaseResultDTO> getResultClass() {
        return resultClass;
    }

    public SessionContext getSessionCtx() {
        return sessionCtx;
    }

    public TimerService getTimerService() {
        return timerService;
    }

    /**
     * Get any completed test runs based on a submitted list of UUIDs.
     *
     * @param uuids
     * @return the list of done tests.
     */
    public List<Y> getCompletedTests(List<UUID> uuids) {
        final String METHODNAME = "getCompletedTests ";
        List<Y> result = new ArrayList<Y>();
        for (UUID uuid : uuids) {
            if (completedMap.containsKey(uuid)) {
                result.add(completedMap.get(uuid));
                completedMap.remove(uuid);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.info(METHODNAME, "got: ", uuids.size());
            logger.info(METHODNAME, "returning a total # of new done DTOs: ", result.size());
        }
        return result;
    }

    /**
     * Queue a list of tests.
     *
     * @param testIds
     * @return the list of UUIDs that were queued.
     */
    public List<UUID> queueTests(List<String> testIds) {
        final String METHODNAME = "queueTests ";
        List<UUID> result = new LinkedList<UUID>();
        Iterator<String> iterator = testIds.iterator();
        while (iterator.hasNext()) {
            result.add(queueTest(iterator.next()));
        }
        return result;
    }

    /**
     * Queue a single test.
     *
     * @param testId
     * @return the UUID of the test that was queued.
     */
    public UUID queueTest(String testId) {
        final String METHODNAME = "queueTest ";
        try {
            Y resultDTO = (Y) getResultClass().newInstance();
            T testDTO = (T) getTestClass().newInstance();
            testDTO.setTestId(testId);
            resultDTO.setTestDTO(testDTO);
            queuedMap.put(resultDTO.getUuid(), resultDTO);
            return resultDTO.getUuid();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Main Timeout method - first move done tests to completed, queued tests to
     * processing and finally expire any old results.
     */
    @Timeout
    private void processQueue() {
        final String METHODNAME = "processQueue ";
        try {
            if (processingMap.size() > 0) {
                moveDoneToCompletedMap();
            }
            if (queuedMap.size() > 0) {
                queueToProcessingMap();
            }
            if (completedMap.size() > 0) {
                expireOldMapEntries();
            }
        } catch (Exception e) {
            logger.error(METHODNAME, e);
        }
        if (logger.isDebugEnabled()) {
            logger.info(METHODNAME, "queuedMap size: ", queuedMap.size());
            logger.info(METHODNAME, "processingMap size: ", processingMap.size());
            logger.info(METHODNAME, "completedMap size: ", completedMap.size());
        }
    }

    /**
     * Iterate over the processingMap and if the Future object isDone then store
     * the resultDTO on the completedMap.
     */
    private void moveDoneToCompletedMap() {
        final String METHODNAME = "moveDoneToCompletedMap ";
        Iterator<Map.Entry<UUID, Future<Y>>> iterator = processingMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Future<Y>> item = iterator.next();
            if (item.getValue().isDone()) {
                logger.debug(METHODNAME, "found done test: ", item.getKey().toString());
                try {
                    Y iceTestResultDTO = item.getValue().get();
                    iceTestResultDTO.setUuid(item.getKey());
                    completedMap.put(item.getKey(), iceTestResultDTO);
                    logger.debug(METHODNAME, "added to completed: ", item.getKey().toString());
                } catch (Exception e) {
                    logger.error(METHODNAME, "error processing: ", item.getKey().toString());
                    logger.error(e);
                    completedMap.put(item.getKey(), getEmptyIceTestResultDTO(item.getKey()));
                }
                iterator.remove();
            }
        }
    }

    /**
     * Retrieve queued tests and submit them to OpenCDS and store the async call
     * future object in the processingMap.
     */
    private void queueToProcessingMap() {
        final String METHODNAME = "queueToProcessingMap ";
        logger.debug(METHODNAME, "called.");
        int itemsProcessing = processingMap.size();
        if (itemsProcessing < maxQueueSize) {
            int queuedCount = 0;
            Iterator<Map.Entry<UUID, Y>> iterator = queuedMap.entrySet().iterator();
            while (((queuedCount + itemsProcessing) < maxQueueSize) && iterator.hasNext()) {
                Map.Entry<UUID, Y> item = iterator.next();
                PropertyBagDTO propertyBagDTO = new PropertyBagDTO();
                propertyBagDTO.put("trimResults", true);
                propertyBagDTO.put("testId", item.getValue().getTestDTO().getTestId());
                try {
                    BaseTestCaseResultBO testResultBO = (BaseTestCaseResultBO) EJBUtils.getDtoBo(getResultClass());
                    Future<Y> result = testResultBO.runTest(AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
                    processingMap.put(item.getKey(), result);
                } catch (Exception e) {
                    logger.error(METHODNAME, "error processing: ", item.getKey().toString());
                    logger.error(e);
                    completedMap.put(item.getKey(), getEmptyIceTestResultDTO(item.getKey()));
                }
                queuedCount++;
                iterator.remove();
            }
        }
    }

    /**
     * Returns an empty IceTestResultDTO for when there is an exception.
     *
     * @param uuid
     * @return
     */
    private Y getEmptyIceTestResultDTO(UUID uuid) {
        try {
            Y result = (Y) getResultClass().newInstance();
            result.setUuid(uuid);
            result.setTestDTO(getTestClass().newInstance());
            result.setLastModDatetime(new Date());
            return result;
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Expire any test resultDTO older than MAX_TEST_RESULT_AGE
     */
    private void expireOldMapEntries() {
        final String METHODNAME = "expireOldMapEntries ";
        Iterator<Map.Entry<UUID, Y>> iterator = completedMap.entrySet().iterator();
        long now = (new Date()).getTime();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Y> item = iterator.next();
            if (item != null && item.getValue() != null && item.getValue().getLastModDatetime() != null) {
                if (logger.isDebugEnabled()) {
                    logger.info(METHODNAME, "item age: ", (now - item.getValue().getLastModDatetime().getTime()));
                }
                if ((now - item.getValue().getLastModDatetime().getTime()) > (MAX_TEST_RESULT_AGE)) {
                    iterator.remove();
                }
            }
        }
    }

    public void cancelTests(List<UUID> uuids) {
        final String METHODNAME = "cancelTests ";
        logger.info(METHODNAME, "attempting to cancel: ", uuids);
        for (UUID uuid : uuids) {
            Iterator<Map.Entry<UUID, Y>> queuedMapIterator = queuedMap.entrySet().iterator();
            while (queuedMapIterator.hasNext()) {
                Map.Entry<UUID, Y> item = queuedMapIterator.next();
                if (uuid.equals(item.getKey())) {
                    logger.info(METHODNAME, "canceling queuedMap: ", uuid.toString());
                    queuedMapIterator.remove();
                }
            }
            Iterator<Map.Entry<UUID, Y>> completedMapIterator = completedMap.entrySet().iterator();
            while (completedMapIterator.hasNext()) {
                Map.Entry<UUID, Y> item = completedMapIterator.next();
                if (uuid.equals(item.getKey())) {
                    logger.info(METHODNAME, "canceling completedMap: ", uuid.toString());
                    completedMapIterator.remove();
                }
            }
            Iterator<Map.Entry<UUID, Future<Y>>> processingMapIterator = processingMap.entrySet().iterator();
            while (processingMapIterator.hasNext()) {
                Map.Entry<UUID, Future<Y>> item = processingMapIterator.next();
                if (uuid.equals(item.getKey())) {
                    logger.info(METHODNAME, "canceling processingMap: ", uuid.toString());
                    processingMapIterator.remove();
                }
            }
        }
    }
}
