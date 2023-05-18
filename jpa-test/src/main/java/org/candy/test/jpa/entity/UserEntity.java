package org.candy.test.jpa.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import org.candy.test.jpa.common.Gender;
import org.candy.test.jpa.entity.base.AbstractEntity;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.JsonJdbcType;

import java.io.Serial;
import java.util.UUID;

/**
 * UserEntity
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/10
 */
@Table(name = "c_user")
@Entity
public class UserEntity extends AbstractEntity<UUID> {
    @Serial
    private static final long serialVersionUID = 7228243748563940433L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;
    @Column(name = "created_time")
    private long createdTime;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @JdbcType(JsonJdbcType.class)
    @Column(name = "additional_info")
    private JsonNode additionalInfo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public JsonNode getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(JsonNode additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public static UserEntity ofExample() {
        UserEntity entity = new UserEntity();
        entity.id = UUID.randomUUID();
        entity.createdTime = System.currentTimeMillis();
        entity.name = "rose";
        entity.age = 16;
        entity.gender = Gender.W;
        entity.country = "河南";
        entity.city = "郑州";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            entity.additionalInfo = objectMapper.readValue("{\"enabled\":true}", ObjectNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }
}
