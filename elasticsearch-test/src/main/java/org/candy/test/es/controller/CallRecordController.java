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
package org.candy.test.es.controller;

import org.candy.test.es.service.CallRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * CallRecordController
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/12/22
 */
@RestController
public class CallRecordController {

    @Autowired
    private CallRecordService recordService;

    @GetMapping("/count")
    public Object count() {
        return recordService.countTotal();
    }

    @GetMapping("/query")
    public Object query(@RequestParam("tenantId") String strTenantId,
                        @RequestParam("bizCode") String bizCode,
                        @RequestParam("page") int page,
                        @RequestParam("size") int size) {
        return recordService.find(UUID.fromString(strTenantId), bizCode, PageRequest.of(page, size));
    }
}
