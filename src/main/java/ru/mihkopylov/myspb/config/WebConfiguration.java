package ru.mihkopylov.myspb.config;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import ru.mihkopylov.myspb.Const;

@Configuration
@AllArgsConstructor
public class WebConfiguration {
    @Bean
    public WebMvcConfigurer spaConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/**/*.css", "/**/*.html", "/**/*.js", "/**/*.jsx", "/**/*.png",
                        "/**/*.ttf", "/**/*.woff", "/**/*.woff2").addResourceLocations("classpath:/static/");

                registry.addResourceHandler("/", "/**")
                        .addResourceLocations("classpath:/static/index.html")
                        .resourceChain(true)
                        .addResolver(new PathResourceResolver() {
                            @Override
                            protected Resource getResource(@NonNull String resourcePath, @NonNull Resource location) {
                                if (resourcePath.startsWith(Const.REST) || resourcePath.startsWith(
                                        Const.REST.substring(1))) {
                                    return null;
                                }

                                return location.exists() && location.isReadable() ? location : null;
                            }
                        } );
            }
        };
    }
}
