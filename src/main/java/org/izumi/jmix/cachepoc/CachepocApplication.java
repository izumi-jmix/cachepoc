package org.izumi.jmix.cachepoc;

import com.google.common.base.Strings;
import io.jmix.core.impl.scanning.AnnotationScanMetadataReaderFactory;
import io.jmix.ui.sys.ActionsConfiguration;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@ComponentScan(basePackages = {"ru.gpt", "org.izumi.jmix"})
@SpringBootApplication
public class CachepocApplication {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(CachepocApplication.class, args);
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource.hikari")
    DataSource dataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @EventListener
    public void printApplicationUrl(ApplicationStartedEvent event) {
        LoggerFactory.getLogger(CachepocApplication.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }

    @Bean("UiActions")
    public ActionsConfiguration actions(final ApplicationContext applicationContext,
                                        final AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        final var actionsConfiguration = new ActionsConfiguration(applicationContext, metadataReaderFactory);
        actionsConfiguration.setBasePackages(List.of("ru.gpt", "org.izumi.jmix"));
        return actionsConfiguration;
    }
}
