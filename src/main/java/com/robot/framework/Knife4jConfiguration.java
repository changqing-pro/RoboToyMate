package com.robot.framework;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.collect.Lists;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.PropertySpecificationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@EnableSwagger2
@Configuration
public class Knife4jConfiguration implements ModelPropertyBuilderPlugin {


    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 枚举字段可以使用null、空字符串、非匹配的任意字符串进行传参，而不引发异常
     */
    @PostConstruct
    public void myObjectMapper() {
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }


    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
        apiKeyList.add(new ApiKey("Authorization", "token", In.HEADER.toValue()));
        return apiKeyList;
    }

    public ApiInfo sysApiInfo() {
        return new ApiInfoBuilder()
                .title("API")
                .description("API")
                .contact(new Contact("API", "", ""))
                .version("1.0")
                .build();
    }



    @Bean
    public Docket createSysApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(sysApiInfo())
                .groupName("API")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.robot.controller"))
                .paths(PathSelectors.any())
                .build()
                /* 设置安全模式，swagger可以设置访问token */
                .securitySchemes(securitySchemes());
    }

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }

    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }




    @Override
    public void apply(ModelPropertyContext context) {

        Optional<BeanPropertyDefinition> beanPropertyDefinition = context.getBeanPropertyDefinition();
        PropertySpecificationBuilder specificationBuilder = context.getSpecificationBuilder();
        Class<?> rawType = beanPropertyDefinition.get().getField().getRawType();
        List<String> resultList = Lists.newArrayList();
        if (Enum.class.isAssignableFrom(rawType)) {
            try {
                Object[] enumConstants = rawType.getEnumConstants();
                for (Object enumConstant : enumConstants) {
                    Field descField = ReflectionUtils.findField(rawType, "desc");
                    descField.setAccessible(true);
                    String descStr = (String) descField.get(enumConstant);
                    Field codeField = ReflectionUtils.findField(rawType, "code");
                    codeField.setAccessible(true);
                    Integer codeStr = (Integer) codeField.get(enumConstant);
                    resultList.add(codeStr + ":" + descStr);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
//        specificationBuilder.type(new ModelSpecificationBuilder().scalarModel(ScalarType.INTEGER).build());
//        AllowableListValues allowableListValues = new AllowableListValues(Lists.newArrayList(), "Integer");
//        specificationBuilder.enumerationFacet(builder -> builder.allowedValues(allowableListValues));
        specificationBuilder.description(StrUtil.join(" , ", resultList));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

}

