package angelina.quartz.service.config.quartz;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Angelina
 */
@Configuration
@Slf4j
public class QuartzConfig {

    @Value("${quartz.org.quartz.scheduler.instanceName}")
    private String instanceName;

    @Value("${quartz.org.quartz.scheduler.instanceId}")
    private String instanceId;

    @Value("${quartz.org.quartz.scheduler.skipUpdateCheck}")
    private String skipUpdateCheck;

    @Value("${quartz.org.quartz.scheduler.jobFactory.class}")
    private String jobFactoryClass;

    @Value("${quartz.org.quartz.jobStore.class}")
    private String jobStoreClass;

    @Value("${quartz.org.quartz.jobStore.driverDelegateClass}")
    private String jobStoreDriverDelegateClass;

    @Value("${quartz.org.quartz.jobStore.tablePrefix}")
    private String jobStoreTablePrefix;

    @Value("${quartz.org.quartz.jobStore.isClustered}")
    private String jobStoreIsClustered;

    @Value("${quartz.org.quartz.jobStore.clusterCheckinInterval}")
    private String clusterCheckinInterval;

    @Value("${quartz.org.quartz.threadPool.threadCount}")
    private String threadCount;

    @Value("${quartz.org.quartz.threadPool.class}")
    private String threadPoolClass;



    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {

        QuartzSpringBeanConfig jobFactory = new QuartzSpringBeanConfig();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean("quartzDataSource")
    @ConfigurationProperties("dataSource.quartz")
    public HikariDataSource quartzDataSource() {
        return (HikariDataSource)DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("quartzDataSource") DataSource quartzDataSource,
            JobFactory jobFactory) throws IOException {

        log.info("Start Schedule By SchedulerFactoryBean");
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(quartzDataSource);
        factory.setJobFactory(jobFactory);

        Properties properties = quartzProperties();
        properties.put("org.quartz.scheduler.instanceName", instanceName);
        properties.put("org.quartz.scheduler.instanceId", instanceId);
        properties.put("org.quartz.scheduler.skipUpdateCheck", skipUpdateCheck);
        properties.put("org.quartz.scheduler.jobFactory.class", jobFactoryClass);
        properties.put("org.quartz.jobStore.class", jobStoreClass);
        properties.put("org.quartz.jobStore.driverDelegateClass", jobStoreDriverDelegateClass);
        properties.put("quartz.jobStore.tablePrefix", jobStoreTablePrefix);
        properties.put("org.quartz.jobStore.isClustered", jobStoreIsClustered);
        properties.put("quartz.org.quartz.jobStore.clusterCheckinInterval", clusterCheckinInterval);
        properties.put("org.quartz.threadPool.threadCount", threadCount);
        properties.put("org.quartz.threadPool.class", threadPoolClass);
        factory.setQuartzProperties(properties);

        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {

        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}
