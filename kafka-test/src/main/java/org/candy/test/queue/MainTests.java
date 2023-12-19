package org.candy.test.queue;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.acl.*;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourcePattern;
import org.apache.kafka.common.resource.ResourcePatternFilter;
import org.apache.kafka.common.resource.ResourceType;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * MainTests
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/7/12
 */
public class MainTests {

    public static void main(String[] args) {

        KafkaSettings settings = new KafkaSettings();

        String user = "user1";
        String password = "123456";
        String topic = "root." + user;

        try (AdminClient client = AdminClient.create(settings.toAdminProps())) {
            NewTopic newTopic = new NewTopic(topic, Optional.empty(), Optional.empty());

            createUser(client, user, password);

            aclBinding(client, topic, user);

//            describeAccount(client, user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void createUser(AdminClient client, String user, String password) {
        ScramCredentialInfo scramCredentialInfo = new ScramCredentialInfo(ScramMechanism.SCRAM_SHA_512, 8192);
        UserScramCredentialAlteration userScramCredentialAlteration =
                new UserScramCredentialUpsertion(user, scramCredentialInfo, password);
        client.alterUserScramCredentials(List.of(userScramCredentialAlteration));
    }

    public static void describeAccount(AdminClient client, String user){
        try{
            //构造kaf_java_int的资源对象。这里ResourceType.ANY改为ResourceType.GROUP那么就只能输出kaf_java_int账号相关的Group ID信息。
            ResourcePatternFilter resourcePatternFilter = new ResourcePatternFilter(ResourceType.ANY, user, PatternType.ANY);
            //绑定查询权限
            AclBindingFilter aclBindingFilter=new AclBindingFilter(resourcePatternFilter,AccessControlEntryFilter.ANY);
            //查询
            DescribeAclsResult result = client.describeAcls(aclBindingFilter);
            Collection<AclBinding> gets = result.values().get();
            for (AclBinding get : gets) {
                System.out.println(get.pattern().name()); //输出当前Topic名
                System.out.println(get.pattern().patternType());//输出当前写入模式
                System.out.println(get.pattern().resourceType());//输出当前资源类型
                System.out.println(get.entry().principal());//输出当前账户名
                System.out.println(get.entry().permissionType());//输出允许类型
                System.out.println(get.entry().operation());//输出操作
                System.out.println("-------------------------");
            }
            System.out.println();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void aclBinding(AdminClient client, String topic, String user) {
        ResourcePattern resourcePattern = new ResourcePattern(ResourceType.TOPIC, topic, PatternType.LITERAL);
        AccessControlEntry accessControlEntry = new AccessControlEntry("User:" + user, "*",
                AclOperation.READ, AclPermissionType.ALLOW);
        AclBinding aclBinding = new AclBinding(resourcePattern, accessControlEntry);
        client.createAcls(List.of(aclBinding));
    }
}
