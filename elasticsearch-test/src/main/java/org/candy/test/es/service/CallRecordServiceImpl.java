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
package org.candy.test.es.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import org.candy.test.es.dao.CallRecordRepository;
import org.candy.test.es.dao.entity.CallRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.data.elasticsearch.core.SearchHitSupport.unwrapSearchHits;

/**
 * CallRecordServiceImpl
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/12/22
 */
@Service
public class CallRecordServiceImpl implements CallRecordService {

    private final ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private CallRecordRepository repository;

    public CallRecordServiceImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public CallRecord record(CallRecord record) {
        return elasticsearchOperations.save(record);
    }

    @Override
    public long countTotal() {
        return elasticsearchOperations.count(Query.findAll(), CallRecord.class);
    }

    @Override
    public Page<CallRecord> find(UUID tenantId, String bizCode, Pageable pageable) {
        Criteria criteria = Criteria.where("tenant_id").is(tenantId).and("error").notEmpty()
                .and("biz_code").contains(bizCode);
        Query query = new CriteriaQuery(criteria, pageable);
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q
                                .bool(b -> b
                                        .must(q1 -> q1
                                                .queryString(builder -> builder
                                                        .defaultOperator(Operator.And)
                                                        .fields("tenant_id")
                                                        .query(tenantId.toString())))
                                        .must(q2 -> q2
                                                .queryString(builder -> builder
                                                        .analyzeWildcard(true)
                                                        .fields("biz_code")
                                                        .query("*" + bizCode + "*"))))
                        /*.matchAll(my -> my)*/)
                .withSort(Sort.by(Sort.Direction.ASC, "call_time"))
                .build();
        return (Page<CallRecord>) unwrapSearchHits(SearchHitSupport.searchPageFor(elasticsearchOperations.search(query, CallRecord.class), pageable));
    }
}
