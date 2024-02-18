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
package org.candy.test.es.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * CallRecord
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/12/22
 */
@ToString
@Getter
@Setter
@Document(indexName = "call_record")
@Setting(shards = 5)
public class CallRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = -4692583727414687456L;

    @Id
    @Field(name = "id", type = FieldType.Text)
    private String id;

    @Field(name = "tenant_id", type = FieldType.Keyword)
    private UUID tenantId;

    @Field(name = "biz_code", type = FieldType.Text)
    private String bizCode;

    @Field(name = "call_path", type = FieldType.Text)
    private String callPath;

    @Field(name = "call_time", type = FieldType.Long)
    private long callTime;

    @Field(name = "status", type = FieldType.Text)
    private String status;

    @Field(name = "args", type = FieldType.Text)
    private String args;

    @Field(name = "results", type = FieldType.Text)
    private String results;
}
