package angelina.quartz.service.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket testApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Angelina-quartz-service")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .enable(false)
                .select()
                .paths(or(regex("/api/.*")))
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * 组件接口说明信息
     * @return Api接口信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfo("Angelina 哼哼哈嘿","我就是写着练练手",
                "1.0.0","","","","");
    }
}
