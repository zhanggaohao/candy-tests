/*
 * Copyright [yyyy] [name of copyright owner]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.candy.test.es;

import lombok.extern.slf4j.Slf4j;
import org.candy.test.es.dao.entity.CallRecord;
import org.candy.test.es.service.CallRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * SimulateCall
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/12/22
 */
@Slf4j
//@Component
public class SimulateCall implements ApplicationListener<ApplicationReadyEvent> {

    private final static UUID[] tenantIds = {
            UUID.fromString("8abd639b-ff64-4c40-b5ec-d762951428ab"),
            UUID.fromString("664fa221-7e1e-4074-8b9c-fb265cb44f98"),
            UUID.fromString("5ce9ec4b-a50a-4834-b27e-ff0776df9ce9"),
            UUID.fromString("fd51b94e-f7d4-44c6-bf99-2ad5e031b6ec"),
            UUID.fromString("20fe05a4-cd83-45db-9c4a-3694c1883d90")
    };
    private final static String[] bizCode = {"tyfPd", "VknFZ", "FYkWK", "pzKzf", "pdEzf", "baQzf", "aDePf", "PabDe", "AbaDc", "DdKdg"};

    private final Random random = new SecureRandom();

    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private CallRecordService recordService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        service.scheduleAtFixedRate(this::call, 1, 333, TimeUnit.MILLISECONDS);
    }

    private UUID getTenantId() {
        return tenantIds[random.nextInt(5)];
    }

    private String getBizCode() {
        return bizCode[random.nextInt(10)];
    }

    private void call() {
        System.out.print("call: ");
        try {
            CallRecord record = new CallRecord();
            record.setTenantId(getTenantId());
            record.setBizCode(getBizCode());
            record.setCallPath("/api/test");
            record.setCallTime(System.currentTimeMillis());
            record.setArgs(null);
            record.setStatus("200");
            record.setResults(null);
            System.out.println(recordService.record(record));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
